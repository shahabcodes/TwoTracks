package com.shahaabapps.twotracks.feature.listen

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.shahaabapps.twotracks.session.SessionStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Where an episode's sound comes from. The audio is not in the app: it is in
 * the couple's own folder in Firebase Storage, couples/<space>/podcasts, which
 * only that space's members may read (firebase/storage.rules), so the
 * episodes are not public with the code or the APK. The first play fetches
 * it to the phone; after that it plays from there, offline.
 *
 * The script and its timing still ship in the app (Episodes): the transcript
 * and the board work before a single byte is downloaded.
 */
@Singleton
class EpisodeAudio @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessions: SessionStore,
) {
    /** An episode on its way, or why it could not come. */
    sealed interface Fetch {
        data class Downloading(val fraction: Float) : Fetch
        data class Failed(val message: String) : Fetch
    }

    private val _fetching = MutableStateFlow<Map<String, Fetch>>(emptyMap())
    val fetching: StateFlow<Map<String, Fetch>> = _fetching

    private val locks = ConcurrentHashMap<String, Mutex>()

    private fun file(episodeId: String) = File(File(context.filesDir, Episodes.DIR), "$episodeId.ogg")

    /** True once the episode is on the phone. */
    fun ready(episodeId: String): Boolean = file(episodeId).let { it.isFile && it.length() > 0 }

    /** What the player plays: the copy on the phone. */
    fun uri(episodeId: String): Uri = Uri.fromFile(file(episodeId))

    /**
     * Fetches the episode if it is not on the phone yet. True when it is ready
     * to play; false with the reason in [fetching] when it could not come.
     */
    suspend fun fetch(episodeId: String): Boolean = locks.getOrPut(episodeId) { Mutex() }.withLock {
        if (ready(episodeId)) return@withLock true
        // Just after the app opens the session may still be loading; wait a little rather than say "sign in".
        val coupleId = sessions.current()?.coupleId
            ?: kotlinx.coroutines.withTimeoutOrNull(15_000) {
                sessions.state.first { it !is com.shahaabapps.twotracks.session.SessionState.Loading }
            }.let { (it as? com.shahaabapps.twotracks.session.SessionState.Ready)?.session?.coupleId }
        if (coupleId == null) {
            fail(episodeId, "Sign in to download this episode.")
            return@withLock false
        }
        val target = file(episodeId)
        target.parentFile?.mkdirs()
        // Fetched beside its final name and moved into place only when whole,
        // so a dropped connection never leaves half an episode that looks ready.
        val partial = File(target.parentFile, "$episodeId.part")
        _fetching.update { it + (episodeId to Fetch.Downloading(0f)) }
        try {
            FirebaseStorage.getInstance().reference.child("couples/$coupleId/podcasts/$episodeId.ogg")
                .getFile(partial)
                .addOnProgressListener { s ->
                    if (s.totalByteCount > 0) {
                        val fraction = (s.bytesTransferred.toFloat() / s.totalByteCount).coerceIn(0f, 1f)
                        _fetching.update { it + (episodeId to Fetch.Downloading(fraction)) }
                    }
                }
                .await()
            if (!partial.renameTo(target)) {
                partial.copyTo(target, overwrite = true)
                partial.delete()
            }
            _fetching.update { it - episodeId }
            true
        } catch (e: Exception) {
            partial.delete()
            fail(episodeId, message(e))
            false
        }
    }

    private fun fail(episodeId: String, message: String) = _fetching.update { it + (episodeId to Fetch.Failed(message)) }

    private fun message(e: Exception): String = when ((e as? StorageException)?.errorCode) {
        StorageException.ERROR_OBJECT_NOT_FOUND -> "This episode isn't uploaded yet."
        StorageException.ERROR_NOT_AUTHORIZED -> "Your account can't download episodes."
        StorageException.ERROR_RETRY_LIMIT_EXCEEDED -> "No connection. Try again when you're online."
        else -> "Couldn't download the episode. Try again."
    }
}
