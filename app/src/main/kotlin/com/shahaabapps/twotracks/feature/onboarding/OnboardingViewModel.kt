package com.shahaabapps.twotracks.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.data.auth.AuthRepository
import com.shahaabapps.core.data.couple.CoupleRepository
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.Profile
import com.shahaabapps.core.seed.SeedSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class OnboardingMode { CHOOSE, CREATE, JOIN }

data class OnboardingUiState(
    val mode: OnboardingMode = OnboardingMode.CHOOSE,
    val name: String = "",
    val role: PersonRole = PersonRole.TECH,
    val timezone: String = SeedSchedule.defaultTimezone.getValue(PersonRole.TECH),
    val inviteCode: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
) {
    val canSubmit: Boolean
        get() = name.isNotBlank() && !isSubmitting && (mode == OnboardingMode.CREATE || inviteCode.trim().length == 6)
}

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val coupleRepository: CoupleRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun setMode(mode: OnboardingMode) = _uiState.update { it.copy(mode = mode, errorMessage = null) }
    fun onNameChange(v: String) = _uiState.update { it.copy(name = v, errorMessage = null) }
    fun onInviteCodeChange(v: String) = _uiState.update { it.copy(inviteCode = v.uppercase().take(6), errorMessage = null) }
    fun onTimezoneChange(v: String) = _uiState.update { it.copy(timezone = v) }

    fun onRoleChange(role: PersonRole) = _uiState.update {
        it.copy(role = role, timezone = SeedSchedule.defaultTimezone.getValue(role))
    }

    fun submit() {
        val s = _uiState.value
        val uid = authRepository.currentUserIdOrNull() ?: return
        if (!s.canSubmit) return
        _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
        val profile = Profile(uid = uid, name = s.name.trim(), role = s.role, timezone = s.timezone, reminderMinutes = null)
        viewModelScope.launch {
            val result = if (s.mode == OnboardingMode.CREATE) {
                coupleRepository.createCouple(uid, profile)
            } else {
                coupleRepository.joinCouple(uid, s.inviteCode, profile)
            }
            when (result) {
                is AppResult.Success -> _uiState.update { it.copy(isSubmitting = false) }
                is AppResult.Failure -> _uiState.update {
                    it.copy(isSubmitting = false, errorMessage = result.error.message ?: "Something went wrong")
                }
            }
        }
    }

    fun signOut() = authRepository.signOut()
}
