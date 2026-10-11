package com.shahaabapps.core.data.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.retryWhen

/** Firestore layout: everything lives under couples/{coupleId}/{collection}. */
object Paths {
    const val COUPLES = "couples"
    const val PROFILES = "profiles"
    const val AVAILABILITY = "availability"
    const val GOALS = "studyGoals"
    const val STUDY_LOGS = "studyLogs"
    const val MOCK_SCORES = "mockScores"
    const val SESSIONS = "plannedSessions"
    const val HABITS = "habits"
    const val HABIT_LOGS = "habitLogs"
    const val MILESTONES = "milestones"
    const val LEVELS = "levels"
    const val COUNTRIES = "countries"
    const val TESTS = "tests"
    const val DOCUMENTS = "documents"
    const val BUDGET = "budget"
    const val DEADLINES = "deadlines"
    const val TOPIC_PROGRESS = "topicProgress"
    const val QUIZ_ATTEMPTS = "quizAttempts"
    const val FLASHCARDS = "flashcards"
}

fun FirebaseFirestore.couple(coupleId: String): DocumentReference =
    collection(Paths.COUPLES).document(coupleId)

fun FirebaseFirestore.sub(coupleId: String, name: String): CollectionReference =
    couple(coupleId).collection(name)

/**
 * Live list of mapped documents. A listener error (for example PERMISSION_DENIED
 * while a just-created couple document is still propagating) re-registers the
 * listener with a short backoff instead of leaving the flow silently stale.
 */
fun <T> Query.asFlow(mapper: (DocumentSnapshot) -> T?): Flow<List<T>> = callbackFlow {
    val registration = addSnapshotListener { snapshot, error ->
        if (error != null) {
            close(error)
            return@addSnapshotListener
        }
        trySend(snapshot?.documents?.mapNotNull { runCatching { mapper(it) }.getOrNull() }.orEmpty())
    }
    awaitClose { registration.remove() }
}.retryWhen { _, attempt ->
    delay(minOf(1_000L * (attempt + 1), 10_000L))
    true
}

fun <T> DocumentReference.asFlow(mapper: (DocumentSnapshot) -> T?): Flow<T?> = callbackFlow {
    val registration = addSnapshotListener { snapshot, error ->
        if (error != null) {
            close(error)
            return@addSnapshotListener
        }
        trySend(snapshot?.takeIf { it.exists() }?.let { runCatching { mapper(it) }.getOrNull() })
    }
    awaitClose { registration.remove() }
}.retryWhen { _, attempt ->
    delay(minOf(1_000L * (attempt + 1), 10_000L))
    true
}

// ---- small typed getters ----
fun DocumentSnapshot.str(field: String): String = getString(field).orEmpty()
fun DocumentSnapshot.strOrNull(field: String): String? = getString(field)
fun DocumentSnapshot.int(field: String, default: Int = 0): Int = getLong(field)?.toInt() ?: default
fun DocumentSnapshot.intOrNull(field: String): Int? = getLong(field)?.toInt()
fun DocumentSnapshot.dbl(field: String, default: Double = 0.0): Double = getDouble(field) ?: default
fun DocumentSnapshot.bool(field: String, default: Boolean = false): Boolean = getBoolean(field) ?: default
fun DocumentSnapshot.lng(field: String, default: Long = 0L): Long = getLong(field) ?: default

@Suppress("UNCHECKED_CAST")
fun DocumentSnapshot.stringList(field: String): List<String> = (get(field) as? List<String>).orEmpty()

@Suppress("UNCHECKED_CAST")
fun DocumentSnapshot.mapList(field: String): List<Map<String, Any?>> = (get(field) as? List<Map<String, Any?>>).orEmpty()

@Suppress("UNCHECKED_CAST")
fun DocumentSnapshot.stringMap(field: String): Map<String, String> =
    (get(field) as? Map<String, Any?>)?.mapValues { it.value?.toString().orEmpty() }.orEmpty()

/** A map of question id to a whole number, which Firestore hands back as Long. */
@Suppress("UNCHECKED_CAST")
fun DocumentSnapshot.intMap(field: String): Map<String, Int> =
    (get(field) as? Map<String, Any?>)
        ?.mapNotNull { (k, v) -> (v as? Number)?.let { k to it.toInt() } }
        ?.toMap()
        .orEmpty()

/** A map of question id to a yes or no. */
@Suppress("UNCHECKED_CAST")
fun DocumentSnapshot.boolMap(field: String): Map<String, Boolean> =
    (get(field) as? Map<String, Any?>)
        ?.mapNotNull { (k, v) -> (v as? Boolean)?.let { k to it } }
        ?.toMap()
        .orEmpty()

/** A map of question id to a list of whole numbers. */
@Suppress("UNCHECKED_CAST")
fun DocumentSnapshot.intListMap(field: String): Map<String, List<Int>> =
    (get(field) as? Map<String, Any?>)
        ?.mapNotNull { (k, v) -> (v as? List<Any?>)?.let { l -> k to l.mapNotNull { (it as? Number)?.toInt() } } }
        ?.toMap()
        .orEmpty()

inline fun <reified E : Enum<E>> DocumentSnapshot.enum(field: String, default: E): E =
    getString(field)?.let { v -> enumValues<E>().firstOrNull { it.name == v } } ?: default

inline fun <reified E : Enum<E>> DocumentSnapshot.enumOrNull(field: String): E? =
    getString(field)?.let { v -> enumValues<E>().firstOrNull { it.name == v } }

fun Map<String, Any?>.s(key: String): String = this[key]?.toString().orEmpty()
fun Map<String, Any?>.sOrNull(key: String): String? = this[key]?.toString()
fun Map<String, Any?>.d(key: String): Double = (this[key] as? Number)?.toDouble() ?: 0.0
fun Map<String, Any?>.i(key: String): Int = (this[key] as? Number)?.toInt() ?: 0
fun Map<String, Any?>.b(key: String): Boolean = this[key] as? Boolean ?: false
