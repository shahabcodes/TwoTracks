package com.shahaabapps.twotracks.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** The three faces of the auth screen. */
enum class AuthMode { SIGN_IN, SIGN_UP, RESET }

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val mode: AuthMode = AuthMode.SIGN_IN,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    /** Bumps on every failure so the screen can shake once per error. */
    val errorTick: Int = 0,
) {
    val isSignUp: Boolean get() = mode == AuthMode.SIGN_UP
    val emailValid: Boolean get() = email.contains("@") && email.substringAfter("@").contains(".")
    val passwordsMatch: Boolean get() = confirmPassword == password
    val canSubmit: Boolean
        get() = !isSubmitting && when (mode) {
            AuthMode.RESET -> emailValid
            AuthMode.SIGN_UP -> emailValid && password.length >= 6 && confirmPassword.isNotEmpty() && passwordsMatch
            AuthMode.SIGN_IN -> emailValid && password.length >= 6
        }
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value.trim(), errorMessage = null, infoMessage = null) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value, errorMessage = null, infoMessage = null) }
    fun onConfirmChange(value: String) = _uiState.update { it.copy(confirmPassword = value, errorMessage = null, infoMessage = null) }
    fun setMode(mode: AuthMode) = _uiState.update { it.copy(mode = mode, confirmPassword = "", errorMessage = null, infoMessage = null) }
    fun toggleMode() = setMode(if (_uiState.value.isSignUp) AuthMode.SIGN_IN else AuthMode.SIGN_UP)

    fun submit() {
        val current = _uiState.value
        if (!current.canSubmit) return
        if (current.mode == AuthMode.RESET) { resetPassword(); return }
        _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
        viewModelScope.launch {
            val result = if (current.isSignUp) {
                authRepository.signUpWithEmail(current.email, current.password)
            } else {
                authRepository.signInWithEmail(current.email, current.password)
            }
            when (result) {
                // Clear the form so the next person to sign in (after a sign-out) starts fresh.
                is AppResult.Success -> _uiState.value = AuthUiState()
                is AppResult.Failure -> fail(friendly(result.error.message))
            }
        }
    }

    fun resetPassword() {
        val email = _uiState.value.email
        if (!_uiState.value.emailValid) { fail("Enter your email first"); return }
        _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
        viewModelScope.launch {
            when (val r = authRepository.sendPasswordReset(email)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isSubmitting = false, infoMessage = "Reset link sent to $email. Check your inbox and spam, choose a new password, then sign in here.")
                }
                is AppResult.Failure -> fail(friendly(r.error.message))
            }
        }
    }

    private fun fail(message: String) = _uiState.update { it.copy(isSubmitting = false, errorMessage = message, errorTick = it.errorTick + 1) }

    private fun friendly(raw: String?): String = when {
        raw == null -> "Something went wrong"
        raw.contains("INVALID_LOGIN_CREDENTIALS", true) || raw.contains("password is invalid", true) || raw.contains("no user record", true) ->
            "Email or password does not match"
        raw.contains("email address is already in use", true) -> "That email already has an account. Sign in instead."
        raw.contains("badly formatted", true) -> "That email does not look right"
        raw.contains("network", true) -> "No connection. Try again when online."
        raw.contains("too many", true) -> "Too many attempts. Wait a minute and try again."
        else -> raw
    }
}
