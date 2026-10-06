package com.shahaabapps.twotracks.feature.study

import com.shahaabapps.core.content.Courses
import com.shahaabapps.twotracks.session.CourseBridge
import com.shahaabapps.twotracks.session.Celebrations
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
    val courseId: String? = null,
    val modules: List<CourseBridge.ModuleStatus> = emptyList(),
    val goal: StudyGoal? = null,
    val progress: GoalProgress? = null,
    val isMine: Boolean = false,
    val recentLogs: List<StudyLog> = emptyList(),
    val last14: List<DayBar> = emptyList(),
    val mockScores: List<MockScore> = emptyList(),
    val todayIso: String = "",
    val message: String? = null,
    val goals: List<StudyGoal> = emptyList(),
    /** Plain sentence for the top of the page. */
    val sentence: String = "",
    val sentenceGood: Boolean = true,
)

@HiltViewModel
class GoalDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionStore: SessionStore,
    private val dataStore: CoupleDataStore,
    private val studyRepository: StudyRepository,
    private val actions: StudyActions,
    private val celebrations: Celebrations,
) : ViewModel() {

    private val goalId: String = savedStateHandle.get<String>("goalId").orEmpty()
    private val message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<GoalDetailUiState> = combine(
        sessionStore.session, dataStore.goals, dataStore.logs, dataStore.mockScores,
        combine(message, dataStore.topicProgress, dataStore.quizAttempts) { m, tp, qa -> Triple(m, tp, qa) },
    ) { session, goals, logs, scores, (msg, topicProgress, quizAttempts) ->
        val goal = goals.firstOrNull { it.id == goalId } ?: return@combine GoalDetailUiState(message = msg, goals = goals)
        val owner = session?.profileFor(goal.owner)
        val today = Clock.today(owner ?: session?.me)
        val course = Courses.all.firstOrNull { it.goalId == goal.id }
        val modules = course?.let { CourseBridge.moduleStatuses(it, goal.owner, topicProgress, quizAttempts) }.orEmpty()
        val progress = ProgressCalculator.compute(goal, logs, today, CourseBridge.statesFor(goal.owner, topicProgress, quizAttempts)[goal.id] ?: com.shahaabapps.core.planner.CourseState.NONE)
        val goalLogs = logs.filter { it.goalId == goal.id }
        val byDay = goalLogs.groupBy { it.dateIso }.mapValues { e -> e.value.sumOf { it.minutes } }
        val (sentence, good) = sentenceFor(goal, progress)
        GoalDetailUiState(
            courseId = course?.id, modules = modules,
            goal = goal, progress = progress, isMine = session?.myRole == goal.owner,
            recentLogs = goalLogs.sortedWith(compareByDescending<StudyLog> { it.dateIso }.thenByDescending { it.createdAtMillis }).take(30),
            last14 = (13L downTo 0L).map { off -> val d = today.minusDays(off); DayBar(d, byDay[Dates.format(d)] ?: 0) },
            mockScores = scores.filter { it.goalId == goal.id }.sortedByDescending { it.dateIso },
            todayIso = Dates.format(today), message = msg, goals = goals, sentence = sentence, sentenceGood = good,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GoalDetailUiState())

    private fun sentenceFor(goal: StudyGoal, p: GoalProgress): Pair<String, Boolean> {
        if (goal.passed) return "Passed on ${Clock.formatDate(goal.passedAtIso)}." to true
        val due = p.targetDate?.let { Clock.formatDate(it) }
        return when (p.feasibility) {
            com.shahaabapps.core.planner.Feasibility.DONE -> "All the hours are in. Mark it passed when the result is in." to true
            com.shahaabapps.core.planner.Feasibility.FITS -> "On track${due?.let { " for $it" } ?: ""}." to true
            com.shahaabapps.core.planner.Feasibility.TIGHT -> "Nearly on track. A little more each week keeps $due." to true
            com.shahaabapps.core.planner.Feasibility.DOES_NOT_FIT -> {
                val forecast = p.forecastDate?.let { Clock.formatDate(it) }
                val need = p.weeklyNeedHours?.let { "About %.0f hours a week gets it done by $due.".format(it) } ?: ""
                if (p.hoursLogged < 3.0) "Early days. $need".trim() to true
                else if (forecast != null) "At this pace it finishes $forecast, not $due. $need".trim() to false
                else "Nothing logged yet. The first session is the hardest." to false
            }
            com.shahaabapps.core.planner.Feasibility.NO_TARGET -> "No date set. Add one in Edit to get a pace." to true
        }
    }

    fun log(componentId: String, minutes: Int, note: String, dateIso: String) = viewModelScope.launch {
        message.value = actions.logMinutes(goalId, componentId, minutes, dateIso, note) ?: "Logged $minutes min"
    }

    fun logSplit(minutesByComponent: Map<String, Int>, note: String, dateIso: String) = viewModelScope.launch {
        var error: String? = null
        minutesByComponent.forEach { (c, m) -> error = error ?: actions.logMinutes(goalId, c, m, dateIso, note) }
        message.value = error ?: "Logged ${minutesByComponent.values.sum()} min"
    }

    /** The most recently swiped-away entry, offered back through Undo. */
    val undoLog = MutableStateFlow<StudyLog?>(null)
    val undoMock = MutableStateFlow<MockScore?>(null)

    fun deleteLog(log: StudyLog) = viewModelScope.launch {
        val error = actions.deleteLog(log.id)
        if (error == null) undoLog.value = log else message.value = error
    }

    fun undoDeleteLog() = viewModelScope.launch {
        val log = undoLog.value ?: return@launch
        undoLog.value = null
        message.value = actions.restoreLog(log) ?: "Entry restored"
    }

    fun clearUndo() { undoLog.value = null; undoMock.value = null }

    fun undoDeleteMock() = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        val m = undoMock.value ?: return@launch
        undoMock.value = null
        studyRepository.addMockScore(s.coupleId, m.copy(id = ""))
        celebrations.saved("Score restored")
        message.value = "Mock score restored"
    }

    fun setPassed(passed: Boolean) = viewModelScope.launch {
        message.value = actions.setGoalPassed(goalId, passed) ?: if (passed) "Marked as passed" else "Reopened"
    }

    fun addMockScore(scores: Map<String, String>, note: String) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        val goal = uiState.value.goal ?: return@launch
        studyRepository.addMockScore(s.coupleId, MockScore(owner = goal.owner, goalId = goal.id, dateIso = uiState.value.todayIso, scores = scores, note = note))
        celebrations.saved("Score saved")
        message.value = "Mock score saved"
    }

    fun deleteMockScore(m: MockScore) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        studyRepository.deleteMockScore(s.coupleId, m.id)
        celebrations.removed("Score removed")
        undoMock.value = m
    }

    fun clearMessage() { message.value = null }
}
