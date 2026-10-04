package com.shahaabapps.twotracks.feature.lock

import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Brand
import com.shahaabapps.core.ui.theme.Spacing

/** Shows a BiometricPrompt as soon as it appears; an Unlock button retries it. */
@Composable
fun LockScreen(
    allowedAuthenticators: Int,
    onUnlocked: () -> Unit,
) {
    val context = LocalContext.current
    var error by remember { mutableStateOf<String?>(null) }

    fun prompt() {
        val activity = context as? FragmentActivity ?: run { error = "Biometric prompt unavailable"; return }
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) = onUnlocked()
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (errorCode != BiometricPrompt.ERROR_USER_CANCELED && errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) error = errString.toString()
            }
        }
        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Two Tracks")
            .setSubtitle("Use your fingerprint, face or device PIN")
            .setAllowedAuthenticators(allowedAuthenticators)
            .build()
        BiometricPrompt(activity, ContextCompat.getMainExecutor(context), callback).authenticate(info)
    }

    LaunchedEffect(Unit) { prompt() }

    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding().padding(horizontal = Spacing.listHorizontal),
        verticalArrangement = Arrangement.spacedBy(Spacing.md, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.size(72.dp).clip(RoundedCornerShape(20.dp)).background(Brand.accentGradient),
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.Filled.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp)) }
        Text("Two Tracks is locked", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Unlock with your fingerprint, face or device PIN.",
            style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, textAlign = TextAlign.Center,
        )
        error?.let { Text(it, color = AppTheme.colors.destructive, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center) }
        CupertinoPrimaryButton(text = "Unlock", onClick = { error = null; prompt() }, modifier = Modifier.padding(top = Spacing.sm))
    }
}
