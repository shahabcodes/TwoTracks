package com.shahaabapps.twotracks.feature.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.data.study.forRole
import com.shahaabapps.core.model.Deadline
import com.shahaabapps.core.model.Habit
import com.shahaabapps.core.model.HabitLog
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.model.Profile
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.CapacityReport
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.planner.ProgressCalculator
import com.shahaabapps.core.planner.Streaks
import com.shahaabapps.core.data.tracking.TrackingRepository
import com.shahaabapps.twotracks.reminders.SessionPlanning
import com.shahaabapps.twotracks.session.AppSession
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.session.StudyActions
import com.shahaabapps.twotracks.session.StudyTimer
import com.shahaabapps.twotracks.session.TimerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

data class SessionRow(
    val session: PlannedSession,
    val goal: StudyGoal?,
    val componentName: String,
    val timeLabel: String,
)

data class HabitRow(
    val habit: Habit,
    val thisWeek: Int,
    val total: Int,
)

data class PersonSummary(
    val profile: Profile?,
    val role: PersonRole,
    val todayMinutes: Int,
    val streak: Int,
    val sessions: List<SessionRow>,
    val localTime: String,
)

data class TodayUiState(
    val loading: Boolean = true,
    val today: LocalDate = LocalDate.now(),
    val me: PersonSummary? = null,
    val partner: PersonSummary? = null,
    val goals: List<StudyGoal> = emptyList(),
    val habits: List<HabitRow> = emptyList(),
    val nextDeadline: Deadline? = null,
    val daysToDeadline: Long? = null,
    val capacity: CapacityReport? = null,
    val timer: TimerState? = null,
    val timerGoalLabel: String = "",
    val message: String? = null,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TodayViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    private val dataStore: CoupleDataStore,
    private val sessionPlanning: SessionPlanning,
    private val actions: StudyActions,
    private val trackingRepository: TrackingRepository,
    timer: StudyTimer,
) : ViewModel() {

    private val message = MutableStateFlow<String?>(null)

    private val sessionsFlow = sessionStore.session.flatMapLatest { s ->
        if (s == null) flowOf(emptyList()) else {
            val today = Clock.today(s.me)
            sessionPlanning.sessionsBetween(today.minusDays(1), today.plusDays(1))
        }
    }

    init {
        viewModelScope.launch {
            sessionStore.session.collect { s -> if (s != null) sessionPlanning.ensurePlanned(Clock.today(s.me)) }
        }
    }

    private val studyPart = combine(sessionStore.session, dataStore.goals, dataStore.logs, dataStore.availability, sessionsFlow) {
            session, goals, logs, availability, sessions ->
        if (session == null) return@combine null
        val today = Clock.today(session.me)
        val todayIso = Dates.format(today)
        val partnerToday = Clock.todayIso(session.partner)
        val goalsById = goals.associateBy { it.id }

        fun rows(role: PersonRole, dateIso: String) = sessions
            .filter { it.owner == role && it.dateIso == dateIso }
            .sortedBy { it.startMinutes }
            .map { s ->
                val g = goalsById[s.goalId]
                SessionRow(
                    session = s, goal = g,
                    componentName = g?.components?.firstOrNull { it.id == s.componentId }?.name.orEmpty(),
                    timeLabel = "${Dates.minutesToClock(s.startMinutes)} to ${Dates.minutesToClock(s.endMinutes)}",
                )
            }

        fun summary(role: PersonRole, profile: Profile?, dateIso: String) = PersonSummary(
            profile = profile, role = role,
            todayMinutes = logs.filter { it.owner == role && it.dateIso == dateIso }.sumOf { it.minutes },
            streak = Streaks.current(logs.filter { it.owner == role }.map { it.dateIso }, Dates.parse(dateIso)),
            sessions = rows(role, dateIso),
            localTime = Clock.formatTime(Clock.now(profile)),
        )

        val myGoals = goals.filter { it.owner == session.myRole }
        val progress = ProgressCalculator.computeAll(myGoals, logs, today)
        val capacity = CapacityReport.build(availability.forRole(session.myRole).weeklyStudyMinutes, progress.values)

        StudyPart(
            session = session, today = today, goals = goals,
            me = summary(session.myRole, session.me, todayIso),
            partner = summary(session.partnerRole, session.partner, partnerToday),
            capacity = capacity,
        )
    }

    private data class StudyPart(
        val session: AppSession, val today: LocalDate, val goals: List<StudyGoal>,
        val me: PersonSummary, val partner: PersonSummary, val capacity: CapacityReport,
    )

    val uiState: StateFlow<TodayUiState> = combine(
        studyPart, dataStore.habits, dataStore.habitLogs, dataStore.deadlines, combine(timer.state, message) { t, m -> t to m },
    ) { part, habits, habitLogs, deadlines, (timerState, msg) ->
        if (part == null) return@combine TodayUiState(loading = true)
        val weekStart = Dates.weekStart(part.today)
        val weekIso = Dates.format(weekStart)
        val habitRows = habits.filter { it.owner == part.session.myRole && it.active }.map { h ->
            val mine = habitLogs.filter { it.habitId == h.id }
            HabitRow(habit = h, thisWeek = mine.filter { it.dateIso >= weekIso }.sumOf { it.value }, total = mine.sumOf { it.value })
        }
        val next = deadlines.mapNotNull { d -> Dates.parseOrNull(d.dateIso)?.let { d to it } }
            .filter { !it.second.isBefore(part.today) }
            .minByOrNull { it.second }
        val timerGoal = timerState?.let { t -> part.goals.firstOrNull { it.id == t.goalId } }
        val timerLabel = timerGoal?.let { g -> listOfNotNull(g.title, g.components.firstOrNull { it.id == timerState.componentId }?.name).joinToString(": ") }.orEmpty()
        TodayUiState(
            loading = false, today = part.today, me = part.me, partner = part.partner, goals = part.goals, habits = habitRows,
            nextDeadline = next?.first, daysToDeadline = next?.let { ChronoUnit.DAYS.between(part.today, it.second) },
            capacity = part.capacity, timer = timerState, timerGoalLabel = timerLabel, message = msg,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TodayUiState())

    fun logMinutes(goalId: String, componentId: String, minutes: Int, note: String, sessionId: String?, fromClass: Boolean) {
        viewModelScope.launch {
            val today = Clock.todayIso(sessionStore.current()?.me)
            message.value = actions.logMinutes(goalId, componentId, minutes, today, note, sessionId, fromClass) ?: "Logged $minutes min"
        }
    }

    fun logSplit(goalId: String, minutesByComponent: Map<String, Int>, note: String, sessionId: String?) {
        viewModelScope.launch {
            val today = Clock.todayIso(sessionStore.current()?.me)
            var error: String? = null
            minutesByComponent.entries.forEachIndexed { index, (componentId, minutes) ->
                val sid = if (index == 0) sessionId else null
                error = error ?: actions.logMinutes(goalId, componentId, minutes, today, note, sid, fromClass = true)
            }
            message.value = error ?: "Logged ${minutesByComponent.values.sum()} min across ${minutesByComponent.size} skills"
        }
    }

    fun skip(sessionId: String) = viewModelScope.launch { message.value = actions.skipSession(sessionId) }
    fun reopen(sessionId: String) = viewModelScope.launch { message.value = actions.reopenSession(sessionId) }

    fun startTimer(goalId: String, componentId: String, sessionId: String?) = actions.startTimer(goalId, componentId, sessionId)

    fun stopTimer() = viewModelScope.launch {
        message.value = actions.stopTimerAndLog(Clock.todayIso(sessionStore.current()?.me)) ?: "Timer logged"
    }

    fun discardTimer() = actions.discardTimer()

    fun addHabit(habitId: String, value: Int) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        trackingRepository.addHabitLog(s.coupleId, HabitLog(owner = s.myRole, habitId = habitId, dateIso = Clock.todayIso(s.me), value = value))
    }

    fun clearMessage() { message.value = null }

    fun statusDone(s: PlannedSession) = s.status == SessionStatus.DONE
}
