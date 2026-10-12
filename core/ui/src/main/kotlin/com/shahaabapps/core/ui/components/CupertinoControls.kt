package com.shahaabapps.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Shapes
import com.shahaabapps.core.ui.theme.Spacing

/** A toggle whose "on" track is ink. */
@Composable
fun CupertinoSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        modifier = modifier,
        colors = SwitchDefaults.colors(
            checkedThumbColor = AppTheme.colors.onSolid,
            checkedTrackColor = AppTheme.colors.accent,
            checkedBorderColor = Color.Transparent,
            uncheckedThumbColor = AppTheme.colors.secondaryLabel,
            uncheckedTrackColor = AppTheme.colors.fill,
            uncheckedBorderColor = AppTheme.colors.secondaryLabel,
            disabledCheckedTrackColor = AppTheme.colors.accent.copy(alpha = 0.4f),
            disabledUncheckedTrackColor = AppTheme.colors.fill.copy(alpha = 0.5f),
        ),
    )
}

/** A full-width primary button: solid ink, 12dp radius, 52dp tall. */
@Composable
fun CupertinoPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .clip(Shapes.button)
            .background(if (enabled) AppTheme.colors.accent else AppTheme.colors.accent.copy(alpha = 0.3f))
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(vertical = 14.dp, horizontal = Spacing.md),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = AppTheme.colors.onSolid,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
        )
    }
}

/** A full-width secondary button: the page colour inside a hairline, ink text. */
@Composable
fun CupertinoSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .clip(Shapes.button)
            .border(Spacing.rule, AppTheme.colors.separator, Shapes.button)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(vertical = 14.dp, horizontal = Spacing.md),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = if (enabled) AppTheme.colors.ink else AppTheme.colors.secondaryLabel,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
        )
    }
}

/** A borderless text button (e.g. "Cancel", "Sign out"): blue, or red when [destructive]. */
@Composable
fun CupertinoTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    destructive: Boolean = false,
) {
    val color = if (destructive) AppTheme.colors.destructive else AppTheme.colors.accentText
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 44.dp)
            .clickable(role = Role.Button, onClick = onClick),
    ) {
        Text(
            text = text,
            color = color,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 12.dp),
        )
    }
}
