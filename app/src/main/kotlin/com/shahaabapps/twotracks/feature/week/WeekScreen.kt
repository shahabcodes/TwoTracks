package com.shahaabapps.twotracks.feature.week

import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.model.AvailabilityBlock
import java.time.LocalDate
import com.shahaabapps.twotracks.ui.components.NavAction
import com.shahaabapps.core.model.PersonRole
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.semantics.Role
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.Check
import androidx.compose.foundation.shape.CircleShape
import com.shahaabapps.core.ui.theme.MonoFamily
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import com.shahaabapps.twotracks.ui.components.DialogButton
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.components.Chip

/**
 * Your week, as the Parallel prototype draws it: the dates, two figures for
 * the hours planned and the hours the goals need, whether that fits, then the
 * seven days, each a column of times with a rail in the course's colour.
 *
 * What the prototype has no room for is folded in rather than drawn as more
 * sections: the arrows beside the dates move between weeks, the verdict opens
 * the ways to make the week fit, a day's name adds a block to it, and a study
 * block assigns a goal to it.
 */
@Composable
fun WeekScreen(
    onBack: () -> Unit,
    onOpenGoal: (String) -> Unit,
    onOpenBothWeeks: () -> Unit,
    onOpenFit: () -> Unit,
    viewModel: WeekViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    WeekContent(
        state = state, onBack = onBack, onOpenGoal = onOpenGoal, onOpenBothWeeks = onOpenBothWeeks, onOpenFit = onOpenFit,
        onPreviousWeek = viewModel::previousWeek, onNextWeek = viewModel::nextWeek, onThisWeek = viewModel::thisWeek,
        onTogglePerson = viewModel::togglePerson, onReplan = { viewModel.replan() },
        onAssign = { block, date, goal -> viewModel.assign(block, date, goal) },
        onAddBlock = { day, start, end -> viewModel.addBlock(day, start, end, BlockKind.STUDY) },
        onMessageShown = viewModel::clearMessage,
    )
}

/** Your week drawn from its state alone, so a preview can show it without an account. */
@Composable
fun WeekContent(
    state: WeekUiState,
    onBack: () -> Unit,
    onOpenGoal: (String) -> Unit,
    onOpenBothWeeks: () -> Unit,
    onOpenFit: () -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onThisWeek: () -> Unit,
    onTogglePerson: () -> Unit,
    onReplan: () -> Unit,
    onAssign: (AvailabilityBlock, LocalDate, StudyGoal) -> Unit,
    onAddBlock: (Int, Int, Int) -> Unit,
    onMessageShown: () -> Unit,
) {
    val snackbar = remember { SnackbarHostState() }
    var assignRow by remember { mutableStateOf<Pair<DayPlan, BlockRow>?>(null) }
    var addBlockFor by remember { mutableStateOf<DayPlan?>(null) }

    LaunchedEffect(state.message) { state.message?.let { snackbar.showSnackbar(it); onMessageShown() } }

    if (AppTheme.departures) BoardTimetable(
        state, onBack, onOpenGoal, onOpenBothWeeks, onOpenFit, onPreviousWeek, onNextWeek, onThisWeek, onTogglePerson, onReplan,
        onAssign = { day, row -> assignRow = day to row }, onAddBlock = { addBlockFor = it },
    ) else DetailScaffold(
        title = if (state.isMe) "Your week" else "${state.viewingName.ifBlank { "Their" }}'s week",
        onBack = onBack,
        eyebrow = weekRange(state.weekStart),
        actions = {
            WeekArrow(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Previous week", onPreviousWeek)
            WeekArrow(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Next week", onNextWeek)
            NavAction("Both weeks", onClick = onOpenBothWeeks)
        },
    ) {
        Box {
            LazyColumn(contentPadding = PaddingValues(bottom = Spacing.xl)) {
                item {
                    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        state.capacity?.let { cap ->
                            Row(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.padding(top = 2.dp)) {
                                Figure(hours(state.plannedMinutes / 60.0), "planned")
                                Figure(hours(cap.weeklyNeedHours), "needed to stay on track")
                            }
                            val spare = state.plannedMinutes / 60.0 - cap.weeklyNeedHours
                            val fits = cap.fits && spare >= 0
                            Row(
                                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.clickable(onClickLabel = "Ways to make it fit", onClick = onOpenFit).heightIn(min = 32.dp),
                            ) {
                                val tone = if (fits) AppTheme.colors.success else AppTheme.colors.warn
                                Icon(if (fits) Icons.Filled.Check else Icons.Filled.WarningAmber, contentDescription = null, tint = tone, modifier = Modifier.size(16.dp))
                                Text(
                                    if (fits) "It fits, with " + spareWords(spare) + " to spare"
                                    else "Short by %.1f hours a week. See what would fit".format(maxOf(cap.gapHours, -spare)),
                                    style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = tone,
                                )
                            }
                        }
                    }
                }
                item {
                    HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule, modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 12.dp))
                }
                items(state.days, key = { it.date.toString() }) { day ->
                    DayRows(
                        day = day,
                        editable = state.isMe,
                        trackColor = AppTheme.colors.track(state.viewingRole == PersonRole.TECH),
                        onOpenGoal = onOpenGoal,
                        onAssign = { row -> assignRow = day to row },
                        onAddBlock = { addBlockFor = day },
                    )
                }
                // What the prototype has no room for at the top: whose week, re-planning, and back to this week.
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 12.dp)) {
                        TextLink(if (state.isMe) "See ${state.partnerLabel()}'s week" else "Back to yours", onTogglePerson)
                        if (state.isMe) TextLink("Re-plan the week", onReplan)
                        TextLink("This week", onThisWeek)
                    }
                }
            }
            SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }

    assignRow?.let { (day, row) ->
        CupertinoDialog(
            onDismissRequest = { assignRow = null },
            confirmButton = { DialogButton(onClick = { assignRow = null }) { Text("Cancel") } },
            title = { Text("${Clock.formatDayMonth(day.date)} ${Dates.minutesToClock(row.block.startMinutes)}") },
            text = {
                Column {
                    Text("Study which goal in this slot?", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                    state.myGoals.forEach { g ->
                        Text(
                            g.title, style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth().clickable { onAssign(row.block, day.date, g); assignRow = null }.padding(vertical = 10.dp),
                        )
                    }
                }
            },
        )
    }

    addBlockFor?.let { day ->
        AddBlockDialog(
            dayLabel = Clock.formatDayMonth(day.date),
            onDismiss = { addBlockFor = null },
            onAdd = { start, end -> onAddBlock(day.date.dayOfWeek.value, start, end); addBlockFor = null },
        )
    }
}

private fun WeekUiState.partnerLabel(): String = "your partner"

/** "8 h" for whole hours, "9.4 h" otherwise. */
internal fun hours(h: Double): String =
    if (h == Math.floor(h)) "${h.toLong()} h" else "%.1f h".format(h)

/** "12 – 18 Oct", or "28 Sep – 4 Oct" across a month. */
internal fun weekRange(start: LocalDate): String {
    val end = start.plusDays(6)
    val month = java.time.format.DateTimeFormatter.ofPattern("MMM", java.util.Locale.ENGLISH)
    return if (start.month == end.month) "${start.dayOfMonth} – ${end.dayOfMonth} ${end.format(month)}"
    else "${start.dayOfMonth} ${start.format(month)} – ${end.dayOfMonth} ${end.format(month)}"
}

/** "an hour and a half", "20 minutes", "2 hours". */
internal fun spareWords(hours: Double): String {
    val minutes = (hours * 60).toInt().coerceAtLeast(0)
    return when {
        minutes < 60 -> "$minutes minutes"
        minutes in 60..74 -> "an hour"
        minutes in 75..104 -> "an hour and a half"
        else -> "%.1f hours".format(minutes / 60.0).replace(".0 ", " ")
    }
}

@Composable
private fun Figure(value: String, label: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(value, style = MaterialTheme.typography.headlineMedium.copy(fontSize = 26.sp))
        Text(label, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
    }
}

@Composable
private fun WeekArrow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(36.dp).clip(CircleShape).clickable(role = Role.Button, onClickLabel = label, onClick = onClick),
    ) { Icon(icon, contentDescription = label, tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(20.dp)) }
}

@Composable
private fun TextLink(text: String, onClick: () -> Unit) {
    Text(
        text, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = AppTheme.colors.accentText,
        modifier = Modifier.clip(CircleShape).clickable(role = Role.Button, onClick = onClick).padding(vertical = 8.dp),
    )
}

/**
 * One day: its name and date in a column on the left, then its blocks, each a
 * time, a rail and what it holds. Today is in the reader's colour. Tapping the
 * day's name adds a block; tapping a study block chooses its goal.
 */
@Composable
private fun DayRows(day: DayPlan, editable: Boolean, trackColor: Color, onOpenGoal: (String) -> Unit, onAssign: (BlockRow) -> Unit, onAddBlock: () -> Unit) {
    val shown = day.blocks.filter { it.block.kind != BlockKind.WORK }
    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
            Column(
                modifier = Modifier.width(52.dp).then(if (editable) Modifier.clickable(onClickLabel = "Add a block") { onAddBlock() } else Modifier),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    day.date.format(java.time.format.DateTimeFormatter.ofPattern("EEE", java.util.Locale.ENGLISH)),
                    style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold,
                    color = if (day.isToday) trackColor else AppTheme.colors.ink,
                )
                Text("${day.date.dayOfMonth}", style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 11.sp), color = AppTheme.colors.secondaryLabel)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                if (shown.isEmpty()) {
                    Text("Rest day. Logging anything still counts.", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                }
                shown.forEach { row ->
                    val status = row.session?.status
                    val rail = when {
                        row.block.kind == BlockKind.CLASS -> AppTheme.colors.accentText
                        status == SessionStatus.DONE -> AppTheme.colors.success
                        status == SessionStatus.SKIPPED -> AppTheme.colors.warn
                        else -> trackColor
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 28.dp)
                            .then(
                                if (row.block.kind == BlockKind.STUDY && editable) Modifier.clickable { onAssign(row) }
                                else if (row.goal != null) Modifier.clickable { onOpenGoal(row.goal.id) } else Modifier,
                            ),
                    ) {
                        Text(
                            Dates.minutesToClock(row.block.startMinutes),
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 12.sp), color = AppTheme.colors.secondaryLabel,
                            modifier = Modifier.width(40.dp),
                        )
                        Box(Modifier.width(3.dp).height(18.dp).background(rail))
                        Text(
                            row.goal?.let { "${it.title} · ${row.componentName}" } ?: if (editable) "Unplanned · tap to assign" else "Unplanned",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (row.goal == null) AppTheme.colors.secondaryLabel else AppTheme.colors.ink,
                            textDecoration = if (status == SessionStatus.DONE) TextDecoration.LineThrough else TextDecoration.None,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            "${(row.block.endMinutes - row.block.startMinutes)}m",
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 11.sp), color = AppTheme.colors.secondaryLabel,
                        )
                    }
                }
            }
        }
        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddBlockDialog(dayLabel: String, onDismiss: () -> Unit, onAdd: (Int, Int) -> Unit) {
    var startHour by remember { mutableStateOf(9) }
    var duration by remember { mutableStateOf(60) }
    CupertinoDialog(
        onDismissRequest = onDismiss,
        confirmButton = { DialogButton(onClick = { onAdd(startHour * 60, startHour * 60 + duration) }) { Text("Add") } },
        dismissButton = { DialogButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Extra study block, $dayLabel") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                Text("Repeats every week on this weekday.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                Text("Start", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    listOf(7, 9, 11, 15, 18, 20).forEach { h -> Chip(text = "%02d:00".format(h), selected = startHour == h, onClick = { startHour = h }) }
                }
                Text("Length", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    listOf(60, 90, 120, 180).forEach { m -> Chip(text = "$m min", selected = duration == m, onClick = { duration = m }) }
                }
            }
        },
    )
}
