package com.shahaabapps.twotracks.feature.study

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.model.GoalKind
import com.shahaabapps.core.model.MockScore
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.model.StudyLog
import com.shahaabapps.core.ui.components.CupertinoSwitch
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.BarlowFamily
import com.shahaabapps.core.ui.theme.CondensedFamily
import com.shahaabapps.core.ui.theme.JetBrainsFamily
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.board.BoardBack
import com.shahaabapps.twotracks.ui.board.BoardButton
import com.shahaabapps.twotracks.ui.board.BoardField
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardLink
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardOutlineButton
import com.shahaabapps.twotracks.ui.board.BoardStat
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardTitle
import com.shahaabapps.twotracks.ui.components.SwipeToDelete
import java.time.format.DateTimeFormatter
import java.util.Locale

/** "120H", the legs' hours as the board's form totals them. */
internal fun boardTotal(parts: List<ComponentDraft>): String = totalHours(parts).removeSuffix(" h total") + "H"

/** "128H", "4.5H": hours as the fit check's bars print them. */
internal fun boardHours(h: Double): String = fmtHours(h).replace(" h", "H")

/** "12h", "4.5h": a figure on the goal's cards. */
internal fun goalHours(h: Double): String =
    if (h >= 10) "%.0fh".format(Locale.ENGLISH, h) else "%.1fh".format(Locale.ENGLISH, h).replace(".0h", "h")

private val dayMonthNumbers = DateTimeFormatter.ofPattern("dd/MM", Locale.ENGLISH)
private val dayMonthWords = DateTimeFormatter.ofPattern("d MMMM", Locale.ENGLISH)

/**
 * A goal on the Departures board, as its prototype draws it: the class and
 * fee above the title in capitals, three figures on cards, whether it is on
 * time, each leg with its hours, the practice scores on a card, and the one
 * thing to do next held at the foot.
 */
@Composable
internal fun BoardGoalBody(
    state: GoalDetailUiState,
    snackbar: SnackbarHostState,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onOpenCourse: (String) -> Unit,
    onAskPass: () -> Unit,
    onReopen: () -> Unit,
    onAddMock: () -> Unit,
    onDeleteMock: (MockScore) -> Unit,
    onDeleteLog: (StudyLog) -> Unit,
) {
    val goal = state.goal
    val p = state.progress
    val color = AppTheme.colors.track(goal?.owner != PersonRole.TEACHER)
    Scaffold(
        containerColor = AppTheme.colors.groupedBackground,
        bottomBar = { if (goal != null) GoalFoot(state, goal, onOpenCourse, onAskPass, onReopen) },
    ) { inner ->
        Box(Modifier.fillMaxSize().padding(inner)) {
            LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
                item {
                    Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().height(44.dp)) {
                            BoardBack(onBack, "Back to the level")
                            Box(Modifier.weight(1f))
                            if (state.isMine) BoardLink("Edit", onEdit, size = 15.sp)
                        }
                        if (goal == null) return@Column
                        BoardMono(goalEyebrow(goal).uppercase(Locale.ENGLISH))
                        BoardTitle(goal.title, size = 52.sp)
                        val why = goal.why.ifBlank { goal.subtitle }
                        if (why.isNotBlank()) BoardText(why)
                    }
                }
                if (goal == null || p == null) return@LazyColumn
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 14.dp).height(IntrinsicSize.Min)) {
                        val m = Modifier.weight(1f).fillMaxHeight()
                        if (state.hoursCounted) {
                            BoardStat(goalHours(p.hoursLogged), "Of %.0f".format(Locale.ENGLISH, p.hoursRequired), size = 20.sp, modifier = m)
                            BoardStat(p.weeklyNeedHours?.let { goalHours(it) } ?: "–", "A week", size = 20.sp, modifier = m)
                        } else {
                            BoardStat("${goal.components.size}", if (goal.components.size == 1) "Leg" else "Legs", size = 20.sp, modifier = m)
                            BoardStat(
                                p.targetDate?.let { java.time.temporal.ChronoUnit.DAYS.between(p.today, it).coerceAtLeast(0).toString() } ?: "–",
                                "Days left", size = 20.sp, modifier = m,
                            )
                        }
                        BoardStat(p.targetDate?.format(dayMonthNumbers) ?: "–", p.targetDate?.let { "${it.year} · due" } ?: "No date", size = 20.sp, modifier = m)
                    }
                }
                if (state.sentence.isNotBlank()) {
                    item {
                        val ink = if (state.sentenceGood) AppTheme.colors.success else AppTheme.colors.warn
                        Row(
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 10.dp),
                        ) {
                            Box(Modifier.size(8.dp).clip(CircleShape).background(ink))
                            BoardLabel(state.sentence, color = ink, size = 15.sp, weight = FontWeight.Bold, spacing = 0.08.em)
                        }
                    }
                }
                item {
                    Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 20.dp)) {
                        Row(modifier = Modifier.padding(bottom = 8.dp)) {
                            BoardLabel("Leg", spacing = 0.12.em, modifier = Modifier.weight(1f))
                            BoardLabel(if (state.hoursCounted) "Hours" else "Target", spacing = 0.12.em)
                        }
                        Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                    }
                }
                itemsIndexed(p.components, key = { _, c -> "part-" + c.component.id }) { _, c ->
                    Column(modifier = Modifier.padding(horizontal = BoardGutter)) {
                        Column(modifier = Modifier.padding(vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                BoardText(c.component.name, color = AppTheme.colors.ink, size = 14.sp, weight = FontWeight.Medium, modifier = Modifier.weight(1f))
                                val right = when {
                                    !state.hoursCounted -> c.component.targetScore?.let { "target $it" }.orEmpty()
                                    c.finishedByCourse -> "done"
                                    else -> "%.0f/%.0f".format(Locale.ENGLISH, c.hoursLogged, c.hoursRequired)
                                }
                                BoardMono(right.uppercase(Locale.ENGLISH), color = if (c.finishedByCourse) AppTheme.colors.success else AppTheme.colors.secondaryLabel, size = 12.sp, spacing = 0.sp)
                            }
                            if (state.hoursCounted) {
                                Box(Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(3.dp)).background(AppTheme.colors.separator)) {
                                    Box(
                                        Modifier.fillMaxWidth(c.fraction.toFloat().coerceIn(0.01f, 1f)).height(5.dp).clip(RoundedCornerShape(3.dp))
                                            .background(if (c.finishedByCourse) AppTheme.colors.success else color),
                                    )
                                }
                            }
                        }
                        Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                    }
                }
                item {
                    Column(
                        modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 20.dp).fillMaxWidth().clip(RoundedCornerShape(14.dp))
                            .background(AppTheme.colors.card).padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            BoardLabel("Practice scores", size = 13.sp, weight = FontWeight.Bold, spacing = 0.12.em, modifier = Modifier.weight(1f))
                            if (state.isMine) BoardLink("Add one", onAddMock)
                        }
                        if (state.mockScores.isEmpty()) {
                            BoardText(
                                if (state.courseId != null) "None yet. The mock exam in the course is the closest thing to the real one." else "None yet. Add a result after each practice test to see the trend.",
                                size = 14.sp,
                            )
                        } else {
                            state.mockScores.forEach { m ->
                                Swiped(state.isMine, onDelete = { onDeleteMock(m) }, background = AppTheme.colors.card) {
                                    Entry(
                                        m.scores.entries.joinToString(" · ") { (k, v) ->
                                            val name = goal.components.firstOrNull { it.id == k }?.name?.substringBefore(" (") ?: k
                                            if (m.scores.size == 1) "$name $v" else "${name.take(1)} $v"
                                        },
                                        Clock.formatDate(m.dateIso) + if (m.note.isNotBlank()) ", ${m.note}" else "",
                                    )
                                }
                            }
                        }
                    }
                }
                // Not in the prototype: the entries behind the hours, kept so a
                // wrong one can still be swiped away.
                if (state.recentLogs.isNotEmpty()) {
                    item {
                        Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 20.dp)) {
                            BoardLabel("Recent entries", spacing = 0.12.em, modifier = Modifier.padding(bottom = 8.dp))
                            Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                        }
                    }
                    itemsIndexed(state.recentLogs.take(10), key = { _, log -> "log-" + log.id }) { _, log ->
                        val name = goal.components.firstOrNull { it.id == log.componentId }?.name ?: log.componentId
                        Swiped(state.isMine, onDelete = { onDeleteLog(log) }, background = AppTheme.colors.groupedBackground) {
                            Column(Modifier.padding(horizontal = BoardGutter)) {
                                Entry("$name, ${log.minutes} min" + if (log.fromClass) " (class)" else "", Clock.formatDate(log.dateIso) + if (log.note.isNotBlank()) ", ${log.note}" else "")
                                Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                            }
                        }
                    }
                }
                if (state.isMine && state.courseId != null) {
                    item {
                        BoardLink(
                            if (goal.passed) "Reopen, not passed after all" else "Mark as passed",
                            onClick = if (goal.passed) onReopen else onAskPass, size = 15.sp,
                            modifier = Modifier.padding(start = BoardGutter, top = 18.dp),
                        )
                    }
                }
            }
            SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
private fun Swiped(enabled: Boolean, onDelete: () -> Unit, background: Color, content: @Composable () -> Unit) {
    if (enabled) SwipeToDelete(onDelete = onDelete) { Box(Modifier.background(background)) { content() } } else content()
}

@Composable
private fun Entry(title: String, subtitle: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        BoardText(title, color = AppTheme.colors.ink, size = 15.sp, weight = FontWeight.SemiBold)
        BoardMono(subtitle.uppercase(Locale.ENGLISH), spacing = 0.sp)
    }
}

/**
 * The prototype's foot says LOG STUDY TIME. Minutes are no longer typed in,
 * so it carries what moves a goal on instead, as the Parallel goal does: the
 * course that feeds it, or marking it passed.
 */
@Composable
private fun GoalFoot(state: GoalDetailUiState, goal: StudyGoal, onOpenCourse: (String) -> Unit, onPass: () -> Unit, onReopen: () -> Unit) {
    val courseId = state.courseId
    val (label, action) = when {
        courseId != null -> "Open the course" to { onOpenCourse(courseId) }
        !state.isMine -> return
        goal.passed -> "Reopen" to onReopen
        else -> "Mark as passed" to onPass
    }
    Column(modifier = Modifier.fillMaxWidth().background(AppTheme.colors.groupedBackground).navigationBarsPadding()) {
        Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
        BoardButton(label, onClick = action, arrow = false, modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 12.dp, bottom = 24.dp))
    }
}

private val boardKinds = listOf(
    GoalKind.LANGUAGE_TEST to "Language test",
    GoalKind.LANGUAGE_LEVEL to "Language level",
    GoalKind.CERTIFICATION to "Certification",
    GoalKind.CUSTOM to "Custom",
)

/**
 * The goal form on the Departures board, as its prototype draws it: CANCEL
 * and EDIT GOAL in a bar, the fields on dark boxes, the class as chips, the
 * legs and their hours on a card, and SAVE held at the foot.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun BoardGoalEdit(
    s: GoalEditUiState,
    onCancel: () -> Unit,
    onTitle: (String) -> Unit,
    onWhy: (String) -> Unit,
    onKind: (GoalKind) -> Unit,
    onTargetDate: (String) -> Unit,
    onPriority: (String) -> Unit,
    onRequiredForLevel: (Boolean) -> Unit,
    onComponent: (Int, String?, String?, String?) -> Unit,
    onAddComponent: () -> Unit,
    onRemoveComponent: (Int) -> Unit,
    onPreset: (List<String>, Int) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
) {
    Scaffold(
        containerColor = AppTheme.colors.groupedBackground,
        bottomBar = {
            Column(modifier = Modifier.background(AppTheme.colors.groupedBackground).navigationBarsPadding().imePadding()) {
                Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                s.error?.let { BoardText(it, color = AppTheme.colors.destructive, size = 13.sp, modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 10.dp)) }
                BoardButton(
                    if (s.saving) "Saving" else "Save", onClick = onSave, enabled = s.canSave && !s.saving, arrow = false,
                    modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 12.dp, bottom = 24.dp),
                )
            }
        },
    ) { inner ->
        Column(modifier = Modifier.fillMaxSize().padding(inner)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = BoardGutter).padding(top = 6.dp).heightIn(min = 44.dp)) {
                Box(Modifier.weight(1f)) { BoardLink("Cancel", onCancel, color = AppTheme.colors.secondaryLabel, size = 15.sp) }
                BoardLabel(
                    if (!s.isNew) "Edit goal" else if (s.levelId != null) "New goal for this stop" else "New goal",
                    color = AppTheme.colors.ink, size = 17.sp, weight = FontWeight.Bold, spacing = 0.12.em, modifier = Modifier.semantics { heading() },
                )
                Box(Modifier.weight(1f))
            }
            if (!s.loaded) return@Column
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = BoardGutter).padding(top = 14.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                BoardField(
                    s.title, onTitle, "Title", placeholder = "AZ-305 Solutions Architect", height = 52.dp,
                    textStyle = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, color = AppTheme.colors.ink),
                )
                BoardField(s.why, onWhy, "Why it matters", placeholder = "What it opens up", singleLine = false, height = 64.dp)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    BoardLabel("Class")
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        boardKinds.forEach { (kind, label) ->
                            val on = s.kind == kind
                            val shape = RoundedCornerShape(8.dp)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.height(40.dp).clip(shape).background(if (on) AppTheme.colors.accent else AppTheme.colors.card)
                                    .border(if (on) 1.5.dp else 1.dp, if (on) AppTheme.colors.accent else AppTheme.colors.separator, shape)
                                    .clickable(role = Role.RadioButton) { onKind(kind) }.padding(horizontal = 12.dp),
                            ) { BoardLabel(label, color = if (on) AppTheme.colors.onSolid else AppTheme.colors.ink, size = 15.sp, weight = FontWeight.Bold, spacing = 0.06.em) }
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    BoardField(s.targetDateIso, onTargetDate, "Arrive by", placeholder = "2027-03-31", mono = true, keyboardType = KeyboardType.Number, height = 48.dp, modifier = Modifier.weight(1.4f))
                    BoardField(s.priority, onPriority, "Priority", placeholder = "50", mono = true, keyboardType = KeyboardType.Number, height = 48.dp, modifier = Modifier.weight(1f))
                }
                if (s.levelId != null && s.isNew) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            BoardText("Required to clear the stop", color = AppTheme.colors.ink)
                            BoardText("Optional goals stay on the stop but do not take study slots.", size = 13.sp)
                        }
                        CupertinoSwitch(checked = s.requiredForLevel, onCheckedChange = onRequiredForLevel)
                    }
                }
                Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.card).padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.heightIn(min = 40.dp)) {
                        BoardLabel("Legs and hours", size = 13.sp, weight = FontWeight.Bold, spacing = 0.12.em, modifier = Modifier.weight(1f))
                        BoardMono(boardTotal(s.components), color = AppTheme.colors.accent, size = 12.sp, spacing = 0.sp)
                    }
                    Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                    if (s.components.isEmpty()) {
                        // A new goal starts with nothing in it; these fill it in one tap.
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                            BoardLink("IELTS 4 skills", { onPreset(listOf("Listening", "Reading", "Writing", "Speaking"), 40) })
                            BoardLink("Language", { onPreset(listOf("Vocabulary", "Grammar", "Listening", "Speaking"), 30) })
                            BoardLink("Exam domains", { onPreset(listOf("Domain 1", "Domain 2", "Domain 3", "Practice exams"), 15) })
                        }
                    }
                    val scored = s.kind == GoalKind.LANGUAGE_TEST
                    s.components.forEachIndexed { i, c ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(min = 46.dp)) {
                            Inline(c.name, { onComponent(i, it, null, null) }, "Leg", "Leg name", Modifier.weight(1f))
                            if (scored) Inline(c.targetScore, { onComponent(i, null, null, it) }, "band", "Target band for ${c.name}", Modifier.width(52.dp), boxed = true)
                            Inline(
                                c.hours, { onComponent(i, null, it.filter { ch -> ch.isDigit() || ch == '.' }, null) }, "h", "Hours for ${c.name}",
                                Modifier.width(52.dp), boxed = true, keyboardType = KeyboardType.Decimal,
                            )
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(36.dp).clip(CircleShape).clickable(role = Role.Button, onClickLabel = "Remove ${c.name}") { onRemoveComponent(i) },
                            ) { Icon(Icons.Filled.Close, contentDescription = "Remove ${c.name}", tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(16.dp)) }
                        }
                        Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                    }
                    Box(Modifier.padding(top = 4.dp).heightIn(min = 44.dp), contentAlignment = Alignment.CenterStart) { BoardLink("+ Add a leg", onAddComponent, size = 15.sp) }
                }
                if (s.canDelete) BoardLink("Cancel this service", onDelete, color = AppTheme.colors.destructive, size = 15.sp)
            }
        }
    }
}

/** A field inside the legs card: bare for a name, on a near-black box for a number. */
@Composable
private fun Inline(value: String, onChange: (String) -> Unit, placeholder: String, label: String, modifier: Modifier, boxed: Boolean = false, keyboardType: KeyboardType = KeyboardType.Text) {
    val style = if (boxed) TextStyle(fontFamily = JetBrainsFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AppTheme.colors.ink, textAlign = TextAlign.End)
    else TextStyle(fontFamily = BarlowFamily, fontSize = 15.sp, color = AppTheme.colors.ink)
    BasicTextField(
        value = value, onValueChange = onChange, singleLine = true, textStyle = style, cursorBrush = SolidColor(AppTheme.colors.accent),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier.height(38.dp).clip(RoundedCornerShape(6.dp)).background(if (boxed) AppTheme.colors.deep else Color.Transparent).semantics { contentDescription = label },
        decorationBox = { inner ->
            Box(contentAlignment = if (boxed) Alignment.CenterEnd else Alignment.CenterStart, modifier = Modifier.fillMaxSize().padding(horizontal = if (boxed) 6.dp else 0.dp)) {
                if (value.isEmpty()) Text(placeholder, style = style.copy(color = AppTheme.colors.secondaryLabel, fontWeight = FontWeight.Normal))
                inner()
            }
        },
    )
}

/**
 * The fit check on the Departures board, as its prototype draws it: DELAYED
 * on an orange chip with the stop, the headline in capitals, what is needed
 * against what the blocks hold as two bars, the ways out as lettered cards,
 * and BACK beside the chosen one's button.
 */
@Composable
internal fun BoardFitContent(fit: FitCheck, onChoose: (FitChoice) -> Unit, onBack: () -> Unit) {
    val r = fit.result
    val options = fitOptions(fit)
    var picked by remember(fit) { mutableIntStateOf(0) }
    val available = r.weeklyCapacityHours * r.weeksLeft
    val due = Dates.parseOrNull(fit.levelDueIso)
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = BoardGutter).navigationBarsPadding().padding(top = 20.dp, bottom = 36.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.height(26.dp).clip(RoundedCornerShape(6.dp)).background(AppTheme.colors.warn).padding(horizontal = 10.dp)) {
                BoardLabel("Delayed", color = AppTheme.colors.onSolid, size = 15.sp, weight = FontWeight.Bold, spacing = 0.1.em)
            }
            BoardMono("Stop %02d · %s".format(fit.levelNumber, fit.levelName).uppercase(Locale.ENGLISH))
        }
        BoardTitle("Won't arrive by ${due?.format(dayMonthWords) ?: Clock.formatDate(fit.levelDueIso)}", size = 38.sp, modifier = Modifier.padding(top = 10.dp))
        BoardText(
            "Adding ${fmtHours(fit.addedHours)} leaves the stop ${fmtHours(r.demandHours)} of work. Your study blocks hold ${fmtHours(available)} before it is due.",
            color = AppTheme.colors.ink, modifier = Modifier.padding(top = 10.dp),
        )
        Column(
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.groupedBackground).padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val most = maxOf(r.demandHours, available, 0.1)
            FitBar("Needed", r.demandHours, (r.demandHours / most).toFloat(), AppTheme.colors.warn)
            FitBar("Blocks", available, (available / most).toFloat(), AppTheme.colors.secondaryLabel)
        }
        Column(modifier = Modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEachIndexed { i, o ->
                val on = picked == i
                val shape = RoundedCornerShape(12.dp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().clip(shape).background(AppTheme.colors.groupedBackground)
                        .border(if (on) 2.dp else 1.dp, if (on) AppTheme.colors.accent else AppTheme.colors.separator, shape)
                        .semantics { this.selected = on }.clickable(role = Role.RadioButton) { picked = i }.padding(horizontal = 14.dp, vertical = 12.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(top = 1.dp).size(26.dp).clip(RoundedCornerShape(6.dp)).background(if (on) AppTheme.colors.accent else AppTheme.colors.deep),
                    ) { BoardLabel("ABC".getOrElse(i) { 'D' + i - 3 }.toString(), color = if (on) AppTheme.colors.onSolid else AppTheme.colors.secondaryLabel, size = 13.sp, weight = FontWeight.Bold, spacing = 0.sp) }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        BoardText(o.title, color = AppTheme.colors.ink, weight = FontWeight.SemiBold)
                        BoardText(o.detail, size = 13.sp)
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 28.dp)) {
            BoardOutlineButton("Back", onBack, height = 52.dp, modifier = Modifier.weight(1f))
            BoardButton(options[picked].cta, onClick = { onChoose(options[picked].choice) }, arrow = false, modifier = Modifier.weight(1.7f))
        }
    }
}

@Composable
private fun FitBar(label: String, hours: Double, fraction: Float, fill: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        BoardLabel(label, size = 13.sp, weight = FontWeight.Bold, spacing = 0.12.em, modifier = Modifier.width(76.dp))
        Box(Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp)).background(AppTheme.colors.separator)) {
            Box(Modifier.fillMaxWidth(fraction.coerceIn(0.02f, 1f)).height(8.dp).clip(RoundedCornerShape(4.dp)).background(fill))
        }
        BoardMono(boardHours(hours), color = AppTheme.colors.ink, size = 13.sp, weight = FontWeight.Bold, spacing = 0.sp, textAlign = TextAlign.End, modifier = Modifier.width(52.dp))
    }
}

