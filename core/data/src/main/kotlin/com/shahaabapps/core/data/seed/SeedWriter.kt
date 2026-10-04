package com.shahaabapps.core.data.seed

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import com.shahaabapps.core.data.firestore.Mappers.toLevel
import com.shahaabapps.core.data.firestore.Mappers.toMap
import com.shahaabapps.core.data.firestore.Paths
import com.shahaabapps.core.data.firestore.couple
import com.shahaabapps.core.data.firestore.sub
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.seed.Seed
import com.shahaabapps.core.seed.SeedSchedule
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Writes the guide content into a couple space. Seeded goals and levels are
 * merged so progress fields (passed, done, startedEarly) survive a re-seed;
 * other seeded docs are replaced. User-created docs and all logs are untouched.
 */
@Singleton
class SeedWriter @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    val version: Int get() = Seed.VERSION

    suspend fun seedAll(coupleId: String) {
        val batches = BatchRunner(firestore)
        Seed.goals.forEach { batches.merge(firestore.sub(coupleId, Paths.GOALS).document(it.id), it.toMap() - setOf("passed", "passedAtIso", "active")) }
        PersonRole.entries.forEach { role ->
            batches.set(firestore.sub(coupleId, Paths.AVAILABILITY).document(role.name), SeedSchedule.availabilityFor(role).toMap())
        }
        Seed.habits.forEach { batches.set(firestore.sub(coupleId, Paths.HABITS).document(it.id), it.toMap()) }
        Seed.deadlines.forEach { batches.set(firestore.sub(coupleId, Paths.DEADLINES).document(it.id), it.toMap()) }
        // Levels: keep each item's done state and the level's progress flags from the existing doc.
        val existingLevels = firestore.sub(coupleId, Paths.LEVELS).get().await().documents.associate { it.id to it.toLevel() }
        Seed.levels.forEach { seed ->
            val existing = existingLevels[seed.id]
            val items = seed.items.map { item ->
                val old = existing?.items?.firstOrNull { it.id == item.id }
                if (old != null) item.copy(done = old.done, doneAtIso = old.doneAtIso) else item
            }
            batches.merge(firestore.sub(coupleId, Paths.LEVELS).document(seed.id), seed.copy(items = items).toMap() - setOf("startedEarly", "completedAtIso", "branch"))
        }
        Seed.countries.forEach { batches.set(firestore.sub(coupleId, Paths.COUNTRIES).document(it.code), it.toMap()) }
        Seed.tests.forEach { batches.set(firestore.sub(coupleId, Paths.TESTS).document(it.id), it.toMap()) }
        Seed.documents.forEach { batches.set(firestore.sub(coupleId, Paths.DOCUMENTS).document(it.id), it.toMap()) }
        Seed.budget.forEach { batches.set(firestore.sub(coupleId, Paths.BUDGET).document(it.id), it.toMap()) }
        batches.update(firestore.couple(coupleId), mapOf("seededVersion" to Seed.VERSION))
        batches.commit()
    }

    /** Firestore batches are capped at 500 writes; this splits automatically. */
    private class BatchRunner(private val firestore: FirebaseFirestore) {
        private val batches = mutableListOf<WriteBatch>()
        private var count = 0

        private fun current(): WriteBatch {
            if (batches.isEmpty() || count >= 450) {
                batches += firestore.batch()
                count = 0
            }
            count++
            return batches.last()
        }

        fun set(ref: com.google.firebase.firestore.DocumentReference, data: Map<String, Any?>) {
            current().set(ref, data)
        }

        fun merge(ref: com.google.firebase.firestore.DocumentReference, data: Map<String, Any?>) {
            current().set(ref, data, com.google.firebase.firestore.SetOptions.merge())
        }

        fun update(ref: com.google.firebase.firestore.DocumentReference, data: Map<String, Any?>) {
            current().update(ref, data)
        }

        suspend fun commit() {
            batches.forEach { it.commit().await() }
        }
    }
}
