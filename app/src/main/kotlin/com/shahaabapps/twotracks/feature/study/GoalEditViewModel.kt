package com.shahaabapps.twotracks.feature.study

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.data.study.StudyRepository
import com.shahaabapps.core.model.GoalComponent
import com.shahaabapps.core.model.GoalKind
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

data class ComponentDraft(val id: String, val name: String, val hours: String, val targetScore: String)

data class GoalEditUiState(
    val isNew: Boolean = true,
    val loaded: Boolean = false,
    val title: String = "",
    val subtitle: String = "",
    val why: String = "",
    val kind: GoalKind = GoalKind.CUSTOM,
    val priority: String = "50",
    val targetDateIso: String = "",
    val active: Boolean = true,
    val components: List<ComponentDraft> = emptyList(),
    val saving: Boolean = false,
    val error: String? = null,
    val canDelete: Boolean = false,
) {
    val canSave: Boolean
        get() = title.isNotBlank() && !saving && components.isNotEmpty() &&
            components.all { it.name.isNotBlank() && (it.hours.toDoubleOrNull() ?: 0.0) > 0.0 } &&
            (targetDateIso.isBlank() || Dates.parseOrNull(targetDateIso) != null)
}

@HiltViewModel
class GoalEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionStore: SessionStore,
    private val dataStore: CoupleDataStore,
    private val studyRepository: StudyRepository,
) : ViewModel() {

    private val goalId: String? = savedStateHandle.get<String>("goalId")?.takeIf { it.isNotBlank() && it != "new" }
    private var original: StudyGoal? = null

    private val _uiState = MutableStateFlow(GoalEditUiState(isNew = goalId == null))
    val uiState: StateFlow<GoalEditUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if (goalId == null) {
                _uiState.update {
                    it.copy(loaded = true, components = listOf(ComponentDraft("c1", "", "", "")))
                }
            } else {
                val goal = dataStore.goals.first { list -> list.any { it.id == goalId } }.first { it.id == goalId }
                original = goal
                _uiState.update {
                    it.copy(
                        loaded = true, title = goal.title, subtitle = goal.subtitle, why = goal.why, kind = goal.kind,
                        priority = goal.priority.toString(), targetDateIso = goal.targetDateIso.orEmpty(), active = goal.active,
                        components = goal.components.map { c -> ComponentDraft(c.id, c.name, trimHours(c.hoursRequired), c.targetScore.orEmpty()) },
                        canDelete = true,
                    )
                }
            }
        }
    }

    private fun trimHours(h: Double) = if (h % 1.0 == 0.0) h.toInt().toString() else h.toString()

    fun onTitle(v: String) = _uiState.update { it.copy(title = v) }
    fun onSubtitle(v: String) = _uiState.update { it.copy(subtitle = v) }
    fun onWhy(v: String) = _uiState.update { it.copy(why = v) }
    fun onKind(v: GoalKind) = _uiState.update { it.copy(kind = v) }
    fun onPriority(v: String) = _uiState.update { it.copy(priority = v.filter { c -> c.isDigit() }.take(3)) }
    fun onTargetDate(v: String) = _uiState.update { it.copy(targetDateIso = v.trim()) }
    fun onActive(v: Boolean) = _uiState.update { it.copy(active = v) }

    fun onComponent(index: Int, name: String? = null, hours: String? = null, target: String? = null) = _uiState.update { s ->
        s.copy(components = s.components.mapIndexed { i, c ->
            if (i != index) c else c.copy(name = name ?: c.name, hours = hours ?: c.hours, targetScore = target ?: c.targetScore)
        })
    }

    fun addComponent() = _uiState.update { s ->
        s.copy(components = s.components + ComponentDraft("c${System.currentTimeMillis()}", "", "", ""))
    }

    fun removeComponent(index: Int) = _uiState.update { s -> s.copy(components = s.components.filterIndexed { i, _ -> i != index }) }

    fun applyPreset(names: List<String>, hoursEach: Int) = _uiState.update { s ->
        s.copy(components = names.map { n -> ComponentDraft(n.lowercase(Locale.ENGLISH).replace(' ', '-'), n, hoursEach.toString(), "") })
    }

    fun save(onDone: () -> Unit) {
        val s = _uiState.value
        val session = sessionStore.current() ?: return
        if (!s.canSave) return
        _uiState.update { it.copy(saving = true, error = null) }
        viewModelScope.launch {
            val base = original ?: StudyGoal(id = "", owner = session.myRole)
            val goal = base.copy(
                title = s.title.trim(), subtitle = s.subtitle.trim(), why = s.why.trim(), kind = s.kind,
                priority = s.priority.toIntOrNull() ?: 50, targetDateIso = s.targetDateIso.ifBlank { null }, active = s.active,
                components = s.components.map { c ->
                    GoalComponent(
                        id = c.id.ifBlank { c.name.lowercase(Locale.ENGLISH).replace(' ', '-') }, name = c.name.trim(),
                        hoursRequired = c.hours.toDoubleOrNull() ?: 0.0, targetScore = c.targetScore.ifBlank { null },
                    )
                },
                defaultHours = false,
            )
            when (val r = studyRepository.saveGoal(session.coupleId, goal)) {
                is AppResult.Success -> { _uiState.update { it.copy(saving = false) }; onDone() }
                is AppResult.Failure -> _uiState.update { it.copy(saving = false, error = r.error.message ?: "Could not save") }
            }
        }
    }

    fun delete(onDone: () -> Unit) {
        val session = sessionStore.current() ?: return
        val id = goalId ?: return
        viewModelScope.launch {
            studyRepository.deleteGoal(session.coupleId, id)
            onDone()
        }
    }
}
