package com.shahaabapps.twotracks.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing

/** What the sheet starts with; goal/component are pre-selected when launched from a session. */
data class LogTarget(
    val goalId: String,
    val componentId: String?,
    val sessionId: String? = null,
    val defaultMinutes: Int = 30,
    val dateIso: String,
    val isClass: Boolean = false,
)

/**
 * Bottom sheet to log minutes against a goal component. For class sessions the
 * sheet shows every component with its own minute field so a two-hour class
 * can be split across Listening / Reading / Writing / Speaking.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LogMinutesSheet(
    target: LogTarget,
    goals: List<StudyGoal>,
    onDismiss: () -> Unit,
    onLog: (goalId: String, componentId: String, minutes: Int, note: String) -> Unit,
    onLogSplit: (goalId: String, minutesByComponent: Map<String, Int>, note: String) -> Unit,
) {
    val goal = goals.firstOrNull { it.id == target.goalId } ?: return
    var componentId by remember { mutableStateOf(target.componentId ?: goal.components.firstOrNull()?.id.orEmpty()) }
    var minutesText by remember { mutableStateOf(target.defaultMinutes.toString()) }
    var note by remember { mutableStateOf("") }
    var split by remember { mutableStateOf(target.isClass) }
    val splitMinutes = remember { mutableStateOf(goal.components.associate { it.id to 0 }) }

    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = AppTheme.colors.groupedBackground) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.lg)
                .padding(bottom = Spacing.xl)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            Text(goal.title, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = if (split) "Split the time across skills" else "Pick a skill and how long you studied",
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.colors.secondaryLabel,
            )
            if (goal.components.size > 1) {
                SegmentedChoice(
                    options = listOf(false, true),
                    selected = split,
                    label = { if (it) "Split across skills" else "One skill" },
                    onSelect = { split = it },
                )
            }

            if (!split) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), verticalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.fillMaxWidth()) {
                    goal.components.forEach { c ->
                        Chip(text = c.name, selected = c.id == componentId, onClick = { componentId = c.id })
                    }
                }
                FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    listOf(15, 30, 45, 60, 90).forEach { m ->
                        Chip(text = "$m min", selected = minutesText == m.toString(), onClick = { minutesText = m.toString() })
                    }
                }
                CupertinoTextField(
                    value = minutesText, onValueChange = { minutesText = it.filter { ch -> ch.isDigit() }.take(3) },
                    placeholder = "Minutes", keyboardType = KeyboardType.Number,
                )
            } else {
                goal.components.forEach { c ->
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                        Text(c.name, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                        CupertinoTextField(
                            value = splitMinutes.value[c.id]?.takeIf { it > 0 }?.toString().orEmpty(),
                            onValueChange = { v ->
                                splitMinutes.value = splitMinutes.value + (c.id to (v.filter { ch -> ch.isDigit() }.take(3).toIntOrNull() ?: 0))
                            },
                            placeholder = "min", keyboardType = KeyboardType.Number,
                            modifier = Modifier.weight(0.6f),
                        )
                    }
                }
                val total = splitMinutes.value.values.sum()
                Text("Total: $total min", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
            }
            CupertinoTextField(value = note, onValueChange = { note = it }, placeholder = "Note (optional)")

            val canSave = if (split) splitMinutes.value.values.sum() > 0 else (minutesText.toIntOrNull() ?: 0) > 0
            CupertinoPrimaryButton(
                text = "Save",
                enabled = canSave,
                onClick = {
                    if (split) {
                        onLogSplit(goal.id, splitMinutes.value.filterValues { it > 0 }, note)
                    } else {
                        onLog(goal.id, componentId, minutesText.toIntOrNull() ?: 0, note)
                    }
                },
            )
        }
    }
}
