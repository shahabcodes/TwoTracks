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
import androidx.compose.ui.graphics.drawscope.withTransform
import com.shahaabapps.core.ui.theme.AppTheme
import kotlin.math.PI
import kotlin.math.sin

/** Which full-body person to draw. */
enum class Person { SANYA, USAID }

/**
 * A chibi person: big head, small body, hair, arms and legs. Shares the
 * face with every other companion. Waves on arrival and when idle, covers
 * the eyes with both hands for a password, peeks between the fingers when
 * it is shown, leans toward the email while it is typed, and jumps with
 * arms up when the form is ready.
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
    // Waves for a moment on arrival.
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
    val gradient = AppTheme.colors.gradient
    Canvas(modifier = modifier) {
        val h = size.height
        val headR = h * 0.18f
        val groundY = h * 0.93f
        val bob = (breathe - 0.5f) * headR * 0.08f
        val lift = jump.value * headR * 1.1f
        val cx = size.width / 2f + (1f - arrive.value) * -size.width * 0.9f
        val feetY = groundY - lift
        val waveAngle = if (wave.value < 1f) sin(wave.value * PI.toFloat() * 5f) * 25f else 0f
        withTransform({ rotate(lean, pivot = Offset(cx, feetY)) }) {
            drawPerson(person, cx, feetY, headR, bob, gradient, waveAngle, cover, peek, jump.value, lookX, lookY, maxOf(closed, blink), mood, suspicion, near)
        }
    }
}

private class Look(val skin: Color, val hair: Color, val top: Color, val bottom: Color, val shoes: Color)

private fun DrawScope.drawPerson(
    person: Person, cx: Float, feetY: Float, headR: Float, bob: Float, gradient: List<Color>,
    waveAngle: Float, cover: Float, peek: Float, jump: Float, lookX: Float, lookY: Float, closed: Float, mood: Float,
    suspicion: Float = 0f, near: Float = 0f,
) {
    val accent = gradient.first()
    val look = when (person) {
        // Sanya: hijab in the accent over a soft cream dress. Usaid: polo, baggy denim, glasses.
        Person.SANYA -> Look(Color(0xFFF2CDB8), accent, Color(0xFFFDF7F9), Color(0xFF3A3A4A), Color(0xFFFFFFFF))
        Person.USAID -> Look(Color(0xFFE3B393), Color(0xFF2B2622), Color(0xFFFDF7F9), Color(0xFF4F6FA3), Color(0xFF2A2630))
    }
    val ink = Color(0xFF2A2630)
    val legH = headR * 1.05f
    val bodyH = headR * 1.25f
    val bodyW = headR * 1.1f
    val bodyTop = feetY - legH - bodyH + bob
    val headC = Offset(cx, bodyTop - headR * 0.85f)

    // Legs and shoes. In a jump they tuck up a little.
    listOf(-1f, 1f).forEach { side ->
        val lx = cx + side * headR * 0.3f
        val tuck = jump * headR * 0.25f
        val legW = if (person == Person.USAID) headR * 0.5f else headR * 0.34f
        drawRoundRect(look.bottom, Offset(lx - legW / 2f, feetY - legH + tuck), Size(legW, legH - tuck), CornerRadius(headR * 0.12f))
        if (person == Person.USAID) {
            // Denim seam and a turned-up cuff.
            drawLine(Color.White.copy(alpha = 0.25f), Offset(lx, feetY - legH + tuck + headR * 0.1f), Offset(lx, feetY - headR * 0.25f + tuck), strokeWidth = 1.5f)
            drawRoundRect(Color(0xFF6E8BBE), Offset(lx - legW / 2f, feetY - headR * 0.42f + tuck), Size(legW, headR * 0.16f), CornerRadius(headR * 0.06f))
        }
        drawRoundRect(look.shoes, Offset(lx - headR * 0.22f, feetY - headR * 0.2f + tuck), Size(headR * 0.44f, headR * 0.22f), CornerRadius(headR * 0.1f))
        drawRoundRect(ink.copy(alpha = 0.25f), Offset(lx - headR * 0.22f, feetY - headR * 0.2f + tuck), Size(headR * 0.44f, headR * 0.22f), CornerRadius(headR * 0.1f), style = Stroke(width = 1.5f))
    }
    // Body: dress for Mia, tee for Leo.
    if (person == Person.SANYA) {
        // Long dress that flares to the ankles, with an accent hem.
        val hemY = feetY - headR * 0.18f
        val dress = Path().apply {
            moveTo(cx - bodyW * 0.42f, bodyTop)
            lineTo(cx + bodyW * 0.42f, bodyTop)
            quadraticTo(cx + bodyW * 0.3f, bodyTop + bodyH * 0.45f, cx + bodyW * 0.72f, hemY)
            lineTo(cx - bodyW * 0.72f, hemY)
            quadraticTo(cx - bodyW * 0.3f, bodyTop + bodyH * 0.45f, cx - bodyW * 0.42f, bodyTop)
            close()
        }
        drawPath(dress, Brush.verticalGradient(listOf(Color.White, look.top, Color(0xFFF2DCE4)), startY = bodyTop, endY = hemY))
        drawPath(dress, accent.copy(alpha = 0.35f), style = Stroke(width = headR * 0.04f))
        drawLine(accent, Offset(cx - bodyW * 0.68f, hemY - headR * 0.05f), Offset(cx + bodyW * 0.68f, hemY - headR * 0.05f), strokeWidth = headR * 0.1f)
        // Hijab drape over the shoulders.
        val drape = Path().apply {
            moveTo(cx - headR * 0.75f, bodyTop - headR * 0.1f)
            quadraticTo(cx, bodyTop + headR * 0.75f, cx + headR * 0.75f, bodyTop - headR * 0.1f)
            lineTo(cx + headR * 0.95f, bodyTop + headR * 0.35f)
            quadraticTo(cx, bodyTop + headR * 1.15f, cx - headR * 0.95f, bodyTop + headR * 0.35f)
            close()
        }
        drawPath(drape, Brush.verticalGradient(listOf(accent, gradient.last()), startY = bodyTop - headR * 0.1f, endY = bodyTop + headR * 1.2f))
    } else {
        // Polo: body, collar, placket and two buttons.
        val polo = Path().apply {
            moveTo(cx - bodyW * 0.56f, bodyTop + headR * 0.1f)
            quadraticTo(cx - bodyW * 0.56f, bodyTop, cx - bodyW * 0.4f, bodyTop)
            lineTo(cx + bodyW * 0.4f, bodyTop)
            quadraticTo(cx + bodyW * 0.56f, bodyTop, cx + bodyW * 0.56f, bodyTop + headR * 0.1f)
            lineTo(cx + bodyW * 0.46f, bodyTop + bodyH)
            lineTo(cx - bodyW * 0.46f, bodyTop + bodyH)
            close()
        }
        drawPath(polo, look.top)
        drawPath(polo, accent.copy(alpha = 0.35f), style = Stroke(width = headR * 0.04f))
        // Chest line for a little definition.
        drawLine(accent.copy(alpha = 0.12f), Offset(cx - bodyW * 0.22f, bodyTop + headR * 0.55f), Offset(cx + bodyW * 0.22f, bodyTop + headR * 0.55f), strokeWidth = headR * 0.05f, cap = StrokeCap.Round)
        listOf(-1f, 1f).forEach { side -> drawLine(accent.copy(alpha = 0.5f), Offset(cx + side * bodyW * 0.5f, bodyTop + headR * 0.12f), Offset(cx + side * bodyW * 0.5f, bodyTop + headR * 0.42f), strokeWidth = headR * 0.06f) }
        val collar = Path().apply {
            moveTo(cx - headR * 0.42f, bodyTop - headR * 0.02f)
            lineTo(cx, bodyTop + headR * 0.4f)
            lineTo(cx + headR * 0.42f, bodyTop - headR * 0.02f)
            lineTo(cx + headR * 0.3f, bodyTop + headR * 0.08f)
            lineTo(cx, bodyTop + headR * 0.28f)
            lineTo(cx - headR * 0.3f, bodyTop + headR * 0.08f)
            close()
        }
        drawPath(collar, accent)
        drawLine(accent.copy(alpha = 0.4f), Offset(cx, bodyTop + headR * 0.3f), Offset(cx, bodyTop + headR * 0.7f), strokeWidth = headR * 0.05f)
        drawCircle(accent, headR * 0.045f, Offset(cx, bodyTop + headR * 0.45f))
        drawCircle(accent, headR * 0.045f, Offset(cx, bodyTop + headR * 0.62f))
    }
    // Arms: rest at the sides, wave with the right, both rise to cover the eyes, both up in a jump.
    val shoulderY = bodyTop + headR * 0.15f
    val armLen = headR * 0.95f
    listOf(-1f, 1f).forEach { side ->
        val sx = cx + side * bodyW * 0.48f
        val restAngle = side * -22f
        val coverAngle = side * -150f
        val jumpAngle = side * -160f
        val waveA = if (side > 0) waveAngle - 140f else 0f
        val holdAngle = side * 8f
        val angle = when {
            jump > 0.05f -> restAngle + (jumpAngle - restAngle) * jump
            cover > 0.01f -> restAngle + (coverAngle - restAngle) * cover
            side > 0 && waveAngle != 0f -> waveA
            near > 0.01f -> restAngle + (holdAngle - restAngle) * near
            else -> restAngle
        }
        val rad = Math.toRadians(angle.toDouble() + 90.0)
        val ex = sx + (kotlin.math.cos(rad) * armLen).toFloat()
        val ey = shoulderY + (kotlin.math.sin(rad) * armLen).toFloat()
        val handR = headR * 0.17f
        // When covering, hands land on the eyes; when peeking they part a little.
        val target = if (cover > 0.01f && jump <= 0.05f) {
            val eyeX = faceCenterX(person, headC) + side * headR * 0.36f + side * peek * headR * 0.28f
            val eyeY = faceCenterY(person, headC, headR) - headR * 0.08f + peek * headR * 0.12f
            Offset(sx + (eyeX - sx) * cover, shoulderY + (eyeY - shoulderY) * cover)
        } else Offset(ex, ey)
        val sleeve = if (person == Person.SANYA) look.top else look.top
        drawLine(sleeve, Offset(sx, shoulderY), target, strokeWidth = headR * 0.26f, cap = StrokeCap.Round)
        drawLine(look.skin, Offset(sx + (target.x - sx) * 0.45f, shoulderY + (target.y - shoulderY) * 0.45f), target, strokeWidth = headR * 0.22f, cap = StrokeCap.Round)
        drawCircle(look.skin, handR, target)
    }
    // Head: a sculpted face with cheekbones and a jawline instead of a plain circle.
    // Sanya's face sits a little smaller inside the hijab; Usaid's jaw is squarer.
    val faceR = if (person == Person.SANYA) headR * 0.9f else headR
    val faceC = if (person == Person.SANYA) Offset(headC.x, headC.y + headR * 0.07f) else headC
    val square = if (person == Person.USAID) 1f else 0f
    val face = facePath(faceC, faceR, square)
    when (person) {
        Person.SANYA -> {
            // Hijab hood, then the face opening, then folds and a pin.
            val hood = Path().apply {
                addOval(Rect(headC.x - headR * 1.06f, headC.y - headR * 1.2f, headC.x + headR * 1.06f, headC.y + headR * 1.42f))
            }
            drawPath(hood, Brush.verticalGradient(listOf(accent, gradient.last()), startY = headC.y - headR * 1.2f, endY = headC.y + headR * 1.3f))
            drawPath(face, look.skin)
            // Soft shadow where the fabric meets the face, and a jaw contour.
            drawPath(face, Color.Black.copy(alpha = 0.1f), style = Stroke(width = faceR * 0.06f))
            drawArc(Color.White.copy(alpha = 0.3f), 200f, 70f, false, Offset(headC.x - headR * 1.03f, headC.y - headR * 1.03f), Size(headR * 2.06f, headR * 2.06f), style = Stroke(width = headR * 0.09f, cap = StrokeCap.Round))
            for (k in 0 until 3) {
                drawArc(Color.Black.copy(alpha = 0.07f), 250f + k * 12f, 40f, false, Offset(headC.x - headR * (1.03f - k * 0.12f), headC.y - headR * (0.93f - k * 0.1f)), Size(headR * (2.06f - k * 0.24f), headR * (1.96f - k * 0.2f)), style = Stroke(width = headR * 0.04f, cap = StrokeCap.Round))
            }
            drawCircle(Color.White, headR * 0.07f, Offset(headC.x + headR * 0.95f, headC.y + headR * 0.55f))
            // Cheekbone highlights.
            listOf(-1f, 1f).forEach { side -> drawOval(Color.White.copy(alpha = 0.22f), topLeft = Offset(faceC.x + side * faceR * 0.62f - faceR * 0.16f, faceC.y + faceR * 0.02f), size = Size(faceR * 0.32f, faceR * 0.14f)) }
        }
        Person.USAID -> {
            listOf(-1f, 1f).forEach { side -> drawCircle(look.skin, headR * 0.13f, Offset(headC.x + side * headR * 0.86f, headC.y - headR * 0.05f)) }
            drawPath(face, look.skin)
            // Jaw contour and a faint stubble band along the jaw.
            jawShade(faceC, faceR, square, ink.copy(alpha = 0.06f), faceR * 0.22f)
            jawShade(faceC, faceR, square, ink.copy(alpha = 0.12f), faceR * 0.05f)
            // Hair: pushed up and back from a side part on the right, short at the sides,
            // with a high hairline that leaves the forehead clear.
            val hair = Path().apply {
                moveTo(headC.x - headR * 0.9f, headC.y - headR * 0.05f)
                cubicTo(headC.x - headR * 1.02f, headC.y - headR * 0.9f, headC.x - headR * 0.5f, headC.y - headR * 1.42f, headC.x + headR * 0.1f, headC.y - headR * 1.32f)
                cubicTo(headC.x + headR * 0.6f, headC.y - headR * 1.24f, headC.x + headR * 0.98f, headC.y - headR * 0.9f, headC.x + headR * 0.88f, headC.y - headR * 0.15f)
                cubicTo(headC.x + headR * 0.78f, headC.y - headR * 0.5f, headC.x + headR * 0.45f, headC.y - headR * 0.66f, headC.x + headR * 0.15f, headC.y - headR * 0.64f)
                cubicTo(headC.x - headR * 0.2f, headC.y - headR * 0.62f, headC.x - headR * 0.6f, headC.y - headR * 0.66f, headC.x - headR * 0.9f, headC.y - headR * 0.05f)
                close()
            }
            drawPath(hair, Brush.verticalGradient(listOf(Color(0xFF3A322C), look.hair), startY = headC.y - headR * 1.4f, endY = headC.y - headR * 0.4f))
            // Side part.
            drawLine(look.skin.copy(alpha = 0.9f), Offset(headC.x + headR * 0.38f, headC.y - headR * 0.66f), Offset(headC.x + headR * 0.82f, headC.y - headR * 0.98f), strokeWidth = headR * 0.035f, cap = StrokeCap.Round)
            // Texture: strands swept up and to the left from the part.
            for (k in 0 until 4) {
                val strand = Path().apply {
                    moveTo(headC.x + headR * (0.32f - 0.1f * k), headC.y - headR * (0.7f + 0.06f * k))
                    quadraticTo(headC.x - headR * (0.05f + 0.14f * k), headC.y - headR * (1.3f - 0.02f * k), headC.x - headR * (0.5f + 0.1f * k), headC.y - headR * (1.1f - 0.12f * k))
                }
                drawPath(strand, Color.White.copy(alpha = 0.14f), style = Stroke(width = headR * 0.05f, cap = StrokeCap.Round))
            }
            // Short sides: a darker band at the temples.
            listOf(-1f, 1f).forEach { side ->
                drawLine(Color.Black.copy(alpha = 0.12f), Offset(headC.x + side * headR * 0.86f, headC.y - headR * 0.45f), Offset(headC.x + side * headR * 0.84f, headC.y - headR * 0.08f), strokeWidth = headR * 0.1f, cap = StrokeCap.Round)
            }
        }
    }
    // Face, drawn after the hair; the brows sit just under the fringe.
    val lip = if (person == Person.SANYA) Color(0xFFD9607E) else null
    drawExpression(faceC, faceR, accent, lookX, lookY, closed, peek, mood, suspicion = suspicion, lashes = person == Person.SANYA, lipTint = lip, browWeight = if (person == Person.USAID) 1.8f else 1.1f)
    if (person == Person.USAID) {
        // Round glasses with a bridge and arms to the ears.
        val gr = faceR * 0.3f
        val gy = faceC.y - faceR * 0.08f
        listOf(-1f, 1f).forEach { side ->
            val gx = faceC.x + side * faceR * 0.36f
            drawCircle(Color(0xFFEAF3FF).copy(alpha = 0.25f), gr, Offset(gx, gy))
            drawCircle(ink, gr, Offset(gx, gy), style = Stroke(width = headR * 0.05f))
            drawLine(ink, Offset(gx + side * gr, gy - headR * 0.02f), Offset(faceC.x + side * faceR * 0.86f, gy - headR * 0.06f), strokeWidth = headR * 0.05f, cap = StrokeCap.Round)
        }
        drawLine(ink, Offset(headC.x - headR * 0.06f, gy), Offset(headC.x + headR * 0.06f, gy), strokeWidth = headR * 0.06f, cap = StrokeCap.Round)
    }
    // Hands over the eyes are drawn last.
    if (cover > 0.01f && jump <= 0.05f) {
        listOf(-1f, 1f).forEach { side ->
            val eyeX = faceCenterX(person, headC) + side * headR * 0.36f + side * peek * headR * 0.28f
            val eyeY = faceCenterY(person, headC, headR) - headR * 0.08f + peek * headR * 0.12f
            val sx = cx + side * bodyW * 0.48f
            val hx = sx + (eyeX - sx) * cover
            val hy = shoulderY + (eyeY - shoulderY) * cover
            drawCircle(look.skin, headR * 0.26f, Offset(hx, hy))
            drawCircle(ink.copy(alpha = 0.15f), headR * 0.26f, Offset(hx, hy), style = Stroke(width = 1.5f))
            // Finger lines.
            for (f in -1..1) drawLine(ink.copy(alpha = 0.18f), Offset(hx + f * headR * 0.09f, hy - headR * 0.18f), Offset(hx + f * headR * 0.09f, hy + headR * 0.05f), strokeWidth = 1.5f, cap = StrokeCap.Round)
        }
    }
}

/**
 * Face outline: an egg, narrower than it is tall, with cheekbones at eye
 * level and a chin that comes to a point. [square] 0 keeps the point sharp
 * (Sanya); 1 blunts it slightly and widens the jaw (Usaid).
 */
private fun facePath(c: Offset, r: Float, square: Float): Path = Path().apply {
    val w = r * 0.84f
    val top = c.y - r * 1.02f
    val cheekY = c.y - r * 0.1f
    val jawX = r * (0.5f + 0.1f * square)
    val jawY = c.y + r * 0.82f
    val chinW = r * (0.06f + 0.1f * square)
    val chinY = c.y + r * (1.18f - 0.06f * square)
    moveTo(c.x, top)
    cubicTo(c.x + w * 0.62f, top, c.x + w, cheekY - r * 0.5f, c.x + w, cheekY)
    cubicTo(c.x + w, cheekY + r * 0.45f, c.x + jawX + r * 0.12f, jawY - r * 0.15f, c.x + jawX, jawY)
    cubicTo(c.x + jawX * 0.6f, jawY + r * 0.25f, c.x + chinW, chinY, c.x, chinY)
    cubicTo(c.x - chinW, chinY, c.x - jawX * 0.6f, jawY + r * 0.25f, c.x - jawX, jawY)
    cubicTo(c.x - jawX - r * 0.12f, jawY - r * 0.15f, c.x - w, cheekY + r * 0.45f, c.x - w, cheekY)
    cubicTo(c.x - w, cheekY - r * 0.5f, c.x - w * 0.62f, top, c.x, top)
    close()
}

/** A stroke along the jaw only, from one cheekbone round the chin to the other. */
private fun DrawScope.jawShade(c: Offset, r: Float, square: Float, color: Color, width: Float) {
    val w = r * 0.84f
    val cheekY = c.y - r * 0.1f
    val jawX = r * (0.5f + 0.1f * square)
    val jawY = c.y + r * 0.82f
    val chinW = r * (0.06f + 0.1f * square)
    val chinY = c.y + r * (1.18f - 0.06f * square)
    val p = Path().apply {
        moveTo(c.x + w * 0.98f, cheekY + r * 0.3f)
        cubicTo(c.x + w, cheekY + r * 0.45f, c.x + jawX + r * 0.12f, jawY - r * 0.15f, c.x + jawX, jawY)
        cubicTo(c.x + jawX * 0.6f, jawY + r * 0.25f, c.x + chinW, chinY, c.x, chinY)
        cubicTo(c.x - chinW, chinY, c.x - jawX * 0.6f, jawY + r * 0.25f, c.x - jawX, jawY)
        cubicTo(c.x - jawX - r * 0.12f, jawY - r * 0.15f, c.x - w, cheekY + r * 0.45f, c.x - w * 0.98f, cheekY + r * 0.3f)
    }
    drawPath(p, color, style = Stroke(width = width, cap = StrokeCap.Round))
}

private fun faceCenterX(person: Person, headC: Offset): Float = headC.x
private fun faceCenterY(person: Person, headC: Offset, headR: Float): Float = if (person == Person.SANYA) headC.y + headR * 0.07f else headC.y
