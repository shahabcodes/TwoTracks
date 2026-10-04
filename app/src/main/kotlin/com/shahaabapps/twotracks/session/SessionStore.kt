package com.shahaabapps.twotracks.session

import com.shahaabapps.core.data.auth.AuthRepository
import com.shahaabapps.core.data.couple.CoupleRepository
import com.shahaabapps.core.model.Couple
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

/** Everything the UI needs to know about who is signed in and which space they share. */
sealed interface SessionState {
    data object Loading : SessionState
    data object SignedOut : SessionState
    /** Signed in but not yet in a couple space (create or join). */
    data class NoSpace(val uid: String) : SessionState
    data class Ready(val session: AppSession) : SessionState
}

data class AppSession(
    val uid: String,
    val couple: Couple,
    val me: Profile,
    val partner: Profile?,
) {
    val coupleId: String get() = couple.id
    val myRole: PersonRole get() = me.role
    val partnerRole: PersonRole get() = me.role.other
    fun profileFor(role: PersonRole): Profile? = if (role == me.role) me else partner
    fun nameFor(role: PersonRole): String = profileFor(role)?.name?.ifBlank { null } ?: if (role == PersonRole.TECH) "Tech" else "Teacher"
}

/**
 * Single source of truth for the signed-in session, shared by every ViewModel.
 * Lives for the whole process so screens don't each re-subscribe to Firestore.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class SessionStore @Inject constructor(
    authRepository: AuthRepository,
    coupleRepository: CoupleRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val state: StateFlow<SessionState> = authRepository.currentUserId
        .flatMapLatest { uid ->
            if (uid == null) {
                flowOf(SessionState.SignedOut)
            } else {
                coupleRepository.observeCoupleForUser(uid).flatMapLatest { couple ->
                    if (couple == null) {
                        flowOf(SessionState.NoSpace(uid))
                    } else {
                        coupleRepository.observeProfiles(couple.id).flatMapLatest { profiles ->
                            val me = profiles.firstOrNull { it.uid == uid }
                            if (me == null) {
                                flowOf(SessionState.NoSpace(uid))
                            } else {
                                flowOf(
                                    SessionState.Ready(
                                        AppSession(
                                            uid = uid,
                                            couple = couple,
                                            me = me,
                                            partner = profiles.firstOrNull { it.uid != uid },
                                        ),
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
        .stateIn(scope, SharingStarted.Eagerly, SessionState.Loading)

    /** The ready session or null; convenient for ViewModels. */
    val session: StateFlow<AppSession?> = combine(state, flowOf(Unit)) { s, _ -> (s as? SessionState.Ready)?.session }
        .stateIn(scope, SharingStarted.Eagerly, null)

    fun current(): AppSession? = (state.value as? SessionState.Ready)?.session
}
