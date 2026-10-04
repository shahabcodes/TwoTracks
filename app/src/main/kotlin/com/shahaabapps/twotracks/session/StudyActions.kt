package com.shahaabapps.twotracks.session

import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.data.study.StudyRepository
import com.shahaabapps.core.model.Level
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.model.StudyLog
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

    suspend fun saveLevel(level: Level): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        return (studyRepository.saveLevel(s.coupleId, level) as? AppResult.Failure)?.error?.message
    }

    fun todayIso(): String = Clock.todayIso(sessionStore.current()?.me)
}
