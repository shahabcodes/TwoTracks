package com.shahaabapps.twotracks.feature.study

import com.shahaabapps.twotracks.session.Celebrations
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.data.study.StudyRepository
import com.shahaabapps.core.model.GoalComponent
import com.shahaabapps.core.model.GoalKind
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.FitPlanner
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.PlanStore
import com.shahaabapps.twotracks.session.StudyActions
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
    /** Set when creating from a level page: the goal becomes an item of that level. */
    val levelId: String? = null,
    val requiredForLevel: Boolean = true,
    /** Set when the save would overload the level: the sheet with the two ways out. */
    val fit: FitCheck? = null,
) {
    val canSave: Boolean
        get() = title.isNotBlank() && !saving && components.isNotEmpty() &&
            components.all { it.name.isNotBlank() && (it.hours.toDoubleOrNull() ?: 0.0) > 0.0 } &&
            (targetDateIso.isBlank() || Dates.parseOrNull(targetDateIso) != null)
}

/** What the sheet shows: the computed result plus the level it was checked against. */
data class FitCheck(
    val result: FitPlanner.FitResult,
    val levelId: String,
    val levelNumber: Int,
    val levelName: String,
    val levelDueIso: String,
    val addedHours: Double,
)

enum class FitChoice { EXTEND, MORE_HOURS, SAVE_ANYWAY }

@HiltViewModel
class GoalEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionStore: SessionStore,
    private val dataStore: CoupleDataStore,
    private val studyRepository: StudyRepository,
    private val actions: StudyActions,
    private val planStore: PlanStore,
    private val celebrations: Celebrations,
) : ViewModel() {

    private val goalId: String? = savedStateHandle.get<String>("goalId")?.takeIf { it.isNotBlank() && it != "new" }
    private val levelId: String? = savedStateHandle.get<String>("levelId")?.takeIf { it.isNotBlank() }
    private var original: StudyGoal? = null

    private val _uiState = MutableStateFlow(GoalEditUiState(isNew = goalId == null, levelId = levelId))
    val uiState: StateFlow<GoalEditUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if (goalId == null) {
                // A goal made from a level page is due when the level is, unless the user says otherwise.
                val levelDue = levelId?.let { id -> planStore.plan.first { it != null }?.me?.levels?.firstOrNull { it.level.id == id }?.level?.dueDateIso }
                _uiState.update {
                    it.copy(loaded = true, components = listOf(ComponentDraft("c1", "", "", "")), targetDateIso = levelDue.orEmpty())
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
    fun onRequiredForLevel(v: Boolean) = _uiState.update { it.copy(requiredForLevel = v) }

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

    private fun buildGoal(s: GoalEditUiState, owner: com.shahaabapps.core.model.PersonRole): StudyGoal {
        val base = original ?: StudyGoal(id = "", owner = owner)
        return base.copy(
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
    }

    /**
     * Runs the fit check first. If the hours still fit the level by its due
     * date the goal is saved straight away; otherwise the sheet is shown and
     * [choose] finishes the save.
     */
    fun save(onDone: () -> Unit) {
        val s = _uiState.value
        val session = sessionStore.current() ?: return
        if (!s.canSave) return
        _uiState.update { it.copy(saving = true, error = null) }
        viewModelScope.launch {
            val goal = buildGoal(s, session.myRole)
            val check = if (s.levelId == null && original == null) null else fitCheck(goal, s)
            if (check == null || check.result.fits) {
                persist(goal, s, onDone)
            } else {
                _uiState.update { it.copy(saving = false, fit = check) }
            }
        }
    }

    fun dismissFit() = _uiState.update { it.copy(fit = null) }

    fun choose(choice: FitChoice, onDone: () -> Unit) {
        val s = _uiState.value
        val check = s.fit ?: return
        val session = sessionStore.current() ?: return
        _uiState.update { it.copy(saving = true, fit = null) }
        viewModelScope.launch {
            val goal = buildGoal(s, session.myRole)
            val err = when (choice) {
                FitChoice.EXTEND -> check.result.extend?.let { actions.extendLevel(check.levelId, Dates.format(it.newDueDate), it.shiftDays) }
                FitChoice.MORE_HOURS -> check.result.moreHours?.let { actions.lengthenStudyBlocks(it.newBlocks) }
                FitChoice.SAVE_ANYWAY -> null
            }
            if (err != null) { _uiState.update { it.copy(saving = false, error = err) }; return@launch }
            // When the level moved, a goal due on the old date follows it.
            val ext = check.result.extend
            val adjusted = if (choice == FitChoice.EXTEND && ext != null) {
                val newDue = Dates.format(ext.newDueDate)
                val target = goal.targetDateIso
                if (target == null || target == check.levelDueIso || target > newDue) goal.copy(targetDateIso = newDue) else goal
            } else goal
            persist(adjusted, s, onDone)
        }
    }

    private suspend fun persist(goal: StudyGoal, s: GoalEditUiState, onDone: () -> Unit) {
        val session = sessionStore.current() ?: return
        val newId = if (goal.id.isBlank()) "custom-" + System.currentTimeMillis() else goal.id
        when (val r = studyRepository.saveGoal(session.coupleId, goal.copy(id = newId))) {
            is AppResult.Success -> {
                val lvl = s.levelId
                if (lvl != null && original == null) actions.addGoalItem(lvl, newId, goal.title, s.requiredForLevel, goal.targetDateIso)
                _uiState.update { it.copy(saving = false) }
                celebrations.saved(if (original == null) "${goal.title} added" else "${goal.title} saved")
                onDone()
            }
            is AppResult.Failure -> _uiState.update { it.copy(saving = false, error = r.error.message ?: "Could not save") }
        }
    }

    /**
     * Compares the level's remaining required hours (with this goal's new
     * budget) against the study template until the level's due date. Null
     * when the goal is optional or belongs to no level.
     */
    private suspend fun fitCheck(goal: StudyGoal, s: GoalEditUiState): FitCheck? {
        val plan = planStore.plan.first { it != null }?.me ?: return null
        val level = when {
            s.levelId != null -> plan.levels.firstOrNull { it.level.id == s.levelId }
            else -> plan.levels.firstOrNull { lp -> lp.items.any { it.item.goalId == goal.id && it.item.required } }
        } ?: return null
        if (s.levelId != null && original == null && !s.requiredForLevel) return null
        val levelDue = level.dueDate ?: return null
        val others = level.items
            .filter { it.item.required && it.item.goalId != null && it.item.goalId != goal.id }
            .mapNotNull { plan.goalProgress[it.item.goalId!!] }
            .filter { !it.goal.passed }
            .sumOf { it.remainingHours }
        val loggedByComponent = plan.goalProgress[goal.id]?.components?.associate { it.component.id to it.hoursLogged }.orEmpty()
        val goalRemaining = goal.components.sumOf { c -> (c.hoursRequired - (loggedByComponent[c.id] ?: 0.0)).coerceAtLeast(0.0) }
        val previousRemaining = plan.goalProgress[goal.id]?.remainingHours ?: 0.0
        val blocks = dataStore.availability.value.firstOrNull { it.owner == plan.role }?.blocks.orEmpty()
        val result = FitPlanner.check(plan.today, levelDue, others, goalRemaining, blocks)
        return FitCheck(result, level.level.id, level.level.number, level.level.name, level.level.dueDateIso, addedHours = goalRemaining - previousRemaining)
    }

    fun delete(onDone: () -> Unit) {
        val session = sessionStore.current() ?: return
        val id = goalId ?: return
        viewModelScope.launch {
            actions.deleteGoal(id)
            onDone()
        }
    }
}
