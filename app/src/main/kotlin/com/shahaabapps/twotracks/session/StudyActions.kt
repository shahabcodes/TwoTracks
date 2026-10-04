package com.shahaabapps.twotracks.session

import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.data.study.StudyRepository
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.model.StudyLog
import com.shahaabapps.twotracks.reminders.ReminderScheduler
import javax.inject.Inject
import javax.inject.Singleton

/** Write-side helpers shared by Today, Week and Study screens. */
@Singleton
class StudyActions @Inject constructor(
    private val sessionStore: SessionStore,
    private val studyRepository: StudyRepository,
    private val timer: StudyTimer,
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

    suspend fun deleteLog(logId: String): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        return (studyRepository.deleteLog(s.coupleId, logId) as? AppResult.Failure)?.error?.message
    }

    fun startTimer(goalId: String, componentId: String, sessionId: String?) = timer.start(goalId, componentId, sessionId)

    /** Stops the timer and logs the elapsed time (minimum 1 minute) on today's date. */
    suspend fun stopTimerAndLog(todayIso: String): String? {
        val t = timer.stop() ?: return null
        val minutes = t.elapsedMinutes().coerceAtLeast(1)
        return logMinutes(t.goalId, t.componentId, minutes, todayIso, sessionId = t.sessionId)
    }

    fun discardTimer() {
        timer.stop()
    }
}
