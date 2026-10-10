package com.shahaabapps.twotracks.feature.settings

import com.shahaabapps.twotracks.session.Celebrations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.data.auth.AuthRepository
import com.shahaabapps.core.data.couple.CoupleRepository
import com.shahaabapps.core.data.study.StudyRepository
import com.shahaabapps.core.data.study.forRole
import com.shahaabapps.core.model.Availability
import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.Profile
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.seed.Seed
import com.shahaabapps.twotracks.reminders.ReminderScheduler
import com.shahaabapps.twotracks.reminders.SessionPlanning
import com.shahaabapps.twotracks.session.AppLock
import com.shahaabapps.twotracks.session.AppSession
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val session: AppSession? = null,
    val email: String = "",
    val availability: Availability = Availability(),
    val myGoals: List<StudyGoal> = emptyList(),
    val seedOutdated: Boolean = false,
    val message: String? = null,
    val biometricAvailable: Boolean = false,
    val biometricEnabled: Boolean = false,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    private val dataStore: CoupleDataStore,
    private val authRepository: AuthRepository,
    private val coupleRepository: CoupleRepository,
    private val studyRepository: StudyRepository,
    private val sessionPlanning: SessionPlanning,
    private val reminderScheduler: ReminderScheduler,
    private val appLock: AppLock,
    private val celebrations: Celebrations,
) : ViewModel() {

    private val message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<SettingsUiState> = combine(
        sessionStore.session, dataStore.availability, dataStore.goals, message, appLock.enabled,
    ) { s, availability, goals, msg, lockEnabled ->
        SettingsUiState(
            session = s, email = authRepository.currentEmailOrNull().orEmpty(),
            availability = s?.let { availability.forRole(it.myRole) } ?: Availability(),
            myGoals = goals.filter { it.owner == s?.myRole },
            seedOutdated = (s?.couple?.seededVersion ?: 0) < Seed.VERSION,
            message = msg,
            biometricAvailable = appLock.isAvailable(),
            biometricEnabled = lockEnabled,
        )
    }
        // Off the main thread. The block asks BiometricManager whether this
        // phone can authenticate, which is a binder call into the biometric
        // service, and reads the signed-in email — and a combine is evaluated
        // wherever it is collected, which here is the UI.
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    fun setBiometricLock(enabled: Boolean) {
        appLock.setEnabled(enabled)
        message.value = if (enabled) "The app will ask to unlock when you open it" else "Biometric lock off"
    }

    fun saveProfile(update: (Profile) -> Profile) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        when (val r = coupleRepository.saveProfile(s.coupleId, update(s.me))) {
            is AppResult.Success -> { message.value = "Saved"; reminderScheduler.refresh(); celebrations.saved("Profile saved") }
            is AppResult.Failure -> message.value = r.error.message
        }
    }

    fun saveAvailability(blocks: List<AvailabilityBlock>) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        studyRepository.saveAvailability(s.coupleId, Availability(owner = s.myRole, blocks = blocks))
        val today = Clock.today(s.me)
        val week = com.shahaabapps.core.planner.Dates.weekStart(today)
        sessionPlanning.replan(week, s.myRole)
        sessionPlanning.replan(week.plusWeeks(1), s.myRole)
        reminderScheduler.refresh()
        celebrations.saved("Week template saved")
        message.value = "Schedule saved and weeks re-planned"
    }

    fun resetSeed() = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        when (val r = coupleRepository.resetSeedContent(s.coupleId, Clock.todayIso(s.me))) {
            is AppResult.Success -> { message.value = "Guide content restored and the plan restarted from today. Your logs are untouched."; celebrations.saved("Plan restarted today") }
            is AppResult.Failure -> message.value = r.error.message
        }
    }

    fun changePassword(current: String, new: String) = viewModelScope.launch {
        if (new.length < 6) { message.value = "New password needs at least 6 characters"; return@launch }
        when (val r = authRepository.changePassword(current, new)) {
            is AppResult.Success -> { celebrations.saved("Password changed"); message.value = "Password changed" }
            is AppResult.Failure -> message.value = friendlyAuthError(r.error.message)
        }
    }

    /** Emails the Firebase reset link to the signed-in address. */
    fun sendPasswordReset() = viewModelScope.launch {
        val email = authRepository.currentEmailOrNull() ?: run { message.value = "No email on this account"; return@launch }
        when (val r = authRepository.sendPasswordReset(email)) {
            is AppResult.Success -> message.value = "Reset link sent to $email. Open it, choose a new password, then sign in again."
            is AppResult.Failure -> message.value = r.error.message ?: "Could not send the reset email"
        }
    }

    private fun friendlyAuthError(raw: String?): String = when {
        raw == null -> "Could not change the password"
        raw.contains("password is invalid", true) || raw.contains("INVALID_LOGIN_CREDENTIALS", true) || raw.contains("credential", true) -> "Current password is wrong"
        raw.contains("weak", true) -> "New password is too weak"
        raw.contains("network", true) -> "No connection. Try again when online."
        else -> raw
    }

    fun signOut() = authRepository.signOut()

    fun clearMessage() { message.value = null }

    fun roleLabel(role: PersonRole) = if (role == PersonRole.TECH) "Developer (Germany, Ireland, Netherlands first)" else "Teacher (Canada, New Zealand first)"
}
