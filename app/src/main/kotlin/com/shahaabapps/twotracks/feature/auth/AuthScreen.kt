package com.shahaabapps.twotracks.feature.auth

import com.shahaabapps.twotracks.ui.board.BoardGutter
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.components.SectionLabel
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.LoadingOverlay
import com.shahaabapps.twotracks.ui.components.TwoTracksMark
import kotlin.math.roundToInt

/**
 * Sign in, sign up and reset, as the Parallel prototype draws them: the two
 * bars and the name on a white page, labelled fields that are only a line,
 * one ink button, and the way to the other two modes at the foot.
 *
 * Sign up is step 1 of 3; onboarding is the other two.
 */
@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    if (state.isSubmitting) LoadingOverlay(if (state.mode == AuthMode.RESET) "Sending the link" else if (state.isSignUp) "Creating your account" else "Signing you in")

    // One shake per failure.
    val shake = remember { Animatable(0f) }
    LaunchedEffect(state.errorTick) {
        if (state.errorTick == 0) return@LaunchedEffect
        shake.animateTo(0f, keyframes { durationMillis = 420; 0f at 0; -14f at 60; 12f at 120; -8f at 180; 5f at 240; -2f at 300; 0f at 420 })
    }

    val departures = AppTheme.departures
    val actions = remember(viewModel) {
        AuthActions(viewModel::onEmailChange, viewModel::onPasswordChange, viewModel::onConfirmChange, viewModel::setMode, viewModel::submit)
    }
    AnimatedContent(
        targetState = state.mode,
        transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
        label = "mode",
        modifier = modifier.fillMaxSize().background(AppTheme.colors.groupedBackground),
    ) { mode ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = if (departures) BoardGutter else Spacing.listHorizontal)
                .offset { IntOffset(shake.value.roundToInt(), 0) },
        ) {
            if (departures) {
                BoardAuth(state.copy(mode = mode), actions)
            } else when (mode) {
                AuthMode.SIGN_IN -> SignIn(state, viewModel)
                AuthMode.SIGN_UP -> SignUp(state, viewModel)
                AuthMode.RESET -> Reset(state, viewModel)
            }
        }
    }
}

@Composable
private fun ColumnScope.SignIn(state: AuthUiState, viewModel: AuthViewModel) {
    var shown by remember { mutableStateOf(false) }
    Spacer(Modifier.height(64.dp))
    TwoTracksMark()
    Text(
        "Two Tracks",
        style = MaterialTheme.typography.displayLarge.copy(fontSize = 40.sp, lineHeight = 42.sp, letterSpacing = (-1.2).sp),
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 24.dp).semantics { heading() },
    )
    Text(
        "Two people, one plan. Pick up where the two of you left off.",
        style = MaterialTheme.typography.bodyLarge, color = AppTheme.colors.secondaryLabel,
        modifier = Modifier.padding(top = 10.dp),
    )
    Column(modifier = Modifier.padding(top = 36.dp), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        EmailField(state, viewModel)
        CupertinoTextField(
            value = state.password, onValueChange = viewModel::onPasswordChange,
            label = "Password", keyboardType = KeyboardType.Password,
            visualTransformation = if (shown) VisualTransformation.None else PasswordVisualTransformation(),
            labelTrailing = { InlineLink("Forgot?") { viewModel.setMode(AuthMode.RESET) } },
            trailing = { ShowPassword(shown) { shown = !shown } },
        )
    }
    Messages(state)
    SubmitButton("Sign in", state, viewModel, Modifier.padding(top = 28.dp))
    Spacer(Modifier.weight(1f).heightIn(min = Spacing.xl))
    Footer("New here?", "Create your space") { viewModel.setMode(AuthMode.SIGN_UP) }
}

@Composable
private fun ColumnScope.SignUp(state: AuthUiState, viewModel: AuthViewModel) {
    var shown by remember { mutableStateOf(false) }
    val transform = if (shown) VisualTransformation.None else PasswordVisualTransformation()
    BackButton("Back to sign in") { viewModel.setMode(AuthMode.SIGN_IN) }
    SectionLabel("Step 1 of 3", modifier = Modifier.padding(top = 18.dp))
    Title("Start your two tracks")
    Text(
        "One space for both of you. Every study hour counts toward the move.",
        style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
        modifier = Modifier.padding(top = 10.dp),
    )
    Column(modifier = Modifier.padding(top = 26.dp), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        EmailField(state, viewModel)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            CupertinoTextField(
                value = state.password, onValueChange = viewModel::onPasswordChange,
                label = "Password", placeholder = "6 characters or more", keyboardType = KeyboardType.Password,
                visualTransformation = transform,
                trailing = { ShowPassword(shown) { shown = !shown } },
            )
            StrengthMeter(state.password)
        }
        val mismatch = state.confirmPassword.isNotEmpty() && !state.passwordsMatch
        CupertinoTextField(
            value = state.confirmPassword, onValueChange = viewModel::onConfirmChange,
            label = "Same again", keyboardType = KeyboardType.Password,
            visualTransformation = transform, isError = mismatch,
        )
        AnimatedVisibility(visible = mismatch, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
            Text("The two passwords differ.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.destructive)
        }
    }
    Messages(state)
    SubmitButton("Create account", state, viewModel, Modifier.padding(top = 24.dp))
    Spacer(Modifier.weight(1f).heightIn(min = Spacing.xl))
    Footer("Already have an account?", "Sign in") { viewModel.setMode(AuthMode.SIGN_IN) }
}

@Composable
private fun ColumnScope.Reset(state: AuthUiState, viewModel: AuthViewModel) {
    BackButton("Back to sign in") { viewModel.setMode(AuthMode.SIGN_IN) }
    Spacer(Modifier.height(22.dp))
    Title("Reset your password")
    Text(
        "Type the email you signed up with and we will send a link to choose a new one. Your plan and progress stay as they are.",
        style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
        modifier = Modifier.padding(top = 10.dp),
    )
    Box(Modifier.padding(top = 28.dp)) { EmailField(state, viewModel) }
    Messages(state)
    SubmitButton(if (state.infoMessage != null) "Send again" else "Send reset link", state, viewModel, Modifier.padding(top = 24.dp), arrow = false)
    Spacer(Modifier.weight(1f).heightIn(min = Spacing.xl))
    Box(Modifier.fillMaxWidth().padding(bottom = 36.dp), contentAlignment = Alignment.Center) {
        InlineLink("Back to sign in", underline = true) { viewModel.setMode(AuthMode.SIGN_IN) }
    }
}

// ------------------------------------------------------------------ pieces

@Composable
private fun EmailField(state: AuthUiState, viewModel: AuthViewModel) {
    CupertinoTextField(
        value = state.email, onValueChange = viewModel::onEmailChange,
        label = "Email", keyboardType = KeyboardType.Email,
        isError = state.email.isNotEmpty() && !state.emailValid,
    )
}

@Composable
private fun Title(text: String) {
    Text(
        text, style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp).semantics { heading() },
    )
}

@Composable
private fun BackButton(label: String, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(top = 8.dp)
            .offset(x = (-10).dp)
            .size(44.dp)
            .clip(CircleShape)
            .clickable(role = Role.Button, onClickLabel = label, onClick = onClick),
    ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = label, tint = AppTheme.colors.ink)
    }
}

@Composable
private fun ShowPassword(shown: Boolean, onToggle: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(40.dp).clip(CircleShape).clickable(role = Role.Button, onClick = onToggle),
    ) {
        Icon(
            if (shown) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
            contentDescription = if (shown) "Hide password" else "Show password",
            tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun InlineLink(text: String, underline: Boolean = false, onClick: () -> Unit) {
    Text(
        text,
        style = MaterialTheme.typography.labelLarge.copy(textDecoration = if (underline) TextDecoration.Underline else TextDecoration.None),
        color = if (underline) AppTheme.colors.ink else AppTheme.colors.accentText,
        modifier = Modifier.clip(CircleShape).clickable(role = Role.Button, onClick = onClick).padding(horizontal = 4.dp, vertical = 12.dp),
    )
}

@Composable
private fun Footer(lead: String, action: String, onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
    ) {
        Text(lead, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
        Spacer(Modifier.size(4.dp))
        InlineLink(action, underline = true, onClick = onClick)
    }
}

@Composable
private fun SubmitButton(text: String, state: AuthUiState, viewModel: AuthViewModel, modifier: Modifier = Modifier, arrow: Boolean = true) {
    Box(modifier) {
        CupertinoPrimaryButton(text = if (arrow) "$text  →" else text, onClick = viewModel::submit, enabled = state.canSubmit)
    }
}

/** An error or a confirmation under the form: a rule in its colour, then the words. */
@Composable
private fun Messages(state: AuthUiState) {
    AnimatedVisibility(visible = state.errorMessage != null, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
        Note(state.errorMessage.orEmpty(), AppTheme.colors.destructive, title = null)
    }
    AnimatedVisibility(visible = state.infoMessage != null, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
        Note(state.infoMessage.orEmpty(), AppTheme.colors.success, title = "Check your inbox")
    }
}

@Composable
private fun Note(text: String, color: Color, title: String?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(top = 18.dp).semantics { liveRegion = LiveRegionMode.Polite },
    ) {
        HorizontalDivider(color = color, thickness = Spacing.rule)
        if (title != null) Text(title, style = MaterialTheme.typography.labelLarge, color = color, modifier = Modifier.padding(top = 10.dp))
        Text(
            text, style = MaterialTheme.typography.bodyMedium,
            color = if (title == null) color else AppTheme.colors.secondaryLabel,
            modifier = Modifier.padding(top = if (title == null) 10.dp else 0.dp),
        )
    }
}

/** Four segments that fill as the password gets longer and more varied, and a word for it. */
@Composable
private fun StrengthMeter(password: String) {
    val score = passwordScore(password)
    val (word, color) = when (score) {
        0 -> "" to AppTheme.colors.separator
        1 -> "Weak" to AppTheme.colors.destructive
        2 -> "Fair" to AppTheme.colors.warn
        3 -> "Good" to AppTheme.colors.success
        else -> "Strong" to AppTheme.colors.success
    }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.weight(1f)) {
            repeat(4) { i ->
                Box(Modifier.weight(1f).height(3.dp).background(if (i < score) color else AppTheme.colors.separator))
            }
        }
        if (word.isNotEmpty()) Text(word, style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp), color = color)
    }
}

/** 0 for nothing typed, then 1 to 4: length past 6 and past 10, mixed case, and a digit or symbol. */
internal fun passwordScore(password: String): Int {
    if (password.isEmpty()) return 0
    var score = 1
    if (password.length >= 10) score++
    if (password.any { it.isUpperCase() } && password.any { it.isLowerCase() }) score++
    if (password.any { !it.isLetter() }) score++
    if (password.length < 6) score = 1
    return score.coerceAtMost(4)
}
