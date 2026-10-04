package com.shahaabapps.twotracks.feature.today

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.ui.components.Avatar
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.components.Hero
import com.shahaabapps.core.ui.components.StatusChip
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.LogMinutesSheet
import com.shahaabapps.twotracks.ui.components.LogTarget
import com.shahaabapps.twotracks.ui.components.SectionLabel
import kotlinx.coroutines.delay

@Composable
fun TodayScreen(
    onOpenGoal: (String) -> Unit,
    viewModel: TodayViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var logTarget by remember { mutableStateOf<LogTarget?>(null) }
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let { snackbar.showSnackbar(it); viewModel.clearMessage() }
    }

    val me = state.me
    androidx.compose.foundation.layout.Box {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = Spacing.xl),
        ) {
            item {
                Hero(
                    title = "Today",
                    subtitle = Clock.formatLong(state.today),
                ) {
                    if (me != null) {
                        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.lg), modifier = Modifier.padding(top = Spacing.sm)) {
                            HeroStat("${me.todayMinutes} min", "studied today")
                            HeroStat("${me.streak} day${if (me.streak == 1) "" else "s"}", "streak")
                            state.capacity?.let { cap ->
                                HeroStat(
                                    "%.0f / %.0f h".format(cap.weeklyNeedHours, cap.weeklyCapacityHours),
                                    if (cap.fits) "need vs capacity" else "need vs capacity, over",
                                )
                            }
                        }
                    }
                }
            }

            // Running timer
            state.timer?.let { t ->
                item {
                    var tick by remember { mutableStateOf(0) }
                    LaunchedEffect(Unit) { while (true) { delay(30_000); tick++ } }
                    val elapsed = remember(tick) { t.elapsedMinutes() }
                    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Timer running", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                                Text(state.timerGoalLabel, style = MaterialTheme.typography.titleMedium)
                                Text("$elapsed min so far", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.accent)
                            }
                            IconButton(onClick = viewModel::stopTimer) {
                                Icon(Icons.Filled.Stop, contentDescription = "Stop and log", tint = AppTheme.colors.destructive)
                            }
                        }
                        CupertinoTextButton(text = "Discard timer", onClick = viewModel::discardTimer, destructive = true)
                    }
                }
            }

            // My sessions
            if (me != null) {
                item { SectionLabel("Your sessions") }
                if (me.sessions.isEmpty()) {
                    item {
                        CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                            Text("No study slot today. Log time anyway from the Study tab, or add a block in Settings.", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                        }
                    }
                }
                items(me.sessions, key = { it.session.id }) { row ->
                    val done = row.session.status == SessionStatus.DONE
                    val skipped = row.session.status == SessionStatus.SKIPPED
                    CupertinoCard(
                        modifier = Modifier.padding(horizontal = Spacing.listHorizontal),
                        onClick = { row.goal?.let { onOpenGoal(it.id) } },
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                            Icon(
                                imageVector = if (done) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (done) AppTheme.colors.success else AppTheme.colors.secondaryLabel,
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(row.goal?.title ?: "Session", style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "${row.componentName} at ${row.timeLabel} (${row.session.durationMinutes} min)",
                                    style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
                                )
                            }
                            if (row.session.isClass) StatusChip("Class", AppTheme.colors.accent)
                            if (skipped) StatusChip("Skipped", AppTheme.colors.warn)
                        }
                        if (!done && !skipped) {
                            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.padding(top = Spacing.sm)) {
                                if (!row.session.isClass && state.timer == null) {
                                    Chip(text = "Start timer", onClick = { viewModel.startTimer(row.session.goalId, row.session.componentId, row.session.id) })
                                }
                                Chip(text = if (row.session.isClass) "Log class" else "Log minutes", onClick = {
                                    logTarget = LogTarget(
                                        goalId = row.session.goalId, componentId = row.session.componentId, sessionId = row.session.id,
                                        defaultMinutes = row.session.durationMinutes, dateIso = row.session.dateIso, isClass = row.session.isClass,
                                    )
                                })
                                Chip(text = "Skip", onClick = { viewModel.skip(row.session.id) })
                            }
                        } else if (skipped) {
                            CupertinoTextButton(text = "Reopen", onClick = { viewModel.reopen(row.session.id) })
                        }
                    }
                }
            }

            // Habits
            if (state.habits.isNotEmpty()) {
                item { SectionLabel("Counts") }
                item {
                    CupertinoListSection {
                        state.habits.forEachIndexed { i, h ->
                            val target = h.habit.weeklyTarget?.let { "${h.thisWeek} / $it this week" }
                                ?: h.habit.totalTarget?.let { "${h.total} / $it total" } ?: "${h.total} total"
                            CupertinoListRow(
                                title = h.habit.name,
                                subtitle = target,
                                showDivider = i < state.habits.lastIndex,
                                trailing = { Chip(text = "+1", onClick = { viewModel.addHabit(h.habit.id, 1) }) },
                            )
                        }
                    }
                }
            }

            // Partner
            state.partner?.let { p ->
                item { SectionLabel(p.profile?.name?.ifBlank { null } ?: "Partner") }
                item {
                    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                            Avatar(name = p.profile?.name ?: "?", photoUrl = p.profile?.photoUrl, size = 44.dp)
                            Column(modifier = Modifier.weight(1f)) {
                                if (p.profile == null) {
                                    Text("Not joined yet", style = MaterialTheme.typography.titleMedium)
                                    Text("Share the invite code from Settings", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                                } else {
                                    Text("${p.todayMinutes} min today, ${p.streak}-day streak", style = MaterialTheme.typography.titleMedium)
                                    Text("Local time ${p.localTime}", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                                }
                            }
                        }
                        p.sessions.forEach { row ->
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = Spacing.sm)) {
                                Icon(
                                    imageVector = if (row.session.status == SessionStatus.DONE) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                                    contentDescription = null, tint = if (row.session.status == SessionStatus.DONE) AppTheme.colors.success else AppTheme.colors.secondaryLabel,
                                    modifier = Modifier.padding(end = Spacing.sm),
                                )
                                Text("${row.goal?.title ?: ""}: ${row.componentName}, ${row.timeLabel}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }

            // Next deadline
            state.nextDeadline?.let { d ->
                item { SectionLabel("Next deadline") }
                item {
                    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(d.title, style = MaterialTheme.typography.titleMedium)
                                if (d.detail.isNotBlank()) Text(d.detail, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                            }
                            Text("${state.daysToDeadline ?: 0} days", style = MaterialTheme.typography.headlineMedium, color = AppTheme.colors.accent)
                        }
                    }
                }
            }
        }
        SnackbarHost(hostState = snackbar, modifier = Modifier.align(Alignment.BottomCenter))
    }

    logTarget?.let { target ->
        LogMinutesSheet(
            target = target,
            goals = state.goals,
            onDismiss = { logTarget = null },
            onLog = { g, c, m, n -> viewModel.logMinutes(g, c, m, n, target.sessionId, target.isClass); logTarget = null },
            onLogSplit = { g, map, n -> viewModel.logSplit(g, map, n, target.sessionId); logTarget = null },
        )
    }
}

@Composable
private fun HeroStat(value: String, label: String) {
    Column {
        Text(value, style = MaterialTheme.typography.headlineMedium, color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall, color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.85f))
    }
}
