package com.shahaabapps.twotracks.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.theme.AccentPalette
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Celebration
import com.shahaabapps.twotracks.session.CelebrationKind
import com.shahaabapps.twotracks.session.Celebrations
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private const val DURATION_MS = 1700

/**
 * Full-screen celebration played after every save, log, pass or delete.
 * On the Qatar theme a maroon-and-white flag flies in and waves; on the other
 * palettes a confetti burst in the accent gradient. Tap anywhere to skip.
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
    // Scrim: in over the first 15%, out over the last 25%.
    val scrim = when {
        t < 0.15f -> t / 0.15f
        t > 0.75f -> (1f - t) / 0.25f
        else -> 1f
    }.coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(event.id) { detectTapGestures { current = null } },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(Color.Black.copy(alpha = 0.58f * scrim))
            if (palette == AccentPalette.QATAR) drawFlagScene(t, event.kind) else drawConfettiScene(t, event.kind, palette, event.id)
        }
        LabelBlock(event, t, scrim, palette)
    }
}

@Composable
private fun LabelBlock(event: Celebration, t: Float, scrim: Float, palette: AccentPalette) {
    val appear = ((t - 0.25f) / 0.2f).coerceIn(0f, 1f)
    val ease = 1f - (1f - appear) * (1f - appear) * (1f - appear)
    val icon = when (event.kind) {
        CelebrationKind.SAVED -> Icons.Filled.Check
        CelebrationKind.REMOVED -> Icons.Filled.Delete
        CelebrationKind.PASSED -> Icons.Filled.EmojiEvents
        CelebrationKind.LOGGED -> Icons.Filled.Schedule
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = if (palette == AccentPalette.QATAR) 300.dp else 0.dp)
            .alpha(ease * scrim)
            .scale(0.7f + 0.3f * ease),
    ) {
        Box(
            modifier = Modifier.size(64.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(44.dp))
        }
        Text(
            event.label,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = Spacing.lg),
        )
    }
}

// ---------------------------------------------------------------- Qatar flag

private val Maroon = Color(0xFF8A1538)

/**
 * The flag flies in from the left (REMOVED: flies out to the right at the
 * end), waving the whole time, with a soft shadow and lit ridges.
 */
private fun DrawScope.drawFlagScene(t: Float, kind: CelebrationKind) {
    val w = size.width
    val flagW = w * 0.74f
    val flagH = flagW * 11f / 28f
    val centerY = size.height * 0.40f

    // Entry: 0..0.3 ease-out from off-screen left. Exit: 0.78..1.
    val enter = (t / 0.3f).coerceIn(0f, 1f)
    val enterEase = 1f - (1f - enter) * (1f - enter) * (1f - enter)
    val exit = ((t - 0.78f) / 0.22f).coerceIn(0f, 1f)
    val exitEase = exit * exit
    val startX = -flagW
    val restX = (w - flagW) / 2f
    val x = when (kind) {
        CelebrationKind.REMOVED -> restX + (startX * -1f + flagW) * exitEase * 1.3f
        else -> restX
    }.let { base -> if (enter < 1f) startX + (restX - startX) * enterEase else base }
    val y = centerY - flagH / 2f - (if (kind == CelebrationKind.REMOVED) 0f else 60f * exitEase)
    val alpha = if (kind == CelebrationKind.REMOVED) 1f - exitEase * 0.6f else 1f - exitEase
    val tilt = -8f * (1f - enterEase) + (if (kind == CelebrationKind.REMOVED) 10f * exitEase else 0f)
    val scale = 0.85f + 0.15f * enterEase
    val phase = t * 2f * PI.toFloat() * 3.2f
    val amp = flagH * (0.07f + 0.05f * (1f - enterEase))

    rotate(tilt, pivot = Offset(x + flagW / 2f, y + flagH / 2f)) {
        // Shadow.
        drawWavingFlag(Offset(x + 10f, y + 18f), flagW * scale, flagH * scale, phase, amp, alpha * 0.35f, shadow = true)
        drawWavingFlag(Offset(x, y), flagW * scale, flagH * scale, phase, amp, alpha, shadow = false)
    }
    // Wind streaks behind the flag while it flies.
    if (enter < 1f || kind == CelebrationKind.REMOVED && exit > 0f) {
        val n = 7
        for (i in 0 until n) {
            val sy = y + flagH * (0.1f + 0.8f * i / n)
            val len = flagW * (0.25f + 0.1f * (i % 3))
            val sx = x - len * 0.4f - 30f
            drawLine(
                Color.White.copy(alpha = 0.35f * alpha * (if (enter < 1f) 1f - enterEase else exitEase)),
                Offset(sx, sy), Offset(sx + len * 0.6f, sy), strokeWidth = 3f,
            )
        }
    }
}

/** Draws the Qatar flag as waving vertical strips with the nine-point serration on the hoist. */
private fun DrawScope.drawWavingFlag(
    topLeft: Offset, flagW: Float, flagH: Float, phase: Float, amp: Float, alpha: Float, shadow: Boolean,
) {
    val strips = 72
    val sw = flagW / strips
    val band = flagW * 0.30f      // white hoist band incl. teeth
    val depth = flagW * 0.085f    // tooth depth
    val teeth = 9
    val toothH = flagH / teeth
    for (i in 0 until strips) {
        val u = i.toFloat() / strips
        val x = topLeft.x + i * sw
        val wave = phase + u * 2f * PI.toFloat() * 1.5f
        val yOff = amp * sin(wave) * (0.35f + 0.65f * u) // the hoist moves less than the fly
        // Lit ridges from the wave, plus a soft darkening toward the fly edge.
        val light = ((1f + 0.22f * cos(wave)) * (1f - 0.12f * ((u - 0.85f) / 0.15f).coerceIn(0f, 1f))).coerceIn(0.65f, 1.25f)
        val top = topLeft.y + yOff
        val maroon = if (shadow) Color.Black else shade(Maroon, light)
        val white = if (shadow) Color.Black else shade(Color.White, light).copy(alpha = alpha)
        drawRect(maroon.copy(alpha = alpha), Offset(x, top), Size(sw + 1.5f, flagH))
        val fromBand = x - topLeft.x
        if (fromBand < band - depth) {
            drawRect(white, Offset(x, top), Size(sw + 1.5f, flagH))
        } else if (fromBand < band) {
            val tt = ((fromBand - (band - depth)) / depth).coerceIn(0f, 1f)
            for (k in 0 until teeth) {
                val y0 = top + k * toothH + tt * toothH / 2f
                val y1 = top + (k + 1) * toothH - tt * toothH / 2f
                if (y1 > y0) drawRect(white, Offset(x, y0), Size(sw + 1.5f, y1 - y0))
            }
        }
    }
}

private fun shade(c: Color, factor: Float): Color =
    Color((c.red * factor).coerceIn(0f, 1f), (c.green * factor).coerceIn(0f, 1f), (c.blue * factor).coerceIn(0f, 1f), c.alpha)

// ------------------------------------------------------------------ confetti

private class Particle(
    val angle: Float, val speed: Float, val size: Float, val color: Color, val spin: Float, val drift: Float, val delay: Float,
)

private fun particles(seed: Long, palette: AccentPalette, kind: CelebrationKind): List<Particle> {
    val rnd = Random(seed)
    val colors = palette.gradient + listOf(Color.White, palette.accentLight)
    return List(110) {
        val a = rnd.nextFloat() * 2f * PI.toFloat()
        Particle(
            angle = if (kind == CelebrationKind.REMOVED) a else a,
            speed = 0.45f + rnd.nextFloat() * 0.9f,
            size = 8f + rnd.nextFloat() * 16f,
            color = colors[rnd.nextInt(colors.size)],
            spin = (rnd.nextFloat() - 0.5f) * 1400f,
            drift = (rnd.nextFloat() - 0.5f) * 0.4f,
            delay = rnd.nextFloat() * 0.12f,
        )
    }
}

/**
 * Confetti bursts from the centre, arcs under gravity and fades. REMOVED
 * pulls the pieces inward and down instead, like sweeping up.
 */
private fun DrawScope.drawConfettiScene(t: Float, kind: CelebrationKind, palette: AccentPalette, seed: Long) {
    val ps = particles(seed, palette, kind)
    val cx = size.width / 2f
    val cy = size.height * 0.42f
    val reach = size.width * 0.9f
    for (p in ps) {
        val lt = ((t - p.delay) / (1f - p.delay)).coerceIn(0f, 1f)
        if (lt <= 0f) continue
        val ease = 1f - (1f - lt) * (1f - lt)
        val (px, py) = if (kind == CelebrationKind.REMOVED) {
            // Start spread out, converge to a point below centre.
            val sx = cx + cos(p.angle) * reach * 0.6f * p.speed
            val sy = cy + sin(p.angle) * reach * 0.6f * p.speed
            val tx = cx
            val ty = cy + size.height * 0.25f
            (sx + (tx - sx) * ease) to (sy + (ty - sy) * ease)
        } else {
            val dist = reach * p.speed * ease
            val gravity = size.height * 0.55f * lt * lt
            (cx + cos(p.angle) * dist + p.drift * size.width * lt) to (cy + sin(p.angle) * dist * 0.75f + gravity)
        }
        val alpha = (1f - lt).coerceIn(0f, 1f).let { if (lt < 0.1f) lt / 0.1f else it }
        rotate(p.spin * lt, pivot = Offset(px, py)) {
            drawRect(p.color.copy(alpha = alpha), Offset(px - p.size / 2f, py - p.size / 4f), Size(p.size, p.size / 2f))
        }
    }
    // Centre ring pulse.
    val ring = (t / 0.5f).coerceIn(0f, 1f)
    val ringEase = 1f - (1f - ring) * (1f - ring)
    drawCircle(
        color = palette.accentLight.copy(alpha = (1f - ringEase) * 0.5f),
        radius = 40f + ringEase * size.width * 0.45f,
        center = Offset(cx, cy),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 6f * (1f - ringEase) + 1f),
    )
}
