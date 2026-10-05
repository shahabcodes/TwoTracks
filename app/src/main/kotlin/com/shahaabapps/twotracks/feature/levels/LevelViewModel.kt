package com.shahaabapps.twotracks.feature.levels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.model.LevelItem
import com.shahaabapps.core.model.LevelItemKind
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.core.planner.LevelState
import com.shahaabapps.twotracks.session.PlanStore
import com.shahaabapps.twotracks.session.StudyActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LevelUiState(
    val progress: LevelProgress? = null,
    val isMine: Boolean = false,
    val ownerName: String = "",
    val goalsById: Map<String, StudyGoal> = emptyMap(),
    val next: LevelProgress? = null,
    val message: String? = null,
)

@HiltViewModel
class LevelViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    planStore: PlanStore,
    private val actions: StudyActions,
) : ViewModel() {
    private val levelId: String = savedStateHandle.get<String>("levelId").orEmpty()
    private val message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<LevelUiState> = combine(planStore.plan, message) { plan, msg ->
        if (plan == null) return@combine LevelUiState(message = msg)
        val person = listOf(plan.me, plan.partner).firstOrNull { p -> p.levels.any { it.level.id == levelId } }
            ?: return@combine LevelUiState(message = msg)
        val progress = person.levels.first { it.level.id == levelId }
        LevelUiState(
            progress = progress, isMine = person.role == plan.me.role, ownerName = person.name,
            goalsById = person.goals.associateBy { it.id },
            next = person.levels.firstOrNull { it.level.number > progress.level.number },
            message = msg,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LevelUiState())

    fun toggleTask(item: LevelItem) = viewModelScope.launch {
        val level = uiState.value.progress?.level ?: return@launch
        if (item.kind != LevelItemKind.TASK) return@launch
        message.value = actions.setTaskDone(level, item.id, !item.done)
    }

    fun setPassed(goalId: String, passed: Boolean) = viewModelScope.launch {
        message.value = actions.setGoalPassed(goalId, passed) ?: if (passed) "Marked as passed" else "Reopened"
    }

    fun startNextEarly() = viewModelScope.launch {
        val next = uiState.value.next?.level ?: return@launch
        if (uiState.value.next?.state != LevelState.LOCKED) return@launch
        message.value = actions.startLevelEarly(next) ?: "Level ${next.number} started"
    }

    fun chooseBranch(branch: String) = viewModelScope.launch {
        val level = uiState.value.progress?.level ?: return@launch
        message.value = actions.chooseBranch(level, branch)
    }

    fun addTask(title: String, detail: String, required: Boolean) = viewModelScope.launch {
        val level = uiState.value.progress?.level ?: return@launch
        val item = LevelItem(id = "custom-${System.currentTimeMillis()}", kind = LevelItemKind.TASK, title = title.trim(), detail = detail.trim(), required = required)
        message.value = actions.saveLevel(level.copy(items = level.items + item)) ?: "Added to Level ${level.number}"
    }

    /** Removes an item; a custom goal behind it is deleted too, seeded goals are only unlinked. */
    fun removeItem(item: LevelItem) = viewModelScope.launch {
        val level = uiState.value.progress?.level ?: return@launch
        val goalId = item.goalId
        message.value = if (goalId != null && goalId.startsWith("custom-")) {
            actions.deleteGoal(goalId) ?: "Removed"
        } else {
            actions.saveLevel(level.copy(items = level.items.filterNot { it.id == item.id })) ?: "Removed from the level"
        }
    }

    fun clearMessage() { message.value = null }
}
