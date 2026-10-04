package com.shahaabapps.twotracks.feature.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.model.Deadline
import com.shahaabapps.core.model.Habit
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.planner.GoalProgress
import com.shahaabapps.core.planner.ProgressCalculator
import com.shahaabapps.core.planner.Streaks
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

data class WeekBar(val weekStart: LocalDate, val minutesByGoal: Map<String, Int>) {
    val total: Int get() = minutesByGoal.values.sum()
}

data class HabitProgress(val habit: Habit, val thisWeek: Int, val total: Int)

data class DeadlineRow(val deadline: Deadline, val days: Long)

data class PersonProgress(
    val role: PersonRole,
    val name: String,
    val totalHours: Double,
    val requiredHours: Double,
    val streak: Int,
    val longestStreak: Int,
    val weeks: List<WeekBar>,
    val goals: List<GoalProgress>,
    val habits: List<HabitProgress>,
)

data class ProgressUiState(
    val me: PersonProgress? = null,
    val partner: PersonProgress? = null,
    val goalsById: Map<String, StudyGoal> = emptyMap(),
    val deadlines: List<DeadlineRow> = emptyList(),
    val showPartner: Boolean = false,
)

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    private val dataStore: CoupleDataStore,
) : ViewModel() {

    private val showPartner = MutableStateFlow(false)

    val uiState: StateFlow<ProgressUiState> = combine(
        combine(sessionStore.session, showPartner) { s, p -> s to p },
        dataStore.goals, dataStore.logs, combine(dataStore.habits, dataStore.habitLogs) { h, l -> h to l }, dataStore.deadlines,
    ) { (session, partner), goals, logs, (habits, habitLogs), deadlines ->
        if (session == null) return@combine ProgressUiState()
        val today = Clock.today(session.me)

        fun person(role: PersonRole): PersonProgress {
            val roleGoals = goals.filter { it.owner == role }
            val progress = ProgressCalculator.computeAll(roleGoals, logs, today)
            val roleLogs = logs.filter { it.owner == role }
            val weekStart = Dates.weekStart(today)
            val weeks = (7L downTo 0L).map { off ->
                val ws = weekStart.minusWeeks(off)
                val from = Dates.format(ws)
                val to = Dates.format(ws.plusDays(6))
                WeekBar(ws, roleLogs.filter { it.dateIso in from..to }.groupBy { it.goalId }.mapValues { e -> e.value.sumOf { it.minutes } })
            }
            val weekIso = Dates.format(weekStart)
            return PersonProgress(
                role = role, name = session.nameFor(role),
                totalHours = roleLogs.sumOf { it.minutes } / 60.0,
                requiredHours = roleGoals.filter { it.active }.sumOf { it.totalHoursRequired },
                streak = Streaks.current(roleLogs.map { it.dateIso }, today),
                longestStreak = Streaks.longest(roleLogs.map { it.dateIso }),
                weeks = weeks,
                goals = progress.values.filter { it.goal.active || it.hoursLogged > 0 }.sortedBy { it.goal.priority },
                habits = habits.filter { it.owner == role && it.active }.map { h ->
                    val mine = habitLogs.filter { it.habitId == h.id }
                    HabitProgress(h, mine.filter { it.dateIso >= weekIso }.sumOf { it.value }, mine.sumOf { it.value })
                },
            )
        }
        ProgressUiState(
            me = person(session.myRole), partner = person(session.partnerRole), goalsById = goals.associateBy { it.id },
            deadlines = deadlines.mapNotNull { d -> Dates.parseOrNull(d.dateIso)?.let { DeadlineRow(d, ChronoUnit.DAYS.between(today, it)) } }
                .filter { it.days >= -30 }.sortedBy { it.days },
            showPartner = partner,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProgressUiState())

    fun toggle() { showPartner.value = !showPartner.value }
}
