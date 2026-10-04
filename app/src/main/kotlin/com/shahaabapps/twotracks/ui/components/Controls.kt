package com.shahaabapps.twotracks.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Shapes
import com.shahaabapps.core.ui.theme.Spacing

/** iOS-style segmented control. */
@Composable
fun <T> SegmentedChoice(
    options: List<T>,
    selected: T,
    label: (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        options.forEach { option ->
            val isSelected = option == selected
            val bg by animateColorAsState(if (isSelected) AppTheme.colors.card else Color.Transparent, label = "segbg")
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(bg)
                    .clickable { onSelect(option) }
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label(option),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.onSurface else AppTheme.colors.secondaryLabel,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                )
            }
        }
    }
}

/** A rounded horizontal progress bar with an optional colour override. */
@Composable
fun ProgressBar(
    fraction: Float,
    modifier: Modifier = Modifier,
    height: Dp = 10.dp,
    color: Color = AppTheme.colors.accent,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction.coerceIn(0f, 1f))
                .height(height)
                .clip(CircleShape)
                .background(color),
        )
    }
}

/** Small rounded chip used for quick-pick values (15 / 30 / 45 / 60 min). */
@Composable
fun Chip(
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (selected) AppTheme.colors.accent else MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
        )
    }
}

/** Section title in the app's uppercase caption style, inset like list headers. */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.bodySmall,
        color = AppTheme.colors.secondaryLabel,
        modifier = modifier.padding(horizontal = Spacing.listHorizontal),
    )
}

/** Key/value stat tile. */
@Composable
fun StatTile(label: String, value: String, modifier: Modifier = Modifier, valueColor: Color = MaterialTheme.colorScheme.onSurface) {
    Column(
        modifier = modifier
            .clip(Shapes.card)
            .background(AppTheme.colors.card)
            .padding(Spacing.md),
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
        Text(value, style = MaterialTheme.typography.headlineMedium, color = valueColor)
    }
}

/** Compact timezone picker: a list of common zones plus the device default. */
@Composable
fun TimezonePicker(selected: String, onSelect: (String) -> Unit) {
    var open by remember { mutableStateOf(false) }
    Text(
        text = selected,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.field)
            .background(AppTheme.colors.card)
            .clickable { open = true }
            .padding(16.dp),
    )
    if (open) {
        val zones = remember {
            (listOf(java.time.ZoneId.systemDefault().id) + listOf(
                "Asia/Qatar", "Asia/Karachi", "Asia/Dubai", "Asia/Kolkata", "Asia/Riyadh", "Europe/Berlin", "Europe/Dublin",
                "Europe/Amsterdam", "Europe/London", "America/Toronto", "America/Vancouver", "Australia/Sydney", "Australia/Melbourne",
                "Australia/Perth", "Pacific/Auckland", "UTC",
            )).distinct()
        }
        AlertDialog(
            onDismissRequest = { open = false },
            confirmButton = { TextButton(onClick = { open = false }) { Text("Close") } },
            title = { Text("Timezone") },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    zones.forEach { z ->
                        Text(
                            text = z,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (z == selected) AppTheme.colors.accent else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(z); open = false }
                                .padding(vertical = 10.dp),
                        )
                    }
                }
            },
        )
    }
}
