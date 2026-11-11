package com.shahaabapps.core.data.ink

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.common.runCatchingResult
import com.shahaabapps.core.data.firestore.Mappers.toLessonInk
import com.shahaabapps.core.data.firestore.Mappers.toMap
import com.shahaabapps.core.data.firestore.Paths
import com.shahaabapps.core.data.firestore.asFlow
import com.shahaabapps.core.data.firestore.sub
import com.shahaabapps.core.model.LessonInk
import com.shahaabapps.core.model.LessonMark
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * What one person has marked onto one lesson.
 *
 * There is no query here and no index: a lesson's marks live at a document
 * id made from the reader and the lesson, so the screen asks for exactly the
 * document it wants. That also makes the thing impossible to get wrong in
 * the direction that would matter — one reader can never be handed the
 * other's marks, because the id they ask for does not exist for them.
 */
interface LessonInkRepository {

    /** The record for this reader's marks on this lesson, or null for none yet. */
    fun observe(coupleId: String, owner: String, topicId: String): Flow<LessonInk?>

    /** The strokes, fetched when the lesson opens rather than when it is listed. */
    suspend fun load(record: LessonInk): AppResult<List<LessonMark>>

    /** Writes the marks, and the record that says how many there are. */
    suspend fun save(
        coupleId: String,
        record: LessonInk,
        marks: List<LessonMark>,
    ): AppResult<LessonInk>

    /** Takes every mark off the lesson, record and all. */
    suspend fun clear(coupleId: String, record: LessonInk): AppResult<Unit>
}

@Singleton
class FirebaseLessonInkRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) : LessonInkRepository {

    override fun observe(coupleId: String, owner: String, topicId: String): Flow<LessonInk?> =
        firestore.sub(coupleId, Paths.LESSON_INK).document(id(owner, topicId))
            .asFlow { it.toLessonInk() }

    override suspend fun load(record: LessonInk): AppResult<List<LessonMark>> = runCatchingResult {
        if (record.path.isBlank() || record.strokeCount == 0) return@runCatchingResult emptyList()
        val bytes = storage.getReference(record.path).getBytes(MAX_BYTES).await()
        LessonInkCodec.decode(String(bytes))
    }

    override suspend fun save(coupleId: String, record: LessonInk, marks: List<LessonMark>): AppResult<LessonInk> =
        runCatchingResult {
            val docId = id(record.owner, record.topicId)
            val stored = record.copy(
                id = docId,
                path = record.path.ifBlank { path(coupleId, record.courseId, record.moduleId, docId) },
                strokeCount = marks.size,
            )
            // Strokes first, record second, so a save that fails part way
            // leaves a record that still points at the marks it describes.
            storage.getReference(stored.path)
                .putBytes(LessonInkCodec.encode(marks).toByteArray()).await()
            firestore.sub(coupleId, Paths.LESSON_INK).document(docId).set(stored.toMap()).await()
            stored
        }

    override suspend fun clear(coupleId: String, record: LessonInk): AppResult<Unit> = runCatchingResult {
        if (record.path.isNotBlank()) runCatching { storage.getReference(record.path).delete().await() }
        firestore.sub(coupleId, Paths.LESSON_INK).document(id(record.owner, record.topicId)).delete().await()
        Unit
    }

    private companion object {
        const val MAX_BYTES = 4L * 1024 * 1024

        /** One document per reader per lesson, at an id both ends can work out. */
        fun id(owner: String, topicId: String) = "$owner-$topicId"

        /**
         * Beside the module's attachments, under the path storage.rules
         * already covers. The same reasoning as the handwritten notes: the
         * rule there is the rule these want, and a second clause saying so
         * is a second clause to get wrong.
         */
        fun path(coupleId: String, courseId: String, moduleId: String, docId: String) =
            "couples/$coupleId/modules/$courseId/$moduleId/lesson-$docId"
    }
}
