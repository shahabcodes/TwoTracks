package com.shahaabapps.twotracks.session

import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.data.study.StudyRepository
import com.shahaabapps.core.model.Availability
import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.Level
import com.shahaabapps.core.model.LevelItem
import com.shahaabapps.core.model.LevelItemKind
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.model.StudyLog
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.twotracks.reminders.SessionPlanning
import com.shahaabapps.twotracks.reminders.ReminderScheduler
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/** Write-side helpers shared by Today, Level and Goal screens. */
@Singleton
class StudyActions @Inject constructor(
    private val sessionStore: SessionStore,
    private val studyRepository: StudyRepository,
    private val reminderScheduler: ReminderScheduler,
    private val sessionPlanning: SessionPlanning,
) {
    /**
     * Logs minutes for a component on a date. When [sessionId] is given the
     * planned session is marked DONE too. Returns an error message or null.
     */
    suspend fun logMinutes(
        goalId: String,
        componentId: String,
        minutes: Int,
        dateIso: String,
        note: String = "",
        sessionId: String? = null,
        fromClass: Boolean = false,
    ): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        if (minutes <= 0) return "Enter some minutes"
        val log = StudyLog(
            owner = s.myRole, dateIso = dateIso, goalId = goalId, componentId = componentId,
            minutes = minutes, note = note, fromClass = fromClass,
        )
        when (val r = studyRepository.addLog(s.coupleId, log)) {
            is AppResult.Failure -> return r.error.message ?: "Could not save"
            is AppResult.Success -> Unit
        }
        if (sessionId != null) studyRepository.setSessionStatus(s.coupleId, sessionId, SessionStatus.DONE)
        reminderScheduler.refresh()
        return null
    }

    suspend fun skipSession(sessionId: String): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        return (studyRepository.setSessionStatus(s.coupleId, sessionId, SessionStatus.SKIPPED) as? AppResult.Failure)?.error?.message
    }

    suspend fun reopenSession(sessionId: String): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        return (studyRepository.setSessionStatus(s.coupleId, sessionId, SessionStatus.PLANNED) as? AppResult.Failure)?.error?.message
    }

    /** Re-adds a deleted log (Undo). The entry gets a fresh id. */
    suspend fun restoreLog(log: StudyLog): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        val r = studyRepository.addLog(s.coupleId, log.copy(id = ""))
        return (r as? AppResult.Failure)?.error?.message
    }

    suspend fun deleteLog(logId: String): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        return (studyRepository.deleteLog(s.coupleId, logId) as? AppResult.Failure)?.error?.message
    }

    /** Marks a goal achieved (exam passed, level reached) or reopens it. */
    suspend fun setGoalPassed(goalId: String, passed: Boolean): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        val goal = studyRepository.observeGoals(s.coupleId).first().firstOrNull { it.id == goalId } ?: return "Goal not found"
        val today = Clock.todayIso(s.me)
        val r = studyRepository.saveGoal(s.coupleId, goal.copy(passed = passed, passedAtIso = if (passed) today else null))
        reminderScheduler.refresh()
        return (r as? AppResult.Failure)?.error?.message
    }

    suspend fun setTaskDone(level: Level, itemId: String, done: Boolean): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        val today = Clock.todayIso(s.me)
        val updated = level.copy(items = level.items.map { if (it.id == itemId) it.copy(done = done, doneAtIso = if (done) today else null) else it })
        return (studyRepository.saveLevel(s.coupleId, updated) as? AppResult.Failure)?.error?.message
    }

    suspend fun startLevelEarly(level: Level): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        val r = studyRepository.saveLevel(s.coupleId, level.copy(startedEarly = true))
        reminderScheduler.refresh()
        return (r as? AppResult.Failure)?.error?.message
    }

    suspend fun chooseBranch(level: Level, branch: String): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        return (studyRepository.saveLevel(s.coupleId, level.copy(branch = branch)) as? AppResult.Failure)?.error?.message
    }

    /**
     * Pushes a level's due date to [newDueIso]. Goals and items in that level
     * that were due on the old date move with it; every later level of the
     * same person, and the goals tied to those levels, shift by [shiftDays].
     */
    suspend fun extendLevel(levelId: String, newDueIso: String, shiftDays: Long): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        val levels = studyRepository.observeLevels(s.coupleId).first()
        val level = levels.firstOrNull { it.id == levelId } ?: return "Level not found"
        val goals = studyRepository.observeGoals(s.coupleId).first().associateBy { it.id }
        val oldDue = level.dueDateIso
        fun shift(iso: String?): String? = Dates.parseOrNull(iso)?.plusDays(shiftDays)?.let { Dates.format(it) }

        val touchedGoals = mutableMapOf<String, StudyGoal>()
        val updatedThis = level.copy(
            dueDateIso = newDueIso,
            items = level.items.map { item ->
                val goal = item.goalId?.let { goals[it] }
                if (goal != null && (goal.targetDateIso == null || goal.targetDateIso == oldDue || (goal.targetDateIso ?: "") > newDueIso)) {
                    touchedGoals[goal.id] = goal.copy(targetDateIso = newDueIso)
                }
                if (item.dueDateIso == oldDue) item.copy(dueDateIso = newDueIso) else item
            },
        )
        val later = levels.filter { it.owner == level.owner && it.number > level.number }.map { lvl ->
            lvl.copy(
                startDateIso = shift(lvl.startDateIso) ?: lvl.startDateIso,
                dueDateIso = shift(lvl.dueDateIso) ?: lvl.dueDateIso,
                items = lvl.items.map { item ->
                    val goal = item.goalId?.let { goals[it] }
                    if (goal != null && goal.targetDateIso != null && goal.id !in touchedGoals) {
                        touchedGoals[goal.id] = goal.copy(targetDateIso = shift(goal.targetDateIso))
                    }
                    item.copy(dueDateIso = shift(item.dueDateIso))
                },
            )
        }
        for (lvl in listOf(updatedThis) + later) {
            (studyRepository.saveLevel(s.coupleId, lvl) as? AppResult.Failure)?.let { return it.error.message }
        }
        for (g in touchedGoals.values) {
            (studyRepository.saveGoal(s.coupleId, g) as? AppResult.Failure)?.let { return it.error.message }
        }
        replanFromToday()
        return null
    }

    /** Replaces study blocks in my weekly template by id and replans this week and next. */
    suspend fun lengthenStudyBlocks(newBlocks: List<AvailabilityBlock>): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        val byId = newBlocks.associateBy { it.id }
        val mine = studyRepository.observeAvailability(s.coupleId).first().firstOrNull { it.owner == s.myRole }
            ?: Availability(owner = s.myRole)
        val updated = mine.copy(blocks = mine.blocks.map { byId[it.id] ?: it })
        (studyRepository.saveAvailability(s.coupleId, updated) as? AppResult.Failure)?.let { return it.error.message }
        replanFromToday()
        return null
    }

    private suspend fun replanFromToday() {
        val s = sessionStore.current() ?: return
        val week = Dates.weekStart(Clock.today(s.me))
        sessionPlanning.replan(week, s.myRole)
        sessionPlanning.replan(week.plusWeeks(1), s.myRole)
        reminderScheduler.refresh()
    }

    /** Appends a GOAL item for [goalId] to the level (used after creating a goal from a level page). */
    suspend fun addGoalItem(levelId: String, goalId: String, title: String, required: Boolean, dueDateIso: String?): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        val level = studyRepository.observeLevels(s.coupleId).first().firstOrNull { it.id == levelId } ?: return "Level not found"
        if (level.items.any { it.goalId == goalId }) return null
        val item = LevelItem(id = "custom-goal-$goalId", kind = LevelItemKind.GOAL, title = title, goalId = goalId, required = required, dueDateIso = dueDateIso)
        val r = studyRepository.saveLevel(s.coupleId, level.copy(items = level.items + item))
        reminderScheduler.refresh()
        return (r as? AppResult.Failure)?.error?.message
    }

    /** Deletes a goal and removes every level item that pointed at it. */
    suspend fun deleteGoal(goalId: String): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        studyRepository.observeLevels(s.coupleId).first()
            .filter { lvl -> lvl.items.any { it.goalId == goalId } }
            .forEach { lvl -> studyRepository.saveLevel(s.coupleId, lvl.copy(items = lvl.items.filterNot { it.goalId == goalId })) }
        val r = studyRepository.deleteGoal(s.coupleId, goalId)
        reminderScheduler.refresh()
        return (r as? AppResult.Failure)?.error?.message
    }

    suspend fun saveLevel(level: Level): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        return (studyRepository.saveLevel(s.coupleId, level) as? AppResult.Failure)?.error?.message
    }

    fun todayIso(): String = Clock.todayIso(sessionStore.current()?.me)
}
