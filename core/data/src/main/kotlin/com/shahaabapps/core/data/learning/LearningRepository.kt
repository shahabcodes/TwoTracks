package com.shahaabapps.core.data.learning

import com.shahaabapps.core.model.Flashcard
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.common.runCatchingResult
import com.shahaabapps.core.data.firestore.Paths
import com.shahaabapps.core.data.firestore.asFlow
import com.shahaabapps.core.data.firestore.bool
import com.shahaabapps.core.data.firestore.int
import com.shahaabapps.core.data.firestore.str
import com.shahaabapps.core.data.firestore.stringList
import com.shahaabapps.core.data.firestore.sub
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.QuizAttempt
import com.shahaabapps.core.model.TopicProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Learning progress lives in two subcollections of the shared space, so both
 * partners see each other's courses: `topicProgress` keyed by owner and
 * topic, and `quizAttempts` with one document per sitting.
 */
interface LearningRepository {
    fun observeTopicProgress(coupleId: String): Flow<List<TopicProgress>>
    fun observeQuizAttempts(coupleId: String): Flow<List<QuizAttempt>>
    suspend fun markLearned(coupleId: String, progress: TopicProgress): AppResult<Unit>
    suspend fun clearLearned(coupleId: String, owner: PersonRole, topicId: String): AppResult<Unit>
    suspend fun addAttempt(coupleId: String, attempt: QuizAttempt): AppResult<String>
    fun observeFlashcards(coupleId: String): Flow<List<Flashcard>>
    suspend fun saveFlashcards(coupleId: String, cards: List<Flashcard>): AppResult<Unit>
}

@Singleton
class FirebaseLearningRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) : LearningRepository {

    override fun observeTopicProgress(coupleId: String): Flow<List<TopicProgress>> =
        firestore.sub(coupleId, Paths.TOPIC_PROGRESS).asFlow { it.toTopicProgress() }

    override fun observeQuizAttempts(coupleId: String): Flow<List<QuizAttempt>> =
        firestore.sub(coupleId, Paths.QUIZ_ATTEMPTS).asFlow { it.toQuizAttempt() }

    override suspend fun markLearned(coupleId: String, progress: TopicProgress): AppResult<Unit> = runCatchingResult {
        firestore.sub(coupleId, Paths.TOPIC_PROGRESS).document(topicDocId(progress.owner, progress.topicId)).set(progress.toMap()).await()
        Unit
    }

    override suspend fun clearLearned(coupleId: String, owner: PersonRole, topicId: String): AppResult<Unit> = runCatchingResult {
        firestore.sub(coupleId, Paths.TOPIC_PROGRESS).document(topicDocId(owner, topicId)).delete().await()
        Unit
    }

    override suspend fun addAttempt(coupleId: String, attempt: QuizAttempt): AppResult<String> = runCatchingResult {
        val ref = firestore.sub(coupleId, Paths.QUIZ_ATTEMPTS).document()
        ref.set(attempt.copy(id = ref.id).toMap()).await()
        ref.id
    }

    override fun observeFlashcards(coupleId: String): Flow<List<Flashcard>> =
        firestore.sub(coupleId, Paths.FLASHCARDS).asFlow { it.toFlashcard() }

    override suspend fun saveFlashcards(coupleId: String, cards: List<Flashcard>): AppResult<Unit> = runCatchingResult {
        val batch = firestore.batch()
        cards.forEach { c -> batch.set(firestore.sub(coupleId, Paths.FLASHCARDS).document("${c.owner.name.lowercase()}_${c.wordId}"), c.toMap()) }
        batch.commit().await()
        Unit
    }

    private fun Flashcard.toMap() = mapOf(
        "owner" to owner.name, "courseId" to courseId, "wordId" to wordId, "step" to step, "dueIso" to dueIso,
        "lastReviewIso" to lastReviewIso, "correct" to correct, "misses" to misses,
    )

    private fun DocumentSnapshot.toFlashcard() = Flashcard(
        id = id, owner = runCatching { PersonRole.valueOf(str("owner")) }.getOrDefault(PersonRole.TECH),
        courseId = str("courseId"), wordId = str("wordId"), step = int("step"), dueIso = str("dueIso"),
        lastReviewIso = str("lastReviewIso"), correct = int("correct"), misses = int("misses"),
    )

    private fun topicDocId(owner: PersonRole, topicId: String) = "${owner.name.lowercase()}_$topicId"

    private fun TopicProgress.toMap() = mapOf(
        "owner" to owner.name, "courseId" to courseId, "topicId" to topicId, "learnedAtIso" to learnedAtIso, "minutes" to minutes,
    )

    private fun DocumentSnapshot.toTopicProgress() = TopicProgress(
        id = id, owner = runCatching { PersonRole.valueOf(str("owner")) }.getOrDefault(PersonRole.TECH),
        courseId = str("courseId"), topicId = str("topicId"), learnedAtIso = str("learnedAtIso"), minutes = int("minutes"),
    )

    private fun QuizAttempt.toMap() = mapOf(
        "owner" to owner.name, "courseId" to courseId, "moduleId" to moduleId, "dateIso" to dateIso,
        "score" to score, "total" to total, "passed" to passed, "missed" to missed,
    )

    private fun DocumentSnapshot.toQuizAttempt() = QuizAttempt(
        id = id, owner = runCatching { PersonRole.valueOf(str("owner")) }.getOrDefault(PersonRole.TECH),
        courseId = str("courseId"), moduleId = str("moduleId"), dateIso = str("dateIso"),
        score = int("score"), total = int("total"), passed = bool("passed"), missed = stringList("missed"),
    )
}
