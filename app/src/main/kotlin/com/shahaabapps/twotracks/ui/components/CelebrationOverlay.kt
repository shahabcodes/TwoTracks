package com.shahaabapps.twotracks.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.shahaabapps.core.ui.theme.AccentPalette
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Celebration
import com.shahaabapps.twotracks.session.CelebrationKind
import com.shahaabapps.twotracks.session.Celebrations
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val DURATION_MS = 1600

/**
 * Full-screen moment after every write, built from the app icon: two tracks
 * rising together with a marker at the end of the lead track. Each action
 * animates the tracks differently (see [drawTracksScene]). Tap to skip.
 */
@Composable
fun CelebrationOverlay(celebrations: Celebrations, palette: AccentPalette) {
    var current by remember { mutableStateOf<Celebration?>(null) }
    LaunchedEffect(Unit) { celebrations.events.collect { current = it } }
    val event = current ?: return
    val haptic = LocalHapticFeedback.current
    val progress = remember(event.id) { Animatable(0f) }
    LaunchedEffect(event.id) {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        progress.animateTo(1f, tween(DURATION_MS, easing = LinearEasing))
        if (current?.id == event.id) current = null
    }
    val t = progress.value
    val scrim = when {
        t < 0.12f -> t / 0.12f
        t > 0.78f -> (1f - t) / 0.22f
        else -> 1f
    }.coerceIn(0f, 1f)
    val colors = TrackColors(
        lead = when (event.kind) {
            CelebrationKind.REMOVED -> AppTheme.colors.destructive
            CelebrationKind.PASSED -> AppTheme.colors.success
            else -> Color.White
        },
        gradient = when (event.kind) {
            CelebrationKind.REMOVED -> listOf(AppTheme.colors.destructive, AppTheme.colors.destructive.copy(alpha = 0.5f))
            CelebrationKind.PASSED -> listOf(AppTheme.colors.success, Color.White)
            else -> listOf(palette.gradient.first(), Color.White)
        },
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(event.id) { detectTapGestures { current = null } },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(Color.Black.copy(alpha = 0.62f * scrim))
            drawTracksScene(t, event.kind, colors, scrim)
        }
        LabelBlock(event.label, t, scrim)
    }
}

@Composable
private fun LabelBlock(label: String, t: Float, scrim: Float) {
    val appear = ((t - 0.3f) / 0.2f).coerceIn(0f, 1f)
    val ease = 1f - (1f - appear) * (1f - appear) * (1f - appear)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 420.dp).alpha(ease * scrim).scale(0.8f + 0.2f * ease),
    ) {
        Text(
            label,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = Spacing.lg),
        )
    }
}

// ------------------------------------------------------------------ geometry

private class TrackColors(val lead: Color, val gradient: List<Color>)

/** The two curves of the launcher icon in its 108-unit viewport. */
private object IconTracks {
    fun lead(): Path = Path().apply {
        moveTo(32f, 74f); cubicTo(40f, 60f, 44f, 52f, 54f, 44f); cubicTo(60f, 39f, 66f, 37f, 76f, 34f)
    }
    fun trail(): Path = Path().apply {
        moveTo(32f, 56f); cubicTo(44f, 52f, 52f, 44f, 58f, 36f); cubicTo(62f, 31f, 68f, 28f, 76f, 26f)
    }
}

private class Scaled(val lead: Path, val trail: Path, val leadMeasure: PathMeasure, val trailMeasure: PathMeasure, val stroke: Float, val dot: Float)

private fun DrawScope.scaledTracks(widthFraction: Float, centerY: Float): Scaled {
    val scale = size.width * widthFraction / 44f
    val ox = (size.width - 44f * scale) / 2f - 32f * scale
    val oy = centerY - 50f * scale
    fun map(p: Path): Path = Path().apply {
        addPath(p)
        translate(Offset(0f, 0f))
    }
    val lead = Path().apply {
        moveTo(ox + 32f * scale, oy + 74f * scale)
        cubicTo(ox + 40f * scale, oy + 60f * scale, ox + 44f * scale, oy + 52f * scale, ox + 54f * scale, oy + 44f * scale)
        cubicTo(ox + 60f * scale, oy + 39f * scale, ox + 66f * scale, oy + 37f * scale, ox + 76f * scale, oy + 34f * scale)
    }
    val trail = Path().apply {
        moveTo(ox + 32f * scale, oy + 56f * scale)
        cubicTo(ox + 44f * scale, oy + 52f * scale, ox + 52f * scale, oy + 44f * scale, ox + 58f * scale, oy + 36f * scale)
        cubicTo(ox + 62f * scale, oy + 31f * scale, ox + 68f * scale, oy + 28f * scale, ox + 76f * scale, oy + 26f * scale)
    }
    return Scaled(lead, trail, PathMeasure().apply { setPath(lead, false) }, PathMeasure().apply { setPath(trail, false) }, 7f * scale, 5f * scale)
}

private fun segment(measure: PathMeasure, from: Float, to: Float): Path {
    val out = Path()
    val len = measure.length
    val a = (from.coerceIn(0f, 1f) * len)
    val b = (to.coerceIn(0f, 1f) * len)
    if (b > a) measure.getSegment(a, b, out, true)
    return out
}

private fun ease(x: Float): Float = 1f - (1f - x) * (1f - x) * (1f - x)
private fun window(t: Float, from: Float, to: Float): Float = ((t - from) / (to - from)).coerceIn(0f, 1f)

/**
 * SAVED and LOGGED: tracks draw in, the marker rides the lead track and lands
 * with a pulse (LOGGED adds a tick). PASSED: the marker bursts into rings and
 * sparks and the tracks glow. REMOVED: the tracks unravel from the start and
 * the marker drops away.
 */
private fun DrawScope.drawTracksScene(t: Float, kind: CelebrationKind, colors: TrackColors, scrim: Float) {
    val s = scaledTracks(0.62f, size.height * 0.36f)
    val brush = Brush.linearGradient(colors.gradient, start = Offset(0f, size.height * 0.5f), end = Offset(size.width, size.height * 0.2f))
    val stroke = Stroke(width = s.stroke, cap = StrokeCap.Round)
    val thin = Stroke(width = s.stroke * 0.85f, cap = StrokeCap.Round)

    when (kind) {
        CelebrationKind.REMOVED -> {
            val show = window(t, 0f, 0.12f)
            val erase = ease(window(t, 0.22f, 0.72f))
            val drop = window(t, 0.45f, 0.9f)
            drawPath(segment(s.trailMeasure, erase, 1f), colors.lead.copy(alpha = 0.45f * show * scrim), style = thin)
            drawPath(segment(s.leadMeasure, erase, 1f), colors.lead.copy(alpha = show * scrim), style = stroke)
            val end = s.leadMeasure.getPosition(s.leadMeasure.length)
            val dy = drop * drop * size.height * 0.35f
            val r = s.dot * (1f - drop * 0.7f)
            drawCircle(colors.lead.copy(alpha = (1f - drop) * scrim), r, Offset(end.x, end.y + dy))
            // A faint ghost of where the track was.
            drawPath(s.lead, Color.White.copy(alpha = 0.08f * scrim), style = stroke)
        }
        CelebrationKind.PASSED -> {
            val draw = ease(window(t, 0f, 0.45f))
            val burst = window(t, 0.45f, 0.95f)
            val glow = 1f + 0.5f * sin(burst * PI.toFloat())
            drawPath(segment(s.trailMeasure, 0f, draw), Color.White.copy(alpha = 0.5f * scrim), style = thin)
            drawPath(segment(s.leadMeasure, 0f, draw), brush, style = Stroke(width = s.stroke * glow, cap = StrokeCap.Round), alpha = scrim)
            val pos = s.leadMeasure.getPosition(draw * s.leadMeasure.length)
            drawCircle(colors.lead, s.dot * (1f + 0.4f * sin(burst * PI.toFloat())), pos, alpha = scrim)
            if (burst > 0f) {
                for (ring in 0 until 3) {
                    val rt = window(burst, ring * 0.15f, 0.7f + ring * 0.15f)
                    if (rt <= 0f) continue
                    drawCircle(colors.lead.copy(alpha = (1f - rt) * 0.8f * scrim), s.dot + rt * size.width * 0.28f, pos, style = Stroke(width = s.stroke * 0.5f * (1f - rt) + 1f))
                }
                val n = 10
                for (i in 0 until n) {
                    val a = i * 2f * PI.toFloat() / n - PI.toFloat() / 2f
                    val st = window(burst, 0f, 0.55f)
                    val inner = s.dot * 2f + st * size.width * 0.12f
                    val outer = inner + s.dot * 3f * (1f - st)
                    drawLine(
                        Color.White.copy(alpha = (1f - st) * scrim),
                        Offset(pos.x + cos(a) * inner, pos.y + sin(a) * inner),
                        Offset(pos.x + cos(a) * outer, pos.y + sin(a) * outer),
                        strokeWidth = s.stroke * 0.4f, cap = StrokeCap.Round,
                    )
                }
            }
        }
        else -> {
            val draw = ease(window(t, 0f, 0.5f))
            val trailDraw = ease(window(t, 0.06f, 0.56f))
            val pulse = window(t, 0.5f, 0.85f)
            drawPath(segment(s.trailMeasure, 0f, trailDraw), Color.White.copy(alpha = 0.5f * scrim), style = thin)
            drawPath(segment(s.leadMeasure, 0f, draw), brush, style = stroke, alpha = scrim)
            val pos = s.leadMeasure.getPosition(draw * s.leadMeasure.length)
            drawCircle(colors.lead, s.dot * (1f + 0.25f * sin(pulse * PI.toFloat())), pos, alpha = scrim)
            if (pulse > 0f) {
                drawCircle(colors.lead.copy(alpha = (1f - pulse) * 0.7f * scrim), s.dot + pulse * size.width * 0.2f, pos, style = Stroke(width = s.stroke * 0.5f * (1f - pulse) + 1f))
            }
            if (kind == CelebrationKind.LOGGED && pulse > 0f) {
                // A tick grows out of the marker.
                val k = ease(pulse)
                val a = Offset(pos.x - s.dot * 1.6f, pos.y + s.dot * 0.1f)
                val b = Offset(pos.x - s.dot * 0.4f, pos.y + s.dot * 1.3f)
                val c = Offset(pos.x + s.dot * 2.2f, pos.y - s.dot * 1.4f)
                val first = window(k, 0f, 0.4f)
                val second = window(k, 0.4f, 1f)
                drawLine(Color.White, a, Offset(a.x + (b.x - a.x) * first, a.y + (b.y - a.y) * first), strokeWidth = s.stroke * 0.55f, cap = StrokeCap.Round, alpha = scrim)
                if (second > 0f) drawLine(Color.White, b, Offset(b.x + (c.x - b.x) * second, b.y + (c.y - b.y) * second), strokeWidth = s.stroke * 0.55f, cap = StrokeCap.Round, alpha = scrim)
            }
        }
    }
}

// -------------------------------------------------------------------- loader

/**
 * The icon's tracks as an endless loader: a lit window runs along each track
 * with the marker at its head. Used wherever the app waits on the network.
 */
@Composable
fun TracksLoader(modifier: Modifier = Modifier, size: Dp = 140.dp, label: String? = null) {
    val transition = rememberInfiniteTransition(label = "tracks")
    val u by transition.animateFloat(0f, 1.3f, infiniteRepeatable(tween(1500, easing = LinearEasing), RepeatMode.Restart), label = "u")
    val accent = AppTheme.colors.accent
    val secondary = AppTheme.colors.secondaryLabel
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        Canvas(modifier = Modifier.size(size)) {
            val s = scaledTracks(0.78f, this.size.height * 0.5f)
            drawPath(s.trail, secondary.copy(alpha = 0.15f), style = Stroke(width = s.stroke * 0.85f, cap = StrokeCap.Round))
            drawPath(s.lead, secondary.copy(alpha = 0.2f), style = Stroke(width = s.stroke, cap = StrokeCap.Round))
            val head = u.coerceAtMost(1f)
            val tail = (u - 0.35f).coerceIn(0f, 1f)
            val head2 = (u - 0.15f).coerceIn(0f, 1f)
            val tail2 = (u - 0.5f).coerceIn(0f, 1f)
            drawPath(segment(s.trailMeasure, tail2, head2), accent.copy(alpha = 0.45f), style = Stroke(width = s.stroke * 0.85f, cap = StrokeCap.Round))
            drawPath(segment(s.leadMeasure, tail, head), accent, style = Stroke(width = s.stroke, cap = StrokeCap.Round))
            if (u <= 1.05f) {
                val pos = s.leadMeasure.getPosition(head * s.leadMeasure.length)
                drawCircle(accent, s.dot, pos)
            }
        }
        label?.let { Text(it, style = MaterialTheme.typography.bodyMedium, color = secondary) }
    }
}

/** Full-screen, non-dismissable wait state over the current screen. */
@Composable
fun LoadingOverlay(label: String) {
    Dialog(onDismissRequest = {}, properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = false, dismissOnClickOutside = false)) {
        Box(modifier = Modifier.fillMaxSize().background(AppTheme.colors.groupedBackground.copy(alpha = 0.92f)), contentAlignment = Alignment.Center) {
            TracksLoader(label = label)
        }
    }
}
