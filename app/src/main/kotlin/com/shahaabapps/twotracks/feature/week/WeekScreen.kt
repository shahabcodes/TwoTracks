package com.shahaabapps.twotracks.feature.week

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoScaffold
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.ProgressBar
import com.shahaabapps.twotracks.ui.components.SegmentedChoice

@Composable
fun WeekScreen(
    onOpenGoal: (String) -> Unit,
    viewModel: WeekViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    var assignRow by remember { mutableStateOf<Pair<DayPlan, BlockRow>?>(null) }
    var addBlockFor by remember { mutableStateOf<DayPlan?>(null) }

    LaunchedEffect(state.message) { state.message?.let { snackbar.showSnackbar(it); viewModel.clearMessage() } }

    CupertinoScaffold(title = "Week") {
        Box {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
                item {
                    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = viewModel::previousWeek) { Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Previous week") }
                            Text(
                                text = "${Clock.formatDayMonth(state.weekStart)} to ${Clock.formatDayMonth(state.weekStart.plusDays(6))}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f).clickable { viewModel.thisWeek() },
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            )
                            IconButton(onClick = viewModel::nextWeek) { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Next week") }
                        }
                        SegmentedChoice(
                            options = listOf(true, false), selected = state.isMe,
                            label = { if (it) "Me" else "Partner" }, onSelect = { if (it != state.isMe) viewModel.togglePerson() },
                        )
                    }
                }
                item {
                    state.capacity?.let { cap ->
                        CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                            Text("Capacity this week", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                            Text(
                                "%.1f h needed, %.1f h available".format(cap.weeklyNeedHours, cap.weeklyCapacityHours),
                                style = MaterialTheme.typography.titleMedium,
                                color = if (cap.fits) AppTheme.colors.success else AppTheme.colors.destructive,
                            )
                            ProgressBar(
                                fraction = if (cap.weeklyNeedHours <= 0) 0f else (cap.weeklyCapacityHours / cap.weeklyNeedHours).toFloat(),
                                color = if (cap.fits) AppTheme.colors.success else AppTheme.colors.warn,
                                modifier = Modifier.padding(vertical = Spacing.sm),
                            )
                            Text(
                                "Planned ${state.plannedMinutes / 60}h ${state.plannedMinutes % 60}m, logged ${state.doneMinutes / 60}h ${state.doneMinutes % 60}m",
                                style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                            )
                            if (!cap.fits) {
                                Text(
                                    "Over by %.1f h/week. Add weekend blocks, pause a goal, or move a target date.".format(cap.gapHours),
                                    style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.warn, modifier = Modifier.padding(top = Spacing.xs),
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.padding(top = Spacing.sm)) {
                                Chip(text = "Re-plan week", onClick = viewModel::replan)
                            }
                        }
                    }
                }
                items(state.days, key = { it.date.toString() }) { day ->
                    DayCard(
                        day = day,
                        onOpenGoal = onOpenGoal,
                        onAssign = { row -> assignRow = day to row },
                        onAddBlock = { addBlockFor = day },
                    )
                }
            }
            SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }

    assignRow?.let { (day, row) ->
        AlertDialog(
            onDismissRequest = { assignRow = null },
            confirmButton = { TextButton(onClick = { assignRow = null }) { Text("Cancel") } },
            title = { Text("${Clock.formatDayMonth(day.date)} ${Dates.minutesToClock(row.block.startMinutes)}") },
            text = {
                Column {
                    Text("Study which goal in this slot?", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                    state.myGoals.forEach { g ->
                        Text(
                            g.title, style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth().clickable { viewModel.assign(row.block, day.date, g); assignRow = null }.padding(vertical = 10.dp),
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
            onAdd = { start, end -> viewModel.addBlock(day.date.dayOfWeek.value, start, end, BlockKind.STUDY); addBlockFor = null },
        )
    }
}

@Composable
private fun DayCard(day: DayPlan, onOpenGoal: (String) -> Unit, onAssign: (BlockRow) -> Unit, onAddBlock: () -> Unit) {
    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                Clock.formatDayMonth(day.date),
                style = MaterialTheme.typography.titleMedium,
                color = if (day.isToday) AppTheme.colors.accent else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
            )
            Chip(text = "+ block", onClick = onAddBlock)
        }
        if (day.blocks.isEmpty()) {
            Text("Free day", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = Spacing.xs))
        }
        day.blocks.forEach { row ->
            val color = when (row.block.kind) {
                BlockKind.WORK -> AppTheme.colors.secondaryLabel
                BlockKind.CLASS -> AppTheme.colors.accent
                BlockKind.STUDY -> when (row.session?.status) {
                    SessionStatus.DONE -> AppTheme.colors.success
                    SessionStatus.SKIPPED -> AppTheme.colors.warn
                    else -> AppTheme.colors.accent
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.sm)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.10f))
                    .then(
                        if (row.block.kind == BlockKind.STUDY) Modifier.clickable { onAssign(row) }
                        else if (row.goal != null) Modifier.clickable { onOpenGoal(row.goal.id) } else Modifier,
                    )
                    .padding(horizontal = Spacing.sm, vertical = 8.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "${Dates.minutesToClock(row.block.startMinutes)} to ${Dates.minutesToClock(row.block.endMinutes)}  ${row.block.label}",
                        style = MaterialTheme.typography.bodySmall, color = color,
                    )
                    if (row.goal != null) {
                        Text("${row.goal.title}: ${row.componentName}", style = MaterialTheme.typography.bodyMedium)
                    } else if (row.block.kind != BlockKind.WORK) {
                        Text("Unplanned. Tap to assign", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                    }
                }
                row.session?.let { s ->
                    Text(
                        when (s.status) { SessionStatus.DONE -> "Done"; SessionStatus.SKIPPED -> "Skipped"; SessionStatus.PLANNED -> "" },
                        style = MaterialTheme.typography.labelMedium, color = color,
                    )
                }
            }
        }
    }
}

@Composable
private fun AddBlockDialog(dayLabel: String, onDismiss: () -> Unit, onAdd: (Int, Int) -> Unit) {
    var startHour by remember { mutableStateOf(9) }
    var duration by remember { mutableStateOf(60) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = { onAdd(startHour * 60, startHour * 60 + duration) }) { Text("Add") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Extra study block, $dayLabel") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                Text("Repeats every week on this weekday.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                Text("Start", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    listOf(7, 9, 11, 15, 18, 20).forEach { h -> Chip(text = "%02d:00".format(h), selected = startHour == h, onClick = { startHour = h }) }
                }
                Text("Length", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    listOf(60, 90, 120, 180).forEach { m -> Chip(text = "$m min", selected = duration == m, onClick = { duration = m }) }
                }
            }
        },
    )
}
