package com.shahaabapps.twotracks.feature.listen

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.PodcastSeries
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/** The episode in the player, as the screens draw it. */
data class NowPlaying(
    val topicId: String,
    val title: String,
    /** Whether it is meant to be playing: true while it buffers, false once paused. */
    val playing: Boolean,
    val position: Double,
    val duration: Double,
    val speed: Float,
    val ended: Boolean = false,
)

/**
 * The app's side of the player: a controller on [PodcastService]'s session,
 * so the screens, the notification and the lock screen all drive one player
 * and can never disagree about what it is doing.
 */
@Singleton
class ListenPlayer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val episodes: Episodes,
    private val place: ListenPlace,
) {
    private val _state = MutableStateFlow<NowPlaying?>(null)
    val state: StateFlow<NowPlaying?> = _state.asStateFlow()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var controller: MediaController? = null
    private var connecting: ListenableFuture<MediaController>? = null
    private var ticker: Job? = null

    private val covers = java.util.concurrent.ConcurrentHashMap<PodcastSeries, ByteArray>()

    private fun cover(series: PodcastSeries): ByteArray = covers.getOrPut(series) {
        context.resources.openRawResource(coverOf(series)).use { it.readBytes() }
    }

    private val listener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            publish()
            ticker?.cancel()
            // The position moves on its own while playing; nothing announces it.
            if (player.isPlaying) ticker = scope.launch {
                while (isActive) {
                    delay(250)
                    publish()
                }
            }
        }
    }

    /** Plays [topicId]: carries on if it is the one loaded, otherwise loads it at the place you left it. */
    fun play(topicId: String, from: Double? = null) = withController { c ->
        val loaded = c.currentMediaItem?.mediaId == topicId && c.playbackState != Player.STATE_IDLE
        if (loaded && from == null) {
            if (c.playbackState == Player.STATE_ENDED) c.seekTo(0)
            c.play()
            return@withController
        }
        if (loaded) {
            c.seekTo((from!! * 1000).toLong())
            c.play()
            return@withController
        }
        val item = mediaItem(topicId) ?: return@withController
        val start = from ?: place.position(topicId).takeIf { episodes.timeline(topicId)?.resumable(it) == true } ?: 0.0
        c.setMediaItem(item, (start * 1000).toLong())
        c.setPlaybackSpeed(place.speed)
        c.prepare()
        c.play()
    }

    fun pause() = withController { it.pause() }

    fun back() = withController { it.seekBack() }

    fun forward() = withController { it.seekForward() }

    fun seekTo(seconds: Double) = withController { it.seekTo((seconds * 1000).toLong()) }

    fun setSpeed(speed: Float) {
        place.speed = ListenSpeeds.nearest(speed)
        withController { it.setPlaybackSpeed(ListenSpeeds.nearest(speed)) }
    }

    /** Ends the session, keeping your place: the same as the notification's stop. */
    fun stop() = withController { it.sendCustomCommand(SessionCommand(PodcastService.STOP, Bundle.EMPTY), Bundle.EMPTY) }

    private fun mediaItem(topicId: String): MediaItem? {
        val podcast = episodes.podcast(topicId) ?: return null
        val (module, topic) = Courses.all.firstNotNullOfOrNull { Courses.topic(it.id, topicId) } ?: return null
        return MediaItem.Builder()
            .setMediaId(topicId)
            .setUri(Episodes.uri(topicId))
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(topic.title)
                    .setArtist(podcast.series.hosts)
                    .setAlbumTitle("${podcast.series.label} · Module ${module.number}")
                    .setArtworkData(cover(podcast.series), MediaMetadata.PICTURE_TYPE_FRONT_COVER)
                    .setMediaType(MediaMetadata.MEDIA_TYPE_PODCAST_EPISODE)
                    .build(),
            )
            .build()
    }

    private fun publish() {
        val c = controller ?: return
        val item = c.currentMediaItem
        if (item == null || c.playbackState == Player.STATE_IDLE) {
            _state.value = null
            return
        }
        val known = episodes.timeline(item.mediaId)?.seconds ?: 0.0
        _state.value = NowPlaying(
            topicId = item.mediaId,
            title = item.mediaMetadata.title?.toString().orEmpty(),
            playing = c.playWhenReady && c.playbackState != Player.STATE_ENDED,
            position = c.currentPosition / 1000.0,
            duration = if (c.duration == C.TIME_UNSET) known else c.duration / 1000.0,
            speed = c.playbackParameters.speed,
            ended = c.playbackState == Player.STATE_ENDED,
        )
    }

    /** Runs [block] on the controller, connecting to the service the first time. */
    private fun withController(block: (MediaController) -> Unit) {
        controller?.let { block(it); return }
        val future = connecting ?: MediaController.Builder(context, SessionToken(context, ComponentName(context, PodcastService::class.java)))
            .buildAsync().also { connecting = it }
        future.addListener({
            val c = runCatching { future.get() }.getOrNull() ?: run { connecting = null; return@addListener }
            if (controller == null) {
                controller = c
                c.addListener(listener)
            }
            block(c)
            publish()
        }, ContextCompat.getMainExecutor(context))
    }
}
