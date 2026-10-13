package com.shahaabapps.twotracks.feature.levels

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.model.LevelItem
import com.shahaabapps.core.model.LevelItemKind
import com.shahaabapps.core.planner.LevelItemProgress
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.core.planner.LevelState
import com.shahaabapps.core.ui.components.CupertinoSwitch
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.DialogButton
import com.shahaabapps.twotracks.ui.components.SwipeToDelete

@Composable
fun LevelScreen(
    levelId: String,
    onBack: () -> Unit,
    onOpenGoal: (String) -> Unit,
    onNewGoal: () -> Unit = {},
    viewModel: LevelViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LevelContent(
        state = state, onBack = onBack, onOpenGoal = onOpenGoal, onNewGoal = onNewGoal,
        onToggleTask = viewModel::toggleTask, onAddTask = viewModel::addTask, onRemove = viewModel::removeItem,
        onStartNext = viewModel::startNextEarly, onChooseBranch = viewModel::chooseBranch, onMessageShown = viewModel::clearMessage,
    )
}

/**
 * One level, as the prototype draws it: which level and whether it is the
 * current one, its name and purpose, how many required items are done and
 * how many days are left, a segment per required item, then the checklist
 * under an ink rule and a way to add to it.
 */
@Composable
fun LevelContent(
    state: LevelUiState,
    onBack: () -> Unit,
    onOpenGoal: (String) -> Unit,
    onNewGoal: () -> Unit,
    onToggleTask: (LevelItem) -> Unit,
    onAddTask: (String, String, Boolean) -> Unit,
    onRemove: (LevelItem) -> Unit,
    onStartNext: () -> Unit,
    onChooseBranch: (String) -> Unit,
    onMessageShown: () -> Unit,
) {
    val snackbar = remember { SnackbarHostState() }
    val haptic = LocalHapticFeedback.current
    var adding by remember { mutableStateOf(false) }
    var addTask by remember { mutableStateOf(false) }
    var confirmRemove by remember { mutableStateOf<LevelItem?>(null) }
    var swipeReset by remember { mutableIntStateOf(0) }
    LaunchedEffect(state.message) { state.message?.let { snackbar.showSnackbar(it); onMessageShown() } }

    val p = state.progress
    val clickOf: (LevelItem) -> (() -> Unit)? = { item ->
        when (item.kind) {
            LevelItemKind.GOAL -> item.goalId?.takeIf { it in state.goalsById }?.let { id -> { onOpenGoal(id) } }
            LevelItemKind.TASK -> if (state.isMine) ({ haptic.performHapticFeedback(HapticFeedbackType.LongPress); onToggleTask(item) }) else null
            LevelItemKind.COUNT -> null
        }
    }
    val removable: (LevelItem) -> Boolean = { it.id.startsWith("custom-") && state.isMine }
    if (AppTheme.departures) {
        BoardLevelBody(
            state, snackbar, swipeReset, onBack = onBack, clickOf = clickOf, removable = removable,
            onRemove = { confirmRemove = it }, onAdd = { adding = true }, onStartNext = onStartNext, onChooseBranch = onChooseBranch,
        )
    } else {
        LevelBody(state, snackbar, swipeReset, onBack, clickOf, removable, onRemove = { confirmRemove = it }, onAdd = { adding = true }, onStartNext = onStartNext, onChooseBranch = onChooseBranch)
    }

    if (adding) {
        // The prototype's one button. A goal brings hours and skills; a
        // checklist item is a single thing to tick, which a level needs too.
        CupertinoDialog(
            onDismissRequest = { adding = false },
            confirmButton = { DialogButton(onClick = { adding = false; onNewGoal() }) { Text("Goal with hours") } },
            dismissButton = { DialogButton(onClick = { adding = false; addTask = true }) { Text("Checklist item") } },
            title = { Text("Add to Level ${p?.level?.number ?: ""}") },
            text = { Text("A goal has skills with hour budgets, like AZ-204 or German, and shows up in your week. A checklist item is one thing to tick off. Swipe your own items left to remove them.") },
        )
    }
    confirmRemove?.let { item ->
        val isGoal = item.goalId?.startsWith("custom-") == true
        CupertinoDialog(
            onDismissRequest = { confirmRemove = null },
            confirmButton = { DialogButton(onClick = { onRemove(item); confirmRemove = null }, destructive = true) { Text("Remove") } },
            dismissButton = { DialogButton(onClick = { confirmRemove = null; swipeReset++ }) { Text("Keep") } },
            title = { Text("Remove ${item.title}?") },
            text = { Text(if (isGoal) "The goal, its skills and its logged hours are deleted too." else "This takes the item off the level checklist.") },
        )
    }
    if (addTask) {
        var text by remember { mutableStateOf("") }
        var detail by remember { mutableStateOf("") }
        var required by remember { mutableStateOf(true) }
        CupertinoDialog(
            onDismissRequest = { addTask = false },
            confirmButton = { DialogButton(enabled = text.isNotBlank(), onClick = { onAddTask(text, detail, required); addTask = false }) { Text("Add") } },
            dismissButton = { DialogButton(onClick = { addTask = false }) { Text("Cancel") } },
            title = { Text("New checklist item") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.padding(top = 4.dp)) {
                    CupertinoTextField(value = text, onValueChange = { text = it }, placeholder = "e.g. Book the IELTS test", modifier = Modifier.fillMaxWidth())
                    CupertinoTextField(value = detail, onValueChange = { detail = it }, placeholder = "Note (optional)", modifier = Modifier.fillMaxWidth())
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Required to finish the level", modifier = Modifier.weight(1f))
                        CupertinoSwitch(checked = required, onCheckedChange = { required = it })
                    }
                }
            },
        )
    }
}

@Composable
private fun LevelBody(
    state: LevelUiState,
    snackbar: SnackbarHostState,
    swipeReset: Int,
    onBack: () -> Unit,
    clickOf: (LevelItem) -> (() -> Unit)?,
    removable: (LevelItem) -> Boolean,
    onRemove: (LevelItem) -> Unit,
    onAdd: () -> Unit,
    onStartNext: () -> Unit,
    onChooseBranch: (String) -> Unit,
) {
    val p = state.progress
    val color = AppTheme.colors.track(state.isTech)
    val eyebrowInk = when {
        p == null -> color
        p.state == LevelState.DONE -> AppTheme.colors.success
        p.isOverdue -> AppTheme.colors.warn
        p.state == LevelState.OPEN -> color
        else -> AppTheme.colors.secondaryLabel
    }
    DetailScaffold(
        title = p?.level?.name ?: "Level", onBack = onBack, backLabel = "Back to levels",
        eyebrow = p?.let { levelEyebrow(it, if (state.isMine) null else state.ownerName) }, eyebrowColor = eyebrowInk,
    ) {
        if (p == null) return@DetailScaffold
        Box {
            LazyColumn(contentPadding = PaddingValues(bottom = Spacing.xl)) {
                item { Header(p, color) }
                item { HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule, modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 20.dp)) }
                p.items.forEach { ip ->
                    item(key = ip.item.id) {
                        val item = ip.item
                        val row: @Composable () -> Unit = { ItemRow(ip, color, state, onClick = clickOf(item)) }
                        if (removable(item)) {
                            // Re-keying on swipeReset snaps the row back if the user taps "Keep".
                            key(item.id, swipeReset) { SwipeToDelete(onDelete = { onRemove(item) }) { Box(Modifier.background(AppTheme.colors.groupedBackground)) { row() } } }
                        } else {
                            row()
                        }
                    }
                }
                if (p.level.number == 5 && state.isMine) {
                    item { Branch(p.level.branch, onChooseBranch) }
                }
                if (state.isMine) {
                    item {
                        Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            val shape = RoundedCornerShape(12.dp)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().height(50.dp).clip(shape).border(Spacing.rule, AppTheme.colors.separator, shape)
                                    .clickable(role = Role.Button, onClick = onAdd),
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = null, tint = AppTheme.colors.ink, modifier = Modifier.size(16.dp))
                                Text("Add a goal to this level", style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp), color = AppTheme.colors.ink)
                            }
                            val next = state.next
                            if (next != null && next.state == LevelState.LOCKED) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxWidth().height(50.dp).clip(shape).clickable(role = Role.Button, onClick = onStartNext),
                                ) { Text("Start Level ${next.level.number} early", style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp), color = AppTheme.colors.accentText) }
                            }
                        }
                    }
                }
            }
            SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

/** "LEVEL 1 · NOW", "LEVEL 2 · LATER", "SANYA'S LEVEL 1 · NOW". */
internal fun levelEyebrow(p: LevelProgress, owner: String?): String {
    val stateWord = when {
        p.state == LevelState.DONE -> "done"
        p.isOverdue -> "overdue"
        p.state == LevelState.OPEN -> "now"
        else -> "later"
    }
    return (if (owner != null) "$owner's level" else "Level") + " ${p.level.number} · $stateWord"
}

@Composable
private fun Header(p: LevelProgress, color: Color) {
    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (p.level.summary.isNotBlank()) Text(p.level.summary, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.padding(top = 8.dp)) {
            Stat("${p.requiredDone} / ${p.requiredTotal}", "required done")
            val days = p.daysLeft
            val start = p.startDate
            when {
                p.state == LevelState.LOCKED && start != null ->
                    Stat("${java.time.temporal.ChronoUnit.DAYS.between(p.today, start).coerceAtLeast(0)}", "days until it opens")
                p.state == LevelState.DONE -> Stat("Done", p.level.completedAtIso?.let { "on ${Clock.formatDate(it.take(10))}" } ?: "every required item")
                days != null && days >= 0 -> Stat("$days", "days, to ${Clock.formatDate(p.level.dueDateIso)}")
                days != null -> Stat("${-days}", "days over, since ${Clock.formatDate(p.level.dueDateIso)}", AppTheme.colors.warn)
            }
        }
        if (p.requiredTotal > 0) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(top = 4.dp)) {
                repeat(p.requiredTotal) { i -> Box(Modifier.weight(1f).height(4.dp).background(if (i < p.requiredDone) color else AppTheme.colors.separator)) }
            }
        }
    }
}

@Composable
private fun Stat(value: String, label: String, ink: Color = AppTheme.colors.ink) {
    Column {
        Text(value, style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp), fontWeight = FontWeight.SemiBold, color = ink)
        Text(label, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
    }
}

@Composable
private fun ItemRow(ip: LevelItemProgress, color: Color, state: LevelUiState, onClick: (() -> Unit)?) {
    val item = ip.item
    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth().heightIn(min = 68.dp).then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.width(22.dp)) {
                when (item.kind) {
                    LevelItemKind.TASK -> CheckSquare(ip.done)
                    else -> Box(Modifier.width(4.dp).height(26.dp).background(if (item.required) color else AppTheme.colors.separator))
                }
            }
            Column(modifier = Modifier.weight(1f).padding(vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    item.title, style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (item.required) FontWeight.SemiBold else FontWeight.Medium,
                    color = if (item.kind == LevelItemKind.TASK && ip.done) AppTheme.colors.secondaryLabel else AppTheme.colors.ink,
                    textDecoration = if (item.kind == LevelItemKind.TASK && ip.done) TextDecoration.LineThrough else null,
                )
                val line = itemLine(ip, state)
                if (line.isNotBlank()) Text(line, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
            }
            when {
                item.kind == LevelItemKind.GOAL && ip.done -> Icon(Icons.Filled.Check, contentDescription = "Passed", tint = AppTheme.colors.success, modifier = Modifier.size(18.dp))
                item.kind == LevelItemKind.GOAL && onClick != null -> Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(18.dp))
                item.kind == LevelItemKind.COUNT -> Text("${ip.weekCount ?: 0}", style = MaterialTheme.typography.titleMedium, color = color)
            }
        }
        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
    }
}

/** "Goal · 12 of 120 h · by 31 Mar 2027", "Optional · …", or a task's note. */
internal fun itemLine(ip: LevelItemProgress, state: LevelUiState): String {
    val item = ip.item
    val goal = item.goalId?.let { state.goalsById[it] }
    return buildList {
        if (!item.required) add("Optional")
        when (item.kind) {
            LevelItemKind.GOAL -> {
                if (item.required) add(if (ip.done) "Passed" else "Goal")
                item.goalId?.let { state.goalHours[it] }?.takeIf { it.second > 0 }?.let { (done, need) -> add("$done of $need h") }
                (item.dueDateIso ?: goal?.targetDateIso)?.takeIf { it.isNotBlank() }?.let { add("by ${Clock.formatDate(it)}") }
            }
            LevelItemKind.COUNT -> add("${ip.weekCount ?: 0} this week")
            LevelItemKind.TASK -> item.dueDateIso?.let { add("by ${Clock.formatDate(it)}") }
        }
        if (item.detail.isNotBlank()) add(item.detail)
    }.joinToString(" · ")
}

@Composable
private fun CheckSquare(done: Boolean) {
    val shape = RoundedCornerShape(4.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(20.dp).clip(shape).background(if (done) AppTheme.colors.ink else Color.Transparent).border(Spacing.rule, AppTheme.colors.ink, shape),
    ) { if (done) Icon(Icons.Filled.Check, contentDescription = "Done", tint = AppTheme.colors.groupedBackground, modifier = Modifier.size(14.dp)) }
}

/** The checkpoint's fork. Not in the prototype, which shows Level 1; level 5 needs it. */
@Composable
private fun Branch(branch: String?, onChoose: (String) -> Unit) {
    Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Which way? The other items stay visible but stop counting.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("offer" to "Offer in hand", "backup" to "No offer yet").forEach { (id, label) ->
                val on = branch == id
                val shape = RoundedCornerShape(10.dp)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(1f).height(44.dp).clip(shape).background(if (on) AppTheme.colors.ink else Color.Transparent)
                        .border(Spacing.rule, if (on) AppTheme.colors.ink else AppTheme.colors.separator, shape).clickable(role = Role.RadioButton) { onChoose(id) },
                ) { Text(label, style = MaterialTheme.typography.labelLarge, color = if (on) AppTheme.colors.groupedBackground else AppTheme.colors.ink) }
            }
        }
    }
}
