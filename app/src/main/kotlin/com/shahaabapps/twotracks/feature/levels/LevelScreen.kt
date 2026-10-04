package com.shahaabapps.twotracks.feature.levels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.model.LevelItemKind
import com.shahaabapps.core.planner.LevelState
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoDisclosure
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoSecondaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.components.StatusChip
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.components.AnimatedSwap
import com.shahaabapps.twotracks.ui.components.PopIn
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.ProgressBar
import com.shahaabapps.twotracks.ui.components.SectionLabel

/** Level 2 of the app: one level's checklist with dates, progress bars and "Mark as passed". */
@Composable
fun LevelScreen(
    levelId: String,
    onBack: () -> Unit,
    onOpenGoal: (String) -> Unit,
    onOpenLevel: (String) -> Unit,
    viewModel: LevelViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val haptic = LocalHapticFeedback.current
    var confirmPass by remember { mutableStateOf<String?>(null) }
    var addTask by remember { mutableStateOf(false) }
    LaunchedEffect(state.message) { state.message?.let { snackbar.showSnackbar(it); viewModel.clearMessage() } }

    val p = state.progress
    DetailScaffold(title = p?.let { "Level ${it.level.number}" } ?: "Level", onBack = onBack) {
        if (p == null) return@DetailScaffold
        Box {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
                item {
                    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(p.level.name, style = MaterialTheme.typography.displayLarge, modifier = Modifier.weight(1f))
                            when (p.state) {
                                LevelState.DONE -> StatusChip("Done", AppTheme.colors.success)
                                LevelState.OPEN -> if (p.isOverdue) StatusChip("Overdue", AppTheme.colors.warn) else StatusChip("Current", AppTheme.colors.accent)
                                LevelState.LOCKED -> StatusChip("Locked", AppTheme.colors.secondaryLabel)
                            }
                        }
                        if (!state.isMine) Text("${state.ownerName}'s level", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                        Text(p.level.summary, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                        Text(
                            "Start by ${Clock.formatDate(p.level.startDateIso)} · complete by ${Clock.formatDate(p.level.dueDateIso)}" +
                                (p.daysLeft?.let { if (it >= 0) " · $it days left" else " · ${-it} days overdue" } ?: ""),
                            style = MaterialTheme.typography.bodySmall, color = if (p.isOverdue) AppTheme.colors.warn else AppTheme.colors.secondaryLabel,
                        )
                        ProgressBar(fraction = if (p.requiredTotal == 0) 0f else p.requiredDone.toFloat() / p.requiredTotal, modifier = Modifier.padding(top = Spacing.xs))
                        Text("${p.requiredDone} of ${p.requiredTotal} required items done", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                    }
                }

                item { SectionLabel("To complete this level") }
                item {
                    CupertinoListSection {
                        val items = p.items
                        items.forEachIndexed { i, ip ->
                            val item = ip.item
                            val goal = item.goalId?.let { state.goalsById[it] }
                            val subtitle = buildList {
                                when (item.kind) {
                                    LevelItemKind.GOAL -> ip.fraction?.let { add("%.0f%% of the hours".format(it * 100)) }
                                    LevelItemKind.COUNT -> add("${ip.weekCount ?: 0} this week")
                                    LevelItemKind.TASK -> Unit
                                }
                                item.dueDateIso?.let { add("by ${Clock.formatDate(it)}") }
                                if (!item.required) add("optional")
                                if (item.detail.isNotBlank()) add(item.detail)
                            }.joinToString(" · ")
                            CupertinoListRow(
                                title = item.title,
                                subtitle = subtitle.ifBlank { null },
                                showDivider = i < items.lastIndex,
                                onClick = when (item.kind) {
                                    LevelItemKind.GOAL -> goal?.let { g -> { onOpenGoal(g.id) } }
                                    LevelItemKind.TASK -> if (state.isMine) ({ haptic.performHapticFeedback(HapticFeedbackType.LongPress); viewModel.toggleTask(item) }) else null
                                    LevelItemKind.COUNT -> null
                                },
                                trailing = {
                                    when (item.kind) {
                                        LevelItemKind.TASK -> AnimatedSwap(ip.done) { done ->
                                            Icon(
                                                if (done) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank, contentDescription = null,
                                                tint = if (done) AppTheme.colors.success else AppTheme.colors.secondaryLabel,
                                            )
                                        }
                                        LevelItemKind.GOAL -> if (ip.done) {
                                            PopIn { Icon(Icons.Filled.CheckCircle, contentDescription = "Passed", tint = AppTheme.colors.success) }
                                        } else if (state.isMine && goal != null) {
                                            Chip(text = "Passed?", onClick = { confirmPass = goal.id })
                                        } else {
                                            CupertinoDisclosure()
                                        }
                                        LevelItemKind.COUNT -> Text("${ip.weekCount ?: 0}", style = MaterialTheme.typography.titleMedium, color = AppTheme.colors.accent)
                                    }
                                },
                            )
                        }
                    }
                }
                if (state.isMine) {
                    item { CupertinoTextButton(text = "+ Add your own item", onClick = { addTask = true }) }
                }

                if (p.level.number == 5 && state.isMine) {
                    item { SectionLabel("Which way?") }
                    item {
                        CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                            Text("At the checkpoint, pick the branch you are on. The other items stay visible but stop counting.", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.padding(top = Spacing.sm)) {
                                Chip("Offer in hand", selected = p.level.branch == "offer", onClick = { viewModel.chooseBranch("offer") })
                                Chip("No offer yet", selected = p.level.branch == "backup", onClick = { viewModel.chooseBranch("backup") })
                            }
                        }
                    }
                }

                state.next?.let { nxt ->
                    item { SectionLabel("Next") }
                    item {
                        CupertinoListSection {
                            CupertinoListRow(
                                title = "Level ${nxt.level.number} · ${nxt.level.name}",
                                subtitle = when (nxt.state) {
                                    LevelState.LOCKED -> "Starts ${Clock.formatDate(nxt.level.startDateIso)}"
                                    LevelState.OPEN -> "Already open"
                                    LevelState.DONE -> "Done"
                                },
                                showDivider = false, onClick = { onOpenLevel(nxt.level.id) }, trailing = { CupertinoDisclosure() },
                            )
                        }
                    }
                    if (state.isMine && nxt.state == LevelState.LOCKED && p.isComplete) {
                        item {
                            Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                                CupertinoSecondaryButton(text = "Start Level ${nxt.level.number} now", onClick = viewModel::startNextEarly, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }
            }
            SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }

    confirmPass?.let { goalId ->
        val title = state.goalsById[goalId]?.title ?: "this goal"
        AlertDialog(
            onDismissRequest = { confirmPass = null },
            confirmButton = { TextButton(onClick = { viewModel.setPassed(goalId, true); confirmPass = null }) { Text("Yes, passed") } },
            dismissButton = { TextButton(onClick = { confirmPass = null }) { Text("Not yet") } },
            title = { Text("Mark $title as passed?") },
            text = { Text("Use this when the exam is passed or the level is reached. Hours studied stay in your history. You can reopen it from the goal page.") },
        )
    }
    if (addTask) {
        var text by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { addTask = false },
            confirmButton = { TextButton(enabled = text.isNotBlank(), onClick = { viewModel.addTask(text); addTask = false }) { Text("Add") } },
            dismissButton = { TextButton(onClick = { addTask = false }) { Text("Cancel") } },
            title = { Text("New item for this level") },
            text = { CupertinoTextField(value = text, onValueChange = { text = it }, placeholder = "e.g. Book the IELTS test", modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) },
        )
    }
}
