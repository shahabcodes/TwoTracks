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
import androidx.compose.ui.geometry.CornerRadius
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
import com.shahaabapps.core.ui.theme.AppTheme
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/** Which full-body person to draw. */
enum class Person { SANYA, USAID }

/**
 * Anime-style chibi people: a big head where tousled hair (or the hijab)
 * takes the top half and a small face with huge eyes sits low, on a short
 * body. They wave on arrival, cover their eyes with both hands for a
 * password, peek between the fingers when it is shown, lean toward the email
 * while it is typed, rest on the toggle when the email has focus, and jump
 * with arms up when the form is ready.
 */
@Composable
fun PersonCompanion(person: Person, state: MascotState, modifier: Modifier = Modifier) {
    val secret = state.focus == FieldFocus.PASSWORD || state.focus == FieldFocus.CONFIRM
    val closed by animateFloatAsState(if (secret) 1f else 0f, spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessMedium), label = "closed")
    val peek by animateFloatAsState(if (secret && state.passwordShown) 1f else 0f, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow), label = "peek")
    val cover by animateFloatAsState(if (secret) 1f else 0f, spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow), label = "cover")
    val lookX by animateFloatAsState(if (state.focus == FieldFocus.EMAIL) state.lookX else 0f, spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMediumLow), label = "lookX")
    val lookY by animateFloatAsState(if (state.focus == FieldFocus.NONE) 0f else 0.55f, spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMediumLow), label = "lookY")
    val mood by animateFloatAsState(state.mood, tween(350), label = "mood")
    val suspicion by animateFloatAsState(if (state.suspicious) 1f else 0f, tween(400), label = "sus")
    val near by animateFloatAsState(if (state.near) 1f else 0f, spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessLow), label = "near")
    val lean by animateFloatAsState(if (state.focus == FieldFocus.EMAIL) state.lookX * 7f else 0f, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow), label = "lean")
    val jump = remember { Animatable(0f) }
    LaunchedEffect(state.mood >= 1f) {
        if (state.mood >= 1f) { jump.snapTo(0f); jump.animateTo(1f, keyframes { durationMillis = 700; 0f at 0; 1f at 240; 0f at 520; 0.25f at 610; 0f at 700 }) }
    }
    val wave = remember { Animatable(0f) }
    LaunchedEffect(Unit) { wave.animateTo(1f, tween(1600, delayMillis = 500, easing = LinearEasing)) }
    val arrive = remember { Animatable(0f) }
    LaunchedEffect(Unit) { arrive.animateTo(1f, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow)) }

    val transition = rememberInfiniteTransition(label = "person")
    val blink by transition.animateFloat(
        0f, 0f,
        infiniteRepeatable(keyframes { durationMillis = 4300; 0f at 0; 0f at 3950; 1f at 4040; 1f at 4090; 0f at 4200 }, RepeatMode.Restart),
        label = "blink",
    )
    val breathe by transition.animateFloat(0f, 1f, infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Reverse), label = "breathe")
    val hairSway by transition.animateFloat(0f, 1f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Reverse), label = "hair")
    val gradient = AppTheme.colors.gradient
    Canvas(modifier = modifier) {
        val h = size.height
        val headR = h * 0.21f
        val groundY = h * 0.95f
        val bob = (breathe - 0.5f) * headR * 0.06f
        val lift = jump.value * headR * 1.1f
        val cx = size.width / 2f + (1f - arrive.value) * -size.width * 0.9f
        val feetY = groundY - lift
        val waveAngle = if (wave.value < 1f) sin(wave.value * PI.toFloat() * 5f) * 25f else 0f
        withTransform({ rotate(lean, pivot = Offset(cx, feetY)) }) {
            drawPerson(
                person, cx, feetY, headR, bob, gradient, waveAngle, cover, peek, jump.value,
                lookX, lookY, maxOf(closed, blink), mood, suspicion, near, (hairSway - 0.5f) * 2f,
            )
        }
    }
}

private class Look(val skin: Color, val hair: Color, val hairLight: Color, val top: Color, val bottom: Color, val shoes: Color, val iris: Color)

private fun DrawScope.drawPerson(
    person: Person, cx: Float, feetY: Float, headR: Float, bob: Float, gradient: List<Color>,
    waveAngle: Float, cover: Float, peek: Float, jump: Float, lookX: Float, lookY: Float, closed: Float, mood: Float,
    suspicion: Float, near: Float, sway: Float,
) {
    val accent = gradient.first()
    val look = when (person) {
        Person.SANYA -> Look(Color(0xFFF6D6C3), accent, Color.White, Color(0xFFFDF7F9), Color(0xFF3A3A4A), Color.White, Color(0xFF4A2A1E))
        Person.USAID -> Look(Color(0xFFF1C9A9), Color(0xFF7A4B2E), Color(0xFFC8905E), accent, Color(0xFF4F6FA3), Color(0xFF2A2630), Color(0xFF8A4B1F))
    }
    val ink = Color(0xFF2A2630)
    val legH = headR * 0.7f
    val bodyH = headR * 1.0f
    val bodyW = headR * 1.3f
    val bodyTop = feetY - legH - bodyH + bob
    val headC = Offset(cx, bodyTop - headR * 0.78f)

    // ---- legs and shoes
    listOf(-1f, 1f).forEach { side ->
        val lx = cx + side * headR * 0.3f
        val tuck = jump * headR * 0.25f
        val legW = if (person == Person.USAID) headR * 0.46f else headR * 0.34f
        drawRoundRect(look.bottom, Offset(lx - legW / 2f, feetY - legH + tuck), Size(legW, legH - tuck), CornerRadius(headR * 0.12f))
        if (person == Person.USAID) {
            drawRoundRect(Color(0xFF6E8BBE), Offset(lx - legW / 2f, feetY - headR * 0.38f + tuck), Size(legW, headR * 0.14f), CornerRadius(headR * 0.06f))
        }
        drawRoundRect(look.shoes, Offset(lx - headR * 0.22f, feetY - headR * 0.2f + tuck), Size(headR * 0.44f, headR * 0.22f), CornerRadius(headR * 0.1f))
        drawRoundRect(ink.copy(alpha = 0.25f), Offset(lx - headR * 0.22f, feetY - headR * 0.2f + tuck), Size(headR * 0.44f, headR * 0.22f), CornerRadius(headR * 0.1f), style = Stroke(width = 1.5f))
    }

    // ---- body
    if (person == Person.SANYA) {
        val hemY = feetY - headR * 0.18f
        val dress = Path().apply {
            moveTo(cx - bodyW * 0.4f, bodyTop)
            lineTo(cx + bodyW * 0.4f, bodyTop)
            quadraticTo(cx + bodyW * 0.3f, bodyTop + bodyH * 0.45f, cx + bodyW * 0.7f, hemY)
            lineTo(cx - bodyW * 0.7f, hemY)
            quadraticTo(cx - bodyW * 0.3f, bodyTop + bodyH * 0.45f, cx - bodyW * 0.4f, bodyTop)
            close()
        }
        drawPath(dress, Brush.verticalGradient(listOf(Color.White, look.top, Color(0xFFF2DCE4)), startY = bodyTop, endY = hemY))
        drawPath(dress, accent.copy(alpha = 0.35f), style = Stroke(width = headR * 0.04f))
        drawLine(accent, Offset(cx - bodyW * 0.68f, hemY - headR * 0.05f), Offset(cx + bodyW * 0.68f, hemY - headR * 0.05f), strokeWidth = headR * 0.1f)
        val drape = Path().apply {
            moveTo(cx - headR * 0.8f, bodyTop - headR * 0.15f)
            quadraticTo(cx, bodyTop + headR * 0.7f, cx + headR * 0.8f, bodyTop - headR * 0.15f)
            lineTo(cx + headR * 1.0f, bodyTop + headR * 0.35f)
            quadraticTo(cx, bodyTop + headR * 1.1f, cx - headR * 1.0f, bodyTop + headR * 0.35f)
            close()
        }
        drawPath(drape, Brush.verticalGradient(listOf(accent, gradient.last()), startY = bodyTop - headR * 0.1f, endY = bodyTop + headR * 1.2f))
    } else {
        // Hoodie: hood bunched behind the neck, body, kangaroo pocket, drawstrings, a tee at the neck.
        val hoodBack = Path().apply {
            moveTo(cx - headR * 0.95f, bodyTop + headR * 0.25f)
            quadraticTo(cx - headR * 0.9f, bodyTop - headR * 0.35f, cx, bodyTop - headR * 0.3f)
            quadraticTo(cx + headR * 0.9f, bodyTop - headR * 0.35f, cx + headR * 0.95f, bodyTop + headR * 0.25f)
            close()
        }
        drawPath(hoodBack, gradient.last())
        drawRoundRect(look.top, Offset(cx - bodyW * 0.52f, bodyTop), Size(bodyW * 1.04f, bodyH), CornerRadius(headR * 0.22f))
        drawRoundRect(Color.Black.copy(alpha = 0.12f), Offset(cx - bodyW * 0.52f, bodyTop), Size(bodyW * 1.04f, bodyH), CornerRadius(headR * 0.22f), style = Stroke(width = headR * 0.04f))
        val neck = Path().apply {
            moveTo(cx - headR * 0.36f, bodyTop)
            quadraticTo(cx, bodyTop + headR * 0.4f, cx + headR * 0.36f, bodyTop)
            close()
        }
        drawPath(neck, Color(0xFFFDF7F9))
        val rim = Path().apply {
            moveTo(cx - headR * 0.55f, bodyTop - headR * 0.05f)
            quadraticTo(cx, bodyTop + headR * 0.5f, cx + headR * 0.55f, bodyTop - headR * 0.05f)
        }
        drawPath(rim, Color.Black.copy(alpha = 0.18f), style = Stroke(width = headR * 0.1f, cap = StrokeCap.Round))
        listOf(-1f, 1f).forEach { side ->
            drawLine(Color(0xFFFDF7F9), Offset(cx + side * headR * 0.14f, bodyTop + headR * 0.18f), Offset(cx + side * headR * 0.17f, bodyTop + headR * 0.62f), strokeWidth = headR * 0.04f, cap = StrokeCap.Round)
            drawCircle(Color(0xFFFDF7F9), headR * 0.035f, Offset(cx + side * headR * 0.17f, bodyTop + headR * 0.64f))
        }
        drawRoundRect(Color.Black.copy(alpha = 0.1f), Offset(cx - bodyW * 0.3f, bodyTop + bodyH * 0.62f), Size(bodyW * 0.6f, bodyH * 0.3f), CornerRadius(headR * 0.08f))
    }

    // ---- arms
    val shoulderY = bodyTop + headR * 0.15f
    val armLen = headR * 0.85f
    val faceC = faceCenter(person, headC, headR)
    val faceR = faceRadius(person, headR)
    listOf(-1f, 1f).forEach { side ->
        val sx = cx + side * bodyW * 0.48f
        val restAngle = side * -22f
        val coverAngle = side * -150f
        val jumpAngle = side * -160f
        val holdAngle = side * 8f
        val waveA = if (side > 0) waveAngle - 140f else 0f
        val angle = when {
            jump > 0.05f -> restAngle + (jumpAngle - restAngle) * jump
            cover > 0.01f -> restAngle + (coverAngle - restAngle) * cover
            side > 0 && waveAngle != 0f -> waveA
            near > 0.01f -> restAngle + (holdAngle - restAngle) * near
            else -> restAngle
        }
        val rad = Math.toRadians(angle.toDouble() + 90.0)
        val ex = sx + (cos(rad) * armLen).toFloat()
        val ey = shoulderY + (sin(rad) * armLen).toFloat()
        val target = if (cover > 0.01f && jump <= 0.05f) {
            val e = eyeCenter(faceC, faceR, side, peek)
            Offset(sx + (e.x - sx) * cover, shoulderY + (e.y - shoulderY) * cover)
        } else Offset(ex, ey)
        drawLine(look.top, Offset(sx, shoulderY), target, strokeWidth = headR * 0.28f, cap = StrokeCap.Round)
        drawLine(look.skin, Offset(sx + (target.x - sx) * 0.55f, shoulderY + (target.y - shoulderY) * 0.55f), target, strokeWidth = headR * 0.22f, cap = StrokeCap.Round)
        drawCircle(look.skin, headR * 0.17f, target)
    }

    // ---- head
    val face = facePath(faceC, faceR, if (person == Person.USAID) 1f else 0f)
    when (person) {
        Person.SANYA -> {
            // Hijab: a large smooth hood; the face opening sits low so the fabric covers the forehead.
            val hood = Path().apply {
                addOval(Rect(headC.x - headR * 1.12f, headC.y - headR * 1.22f, headC.x + headR * 1.12f, headC.y + headR * 1.4f))
            }
            drawPath(hood, Color.White, style = Stroke(width = headR * 0.08f))
            drawPath(hood, Brush.verticalGradient(listOf(accent, gradient.last()), startY = headC.y - headR * 1.2f, endY = headC.y + headR * 1.4f))
            drawPath(face, look.skin)
            drawPath(face, Color.Black.copy(alpha = 0.12f), style = Stroke(width = faceR * 0.06f))
            drawArc(Color.White.copy(alpha = 0.3f), 200f, 70f, false, Offset(headC.x - headR * 1.03f, headC.y - headR * 1.08f), Size(headR * 2.06f, headR * 2.06f), style = Stroke(width = headR * 0.09f, cap = StrokeCap.Round))
            for (k in 0 until 3) {
                drawArc(Color.Black.copy(alpha = 0.07f), 250f + k * 12f, 40f, false, Offset(headC.x - headR * (1.03f - k * 0.12f), headC.y - headR * (0.98f - k * 0.1f)), Size(headR * (2.06f - k * 0.24f), headR * (2.0f - k * 0.2f)), style = Stroke(width = headR * 0.04f, cap = StrokeCap.Round))
            }
            drawCircle(Color.White, headR * 0.07f, Offset(headC.x + headR * 0.98f, headC.y + headR * 0.6f))
        }
        Person.USAID -> {
            listOf(-1f, 1f).forEach { side -> drawCircle(look.skin, headR * 0.13f, Offset(headC.x + side * headR * 0.86f, headC.y + headR * 0.12f)) }
            drawPath(face, Color.White, style = Stroke(width = headR * 0.07f))
            drawPath(face, look.skin)
            jawShade(faceC, faceR, 1f, ink.copy(alpha = 0.05f), faceR * 0.2f)
        }
    }

    // ---- face, then the hair so the fringe hangs over the brows
    drawAnimeFace(
        faceC, faceR, accent, look.iris, lookX, lookY, closed, peek, mood, suspicion,
        lashes = person == Person.SANYA, lipTint = if (person == Person.SANYA) Color(0xFFD9607E) else null,
    )
    if (person == Person.USAID) {
        drawTousledHair(headC, headR, look, sway)
        val gr = faceR * 0.31f
        val gy = faceC.y + faceR * 0.2f
        listOf(-1f, 1f).forEach { side ->
            val gx = faceC.x + side * faceR * 0.37f
            drawCircle(Color(0xFFEAF3FF).copy(alpha = 0.2f), gr, Offset(gx, gy))
            drawCircle(ink, gr, Offset(gx, gy), style = Stroke(width = faceR * 0.05f))
            drawLine(ink, Offset(gx + side * gr, gy - faceR * 0.02f), Offset(headC.x + side * headR * 0.86f, gy - faceR * 0.08f), strokeWidth = faceR * 0.045f, cap = StrokeCap.Round)
        }
        drawLine(ink, Offset(faceC.x - faceR * 0.06f, gy - faceR * 0.02f), Offset(faceC.x + faceR * 0.06f, gy - faceR * 0.02f), strokeWidth = faceR * 0.06f, cap = StrokeCap.Round)
    }

    // ---- hands over the eyes are drawn last
    if (cover > 0.01f && jump <= 0.05f) {
        listOf(-1f, 1f).forEach { side ->
            val e = eyeCenter(faceC, faceR, side, peek)
            val sx = cx + side * bodyW * 0.48f
            val hx = sx + (e.x - sx) * cover
            val hy = shoulderY + (e.y - shoulderY) * cover
            drawCircle(look.skin, headR * 0.26f, Offset(hx, hy))
            drawCircle(ink.copy(alpha = 0.15f), headR * 0.26f, Offset(hx, hy), style = Stroke(width = 1.5f))
            for (f in -1..1) drawLine(ink.copy(alpha = 0.18f), Offset(hx + f * headR * 0.09f, hy - headR * 0.18f), Offset(hx + f * headR * 0.09f, hy + headR * 0.05f), strokeWidth = 1.5f, cap = StrokeCap.Round)
        }
    }
}

/** Big messy hair: a spiky crown that sways a little, with a fringe of tips over the forehead and lighter streaks. */
private fun DrawScope.drawTousledHair(c: Offset, r: Float, look: Look, sway: Float) {
    val hair = Path().apply {
        moveTo(c.x - r * 1.0f, c.y + r * 0.2f)
        val spikes = listOf(
            Triple(-1.12f, -0.35f, false), Triple(-1.3f, -0.75f, true), Triple(-0.95f, -0.85f, false),
            Triple(-0.85f, -1.3f, true), Triple(-0.45f, -1.05f, false), Triple(-0.3f, -1.45f, true),
            Triple(0.05f, -1.12f, false), Triple(0.35f, -1.4f, true), Triple(0.6f, -1.05f, false),
            Triple(0.98f, -1.2f, true), Triple(0.95f, -0.8f, false), Triple(1.3f, -0.6f, true),
            Triple(1.08f, -0.3f, false), Triple(1.0f, 0.2f, false),
        )
        spikes.forEach { (fx, fy, tip) ->
            val dx = if (tip) sway * r * 0.05f else 0f
            lineTo(c.x + fx * r + dx, c.y + fy * r)
        }
        val fringe = listOf(0.85f to -0.22f, 0.62f to -0.08f, 0.4f to -0.38f, 0.2f to -0.12f, 0.0f to -0.42f, -0.2f to -0.14f, -0.42f to -0.4f, -0.62f to -0.1f, -0.85f to -0.22f)
        fringe.forEach { (fx, fy) -> lineTo(c.x + fx * r, c.y + fy * r) }
        close()
    }
    drawPath(hair, Color.White, style = Stroke(width = r * 0.08f))
    drawPath(hair, Brush.verticalGradient(listOf(look.hairLight, look.hair), startY = c.y - r * 1.45f, endY = c.y + r * 0.2f))
    for (k in 0 until 5) {
        val x0 = c.x + r * (-0.75f + k * 0.35f)
        val p = Path().apply {
            moveTo(x0, c.y - r * 0.2f)
            quadraticTo(x0 + r * 0.1f, c.y - r * 0.8f, x0 + r * 0.25f * (if (k % 2 == 0) -1f else 1f), c.y - r * 1.15f)
        }
        drawPath(p, Color.White.copy(alpha = 0.22f), style = Stroke(width = r * 0.06f, cap = StrokeCap.Round))
    }
    drawArc(Color.Black.copy(alpha = 0.1f), 200f, 140f, false, Offset(c.x - r * 0.95f, c.y - r * 1.05f), Size(r * 1.9f, r * 1.3f), style = Stroke(width = r * 0.12f, cap = StrokeCap.Round))
}

/**
 * Anime eyes and face: huge glossy eyes set low with a thick lash line and
 * layered highlights, thin brows, a dot nose, soft blush and a smile that
 * opens to show teeth when happy. Shares the gaze, blink, wink, suspicion
 * and mood rules with the other companions.
 */
private fun DrawScope.drawAnimeFace(
    c: Offset, r: Float, accent: Color, iris: Color,
    lookX: Float, lookY: Float, closed: Float, peek: Float, mood: Float, suspicion: Float,
    lashes: Boolean, lipTint: Color?,
) {
    val ink = Color(0xFF2A2630)
    val eyeDx = r * 0.37f
    val eyeY = c.y + r * 0.2f
    val rx = r * 0.24f
    val ry = r * 0.3f
    val sideGlance = if (lookX == 0f) 0.65f * suspicion else 0f
    val gazeX = (lookX + sideGlance) * r * 0.07f
    val gazeY = lookY * r * 0.06f
    listOf(-1f, 1f).forEachIndexed { i, side ->
        val ex = c.x + side * eyeDx
        val ey = eyeY
        val thisClosed = if (i == 1) closed * (1f - peek * 0.85f) else closed
        if (thisClosed < 0.6f) {
            val open = 1f - thisClosed / 0.6f
            val squint = suspicion * (if (i == 1) 0.45f else 0.35f)
            val ryo = ry * (0.3f + 0.7f * open) * (1f - squint)
            val eyeRect = Rect(ex - rx, ey - ryo, ex + rx, ey + ryo)
            val eyeShape = Path().apply { addOval(eyeRect) }
            drawPath(eyeShape, Color.White)
            clipPath(eyeShape) {
                val ic = Offset(ex + gazeX, ey + ryo * 0.12f + gazeY)
                val ir = r * 0.2f
                drawCircle(Brush.radialGradient(listOf(iris.lighten(0.35f), iris, iris.darken(0.35f)), center = ic, radius = ir), ir, ic)
                drawCircle(ink, ir * 0.42f, ic)
                drawRect(ink.copy(alpha = 0.18f), Offset(ex - rx, ey - ryo), Size(rx * 2, ryo * 0.45f))
                drawOval(Color.White, Offset(ic.x - ir * 0.75f, ic.y - ir * 0.85f), Size(ir * 0.7f, ir * 0.55f))
                drawCircle(Color.White.copy(alpha = 0.9f), ir * 0.18f, Offset(ic.x + ir * 0.45f, ic.y + ir * 0.5f))
            }
            val lashRect = Rect(ex - rx * 1.02f, ey - ryo * 1.02f, ex + rx * 1.02f, ey + ryo * 1.02f)
            drawArc(ink, 190f, 160f, false, lashRect.topLeft, lashRect.size, style = Stroke(width = r * 0.075f, cap = StrokeCap.Round))
            drawArc(ink.copy(alpha = 0.55f), 20f, 60f * side, false, lashRect.topLeft, lashRect.size, style = Stroke(width = r * 0.03f, cap = StrokeCap.Round))
            if (lashes) {
                for (k in 0 until 3) {
                    val a = Math.toRadians((-62.0 + k * 20.0)).toFloat()
                    val bx = ex + side * rx * (0.55f + 0.18f * k)
                    val by = ey - ryo * (0.98f - 0.25f * k)
                    drawLine(ink, Offset(bx, by), Offset(bx + side * r * 0.12f * cos(a), by - r * 0.12f * abs(sin(a))), strokeWidth = r * 0.035f, cap = StrokeCap.Round)
                }
            }
        } else {
            val p = Path().apply {
                moveTo(ex - rx, ey + ry * 0.2f)
                quadraticTo(ex, ey - ry * 0.7f, ex + rx, ey + ry * 0.2f)
            }
            drawPath(p, ink, style = Stroke(width = r * 0.07f, cap = StrokeCap.Round))
        }
    }
    // Brows: thin, just above the eyes; one lifts and the other drops when suspicious.
    listOf(-1f, 1f).forEach { side ->
        val bx = c.x + side * eyeDx + gazeX * 0.4f
        val lift = if (side > 0) suspicion * r * 0.1f else -suspicion * r * 0.04f
        val by = eyeY - ry - r * 0.14f - lift - r * 0.04f * mood.coerceAtLeast(0f)
        val tiltIn = if (side < 0) suspicion * r * 0.08f else 0f
        val p = Path().apply {
            moveTo(bx - rx * 0.95f, by + r * 0.03f + (if (side < 0) 0f else tiltIn))
            quadraticTo(bx, by - r * 0.05f, bx + rx * 0.95f, by + r * 0.03f + (if (side < 0) tiltIn else 0f))
        }
        drawPath(p, ink.copy(alpha = 0.8f), style = Stroke(width = r * 0.05f, cap = StrokeCap.Round))
    }
    // Nose and blush.
    drawCircle(Color(0xFFE59AA0), r * 0.035f, Offset(c.x, c.y + r * 0.52f))
    listOf(-1f, 1f).forEach { side ->
        val a = (0.3f + 0.3f * mood.coerceAtLeast(0f)) * (1f - 0.5f * suspicion)
        drawOval(Color(0xFFF08A9A).copy(alpha = a), Offset(c.x + side * r * 0.62f - r * 0.14f, c.y + r * 0.42f), Size(r * 0.28f, r * 0.14f))
    }
    // Mouth.
    val my = c.y + r * 0.72f
    if (mood < -0.3f) {
        val k = (-mood).coerceIn(0f, 1f)
        drawOval(ink, topLeft = Offset(c.x - r * 0.07f * k - r * 0.02f, my - r * 0.08f * k), size = Size(r * 0.14f * k + r * 0.04f, r * 0.16f * k + r * 0.02f))
    } else {
        val happy = mood.coerceIn(0f, 1f) * (1f - suspicion)
        val mw = r * (0.14f + 0.22f * happy)
        val curve = r * (0.06f + 0.26f * happy) * (1f - suspicion)
        val smirkLift = suspicion * r * 0.06f
        if (happy > 0.55f) {
            val open = (happy - 0.55f) / 0.45f
            val mouth = Path().apply {
                moveTo(c.x - mw, my - r * 0.02f)
                quadraticTo(c.x, my + curve * (0.6f + 0.6f * open), c.x + mw, my - r * 0.02f)
                close()
            }
            drawPath(mouth, Color(0xFF7A2E3A))
            clipPath(mouth) {
                drawRect(Color.White, Offset(c.x - mw, my - r * 0.03f), Size(mw * 2, r * (0.05f + 0.06f * open)))
            }
            drawPath(mouth, ink, style = Stroke(width = r * 0.04f, cap = StrokeCap.Round))
        } else {
            val mouth = Path().apply {
                moveTo(c.x - mw, my - curve * 0.35f + smirkLift * 0.3f)
                quadraticTo(c.x + smirkLift, my + curve, c.x + mw, my - curve * 0.35f - smirkLift)
            }
            drawPath(mouth, ink, style = Stroke(width = r * 0.05f, cap = StrokeCap.Round))
            lipTint?.let { tint ->
                val lp = Path().apply {
                    moveTo(c.x - mw * 0.8f, my + curve * 0.25f + r * 0.01f)
                    quadraticTo(c.x, my + curve + r * 0.08f, c.x + mw * 0.8f, my + curve * 0.25f + r * 0.01f)
                    close()
                }
                drawPath(lp, tint.copy(alpha = 0.5f * (1f - suspicion * 0.4f)))
            }
        }
    }
    if (mood > 0.7f && suspicion < 0.5f) {
        val k = (mood - 0.7f) / 0.3f
        val sx = c.x + r * 0.95f
        val sy = c.y - r * 0.25f
        val len = r * 0.12f * k
        drawLine(accent, Offset(sx - len, sy), Offset(sx + len, sy), strokeWidth = r * 0.035f, cap = StrokeCap.Round)
        drawLine(accent, Offset(sx, sy - len), Offset(sx, sy + len), strokeWidth = r * 0.035f, cap = StrokeCap.Round)
    }
}

private fun Color.lighten(k: Float): Color = Color(red + (1f - red) * k, green + (1f - green) * k, blue + (1f - blue) * k, alpha)
private fun Color.darken(k: Float): Color = Color(red * (1f - k), green * (1f - k), blue * (1f - k), alpha)

/** Face outline: an egg that tapers to a pointed chin; [square] widens the jaw a little. */
private fun facePath(c: Offset, r: Float, square: Float): Path = Path().apply {
    val w = r * 0.86f
    val top = c.y - r * 1.0f
    val cheekY = c.y + r * 0.05f
    val jawX = r * (0.5f + 0.08f * square)
    val jawY = c.y + r * 0.8f
    val chinW = r * (0.06f + 0.08f * square)
    val chinY = c.y + r * (1.14f - 0.04f * square)
    moveTo(c.x, top)
    cubicTo(c.x + w * 0.62f, top, c.x + w, cheekY - r * 0.55f, c.x + w, cheekY)
    cubicTo(c.x + w, cheekY + r * 0.4f, c.x + jawX + r * 0.12f, jawY - r * 0.12f, c.x + jawX, jawY)
    cubicTo(c.x + jawX * 0.6f, jawY + r * 0.22f, c.x + chinW, chinY, c.x, chinY)
    cubicTo(c.x - chinW, chinY, c.x - jawX * 0.6f, jawY + r * 0.22f, c.x - jawX, jawY)
    cubicTo(c.x - jawX - r * 0.12f, jawY - r * 0.12f, c.x - w, cheekY + r * 0.4f, c.x - w, cheekY)
    cubicTo(c.x - w, cheekY - r * 0.55f, c.x - w * 0.62f, top, c.x, top)
    close()
}

private fun DrawScope.jawShade(c: Offset, r: Float, square: Float, color: Color, width: Float) {
    val w = r * 0.86f
    val cheekY = c.y + r * 0.05f
    val jawX = r * (0.5f + 0.08f * square)
    val jawY = c.y + r * 0.8f
    val chinW = r * (0.06f + 0.08f * square)
    val chinY = c.y + r * (1.14f - 0.04f * square)
    val p = Path().apply {
        moveTo(c.x + w * 0.98f, cheekY + r * 0.25f)
        cubicTo(c.x + w, cheekY + r * 0.4f, c.x + jawX + r * 0.12f, jawY - r * 0.12f, c.x + jawX, jawY)
        cubicTo(c.x + jawX * 0.6f, jawY + r * 0.22f, c.x + chinW, chinY, c.x, chinY)
        cubicTo(c.x - chinW, chinY, c.x - jawX * 0.6f, jawY + r * 0.22f, c.x - jawX, jawY)
        cubicTo(c.x - jawX - r * 0.12f, jawY - r * 0.12f, c.x - w, cheekY + r * 0.4f, c.x - w * 0.98f, cheekY + r * 0.25f)
    }
    drawPath(p, color, style = Stroke(width = width, cap = StrokeCap.Round))
}

/** Sanya's face sits lower and smaller inside the hijab; Usaid's fills the head under the hair. */
private fun faceCenter(person: Person, headC: Offset, headR: Float): Offset =
    if (person == Person.SANYA) Offset(headC.x, headC.y + headR * 0.2f) else headC

private fun faceRadius(person: Person, headR: Float): Float = if (person == Person.SANYA) headR * 0.82f else headR

/** Where an eye is, in face space; [peek] parts the hands a little. */
private fun eyeCenter(faceC: Offset, faceR: Float, side: Float, peek: Float): Offset =
    Offset(faceC.x + side * faceR * 0.37f + side * peek * faceR * 0.28f, faceC.y + faceR * 0.2f + peek * faceR * 0.1f)
