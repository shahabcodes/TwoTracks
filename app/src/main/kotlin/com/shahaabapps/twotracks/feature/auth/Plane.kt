package com.shahaabapps.twotracks.feature.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import com.shahaabapps.core.ui.theme.AppTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** The companions a person can pick for the sign-in screen. */
enum class CompanionKind(val label: String) { BUDDY("Buddy"), PLANE("Plane"), SANYA("Sanya"), USAID("Usaid") }

/** Picks the companion by [kind]; both react to the same [MascotState]. */
@Composable
fun Companion(kind: CompanionKind, state: MascotState, modifier: Modifier = Modifier) {
    when (kind) {
        CompanionKind.BUDDY -> Mascot(state, modifier)
        CompanionKind.PLANE -> PlaneCompanion(state, modifier)
        CompanionKind.SANYA -> PersonCompanion(Person.SANYA, state, modifier)
        CompanionKind.USAID -> PersonCompanion(Person.USAID, state, modifier)
    }
}

/**
 * A small cartoon plane with a face in the windshield. It banks toward the
 * email as it is typed, closes its eyes for passwords, winks when a password
 * is shown, flies in from the left on open and pulls a happy climb when the
 * form is ready. The propeller never stops.
 */
@Composable
fun PlaneCompanion(state: MascotState, modifier: Modifier = Modifier) {
    val secret = state.focus == FieldFocus.PASSWORD || state.focus == FieldFocus.CONFIRM
    val closed by animateFloatAsState(if (secret) 1f else 0f, spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessMedium), label = "closed")
    val peek by animateFloatAsState(if (secret && state.passwordShown) 1f else 0f, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow), label = "peek")
    val lookX by animateFloatAsState(if (state.focus == FieldFocus.EMAIL) state.lookX else 0f, spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMediumLow), label = "lookX")
    val lookY by animateFloatAsState(if (state.focus == FieldFocus.NONE) 0f else 0.55f, spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMediumLow), label = "lookY")
    val mood by animateFloatAsState(state.mood, tween(350), label = "mood")
    // Banks with the gaze; pitches away a little while a password is typed.
    val bank by animateFloatAsState(if (secret && !state.passwordShown) -10f else lookX * 12f, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow), label = "bank")
    val driftX by animateFloatAsState(if (state.focus == FieldFocus.EMAIL) state.lookX * 0.45f else 0f, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow), label = "drift")
    val climb = remember { Animatable(0f) }
    LaunchedEffect(state.mood >= 1f) {
        if (state.mood >= 1f) { climb.snapTo(0f); climb.animateTo(1f, keyframes { durationMillis = 800; 0f at 0; 1f at 260; 0.2f at 560; 0f at 800 }) }
    }
    val arrive = remember { Animatable(0f) }
    LaunchedEffect(Unit) { arrive.animateTo(1f, spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessLow)) }

    val transition = rememberInfiniteTransition(label = "plane")
    val blink by transition.animateFloat(
        0f, 0f,
        infiniteRepeatable(keyframes { durationMillis = 4300; 0f at 0; 0f at 3950; 1f at 4040; 1f at 4090; 0f at 4200 }, RepeatMode.Restart),
        label = "blink",
    )
    val hover by transition.animateFloat(0f, 1f, infiniteRepeatable(tween(1600, easing = LinearEasing), RepeatMode.Restart), label = "hover")
    val prop by transition.animateFloat(0f, 1f, infiniteRepeatable(tween(120, easing = LinearEasing), RepeatMode.Restart), label = "prop")
    val gradient = AppTheme.colors.gradient
    Canvas(modifier = modifier) {
        val r = size.height / 2f * 0.62f
        val bob = sin(hover * 2f * PI.toFloat()) * r * 0.14f
        val dx = driftX * (size.width / 2f - r * 2.2f) + (1f - arrive.value) * -size.width
        val dy = bob - climb.value * r * 0.9f
        val c = Offset(size.width / 2f + dx, size.height / 2f + dy)
        val pitch = bank - climb.value * 18f
        withTransform({ rotate(pitch, pivot = c) }) {
            drawContrail(c, r, hover, gradient.first())
            drawPlaneBody(c, r, gradient, prop)
            // Face lives in the windshield, slightly forward of centre.
            drawExpression(Offset(c.x + r * 0.55f, c.y - r * 0.12f), r * 0.62f, gradient.first(), lookX, lookY, maxOf(closed, blink), peek, mood)
        }
    }
}

private fun DrawScope.drawPlaneBody(c: Offset, r: Float, gradient: List<Color>, prop: Float) {
    val accent = gradient.first()
    val deep = gradient.last()
    val cream = Color(0xFFFBF1F4)
    // Far wing (behind), darker.
    val farWing = Path().apply {
        moveTo(c.x - r * 0.2f, c.y - r * 0.2f)
        lineTo(c.x + r * 0.6f, c.y - r * 1.25f)
        lineTo(c.x + r * 1.05f, c.y - r * 1.2f)
        lineTo(c.x + r * 0.5f, c.y - r * 0.2f)
        close()
    }
    drawPath(farWing, deep.copy(alpha = 0.85f))
    // Tail fin.
    val fin = Path().apply {
        moveTo(c.x - r * 1.35f, c.y - r * 0.25f)
        lineTo(c.x - r * 1.75f, c.y - r * 1.05f)
        lineTo(c.x - r * 1.2f, c.y - r * 1.0f)
        lineTo(c.x - r * 0.85f, c.y - r * 0.25f)
        close()
    }
    drawPath(fin, accent)
    drawPath(fin, Color.White.copy(alpha = 0.5f), style = Stroke(width = r * 0.04f))
    // Fuselage: a long rounded body with a cream top and accent belly.
    val body = Rect(c.x - r * 1.75f, c.y - r * 0.5f, c.x + r * 1.75f, c.y + r * 0.5f)
    drawRoundRect(Brush.verticalGradient(listOf(Color.White, cream, Color(0xFFF2DCE4)), startY = body.top, endY = body.bottom), topLeft = body.topLeft, size = body.size, cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.5f))
    // Belly stripe.
    val belly = Path().apply {
        addRoundRect(androidx.compose.ui.geometry.RoundRect(Rect(body.left, c.y + r * 0.18f, body.right, body.bottom), androidx.compose.ui.geometry.CornerRadius(r * 0.5f)))
    }
    drawPath(belly, accent.copy(alpha = 0.9f))
    drawRoundRect(accent.copy(alpha = 0.4f), topLeft = body.topLeft, size = body.size, cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.5f), style = Stroke(width = r * 0.045f))
    // Windshield: a dark glass dome on the front top.
    val glass = Path().apply {
        moveTo(c.x + r * 0.05f, c.y - r * 0.05f)
        quadraticTo(c.x + r * 0.55f, c.y - r * 1.05f, c.x + r * 1.15f, c.y - r * 0.05f)
        close()
    }
    drawPath(glass, Brush.verticalGradient(listOf(Color(0xFFEAF3FF), Color(0xFFCFE0F7)), startY = c.y - r, endY = c.y))
    drawPath(glass, accent.copy(alpha = 0.45f), style = Stroke(width = r * 0.04f))
    // Near wing (front), swept back and down.
    val wing = Path().apply {
        moveTo(c.x - r * 0.1f, c.y + r * 0.05f)
        lineTo(c.x + r * 0.45f, c.y + r * 1.05f)
        lineTo(c.x + r * 1.0f, c.y + r * 1.0f)
        lineTo(c.x + r * 0.75f, c.y + r * 0.05f)
        close()
    }
    drawPath(wing, Brush.linearGradient(listOf(accent, deep), start = Offset(c.x, c.y), end = Offset(c.x + r, c.y + r)))
    drawPath(wing, Color.White.copy(alpha = 0.5f), style = Stroke(width = r * 0.04f))
    // Nose cone and propeller.
    val nose = Offset(c.x + r * 1.75f, c.y)
    drawCircle(accent, r * 0.22f, nose)
    val hub = Offset(nose.x + r * 0.12f, nose.y)
    val angle = prop * 2f * PI.toFloat()
    drawCircle(Color(0xFF2A2630).copy(alpha = 0.06f), r * 0.62f, hub)
    for (i in 0 until 3) {
        val a = angle + i * 2f * PI.toFloat() / 3f
        val len = r * 0.6f
        drawLine(Color(0xFF2A2630).copy(alpha = 0.55f), hub, Offset(hub.x + cos(a) * len * 0.15f, hub.y + sin(a) * len), strokeWidth = r * 0.12f, cap = StrokeCap.Round)
    }
    drawCircle(Color(0xFF2A2630), r * 0.09f, hub)
}

/** Puffs behind the tail, drifting back and fading. */
private fun DrawScope.drawContrail(c: Offset, r: Float, hover: Float, accent: Color) {
    for (i in 0 until 5) {
        val k = ((hover + i * 0.2f) % 1f)
        val x = c.x - r * (1.9f + k * 2.2f)
        val y = c.y + r * 0.15f + sin(k * 6f + i) * r * 0.12f
        drawCircle(Color.White.copy(alpha = (1f - k) * 0.55f), r * (0.16f * (1f - k) + 0.04f), Offset(x, y))
        drawCircle(accent.copy(alpha = (1f - k) * 0.25f), r * (0.16f * (1f - k) + 0.04f), Offset(x, y), style = Stroke(width = 1.5f))
    }
}
