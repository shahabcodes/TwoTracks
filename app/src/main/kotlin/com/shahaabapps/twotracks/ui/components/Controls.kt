package com.shahaabapps.twotracks.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.components.CupertinoSegmentedControl
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Shapes
import com.shahaabapps.core.ui.theme.Spacing

/** iOS segmented control (delegates to the shared Cupertino component). */
@Composable
fun <T> SegmentedChoice(
    options: List<T>,
    selected: T,
    label: (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    CupertinoSegmentedControl(options = options, selected = selected, label = label, onSelect = onSelect, modifier = modifier)
}

/** A rounded horizontal progress bar on the translucent gray track. */
@Composable
fun ProgressBar(
    fraction: Float,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
    color: Color = AppTheme.colors.accent,
) {
    val animated by animateFloatAsState(
        targetValue = fraction.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "progress",
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(AppTheme.colors.fill),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animated)
                .height(height)
                .clip(CircleShape)
                .background(color),
        )
    }
}

/**
 * iOS filter pill (Browse Safe PillChip / bg_pill_selected): translucent gray
 * when idle, solid accent with white text when selected. 13sp label, 10dp radius.
 */
@Composable
fun Chip(
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(Shapes.field)
            .background(if (selected) AppTheme.colors.accent else AppTheme.colors.fill)
            .clickable(onClick = onClick)
            .heightIn(min = 32.dp)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = if (selected) Color.White else AppTheme.colors.accent,
        )
    }
}

/** iOS grouped-list section header: 12sp uppercase gray, 18dp above, 8dp below, 4dp extra inset. */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = AppTheme.colors.secondaryLabel,
        modifier = modifier.padding(
            start = Spacing.listHorizontal + Spacing.sectionStart,
            end = Spacing.listHorizontal,
            top = Spacing.xs,
        ),
    )
}

/** Key/value stat tile on a settings-card surface. */
@Composable
fun StatTile(label: String, value: String, modifier: Modifier = Modifier, valueColor: Color = MaterialTheme.colorScheme.onSurface) {
    Column(
        modifier = modifier
            .clip(Shapes.card)
            .background(AppTheme.colors.card)
            .padding(horizontal = Spacing.md, vertical = 12.dp),
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
        Text(value, style = MaterialTheme.typography.headlineMedium, color = valueColor)
    }
}

/** Compact timezone picker: a value field that opens a list of common zones plus the device default. */
@Composable
fun TimezonePicker(selected: String, onSelect: (String) -> Unit) {
    var open by remember { mutableStateOf(false) }
    Text(
        text = selected,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.field)
            .background(AppTheme.colors.fill)
            .clickable { open = true }
            .padding(horizontal = 16.dp, vertical = 14.dp),
    )
    if (open) TimezoneDialog(selected = selected, onSelect = onSelect, onDismiss = { open = false })
}

/**
 * Picking the day your study starts counting.
 *
 * It offers "today" and "clear" alongside the calendar because those are the
 * two answers people actually have: starting now, or undoing a date set by
 * mistake. Future dates are allowed on purpose — someone setting up a space
 * before a course begins is the case this exists for.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartDateDialog(selectedIso: String?, onPick: (String?) -> Unit, onDismiss: () -> Unit) {
    val initial = remember(selectedIso) {
        runCatching { java.time.LocalDate.parse(selectedIso) }.getOrNull()
            ?.atStartOfDay(java.time.ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }
    val pickerState = rememberDatePickerState(initialSelectedDateMillis = initial)
    // Material's own dialog rather than CupertinoDialog: that one puts its
    // content in a vertical scroll, and a DatePicker scrolls internally, so
    // the calendar is measured with no height to fit in and the screen dies.
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val iso = pickerState.selectedDateMillis?.let {
                        java.time.Instant.ofEpochMilli(it).atZone(java.time.ZoneOffset.UTC).toLocalDate().toString()
                    }
                    onPick(iso)
                    onDismiss()
                },
                enabled = pickerState.selectedDateMillis != null,
            ) { Text("Save", color = AppTheme.colors.accent, fontWeight = FontWeight.SemiBold) }
        },
        dismissButton = {
            Row {
                if (selectedIso != null) {
                    TextButton(onClick = { onPick(null); onDismiss() }) {
                        Text("Clear", color = AppTheme.colors.secondaryLabel)
                    }
                }
                TextButton(onClick = onDismiss) { Text("Cancel", color = AppTheme.colors.secondaryLabel) }
            }
        },
    ) {
        // Only the calendar: DatePickerDialog sizes itself around it, and
        // anything added above is clipped off the top. What this date does is
        // said on the Settings row and in its section footer, where it is
        // read before the dialog is ever opened.
        DatePicker(state = pickerState, title = null, headline = null, showModeToggle = false)
    }
}

/** The timezone list as a dialog, for use behind a settings row. */
@Composable
fun TimezoneDialog(selected: String, onSelect: (String) -> Unit, onDismiss: () -> Unit) {
    run {
        val zones = remember {
            (listOf(java.time.ZoneId.systemDefault().id) + listOf(
                "Asia/Qatar", "Asia/Karachi", "Asia/Dubai", "Asia/Kolkata", "Asia/Riyadh", "Europe/Berlin", "Europe/Dublin",
                "Europe/Amsterdam", "Europe/London", "America/Toronto", "America/Vancouver", "Australia/Sydney", "Australia/Melbourne",
                "Australia/Perth", "Pacific/Auckland", "UTC",
            )).distinct()
        }
        CupertinoDialog(
            onDismissRequest = onDismiss,
            confirmButton = { DialogButton(onClick = onDismiss) { Text("Close") } },
            title = { Text("Timezone") },
            text = {
                // No scroll of its own. CupertinoDialog already puts its text
                // slot in a bounded, scrolling column, and a second scroller
                // inside it is measured with no height to fit in, which threw
                // the moment this dialog opened.
                Column {
                    zones.forEach { z ->
                        Text(
                            text = z,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (z == selected) AppTheme.colors.accent else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(z); onDismiss() }
                                .padding(vertical = 10.dp),
                        )
                    }
                }
            },
        )
    }
}
