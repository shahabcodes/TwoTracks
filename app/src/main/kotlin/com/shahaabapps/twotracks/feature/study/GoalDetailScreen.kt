package com.shahaabapps.twotracks.feature.study

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.model.CertTier
import com.shahaabapps.core.model.GoalKind
import com.shahaabapps.core.model.MockScore
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.model.StudyLog
import com.shahaabapps.core.planner.GoalProgress
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.DialogButton
import com.shahaabapps.twotracks.ui.components.NavAction
import com.shahaabapps.twotracks.ui.components.SegmentedChoice
import com.shahaabapps.twotracks.ui.components.SwipeToDelete
import java.time.format.DateTimeFormatter
import java.util.Locale

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
    GoalDetailContent(
        state, snackbar, onBack = onBack, onEdit = onEdit, onOpenCourse = onOpenCourse,
        onSetPassed = viewModel::setPassed, onAddMock = viewModel::addMockScore, onDeleteMock = viewModel::deleteMockScore, onDeleteLog = viewModel::deleteLog,
    )
}

/**
 * A goal, as the prototype draws it: what kind of goal and what it costs,
 * the title and why it matters, three numbers under an ink rule, whether
 * it is on track, where the hours go part by part, the practice scores,
 * and the one thing to do next held at the foot.
 */
@Composable
fun GoalDetailContent(
    state: GoalDetailUiState,
    snackbar: SnackbarHostState,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onOpenCourse: (String) -> Unit,
    onSetPassed: (Boolean) -> Unit,
    onAddMock: (Map<String, String>, String) -> Unit,
    onDeleteMock: (MockScore) -> Unit,
    onDeleteLog: (StudyLog) -> Unit,
) {
    var showMock by remember { mutableStateOf(false) }
    var confirmPass by remember { mutableStateOf(false) }
    val goal = state.goal
    val p = state.progress
    val color = AppTheme.colors.track(goal?.owner != PersonRole.TEACHER)
    if (AppTheme.departures) {
        BoardGoalBody(
            state, snackbar, onBack = onBack, onEdit = onEdit, onOpenCourse = onOpenCourse,
            onAskPass = { confirmPass = true }, onReopen = { onSetPassed(false) }, onAddMock = { showMock = true },
            onDeleteMock = onDeleteMock, onDeleteLog = onDeleteLog,
        )
    } else DetailScaffold(
        title = goal?.title ?: "Goal", onBack = onBack, backLabel = "Back",
        eyebrow = goal?.let { goalEyebrow(it) },
        actions = { if (state.isMine) NavAction("Edit", onEdit) },
    ) {
        if (goal == null || p == null) return@DetailScaffold
        Box {
            LazyColumn(contentPadding = PaddingValues(bottom = 120.dp)) {
                val why = goal.why.ifBlank { goal.subtitle }
                if (why.isNotBlank()) {
                    item { Text(why, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) }
                }
                item { Stats(goal, p, state.hoursCounted) }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 10.dp),
                    ) {
                        val ink = if (state.sentenceGood) AppTheme.colors.success else AppTheme.colors.warn
                        Icon(if (state.sentenceGood) Icons.Filled.Check else Icons.Filled.Info, contentDescription = null, tint = ink, modifier = Modifier.size(14.dp))
                        Text(state.sentence, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = ink)
                    }
                }

                item { Heading(if (state.hoursCounted) "Where the hours go" else "What it is made of", top = 22.dp) }
                itemsIndexed(p.components, key = { _, c -> "part-" + c.component.id }) { _, c ->
                    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        Column(modifier = Modifier.padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row {
                                Text(c.component.name, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                                val right = when {
                                    !state.hoursCounted -> c.component.targetScore?.let { "target $it" }.orEmpty()
                                    c.finishedByCourse -> "done"
                                    else -> "%.0f / %.0f h".format(Locale.ENGLISH, c.hoursLogged, c.hoursRequired)
                                }
                                Text(right, style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 12.sp), color = if (c.finishedByCourse) AppTheme.colors.success else AppTheme.colors.secondaryLabel)
                            }
                            if (state.hoursCounted) {
                                Box(Modifier.fillMaxWidth().height(4.dp).background(AppTheme.colors.separator)) {
                                    Box(Modifier.fillMaxWidth(c.fraction.toFloat().coerceIn(0.01f, 1f)).height(4.dp).background(if (c.finishedByCourse) AppTheme.colors.success else color))
                                }
                            }
                        }
                        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                    }
                }

                item {
                    Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 22.dp)) {
                        Text("Practice scores", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                        if (state.isMine) Text(
                            "Add one", style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp), color = AppTheme.colors.accentText,
                            modifier = Modifier.clip(RoundedCornerShape(6.dp)).clickable(role = Role.Button) { showMock = true }.padding(vertical = 6.dp),
                        )
                    }
                }
                if (state.mockScores.isEmpty()) {
                    item {
                        Text(
                            if (state.courseId != null) "None yet. The mock exam in the course is the closest thing to the real one." else "None yet. Add a result after each practice test to see the trend.",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), color = AppTheme.colors.secondaryLabel,
                            modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 8.dp),
                        )
                    }
                } else {
                    itemsIndexed(state.mockScores, key = { _, m -> "mock-" + m.id }) { _, m ->
                        Swipeable(enabled = state.isMine, onDelete = { onDeleteMock(m) }) {
                            Line(
                                m.scores.entries.joinToString(" · ") { (k, v) ->
                                    val name = goal.components.firstOrNull { it.id == k }?.name?.substringBefore(" (") ?: k
                                    if (m.scores.size == 1) "$name $v" else "${name.take(1)} $v"
                                },
                                Clock.formatDate(m.dateIso) + if (m.note.isNotBlank()) ", ${m.note}" else "",
                            )
                        }
                    }
                }

                // Not in the prototype: the entries behind the hours, kept so a
                // wrong one can still be swiped away.
                if (state.recentLogs.isNotEmpty()) {
                    item { Heading("Recent entries", top = 22.dp) }
                    itemsIndexed(state.recentLogs.take(10), key = { _, log -> "log-" + log.id }) { _, log ->
                        val name = goal.components.firstOrNull { it.id == log.componentId }?.name ?: log.componentId
                        Swipeable(enabled = state.isMine, onDelete = { onDeleteLog(log) }) {
                            Line("$name, ${log.minutes} min" + if (log.fromClass) " (class)" else "", Clock.formatDate(log.dateIso) + if (log.note.isNotBlank()) ", ${log.note}" else "")
                        }
                    }
                }
                if (state.isMine && state.courseId != null) {
                    item {
                        Text(
                            if (goal.passed) "Reopen, not passed after all" else "Mark as passed",
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp), color = AppTheme.colors.accentText,
                            modifier = Modifier.padding(start = Spacing.listHorizontal, top = 18.dp).clip(RoundedCornerShape(6.dp))
                                .clickable(role = Role.Button) { if (goal.passed) onSetPassed(false) else confirmPass = true }.padding(vertical = 10.dp),
                        )
                    }
                }
            }
            FootButton(state, goal, onOpenCourse = onOpenCourse, onPass = { confirmPass = true }, onReopen = { onSetPassed(false) }, modifier = Modifier.align(Alignment.BottomCenter))
            SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 100.dp))
        }
    }

    if (confirmPass && goal != null) {
        CupertinoDialog(
            onDismissRequest = { confirmPass = false },
            confirmButton = { DialogButton(onClick = { onSetPassed(true); confirmPass = false }) { Text("Yes, passed") } },
            dismissButton = { DialogButton(onClick = { confirmPass = false }) { Text("Not yet") } },
            title = { Text("Mark ${goal.title} as passed?") },
            text = { Text("Use this when the exam is passed or the level is reached. Your hours stay in the history and the planner stops scheduling it.") },
        )
    }
    if (showMock && goal != null) MockDialog(goal, onSave = { s, n -> onAddMock(s, n); showMock = false }, onDismiss = { showMock = false })
}

private val shortDate = DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH)

/** "CERTIFICATION · TIER 1 · USD 165". */
internal fun goalEyebrow(goal: StudyGoal): String = buildList {
    add(when (goal.kind) { GoalKind.LANGUAGE_TEST -> "Language test"; GoalKind.LANGUAGE_LEVEL -> "Language level"; GoalKind.CERTIFICATION -> "Certification"; GoalKind.CUSTOM -> "Goal" })
    goal.tier?.let { add(when (it) { CertTier.TIER_1 -> "Tier 1"; CertTier.TIER_2 -> "Tier 2"; CertTier.TIER_3 -> "Tier 3" }) }
    goal.feeUsd?.let { add("USD $it") }
    if (goal.passed) add("Passed")
}.joinToString(" · ")

@Composable
private fun Stats(goal: StudyGoal, p: GoalProgress, hoursCounted: Boolean) {
    Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 18.dp)) {
        HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            if (hoursCounted) {
                Stat("%.0f h".format(Locale.ENGLISH, p.hoursLogged), "of %.0f logged".format(Locale.ENGLISH, p.hoursRequired), first = true)
                Stat(p.weeklyNeedHours?.let { "%.1f h".format(Locale.ENGLISH, it).replace(".0 h", " h") } ?: "–", "a week to finish")
            } else {
                Stat("${goal.components.size}", if (goal.components.size == 1) "part" else "parts", first = true)
                Stat(p.targetDate?.let { java.time.temporal.ChronoUnit.DAYS.between(p.today, it).coerceAtLeast(0).toString() } ?: "–", "days left")
            }
            Stat(p.targetDate?.format(shortDate) ?: "–", p.targetDate?.let { "${it.year} target" } ?: "no date set")
        }
        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.Stat(value: String, label: String, first: Boolean = false) {
    Row(modifier = Modifier.weight(1f).fillMaxHeight()) {
        if (!first) Box(Modifier.width(Spacing.hairline).fillMaxHeight().background(AppTheme.colors.separator))
        Column(modifier = Modifier.padding(start = if (first) 0.dp else 12.dp, top = 14.dp, bottom = 14.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(value, style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp), fontWeight = FontWeight.SemiBold, maxLines = 1)
            Text(label, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
        }
    }
}

@Composable
private fun Heading(text: String, top: androidx.compose.ui.unit.Dp) {
    Text(text, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = top, bottom = 6.dp))
}

@Composable
private fun Line(title: String, subtitle: String) {
    Column(modifier = Modifier.fillMaxWidth().background(AppTheme.colors.groupedBackground).padding(horizontal = Spacing.listHorizontal)) {
        Column(modifier = Modifier.padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
        }
        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
    }
}

@Composable
private fun Swipeable(enabled: Boolean, onDelete: () -> Unit, content: @Composable () -> Unit) {
    if (enabled) SwipeToDelete(onDelete = onDelete) { content() } else content()
}

/**
 * The prototype's button says "Log study time". Minutes are no longer typed
 * in, so the foot carries what moves a goal on instead: the course that
 * feeds it, or marking it passed.
 */
@Composable
private fun FootButton(state: GoalDetailUiState, goal: StudyGoal, onOpenCourse: (String) -> Unit, onPass: () -> Unit, onReopen: () -> Unit, modifier: Modifier) {
    val courseId = state.courseId
    val (label, action) = when {
        courseId != null -> "Open the course" to { onOpenCourse(courseId) }
        !state.isMine -> return
        goal.passed -> "Reopen, not passed after all" to onReopen
        else -> "Mark as passed" to onPass
    }
    Column(modifier = modifier.fillMaxWidth().background(AppTheme.colors.groupedBackground).navigationBarsPadding()) {
        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
        val shape = RoundedCornerShape(12.dp)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 12.dp, bottom = 24.dp).fillMaxWidth().height(52.dp)
                .clip(shape).background(AppTheme.colors.accent).clickable(role = Role.Button, onClick = action),
        ) { Text(label, style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp), color = AppTheme.colors.onSolid) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MockDialog(goal: StudyGoal, onSave: (Map<String, String>, String) -> Unit, onDismiss: () -> Unit) {
    val values = remember { mutableStateOf(goal.components.associate { it.id to "" }) }
    var note by remember { mutableStateOf("") }
    var fullTest by remember { mutableStateOf(goal.components.size == 1) }
    var skillId by remember { mutableStateOf(goal.components.firstOrNull()?.id.orEmpty()) }
    var single by remember { mutableStateOf("") }
    val canSave = if (fullTest) values.value.values.any { it.isNotBlank() } else single.isNotBlank()
    CupertinoDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            DialogButton(enabled = canSave, onClick = {
                onSave(if (fullTest) values.value.filterValues { it.isNotBlank() } else mapOf(skillId to single.trim()), note)
            }) { Text("Save") }
        },
        dismissButton = { DialogButton(onClick = onDismiss) { Text("Cancel") } },
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
