package com.shahaabapps.twotracks.session

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Optional biometric lock. When enabled, the app asks for fingerprint, face or
 * the device PIN on a cold start and whenever it comes back after being in the
 * background for more than [RELOCK_AFTER_MS]. Firebase sign-in is untouched;
 * this only gates the UI on this device.
 */
@Singleton
class AppLock @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val prefs = context.getSharedPreferences("app_lock", Context.MODE_PRIVATE)

    private val _enabled = MutableStateFlow(prefs.getBoolean(KEY_ENABLED, false))
    val enabled: StateFlow<Boolean> = _enabled

    private val _locked = MutableStateFlow(_enabled.value)
    val locked: StateFlow<Boolean> = _locked

    private var backgroundedAt: Long = 0L

    val allowedAuthenticators: Int = BIOMETRIC_STRONG or DEVICE_CREDENTIAL

    /** True when the device has a usable biometric or screen lock. */
    fun isAvailable(): Boolean =
        BiometricManager.from(context).canAuthenticate(allowedAuthenticators) == BiometricManager.BIOMETRIC_SUCCESS

    fun setEnabled(value: Boolean) {
        prefs.edit().putBoolean(KEY_ENABLED, value).apply()
        _enabled.value = value
        if (!value) _locked.value = false
    }

    fun unlock() { _locked.value = false }

    /** Called from the activity's onStop. */
    fun onBackground() { backgroundedAt = System.currentTimeMillis() }

    /** Called from the activity's onStart. Re-locks after a long enough absence. */
    fun onForeground() {
        if (!_enabled.value) return
        if (backgroundedAt > 0L && System.currentTimeMillis() - backgroundedAt > RELOCK_AFTER_MS) _locked.value = true
    }

    private companion object {
        const val KEY_ENABLED = "enabled"
        const val RELOCK_AFTER_MS = 30_000L
    }
}
