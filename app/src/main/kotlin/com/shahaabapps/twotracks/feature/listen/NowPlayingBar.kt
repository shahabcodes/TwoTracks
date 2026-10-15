package com.shahaabapps.twotracks.feature.listen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.max

/** The player for the bar that sits over the tabs while an episode is loaded. */
@HiltViewModel
class NowPlayingViewModel @Inject constructor(val player: ListenPlayer) : ViewModel()

/**
 * The episode carried on through the rest of the app: a small player above
 * the tab bar with the cover, the topic, play or pause and forward 30, a hair
 * of progress along its foot, and the full player a tap away.
 */
@Composable
fun NowPlayingBar(now: NowPlaying, onOpen: () -> Unit, onPlayPause: () -> Unit, onForward30: () -> Unit, modifier: Modifier = Modifier) {
    val done = if (now.duration > 0) (now.position / now.duration).toFloat().coerceIn(0f, 1f) else 0f
    if (AppTheme.departures) {
        Column(modifier.fillMaxWidth().background(AppTheme.colors.card).clickable(role = Role.Button, onClickLabel = "Open the player", onClick = onOpen)) {
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = BoardGutter, vertical = 10.dp),
            ) {
                Cover(Modifier.size(40.dp), corner = 6.dp)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    BoardLabel("Now playing · ${clock(max(0.0, now.duration - now.position))} left", color = AppTheme.colors.accent, size = 11.sp)
                    BoardText(now.title, color = AppTheme.colors.ink, size = 15.sp, weight = FontWeight.SemiBold)
                }
                BarKey(if (now.playing) "Pause" else "Play", onPlayPause, solid = true) {
                    Icon(if (now.playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, contentDescription = null, tint = AppTheme.colors.onSolid, modifier = Modifier.size(26.dp))
                }
                BarKey("Forward 30 seconds", onForward30, solid = false) { SkipGlyph(30, forward = true, tint = AppTheme.colors.ink, size = 26.dp) }
            }
            Box(Modifier.fillMaxWidth().height(3.dp).background(AppTheme.colors.separator)) {
                Box(Modifier.fillMaxWidth(done).fillMaxHeight().background(AppTheme.colors.accent))
            }
        }
        return
    }
    val shape = RoundedCornerShape(14.dp)
    Box(modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp)) {
        Column(
            Modifier.fillMaxWidth().shadow(10.dp, shape).clip(shape).background(AppTheme.colors.card)
                .clickable(role = Role.Button, onClickLabel = "Open the player", onClick = onOpen),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(start = 8.dp, end = 4.dp, top = 8.dp, bottom = 6.dp)) {
                Cover(Modifier.size(42.dp), corner = 7.dp)
                Column(Modifier.weight(1f)) {
                    Text(now.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${ListenPlayer.HOSTS} · ${clock(max(0.0, now.duration - now.position))} left", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, maxLines = 1)
                }
                BarKey(if (now.playing) "Pause" else "Play", onPlayPause, solid = false) {
                    Icon(if (now.playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, contentDescription = null, tint = AppTheme.colors.ink, modifier = Modifier.size(30.dp))
                }
                BarKey("Forward 30 seconds", onForward30, solid = false) { SkipGlyph(30, forward = true, tint = AppTheme.colors.ink, size = 28.dp) }
            }
            Box(Modifier.padding(horizontal = 14.dp).padding(bottom = 5.dp).fillMaxWidth().height(2.dp).clip(CircleShape).background(AppTheme.colors.separator)) {
                Box(Modifier.fillMaxWidth(done).fillMaxHeight().background(AppTheme.colors.ink))
            }
        }
    }
}

@Composable
private fun BarKey(label: String, onClick: () -> Unit, solid: Boolean, content: @Composable () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(44.dp).clip(if (AppTheme.departures) RoundedCornerShape(10.dp) else CircleShape)
            .background(if (solid) AppTheme.colors.accent else androidx.compose.ui.graphics.Color.Transparent)
            .clickable(role = Role.Button, onClickLabel = label, onClick = onClick),
    ) { content() }
}

/** What the topic's Listen card says: how long the episode is, and whether you are partway through it. */
data class ListenCardState(val minutes: Int, val playing: Boolean = false, val left: Double? = null)

/**
 * The way into a topic's episode, at the head of the lesson: the cover, Maya
 * & Leo, how long it runs, and where you are in it once you have started.
 */
@Composable
fun ListenCard(state: ListenCardState, onListen: () -> Unit, modifier: Modifier = Modifier) {
    val left = state.left?.let { "${clock(it)} left" }
    val line = when {
        state.playing -> "Playing · ${left.orEmpty()}"
        left != null -> "Carry on · $left"
        else -> "${ListenPlayer.HOSTS} talk it through · ${state.minutes} min"
    }
    if (AppTheme.departures) {
        val shape = RoundedCornerShape(12.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier.fillMaxWidth().clip(shape).background(AppTheme.colors.card)
                .clickable(role = Role.Button, onClickLabel = "Listen to this topic", onClick = onListen).padding(10.dp),
        ) {
            Cover(Modifier.size(48.dp), corner = 8.dp)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                BoardLabel("Listen · ${state.minutes} min", color = AppTheme.colors.accent)
                BoardText(line, color = AppTheme.colors.ink, size = 14.sp)
            }
            BoardMono(if (state.playing) "ON AIR" else "PLAY →", color = AppTheme.colors.accent, size = 12.sp)
        }
        return
    }
    val shape = RoundedCornerShape(16.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth().clip(shape).border(Spacing.rule, AppTheme.colors.separator, shape)
            .clickable(role = Role.Button, onClickLabel = "Listen to this topic", onClick = onListen).padding(12.dp),
    ) {
        Cover(Modifier.size(52.dp), corner = 9.dp)
        Column(Modifier.weight(1f)) {
            Text("Listen to this topic", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(line, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
        }
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(36.dp).clip(CircleShape).background(AppTheme.colors.accent)) {
            Icon(if (state.playing) Icons.Rounded.Headphones else Icons.Rounded.PlayArrow, contentDescription = null, tint = AppTheme.colors.onSolid, modifier = Modifier.size(22.dp))
        }
    }
}
