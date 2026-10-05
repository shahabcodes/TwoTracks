package com.shahaabapps.twotracks.feature.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import com.shahaabapps.twotracks.ui.components.AuroraLayer
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.LoadingOverlay
import com.shahaabapps.twotracks.ui.ThemeViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Sign in, sign up and reset in one place: a living aurora backdrop with the
 * icon's tracks drawing themselves in, a frosted card that slides between the
 * three modes, fields that light up on focus and a button with a light sweep.
 */
@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val companionKind by themeViewModel.companion.collectAsStateWithLifecycle()
    if (state.isSubmitting) LoadingOverlay(if (state.mode == AuthMode.RESET) "Sending the link" else if (state.isSignUp) "Creating your account" else "Signing you in")

    // One shake per failure.
    val shake = remember { Animatable(0f) }
    LaunchedEffect(state.errorTick) {
        if (state.errorTick == 0) return@LaunchedEffect
        shake.animateTo(0f, keyframes { durationMillis = 420; 0f at 0; -14f at 60; 12f at 120; -8f at 180; 5f at 240; -2f at 300; 0f at 420 })
    }
    var focus by remember { mutableStateOf(FieldFocus.NONE) }
    var passwordShown by remember { mutableStateOf(false) }
    val mood = when {
        state.errorMessage != null -> -1f
        state.mode == AuthMode.SIGN_UP && state.confirmPassword.isNotEmpty() && !state.passwordsMatch -> -1f
        state.mode == AuthMode.SIGN_UP && state.confirmPassword.isNotEmpty() && state.passwordsMatch && state.password.length >= 6 -> 1f
        state.emailValid && (state.mode == AuthMode.RESET || state.password.length >= 6) -> 1f
        state.emailValid -> 0.6f
        else -> 0.25f
    }
    val mascot = MascotState(focus = focus, lookX = emailLook(state.email), passwordShown = passwordShown, mood = mood)
    // Card rises in on first open.
    val enter = remember { Animatable(0f) }
    LaunchedEffect(Unit) { enter.animateTo(1f, tween(700, delayMillis = 250, easing = FastOutSlowInEasing)) }

    Box(modifier = modifier.fillMaxSize().background(AppTheme.colors.groupedBackground)) {
        AuroraLayer(modifier = Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // The hero folds away while a field has focus so the face stays above the keyboard.
            val heroHeight by animateDpAsState(if (focus == FieldFocus.NONE) 280.dp else 0.dp, tween(360, easing = FastOutSlowInEasing), label = "hero")
            Hero(mode = state.mode, modifier = Modifier.fillMaxWidth().height(heroHeight).clip(RoundedCornerShape(0.dp)))
            Box(
                modifier = Modifier
                    .padding(horizontal = Spacing.listHorizontal)
                    .padding(top = companionHeight(companionKind) / 2)
                    .offset { IntOffset(shake.value.roundToInt(), ((1f - enter.value) * 60).roundToInt()) }
                    .alpha(enter.value),
            ) {
                GlassCard(topInset = companionHeight(companionKind) / 2 + Spacing.xs) {
                    ModeTabs(mode = state.mode, onSelect = viewModel::setMode)
                    AnimatedContent(
                        targetState = state.mode,
                        transitionSpec = {
                            val forward = targetState.ordinal > initialState.ordinal
                            (slideInHorizontally(tween(320)) { if (forward) it / 3 else -it / 3 } + fadeIn(tween(260))) togetherWith
                                (slideOutHorizontally(tween(260)) { if (forward) -it / 3 else it / 3 } + fadeOut(tween(180)))
                        },
                        label = "mode",
                    ) { mode ->
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                            Text(
                                when (mode) {
                                    AuthMode.SIGN_IN -> "Welcome back"
                                    AuthMode.SIGN_UP -> "Start your two tracks"
                                    AuthMode.RESET -> "Reset your password"
                                },
                                style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold,
                            )
                            Text(
                                when (mode) {
                                    AuthMode.SIGN_IN -> "Pick up where the two of you left off."
                                    AuthMode.SIGN_UP -> "One space for both of you. Every study hour counts toward the move."
                                    AuthMode.RESET -> "We email a link. Open it, choose a new password, then sign in here."
                                },
                                style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
                            )
                            NeoField(
                                value = state.email, onValueChange = viewModel::onEmailChange, placeholder = "Email",
                                icon = { Icon(Icons.Filled.Mail, null) }, keyboardType = KeyboardType.Email,
                                valid = state.email.isEmpty() || state.emailValid,
                                onFocusChange = { if (it) focus = FieldFocus.EMAIL else if (focus == FieldFocus.EMAIL) focus = FieldFocus.NONE },
                            )
                            if (mode != AuthMode.RESET) {
                                val show = passwordShown
                                NeoField(
                                    value = state.password, onValueChange = viewModel::onPasswordChange,
                                    placeholder = if (mode == AuthMode.SIGN_UP) "Choose a password (6+ characters)" else "Password",
                                    icon = { Icon(Icons.Filled.Lock, null) }, keyboardType = KeyboardType.Password,
                                    visualTransformation = if (show) VisualTransformation.None else PasswordVisualTransformation(),
                                    trailing = {
                                        Icon(
                                            if (show) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = if (show) "Hide password" else "Show password",
                                            tint = AppTheme.colors.secondaryLabel,
                                            modifier = Modifier.size(22.dp).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { passwordShown = !passwordShown },
                                        )
                                    },
                                    onFocusChange = { if (it) focus = FieldFocus.PASSWORD else if (focus == FieldFocus.PASSWORD) focus = FieldFocus.NONE },
                                )
                                if (mode == AuthMode.SIGN_UP) {
                                    StrengthMeter(state.password)
                                    val mismatch = state.confirmPassword.isNotEmpty() && !state.passwordsMatch
                                    NeoField(
                                        value = state.confirmPassword, onValueChange = viewModel::onConfirmChange, placeholder = "Confirm password",
                                        icon = { Icon(Icons.Filled.Lock, null) }, keyboardType = KeyboardType.Password,
                                        visualTransformation = if (show) VisualTransformation.None else PasswordVisualTransformation(),
                                        valid = !mismatch,
                                        trailing = {
                                            AnimatedVisibility(visible = state.confirmPassword.isNotEmpty() && state.passwordsMatch, enter = fadeIn(), exit = fadeOut()) {
                                                Icon(Icons.Filled.CheckCircle, contentDescription = "Passwords match", tint = AppTheme.colors.success, modifier = Modifier.size(22.dp))
                                            }
                                        },
                                        onFocusChange = { if (it) focus = FieldFocus.CONFIRM else if (focus == FieldFocus.CONFIRM) focus = FieldFocus.NONE },
                                    )
                                    AnimatedVisibility(visible = mismatch, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
                                        Text("The two passwords differ.", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.destructive)
                                    }
                                }
                            }
                        }
                    }

                    AnimatedVisibility(visible = state.errorMessage != null, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
                        MessageBanner(state.errorMessage.orEmpty(), AppTheme.colors.destructive)
                    }
                    AnimatedVisibility(visible = state.infoMessage != null, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
                        MessageBanner(state.infoMessage.orEmpty(), AppTheme.colors.success)
                    }

                    GlowButton(
                        text = when (state.mode) {
                            AuthMode.SIGN_IN -> "Sign in"
                            AuthMode.SIGN_UP -> "Create account"
                            AuthMode.RESET -> "Send reset link"
                        },
                        enabled = state.canSubmit,
                        onClick = viewModel::submit,
                        modifier = Modifier.padding(top = Spacing.xs),
                    )
                    LinkRow(mode = state.mode, onSelect = viewModel::setMode)
                }
                Companion(
                    kind = companionKind,
                    state = mascot,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = -companionHeight(companionKind) / 2 - 6.dp)
                        .size(width = MascotWidth, height = companionHeight(companionKind) + 12.dp),
                )
            }
            Spacer(Modifier.height(Spacing.xl))
        }
    }
}

// ------------------------------------------------------------------ hero

@Composable
private fun Hero(mode: AuthMode, modifier: Modifier = Modifier) {
    val draw = remember { Animatable(0f) }
    LaunchedEffect(Unit) { draw.animateTo(1f, tween(1500, delayMillis = 150, easing = FastOutSlowInEasing)) }
    val transition = rememberInfiniteTransition(label = "hero")
    val pulse by transition.animateFloat(0f, 1f, infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Restart), label = "pulse")
    val drift by transition.animateFloat(0f, 1f, infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Reverse), label = "drift")
    val gradient = AppTheme.colors.gradient
    val accent = AppTheme.colors.accent
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize().clipToBounds()) {
            val t = draw.value
            val s = tracks(size.width * 0.56f, size.height * 0.44f + (drift - 0.5f) * 10f, size.width * 0.42f)
            // Glow halo under the tracks.
            if (size.height > 1f) drawCircle(Brush.radialGradient(listOf(accent.copy(alpha = 0.35f), accent.copy(alpha = 0f)), center = Offset(size.width * 0.58f, size.height * 0.5f), radius = size.width * 0.42f), radius = size.width * 0.42f, center = Offset(size.width * 0.58f, size.height * 0.5f))
            drawPath(segment(s.trailMeasure, 0f, (t * 1.1f - 0.1f).coerceIn(0f, 1f)), Color.White.copy(alpha = 0.55f), style = Stroke(width = s.stroke * 0.85f, cap = StrokeCap.Round))
            drawPath(segment(s.leadMeasure, 0f, t), Brush.linearGradient(listOf(Color.White, gradient.first()), start = Offset(0f, size.height), end = Offset(size.width, 0f)), style = Stroke(width = s.stroke, cap = StrokeCap.Round))
            val pos = s.leadMeasure.getPosition(t * s.leadMeasure.length)
            drawCircle(Color.White, s.dot, pos)
            if (t >= 1f) {
                drawCircle(Color.White.copy(alpha = (1f - pulse) * 0.8f), s.dot + pulse * size.width * 0.12f, pos, style = Stroke(width = 3f * (1f - pulse) + 1f))
            }
        }
        Column(modifier = Modifier.align(Alignment.BottomStart).padding(horizontal = Spacing.lg, vertical = Spacing.md)) {
            Text("TWO TRACKS", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.85f), letterSpacing = androidx.compose.ui.unit.TextUnit(4f, androidx.compose.ui.unit.TextUnitType.Sp))
            AnimatedContent(targetState = mode, transitionSpec = { (fadeIn(tween(300)) togetherWith fadeOut(tween(200))) }, label = "tag") { m ->
                Text(
                    when (m) { AuthMode.SIGN_IN -> "Run in parallel."; AuthMode.SIGN_UP -> "Two people, one plan."; AuthMode.RESET -> "No stress. One link." },
                    style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

private class Tracks(val leadMeasure: PathMeasure, val trailMeasure: PathMeasure, val stroke: Float, val dot: Float)

/** The launcher icon's two curves centred at [cx],[cy] spanning [width]. */
private fun tracks(cx: Float, cy: Float, width: Float): Tracks {
    val scale = width / 44f
    val ox = cx - 54f * scale
    val oy = cy - 50f * scale
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
    return Tracks(PathMeasure().apply { setPath(lead, false) }, PathMeasure().apply { setPath(trail, false) }, 7f * scale, 5f * scale)
}

private fun segment(measure: PathMeasure, from: Float, to: Float): Path {
    val out = Path()
    val a = from.coerceIn(0f, 1f) * measure.length
    val b = to.coerceIn(0f, 1f) * measure.length
    if (b > a) measure.getSegment(a, b, out, true)
    return out
}

// ----------------------------------------------------------------- card

@Composable
private fun GlassCard(topInset: Dp = 0.dp, content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    val border = Brush.linearGradient(listOf(Color.White.copy(alpha = 0.9f), AppTheme.colors.accent.copy(alpha = 0.35f), Color.White.copy(alpha = 0.2f)))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(30.dp, RoundedCornerShape(28.dp), ambientColor = AppTheme.colors.accent.copy(alpha = 0.25f), spotColor = Color.Black.copy(alpha = 0.3f))
            .clip(RoundedCornerShape(28.dp))
            .background(AppTheme.colors.card.copy(alpha = 0.94f))
            .border(1.dp, border, RoundedCornerShape(28.dp))
            .padding(start = Spacing.lg, end = Spacing.lg, bottom = Spacing.lg, top = Spacing.lg + topInset),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) { content() }
}

@Composable
private fun ModeTabs(mode: AuthMode, onSelect: (AuthMode) -> Unit) {
    val options = listOf(AuthMode.SIGN_IN to "Sign in", AuthMode.SIGN_UP to "Sign up")
    val index = if (mode == AuthMode.SIGN_UP) 1 else 0
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.fill).padding(3.dp),
    ) {
        val w = (maxWidth - 6.dp) / 2
        val x by animateDpAsState(w * index, spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessMediumLow), label = "tab")
        val reset = mode == AuthMode.RESET
        Box(
            modifier = Modifier
                .offset(x = x)
                .size(width = w, height = 40.dp)
                .alpha(if (reset) 0.35f else 1f)
                .shadow(6.dp, RoundedCornerShape(11.dp))
                .clip(RoundedCornerShape(11.dp))
                .background(AppTheme.colors.card),
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            options.forEach { (m, label) ->
                val selected = m == mode
                val color by animateFloatAsState(if (selected) 1f else 0f, label = "c")
                Box(
                    modifier = Modifier
                        .size(width = w, height = 40.dp)
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onSelect(m) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        label, style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                        color = androidx.compose.ui.graphics.lerp(AppTheme.colors.secondaryLabel, AppTheme.colors.accent, color),
                    )
                }
            }
        }
    }
}

// --------------------------------------------------------------- fields

@Composable
private fun NeoField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: @Composable () -> Unit,
    keyboardType: KeyboardType,
    valid: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailing: (@Composable () -> Unit)? = null,
    onFocusChange: (Boolean) -> Unit = {},
) {
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()
    LaunchedEffect(focused) { onFocusChange(focused) }
    val accent = AppTheme.colors.accent
    val target = when {
        !valid -> AppTheme.colors.destructive
        focused -> accent
        else -> Color.Transparent
    }
    val borderAlpha by animateFloatAsState(if (focused || !valid) 1f else 0f, tween(220), label = "b")
    val glow by animateDpAsState(if (focused) 14.dp else 0.dp, tween(260), label = "g")
    val iconTint = androidx.compose.ui.graphics.lerp(AppTheme.colors.secondaryLabel, if (valid) accent else AppTheme.colors.destructive, borderAlpha)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(glow, RoundedCornerShape(16.dp), ambientColor = accent.copy(alpha = 0.5f), spotColor = accent.copy(alpha = 0.5f))
            .clip(RoundedCornerShape(16.dp))
            .background(AppTheme.colors.fill)
            .border(1.5.dp, target.copy(alpha = target.alpha * borderAlpha), RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        androidx.compose.runtime.CompositionLocalProvider(androidx.compose.material3.LocalContentColor provides iconTint) {
            Box(modifier = Modifier.size(22.dp), contentAlignment = Alignment.Center) { icon() }
        }
        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) Text(placeholder, style = MaterialTheme.typography.bodyLarge, color = AppTheme.colors.secondaryLabel)
            BasicTextField(
                value = value, onValueChange = onValueChange, singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = visualTransformation,
                cursorBrush = SolidColor(accent),
                interactionSource = interaction,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        trailing?.invoke()
    }
}

@Composable
private fun StrengthMeter(password: String) {
    val score = listOf(password.length >= 6, password.length >= 10, password.any { it.isDigit() }, password.any { !it.isLetterOrDigit() }).count { it }
    val label = when { password.isEmpty() -> ""; score <= 1 -> "Weak"; score == 2 -> "Okay"; score == 3 -> "Strong"; else -> "Very strong" }
    val color = when { score <= 1 -> AppTheme.colors.destructive; score == 2 -> AppTheme.colors.warn; else -> AppTheme.colors.success }
    AnimatedVisibility(visible = password.isNotEmpty(), enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(4) { i ->
                    val fill by animateFloatAsState(if (i < score) 1f else 0f, tween(300, delayMillis = i * 60), label = "s")
                    Box(modifier = Modifier.weight(1f).height(4.dp).clip(CircleShape).background(AppTheme.colors.fill)) {
                        Box(modifier = Modifier.fillMaxWidth(fill).height(4.dp).background(color))
                    }
                }
            }
            Text(label, style = MaterialTheme.typography.labelSmall, color = color)
        }
    }
}

@Composable
private fun MessageBanner(text: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(color.copy(alpha = 0.12f)).padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Text(text, style = MaterialTheme.typography.bodySmall, color = color, modifier = Modifier.weight(1f))
    }
}

// --------------------------------------------------------------- button

@Composable
private fun GlowButton(text: String, enabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.97f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium), label = "p")
    val alpha by animateFloatAsState(if (enabled) 1f else 0.45f, tween(200), label = "a")
    val transition = rememberInfiniteTransition(label = "sweep")
    val sweep by transition.animateFloat(-0.6f, 1.6f, infiniteRepeatable(tween(2600, easing = LinearEasing), RepeatMode.Restart), label = "x")
    val gradient = AppTheme.colors.gradient
    val haptic = LocalHapticFeedback.current
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .alpha(alpha)
            .shadow(if (enabled) 18.dp else 0.dp, RoundedCornerShape(16.dp), ambientColor = gradient.first().copy(alpha = 0.6f), spotColor = gradient.first().copy(alpha = 0.6f))
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.horizontalGradient(gradient))
            .clickable(enabled = enabled, interactionSource = interaction, indication = null) { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onClick() },
    ) {
        val w = constraints.maxWidth.toFloat()
        if (enabled) {
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.linearGradient(
                        listOf(Color.Transparent, Color.White.copy(alpha = 0.35f), Color.Transparent),
                        start = Offset(w * sweep - w * 0.25f, 0f), end = Offset(w * sweep + w * 0.25f, 120f),
                    ),
                ),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = Color.White)
            Spacer(Modifier.size(8.dp))
            Icon(Icons.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun LinkRow(mode: AuthMode, onSelect: (AuthMode) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        when (mode) {
            AuthMode.SIGN_IN -> LinkText("Forgot password?") { onSelect(AuthMode.RESET) }
            AuthMode.SIGN_UP -> LinkText("Already have an account? Sign in") { onSelect(AuthMode.SIGN_IN) }
            AuthMode.RESET -> LinkText("Back to sign in") { onSelect(AuthMode.SIGN_IN) }
        }
    }
}

@Composable
private fun LinkText(text: String, onClick: () -> Unit) {
    Text(
        text, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.accent, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center,
        modifier = Modifier.clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick).padding(8.dp),
    )
}

/** Full-body companions need more room than the floating ones. */
private fun companionHeight(kind: CompanionKind): Dp = when (kind) {
    CompanionKind.SANYA, CompanionKind.USAID -> 150.dp
    else -> MascotSize
}
