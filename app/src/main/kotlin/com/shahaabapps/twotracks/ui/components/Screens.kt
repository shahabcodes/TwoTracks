package com.shahaabapps.twotracks.ui.components

import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.components.SectionLabel
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing

/**
 * Scaffold for pushed screens, in the Parallel style: a 44dp back button on
 * its own row with any actions opposite it, then an optional mono [eyebrow]
 * and the screen's title at page-title size, flush with the gutter.
 */
@Composable
fun DetailScaffold(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    backLabel: String = "Back",
    eyebrow: String? = null,
    eyebrowColor: Color = Color.Unspecified,
    actions: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(modifier = modifier, containerColor = AppTheme.colors.groupedBackground) { inner ->
        Column(modifier = Modifier.fillMaxSize().padding(inner)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal - 8.dp, top = 6.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .offset(x = (-10).dp)
                        .size(44.dp)
                        .clip(CircleShape)
                        .clickable(role = Role.Button, onClickLabel = backLabel, onClick = onBack),
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = backLabel, tint = AppTheme.colors.ink)
                }
                Spacer(Modifier.weight(1f))
                actions()
            }
            Column(modifier = Modifier.fillMaxWidth().padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 4.dp, bottom = Spacing.sm)) {
                if (eyebrow != null) SectionLabel(eyebrow, modifier = Modifier.padding(bottom = 6.dp), color = eyebrowColor.takeOrElse { AppTheme.colors.secondaryLabel })
                if (title.isNotEmpty()) Text(
                    text = title,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.semantics { heading() },
                )
            }
            content(PaddingValues(0.dp))
        }
    }
}

/** A text action for a screen's top row (e.g. "Done", "Edit"): blue, semibold. */
@Composable
fun NavAction(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = AppTheme.colors.accentText,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.clickable(role = Role.Button, onClick = onClick).padding(horizontal = 8.dp, vertical = 12.dp),
    )
}

@Composable
fun EmptyState(text: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().padding(Spacing.xl), contentAlignment = Alignment.Center) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.colors.secondaryLabel,
            textAlign = TextAlign.Center,
        )
    }
}
