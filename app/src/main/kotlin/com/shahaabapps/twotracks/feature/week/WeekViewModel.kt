package com.shahaabapps.twotracks.feature.week

import com.shahaabapps.twotracks.session.Celebrations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.data.study.StudyRepository
import com.shahaabapps.core.data.study.forRole
import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.CapacityReport
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.planner.ProgressCalculator
import com.shahaabapps.twotracks.reminders.ReminderScheduler
import com.shahaabapps.twotracks.reminders.SessionPlanning
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DayPlan(
    val date: LocalDate,
    val isToday: Boolean,
    val blocks: List<BlockRow>,
)

data class BlockRow(
    val block: AvailabilityBlock,
    val session: PlannedSession?,
    val goal: StudyGoal?,
    val componentName: String,
)

data class WeekUiState(
    val weekStart: LocalDate = LocalDate.now(),
    val days: List<DayPlan> = emptyList(),
    val viewingRole: PersonRole = PersonRole.TECH,
    /** The first name of whoever's week this is. */
    val viewingName: String = "",
    val isMe: Boolean = true,
    val myGoals: List<StudyGoal> = emptyList(),
    val capacity: CapacityReport? = null,
    val plannedMinutes: Int = 0,
    val doneMinutes: Int = 0,
    val message: String? = null,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WeekViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    private val dataStore: CoupleDataStore,
    private val sessionPlanning: SessionPlanning,
    private val studyRepository: StudyRepository,
    private val reminderScheduler: ReminderScheduler,
    private val celebrations: Celebrations,
) : ViewModel() {

    private val weekOffset = MutableStateFlow(0)
    private val viewPartner = MutableStateFlow(false)
    private val message = MutableStateFlow<String?>(null)

    private val weekStartFlow = combine(sessionStore.session, weekOffset) { s, offset ->
        Dates.weekStart(Clock.today(s?.me)).plusWeeks(offset.toLong())
    }

    private val sessionsFlow = weekStartFlow.flatMapLatest { ws -> sessionPlanning.sessionsBetween(ws, ws.plusDays(6)) }

    val uiState: StateFlow<WeekUiState> = combine(
        combine(sessionStore.session, weekStartFlow, viewPartner) { s, ws, vp -> Triple(s, ws, vp) },
        dataStore.goals, dataStore.logs, dataStore.availability, combine(sessionsFlow, message) { a, b -> a to b },
    ) { (session, weekStart, vp), goals, logs, availability, (sessions, msg) ->
        if (session == null) return@combine WeekUiState()
        val role = if (vp) session.partnerRole else session.myRole
        val avail = availability.forRole(role)
        val today = Clock.today(session.profileFor(role) ?: session.me)
        val goalsById = goals.associateBy { it.id }
        val days = Dates.daysOfWeek(weekStart).map { date ->
            val iso = Dates.format(date)
            val blocks = avail.blocks.filter { it.dayOfWeek == date.dayOfWeek.value }.sortedBy { it.startMinutes }.map { b ->
                val session = sessions.firstOrNull { it.owner == role && it.dateIso == iso && it.blockId == b.id }
                val g = session?.let { goalsById[it.goalId] }
                BlockRow(b, session, g, g?.components?.firstOrNull { it.id == session.componentId }?.name.orEmpty())
            }
            DayPlan(date, date == today, blocks)
        }
        val roleGoals = goals.filter { it.owner == role }
        val progress = ProgressCalculator.computeAll(roleGoals, logs, today)
        val weekIso = Dates.format(weekStart)
        val weekEndIso = Dates.format(weekStart.plusDays(6))
        WeekUiState(
            weekStart = weekStart, days = days, viewingRole = role, viewingName = session.profileFor(role)?.name.orEmpty(), isMe = !vp, myGoals = roleGoals.filter { it.active },
            capacity = CapacityReport.build(avail.weeklyStudyMinutes, progress.values),
            plannedMinutes = sessions.filter { it.owner == role }.sumOf { it.durationMinutes },
            doneMinutes = logs.filter { it.owner == role && it.dateIso in weekIso..weekEndIso }.sumOf { it.minutes },
            message = msg,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WeekUiState())

    fun previousWeek() { weekOffset.value -= 1 }
    fun nextWeek() { weekOffset.value += 1 }
    fun thisWeek() { weekOffset.value = 0 }
    fun togglePerson() { viewPartner.value = !viewPartner.value }
    fun clearMessage() { message.value = null }

    /** Regenerate this week's PLANNED sessions for the viewed person. */
    fun replan() = viewModelScope.launch {
        val s = uiState.value
        sessionPlanning.replan(s.weekStart, s.viewingRole)
        reminderScheduler.refresh()
        message.value = "Week re-planned"
    }

    /** Pin a block to a goal for this week and re-plan around it. */
    fun assign(block: AvailabilityBlock, date: LocalDate, goal: StudyGoal) = viewModelScope.launch {
        val s = uiState.value
        val id = com.shahaabapps.core.planner.SessionPlanner.sessionId(Dates.format(date), block.id)
        sessionPlanning.replan(s.weekStart, s.viewingRole, override = mapOf(id to goal.id))
        reminderScheduler.refresh()
    }

    /** Add a one-off extra study block (e.g. a weekend morning) to the viewed person's template. */
    fun addBlock(dayOfWeek: Int, startMinutes: Int, endMinutes: Int, kind: BlockKind) = viewModelScope.launch {
        val session = sessionStore.current() ?: return@launch
        val role = uiState.value.viewingRole
        val current = dataStore.availability.value.forRole(role)
        val block = AvailabilityBlock(
            id = "${role.name.lowercase()}-${kind.name.lowercase()}-$dayOfWeek-$startMinutes", dayOfWeek = dayOfWeek,
            startMinutes = startMinutes, endMinutes = endMinutes, kind = kind, label = if (kind == BlockKind.STUDY) "Study slot" else kind.name.lowercase(),
        )
        studyRepository.saveAvailability(session.coupleId, current.copy(blocks = current.blocks + block))
        sessionPlanning.replan(uiState.value.weekStart, role)
        celebrations.saved("Block added")
        message.value = "Block added"
    }
}
