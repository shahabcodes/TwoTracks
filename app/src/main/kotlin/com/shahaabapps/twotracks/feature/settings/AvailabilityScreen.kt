package com.shahaabapps.twotracks.feature.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.DialogButton
import com.shahaabapps.twotracks.ui.components.SegmentedChoice
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

/** Edits the signed-in person's weekly template. Changes are saved, and the weeks re-planned, on the way out. */
@Composable
fun AvailabilityScreen(onBack: () -> Unit, viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var blocks by remember { mutableStateOf<List<AvailabilityBlock>?>(null) }
    LaunchedEffect(state.availability) { if (blocks == null && state.session != null) blocks = state.availability.blocks }
    val current = blocks ?: return
    val changed = current != state.availability.blocks
    val leave = {
        if (changed) viewModel.saveAvailability(current)
        onBack()
    }
    BackHandler(onBack = leave)
    AvailabilityContent(
        blocks = current, isTech = state.session?.myRole != com.shahaabapps.core.model.PersonRole.TEACHER, goals = state.myGoals,
        onBack = leave, onAdd = { blocks = current + it }, onRemove = { b -> blocks = current - b },
    )
}

/** Hours of each kind in a week: "9.4 h", "45 h". */
internal fun kindHours(blocks: List<AvailabilityBlock>, kind: BlockKind): String {
    val h = blocks.filter { it.kind == kind }.sumOf { it.endMinutes - it.startMinutes } / 60.0
    return if (h == Math.floor(h)) "%.0f h".format(Locale.ENGLISH, h) else "%.1f h".format(Locale.ENGLISH, h)
}

/** The hours the grid spans: at least 06 to 23, more when a block runs outside them. */
internal fun gridHours(blocks: List<AvailabilityBlock>): IntRange {
    val from = minOf(6, (blocks.minOfOrNull { it.startMinutes } ?: 360) / 60)
    val to = maxOf(23, ((blocks.maxOfOrNull { it.endMinutes } ?: 1380) + 59) / 60)
    return from..to.coerceAtMost(24)
}

/**
 * The week as the prototype draws it: a column per day, study blocks in your
 * colour and work in grey, hours down the side, and Add a block held at the
 * foot. Tap a block to remove it.
 */
@Composable
fun AvailabilityContent(
    blocks: List<AvailabilityBlock>,
    isTech: Boolean,
    goals: List<StudyGoal>,
    onBack: () -> Unit,
    onAdd: (AvailabilityBlock) -> Unit,
    onRemove: (AvailabilityBlock) -> Unit,
) {
    var adding by remember { mutableStateOf(false) }
    var removing by remember { mutableStateOf<AvailabilityBlock?>(null) }
    val study = AppTheme.colors.track(isTech)
    fun colorOf(k: BlockKind): Color = when (k) {
        BlockKind.STUDY -> study
        BlockKind.CLASS -> study.copy(alpha = 0.45f)
        BlockKind.WORK -> Color.Unspecified
    }
    DetailScaffold(title = "Weekly hours", onBack = onBack, backLabel = "Back to settings") {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("The template the plan fills. Study blocks get sessions; work blocks never do. Times are on your clock.", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(top = 4.dp)) {
                        Key(study, "Study · ${kindHours(blocks, BlockKind.STUDY)}")
                        if (blocks.any { it.kind == BlockKind.CLASS }) Key(colorOf(BlockKind.CLASS), "Class · ${kindHours(blocks, BlockKind.CLASS)}")
                        Key(AppTheme.colors.separator, "Work · ${kindHours(blocks, BlockKind.WORK)}")
                    }
                }
                val hours = gridHours(blocks)
                val hourDp = 24.dp
                val height = hourDp * (hours.last - hours.first)
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Box(Modifier.width(30.dp))
                        (1..7).forEach { d ->
                            Text(
                                DayOfWeek.of(d).getDisplayName(TextStyle.NARROW, Locale.ENGLISH), style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, modifier = Modifier.weight(1f).padding(bottom = 6.dp),
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Box(Modifier.width(30.dp).height(height)) {
                            hours.filter { (it - hours.first) % 2 == 0 && it < hours.last }.forEach { h ->
                                Text(
                                    "%02d".format(h), style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 9.sp), color = AppTheme.colors.secondaryLabel,
                                    textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth().padding(end = 4.dp).offset(y = hourDp * (h - hours.first) - 5.dp),
                                )
                            }
                        }
                        (1..7).forEach { d ->
                            Box(Modifier.weight(1f).height(height).background(AppTheme.colors.fill.copy(alpha = AppTheme.colors.fill.alpha * 0.4f))) {
                                blocks.filter { it.dayOfWeek == d }.sortedBy { it.kind != BlockKind.WORK }.forEach { b ->
                                    val top = hourDp * ((b.startMinutes - hours.first * 60) / 60f)
                                    val h = hourDp * ((b.endMinutes - b.startMinutes) / 60f)
                                    val shape = RoundedCornerShape(3.dp)
                                    Box(
                                        Modifier.padding(horizontal = 1.dp).offset(y = top).fillMaxWidth().height(h).clip(shape)
                                            .background(if (b.kind == BlockKind.WORK) AppTheme.colors.separator else colorOf(b.kind))
                                            .semantics { contentDescription = "${b.label.ifBlank { b.kind.name.lowercase() }}, ${Dates.minutesToClock(b.startMinutes)} to ${Dates.minutesToClock(b.endMinutes)}" }
                                            .clickable(role = Role.Button, onClickLabel = "Remove") { removing = b },
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Column(modifier = Modifier.background(AppTheme.colors.groupedBackground).navigationBarsPadding()) {
                HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                val shape = RoundedCornerShape(12.dp)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 12.dp, bottom = 24.dp).fillMaxWidth().height(52.dp)
                        .clip(shape).background(AppTheme.colors.accent).clickable(role = Role.Button) { adding = true },
                ) { Text("Add a block", style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp), color = AppTheme.colors.onSolid) }
            }
        }
    }

    removing?.let { b ->
        CupertinoDialog(
            onDismissRequest = { removing = null },
            confirmButton = { DialogButton(onClick = { onRemove(b); removing = null }, destructive = true) { Text("Remove") } },
            dismissButton = { DialogButton(onClick = { removing = null }) { Text("Keep") } },
            title = { Text("${DayOfWeek.of(b.dayOfWeek).getDisplayName(TextStyle.FULL, Locale.ENGLISH)}, ${Dates.minutesToClock(b.startMinutes)} to ${Dates.minutesToClock(b.endMinutes)}") },
            text = { Text("${b.label.ifBlank { b.kind.name.lowercase().replaceFirstChar { it.uppercase() } }}. Remove it from the week? The plan is redone when you leave this page.") },
        )
    }
    if (adding) AddBlockDialog(goals, onAdd = { onAdd(it); adding = false }, onDismiss = { adding = false })
}

@Composable
private fun Key(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(Modifier.size(12.dp).background(color))
        Text(label, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddBlockDialog(goals: List<StudyGoal>, onAdd: (AvailabilityBlock) -> Unit, onDismiss: () -> Unit) {
    var days by remember { mutableStateOf(setOf(1)) }
    var kind by remember { mutableStateOf(BlockKind.STUDY) }
    var start by remember { mutableStateOf("19:00") }
    var end by remember { mutableStateOf("20:00") }
    var label by remember { mutableStateOf("") }
    var goalId by remember { mutableStateOf(goals.firstOrNull { it.active }?.id) }
    fun parse(t: String): Int? = t.split(":").takeIf { it.size == 2 }?.let { (h, m) -> h.toIntOrNull()?.let { hh -> m.toIntOrNull()?.let { mm -> hh * 60 + mm } } }
    val s = parse(start)
    val e = parse(end)
    val valid = days.isNotEmpty() && s != null && e != null && e > s
    CupertinoDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            DialogButton(enabled = valid, onClick = {
                days.sorted().forEach { day ->
                    onAdd(
                        AvailabilityBlock(
                            id = "${kind.name.lowercase()}-$day-$s-${System.currentTimeMillis() % 100000}", dayOfWeek = day, startMinutes = s!!, endMinutes = e!!, kind = kind,
                            label = label.ifBlank { when (kind) { BlockKind.WORK -> "Work"; BlockKind.CLASS -> "Class"; BlockKind.STUDY -> "Study slot" } },
                            goalId = if (kind == BlockKind.CLASS) goalId else null,
                        ),
                    )
                }
            }) { Text("Add") }
        },
        dismissButton = { DialogButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Add a block") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                SegmentedChoice(options = BlockKind.entries, selected = kind, label = { it.name.lowercase().replaceFirstChar { c -> c.uppercase() } }, onSelect = { kind = it })
                // More than one day at once: the same evening slot is usually wanted all week.
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    (1..7).forEach { d ->
                        val on = d in days
                        val shape = RoundedCornerShape(8.dp)
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.weight(1f).height(36.dp).clip(shape).background(if (on) AppTheme.colors.ink else Color.Transparent)
                                .border(Spacing.rule, if (on) AppTheme.colors.ink else AppTheme.colors.separator, shape)
                                .clickable(role = Role.Checkbox) { days = if (on) days - d else days + d },
                        ) { Text(DayOfWeek.of(d).getDisplayName(TextStyle.SHORT, Locale.ENGLISH).take(2), style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp), color = if (on) AppTheme.colors.groupedBackground else AppTheme.colors.ink) }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    CupertinoTextField(value = start, onValueChange = { start = it }, label = "From", placeholder = "HH:MM", keyboardType = KeyboardType.Number, modifier = Modifier.weight(1f))
                    CupertinoTextField(value = end, onValueChange = { end = it }, label = "To", placeholder = "HH:MM", keyboardType = KeyboardType.Number, modifier = Modifier.weight(1f))
                }
                CupertinoTextField(value = label, onValueChange = { label = it }, placeholder = "Label (optional)")
                if (kind == BlockKind.CLASS) {
                    Text("Class counts toward", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        goals.take(4).forEach { g -> Chip(text = g.title.take(14), selected = g.id == goalId, onClick = { goalId = g.id }) }
                    }
                }
            }
        },
    )
}
