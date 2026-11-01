package com.shahaabapps.core.data.files

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.common.runCatchingResult
import com.shahaabapps.core.data.firestore.Mappers.toMap
import com.shahaabapps.core.data.firestore.Mappers.toModuleFile
import com.shahaabapps.core.data.firestore.Paths
import com.shahaabapps.core.data.firestore.asFlow
import com.shahaabapps.core.data.firestore.sub
import com.shahaabapps.core.model.ModuleFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The files the two of you have attached to a module.
 *
 * Two halves, deliberately: the bytes go to Cloud Storage, which is cheap and
 * made for them, and a small record goes to Firestore, which is what the
 * screen reads. That split is why the list is instant and works offline from
 * the Firestore cache while the files themselves are fetched only when
 * opened.
 *
 * Both halves live under the couple, so the existing rules already say who
 * may see them: the Firestore subcollection rule covers the records, and
 * storage.rules carries the matching clause for the bytes.
 */
interface ModuleFilesRepository {

    /** Everything attached to this module, newest first. */
    fun observe(coupleId: String, courseId: String, moduleId: String): Flow<List<ModuleFile>>

    /**
     * How many files each module has, by module id, so a course screen can
     * say so without a listener per module. Module ids are unique across the
     * courses, which is what lets one listener answer for all of them.
     */
    fun counts(coupleId: String): Flow<Map<String, Int>>

    /**
     * The same, by topic id, for the row on a lesson. Files attached to a
     * module rather than a topic are not counted: nothing on a lesson should
     * promise something that lesson does not have.
     */
    fun topicCounts(coupleId: String): Flow<Map<String, Int>>

    /**
     * Uploads [uri]'s bytes and writes the record. [onProgress] is called with
     * a fraction while it goes, so a large file does not look like a hang.
     */
    suspend fun upload(
        coupleId: String,
        file: ModuleFile,
        uri: Uri,
        onProgress: (Float) -> Unit = {},
    ): AppResult<ModuleFile>

    /**
     * Gives the file another name.
     *
     * Only the record changes. The bytes are stored under the file's id and
     * never under its name, which is what makes this one field write rather
     * than a copy, and what means a rename cannot lose anything.
     */
    suspend fun rename(coupleId: String, file: ModuleFile, name: String): AppResult<Unit>

    /** A link to the bytes, for handing to whatever app opens that kind of file. */
    suspend fun url(file: ModuleFile): AppResult<String>

    /**
     * The bytes on this phone, so the app can draw them itself.
     *
     * Fetched once and kept in the cache directory, which is what makes a
     * second look at the same page instant and makes it work on the train.
     * A PDF has to be a real file — the renderer takes a file descriptor,
     * not a stream — and an image may as well be the same file, so both go
     * through here. [onProgress] reports a fraction while it downloads.
     */
    suspend fun localFile(file: ModuleFile, onProgress: (Float) -> Unit = {}): AppResult<File>

    /** Removes the bytes and then the record. */
    suspend fun delete(coupleId: String, file: ModuleFile): AppResult<Unit>
}

@Singleton
class FirebaseModuleFilesRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) : ModuleFilesRepository {

    override fun observe(coupleId: String, courseId: String, moduleId: String): Flow<List<ModuleFile>> =
        firestore.sub(coupleId, Paths.MODULE_FILES)
            .whereEqualTo("courseId", courseId)
            .whereEqualTo("moduleId", moduleId)
            .asFlow { it.toModuleFile() }
            // Sorted here rather than in the query. Two equality filters need
            // no composite index; adding an orderBy on a third field does, and
            // a missing index is a runtime failure on the user's phone rather
            // than something a build catches.
            .map { files -> files.sortedByDescending { it.uploadedAtIso } }

    override fun counts(coupleId: String): Flow<Map<String, Int>> =
        firestore.sub(coupleId, Paths.MODULE_FILES)
            .asFlow { it.toModuleFile() }
            .map { files -> files.groupingBy { it.moduleId }.eachCount() }

    override fun topicCounts(coupleId: String): Flow<Map<String, Int>> =
        firestore.sub(coupleId, Paths.MODULE_FILES)
            .asFlow { it.toModuleFile() }
            .map { files -> files.filter { it.topicId.isNotBlank() }.groupingBy { it.topicId }.eachCount() }

    override suspend fun upload(coupleId: String, file: ModuleFile, uri: Uri, onProgress: (Float) -> Unit): AppResult<ModuleFile> =
        runCatchingResult {
            val doc = firestore.sub(coupleId, Paths.MODULE_FILES).document()
            val stored = file.copy(id = doc.id, path = path(coupleId, file.courseId, file.moduleId, doc.id))
            storage.getReference(stored.path).putFile(uri)
                .addOnProgressListener { s ->
                    // A zero total means the provider would not say how big it
                    // is, and dividing by it would report a fraction of NaN,
                    // which draws as an empty bar that never moves.
                    if (s.totalByteCount > 0) onProgress(s.bytesTransferred.toFloat() / s.totalByteCount)
                }
                .await()
            // The record is written only once the bytes are there, so a failed
            // or cancelled upload never leaves a row pointing at nothing.
            doc.set(stored.toMap()).await()
            stored
        }

    override suspend fun rename(coupleId: String, file: ModuleFile, name: String): AppResult<Unit> =
        runCatchingResult {
            firestore.sub(coupleId, Paths.MODULE_FILES).document(file.id)
                .update("name", name.trim()).await()
            Unit
        }

    override suspend fun url(file: ModuleFile): AppResult<String> = runCatchingResult {
        storage.getReference(file.path).downloadUrl.await().toString()
    }

    override suspend fun localFile(file: ModuleFile, onProgress: (Float) -> Unit): AppResult<File> =
        runCatchingResult {
            val folder = File(context.cacheDir, CACHE_FOLDER).apply { mkdirs() }
            // Named by the Firestore id, which is unique across the couple and
            // never changes, so one file's cache entry can never be served for
            // another's — which a name like "scan.pdf" could not promise.
            val target = File(folder, file.id)
            // A size match is the whole check: these records are written once
            // at upload and the bytes beneath them are never rewritten, so a
            // file of the right length is the right file, already here.
            if (file.sizeBytes > 0 && target.length() == file.sizeBytes) return@runCatchingResult target
            val partial = File(folder, file.id + ".part")
            storage.getReference(file.path).getFile(partial)
                .addOnProgressListener { s ->
                    if (s.totalByteCount > 0) onProgress(s.bytesTransferred.toFloat() / s.totalByteCount)
                }
                .await()
            // Only a finished download becomes the cache entry. Downloading
            // straight to [target] would leave half a PDF looking like a whole
            // one to the size check above the next time it is opened.
            target.delete()
            check(partial.renameTo(target)) { "could not keep ${file.name}" }
            target
        }

    override suspend fun delete(coupleId: String, file: ModuleFile): AppResult<Unit> = runCatchingResult {
        // Bytes first: if this fails the record stays and the file is still
        // listed, which is recoverable. The other order leaves bytes nobody
        // can see and nobody can delete.
        runCatching { storage.getReference(file.path).delete().await() }
        firestore.sub(coupleId, Paths.MODULE_FILES).document(file.id).delete().await()
        Unit
    }

    private companion object {
        /** Under cacheDir, so the system may clear it when the phone is short of room. */
        const val CACHE_FOLDER = "module-files"

        /** couples/{coupleId}/modules/{courseId}/{moduleId}/{fileId} — see firebase/storage.rules. */
        fun path(coupleId: String, courseId: String, moduleId: String, fileId: String) =
            "couples/$coupleId/modules/$courseId/$moduleId/$fileId"
    }
}
