package com.shahaabapps.twotracks.feature.listen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.content.PodcastCue
import com.shahaabapps.core.content.PodcastStyle
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.twotracks.feature.learn.DiagramColors
import com.shahaabapps.twotracks.ui.board.BoardBack
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardLink
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardSegments
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardTitle
import com.shahaabapps.twotracks.ui.board.FlipTiles
import com.shahaabapps.twotracks.ui.board.LineMark
import kotlin.math.max

/**
 * The player as the board prints it. The episode's board fills the screen
 * and scrolls, under the departure (cover, title, how long) and the choice of
 * tellings; the timetable, the transcript as the board marks the next train,
 * is one segment away, and every line in it is a stop you can tap to play
 * from. The subtitles stay pinned above the controls, and the controls (the
 * time on flip tiles, the track, the keys and the speeds) fold away to one
 * row to give the board the room, which is remembered.
 */
@Composable
internal fun BoardListen(
    state: ListenUiState,
    onBack: () -> Unit,
    onPlayPause: () -> Unit,
    onBack15: () -> Unit,
    onForward30: () -> Unit,
    onSeek: (Double) -> Unit,
    onSpeed: (Float) -> Unit,
    onStop: () -> Unit,
    onLine: (Int) -> Unit,
    onStyle: (PodcastStyle) -> Unit = {},
    controlsHidden: Boolean = false,
    onToggleControls: () -> Unit = {},
    transcriptFirst: Boolean = false,
    onRefresh: () -> Unit = {},
    onOpenNotes: (() -> Unit)? = null,
) {
    // An episode with a board shows it first, and the timetable a tap away;
    // one without keeps the timetable alone.
    var showBoard by rememberSaveable { mutableStateOf(!transcriptFirst) }
    val board = state.board?.takeIf { showBoard }
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        Column(Modifier.fillMaxSize().padding(inner)) {
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = BoardGutter).padding(top = 6.dp, bottom = 6.dp).height(44.dp),
            ) {
                BoardBack(onBack, "Close the player")
                BoardLabel(state.series.label, size = 13.sp, maxLines = 1, modifier = Modifier.weight(1f))
                // The board and the timetable, one link apart.
                if (state.board != null) BoardLink(if (showBoard) "Transcript" else "Board", { showBoard = !showBoard })
                // Fetch the latest recording, replacing the copy on the phone; offered once there is one.
                if (state.downloaded && state.downloading == null) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(40.dp).clickable(role = Role.Button, onClickLabel = "Download the latest version", onClick = onRefresh),
                    ) { Icon(Icons.Rounded.Refresh, contentDescription = null, tint = AppTheme.colors.accent, modifier = Modifier.size(24.dp)) }
                }
                if (onOpenNotes != null) BoardLink("Notes", onOpenNotes)
                BoardLink("Stop", onStop)
            }
            HorizontalDivider(color = AppTheme.colors.separator)
            Box(Modifier.weight(1f).fillMaxWidth()) {
                if (board != null) {
                    // The board at its full size, scrolling on a short screen
                    // rather than pushing the controls off it.
                    Column(
                        Modifier.fillMaxSize().fadeBottom(28.dp).verticalScroll(rememberScrollState()).padding(horizontal = BoardGutter).padding(top = 12.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Departure(state, onStyle)
                        EpisodeBoard(
                            board, state.line, state.playing,
                            palette = boardPalette(),
                            heading = { BoardLabel(it, color = AppTheme.colors.accent) },
                        )
                    }
                } else {
                    BoardTimetable(state, onLine, Modifier.fillMaxSize()) { Departure(state, onStyle) }
                }
            }
            if (board != null) BoardCaptions(state)
            HorizontalDivider(color = AppTheme.colors.separator)
            AnimatedContent(
                targetState = controlsHidden, label = "controls",
                transitionSpec = { (fadeIn() + expandVertically()) togetherWith (fadeOut() + shrinkVertically()) },
            ) { hidden ->
                if (hidden) {
                    SlimBoardControls(state, onPlayPause, onForward30, onShow = onToggleControls)
                } else {
                    Column(Modifier.padding(horizontal = BoardGutter).padding(bottom = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth().height(30.dp).clickable(role = Role.Button, onClickLabel = "Hide the controls", onClick = onToggleControls),
                        ) { Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = null, tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(26.dp)) }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FlipTiles(clock(state.position).padStart(5, '0'), tileWidth = 28.dp, tileHeight = 38.dp, fontSize = 23.sp, gap = 3.dp)
                            Box(Modifier.weight(1f))
                            Column(horizontalAlignment = Alignment.End) {
                                BoardLabel("Left")
                                BoardMono(clock(max(0.0, state.duration - state.position)), color = AppTheme.colors.ink, size = 16.sp)
                            }
                        }
                        BoardScrubber(state, onSeek)
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().height(56.dp)) {
                            BoardControl("Back 15 seconds", onBack15, Modifier.weight(1f)) { SkipGlyph(15, forward = false, tint = AppTheme.colors.ink, size = 30.dp) }
                            BoardPlayKey(state, onPlayPause, Modifier.weight(1.6f).fillMaxHeight())
                            BoardControl("Forward 30 seconds", onForward30, Modifier.weight(1f)) { SkipGlyph(30, forward = true, tint = AppTheme.colors.ink, size = 30.dp) }
                            // The speed as one key that steps through them, as the Parallel player has it.
                            BoardControl("Change the speed", { onSpeed(ListenSpeeds.next(state.speed)) }, Modifier.weight(1f)) {
                                BoardMono(ListenSpeeds.label(state.speed), color = AppTheme.colors.ink, size = 15.sp, spacing = 0.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

/** The episode as a departure: its cover, its module and hosts, its title, how long, and the tellings to choose from. */
@Composable
private fun Departure(state: ListenUiState, onStyle: (PodcastStyle) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Cover(Modifier.size(64.dp), corner = 8.dp, series = state.series)
            Column(verticalArrangement = Arrangement.spacedBy(3.dp), modifier = Modifier.weight(1f)) {
                BoardLabel("Module ${state.module} · ${state.hosts}", color = AppTheme.colors.accent)
                BoardTitle(state.title, size = 21.sp)
                BoardMono("${state.minutes} MIN · ${state.lines.size} LINES")
                state.fetchLine?.let { BoardMono(it.uppercase()) }
            }
        }
        if (state.styles.size > 1) {
            BoardSegments(state.styles.map { it to it.label }, selected = state.style, onSelect = onStyle, scroll = true)
        }
    }
}

/** Play or pause in the signal colour; while the sound downloads after play, the key fills from the left as it comes. */
@Composable
private fun BoardPlayKey(state: ListenUiState, onPlayPause: () -> Unit, modifier: Modifier, compact: Boolean = false) {
    val downloading = state.downloading
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier.clip(RoundedCornerShape(12.dp))
            .background(if (downloading != null) AppTheme.colors.accent.copy(alpha = 0.35f) else AppTheme.colors.accent)
            .clickable(role = Role.Button, onClickLabel = if (downloading != null) "Downloading" else if (state.playing) "Pause" else "Play", onClick = onPlayPause),
    ) {
        val wide = maxWidth >= 132.dp
        if (downloading != null) Box(Modifier.align(Alignment.CenterStart).fillMaxHeight().fillMaxWidth(downloading).background(AppTheme.colors.accent))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            if (downloading != null) {
                Icon(Icons.Rounded.Download, contentDescription = null, tint = AppTheme.colors.onSolid, modifier = Modifier.size(if (compact) 20.dp else 26.dp))
                BoardLabel("${(downloading * 100).toInt()}%", color = AppTheme.colors.onSolid, size = if (compact) 14.sp else 18.sp, weight = FontWeight.Bold)
            } else {
                Icon(if (state.playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, contentDescription = null, tint = AppTheme.colors.onSolid, modifier = Modifier.size(if (compact) 26.dp else 30.dp))
                // The word only where it fits beside the glyph: a narrow phone with large text keeps the glyph alone.
                if (!compact && wide) BoardLabel(if (state.playing) "Pause" else "Play", color = AppTheme.colors.onSolid, size = 18.sp, weight = FontWeight.Bold)
            }
        }
    }
}

/** The controls folded away: the run so far along the top, then play, the time, forward 30, and the way back to the rest. */
@Composable
private fun SlimBoardControls(state: ListenUiState, onPlayPause: () -> Unit, onForward30: () -> Unit, onShow: () -> Unit) {
    val done = if (state.duration > 0) (state.position / state.duration).toFloat().coerceIn(0f, 1f) else 0f
    Column {
        Box(Modifier.fillMaxWidth().height(3.dp).background(AppTheme.colors.separator)) {
            Box(Modifier.fillMaxWidth(done).fillMaxHeight().background(AppTheme.colors.accent))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = BoardGutter, vertical = 10.dp).height(48.dp),
        ) {
            BoardPlayKey(state, onPlayPause, Modifier.width(76.dp).fillMaxHeight(), compact = true)
            BoardMono("${clock(state.position)}/${clock(state.duration)}", color = AppTheme.colors.ink, size = 14.sp, spacing = 0.sp, modifier = Modifier.weight(1f))
            BoardControl("Forward 30 seconds", onForward30, Modifier.width(52.dp)) { SkipGlyph(30, forward = true, tint = AppTheme.colors.ink, size = 26.dp) }
            BoardControl("Show all the controls", onShow, Modifier.width(52.dp)) {
                Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = null, tint = AppTheme.colors.ink, modifier = Modifier.size(28.dp))
            }
        }
    }
}

/** Subtitles above the board: who is talking, and what they are saying now, in a strip that keeps its height. */
@Composable
private fun BoardCaptions(state: ListenUiState) {
    val subtitle = state.subtitle
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.fillMaxWidth().heightIn(min = 84.dp).background(AppTheme.colors.card).padding(horizontal = BoardGutter, vertical = 10.dp),
    ) {
        if (subtitle != null) {
            BoardLabel(subtitle.host.displayName, color = AppTheme.colors.accent, size = 12.sp)
            BoardText(subtitle.text, color = AppTheme.colors.ink, size = 17.sp, weight = FontWeight.Medium, maxLines = 3)
            subtitle.translation?.let { BoardText(it, color = AppTheme.colors.secondaryLabel, size = 14.sp, maxLines = 2) }
        }
    }
}

/** The board in the Departures look's own colours, the way its other diagrams are drawn. */
@Composable
private fun boardPalette() = BoardPalette(
    ink = AppTheme.colors.ink,
    soft = AppTheme.colors.secondaryLabel,
    accent = AppTheme.colors.accent,
    card = AppTheme.colors.card,
    diagram = DiagramColors(
        accent = AppTheme.colors.accent,
        plate = AppTheme.colors.groupedBackground,
        fill = AppTheme.colors.fill,
        secondary = AppTheme.colors.secondaryLabel,
        ink = AppTheme.colors.ink,
        onSolid = AppTheme.colors.onSolid,
    ),
)

@Composable
private fun BoardTimetable(state: ListenUiState, onLine: (Int) -> Unit, modifier: Modifier, header: @Composable () -> Unit = {}) {
    val list = rememberLazyListState()
    val current = state.line
    LaunchedEffect(current) { if (current != null) list.animateScrollToItem(current) }
    LazyColumn(state = list, modifier = modifier, contentPadding = PaddingValues(horizontal = BoardGutter, vertical = 10.dp)) {
        item { Box(Modifier.padding(top = 2.dp, bottom = 12.dp)) { header() } }
        itemsIndexed(state.lines) { i, line ->
            val lit = i == current
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
                    .clickable(role = Role.Button, onClickLabel = "Play from here") { onLine(i) }.padding(vertical = 8.dp),
            ) {
                BoardMono(state.timeline?.startOf(i)?.let(::clock).orEmpty(), color = if (lit) AppTheme.colors.accent else AppTheme.colors.secondaryLabel, modifier = Modifier.width(40.dp))
                LineMark(if (lit) AppTheme.colors.accent else Color.Transparent)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    BoardLabel(line.host.displayName, color = if (lit) AppTheme.colors.accent else AppTheme.colors.secondaryLabel, size = 11.sp)
                    BoardText(PodcastCue.strip(line.text), color = if (lit) AppTheme.colors.ink else AppTheme.colors.secondaryLabel, size = 15.sp, weight = if (lit) FontWeight.Medium else FontWeight.Normal)
                    // Jonas's German, with the English under it.
                    line.translation?.let { BoardText(it, color = AppTheme.colors.secondaryLabel, size = 13.sp) }
                }
            }
        }
    }
}

/** A square key with a glyph on it, outlined, as the board's secondary actions are. */
@Composable
private fun BoardControl(label: String, onClick: () -> Unit, modifier: Modifier, content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxHeight().clip(shape).border(1.5.dp, AppTheme.colors.strongBorder, shape)
            .clickable(role = Role.Button, onClickLabel = label, onClick = onClick),
    ) { content() }
}

/** The track as the board draws a line: the run so far in signal colour, a marker where you are. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoardScrubber(state: ListenUiState, onSeek: (Double) -> Unit) {
    var dragging by remember { mutableStateOf<Float?>(null) }
    val end = max(state.duration, 1.0).toFloat()
    val shown = dragging ?: state.position.toFloat().coerceIn(0f, end)
    val marker: Dp = if (dragging != null) 26.dp else 18.dp
    Slider(
        value = shown, valueRange = 0f..end,
        onValueChange = { dragging = it },
        onValueChangeFinished = { dragging?.let { onSeek(it.toDouble()) }; dragging = null },
        thumb = { Box(Modifier.width(5.dp).height(marker).clip(RoundedCornerShape(2.dp)).background(AppTheme.colors.ink)) },
        track = {
            Box(Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)).background(AppTheme.colors.separator)) {
                Box(Modifier.fillMaxWidth(shown / end).fillMaxHeight().background(AppTheme.colors.accent))
            }
        },
        modifier = Modifier.fillMaxWidth().height(28.dp),
    )
}
