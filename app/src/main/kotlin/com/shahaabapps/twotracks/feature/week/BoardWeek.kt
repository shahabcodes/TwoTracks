package com.shahaabapps.twotracks.feature.week

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.BarlowFamily
import com.shahaabapps.twotracks.ui.board.BoardBack
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardLink
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardTitle
import com.shahaabapps.twotracks.ui.board.WhoMark
import java.time.format.DateTimeFormatter
import java.util.Locale

/** "1h15", "45m", or a dash for nothing: minutes as the board prints them. */
internal fun boardMinutes(m: Int): String = when {
    m <= 0 -> "—"
    m >= 60 -> "${m / 60}h" + if (m % 60 != 0) "%02d".format(m % 60) else ""
    else -> "${m}m"
}

/** "+1.4h", "-2h": the week's margin, signed. */
internal fun signedHours(h: Double): String {
    val v = if (h == Math.floor(h)) "%.0fh".format(Locale.ENGLISH, kotlin.math.abs(h)) else "%.1fh".format(Locale.ENGLISH, kotlin.math.abs(h))
    return (if (h >= 0) "+" else "-") + v
}

private val dayName = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)

/**
 * Your week on the Departures board, as its prototype draws it: the
 * timetable, three figures for what is scheduled, needed and the margin,
 * then each day as a block of departures.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun BoardTimetable(
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
    onAssign: (DayPlan, BlockRow) -> Unit,
    onAddBlock: (DayPlan) -> Unit,
) {
    val color = AppTheme.colors.track(state.viewingRole == PersonRole.TECH)
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(inner), contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().height(44.dp)) {
                        BoardBack(onBack, "Back to departures")
                        Spacer(Modifier.weight(1f))
                        BoardLink("Both lines", onClick = onOpenBothWeeks, size = 15.sp)
                    }
                    BoardMono((weekRange(state.weekStart) + " · " + state.viewingName).uppercase(Locale.ROOT))
                    BoardTitle(if (state.isMe) "Timetable" else "${state.viewingName}'s timetable", size = 52.sp)
                }
            }
            state.capacity?.let { cap ->
                item {
                    val scheduled = state.plannedMinutes / 60.0
                    val margin = scheduled - cap.weeklyNeedHours
                    val fits = cap.fits && margin >= 0
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 14.dp)) {
                        Figure("%.1fh".format(Locale.ENGLISH, scheduled), "Scheduled", Modifier.weight(1f))
                        Figure("%.1fh".format(Locale.ENGLISH, cap.weeklyNeedHours), "Needed", Modifier.weight(1f))
                        Figure(
                            signedHours(margin), if (fits) "On time" else "Delayed", Modifier.weight(1f).clickable(role = Role.Button, onClickLabel = "Ways to make it fit", onClick = onOpenFit),
                            ink = if (fits) AppTheme.colors.success else AppTheme.colors.warn,
                            background = if (fits) AppTheme.colors.allowedBackground else AppTheme.colors.blockedBackground,
                        )
                    }
                }
            }
            items(state.days, key = { it.date.toString() }) { day ->
                DayBlock(day, state.isMe, color, onOpenGoal, onAssign = { onAssign(day, it) }, onAddBlock = { onAddBlock(day) })
            }
            // What the prototype has no room for at the top: other weeks, whose week, and re-planning.
            item {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(18.dp), modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 16.dp)) {
                    BoardLink("‹ Last week", onPreviousWeek)
                    BoardLink("This week", onThisWeek)
                    BoardLink("Next week ›", onNextWeek)
                    BoardLink(if (state.isMe) "Your partner's line" else "Back to yours", onTogglePerson)
                    if (state.isMe) BoardLink("Re-plan", onReplan)
                }
            }
        }
    }
}

@Composable
private fun Figure(value: String, label: String, modifier: Modifier, ink: Color = AppTheme.colors.ink, background: Color = AppTheme.colors.card) {
    Column(modifier = modifier.clip(RoundedCornerShape(12.dp)).background(background).padding(horizontal = 12.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        BoardMono(value, color = ink, size = 20.sp, weight = FontWeight.Bold, spacing = 0.sp)
        BoardLabel(label, color = if (ink == AppTheme.colors.ink) AppTheme.colors.secondaryLabel else ink, spacing = 0.1.em)
    }
}

@Composable
private fun DayBlock(day: DayPlan, editable: Boolean, color: Color, onOpenGoal: (String) -> Unit, onAssign: (BlockRow) -> Unit, onAddBlock: () -> Unit) {
    val shown = day.blocks.filter { it.block.kind != BlockKind.WORK }
    val ink = if (day.isToday) color else AppTheme.colors.ink
    Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 10.dp)) {
        Row(
            verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().then(if (editable) Modifier.clickable(onClickLabel = "Add a block") { onAddBlock() } else Modifier).padding(bottom = 4.dp),
        ) {
            BoardLabel(day.date.format(dayName), color = ink, size = 17.sp, weight = FontWeight.Bold, spacing = 0.08.em)
            BoardMono("${day.date.dayOfMonth}", modifier = Modifier.padding(bottom = 2.dp))
            Spacer(Modifier.weight(1f))
            if (day.isToday) BoardLabel("Today", color = ink, weight = FontWeight.Bold, spacing = 0.1.em)
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
        if (shown.isEmpty()) {
            BoardLabel("No service · rest day", size = 15.sp, spacing = 0.1.em, color = AppTheme.colors.secondaryLabel.copy(alpha = 0.8f), modifier = Modifier.heightIn(min = 40.dp).padding(vertical = 11.dp))
        }
        shown.forEach { row ->
            val status = row.session?.status
            val gone = status == SessionStatus.DONE || status == SessionStatus.SKIPPED
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().heightIn(min = 40.dp)
                    .then(
                        if (row.block.kind == BlockKind.STUDY && editable) Modifier.clickable { onAssign(row) }
                        else if (row.goal != null) Modifier.clickable { onOpenGoal(row.goal.id) } else Modifier,
                    )
                    .drawBehind { drawLine(Color(0xFF22252B), Offset(0f, size.height), Offset(size.width, size.height), 1.dp.toPx()) },
            ) {
                BoardMono(Dates.minutesToClock(row.block.startMinutes), color = if (gone) AppTheme.colors.secondaryLabel else AppTheme.colors.ink, size = 14.sp, weight = FontWeight.Bold, spacing = 0.sp, modifier = Modifier.width(52.dp))
                WhoMark(
                    when {
                        status == SessionStatus.SKIPPED -> AppTheme.colors.destructive
                        row.block.kind == BlockKind.CLASS -> color.copy(alpha = 0.6f)
                        else -> color
                    },
                )
                androidx.compose.material3.Text(
                    row.goal?.let { "${it.title.substringBefore(" (")} · ${row.componentName}" } ?: if (editable) "Unplanned · tap to assign" else "Unplanned",
                    color = if (row.goal == null || gone) AppTheme.colors.secondaryLabel else AppTheme.colors.ink,
                    style = androidx.compose.ui.text.TextStyle(
                        fontFamily = BarlowFamily, fontSize = 14.sp,
                        textDecoration = if (status == SessionStatus.DONE) TextDecoration.LineThrough else TextDecoration.None,
                    ),
                    maxLines = 1, modifier = Modifier.weight(1f),
                )
                BoardMono("${row.block.endMinutes - row.block.startMinutes}m", textAlign = TextAlign.End, modifier = Modifier.width(44.dp))
            }
        }
    }
}

/**
 * The last seven days for both of you, as the prototype's arrivals board:
 * each of you with your hours in your colour, then a row a day with what
 * each studied and whether the day arrived, which is what the streak counts.
 */
@Composable
internal fun BoardBothLines(state: BothWeeksUiState, onBack: () -> Unit) {
    val me = state.me
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        Column(modifier = Modifier.fillMaxSize().padding(inner).verticalScroll(rememberScrollState()).padding(horizontal = BoardGutter).padding(bottom = 24.dp)) {
            Box(Modifier.padding(top = 6.dp).height(44.dp), contentAlignment = Alignment.CenterStart) { BoardBack(onBack, "Back to your timetable") }
            BoardMono("LAST 7 DAYS · ARRIVALS", modifier = Modifier.padding(top = 6.dp))
            BoardTitle("Both lines", size = 52.sp, modifier = Modifier.padding(top = 6.dp))
            if (me == null) return@Column
            val partner = state.partner
            val meIsTech = me.role == PersonRole.TECH
            val mine = AppTheme.colors.track(meIsTech)
            val theirs = AppTheme.colors.track(!meIsTech)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 14.dp)) {
                Line(me, mine, Modifier.weight(1f))
                if (partner != null) Line(partner, theirs, Modifier.weight(1f))
                else Column(Modifier.weight(1f).clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.card).padding(14.dp)) { BoardText("Your partner has not boarded yet.", size = 13.sp) }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 18.dp, bottom = 6.dp)) {
                BoardLabel("Day", spacing = 0.12.em, modifier = Modifier.width(50.dp))
                BoardLabel(me.name, spacing = 0.12.em, modifier = Modifier.weight(1f))
                BoardLabel(partner?.name ?: "", spacing = 0.12.em, modifier = Modifier.weight(1f))
                BoardLabel("Streak", spacing = 0.12.em, modifier = Modifier.width(72.dp), textAlign = TextAlign.End)
            }
            Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
            state.days.forEachIndexed { i, day ->
                val a = me.minutesByDay.getOrElse(i) { 0 }
                val b = partner?.minutesByDay?.getOrElse(i) { 0 } ?: 0
                val today = i == state.days.lastIndex
                val (status, ink) = when {
                    a > 0 && b > 0 -> "Arrived" to AppTheme.colors.success
                    today -> "Boarding" to AppTheme.colors.accent
                    else -> "No arrival" to AppTheme.colors.secondaryLabel
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                    Column(Modifier.width(50.dp)) {
                        BoardLabel(day.format(dayName), color = AppTheme.colors.ink, size = 16.sp, weight = FontWeight.Bold, spacing = 0.06.em)
                        BoardMono("${day.dayOfMonth}", size = 10.sp, spacing = 0.sp)
                    }
                    BoardMono(boardMinutes(a), color = if (a > 0) mine else AppTheme.colors.strongBorder, size = 14.sp, weight = FontWeight.Bold, spacing = 0.sp, modifier = Modifier.weight(1f))
                    BoardMono(if (partner == null) "" else boardMinutes(b), color = if (b > 0) theirs else AppTheme.colors.strongBorder, size = 14.sp, weight = FontWeight.Bold, spacing = 0.sp, modifier = Modifier.weight(1f))
                    BoardLabel(status, color = ink, size = 14.sp, weight = FontWeight.Bold, spacing = 0.08.em, modifier = Modifier.width(72.dp), textAlign = TextAlign.End)
                }
                Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
            }
            BoardText(
                "A day arrives when both of you studied, and that is what the ${state.togetherStreak}-day streak counts." +
                    if (partner != null) " Today is still boarding until midnight on each of your clocks." else "",
                size = 13.sp, modifier = Modifier.padding(top = 14.dp),
            )
        }
    }
}

@Composable
private fun Line(p: PersonWeek, color: Color, modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier.clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.card)
            .drawBehind { drawRect(color, size = androidx.compose.ui.geometry.Size(size.width, 3.dp.toPx())) }
            .padding(start = 14.dp, end = 14.dp, top = 15.dp, bottom = 12.dp),
    ) {
        androidx.compose.material3.Text(p.name, color = AppTheme.colors.ink, style = androidx.compose.ui.text.TextStyle(fontFamily = com.shahaabapps.core.ui.theme.CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 18.sp))
        BoardMono("%.1fh".format(Locale.ENGLISH, p.totalMinutes / 60.0), color = color, size = 28.sp, weight = FontWeight.Bold, spacing = 0.sp)
        BoardText(p.byGoal.ifBlank { "Nothing logged yet" }, size = 12.sp)
    }
}
