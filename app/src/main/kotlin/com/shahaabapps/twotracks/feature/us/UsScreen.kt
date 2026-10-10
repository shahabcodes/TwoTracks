package com.shahaabapps.twotracks.feature.us

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.model.Habit
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.data.study.forRole
import com.shahaabapps.core.planner.Dates
import androidx.compose.foundation.border
import androidx.compose.ui.text.style.TextAlign
import com.shahaabapps.core.planner.MissedDays
import com.shahaabapps.core.planner.PlanStart
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import com.shahaabapps.core.ui.components.Avatar
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoDisclosure
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoScaffold
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.IosColors
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.PersonPlan
import com.shahaabapps.twotracks.session.PlanSnapshot
import com.shahaabapps.twotracks.session.PlanStore
import com.shahaabapps.twotracks.ui.Routes
import com.shahaabapps.twotracks.ui.components.AnimatedNumber
import com.shahaabapps.twotracks.ui.components.SectionLabel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

data class CountRow(val habit: Habit, val thisWeek: Int, val total: Int)

data class UsUiState(
    val plan: PlanSnapshot? = null,
    /** Per role: the last 28 days with studied / missed / rest / upcoming states and the impact of missed days. */
    val dots: Map<PersonRole, MissedDays.Report> = emptyMap(),
    /** Per role: this calendar month, day by day. */
    val month: Map<PersonRole, List<MissedDays.Day>> = emptyMap(),
    val counts: Map<PersonRole, List<CountRow>> = emptyMap(),
    val totalHours: Map<PersonRole, Double> = emptyMap(),
)

@HiltViewModel
class UsViewModel @Inject constructor(
    planStore: PlanStore,
    dataStore: CoupleDataStore,
) : ViewModel() {
    val uiState: StateFlow<UsUiState> = combine(planStore.plan, dataStore.logs, dataStore.habits, dataStore.habitLogs, dataStore.availability) { plan, logs, habits, habitLogs, availability ->
        if (plan == null) return@combine UsUiState()
        val dots = PersonRole.entries.associateWith { role ->
            val days = logs.filter { it.owner == role }.map { it.dateIso }.toSet()
            val person = plan.forRole(role)
            val planStart = PlanStart.effective(
                plan.session.profileFor(role)?.startDateIso,
                person.levels.map { it.level.startDateIso },
            )
            MissedDays.report(person.today, availability.forRole(role), days, planStart)
        }
        val months = PersonRole.entries.associateWith { role ->
            val days = logs.filter { it.owner == role }.map { it.dateIso }.toSet()
            val person = plan.forRole(role)
            val planStart = PlanStart.effective(
                plan.session.profileFor(role)?.startDateIso,
                person.levels.map { it.level.startDateIso },
            )
            MissedDays.month(YearMonth.from(person.today), person.today, availability.forRole(role), days, planStart)
        }
        val counts = PersonRole.entries.associateWith { role ->
            val weekIso = Dates.format(Dates.weekStart(plan.forRole(role).today))
            habits.filter { it.owner == role && it.active }.map { h ->
                val mine = habitLogs.filter { it.habitId == h.id }
                CountRow(h, mine.filter { it.dateIso >= weekIso }.sumOf { it.value }, mine.sumOf { it.value })
            }
        }
        val totals = PersonRole.entries.associateWith { role -> logs.filter { it.owner == role }.sumOf { it.minutes } / 60.0 }
        UsUiState(plan, dots, months, counts, totals)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UsUiState())
}

/** Tab 2: the two of you side by side, then Settings. */
@Composable
fun UsScreen(onNavigate: (String) -> Unit, onOpenLevel: (String) -> Unit, viewModel: UsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val plan = state.plan
    CupertinoScaffold(title = "US") {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            if (plan != null) {
                listOf(plan.me, plan.partner).forEach { person ->
                    item {
                        PersonCard(person, plan, state, onClick = {
                            if (person.role == plan.me.role) person.current?.let { onOpenLevel(it.level.id) } else onNavigate(Routes.PARTNER)
                        })
                    }
                }
            }
            item { SectionLabel("App") }
            item {
                CupertinoListSection {
                    CupertinoListRow("Settings", subtitle = "Profile, schedule, reminders, partner invite", leadingIcon = Icons.Filled.Settings, iconColor = IosColors.Gray, showDivider = false, onClick = { onNavigate(Routes.SETTINGS) }, trailing = { CupertinoDisclosure() })
                }
            }
        }
    }
}

@Composable
private fun PersonCard(person: PersonPlan, plan: PlanSnapshot, state: UsUiState, onClick: () -> Unit) {
    val profile = plan.session.profileFor(person.role)
    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
            Avatar(name = person.name, photoUrl = profile?.photoUrl, size = 44.dp)
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(person.name, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.weight(1f))
                    CupertinoDisclosure()
                }
                Text(
                    if (profile == null) "Not joined yet" else "${person.streak}-day streak · longest ${person.longestStreak} · %.0f h total".format(state.totalHours[person.role] ?: 0.0),
                    style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                )
            }
        }
        state.dots[person.role]?.let { report ->
            state.month[person.role]?.let { StudyCalendar(days = it, today = person.today) }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.padding(top = Spacing.xs)) {
                // The same tints the days are drawn in, or the key describes
                // a calendar nobody is looking at.
                Legend(AppTheme.colors.accent.copy(alpha = 0.28f), "studied")
                Legend(AppTheme.colors.warn.copy(alpha = 0.28f), "missed")
                Legend(AppTheme.colors.fill, "rest")
            }
            if (report.missedCount > 0) {
                Text(
                    "${report.missedCount} missed study day${if (report.missedCount == 1) "" else "s"} · %.1f h not studied · the plan lands about ${report.delayDays} day${if (report.delayDays == 1L) "" else "s"} later unless it is made up."
                        .format(report.lostHours),
                    style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.warn, modifier = Modifier.padding(top = Spacing.xs),
                )
            } else if (report.days.any { it.state == MissedDays.DayState.LOGGED }) {
                Text("No missed study days. Every planned day was used.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.success, modifier = Modifier.padding(top = Spacing.xs))
            }
        }
        person.current?.let { lvl ->
            Text("Level ${lvl.level.number} · ${lvl.level.name} · ${lvl.requiredDone} of ${lvl.requiredTotal}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = Spacing.xs))
        }
        state.counts[person.role]?.forEach { c ->
            Row(modifier = Modifier.padding(top = Spacing.xs)) {
                Text(c.habit.name, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.weight(1f))
                AnimatedNumber(value = c.habit.weeklyTarget?.let { c.thisWeek } ?: c.total, style = MaterialTheme.typography.bodyMedium)
                Text(
                    c.habit.weeklyTarget?.let { " / $it this week" } ?: c.habit.totalTarget?.let { " / $it" } ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

/**
 * The month, as a calendar.
 *
 * What was here before was four rows of seven dots counted back from today.
 * It looked like a calendar and was not one: no dates, and columns that each
 * held a different weekday depending on what day it happened to be, so the
 * one thing a calendar is for — seeing that it is always Thursdays you miss —
 * could not be read off it.
 *
 * Now the columns are weekdays, the days carry their dates, and the month
 * begins where it really begins. Days still to come are drawn faintly: they
 * are not yet anything, and showing them as rest days would be a small lie
 * every month.
 */
@Composable
private fun StudyCalendar(days: List<MissedDays.Day>, today: LocalDate) {
    if (days.isEmpty()) return
    val first = days.first().date
    // ISO weeks start on Monday, and so does the template's dayOfWeek.
    val leading = first.dayOfWeek.value - 1
    val weeks = (List<MissedDays.Day?>(leading) { null } + days).chunked(7)

    Text(
        first.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        style = MaterialTheme.typography.labelMedium,
        color = AppTheme.colors.secondaryLabel,
        modifier = Modifier.padding(top = Spacing.md, bottom = 6.dp),
    )
    Row(modifier = Modifier.fillMaxWidth()) {
        DayOfWeek.entries.forEach { d ->
            Text(
                d.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.colors.secondaryLabel,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
        }
    }
    weeks.forEach { week ->
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
            // A short last week is padded so its days stay under their own
            // weekday rather than spreading across the row.
            val padded = week + List(7 - week.size) { null }
            padded.forEach { day ->
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    if (day != null) CalendarDay(day, isToday = day.date == today)
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(day: MissedDays.Day, isToday: Boolean) {
    // Tinted rather than filled solid, with the page's own ink on top.
    // A solid accent with a white date measured 4.02:1 on the Blue palette,
    // under the 4.5:1 body rule — ContrastTest caught it. The tint is the
    // same treatment the checks and warm-up blocks already use, and is
    // readable on every accent.
    val tint = when (day.state) {
        MissedDays.DayState.LOGGED -> AppTheme.colors.accent.copy(alpha = 0.28f)
        MissedDays.DayState.MISSED -> AppTheme.colors.warn.copy(alpha = 0.28f)
        MissedDays.DayState.REST -> AppTheme.colors.fill
        MissedDays.DayState.UPCOMING -> AppTheme.colors.fill.copy(alpha = AppTheme.colors.fill.alpha * 0.4f)
    }
    val ink = when (day.state) {
        MissedDays.DayState.UPCOMING -> AppTheme.colors.secondaryLabel
        else -> MaterialTheme.colorScheme.onSurface
    }
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(tint)
            .then(if (isToday) Modifier.border(2.dp, AppTheme.colors.accent, CircleShape) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            day.date.dayOfMonth.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = ink,
        )
    }
}

@Composable
private fun Legend(color: androidx.compose.ui.graphics.Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
                .border(1.dp, AppTheme.colors.separator, CircleShape),
        )
        Text(label, style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.secondaryLabel)
    }
}
