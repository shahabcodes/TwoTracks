package com.shahaabapps.twotracks.feature.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.data.study.StudyRepository
import com.shahaabapps.core.data.study.forRole
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.CapacityReport
import com.shahaabapps.core.planner.GoalProgress
import com.shahaabapps.core.planner.ProgressCalculator
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GoalCard(
    val goal: StudyGoal,
    val progress: GoalProgress,
)

data class StudyUiState(
    val viewingRole: PersonRole = PersonRole.TECH,
    val isMe: Boolean = true,
    val active: List<GoalCard> = emptyList(),
    val inactive: List<GoalCard> = emptyList(),
    val done: List<GoalCard> = emptyList(),
    val capacity: CapacityReport? = null,
)

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    private val dataStore: CoupleDataStore,
    private val studyRepository: StudyRepository,
) : ViewModel() {

    private val viewPartner = MutableStateFlow(false)

    val uiState: StateFlow<StudyUiState> = combine(
        sessionStore.session, viewPartner, dataStore.goals, dataStore.logs, dataStore.availability,
    ) { session, vp, goals, logs, availability ->
        if (session == null) return@combine StudyUiState()
        val role = if (vp) session.partnerRole else session.myRole
        val today = Clock.today(session.profileFor(role) ?: session.me)
        val roleGoals = goals.filter { it.owner == role }.sortedWith(compareBy({ it.priority }, { it.title }))
        val progress = ProgressCalculator.computeAll(roleGoals, logs, today)
        val cards = roleGoals.map { GoalCard(it, progress.getValue(it.id)) }
        StudyUiState(
            viewingRole = role, isMe = !vp,
            active = cards.filter { it.goal.active && !it.progress.isComplete },
            inactive = cards.filter { !it.goal.active && !it.progress.isComplete },
            done = cards.filter { it.progress.isComplete },
            capacity = CapacityReport.build(availability.forRole(role).weeklyStudyMinutes, progress.values),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StudyUiState())

    fun togglePerson() { viewPartner.value = !viewPartner.value }

    fun setActive(goal: StudyGoal, active: Boolean) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        studyRepository.saveGoal(s.coupleId, goal.copy(active = active))
    }
}
