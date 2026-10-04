package com.shahaabapps.twotracks.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.SegmentedChoice
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

/** Edits the signed-in person's weekly template: work, class and study blocks per weekday. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AvailabilityScreen(onBack: () -> Unit, viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var blocks by remember { mutableStateOf<List<AvailabilityBlock>?>(null) }
    var adding by remember { mutableStateOf<Int?>(null) }
    LaunchedEffect(state.availability) { if (blocks == null && state.session != null) blocks = state.availability.blocks }
    val current = blocks ?: return

    DetailScaffold(title = "Weekly template", onBack = onBack) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(bottom = Spacing.xl), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
            Text(
                "Work blocks are unavailable. Study blocks get filled by the planner. Class blocks log time to a fixed goal. Times are in your timezone.",
                style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(horizontal = Spacing.listHorizontal),
            )
            (1..7).forEach { day ->
                val dayBlocks = current.filter { it.dayOfWeek == day }.sortedBy { it.startMinutes }
                CupertinoListSection(header = DayOfWeek.of(day).getDisplayName(TextStyle.FULL, Locale.ENGLISH)) {
                    dayBlocks.forEachIndexed { i, b ->
                        val goalName = b.goalId?.let { id -> state.myGoals.firstOrNull { it.id == id }?.title }
                        CupertinoListRow(
                            title = "${Dates.minutesToClock(b.startMinutes)} to ${Dates.minutesToClock(b.endMinutes)}  ${b.label.ifBlank { b.kind.name.lowercase() }}",
                            subtitle = b.kind.name.lowercase().replaceFirstChar { it.uppercase() } + (goalName?.let { ", $it" } ?: ""),
                            showDivider = true,
                            trailing = { IconButton(onClick = { blocks = current - b }) { Icon(Icons.Filled.Delete, "Remove", tint = AppTheme.colors.secondaryLabel) } },
                        )
                    }
                    CupertinoTextButton(text = "+ Add block", onClick = { adding = day })
                }
            }
            Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                CupertinoPrimaryButton(text = "Save template", onClick = { viewModel.saveAvailability(current); onBack() }, modifier = Modifier.fillMaxWidth())
            }
        }
    }

    adding?.let { day ->
        var kind by remember { mutableStateOf(BlockKind.STUDY) }
        var start by remember { mutableStateOf("16:15") }
        var end by remember { mutableStateOf("17:30") }
        var label by remember { mutableStateOf("") }
        var goalId by remember { mutableStateOf(state.myGoals.firstOrNull { it.active }?.id) }
        fun parse(t: String): Int? = t.split(":").takeIf { it.size == 2 }?.let { (h, m) -> h.toIntOrNull()?.let { hh -> m.toIntOrNull()?.let { mm -> hh * 60 + mm } } }
        val valid = parse(start) != null && parse(end) != null && parse(end)!! > parse(start)!!
        AlertDialog(
            onDismissRequest = { adding = null },
            confirmButton = {
                TextButton(enabled = valid, onClick = {
                    val s = parse(start)!!; val e = parse(end)!!
                    blocks = current + AvailabilityBlock(
                        id = "${kind.name.lowercase()}-$day-$s-${System.currentTimeMillis() % 100000}", dayOfWeek = day, startMinutes = s, endMinutes = e, kind = kind,
                        label = label.ifBlank { when (kind) { BlockKind.WORK -> "Work"; BlockKind.CLASS -> "Class"; BlockKind.STUDY -> "Study slot" } },
                        goalId = if (kind == BlockKind.CLASS) goalId else null,
                    )
                    adding = null
                }) { Text("Add") }
            },
            dismissButton = { TextButton(onClick = { adding = null }) { Text("Cancel") } },
            title = { Text(DayOfWeek.of(day).getDisplayName(TextStyle.FULL, Locale.ENGLISH)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    SegmentedChoice(options = BlockKind.entries, selected = kind, label = { it.name.lowercase().replaceFirstChar { c -> c.uppercase() } }, onSelect = { kind = it })
                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                        CupertinoTextField(value = start, onValueChange = { start = it }, placeholder = "Start HH:MM", keyboardType = KeyboardType.Number, modifier = Modifier.weight(1f))
                        CupertinoTextField(value = end, onValueChange = { end = it }, placeholder = "End HH:MM", keyboardType = KeyboardType.Number, modifier = Modifier.weight(1f))
                    }
                    CupertinoTextField(value = label, onValueChange = { label = it }, placeholder = "Label (optional)")
                    if (kind == BlockKind.CLASS) {
                        Text("Class counts toward", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                            state.myGoals.take(4).forEach { g -> Chip(text = g.title.take(14), selected = g.id == goalId, onClick = { goalId = g.id }) }
                        }
                    }
                }
            },
        )
    }
}
