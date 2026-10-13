package com.shahaabapps.twotracks.feature.lock

import androidx.compose.foundation.layout.Spacer
import com.shahaabapps.twotracks.ui.board.BoardFooter
import com.shahaabapps.twotracks.feature.auth.BoardLock
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import com.shahaabapps.twotracks.ui.components.TwoTracksMark
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing

/**
 * Shows a BiometricPrompt as soon as it appears; the fingerprint button
 * retries it. Drawn as the Parallel lock: the two bars, the line, one button.
 */
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

    if (AppTheme.departures) {
        val now = java.time.LocalDateTime.now()
        Column(modifier = Modifier.fillMaxSize().background(AppTheme.colors.deep).systemBarsPadding()) {
            BoardLock(
                date = now.format(java.time.format.DateTimeFormatter.ofPattern("EEE d MMM", java.util.Locale.ENGLISH)).uppercase(java.util.Locale.ROOT),
                time = now.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")),
                error = error, onUnlock = { error = null; prompt() },
            )
            Spacer(Modifier.weight(1f))
            // The prompt offers the device PIN as well; this says so, as the prototype does.
            BoardFooter("", "Use your device PIN", onClick = { error = null; prompt() }, modifier = Modifier.padding(bottom = 40.dp))
        }
        return
    }

    Column(
        modifier = Modifier.fillMaxSize().background(AppTheme.colors.groupedBackground).systemBarsPadding().padding(horizontal = Spacing.listHorizontal),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TwoTracksMark(height = 64.dp)
        Text(
            "Two Tracks is locked", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center, modifier = Modifier.padding(top = 28.dp).semantics { heading() },
        )
        Text(
            "Your plan, your partner's progress and your documents stay behind your fingerprint, face or device PIN.",
            style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp).widthIn(max = 300.dp),
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 52.dp)
                .size(96.dp)
                .clip(CircleShape)
                .border(Spacing.rule, AppTheme.colors.ink, CircleShape)
                .clickable(role = Role.Button, onClickLabel = "Unlock") { error = null; prompt() },
        ) { Icon(Icons.Filled.Fingerprint, contentDescription = "Unlock", tint = AppTheme.colors.ink, modifier = Modifier.size(44.dp)) }
        Text("Touch to unlock", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = 14.dp))
        error?.let {
            Text(it, color = AppTheme.colors.destructive, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, modifier = Modifier.padding(top = Spacing.md).semantics { liveRegion = LiveRegionMode.Polite })
        }
    }
}
