package com.shahaabapps.twotracks.reminders

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.core.ui.theme.AppTheme as AppThemeRoot
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.twotracks.MainActivity
import com.shahaabapps.twotracks.ui.ThemeViewModel
import com.shahaabapps.twotracks.widget.WidgetRoutes
import dagger.hilt.android.AndroidEntryPoint

/**
 * The reminder, taking over the screen.
 *
 * A notification in a tray is a reminder you can agree with and then ignore.
 * This one stands in front of whatever is on the phone, says the one thing
 * there is to do and offers to start it, which is the whole difference
 * between being told to study and starting.
 *
 * It is reached two ways: as a notification's full-screen intent, which the
 * system fires only where the permission has been given, and by tapping the
 * notification, which works everywhere. Both land here, so the screen is the
 * same either way and nothing has to be written twice.
 */
@AndroidEntryPoint
class StudyReminderActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showOverLockScreen()
        enableEdgeToEdge()

        val title = intent?.getStringExtra(EXTRA_TITLE).orEmpty().ifBlank { "Time to study" }
        val text = intent?.getStringExtra(EXTRA_TEXT).orEmpty()
        val route = intent?.getStringExtra(EXTRA_ROUTE)

        setContent {
            val themeMode by themeViewModel.themeMode.collectAsStateWithLifecycle()
            AppThemeRoot(themeMode = themeMode) {
                ReminderScreen(
                    title = title,
                    text = text,
                    onStart = {
                        startActivity(
                            Intent(this, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                route?.let { putExtra(WidgetRoutes.EXTRA, it) }
                            },
                        )
                        finish()
                    },
                    onLater = { finish() },
                )
            }
        }
    }

    /**
     * Turns the screen on and shows in front of the lock screen. The flags are
     * the pre-27 spelling of the same thing, kept because the app runs back to
     * API 26.
     */
    @Suppress("DEPRECATION")
    private fun showOverLockScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            )
        }
    }

    companion object {
        const val EXTRA_TITLE = "reminder_title"
        const val EXTRA_TEXT = "reminder_text"
        const val EXTRA_ROUTE = "reminder_route"
    }
}

@Composable
private fun ReminderScreen(title: String, text: String, onStart: () -> Unit, onLater: () -> Unit) {
    // The app's own accent, top to bottom, so the screen that wakes you is
    // recognisably this app and not a system dialog.
    val accent = AppTheme.colors.accent
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(accent, accent.copy(alpha = 0.82f), AppTheme.colors.groupedBackground))),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = Spacing.lg),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "TWO TRACKS",
                style = MaterialTheme.typography.labelMedium,
                color = AppTheme.colors.onSolid.copy(alpha = 0.75f),
                letterSpacing = 3.sp,
            )
            Spacer(Modifier.height(Spacing.md))
            Text(
                title,
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = AppTheme.colors.onSolid,
                textAlign = TextAlign.Center,
            )
            if (text.isNotBlank()) {
                Spacer(Modifier.height(Spacing.sm))
                Text(
                    text,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppTheme.colors.onSolid.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(Modifier.height(Spacing.xl))
            // The app's own buttons are drawn in the accent, which is what this
            // whole screen is painted in: an accent button on an accent
            // background is a rectangle you cannot read. On top of the accent
            // the roles swap, so the button takes the ink that sits on the accent and carries the accent.
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.onSolid, contentColor = accent),
            ) {
                Text("Start studying", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
            }
            Spacer(Modifier.height(Spacing.xs))
            TextButton(onClick = onLater, modifier = Modifier.fillMaxWidth()) {
                Text("Not now", style = MaterialTheme.typography.titleMedium, color = AppTheme.colors.onSolid.copy(alpha = 0.92f))
            }
        }
    }
}
