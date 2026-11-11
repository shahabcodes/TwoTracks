package com.shahaabapps.twotracks.feature.learn

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixNormal
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import androidx.compose.foundation.layout.RowScope

/**
 * What the pen is set to: four inks, three nibs, and the eraser.
 *
 * One palette for both places a pen is used — the page of handwriting and
 * the lesson being marked up — because they are the same pen. Somebody who
 * has learned that the third circle is a broad nib on one screen has
 * learned it on the other, and two palettes drifting apart is two sets of
 * habits to keep.
 */
@Composable
fun RowScope.InkPalette(tool: InkTool, onTool: (InkTool) -> Unit) {
    for (ink in INKS) {
        val chosen = !tool.erasing && tool.colour == ink
        Box(
            modifier = Modifier
                .size(if (chosen) 30.dp else 26.dp)
                .clip(CircleShape)
                .background(colourOf(ink))
                .border(
                    if (chosen) 2.dp else Spacing.rule,
                    if (chosen) AppTheme.colors.accent else AppTheme.colors.separator,
                    CircleShape,
                )
                .clickable(role = Role.Button, onClickLabel = "Write in ${NAMES[ink]}") {
                    onTool(tool.copy(colour = ink, erasing = false))
                },
        )
    }
    for (width in WIDTHS) {
        val chosen = !tool.erasing && tool.width == width
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(if (chosen) AppTheme.colors.fill else Color.Transparent)
                .clickable(role = Role.Button, onClickLabel = "A ${WIDTH_NAMES[width]} nib") {
                    onTool(tool.copy(width = width, erasing = false))
                },
        ) {
            Box(
                modifier = Modifier
                    .size((width * 2.2f).dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.ink),
            )
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(if (tool.erasing) AppTheme.colors.fill else Color.Transparent)
            .clickable(role = Role.Button, onClickLabel = "Rub out") {
                onTool(tool.copy(erasing = !tool.erasing))
            },
    ) {
        Icon(
            Icons.Filled.AutoFixNormal, contentDescription = null,
            tint = if (tool.erasing) AppTheme.colors.accentText else AppTheme.colors.secondaryLabel,
            modifier = Modifier.size(20.dp),
        )
    }
}

/** Ink, red for marking, blue for working, and a highlighter's yellow. */
private val INKS = listOf("1C1C20", "C0392B", "1F5FC4", "E3B505")
private val NAMES = mapOf(
    "1C1C20" to "ink", "C0392B" to "red", "1F5FC4" to "blue", "E3B505" to "yellow",
)
private val WIDTHS = listOf(2f, 3.2f, 6f)
private val WIDTH_NAMES = mapOf(2f to "fine", 3.2f to "medium", 6f to "broad")
