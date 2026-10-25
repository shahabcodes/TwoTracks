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
import android.content.Intent
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.isDark
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
import com.shahaabapps.twotracks.widget.WidgetLaunch
import com.shahaabapps.twotracks.widget.WidgetRoutes
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * The app. FragmentActivity (not ComponentActivity) so BiometricPrompt can
 * attach. The launcher reaches it through one of two aliases, one icon for
 * each look; see LauncherIcon.
 */
@AndroidEntryPoint
class AppActivity : FragmentActivity() {

    @Inject lateinit var appLock: AppLock
    @Inject lateinit var celebrations: Celebrations
    @Inject lateinit var widgetLaunch: WidgetLaunch

    /** The look on screen, so the icon can follow it when the app leaves. */
    private var look: AppLook? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        widgetLaunch.offer(intent?.getStringExtra(WidgetRoutes.EXTRA))
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val themeMode by themeViewModel.themeMode.collectAsStateWithLifecycle()
            val look by themeViewModel.look.collectAsStateWithLifecycle()
            LaunchedEffect(look) { this@AppActivity.look = look }

            // Status and navigation bar icons follow the app's own light/dark
            // choice, not the system's, and the Departures board is always dark.
            val dark = isDark(themeMode, look)
            LaunchedEffect(dark) {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = !dark
                    isAppearanceLightNavigationBars = !dark
                }
            }
            AppTheme(themeMode = themeMode, look = look) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Box(Modifier.fillMaxSize()) {
                        AppRoot(appLock, widgetLaunch)
                        CelebrationOverlay(celebrations)
                    }
                }
            }
        }
    }

    /** A second tap on a widget reaches a running activity here, not onCreate. */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        widgetLaunch.offer(intent.getStringExtra(WidgetRoutes.EXTRA))
    }

    override fun onStart() {
        super.onStart()
        appLock.onForeground()
    }

    override fun onStop() {
        super.onStop()
        appLock.onBackground()
        look?.let { LauncherIcon.apply(this, it) }
    }
}

@Composable
private fun AppRoot(appLock: AppLock, widgetLaunch: WidgetLaunch, viewModel: AppViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val locked by appLock.locked.collectAsStateWithLifecycle()
    val signedIn = state is SessionState.Ready || state is SessionState.NoSpace
    LockedOver(
        locked = locked && signedIn,
        lock = { LockScreen(allowedAuthenticators = appLock.allowedAuthenticators, onUnlocked = appLock::unlock) },
    ) {
        when (val current = state) {
            SessionState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { TracksLoader(label = "Opening your space") }
            SessionState.SignedOut -> AuthScreen()
            is SessionState.NoSpace -> OnboardingScreen()
            is SessionState.Ready -> MainNavGraph(session = current.session, widgetLaunch = widgetLaunch)
        }
    }
}

/**
 * The lock, drawn over the app rather than in place of it.
 *
 * This used to be an early return, which took the whole navigation graph out
 * of the composition while the lock was up. The back stack went with it, so
 * locking the phone for a moment and coming back landed you on Today instead
 * of the lesson or episode you were on. Leaving [content] composed underneath
 * is what fixes that: it costs nothing while hidden, and the lock covers it
 * completely and swallows every touch, so nothing behind it can be seen,
 * pressed or reached by a screen reader.
 */
@Composable
internal fun LockedOver(locked: Boolean, lock: @Composable () -> Unit, content: @Composable () -> Unit) {
    Box(Modifier.fillMaxSize()) {
        content()
        if (locked) {
            Box(
                Modifier
                    .fillMaxSize()
                    // Takes every press, drag and swipe, so a tap that lands on
                    // the lock never reaches whatever sits beneath it.
                    .pointerInput(Unit) { awaitPointerEventScope { while (true) awaitPointerEvent() } }
                    .testTag(LOCK_OVERLAY),
            ) {
                lock()
            }
        }
    }
}

/** The lock layer, for the test that holds it over the app rather than in place of it. */
internal const val LOCK_OVERLAY = "lock-overlay"
