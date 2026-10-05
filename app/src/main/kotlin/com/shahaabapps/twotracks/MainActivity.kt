package com.shahaabapps.twotracks

import com.shahaabapps.twotracks.ui.components.TracksLoader
import android.os.Bundle
import androidx.activity.compose.setContent
import com.shahaabapps.core.ui.theme.ThemeMode
import androidx.core.view.WindowCompat
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.twotracks.feature.auth.AuthScreen
import com.shahaabapps.twotracks.feature.lock.LockScreen
import com.shahaabapps.twotracks.feature.onboarding.OnboardingScreen
import com.shahaabapps.twotracks.session.AppLock
import com.shahaabapps.twotracks.session.SessionState
import com.shahaabapps.twotracks.ui.AppViewModel
import com.shahaabapps.twotracks.ui.MainNavGraph
import com.shahaabapps.twotracks.ui.components.CelebrationOverlay
import com.shahaabapps.twotracks.session.Celebrations
import com.shahaabapps.twotracks.ui.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/** FragmentActivity (not ComponentActivity) so BiometricPrompt can attach. */
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject lateinit var appLock: AppLock
    @Inject lateinit var celebrations: Celebrations

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val themeMode by themeViewModel.themeMode.collectAsStateWithLifecycle()
            val accentPalette by themeViewModel.accentPalette.collectAsStateWithLifecycle()

            // Status and navigation bar icons follow the app's own light/dark choice, not the system's.
            val dark = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }
            LaunchedEffect(dark) {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = !dark
                    isAppearanceLightNavigationBars = !dark
                }
            }
            AppTheme(themeMode = themeMode, accentPalette = accentPalette) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Box(Modifier.fillMaxSize()) {
                        AppRoot(appLock)
                        CelebrationOverlay(celebrations, accentPalette)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        appLock.onForeground()
    }

    override fun onStop() {
        super.onStop()
        appLock.onBackground()
    }
}

@Composable
private fun AppRoot(appLock: AppLock, viewModel: AppViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val locked by appLock.locked.collectAsStateWithLifecycle()
    val signedIn = state is SessionState.Ready || state is SessionState.NoSpace
    if (locked && signedIn) {
        LockScreen(allowedAuthenticators = appLock.allowedAuthenticators, onUnlocked = appLock::unlock)
        return
    }
    when (val current = state) {
        SessionState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { TracksLoader(label = "Opening your space") }
        SessionState.SignedOut -> AuthScreen()
        is SessionState.NoSpace -> OnboardingScreen()
        is SessionState.Ready -> MainNavGraph(session = current.session)
    }
}
