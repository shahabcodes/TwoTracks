package com.shahaabapps.twotracks.feature.listen

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.PodcastBoard
import com.shahaabapps.core.content.PodcastBoards
import com.shahaabapps.core.content.PodcastCue
import com.shahaabapps.core.content.PodcastLine
import com.shahaabapps.core.content.PodcastSeries
import com.shahaabapps.core.content.PodcastStyle
import com.shahaabapps.core.content.PodcastTimeline
import com.shahaabapps.core.content.Podcasts
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.twotracks.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.math.max

/** One episode in the player: its script, its timing, and where the player is in it. */
data class ListenUiState(
    /** The episode's id: its topic's id for a classic episode, "topic--style" for another. */
    val episodeId: String = "",
    val title: String = "",
    val module: Int = 0,
    val lines: List<PodcastLine> = emptyList(),
    val timeline: PodcastTimeline? = null,
    /** This episode is the one in the player, rather than one you are looking at. */
    val loaded: Boolean = false,
    val playing: Boolean = false,
    /** Seconds in: the player's, or the place you left this episode. */
    val position: Double = 0.0,
    val speed: Float = 1f,
    /** What to draw in the cover's place while it plays, for an episode that has one. */
    val board: PodcastBoard? = null,
    /** Which show it is: what the player calls it, who is on it, and its cover. */
    val series: PodcastSeries = PodcastSeries.AZ204,
    /** The tellings this topic has in the app; the player offers a choice when there is more than one. */
    val styles: List<PodcastStyle> = emptyList(),
    val style: PodcastStyle = PodcastStyle.CLASSIC,
) {
    val duration: Double get() = timeline?.seconds ?: 0.0
    val line: Int? get() = timeline?.lineAt(position)
    val minutes: Int get() = max(1, ((duration + 30) / 60).toInt())
}

@HiltViewModel
class ListenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val episodes: Episodes,
    private val player: ListenPlayer,
    private val place: ListenPlace,
) : ViewModel() {
    // The route's id is the episode's: a topic's id opens its classic episode.
    private val episodeId = MutableStateFlow(savedStateHandle.get<String>("topicId").orEmpty())
    private val courseId: String = savedStateHandle.get<String>("courseId").orEmpty()
    private val id: String get() = episodeId.value

    val uiState: StateFlow<ListenUiState> = combine(player.state, episodeId) { now, id -> state(now, id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), state(player.state.value, episodeId.value))

    private fun state(now: NowPlaying?, id: String): ListenUiState {
        val podcast = Podcasts.episode(id)
        val topicId = podcast?.topicId ?: id
        val (module, topic) = Courses.topic(courseId, topicId) ?: return ListenUiState(episodeId = id)
        val timeline = episodes.timeline(id)
        val here = now?.takeIf { it.episodeId == id }
        val saved = place.position(id).takeIf { timeline?.resumable(it) == true } ?: 0.0
        return ListenUiState(
            episodeId = id, title = topic.title, module = module.number,
            lines = episodes.podcast(id)?.lines.orEmpty(), timeline = timeline,
            loaded = here != null, playing = here?.playing == true,
            position = here?.position ?: saved, speed = here?.speed ?: place.speed,
            board = PodcastBoards.forEpisode(id),
            series = podcast?.series ?: PodcastSeries.AZ204,
            styles = episodes.styles(topicId).map { it.style },
            style = podcast?.style ?: PodcastStyle.CLASSIC,
        )
    }

    /**
     * Another telling of the same topic: the player shows it, at the place you
     * left it, and remembers the choice for the next topic. What is playing
     * carries on until you press play on this one.
     */
    fun style(style: PodcastStyle) {
        val topicId = Podcasts.episode(id)?.topicId ?: return
        val target = episodes.styles(topicId).firstOrNull { it.style == style } ?: return
        place.style = style
        episodeId.value = target.episodeId
    }

    fun playPause() = if (uiState.value.playing) player.pause() else player.play(id)

    fun back() = if (uiState.value.loaded) player.back() else player.play(id, from = max(0.0, uiState.value.position - PodcastService.BACK_MS / 1000.0))

    fun forward() = if (uiState.value.loaded) player.forward() else player.play(id, from = uiState.value.position + PodcastService.FORWARD_MS / 1000.0)

    fun seek(seconds: Double) = if (uiState.value.loaded) player.seekTo(seconds) else player.play(id, from = seconds)

    /** A line in the transcript was tapped: play from where it starts. */
    fun line(index: Int) {
        val start = uiState.value.timeline?.startOf(index) ?: return
        player.play(id, from = start)
    }

    fun speed(speed: Float) = player.setSpeed(speed)

    fun stop() = player.stop()
}

@Composable
fun ListenScreen(onBack: () -> Unit, viewModel: ListenViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ListenContent(
        state, onBack = onBack, onPlayPause = viewModel::playPause, onBack15 = viewModel::back, onForward30 = viewModel::forward,
        onSeek = viewModel::seek, onSpeed = viewModel::speed, onStop = { viewModel.stop(); onBack() }, onLine = viewModel::line,
        onStyle = viewModel::style,
    )
}

/**
 * The player, drawn from its state alone.
 *
 * Parallel draws it as a podcast app's now-playing screen: the cover on a
 * field of its own blue, growing a little when it plays and settling when it
 * pauses, the scrubber, back 15 and forward 30 either side of play, and the
 * speed, the transcript and stop along the foot. The transcript takes the
 * cover's place, follows the voice line by line, and plays from any line you
 * tap. An episode with a board draws it in place of the cover, building its
 * diagrams as the hosts talk.
 */
@Composable
fun ListenContent(
    state: ListenUiState,
    onBack: () -> Unit,
    onPlayPause: () -> Unit,
    onBack15: () -> Unit,
    onForward30: () -> Unit,
    onSeek: (Double) -> Unit,
    onSpeed: (Float) -> Unit,
    onStop: () -> Unit,
    onLine: (Int) -> Unit,
    transcriptFirst: Boolean = false,
    onStyle: (PodcastStyle) -> Unit = {},
) {
    if (AppTheme.departures) {
        BoardListen(state, onBack, onPlayPause, onBack15, onForward30, onSeek, onSpeed, onStop, onLine, onStyle)
        return
    }
    LightStatusIcons()
    var transcript by rememberSaveable { mutableStateOf(transcriptFirst) }
    Column(
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Deep, Mid, Deep))).statusBarsPadding().navigationBarsPadding()
            .padding(horizontal = 24.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().height(52.dp)) {
            RoundControl("Close the player", onBack, size = 44.dp) {
                Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = null, tint = Ink, modifier = Modifier.size(32.dp))
            }
            Text(
                "${state.series.label.uppercase()} PODCAST", color = Soft, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.4.sp,
                textAlign = TextAlign.Center, modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(44.dp))
        }
        if (state.styles.size > 1) StyleChips(state, onStyle)
        AnimatedContent(
            targetState = transcript, label = "cover or transcript", modifier = Modifier.weight(1f).fillMaxWidth(),
            transitionSpec = { fadeIn() togetherWith fadeOut() },
        ) { showing ->
            if (showing) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                        Cover(Modifier.size(56.dp), corner = 8.dp, series = state.series)
                        Column(Modifier.weight(1f)) { Titles(state, size = 16.sp) }
                    }
                    Transcript(state, onLine, Modifier.weight(1f))
                }
            } else if (state.board != null) {
                // An episode with a board shows it where the cover was: the
                // diagram of what is being said, with the titles under it.
                Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                    EpisodeBoard(state.board, state.line, state.playing, Modifier.fillMaxWidth().weight(1f, fill = false))
                    Spacer(Modifier.height(20.dp))
                    Titles(state, size = 18.sp)
                }
            } else {
                Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                    val scale by animateFloatAsState(if (state.playing) 1f else 0.86f, spring(dampingRatio = 0.62f, stiffness = 260f), label = "cover")
                    BoxWithConstraints(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f).fillMaxWidth()) {
                        // As large as the space allows either way, so a short
                        // screen shrinks the cover rather than the controls.
                        Cover(
                            Modifier.size(min(maxWidth, maxHeight)).graphicsLayer { scaleX = scale; scaleY = scale },
                            corner = 14.dp, shadow = if (state.playing) 28.dp else 12.dp, series = state.series,
                        )
                    }
                    Spacer(Modifier.height(28.dp))
                    Titles(state, size = 21.sp)
                }
            }
        }
        Spacer(Modifier.height(18.dp))
        Scrubber(state, onSeek)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
        ) {
            RoundControl("Back 15 seconds", onBack15, size = 64.dp) { SkipGlyph(15, forward = false, tint = Ink, size = 40.dp) }
            RoundControl(if (state.playing) "Pause" else "Play", onPlayPause, size = 84.dp) {
                Icon(if (state.playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, contentDescription = null, tint = Ink, modifier = Modifier.size(64.dp))
            }
            RoundControl("Forward 30 seconds", onForward30, size = 64.dp) { SkipGlyph(30, forward = true, tint = Ink, size = 40.dp) }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 12.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(32.dp).width(56.dp).clip(RoundedCornerShape(10.dp)).background(Ink.copy(alpha = 0.16f))
                    .clickable(role = Role.Button, onClickLabel = "Change the speed") { onSpeed(ListenSpeeds.next(state.speed)) },
            ) { Text(ListenSpeeds.label(state.speed), color = Ink, fontSize = 15.sp, fontWeight = FontWeight.SemiBold) }
            RoundControl(if (transcript) "Show the cover" else "Show the transcript", { transcript = !transcript }, size = 44.dp, on = transcript) {
                Icon(Icons.Rounded.FormatQuote, contentDescription = null, tint = if (transcript) Deep else Ink, modifier = Modifier.size(26.dp))
            }
            RoundControl("Stop", onStop, size = 44.dp) {
                Icon(Icons.Rounded.Stop, contentDescription = null, tint = Ink, modifier = Modifier.size(28.dp))
            }
        }
    }
}

@Composable
private fun Titles(state: ListenUiState, size: androidx.compose.ui.unit.TextUnit) {
    Text(state.title, color = Ink, fontSize = size, lineHeight = size * 1.2f, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
    Text("${state.series.hosts} · Module ${state.module}", color = Soft, fontSize = 15.sp, modifier = Modifier.padding(top = 2.dp))
}

/** The transcript as a podcast app shows one: big type, the line being spoken lit, the rest dimmed. */
@Composable
private fun Transcript(state: ListenUiState, onLine: (Int) -> Unit, modifier: Modifier = Modifier) {
    val list = rememberLazyListState()
    val current = state.line
    LaunchedEffect(current) { if (current != null) list.animateScrollToItem(max(0, current - 1)) }
    LazyColumn(state = list, modifier = modifier, contentPadding = PaddingValues(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        itemsIndexed(state.lines) { i, line ->
            val lit = i == current
            Column(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                    .clickable(role = Role.Button, onClickLabel = "Play from here") { onLine(i) }.padding(vertical = 4.dp),
            ) {
                if (i == 0 || state.lines[i - 1].host != line.host) {
                    Text(line.host.displayName.uppercase(), color = Soft, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
                }
                Text(
                    PodcastCue.strip(line.text), fontSize = 21.sp, lineHeight = 28.sp, fontWeight = FontWeight.SemiBold,
                    // Large bold type, so the dimmed lines need 3:1 rather than
                    // 4.5:1; at 0.6 they measure about 4:1 on the field.
                    color = if (lit) Ink else Ink.copy(alpha = 0.6f),
                )
                // Jonas's German, with the English under it.
                line.translation?.let {
                    Text(it, fontSize = 16.sp, lineHeight = 21.sp, color = Soft, modifier = Modifier.padding(top = 2.dp))
                }
            }
        }
    }
}

/** The tellings of this topic, one chip each; the one showing is filled. */
@Composable
private fun StyleChips(state: ListenUiState, onStyle: (PodcastStyle) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally), modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 6.dp)) {
        state.styles.forEach { style ->
            val on = style == state.style
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(32.dp).clip(CircleShape).background(if (on) Ink else Ink.copy(alpha = 0.14f))
                    .clickable(role = Role.Tab, onClickLabel = "Listen to the ${style.label} telling") { onStyle(style) }
                    .padding(horizontal = 14.dp),
            ) {
                Text(style.label, color = if (on) Deep else Ink, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

/** A thin track that thickens while you drag it, with the time gone and the time left under it. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Scrubber(state: ListenUiState, onSeek: (Double) -> Unit) {
    var dragging by remember { mutableStateOf<Float?>(null) }
    val end = max(state.duration, 1.0).toFloat()
    val shown = dragging ?: state.position.toFloat().coerceIn(0f, end)
    Slider(
        value = shown, valueRange = 0f..end,
        onValueChange = { dragging = it },
        onValueChangeFinished = { dragging?.let { onSeek(it.toDouble()) }; dragging = null },
        thumb = { Box(Modifier.size(if (dragging != null) 18.dp else 8.dp).clip(CircleShape).background(Ink)) },
        track = {
            Box(Modifier.fillMaxWidth().height(if (dragging != null) 9.dp else 5.dp).clip(CircleShape).background(Ink.copy(alpha = 0.26f))) {
                Box(Modifier.fillMaxWidth(shown / end).fillMaxHeight().background(Ink.copy(alpha = 0.9f)))
            }
        },
        modifier = Modifier.fillMaxWidth().height(28.dp),
    )
    Row(Modifier.fillMaxWidth()) {
        Text(clock(shown.toDouble()), color = Soft, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Text("-" + clock(max(0.0, state.duration - shown)), color = Soft, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

/** A control drawn as a glyph on nothing, the way a now-playing screen draws them, with a soft circle when [on]. */
@Composable
private fun RoundControl(label: String, onClick: () -> Unit, size: Dp, on: Boolean = false, content: @Composable () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size).clip(CircleShape).background(if (on) Ink else Color.Transparent)
            .clickable(role = Role.Button, onClickLabel = label, onClick = onClick),
    ) { content() }
}

/** The circular arrow with the seconds inside it: back 15, forward 30. */
@Composable
internal fun SkipGlyph(seconds: Int, forward: Boolean, tint: Color, size: Dp) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
        Icon(Icons.Rounded.Replay, contentDescription = null, tint = tint, modifier = Modifier.fillMaxSize().graphicsLayer { if (forward) scaleX = -1f })
        Text("$seconds", color = tint, fontSize = (size.value * 0.26f).sp, fontWeight = FontWeight.Bold, modifier = Modifier.offset(y = size * 0.07f))
    }
}

/** The episode's cover, the same picture the notification and the lock screen show. */
@Composable
internal fun Cover(modifier: Modifier, corner: Dp, shadow: Dp = 0.dp, series: PodcastSeries = PodcastSeries.AZ204) {
    val shape = RoundedCornerShape(corner)
    Image(
        painterResource(coverOf(series)), contentDescription = null, contentScale = ContentScale.Crop,
        modifier = modifier.shadow(shadow, shape).clip(shape),
    )
}

/** Each show's cover, drawn by tools/podcast/art/cover.py. */
internal fun coverOf(series: PodcastSeries): Int = when (series) {
    PodcastSeries.AZ204 -> R.drawable.podcast_cover
    PodcastSeries.GERMAN_A1 -> R.drawable.podcast_cover_de
}

/** The status bar's icons in white while the player's dark field is behind them. */
@Composable
private fun LightStatusIcons() {
    val view = LocalView.current
    DisposableEffect(view) {
        val window = (view.context as? Activity)?.window
        val bars = window?.let { WindowCompat.getInsetsController(it, view) }
        val was = bars?.isAppearanceLightStatusBars
        bars?.isAppearanceLightStatusBars = false
        onDispose { if (was != null) bars.isAppearanceLightStatusBars = was }
    }
}

/** 3:07, or 1:02:07 past the hour. */
internal fun clock(seconds: Double): String {
    val s = seconds.toInt().coerceAtLeast(0)
    return if (s >= 3600) "%d:%02d:%02d".format(s / 3600, s / 60 % 60, s % 60) else "%d:%02d".format(s / 60, s % 60)
}

// The cover's own blues, so the screen reads as the episode's rather than
// the app's. White on them is 8:1 at the lightest; Soft is about 5:1.
private val Deep = Color(0xFF081E52)
private val Mid = Color(0xFF0B3F8F)
private val Ink = Color.White
private val Soft = Color.White.copy(alpha = 0.72f)
