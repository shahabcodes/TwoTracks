package com.shahaabapps.twotracks.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.shahaabapps.core.ui.theme.AppTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * The app's living backdrop: three aurora blobs on slow Lissajous paths, a
 * field of twinkling points and a faint perspective grid drifting upward,
 * all in the current theme's colours.
 *
 * @param sky draw the deep gradient base that fades into the grouped
 *   background over [skyFraction] of the height (the auth screen); false
 *   overlays the light on whatever is behind (the Today hero).
 */
@Composable
fun AuroraLayer(
    modifier: Modifier = Modifier,
    sky: Boolean = true,
    skyFraction: Float = 0.62f,
    stars: Boolean = true,
    grid: Boolean = true,
    intensity: Float = 1f,
    seed: Int = 7,
) {
    val transition = rememberInfiniteTransition(label = "aurora")
    val t by transition.animateFloat(0f, 1f, infiniteRepeatable(tween(16000, easing = LinearEasing), RepeatMode.Restart), label = "t")
    val twinkle by transition.animateFloat(0f, 1f, infiniteRepeatable(tween(2600, easing = LinearEasing), RepeatMode.Reverse), label = "tw")
    val gradient = AppTheme.colors.gradient
    val accent = AppTheme.colors.accent
    val base = AppTheme.colors.groupedBackground
    val points = remember(seed) { val r = Random(seed); List(70) { Triple(r.nextFloat(), r.nextFloat(), r.nextFloat()) } }
    Canvas(modifier = modifier) {
        val skyH = if (sky) size.height * skyFraction else size.height
        if (sky) drawRect(Brush.verticalGradient(listOf(gradient.last(), gradient[gradient.size / 2], base), startY = 0f, endY = skyH * 1.15f))
        val phase = t * 2f * PI.toFloat()
        val blobs = listOf(
            Triple(0.25f + 0.18f * sin(phase), 0.22f + 0.08f * cos(phase * 1.3f), gradient.first()),
            Triple(0.75f + 0.15f * cos(phase * 0.8f), 0.35f + 0.1f * sin(phase * 1.1f), accent),
            Triple(0.5f + 0.25f * sin(phase * 0.6f + 1f), 0.08f + 0.06f * cos(phase * 0.9f), Color.White),
        )
        blobs.forEachIndexed { i, (fx, fy, c) ->
            val center = Offset(size.width * fx, skyH * fy)
            val radius = size.width * (0.45f + 0.1f * i)
            val a = (if (i == 2) 0.18f else 0.42f) * intensity
            drawCircle(Brush.radialGradient(listOf(c.copy(alpha = a), c.copy(alpha = 0f)), center = center, radius = radius), radius = radius, center = center)
        }
        if (grid) {
            val lines = 9
            for (i in 0..lines) {
                val f = ((i.toFloat() / lines) + t) % 1f
                val y = skyH * (0.35f + 0.65f * f * f)
                drawLine(Color.White.copy(alpha = 0.06f * f * intensity), Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
            }
            for (i in 0..8) {
                val x0 = size.width * (i / 8f)
                val x1 = size.width * 0.5f + (x0 - size.width * 0.5f) * 0.25f
                drawLine(Color.White.copy(alpha = 0.05f * intensity), Offset(x0, skyH), Offset(x1, skyH * 0.35f), strokeWidth = 1f)
            }
        }
        if (stars) {
            points.forEach { (fx, fy, s) ->
                val a = 0.25f + 0.75f * ((sin((twinkle + s) * 2f * PI.toFloat())) * 0.5f + 0.5f)
                drawCircle(Color.White.copy(alpha = a * 0.7f * intensity), 1.2f + s * 1.6f, Offset(size.width * fx, skyH * (if (sky) fy * 0.6f else fy)))
            }
        }
    }
}
