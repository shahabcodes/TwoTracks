package com.shahaabapps.twotracks.feature.listen

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.shahaabapps.core.content.Courses
import com.shahaabapps.twotracks.AppActivity
import com.shahaabapps.twotracks.R
import com.shahaabapps.twotracks.widget.WidgetRoutes

/**
 * Plays an episode, and keeps playing it with the screen off.
 *
 * The media session is what the system draws its player from: the one in
 * the notification shade, on the lock screen, on a watch or in a car. It
 * shows the cover, the topic and Maya & Leo, a scrubber, and these buttons:
 * back 15 seconds, play or pause, forward 30, the speed (tap to step through
 * them) and stop. How that player is drawn is the system's to decide; what
 * goes into it is decided here, and it is the same controls the app's own
 * player has.
 */
@OptIn(UnstableApi::class)
class PodcastService : MediaSessionService() {
    private var session: MediaSession? = null
    private lateinit var place: ListenPlace
    private val main = Handler(Looper.getMainLooper())

    /** Writes the position down every few seconds while playing, so a crash or a reboot loses almost nothing. */
    private val keepPlace = object : Runnable {
        override fun run() {
            savePlace()
            main.postDelayed(this, SAVE_EVERY_MS)
        }
    }

    override fun onCreate() {
        super.onCreate()
        place = ListenPlace(this)
        val player = ExoPlayer.Builder(this)
            // Speech, and audio focus handled: a call or a navigation prompt
            // pauses the episode rather than talking over it.
            .setAudioAttributes(AudioAttributes.Builder().setUsage(C.USAGE_MEDIA).setContentType(C.AUDIO_CONTENT_TYPE_SPEECH).build(), true)
            // Headphones pulled out: stop talking into the room.
            .setHandleAudioBecomingNoisy(true)
            .setWakeMode(C.WAKE_MODE_LOCAL)
            .setSeekBackIncrementMs(BACK_MS)
            .setSeekForwardIncrementMs(FORWARD_MS)
            .build()
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                savePlace()
                main.removeCallbacks(keepPlace)
                if (isPlaying) main.postDelayed(keepPlace, SAVE_EVERY_MS)
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                place.speed = ListenSpeeds.nearest(playbackParameters.speed)
                session?.setMediaButtonPreferences(buttons(playbackParameters.speed))
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                // Finished: the next play starts from the top, not at the last second.
                if (playbackState == Player.STATE_ENDED) player.currentMediaItem?.mediaId?.let(place::clear)
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                mediaItem?.mediaId?.let { id -> session?.setSessionActivity(openPlayer(id)) }
            }
        })
        setMediaNotificationProvider(DefaultMediaNotificationProvider(this).apply { setSmallIcon(R.drawable.ic_stat_podcast) })
        session = MediaSession.Builder(this, player)
            .setCallback(Controls())
            .setMediaButtonPreferences(buttons(player.playbackParameters.speed))
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = session

    /** Swiped out of recents: keep going if it is playing, as a podcast app does, and go quietly if not. */
    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = session?.player
        if (player == null || !player.playWhenReady || player.mediaItemCount == 0 || player.playbackState == Player.STATE_ENDED) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        main.removeCallbacks(keepPlace)
        session?.run {
            savePlace()
            player.release()
            release()
        }
        session = null
        super.onDestroy()
    }

    private fun savePlace() {
        val player = session?.player ?: return
        val id = player.currentMediaItem?.mediaId ?: return
        if (player.playbackState == Player.STATE_ENDED) return
        place.save(id, player.currentPosition / 1000.0)
    }

    /** Tapping the player in the shade or on the lock screen opens this episode's player in the app. */
    private fun openPlayer(episodeId: String): PendingIntent {
        val courseId = Episodes.courseOf(episodeId).orEmpty()
        val intent = Intent(this, AppActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            .putExtra(WidgetRoutes.EXTRA, WidgetRoutes.listen(courseId, episodeId))
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun buttons(speed: Float): List<CommandButton> = listOf(
        // Session commands rather than the player's seek commands: only a
        // session command reaches the system's player as a button of its own.
        CommandButton.Builder(CommandButton.ICON_SKIP_BACK_15)
            .setSessionCommand(SessionCommand(BACK, Bundle.EMPTY)).setDisplayName("Back 15 seconds")
            .setSlots(CommandButton.SLOT_BACK).build(),
        CommandButton.Builder(CommandButton.ICON_SKIP_FORWARD_30)
            .setSessionCommand(SessionCommand(FORWARD, Bundle.EMPTY)).setDisplayName("Forward 30 seconds")
            .setSlots(CommandButton.SLOT_FORWARD).build(),
        CommandButton.Builder(ListenSpeeds.icon(speed))
            .setSessionCommand(SessionCommand(SPEED, Bundle.EMPTY)).setDisplayName("Speed ${ListenSpeeds.label(speed)}")
            .setSlots(CommandButton.SLOT_OVERFLOW).build(),
        CommandButton.Builder(CommandButton.ICON_STOP)
            .setSessionCommand(SessionCommand(STOP, Bundle.EMPTY)).setDisplayName("Stop")
            .setSlots(CommandButton.SLOT_OVERFLOW).build(),
    )

    private inner class Controls : MediaSession.Callback {
        override fun onConnect(session: MediaSession, controller: MediaSession.ControllerInfo): MediaSession.ConnectionResult =
            MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                // One episode, so nothing to skip to. With these offered the
                // system draws previous and next where back 15 and forward 30
                // belong, which is what it did until they were taken away.
                .setAvailablePlayerCommands(
                    MediaSession.ConnectionResult.DEFAULT_PLAYER_COMMANDS.buildUpon()
                        .removeAll(
                            Player.COMMAND_SEEK_TO_PREVIOUS, Player.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM,
                            Player.COMMAND_SEEK_TO_NEXT, Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM,
                        )
                        .build(),
                )
                .setAvailableSessionCommands(
                    MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
                        .add(SessionCommand(BACK, Bundle.EMPTY))
                        .add(SessionCommand(FORWARD, Bundle.EMPTY))
                        .add(SessionCommand(SPEED, Bundle.EMPTY))
                        .add(SessionCommand(STOP, Bundle.EMPTY))
                        .build(),
                )
                .build()

        override fun onCustomCommand(session: MediaSession, controller: MediaSession.ControllerInfo, customCommand: SessionCommand, args: Bundle): ListenableFuture<SessionResult> {
            val player = session.player
            when (customCommand.customAction) {
                BACK -> player.seekBack()
                FORWARD -> player.seekForward()
                SPEED -> player.setPlaybackSpeed(ListenSpeeds.next(player.playbackParameters.speed))
                STOP -> {
                    // Stopping keeps your place, as pausing does; it only ends the
                    // session, which takes the player out of the shade.
                    savePlace()
                    player.stop()
                    player.clearMediaItems()
                }
                else -> return Futures.immediateFuture(SessionResult(SessionResult.RESULT_ERROR_NOT_SUPPORTED))
            }
            return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
        }
    }

    companion object {
        const val BACK = "com.shahaabapps.twotracks.listen.BACK"
        const val FORWARD = "com.shahaabapps.twotracks.listen.FORWARD"
        const val SPEED = "com.shahaabapps.twotracks.listen.SPEED"
        const val STOP = "com.shahaabapps.twotracks.listen.STOP"
        const val BACK_MS = 15_000L
        const val FORWARD_MS = 30_000L
        private const val SAVE_EVERY_MS = 5_000L
    }
}
