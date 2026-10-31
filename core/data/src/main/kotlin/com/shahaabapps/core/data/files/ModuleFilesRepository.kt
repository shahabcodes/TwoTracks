package com.shahaabapps.core.data.files

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
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
     * Uploads [uri]'s bytes and writes the record. [onProgress] is called with
     * a fraction while it goes, so a large file does not look like a hang.
     */
    suspend fun upload(
        coupleId: String,
        file: ModuleFile,
        uri: Uri,
        onProgress: (Float) -> Unit = {},
    ): AppResult<ModuleFile>

    /** A link to the bytes, for handing to whatever app opens that kind of file. */
    suspend fun url(file: ModuleFile): AppResult<String>

    /** Removes the bytes and then the record. */
    suspend fun delete(coupleId: String, file: ModuleFile): AppResult<Unit>
}

@Singleton
class FirebaseModuleFilesRepository @Inject constructor(
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

    override suspend fun url(file: ModuleFile): AppResult<String> = runCatchingResult {
        storage.getReference(file.path).downloadUrl.await().toString()
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
        /** couples/{coupleId}/modules/{courseId}/{moduleId}/{fileId} — see firebase/storage.rules. */
        fun path(coupleId: String, courseId: String, moduleId: String, fileId: String) =
            "couples/$coupleId/modules/$courseId/$moduleId/$fileId"
    }
}
