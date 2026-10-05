package com.shahaabapps.twotracks.feature.auth

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
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.theme.AppTheme
import kotlin.math.PI
import kotlin.math.sin

/** Which field the person is in; drives the mascot's eyes. */
enum class FieldFocus { NONE, EMAIL, PASSWORD, CONFIRM }

/**
 * What the face should express. [lookX] runs -1..1 across the email as it is
 * typed; a password field closes the eyes unless the text is shown, in which
 * case one eye peeks; [mood] is -1 for a mismatch or error, 0 neutral, 1 happy.
 */
data class MascotState(
    val focus: FieldFocus = FieldFocus.NONE,
    val lookX: Float = 0f,
    val passwordShown: Boolean = false,
    val mood: Float = 0f,
    /** Nothing typed yet: narrowed eyes, one brow up, a smirk. */
    val suspicious: Boolean = false,
    /** The email field has focus: the companion comes down to the card and rests on the toggle. */
    val near: Boolean = false,
)

/**
 * A round companion drawn in the theme gradient: eyes that follow the email,
 * eyelids that drop for passwords, a sneaky peek when the password is shown,
 * a blink every few seconds and a mouth that smiles or flattens.
 */
@Composable
fun Mascot(state: MascotState, modifier: Modifier = Modifier) {
    val secret = state.focus == FieldFocus.PASSWORD || state.focus == FieldFocus.CONFIRM
    val closed by animateFloatAsState(if (secret) 1f else 0f, spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessMedium), label = "closed")
    val peek by animateFloatAsState(if (secret && state.passwordShown) 1f else 0f, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow), label = "peek")
    val lookX by animateFloatAsState(if (state.focus == FieldFocus.EMAIL) state.lookX else 0f, spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMediumLow), label = "lookX")
    val lookY by animateFloatAsState(if (state.focus == FieldFocus.NONE) 0f else 0.55f, spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMediumLow), label = "lookY")
    val mood by animateFloatAsState(state.mood, tween(350), label = "mood")
    val suspicion by animateFloatAsState(if (state.suspicious) 1f else 0f, tween(400), label = "sus")
    // Turns away a little while a password is typed, leans toward the email as it grows.
    val tilt by animateFloatAsState(if (secret && !state.passwordShown) 14f else lookX * 9f, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow), label = "tilt")
    val driftX by animateFloatAsState(if (state.focus == FieldFocus.EMAIL) state.lookX * 0.5f else 0f, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow), label = "drift")
    // A happy hop whenever the mood reaches 1.
    val hop = remember { Animatable(0f) }
    LaunchedEffect(state.mood >= 1f) {
        if (state.mood >= 1f) { hop.snapTo(0f); hop.animateTo(1f, keyframes { durationMillis = 650; 0f at 0; -1f at 220; 0.3f at 450; 0f at 650 }) }
    }
    // Flies in from the top-left on first open.
    val arrive = remember { Animatable(0f) }
    LaunchedEffect(Unit) { arrive.animateTo(1f, spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessLow)) }

    val transition = rememberInfiniteTransition(label = "mascot")
    val blink by transition.animateFloat(
        0f, 0f,
        infiniteRepeatable(keyframes { durationMillis = 4300; 0f at 0; 0f at 3950; 1f at 4040; 1f at 4090; 0f at 4200 }, RepeatMode.Restart),
        label = "blink",
    )
    val hover by transition.animateFloat(0f, 1f, infiniteRepeatable(tween(1400, easing = LinearEasing), RepeatMode.Restart), label = "hover")
    val flap by transition.animateFloat(0f, 1f, infiniteRepeatable(tween(190, easing = LinearEasing), RepeatMode.Restart), label = "flap")
    val gradient = AppTheme.colors.gradient
    val lid = gradient.first()
    Canvas(modifier = modifier) {
        val r = size.height / 2f * 0.72f
        val bob = sin(hover * 2f * PI.toFloat()) * r * 0.12f
        val dx = driftX * (size.width / 2f - r * 1.6f) + (1f - arrive.value) * -size.width * 0.9f
        val dy = bob + hop.value * r * 0.9f + (1f - arrive.value) * -size.height * 1.2f
        val c = Offset(size.width / 2f + dx, size.height / 2f + dy)
        val flapAngle = sin(flap * 2f * PI.toFloat())
        withTransform({ rotate(tilt, pivot = c) }) {
            drawWings(c, r, flapAngle, lid)
            drawTrail(c, r, hover, lid, tilt)
            drawFace(c, r, gradient, lid, lookX, lookY, maxOf(closed, blink), peek, mood, suspicion)
            drawTuft(c, r, lid)
        }
    }
}

/** Two small wings behind the body, flapping fast like a hummingbird. */
private fun DrawScope.drawWings(c: Offset, r: Float, flap: Float, accent: Color) {
    listOf(-1f, 1f).forEach { side ->
        val pivot = Offset(c.x + side * r * 0.82f, c.y + r * 0.05f)
        val angle = side * (-18f + 34f * flap)
        withTransform({ rotate(angle, pivot = pivot) }) {
            val w = r * 0.78f
            val h = r * 0.42f
            val rect = Rect(pivot.x + (if (side > 0) 0f else -w), pivot.y - h / 2f, pivot.x + (if (side > 0) w else 0f), pivot.y + h / 2f)
            drawOval(Brush.horizontalGradient(listOf(Color.White, Color(0xFFF6E3EA)), startX = rect.left, endX = rect.right), topLeft = rect.topLeft, size = rect.size)
            drawOval(accent.copy(alpha = 0.4f), topLeft = rect.topLeft, size = rect.size, style = Stroke(width = r * 0.04f))
            // Feather line.
            drawLine(accent.copy(alpha = 0.25f), Offset(pivot.x, pivot.y), Offset(pivot.x + side * w * 0.85f, pivot.y), strokeWidth = r * 0.03f, cap = StrokeCap.Round)
        }
    }
}

/** Little sparkles trailing below, fading as they fall. */
private fun DrawScope.drawTrail(c: Offset, r: Float, hover: Float, accent: Color, tilt: Float) {
    for (i in 0 until 4) {
        val k = ((hover + i * 0.25f) % 1f)
        val x = c.x - tilt / 9f * r * 0.3f * (1 + i) * 0.3f + (if (i % 2 == 0) -1f else 1f) * r * 0.25f * k
        val y = c.y + r * (1.05f + k * 0.9f)
        drawCircle(accent.copy(alpha = (1f - k) * 0.35f), r * 0.07f * (1f - k) + 1f, Offset(x, y))
    }
}

/** Three soft hairs on top. */
private fun DrawScope.drawTuft(c: Offset, r: Float, accent: Color) {
    listOf(-0.18f, 0f, 0.18f).forEachIndexed { i, dx ->
        val base = Offset(c.x + dx * r, c.y - r * 0.9f)
        val tip = Offset(c.x + dx * r * 2.2f, c.y - r * (1.18f + 0.06f * (1 - (i - 1).let { it * it })))
        val p = Path().apply { moveTo(base.x, base.y); quadraticTo(base.x + dx * r * 0.6f, base.y - r * 0.2f, tip.x, tip.y) }
        drawPath(p, Color(0xFFFBF1F4), style = Stroke(width = r * 0.08f, cap = StrokeCap.Round))
        drawPath(p, accent.copy(alpha = 0.35f), style = Stroke(width = r * 0.025f, cap = StrokeCap.Round))
    }
}

private fun DrawScope.drawFace(
    c: Offset, r: Float, gradient: List<Color>, lid: Color,
    lookX: Float, lookY: Float, closed: Float, peek: Float, mood: Float, suspicion: Float = 0f,
) {
    val accent = gradient.first()
    val body = Rect(c.x - r, c.y - r * 0.94f, c.x + r, c.y + r * 0.98f)
    // Jelly body: cream white with a blush-tinted underside and a thin accent ring.
    drawOval(Brush.verticalGradient(listOf(Color.White, Color(0xFFFBF1F4), Color(0xFFF2DCE4)), startY = body.top, endY = body.bottom), topLeft = body.topLeft, size = body.size)
    drawOval(Brush.radialGradient(listOf(Color.White, Color.White.copy(alpha = 0f)), center = Offset(c.x - r * 0.3f, c.y - r * 0.45f), radius = r * 0.8f), topLeft = body.topLeft, size = body.size)
    drawOval(accent.copy(alpha = 0.35f), topLeft = body.topLeft, size = body.size, style = Stroke(width = r * 0.045f))

    drawExpression(c, r, accent, lookX, lookY, closed, peek, mood, suspicion = suspicion)
}

/**
 * Eyes, brows, cheeks, mouth and the happy sparkle, shared by every companion.
 * [suspicion] narrows the eyes, raises one brow, slides the gaze sideways and
 * turns the mouth into a smirk. [lashes] and [lipTint] add a feminine finish;
 * [browWeight] thickens the brows.
 */
internal fun DrawScope.drawExpression(
    c: Offset, r: Float, accent: Color,
    lookX: Float, lookY: Float, closed: Float, peek: Float, mood: Float,
    suspicion: Float = 0f, lashes: Boolean = false, lipTint: Color? = null, browWeight: Float = 1f,
) {
    val ink = Color(0xFF2A2630)
    val eyeDx = r * 0.36f
    val eyeY = c.y - r * 0.08f
    val eyeR = r * (0.21f + 0.03f * mood.coerceAtLeast(0f) - 0.03f * (-mood).coerceAtLeast(0f))
    // A sideways glance when suspicious and no other gaze is set.
    val sideGlance = if (lookX == 0f) 0.65f * suspicion else 0f
    val gazeX = (lookX + sideGlance) * r * 0.07f
    val gazeY = lookY * r * 0.06f
    listOf(-1f, 1f).forEachIndexed { i, side ->
        val ex = c.x + side * eyeDx + gazeX
        val ey = eyeY + gazeY
        // The right eye winks open for a peek.
        val thisClosed = if (i == 1) closed * (1f - peek * 0.85f) else closed
        if (thisClosed < 0.6f) {
            val open = 1f - thisClosed / 0.6f
            val rx = eyeR
            // Narrowed when suspicious; the eye nearer the glance narrows more.
            val squint = suspicion * (if (i == 1) 0.5f else 0.38f)
            val ry = eyeR * (0.35f + 0.65f * open) * (1f - squint)
            drawOval(ink, topLeft = Offset(ex - rx, ey - ry), size = Size(rx * 2, ry * 2))
            drawCircle(Color.White, eyeR * 0.36f * (1f - 0.3f * suspicion), Offset(ex - eyeR * 0.3f + gazeX * 0.4f, ey - ry * 0.35f))
            drawCircle(Color.White.copy(alpha = 0.85f), eyeR * 0.14f, Offset(ex + eyeR * 0.35f + gazeX * 0.4f, ey + ry * 0.3f))
            if (lashes) {
                for (k in 0 until 3) {
                    val a = Math.toRadians((-70.0 + k * 22.0) * side + (if (side > 0) 0.0 else 0.0)).toFloat()
                    val base = Offset(ex + side * rx * (0.6f + 0.15f * k), ey - ry * (0.95f - 0.25f * k))
                    val tip = Offset(base.x + side * r * 0.11f, base.y - r * (0.1f - 0.025f * k))
                    drawLine(ink, base, tip, strokeWidth = r * 0.035f, cap = StrokeCap.Round)
                }
            }
        } else {
            val p = Path().apply {
                moveTo(ex - eyeR, ey + eyeR * 0.25f)
                quadraticTo(ex, ey - eyeR * 0.75f, ex + eyeR, ey + eyeR * 0.25f)
            }
            drawPath(p, ink, style = Stroke(width = r * 0.065f, cap = StrokeCap.Round))
        }
    }
    // Brows: soft arcs; when suspicious the right one lifts and the left one drops in.
    listOf(-1f, 1f).forEach { side ->
        val bx = c.x + side * eyeDx + gazeX * 0.5f
        val lift = if (side > 0) suspicion * r * 0.12f else -suspicion * r * 0.05f
        val tiltIn = if (side < 0) suspicion * r * 0.08f else 0f
        val by = eyeY - eyeR * 1.45f - lift - r * 0.04f * mood.coerceAtLeast(0f)
        val p = Path().apply {
            moveTo(bx - eyeR * 0.95f, by + r * 0.03f + (if (side < 0) 0f else tiltIn))
            quadraticTo(bx, by - r * 0.06f * (1f - suspicion * 0.5f), bx + eyeR * 0.95f, by + r * 0.03f + (if (side < 0) tiltIn else 0f))
        }
        drawPath(p, ink.copy(alpha = 0.85f), style = Stroke(width = r * 0.045f * browWeight, cap = StrokeCap.Round))
    }
    // Cheeks.
    listOf(-1f, 1f).forEach { side ->
        val a = (0.18f + 0.22f * mood.coerceAtLeast(0f)) * (1f - 0.5f * suspicion)
        drawCircle(accent.copy(alpha = a), r * 0.13f, Offset(c.x + side * r * 0.56f, c.y + r * 0.24f))
    }
    // Mouth.
    val my = c.y + r * 0.42f
    if (mood < -0.3f) {
        val k = (-mood).coerceIn(0f, 1f)
        drawOval(ink, topLeft = Offset(c.x - r * 0.08f * k - r * 0.02f, my - r * 0.09f * k), size = Size(r * 0.16f * k + r * 0.04f, r * 0.18f * k + r * 0.02f))
    } else {
        val happy = mood.coerceIn(0f, 1f) * (1f - suspicion)
        val mw = r * (0.16f + 0.2f * happy)
        val curve = r * (0.08f + 0.22f * happy) * (1f - suspicion)
        // Smirk: flat line with the right corner lifted.
        val smirkLift = suspicion * r * 0.07f
        val mouth = Path().apply {
            moveTo(c.x - mw, my - curve * 0.35f + smirkLift * 0.3f)
            quadraticTo(c.x + smirkLift, my + curve, c.x + mw, my - curve * 0.35f - smirkLift)
        }
        drawPath(mouth, ink, style = Stroke(width = r * 0.06f, cap = StrokeCap.Round))
        lipTint?.let { tint ->
            val lp = Path().apply {
                moveTo(c.x - mw * 0.8f, my + curve * 0.25f + r * 0.02f)
                quadraticTo(c.x, my + curve + r * 0.1f, c.x + mw * 0.8f, my + curve * 0.25f + r * 0.02f)
                close()
            }
            drawPath(lp, tint.copy(alpha = 0.55f * (1f - suspicion * 0.4f)))
        }
        if (happy > 0.6f) {
            val fill = Path().apply {
                moveTo(c.x - mw * 0.8f, my)
                quadraticTo(c.x, my + curve * 0.95f, c.x + mw * 0.8f, my)
                close()
            }
            drawPath(fill, ink.copy(alpha = (happy - 0.6f) / 0.4f))
        }
    }
    if (mood > 0.7f && suspicion < 0.5f) {
        val k = (mood - 0.7f) / 0.3f
        val sx = c.x + r * 0.78f
        val sy = c.y - r * 0.72f
        val len = r * 0.14f * k
        drawLine(accent, Offset(sx - len, sy), Offset(sx + len, sy), strokeWidth = r * 0.035f, cap = StrokeCap.Round)
        drawLine(accent, Offset(sx, sy - len), Offset(sx, sy + len), strokeWidth = r * 0.035f, cap = StrokeCap.Round)
    }
}

/** Where the eyes should look while the email is typed: -1 at the start, 1 past ~28 characters. */
fun emailLook(email: String): Float = ((email.length / 28f) * 2f - 1f).coerceIn(-1f, 1f)

@Suppress("unused")
private fun wobble(t: Float): Float = sin(t * 2f * PI.toFloat())

val MascotSize = 96.dp
val MascotWidth = 200.dp
