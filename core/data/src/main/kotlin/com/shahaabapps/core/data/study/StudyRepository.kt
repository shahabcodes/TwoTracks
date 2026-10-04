package com.shahaabapps.core.data.study

import com.google.firebase.firestore.FirebaseFirestore
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.common.runCatchingResult
import com.shahaabapps.core.data.firestore.Mappers.toAvailability
import com.shahaabapps.core.data.firestore.Mappers.toGoal
import com.shahaabapps.core.data.firestore.Mappers.toLevel
import com.shahaabapps.core.data.firestore.Mappers.toMap
import com.shahaabapps.core.data.firestore.Mappers.toMockScore
import com.shahaabapps.core.data.firestore.Mappers.toSession
import com.shahaabapps.core.data.firestore.Mappers.toStudyLog
import com.shahaabapps.core.data.firestore.Paths
import com.shahaabapps.core.data.firestore.asFlow
import com.shahaabapps.core.data.firestore.sub
import com.shahaabapps.core.model.Availability
import com.shahaabapps.core.model.Level
import com.shahaabapps.core.model.MockScore
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.model.StudyLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface StudyRepository {
    fun observeGoals(coupleId: String): Flow<List<StudyGoal>>
    suspend fun saveGoal(coupleId: String, goal: StudyGoal): AppResult<Unit>
    suspend fun deleteGoal(coupleId: String, goalId: String): AppResult<Unit>

    fun observeLevels(coupleId: String): Flow<List<Level>>
    suspend fun saveLevel(coupleId: String, level: Level): AppResult<Unit>

    fun observeAvailability(coupleId: String): Flow<List<Availability>>
    suspend fun saveAvailability(coupleId: String, availability: Availability): AppResult<Unit>

    fun observeLogs(coupleId: String): Flow<List<StudyLog>>
    suspend fun addLog(coupleId: String, log: StudyLog): AppResult<String>
    suspend fun deleteLog(coupleId: String, logId: String): AppResult<Unit>

    fun observeMockScores(coupleId: String): Flow<List<MockScore>>
    suspend fun addMockScore(coupleId: String, score: MockScore): AppResult<String>
    suspend fun deleteMockScore(coupleId: String, id: String): AppResult<Unit>

    /** Sessions between the two ISO dates (inclusive). */
    fun observeSessions(coupleId: String, fromIso: String, toIso: String): Flow<List<PlannedSession>>
    suspend fun saveSessions(coupleId: String, sessions: List<PlannedSession>): AppResult<Unit>
    suspend fun setSessionStatus(coupleId: String, sessionId: String, status: SessionStatus): AppResult<Unit>
    suspend fun setSessionGoal(coupleId: String, sessionId: String, goalId: String, componentId: String): AppResult<Unit>
}

@Singleton
class FirebaseStudyRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) : StudyRepository {

    override fun observeGoals(coupleId: String): Flow<List<StudyGoal>> =
        firestore.sub(coupleId, Paths.GOALS).asFlow { it.toGoal() }

    override suspend fun saveGoal(coupleId: String, goal: StudyGoal): AppResult<Unit> = runCatchingResult {
        val ref = if (goal.id.isBlank()) firestore.sub(coupleId, Paths.GOALS).document() else firestore.sub(coupleId, Paths.GOALS).document(goal.id)
        ref.set(goal.toMap()).await()
        Unit
    }

    override suspend fun deleteGoal(coupleId: String, goalId: String): AppResult<Unit> = runCatchingResult {
        firestore.sub(coupleId, Paths.GOALS).document(goalId).delete().await()
        Unit
    }

    override fun observeLevels(coupleId: String): Flow<List<Level>> =
        firestore.sub(coupleId, Paths.LEVELS).asFlow { it.toLevel() }

    override suspend fun saveLevel(coupleId: String, level: Level): AppResult<Unit> = runCatchingResult {
        firestore.sub(coupleId, Paths.LEVELS).document(level.id).set(level.toMap()).await()
        Unit
    }

    override fun observeAvailability(coupleId: String): Flow<List<Availability>> =
        firestore.sub(coupleId, Paths.AVAILABILITY).asFlow { it.toAvailability() }

    override suspend fun saveAvailability(coupleId: String, availability: Availability): AppResult<Unit> = runCatchingResult {
        firestore.sub(coupleId, Paths.AVAILABILITY).document(availability.owner.name).set(availability.toMap()).await()
        Unit
    }

    override fun observeLogs(coupleId: String): Flow<List<StudyLog>> =
        firestore.sub(coupleId, Paths.STUDY_LOGS).asFlow { it.toStudyLog() }

    override suspend fun addLog(coupleId: String, log: StudyLog): AppResult<String> = runCatchingResult {
        val ref = firestore.sub(coupleId, Paths.STUDY_LOGS).document()
        ref.set(log.copy(id = ref.id, createdAtMillis = System.currentTimeMillis()).toMap()).await()
        ref.id
    }

    override suspend fun deleteLog(coupleId: String, logId: String): AppResult<Unit> = runCatchingResult {
        firestore.sub(coupleId, Paths.STUDY_LOGS).document(logId).delete().await()
        Unit
    }

    override fun observeMockScores(coupleId: String): Flow<List<MockScore>> =
        firestore.sub(coupleId, Paths.MOCK_SCORES).asFlow { it.toMockScore() }

    override suspend fun addMockScore(coupleId: String, score: MockScore): AppResult<String> = runCatchingResult {
        val ref = firestore.sub(coupleId, Paths.MOCK_SCORES).document()
        ref.set(score.copy(id = ref.id).toMap()).await()
        ref.id
    }

    override suspend fun deleteMockScore(coupleId: String, id: String): AppResult<Unit> = runCatchingResult {
        firestore.sub(coupleId, Paths.MOCK_SCORES).document(id).delete().await()
        Unit
    }

    override fun observeSessions(coupleId: String, fromIso: String, toIso: String): Flow<List<PlannedSession>> =
        firestore.sub(coupleId, Paths.SESSIONS)
            .whereGreaterThanOrEqualTo("dateIso", fromIso)
            .whereLessThanOrEqualTo("dateIso", toIso)
            .asFlow { it.toSession() }

    override suspend fun saveSessions(coupleId: String, sessions: List<PlannedSession>): AppResult<Unit> = runCatchingResult {
        sessions.chunked(400).forEach { chunk ->
            val batch = firestore.batch()
            chunk.forEach { s -> batch.set(firestore.sub(coupleId, Paths.SESSIONS).document(s.id), s.toMap()) }
            batch.commit().await()
        }
        Unit
    }

    override suspend fun setSessionStatus(coupleId: String, sessionId: String, status: SessionStatus): AppResult<Unit> = runCatchingResult {
        firestore.sub(coupleId, Paths.SESSIONS).document(sessionId).update("status", status.name).await()
        Unit
    }

    override suspend fun setSessionGoal(coupleId: String, sessionId: String, goalId: String, componentId: String): AppResult<Unit> = runCatchingResult {
        firestore.sub(coupleId, Paths.SESSIONS).document(sessionId).update(mapOf("goalId" to goalId, "componentId" to componentId)).await()
        Unit
    }
}

/** Convenience for callers that only know a role. */
fun List<Availability>.forRole(role: PersonRole): Availability =
    firstOrNull { it.owner == role } ?: Availability(owner = role)
