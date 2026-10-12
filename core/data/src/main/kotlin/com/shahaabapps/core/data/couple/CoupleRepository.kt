package com.shahaabapps.core.data.couple

import com.google.firebase.firestore.FirebaseFirestore
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.common.runCatchingResult
import com.shahaabapps.core.data.firestore.Mappers.toCouple
import com.shahaabapps.core.data.firestore.Mappers.toMap
import com.shahaabapps.core.data.firestore.Mappers.toProfile
import com.shahaabapps.core.data.firestore.Paths
import com.shahaabapps.core.data.firestore.asDoc
import com.shahaabapps.core.data.firestore.asFlow
import com.shahaabapps.core.data.firestore.sub
import com.shahaabapps.core.data.seed.SeedWriter
import com.shahaabapps.core.model.Couple
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface CoupleRepository {
    /** The couple space the user belongs to, or null when none yet. */
    fun observeCoupleForUser(uid: String): Flow<Couple?>
    fun observeProfiles(coupleId: String): Flow<List<Profile>>

    /** Creates a new couple space with seeded content and the creator's profile. */
    suspend fun createCouple(uid: String, profile: Profile): AppResult<Couple>

    /** Joins an existing couple via its invite code and writes the joiner's profile. */
    suspend fun joinCouple(uid: String, inviteCode: String, profile: Profile): AppResult<Couple>

    suspend fun saveProfile(coupleId: String, profile: Profile): AppResult<Unit>

    /**
     * Re-writes all guide content; keeps logs and progress. With [startIso]
     * the plan restarts on that date (Level 1 starts then, later dates follow);
     * without it the current start date is kept.
     */
    suspend fun resetSeedContent(coupleId: String, startIso: String? = null): AppResult<Unit>
}

@Singleton
class FirebaseCoupleRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val seedWriter: SeedWriter,
) : CoupleRepository {

    override fun observeCoupleForUser(uid: String): Flow<Couple?> =
        firestore.collection(Paths.COUPLES)
            .whereArrayContains("members", uid)
            .limit(1)
            .asFlow { it.toCouple() }
            .map { it.firstOrNull() }

    override fun observeProfiles(coupleId: String): Flow<List<Profile>> =
        firestore.sub(coupleId, Paths.PROFILES).asFlow { it.toProfile() }

    override suspend fun createCouple(uid: String, profile: Profile): AppResult<Couple> = runCatchingResult {
        val ref = firestore.collection(Paths.COUPLES).document()
        val couple = Couple(
            id = ref.id,
            members = listOf(uid),
            inviteCode = generateInviteCode(),
            createdAtMillis = System.currentTimeMillis(),
            seededVersion = 0,
        )
        ref.set(couple.toMap()).await()
        firestore.sub(ref.id, Paths.PROFILES).document(uid).set(profile.copy(uid = uid).toMap()).await()
        seedWriter.seedAll(ref.id, java.time.LocalDate.now().toString())
        couple.copy(seededVersion = seedWriter.version)
    }

    override suspend fun joinCouple(uid: String, inviteCode: String, profile: Profile): AppResult<Couple> = runCatchingResult {
        val code = inviteCode.trim().uppercase()
        require(code.length == INVITE_LENGTH) { "Invite code must be $INVITE_LENGTH characters" }
        val snapshot = firestore.collection(Paths.COUPLES).whereEqualTo("inviteCode", code).limit(1).get().await()
        val doc = snapshot.documents.firstOrNull() ?: throw IllegalArgumentException("No space found for that code")
        val couple = doc.asDoc().toCouple()
        require(uid in couple.members || couple.members.size < 2) { "That space already has two members" }
        // Become a member first: the security rules only let members read the
        // space's subcollections, and the join rule only permits adding yourself.
        if (uid !in couple.members) {
            doc.reference.update("members", couple.members + uid).await()
        }
        val existingProfiles = firestore.sub(couple.id, Paths.PROFILES).get().await().documents.map { it.asDoc().toProfile() }
        val taken = existingProfiles.filter { it.uid != uid }.map { it.role }
        val role = if (profile.role in taken) profile.role.other else profile.role
        firestore.sub(couple.id, Paths.PROFILES).document(uid).set(profile.copy(uid = uid, role = role).toMap()).await()
        couple.copy(members = (couple.members + uid).distinct())
    }

    override suspend fun saveProfile(coupleId: String, profile: Profile): AppResult<Unit> = runCatchingResult {
        firestore.sub(coupleId, Paths.PROFILES).document(profile.uid).set(profile.toMap()).await()
        Unit
    }

    override suspend fun resetSeedContent(coupleId: String, startIso: String?): AppResult<Unit> = runCatchingResult {
        seedWriter.seedAll(coupleId, startIso)
    }

    private fun generateInviteCode(): String {
        val alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        return (1..INVITE_LENGTH).map { alphabet.random() }.joinToString("")
    }

    private companion object {
        const val INVITE_LENGTH = 6
    }
}

/** Default profile role suggestion used by onboarding. */
fun defaultRoleFor(existing: List<Profile>): PersonRole =
    if (existing.any { it.role == PersonRole.TECH }) PersonRole.TEACHER else PersonRole.TECH
