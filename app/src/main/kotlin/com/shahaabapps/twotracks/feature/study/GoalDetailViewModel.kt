package com.shahaabapps.twotracks.feature.study

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.data.study.StudyRepository
import com.shahaabapps.core.model.MockScore
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.model.StudyLog
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.planner.GoalProgress
import com.shahaabapps.core.planner.ProgressCalculator
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.session.StudyActions
import com.shahaabapps.twotracks.session.StudyTimer
import com.shahaabapps.twotracks.session.TimerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DayBar(val date: LocalDate, val minutes: Int)

data class GoalDetailUiState(
    val goal: StudyGoal? = null,
    val progress: GoalProgress? = null,
    val isMine: Boolean = false,
    val recentLogs: List<StudyLog> = emptyList(),
    val last14: List<DayBar> = emptyList(),
    val mockScores: List<MockScore> = emptyList(),
    val timer: TimerState? = null,
    val todayIso: String = "",
    val message: String? = null,
    val goals: List<StudyGoal> = emptyList(),
)

@HiltViewModel
class GoalDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionStore: SessionStore,
    private val dataStore: CoupleDataStore,
    private val studyRepository: StudyRepository,
    private val actions: StudyActions,
    timer: StudyTimer,
) : ViewModel() {

    private val goalId: String = savedStateHandle.get<String>("goalId").orEmpty()
    private val message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<GoalDetailUiState> = combine(
        sessionStore.session, dataStore.goals, dataStore.logs, dataStore.mockScores, combine(timer.state, message) { t, m -> t to m },
    ) { session, goals, logs, scores, (timerState, msg) ->
        val goal = goals.firstOrNull { it.id == goalId } ?: return@combine GoalDetailUiState(message = msg, goals = goals)
        val owner = session?.profileFor(goal.owner)
        val today = Clock.today(owner ?: session?.me)
        val progress = ProgressCalculator.compute(goal, logs, today)
        val goalLogs = logs.filter { it.goalId == goal.id }
        val byDay = goalLogs.groupBy { it.dateIso }.mapValues { e -> e.value.sumOf { it.minutes } }
        GoalDetailUiState(
            goal = goal, progress = progress, isMine = session?.myRole == goal.owner,
            recentLogs = goalLogs.sortedWith(compareByDescending<StudyLog> { it.dateIso }.thenByDescending { it.createdAtMillis }).take(30),
            last14 = (13L downTo 0L).map { off -> val d = today.minusDays(off); DayBar(d, byDay[Dates.format(d)] ?: 0) },
            mockScores = scores.filter { it.goalId == goal.id }.sortedByDescending { it.dateIso },
            timer = timerState, todayIso = Dates.format(today), message = msg, goals = goals,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GoalDetailUiState())

    fun log(componentId: String, minutes: Int, note: String, dateIso: String) = viewModelScope.launch {
        message.value = actions.logMinutes(goalId, componentId, minutes, dateIso, note) ?: "Logged $minutes min"
    }

    fun logSplit(minutesByComponent: Map<String, Int>, note: String, dateIso: String) = viewModelScope.launch {
        var error: String? = null
        minutesByComponent.forEach { (c, m) -> error = error ?: actions.logMinutes(goalId, c, m, dateIso, note) }
        message.value = error ?: "Logged ${minutesByComponent.values.sum()} min"
    }

    fun deleteLog(logId: String) = viewModelScope.launch { message.value = actions.deleteLog(logId) ?: "Entry removed" }

    fun startTimer(componentId: String) = actions.startTimer(goalId, componentId, null)
    fun stopTimer() = viewModelScope.launch { message.value = actions.stopTimerAndLog(uiState.value.todayIso) ?: "Timer logged" }

    fun setActive(active: Boolean) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        val goal = uiState.value.goal ?: return@launch
        studyRepository.saveGoal(s.coupleId, goal.copy(active = active))
    }

    fun addMockScore(scores: Map<String, String>, note: String) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        val goal = uiState.value.goal ?: return@launch
        studyRepository.addMockScore(s.coupleId, MockScore(owner = goal.owner, goalId = goal.id, dateIso = uiState.value.todayIso, scores = scores, note = note))
        message.value = "Mock score saved"
    }

    fun deleteMockScore(id: String) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        studyRepository.deleteMockScore(s.coupleId, id)
    }

    fun clearMessage() { message.value = null }
}
