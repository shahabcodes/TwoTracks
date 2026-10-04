package com.shahaabapps.core.data.tracking

import com.google.firebase.firestore.FirebaseFirestore
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.common.runCatchingResult
import com.shahaabapps.core.data.firestore.Mappers.toBudget
import com.shahaabapps.core.data.firestore.Mappers.toCountry
import com.shahaabapps.core.data.firestore.Mappers.toDeadline
import com.shahaabapps.core.data.firestore.Mappers.toDocument
import com.shahaabapps.core.data.firestore.Mappers.toHabit
import com.shahaabapps.core.data.firestore.Mappers.toHabitLog
import com.shahaabapps.core.data.firestore.Mappers.toMap
import com.shahaabapps.core.data.firestore.Mappers.toMilestone
import com.shahaabapps.core.data.firestore.Mappers.toTest
import com.shahaabapps.core.data.firestore.Paths
import com.shahaabapps.core.data.firestore.asFlow
import com.shahaabapps.core.data.firestore.sub
import com.shahaabapps.core.model.BudgetItem
import com.shahaabapps.core.model.CountryTrack
import com.shahaabapps.core.model.Deadline
import com.shahaabapps.core.model.DocumentItem
import com.shahaabapps.core.model.Habit
import com.shahaabapps.core.model.HabitLog
import com.shahaabapps.core.model.Milestone
import com.shahaabapps.core.model.TestRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface TrackingRepository {
    fun observeHabits(coupleId: String): Flow<List<Habit>>
    fun observeHabitLogs(coupleId: String): Flow<List<HabitLog>>
    suspend fun saveHabit(coupleId: String, habit: Habit): AppResult<Unit>
    suspend fun addHabitLog(coupleId: String, log: HabitLog): AppResult<String>
    suspend fun deleteHabitLog(coupleId: String, id: String): AppResult<Unit>

    fun observeMilestones(coupleId: String): Flow<List<Milestone>>
    suspend fun saveMilestone(coupleId: String, milestone: Milestone): AppResult<Unit>
    suspend fun deleteMilestone(coupleId: String, id: String): AppResult<Unit>

    fun observeCountries(coupleId: String): Flow<List<CountryTrack>>
    suspend fun saveCountry(coupleId: String, country: CountryTrack): AppResult<Unit>

    fun observeTests(coupleId: String): Flow<List<TestRecord>>
    suspend fun saveTest(coupleId: String, test: TestRecord): AppResult<Unit>
    suspend fun deleteTest(coupleId: String, id: String): AppResult<Unit>

    fun observeDocuments(coupleId: String): Flow<List<DocumentItem>>
    suspend fun saveDocument(coupleId: String, doc: DocumentItem): AppResult<Unit>
    suspend fun deleteDocument(coupleId: String, id: String): AppResult<Unit>

    fun observeBudget(coupleId: String): Flow<List<BudgetItem>>
    suspend fun saveBudget(coupleId: String, item: BudgetItem): AppResult<Unit>
    suspend fun deleteBudget(coupleId: String, id: String): AppResult<Unit>

    fun observeDeadlines(coupleId: String): Flow<List<Deadline>>
    suspend fun saveDeadline(coupleId: String, deadline: Deadline): AppResult<Unit>
    suspend fun deleteDeadline(coupleId: String, id: String): AppResult<Unit>
}

@Singleton
class FirebaseTrackingRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) : TrackingRepository {

    private suspend fun set(coupleId: String, collection: String, id: String, data: Map<String, Any?>): AppResult<Unit> = runCatchingResult {
        val ref = if (id.isBlank()) firestore.sub(coupleId, collection).document() else firestore.sub(coupleId, collection).document(id)
        ref.set(data).await()
        Unit
    }

    private suspend fun add(coupleId: String, collection: String, data: (String) -> Map<String, Any?>): AppResult<String> = runCatchingResult {
        val ref = firestore.sub(coupleId, collection).document()
        ref.set(data(ref.id)).await()
        ref.id
    }

    private suspend fun delete(coupleId: String, collection: String, id: String): AppResult<Unit> = runCatchingResult {
        firestore.sub(coupleId, collection).document(id).delete().await()
        Unit
    }

    override fun observeHabits(coupleId: String) = firestore.sub(coupleId, Paths.HABITS).asFlow { it.toHabit() }
    override fun observeHabitLogs(coupleId: String) = firestore.sub(coupleId, Paths.HABIT_LOGS).asFlow { it.toHabitLog() }
    override suspend fun saveHabit(coupleId: String, habit: Habit) = set(coupleId, Paths.HABITS, habit.id, habit.toMap())
    override suspend fun addHabitLog(coupleId: String, log: HabitLog) = add(coupleId, Paths.HABIT_LOGS) { log.copy(id = it).toMap() }
    override suspend fun deleteHabitLog(coupleId: String, id: String) = delete(coupleId, Paths.HABIT_LOGS, id)

    override fun observeMilestones(coupleId: String) = firestore.sub(coupleId, Paths.MILESTONES).asFlow { it.toMilestone() }
    override suspend fun saveMilestone(coupleId: String, milestone: Milestone) = set(coupleId, Paths.MILESTONES, milestone.id, milestone.toMap())
    override suspend fun deleteMilestone(coupleId: String, id: String) = delete(coupleId, Paths.MILESTONES, id)

    override fun observeCountries(coupleId: String) = firestore.sub(coupleId, Paths.COUNTRIES).asFlow { it.toCountry() }
    override suspend fun saveCountry(coupleId: String, country: CountryTrack) = set(coupleId, Paths.COUNTRIES, country.code, country.toMap())

    override fun observeTests(coupleId: String) = firestore.sub(coupleId, Paths.TESTS).asFlow { it.toTest() }
    override suspend fun saveTest(coupleId: String, test: TestRecord) = set(coupleId, Paths.TESTS, test.id, test.toMap())
    override suspend fun deleteTest(coupleId: String, id: String) = delete(coupleId, Paths.TESTS, id)

    override fun observeDocuments(coupleId: String) = firestore.sub(coupleId, Paths.DOCUMENTS).asFlow { it.toDocument() }
    override suspend fun saveDocument(coupleId: String, doc: DocumentItem) = set(coupleId, Paths.DOCUMENTS, doc.id, doc.toMap())
    override suspend fun deleteDocument(coupleId: String, id: String) = delete(coupleId, Paths.DOCUMENTS, id)

    override fun observeBudget(coupleId: String) = firestore.sub(coupleId, Paths.BUDGET).asFlow { it.toBudget() }
    override suspend fun saveBudget(coupleId: String, item: BudgetItem) = set(coupleId, Paths.BUDGET, item.id, item.toMap())
    override suspend fun deleteBudget(coupleId: String, id: String) = delete(coupleId, Paths.BUDGET, id)

    override fun observeDeadlines(coupleId: String) = firestore.sub(coupleId, Paths.DEADLINES).asFlow { it.toDeadline() }
    override suspend fun saveDeadline(coupleId: String, deadline: Deadline) = set(coupleId, Paths.DEADLINES, deadline.id, deadline.toMap())
    override suspend fun deleteDeadline(coupleId: String, id: String) = delete(coupleId, Paths.DEADLINES, id)
}
