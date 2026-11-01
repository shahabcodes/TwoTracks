package com.shahaabapps.core.data.ink

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.common.runCatchingResult
import com.shahaabapps.core.data.firestore.Mappers.toInkNote
import com.shahaabapps.core.data.firestore.Mappers.toMap
import com.shahaabapps.core.data.firestore.Paths
import com.shahaabapps.core.data.firestore.asFlow
import com.shahaabapps.core.data.firestore.sub
import com.shahaabapps.core.model.InkNote
import com.shahaabapps.core.model.InkPage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The pages of handwriting the two of you keep against a course.
 *
 * Split the same way the attachments are, and for the same reason: the
 * strokes go to Cloud Storage, which is made for bytes, and a small record
 * goes to Firestore, which is what a list reads. A page of handwriting is
 * a hundred kilobytes of coordinates; putting that in the document would
 * mean the lesson could not say "two notes" without downloading both.
 */
interface InkNotesRepository {

    /** The notes on one lesson — or on the module itself — most recent first. */
    fun observe(coupleId: String, courseId: String, moduleId: String): Flow<List<InkNote>>

    /** How many notes each topic has, by topic id, for the row on a lesson. */
    fun counts(coupleId: String): Flow<Map<String, Int>>

    /**
     * Writes [page] and the record, creating the note if it has no id yet.
     *
     * The strokes go first and the record second, so a save that fails part
     * way leaves either nothing or a complete note, never a row in a list
     * that opens onto a blank page.
     */
    suspend fun save(coupleId: String, note: InkNote, page: InkPage): AppResult<InkNote>

    /** The strokes, fetched when the note is opened rather than when it is listed. */
    suspend fun load(note: InkNote): AppResult<InkPage>

    /** Gives the note another title; the strokes are not touched. */
    suspend fun rename(coupleId: String, note: InkNote, title: String): AppResult<Unit>

    /** Removes the strokes and then the record. */
    suspend fun delete(coupleId: String, note: InkNote): AppResult<Unit>
}

@Singleton
class FirebaseInkNotesRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) : InkNotesRepository {

    override fun observe(coupleId: String, courseId: String, moduleId: String): Flow<List<InkNote>> =
        firestore.sub(coupleId, Paths.INK_NOTES)
            .whereEqualTo("courseId", courseId)
            .whereEqualTo("moduleId", moduleId)
            .asFlow { it.toInkNote() }
            // Sorted here rather than in the query, as the attachments are:
            // two equality filters need no composite index and an orderBy on
            // a third field does, and a missing index fails on a phone.
            .map { notes -> notes.sortedByDescending { it.updatedAtIso } }

    override fun counts(coupleId: String): Flow<Map<String, Int>> =
        firestore.sub(coupleId, Paths.INK_NOTES)
            .asFlow { it.toInkNote() }
            .map { notes -> notes.filter { it.topicId.isNotBlank() }.groupingBy { it.topicId }.eachCount() }

    override suspend fun save(coupleId: String, note: InkNote, page: InkPage): AppResult<InkNote> =
        runCatchingResult {
            val doc = if (note.id.isBlank()) firestore.sub(coupleId, Paths.INK_NOTES).document()
            else firestore.sub(coupleId, Paths.INK_NOTES).document(note.id)
            val stored = note.copy(
                id = doc.id,
                path = note.path.ifBlank { path(coupleId, note.courseId, note.moduleId, doc.id) },
                strokeCount = page.strokes.count { !it.isEmpty },
            )
            storage.getReference(stored.path)
                .putBytes(InkCodec.encode(page).toByteArray()).await()
            doc.set(stored.toMap()).await()
            stored
        }

    override suspend fun load(note: InkNote): AppResult<InkPage> = runCatchingResult {
        // A page of handwriting is coordinates, and a long one is perhaps
        // 300KB; the cap is generous enough never to be met by writing and
        // small enough that a corrupt record cannot ask for the heap.
        val bytes = storage.getReference(note.path).getBytes(MAX_BYTES).await()
        InkCodec.decode(String(bytes))
    }

    override suspend fun rename(coupleId: String, note: InkNote, title: String): AppResult<Unit> =
        runCatchingResult {
            firestore.sub(coupleId, Paths.INK_NOTES).document(note.id)
                .update("title", title.trim()).await()
            Unit
        }

    override suspend fun delete(coupleId: String, note: InkNote): AppResult<Unit> = runCatchingResult {
        // Strokes first, record second: the other order leaves bytes nobody
        // can see and nobody can delete.
        runCatching { storage.getReference(note.path).delete().await() }
        firestore.sub(coupleId, Paths.INK_NOTES).document(note.id).delete().await()
        Unit
    }

    private companion object {
        const val MAX_BYTES = 4L * 1024 * 1024

        /**
         * The strokes live beside the module's attachments, under the path
         * storage.rules already covers: couples/{id}/modules/{course}/{module}/{file}.
         *
         * Deliberately the same folder rather than a notes/ of its own. The
         * rule there is exactly the rule a note wants — the two of you and
         * nobody else — and a second clause saying the same thing is a second
         * clause to get wrong, and a deploy between a release and its feature
         * working.
         */
        fun path(coupleId: String, courseId: String, moduleId: String, noteId: String) =
            "couples/$coupleId/modules/$courseId/$moduleId/$noteId"
    }
}
