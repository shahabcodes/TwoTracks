package com.shahaabapps.core.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.common.runCatchingResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface AuthRepository {
    /** Emits the current signed-in user id, or null when signed out. */
    val currentUserId: Flow<String?>
    fun currentUserIdOrNull(): String?
    fun currentEmailOrNull(): String?
    suspend fun signUpWithEmail(email: String, password: String): AppResult<String>
    suspend fun signInWithEmail(email: String, password: String): AppResult<String>
    suspend fun sendPasswordReset(email: String): AppResult<Unit>
    fun signOut()
}

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
) : AuthRepository {

    override val currentUserId: Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser?.uid) }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override fun currentUserIdOrNull(): String? = auth.currentUser?.uid

    override fun currentEmailOrNull(): String? = auth.currentUser?.email

    override suspend fun signUpWithEmail(email: String, password: String): AppResult<String> =
        runCatchingResult {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            requireNotNull(result.user?.uid) { "Sign-up succeeded but uid was null" }
        }

    override suspend fun signInWithEmail(email: String, password: String): AppResult<String> =
        runCatchingResult {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            requireNotNull(result.user?.uid) { "Sign-in succeeded but uid was null" }
        }

    override suspend fun sendPasswordReset(email: String): AppResult<Unit> =
        runCatchingResult { auth.sendPasswordResetEmail(email).await(); Unit }

    override fun signOut() = auth.signOut()
}
