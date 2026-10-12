package com.shahaabapps.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Shapes
import com.shahaabapps.core.ui.theme.Spacing

/**
 * An outlined content card: the page colour inside a 1.5dp hairline, 16dp
 * radius, no shadow. [emphasized] draws the outline in ink, for the one card
 * on a screen that is the thing to do next.
 */
@Composable
fun CupertinoCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    emphasized: Boolean = false,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes.contentCard)
            .background(AppTheme.colors.card)
            .border(Spacing.rule, if (emphasized) AppTheme.colors.ink else AppTheme.colors.separator, Shapes.contentCard)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(Spacing.md),
    ) { content() }
}
