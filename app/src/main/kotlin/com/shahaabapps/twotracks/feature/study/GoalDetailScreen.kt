package com.shahaabapps.twotracks.feature.study

import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontWeight
import com.shahaabapps.core.ui.theme.Shapes
import com.shahaabapps.twotracks.ui.components.DialogButton
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoSecondaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Clock
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.NavAction
import com.shahaabapps.twotracks.ui.components.ProgressBar
import com.shahaabapps.twotracks.ui.components.SwipeToDelete
import com.shahaabapps.twotracks.ui.components.SegmentedChoice
import com.shahaabapps.twotracks.ui.components.SectionLabel

/** Level 2: one goal. Sentence, bar, per-skill bars, Log and Mark as passed. Everything else is behind Details. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoalDetailScreen(
    goalId: String,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onOpenCourse: (String) -> Unit = {},
    onOpenQuiz: (String, String) -> Unit = { _, _ -> },
    viewModel: GoalDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    var showMock by remember { mutableStateOf(false) }
    var showDetails by remember { mutableStateOf(false) }
    var confirmPass by remember { mutableStateOf(false) }
    LaunchedEffect(state.message) { state.message?.let { snackbar.showSnackbar(it); viewModel.clearMessage() } }
    val undoLog by viewModel.undoLog.collectAsStateWithLifecycle()
    val undoMock by viewModel.undoMock.collectAsStateWithLifecycle()
    LaunchedEffect(undoLog) {
        if (undoLog != null) {
            val result = snackbar.showSnackbar("Entry deleted", actionLabel = "Undo", duration = SnackbarDuration.Short)
            if (result == SnackbarResult.ActionPerformed) viewModel.undoDeleteLog() else viewModel.clearUndo()
        }
    }
    LaunchedEffect(undoMock) {
        if (undoMock != null) {
            val result = snackbar.showSnackbar("Mock score deleted", actionLabel = "Undo", duration = SnackbarDuration.Short)
            if (result == SnackbarResult.ActionPerformed) viewModel.undoDeleteMock() else viewModel.clearUndo()
        }
    }

    val goal = state.goal
    val p = state.progress
    DetailScaffold(
        title = goal?.title ?: "Goal",
        onBack = onBack,
        actions = { if (state.isMine) NavAction("Edit", onEdit) },
    ) {
        if (goal == null || p == null) return@DetailScaffold
        Box {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
                item {
                    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        Text(state.sentence, style = MaterialTheme.typography.titleMedium, color = if (state.sentenceGood) AppTheme.colors.success else AppTheme.colors.warn)
                        // Hours are shown only where a course records them. For
                        // a goal no course feeds nothing can move the bar, and a
                        // bar stuck on nought reads as failure rather than as a
                        // goal that is simply measured another way.
                        if (state.hoursCounted) {
                            ProgressBar(fraction = p.fraction.toFloat(), modifier = Modifier.padding(top = Spacing.sm, bottom = Spacing.xs))
                            Text("%.0f of %.0f hours".format(p.hoursLogged, p.hoursRequired) + (p.targetDate?.let { " · by ${Clock.formatDate(it)}" } ?: ""), style = MaterialTheme.typography.bodyMedium)
                        } else {
                            p.targetDate?.let { due ->
                                Text("By ${Clock.formatDate(due)}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = Spacing.sm))
                            }
                        }
                        if (goal.subtitle.isNotBlank()) Text(goal.subtitle, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = Spacing.xs))
                        if (state.isMine) {
                            if (goal.passed) {
                                CupertinoTextButton(text = "Reopen (not passed after all)", onClick = { viewModel.setPassed(false) })
                            } else {
                                CupertinoSecondaryButton(text = "Mark as passed", onClick = { confirmPass = true }, modifier = Modifier.padding(top = Spacing.sm))
                            }
                        }
                    }
                }

                if (state.modules.isNotEmpty() && state.courseId != null) {
                    item { SectionLabel("Course") }
                    item {
                        CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), onClick = { onOpenCourse(state.courseId!!) }) {
                            val finished = state.modules.count { it.finished }
                            val available = state.modules.count { it.module.available }
                            Text("$finished of $available modules finished", style = MaterialTheme.typography.titleMedium)
                            Text("A module is finished when every topic is read and its test is passed. That completes the matching skill below, whatever the hours say.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = Spacing.sm)) {
                                state.modules.forEach { m ->
                                    Column(
                                        modifier = Modifier.weight(1f).clip(Shapes.field)
                                            .background(if (m.finished) AppTheme.colors.success else if (!m.module.available) AppTheme.colors.fill.copy(alpha = 0.5f) else AppTheme.colors.fill)
                                            .clickable(enabled = m.module.available) { if (m.passed) onOpenCourse(state.courseId!!) else onOpenQuiz(state.courseId!!, m.module.id) }
                                            .padding(vertical = 8.dp, horizontal = 4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        Text("${m.module.number}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = if (m.finished) AppTheme.colors.onSolid else MaterialTheme.colorScheme.onSurface)
                                        Text(
                                            when {
                                                !m.module.available -> "soon"
                                                m.finished -> "done"
                                                m.passed -> "${m.learned}/${m.total}"
                                                else -> "${m.learned}/${m.total}" + (m.bestPercent?.let { " · $it%" } ?: "")
                                            },
                                            style = MaterialTheme.typography.labelSmall, color = if (m.finished) AppTheme.colors.onSolid.copy(alpha = 0.9f) else AppTheme.colors.secondaryLabel,
                                        )
                                    }
                                }
                            }
                            CupertinoTextButton(text = "Open the course", onClick = { onOpenCourse(state.courseId!!) })
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
                                        // Same rule as the card above: an hours
                                        // line only where a course keeps them.
                                        // The skills are still worth listing —
                                        // they say what the goal is made of.
                                        val line = if (state.hoursCounted) {
                                            when {
                                                c.finishedByCourse -> "Finished in the course · %.0f h logged".format(c.hoursLogged)
                                                c.courseFraction != null -> "%.0f of %.0f h · course %.0f%%".format(c.hoursLogged, c.hoursRequired, c.courseFraction!! * 100)
                                                else -> "%.0f of %.0f h".format(c.hoursLogged, c.hoursRequired)
                                            } + (c.component.targetScore?.let { " · target $it" } ?: "")
                                        } else {
                                            c.component.targetScore?.let { "Target $it" }.orEmpty()
                                        }
                                        if (line.isNotBlank()) {
                                            Text(
                                                line,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (c.finishedByCourse) AppTheme.colors.success else AppTheme.colors.secondaryLabel,
                                            )
                                        }
                                    }
                                }
                                if (state.hoursCounted) {
                                    ProgressBar(fraction = c.fraction.toFloat(), modifier = Modifier.padding(top = Spacing.xs), height = 6.dp)
                                }
                            }
                        }
                    }
                }

                item {
                    CupertinoTextButton(text = if (showDetails) "Hide details" else "Details: history, mock scores, why it matters", onClick = { showDetails = !showDetails })
                }

                if (showDetails) {
                    if (goal.why.isNotBlank()) {
                        item { Text(goal.why, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(horizontal = Spacing.listHorizontal + Spacing.sectionStart)) }
                    }
                    item { SectionLabel("Pace") }
                    item {
                        CupertinoListSection {
                            CupertinoListRow("Needed per week", trailing = { Text(p.weeklyNeedHours?.let { "%.1f h".format(it) } ?: "no date") })
                            CupertinoListRow("Recent pace (14 days)", trailing = { Text("%.1f h / week".format(p.recentWeeklyHours)) })
                            CupertinoListRow("Forecast finish", showDivider = false, trailing = { Text(p.forecastDate?.let { Clock.formatDate(it) } ?: "-") })
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
                                            .background(if (bar.minutes > 0) AppTheme.colors.accent else AppTheme.colors.fill),
                                    )
                                }
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
                        item { Text("No practice-test results yet.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(horizontal = Spacing.listHorizontal + Spacing.sectionStart)) }
                    } else {
                        itemsIndexed(state.mockScores, key = { _, m -> "mock-" + m.id }) { i, m ->
                            SwipeRow(
                                index = i, last = state.mockScores.lastIndex, enabled = state.isMine,
                                onDelete = { viewModel.deleteMockScore(m) },
                                modifier = Modifier.animateItem(),
                            ) {
                                CupertinoListRow(
                                    title = m.scores.entries.joinToString(" · ") { (k, v) ->
                                        val name = goal.components.firstOrNull { it.id == k }?.name?.substringBefore(" (") ?: k
                                        if (m.scores.size == 1) "$name $v" else "${name.take(1)} $v"
                                    },
                                    subtitle = Clock.formatDate(m.dateIso) + if (m.note.isNotBlank()) ", ${m.note}" else "",
                                    showDivider = i < state.mockScores.lastIndex,
                                )
                            }
                        }
                        if (state.isMine) item { Text("Swipe left on an entry to delete it.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(horizontal = Spacing.listHorizontal + Spacing.sectionStart)) }
                    }
                    if (state.recentLogs.isNotEmpty()) {
                        item { SectionLabel("Recent entries") }
                        itemsIndexed(state.recentLogs, key = { _, log -> "log-" + log.id }) { i, log ->
                            val name = goal.components.firstOrNull { it.id == log.componentId }?.name ?: log.componentId
                            SwipeRow(
                                index = i, last = state.recentLogs.lastIndex, enabled = state.isMine,
                                onDelete = { viewModel.deleteLog(log) },
                                modifier = Modifier.animateItem(),
                            ) {
                                CupertinoListRow(
                                    title = "$name, ${log.minutes} min" + if (log.fromClass) " (class)" else "",
                                    subtitle = Clock.formatDate(log.dateIso) + if (log.note.isNotBlank()) ", ${log.note}" else "",
                                    showDivider = i < state.recentLogs.lastIndex,
                                )
                            }
                        }
                    }
                }
            }
            SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }

    if (confirmPass && goal != null) {
        CupertinoDialog(
            onDismissRequest = { confirmPass = false },
            confirmButton = { DialogButton(onClick = { viewModel.setPassed(true); confirmPass = false }) { Text("Yes, passed") } },
            dismissButton = { DialogButton(onClick = { confirmPass = false }) { Text("Not yet") } },
            title = { Text("Mark ${goal.title} as passed?") },
            text = { Text("Use this when the exam is passed or the level is reached. Your hours stay in the history and the planner stops scheduling it.") },
        )
    }

    if (showMock && goal != null) {
        val values = remember { mutableStateOf(goal.components.associate { it.id to "" }) }
        var note by remember { mutableStateOf("") }
        var fullTest by remember { mutableStateOf(goal.components.size == 1) }
        var skillId by remember { mutableStateOf(goal.components.first().id) }
        var single by remember { mutableStateOf("") }
        val canSave = if (fullTest) values.value.values.any { it.isNotBlank() } else single.isNotBlank()
        CupertinoDialog(
            onDismissRequest = { showMock = false },
            confirmButton = {
                DialogButton(enabled = canSave, onClick = {
                    val scores = if (fullTest) values.value.filterValues { it.isNotBlank() } else mapOf(skillId to single.trim())
                    viewModel.addMockScore(scores, note); showMock = false
                }) { Text("Save") }
            },
            dismissButton = { DialogButton(onClick = { showMock = false }) { Text("Cancel") } },
            title = { Text("Practice result") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    if (goal.components.size > 1) {
                        SegmentedChoice(options = listOf(false, true), selected = fullTest, label = { if (it) "Full test" else "One skill" }, onSelect = { fullTest = it })
                    }
                    if (!fullTest) {
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                            goal.components.forEach { c -> Chip(text = c.name.substringBefore(" ("), selected = c.id == skillId, onClick = { skillId = c.id }) }
                        }
                        val target = goal.components.firstOrNull { it.id == skillId }?.targetScore
                        CupertinoTextField(value = single, onValueChange = { single = it }, placeholder = target?.let { "Score (target $it)" } ?: "Score")
                    } else {
                        goal.components.forEach { c ->
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                                Text(c.name.substringBefore(" ("), modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                                Box(modifier = Modifier.width(96.dp)) {
                                    CupertinoTextField(value = values.value[c.id].orEmpty(), onValueChange = { v -> values.value = values.value + (c.id to v) }, placeholder = c.targetScore ?: "score")
                                }
                            }
                        }
                        Text("Leave a skill blank if you did not sit it.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                    }
                    CupertinoTextField(value = note, onValueChange = { note = it }, placeholder = "Note (e.g. Cambridge 18 test 2)")
                }
            },
        )
    }
}

/**
 * One row of a grouped list rendered as its own LazyColumn item so it can be
 * swiped away and animated. Rounds the first and last rows to keep the card look.
 */
@Composable
private fun SwipeRow(
    index: Int,
    last: Int,
    enabled: Boolean,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val r = 12.dp
    val shape = RoundedCornerShape(
        topStart = if (index == 0) r else 0.dp, topEnd = if (index == 0) r else 0.dp,
        bottomStart = if (index == last) r else 0.dp, bottomEnd = if (index == last) r else 0.dp,
    )
    Box(modifier = modifier.padding(horizontal = Spacing.listHorizontal)) {
        if (enabled) {
            SwipeToDelete(onDelete = onDelete, shape = shape) {
                Box(modifier = Modifier.background(AppTheme.colors.card)) { content() }
            }
        } else {
            Box(modifier = Modifier.clip(shape).background(AppTheme.colors.card)) { content() }
        }
    }
}
