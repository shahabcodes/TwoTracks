package com.shahaabapps.twotracks.feature.listen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
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
import com.shahaabapps.core.ui.theme.AppTheme
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
 * The player as the board prints it: the episode as a departure, the time
 * gone on flip tiles, the speeds as a row of segments, and the transcript as
 * the timetable under it, the line being spoken marked as the board marks
 * the next train. Every line is a stop you can tap to play from.
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
) {
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        Column(Modifier.fillMaxSize().padding(inner)) {
            Column(Modifier.padding(horizontal = BoardGutter), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 6.dp).height(44.dp)) {
                    BoardBack(onBack, "Close the player")
                    BoardLabel("Now playing · ${state.series.label} podcast", size = 13.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                    BoardMono(ListenSpeeds.label(state.speed), modifier = Modifier.width(34.dp), textAlign = TextAlign.End, spacing = 0.sp)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Cover(Modifier.size(84.dp), corner = 10.dp, series = state.series)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.weight(1f)) {
                        BoardLabel("Module ${state.module} · ${state.series.hosts}", color = AppTheme.colors.accent)
                        BoardTitle(state.title, size = 26.sp)
                        BoardMono("${state.minutes} MIN · ${state.lines.size} LINES")
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FlipTiles(clock(state.position).padStart(5, '0'), tileWidth = 34.dp, tileHeight = 46.dp, fontSize = 28.sp, gap = 4.dp)
                    Box(Modifier.weight(1f))
                    Column(horizontalAlignment = Alignment.End) {
                        BoardLabel("Left")
                        BoardMono(clock(max(0.0, state.duration - state.position)), color = AppTheme.colors.ink, size = 16.sp)
                    }
                }
                BoardScrubber(state, onSeek)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                    BoardControl("Back 15 seconds", onBack15, Modifier.weight(1f)) { SkipGlyph(15, forward = false, tint = AppTheme.colors.ink, size = 30.dp) }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.weight(1.6f).height(60.dp).clip(RoundedCornerShape(12.dp)).background(AppTheme.colors.accent)
                            .clickable(role = Role.Button, onClickLabel = if (state.playing) "Pause" else "Play", onClick = onPlayPause),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(if (state.playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, contentDescription = null, tint = AppTheme.colors.onSolid, modifier = Modifier.size(30.dp))
                            BoardLabel(if (state.playing) "Pause" else "Play", color = AppTheme.colors.onSolid, size = 18.sp, weight = FontWeight.Bold)
                        }
                    }
                    BoardControl("Forward 30 seconds", onForward30, Modifier.weight(1f)) { SkipGlyph(30, forward = true, tint = AppTheme.colors.ink, size = 30.dp) }
                }
                BoardSegments(ListenSpeeds.ALL.map { it to ListenSpeeds.label(it) }, selected = ListenSpeeds.nearest(state.speed), onSelect = onSpeed)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BoardLabel("Transcript", modifier = Modifier.weight(1f))
                    BoardLink("Stop", onStop)
                }
            }
            HorizontalDivider(color = AppTheme.colors.separator, modifier = Modifier.padding(top = 8.dp))
            BoardTimetable(state, onLine, Modifier.weight(1f))
        }
    }
}

@Composable
private fun BoardTimetable(state: ListenUiState, onLine: (Int) -> Unit, modifier: Modifier) {
    val list = rememberLazyListState()
    val current = state.line
    LaunchedEffect(current) { if (current != null) list.animateScrollToItem(max(0, current - 1)) }
    LazyColumn(state = list, modifier = modifier, contentPadding = PaddingValues(horizontal = BoardGutter, vertical = 10.dp)) {
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
