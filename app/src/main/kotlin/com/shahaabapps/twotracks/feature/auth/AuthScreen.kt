package com.shahaabapps.twotracks.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Brand
import com.shahaabapps.core.ui.theme.Spacing

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.md, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Brand.accentGradient),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Filled.Timeline, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
        }
        Text(
            text = if (state.isSignUp) "Create your account" else "Two Tracks",
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = "Two tracks, run in parallel. Track every study hour toward the move.",
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.colors.secondaryLabel,
            textAlign = TextAlign.Center,
        )

        CupertinoTextField(value = state.email, onValueChange = viewModel::onEmailChange, placeholder = "Email", keyboardType = KeyboardType.Email)
        CupertinoTextField(
            value = state.password, onValueChange = viewModel::onPasswordChange, placeholder = "Password (6+ characters)",
            keyboardType = KeyboardType.Password, isPassword = true,
        )

        state.errorMessage?.let { Text(it, color = AppTheme.colors.destructive, style = MaterialTheme.typography.bodySmall) }
        state.infoMessage?.let { Text(it, color = AppTheme.colors.success, style = MaterialTheme.typography.bodySmall) }

        CupertinoPrimaryButton(
            text = when {
                state.isSubmitting -> "Please wait"
                state.isSignUp -> "Sign up"
                else -> "Sign in"
            },
            onClick = viewModel::submit,
            enabled = state.canSubmit,
            modifier = Modifier.padding(top = Spacing.sm),
        )
        CupertinoTextButton(
            text = if (state.isSignUp) "Already have an account? Sign in" else "New here? Create an account",
            onClick = viewModel::toggleMode,
        )
        if (!state.isSignUp) {
            CupertinoTextButton(text = "Forgot password", onClick = viewModel::resetPassword)
        }
    }
}
