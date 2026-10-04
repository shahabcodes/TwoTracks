package com.shahaabapps.twotracks.reminders

import com.shahaabapps.core.data.study.StudyRepository
import com.shahaabapps.core.data.study.forRole
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.planner.ProgressCalculator
import com.shahaabapps.core.planner.SessionPlanner
import com.shahaabapps.twotracks.session.SessionStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Bridges the pure planner and Firestore: generates sessions for a week from
 * the availability template and current progress, and persists them.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class SessionPlanning @Inject constructor(
    private val sessionStore: SessionStore,
    private val studyRepository: StudyRepository,
) {
    fun sessionsBetween(from: LocalDate, to: LocalDate): Flow<List<PlannedSession>> =
        sessionStore.session.flatMapLatest { s ->
            if (s == null) flowOf(emptyList()) else studyRepository.observeSessions(s.coupleId, Dates.format(from), Dates.format(to))
        }

    /** Plans this week and next for the signed-in person if they have no PLANNED sessions yet. */
    suspend fun ensurePlanned(today: LocalDate) {
        val s = sessionStore.current() ?: return
        val thisWeek = Dates.weekStart(today)
        listOf(thisWeek, thisWeek.plusWeeks(1)).forEach { week ->
            val existing = studyRepository
                .observeSessions(s.coupleId, Dates.format(week), Dates.format(week.plusDays(6)))
                .first()
                .filter { it.owner == s.myRole }
            if (existing.isEmpty()) replan(week, s.myRole)
        }
    }

    /** Regenerates PLANNED sessions for [weekStart]; DONE/SKIPPED ones are kept. */
    suspend fun replan(weekStart: LocalDate, role: PersonRole, override: Map<String, String> = emptyMap()) {
        val s = sessionStore.current() ?: return
        val goals = studyRepository.observeGoals(s.coupleId).first()
        val logs = studyRepository.observeLogs(s.coupleId).first()
        val availability = studyRepository.observeAvailability(s.coupleId).first().forRole(role)
        val existing = studyRepository
            .observeSessions(s.coupleId, Dates.format(weekStart), Dates.format(weekStart.plusDays(6)))
            .first()
            .filter { it.owner == role }
        val progress = ProgressCalculator.computeAll(goals.filter { it.owner == role }, logs, LocalDate.now())
        val planned = SessionPlanner.planWeek(weekStart, availability, goals, progress, existing, override)
        if (planned.isNotEmpty()) studyRepository.saveSessions(s.coupleId, planned)
    }
}
