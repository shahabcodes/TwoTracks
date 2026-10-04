package com.shahaabapps.twotracks.feature.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.data.tracking.TrackingRepository
import com.shahaabapps.core.model.Deadline
import com.shahaabapps.core.model.HabitLog
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.model.Profile
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.twotracks.reminders.SessionPlanning
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.PersonPlan
import com.shahaabapps.twotracks.session.PlanStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.session.StudyActions
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

/** One person's card on Today. */
data class PersonCard(
    val role: PersonRole,
    val name: String,
    val profile: Profile?,
    val sessions: List<SessionRow>,
    val todayMinutes: Int,
    val streak: Int,
    val localTime: String,
    val isRestDay: Boolean,
)

data class TodayUiState(
    val loading: Boolean = true,
    val today: LocalDate = LocalDate.now(),
    val level: LevelProgress? = null,
    val me: PersonCard? = null,
    val partner: PersonCard? = null,
    val status: Pair<String, Boolean> = "" to true,
    val goals: List<StudyGoal> = emptyList(),
    val activeGoals: List<StudyGoal> = emptyList(),
    val nextDeadline: Deadline? = null,
    val daysToDeadline: Long? = null,
    val message: String? = null,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TodayViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    private val dataStore: CoupleDataStore,
    private val planStore: PlanStore,
    private val sessionPlanning: SessionPlanning,
    private val actions: StudyActions,
    private val trackingRepository: TrackingRepository,
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

    val uiState: StateFlow<TodayUiState> = combine(planStore.plan, sessionsFlow, dataStore.deadlines, message) { plan, sessions, deadlines, msg ->
        if (plan == null) return@combine TodayUiState(loading = true, message = msg)
        val goalsById = plan.me.goals.plus(plan.partner.goals).associateBy { it.id }

        fun card(p: PersonPlan): PersonCard {
            val profile = plan.session.profileFor(p.role)
            val todayIso = Dates.format(p.today)
            val rows = sessions.filter { it.owner == p.role && it.dateIso == todayIso }.sortedBy { it.startMinutes }.map { s ->
                val g = goalsById[s.goalId]
                SessionRow(
                    session = s, goal = g,
                    componentName = g?.components?.firstOrNull { it.id == s.componentId }?.name.orEmpty(),
                    timeLabel = "${Dates.minutesToClock(s.startMinutes)} to ${Dates.minutesToClock(s.endMinutes)}",
                )
            }
            return PersonCard(
                role = p.role, name = p.name, profile = profile, sessions = rows, todayMinutes = p.todayMinutes,
                streak = p.streak, localTime = Clock.formatTime(Clock.now(profile ?: plan.session.me)), isRestDay = rows.isEmpty(),
            )
        }

        val next = deadlines.mapNotNull { d -> Dates.parseOrNull(d.dateIso)?.let { d to it } }
            .filter { !it.second.isBefore(plan.me.today) }
            .minByOrNull { it.second }
        TodayUiState(
            loading = false, today = plan.me.today, level = plan.me.current, me = card(plan.me), partner = card(plan.partner),
            status = plan.me.statusSentence, goals = plan.me.goals, activeGoals = plan.me.activeGoals,
            nextDeadline = next?.first, daysToDeadline = next?.let { ChronoUnit.DAYS.between(plan.me.today, it.second) }, message = msg,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TodayUiState())

    /** One-tap Done: logs the planned minutes to the planned goal and skill. */
    fun done(row: SessionRow) = viewModelScope.launch {
        val s = row.session
        message.value = actions.logMinutes(s.goalId, s.componentId, s.durationMinutes, s.dateIso, sessionId = s.id, fromClass = s.isClass)
            ?: "Logged ${s.durationMinutes} min of ${row.goal?.title ?: "study"}"
    }

    fun logMinutes(goalId: String, componentId: String, minutes: Int, note: String, sessionId: String?, fromClass: Boolean) {
        viewModelScope.launch {
            message.value = actions.logMinutes(goalId, componentId, minutes, actions.todayIso(), note, sessionId, fromClass) ?: "Logged $minutes min"
        }
    }

    fun logSplit(goalId: String, minutesByComponent: Map<String, Int>, note: String, sessionId: String?) {
        viewModelScope.launch {
            var error: String? = null
            minutesByComponent.entries.forEachIndexed { index, (componentId, minutes) ->
                val sid = if (index == 0) sessionId else null
                error = error ?: actions.logMinutes(goalId, componentId, minutes, actions.todayIso(), note, sid, fromClass = true)
            }
            message.value = error ?: "Logged ${minutesByComponent.values.sum()} min across ${minutesByComponent.size} skills"
        }
    }

    fun skip(sessionId: String) = viewModelScope.launch { message.value = actions.skipSession(sessionId) }
    fun reopen(sessionId: String) = viewModelScope.launch { message.value = actions.reopenSession(sessionId) }

    fun addHabit(habitId: String, value: Int) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        trackingRepository.addHabitLog(s.coupleId, HabitLog(owner = s.myRole, habitId = habitId, dateIso = Clock.todayIso(s.me), value = value))
    }

    fun clearMessage() { message.value = null }
}
