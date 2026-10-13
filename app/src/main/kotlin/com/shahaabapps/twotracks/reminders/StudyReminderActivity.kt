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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.core.ui.theme.AppTheme as AppThemeRoot
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.twotracks.MainActivity
import com.shahaabapps.twotracks.ui.components.TwoTracksMark
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
            val look by themeViewModel.look.collectAsStateWithLifecycle()
            AppThemeRoot(themeMode = themeMode, look = look) {
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

/**
 * The screen that wakes you, as the sign-in screen is drawn: the mark, a mono
 * line saying what this is, the session in large type, and the two answers at
 * the foot where a thumb is. It used to be painted top to bottom in the
 * accent; the accent is ink now, and a page of ink fading to white left
 * "Not now" white on white.
 */
@Composable
private fun ReminderScreen(title: String, text: String, onStart: () -> Unit, onLater: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.groupedBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = Spacing.listHorizontal),
    ) {
        Spacer(Modifier.height(56.dp))
        TwoTracksMark(height = 72.dp)
        Spacer(Modifier.height(40.dp))
        Text("Two Tracks · time to study".uppercase(), style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
        Spacer(Modifier.height(10.dp))
        Text(title, style = MaterialTheme.typography.displayLarge, color = AppTheme.colors.ink)
        if (text.isNotBlank()) {
            Spacer(Modifier.height(10.dp))
            Text(text, style = MaterialTheme.typography.bodyLarge, color = AppTheme.colors.secondaryLabel)
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.accent, contentColor = AppTheme.colors.onSolid),
        ) {
            Text("Start studying", style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp, fontWeight = FontWeight.SemiBold))
        }
        Spacer(Modifier.height(Spacing.xs))
        TextButton(onClick = onLater, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            Text("Not now", style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp), color = AppTheme.colors.secondaryLabel)
        }
    }
}
