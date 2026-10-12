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
fun <T> Query.asFlow(mapper: (Doc) -> T?): Flow<List<T>> = callbackFlow {
    val registration = addSnapshotListener { snapshot, error ->
        if (error != null) {
            close(error)
            return@addSnapshotListener
        }
        trySend(snapshot?.documents?.mapNotNull { runCatching { mapper(it.asDoc()) }.getOrNull() }.orEmpty())
    }
    awaitClose { registration.remove() }
}.retryWhen { _, attempt ->
    delay(minOf(1_000L * (attempt + 1), 10_000L))
    true
}

fun <T> DocumentReference.asFlow(mapper: (Doc) -> T?): Flow<T?> = callbackFlow {
    val registration = addSnapshotListener { snapshot, error ->
        if (error != null) {
            close(error)
            return@addSnapshotListener
        }
        trySend(snapshot?.takeIf { it.exists() }?.let { runCatching { mapper(it.asDoc()) }.getOrNull() })
    }
    awaitClose { registration.remove() }
}.retryWhen { _, attempt ->
    delay(minOf(1_000L * (attempt + 1), 10_000L))
    true
}

/**
 * What a mapper needs from a stored document: an identifier and some fields.
 *
 * Firestore has one implementation of this and a test has another. Before it
 * existed the mappers took a `DocumentSnapshot`, a final class from the
 * Firebase SDK, so the 19 pairs of functions that every record in this app
 * passes through on its way in and out of storage could not be run without a
 * device. They had no tests at all, and a mistyped key on either side reads
 * back as a default — zero minutes, an empty list, the first enum constant —
 * which is exactly the kind of wrong that looks like real data.
 */
interface Doc {
    val id: String
    fun field(name: String): Any?
}

/** The Firestore implementation. [DocumentSnapshot.get] takes dotted paths too. */
fun DocumentSnapshot.asDoc(): Doc = object : Doc {
    override val id: String get() = this@asDoc.id
    override fun field(name: String): Any? = this@asDoc.get(name)
}

/** A document built from fields, for a test or for anything that already holds them. */
fun docOf(id: String = "", fields: Map<String, Any?>): Doc = object : Doc {
    override val id: String = id
    override fun field(name: String): Any? = fields[name]
}

// ---- small typed getters ----
//
// Every one of these coerces rather than casts, because Firestore does not
// promise the type you wrote. Whole numbers come back as Long whatever was
// stored, a Double field holding 3 comes back as Long, and a field that is
// missing or holds something else should read as the default rather than
// throw: `asFlow` would swallow the exception and drop the whole document.
fun Doc.str(field: String): String = field(field) as? String ?: ""
fun Doc.strOrNull(field: String): String? = field(field) as? String
fun Doc.int(field: String, default: Int = 0): Int = (field(field) as? Number)?.toInt() ?: default
fun Doc.intOrNull(field: String): Int? = (field(field) as? Number)?.toInt()
fun Doc.dbl(field: String, default: Double = 0.0): Double = (field(field) as? Number)?.toDouble() ?: default
fun Doc.bool(field: String, default: Boolean = false): Boolean = field(field) as? Boolean ?: default
fun Doc.lng(field: String, default: Long = 0L): Long = (field(field) as? Number)?.toLong() ?: default

// These used to be unchecked casts to List<String> and the like, which succeed
// on a list of anything and fail later, somewhere else.
fun Doc.stringList(field: String): List<String> =
    (field(field) as? List<*>)?.mapNotNull { it as? String }.orEmpty()

@Suppress("UNCHECKED_CAST")
fun Doc.mapList(field: String): List<Map<String, Any?>> =
    (field(field) as? List<*>)?.mapNotNull { it as? Map<String, Any?> }.orEmpty()

fun Doc.stringMap(field: String): Map<String, String> =
    (field(field) as? Map<*, *>)
        ?.mapNotNull { (k, v) -> (k as? String)?.let { it to v?.toString().orEmpty() } }
        ?.toMap()
        .orEmpty()

/** A map of question id to a whole number, which Firestore hands back as Long. */
fun Doc.intMap(field: String): Map<String, Int> =
    (field(field) as? Map<*, *>)
        ?.mapNotNull { (k, v) -> if (k is String && v is Number) k to v.toInt() else null }
        ?.toMap()
        .orEmpty()

/** A map of question id to a yes or no. */
fun Doc.boolMap(field: String): Map<String, Boolean> =
    (field(field) as? Map<*, *>)
        ?.mapNotNull { (k, v) -> if (k is String && v is Boolean) k to v else null }
        ?.toMap()
        .orEmpty()

/** A map of question id to a list of whole numbers. */
fun Doc.intListMap(field: String): Map<String, List<Int>> =
    (field(field) as? Map<*, *>)
        ?.mapNotNull { (k, v) -> if (k is String && v is List<*>) k to v.mapNotNull { n -> (n as? Number)?.toInt() } else null }
        ?.toMap()
        .orEmpty()

inline fun <reified E : Enum<E>> Doc.enum(field: String, default: E): E =
    strOrNull(field)?.let { v -> enumValues<E>().firstOrNull { it.name == v } } ?: default

inline fun <reified E : Enum<E>> Doc.enumOrNull(field: String): E? =
    strOrNull(field)?.let { v -> enumValues<E>().firstOrNull { it.name == v } }

// ---- the same getters for a map nested inside a document ----
fun Map<String, Any?>.s(key: String): String = this[key]?.toString().orEmpty()
fun Map<String, Any?>.sOrNull(key: String): String? = this[key]?.toString()
fun Map<String, Any?>.d(key: String): Double = (this[key] as? Number)?.toDouble() ?: 0.0
fun Map<String, Any?>.i(key: String): Int = (this[key] as? Number)?.toInt() ?: 0
fun Map<String, Any?>.b(key: String): Boolean = this[key] as? Boolean ?: false
