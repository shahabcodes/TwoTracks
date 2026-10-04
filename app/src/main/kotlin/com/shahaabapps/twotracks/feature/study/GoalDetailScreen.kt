package com.shahaabapps.twotracks.feature.study

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoSwitch
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.LogMinutesSheet
import com.shahaabapps.twotracks.ui.components.LogTarget
import com.shahaabapps.twotracks.ui.components.ProgressBar
import com.shahaabapps.twotracks.ui.components.SectionLabel
import com.shahaabapps.twotracks.ui.components.StatTile

@Composable
fun GoalDetailScreen(
    goalId: String,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    viewModel: GoalDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    var logTarget by remember { mutableStateOf<LogTarget?>(null) }
    var showMock by remember { mutableStateOf(false) }
    LaunchedEffect(state.message) { state.message?.let { snackbar.showSnackbar(it); viewModel.clearMessage() } }

    val goal = state.goal
    val p = state.progress
    DetailScaffold(
        title = goal?.title ?: "Goal",
        onBack = onBack,
        actions = { if (state.isMine) IconButton(onClick = onEdit) { Icon(Icons.Filled.Edit, "Edit", tint = AppTheme.colors.accent) } },
    ) {
        if (goal == null || p == null) return@DetailScaffold
        Box {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
                item {
                    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                        Text(goal.subtitle, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                        if (goal.why.isNotBlank()) Text(goal.why, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                            StatTile("Logged", "%.1f h".format(p.hoursLogged), modifier = Modifier.weight(1f))
                            StatTile("Required", "%.0f h".format(p.hoursRequired), modifier = Modifier.weight(1f))
                            StatTile("Left", "%.0f h".format(p.remainingHours), modifier = Modifier.weight(1f))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                            val (label, color) = feasibilityLabel(p.feasibility)
                            StatTile("Need / week", p.weeklyNeedHours?.let { "%.1f h".format(it) } ?: "no date", modifier = Modifier.weight(1f))
                            StatTile("Recent pace", "%.1f h".format(p.recentWeeklyHours), modifier = Modifier.weight(1f))
                            StatTile("Status", label ?: "-", modifier = Modifier.weight(1f), valueColor = color)
                        }
                        Text(
                            buildString {
                                p.targetDate?.let { append("Target ${Clock.formatDate(it)}") }
                                p.forecastDate?.let { if (!p.isComplete) append(if (isEmpty()) "Forecast ${Clock.formatDate(it)}" else ", forecast ${Clock.formatDate(it)} at the recent pace") }
                                if (isEmpty()) append("Set a target date to get a pace.")
                            },
                            style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                        )
                        if (state.isMine) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Scheduled by the planner", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                                CupertinoSwitch(checked = goal.active, onCheckedChange = viewModel::setActive)
                            }
                        }
                    }
                }

                item { SectionLabel("Skills") }
                item {
                    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        p.components.forEachIndexed { i, c ->
                            Column(modifier = Modifier.padding(top = if (i == 0) 0.dp else Spacing.md)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(c.component.name, style = MaterialTheme.typography.titleMedium)
                                        Text(
                                            "%.1f / %.0f h".format(c.hoursLogged, c.hoursRequired) + (c.component.targetScore?.let { "   target $it" } ?: ""),
                                            style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                                        )
                                    }
                                    if (state.isMine) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                                            if (state.timer == null) Chip(text = "Timer", onClick = { viewModel.startTimer(c.component.id) })
                                            Chip(text = "Log", onClick = {
                                                logTarget = LogTarget(goalId = goal.id, componentId = c.component.id, dateIso = state.todayIso)
                                            })
                                        }
                                    }
                                }
                                ProgressBar(fraction = c.fraction.toFloat(), modifier = Modifier.padding(top = Spacing.xs), height = 8.dp)
                            }
                        }
                        if (state.timer != null && state.timer!!.goalId == goal.id) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = Spacing.md)) {
                                Text("Timer running: ${state.timer!!.elapsedMinutes()} min", modifier = Modifier.weight(1f), color = AppTheme.colors.accent)
                                Chip(text = "Stop and log", onClick = viewModel::stopTimer)
                            }
                        }
                    }
                }

                item { SectionLabel("Last 14 days") }
                item {
                    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        val max = (state.last14.maxOfOrNull { it.minutes } ?: 0).coerceAtLeast(60)
                        Row(modifier = Modifier.fillMaxWidth().height(80.dp), horizontalArrangement = Arrangement.spacedBy(3.dp), verticalAlignment = Alignment.Bottom) {
                            state.last14.forEach { bar ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(fraction = (bar.minutes.toFloat() / max).coerceIn(0.04f, 1f))
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(if (bar.minutes > 0) AppTheme.colors.accent else MaterialTheme.colorScheme.surfaceVariant),
                                )
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth().padding(top = Spacing.xs)) {
                            Text(Clock.formatDayMonth(state.last14.first().date), style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.weight(1f))
                            Text("today", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
                        }
                    }
                }

                item {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = Spacing.listHorizontal)) {
                        SectionLabel("Mock scores", modifier = Modifier.weight(1f))
                        if (state.isMine) Chip(text = "+ Add", onClick = { showMock = true })
                    }
                }
                if (state.mockScores.isEmpty()) {
                    item {
                        Text("No practice-test results yet.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(horizontal = Spacing.listHorizontal))
                    }
                } else {
                    item {
                        CupertinoListSection {
                            state.mockScores.forEachIndexed { i, m ->
                                CupertinoListRow(
                                    title = m.scores.entries.joinToString("  ") { (k, v) -> "${goal.components.firstOrNull { it.id == k }?.name?.take(1) ?: k} $v" },
                                    subtitle = Clock.formatDate(m.dateIso) + if (m.note.isNotBlank()) ", ${m.note}" else "",
                                    showDivider = i < state.mockScores.lastIndex,
                                    trailing = { if (state.isMine) IconButton(onClick = { viewModel.deleteMockScore(m.id) }) { Icon(Icons.Filled.Delete, "Delete", tint = AppTheme.colors.secondaryLabel) } },
                                )
                            }
                        }
                    }
                }

                if (state.recentLogs.isNotEmpty()) {
                    item { SectionLabel("Recent entries") }
                    item {
                        CupertinoListSection {
                            state.recentLogs.forEachIndexed { i, log ->
                                val name = goal.components.firstOrNull { it.id == log.componentId }?.name ?: log.componentId
                                CupertinoListRow(
                                    title = "$name, ${log.minutes} min" + if (log.fromClass) " (class)" else "",
                                    subtitle = Clock.formatDate(log.dateIso) + if (log.note.isNotBlank()) ", ${log.note}" else "",
                                    showDivider = i < state.recentLogs.lastIndex,
                                    trailing = { if (state.isMine) IconButton(onClick = { viewModel.deleteLog(log.id) }) { Icon(Icons.Filled.Delete, "Delete", tint = AppTheme.colors.secondaryLabel) } },
                                )
                            }
                        }
                    }
                }
            }
            SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }

    logTarget?.let { target ->
        LogMinutesSheet(
            target = target, goals = state.goals, onDismiss = { logTarget = null },
            onLog = { _, c, m, n -> viewModel.log(c, m, n, target.dateIso); logTarget = null },
            onLogSplit = { _, map, n -> viewModel.logSplit(map, n, target.dateIso); logTarget = null },
        )
    }

    if (showMock && goal != null) {
        val values = remember { mutableStateOf(goal.components.associate { it.id to "" }) }
        var note by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showMock = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.addMockScore(values.value.filterValues { it.isNotBlank() }, note); showMock = false
                }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showMock = false }) { Text("Cancel") } },
            title = { Text("Mock test result") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    goal.components.forEach { c ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                            Text(c.name, modifier = Modifier.weight(1f))
                            Box(modifier = Modifier.width(96.dp)) {
                                CupertinoTextField(value = values.value[c.id].orEmpty(), onValueChange = { v -> values.value = values.value + (c.id to v) }, placeholder = c.targetScore ?: "score")
                            }
                        }
                    }
                    CupertinoTextField(value = note, onValueChange = { note = it }, placeholder = "Note (e.g. Cambridge 18 test 2)")
                }
            },
        )
    }
}
