package com.shahaabapps.twotracks.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing

/** Which slot a [DialogButton] sits in; decides its look. */
enum class DialogButtonRole { PRIMARY, SECONDARY }

private val LocalDialogButtonRole = compositionLocalOf { DialogButtonRole.SECONDARY }

/**
 * The app's own dialog, replacing the platform AlertDialog everywhere: a
 * rounded card that springs in with a haptic tap, a centred title, free-form
 * content, a full-width filled primary action and lighter secondary actions.
 * The slot names match AlertDialog so screens only swap the composable.
 */
@Composable
fun CupertinoDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
) {
    val haptic = LocalHapticFeedback.current
    val visible = remember { MutableTransitionState(false).apply { targetState = true } }
    LaunchedEffect(Unit) { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove) }
    Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        AnimatedVisibility(
            visibleState = visible,
            enter = scaleIn(initialScale = 0.86f, animationSpec = spring(dampingRatio = 0.62f, stiffness = Spring.StiffnessMediumLow)) + fadeIn(tween(160)),
        ) {
            Column(
                modifier = Modifier
                    .imePadding()
                    .padding(horizontal = 28.dp)
                    .widthIn(max = 400.dp)
                    .shadow(28.dp, RoundedCornerShape(24.dp), ambientColor = Color.Black.copy(alpha = 0.25f), spotColor = Color.Black.copy(alpha = 0.35f))
                    .clip(RoundedCornerShape(24.dp))
                    .background(AppTheme.colors.card)
                    .padding(horizontal = Spacing.lg, vertical = Spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.md),
            ) {
                icon?.let { Box(contentAlignment = Alignment.Center) { it() } }
                title?.let {
                    ProvideTextStyle(MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)) { it() }
                }
                text?.let {
                    Column(modifier = Modifier.fillMaxWidth().heightIn(max = 460.dp).verticalScroll(rememberScrollState())) {
                        ProvideTextStyle(MaterialTheme.typography.bodyMedium.copy(color = AppTheme.colors.secondaryLabel, textAlign = TextAlign.Start)) { it() }
                    }
                }
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.xs), horizontalAlignment = Alignment.CenterHorizontally) {
                    CompositionLocalProvider(LocalDialogButtonRole provides DialogButtonRole.PRIMARY) { confirmButton() }
                    dismissButton?.let { d ->
                        CompositionLocalProvider(LocalDialogButtonRole provides DialogButtonRole.SECONDARY) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) { d() }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Drop-in for a TextButton inside [CupertinoDialog]. In the confirm slot it
 * is a full-width filled pill in the accent (or destructive red); in the
 * dismiss slot a soft pill that presses down with a spring.
 */
@Composable
fun DialogButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    destructive: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    val role = LocalDialogButtonRole.current
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium), label = "press")
    val primary = role == DialogButtonRole.PRIMARY
    val tint = if (destructive) AppTheme.colors.destructive else AppTheme.colors.accent
    val bg = when {
        !enabled -> AppTheme.colors.fill
        primary -> tint
        else -> AppTheme.colors.fill
    }
    val fg = when {
        !enabled -> AppTheme.colors.secondaryLabel
        primary -> Color.White
        else -> tint
    }
    val haptic = LocalHapticFeedback.current
    Row(
        modifier = modifier
            .then(if (primary) Modifier.fillMaxWidth() else Modifier.padding(horizontal = 4.dp))
            .scale(scale)
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .clickable(enabled = enabled, interactionSource = interaction, indication = null) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress); onClick()
            }
            .padding(horizontal = 20.dp, vertical = if (primary) 14.dp else 11.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalContentColor provides fg) {
            ProvideTextStyle(MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, color = fg)) { content() }
        }
    }
}

/** Helper for the common title-only case. */
@Composable
fun DialogTitle(text: String) = Text(text)
