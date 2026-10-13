package com.shahaabapps.twotracks.tap

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.twotracks.changelog.ChangelogContent
import com.shahaabapps.twotracks.feature.auth.AuthActions
import com.shahaabapps.twotracks.feature.auth.AuthMode
import com.shahaabapps.twotracks.feature.auth.AuthUiState
import com.shahaabapps.twotracks.feature.auth.BoardAuth
import com.shahaabapps.twotracks.feature.auth.BoardLock
import com.shahaabapps.twotracks.feature.auth.BoardRoleStep
import com.shahaabapps.twotracks.feature.auth.BoardSpaceStep
import com.shahaabapps.twotracks.feature.settings.AvailabilityContent
import com.shahaabapps.twotracks.feature.settings.SettingsContent
import com.shahaabapps.twotracks.feature.settings.WidgetsContent
import com.shahaabapps.twotracks.preview.SettingsSamples
import com.shahaabapps.twotracks.widget.PlacedWidget
import com.shahaabapps.twotracks.widget.WidgetConfig
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test

/** Settings and its pages, and on the board the account screens: what each press reaches. */
class SettingsTapTest(look: AppLook) : TapTest(look) {
    @Test
    fun `settings reaches every row it offers, and switches the look`() {
        show {
            SettingsContent(
                SettingsSamples.settings(), remember { SnackbarHostState() }, onBack = record("back"), onEditProfile = record("profile"), onCopyInvite = record("invite"),
                onOpenAvailability = record("hours"), onEditStartDate = record("start"), onOpenFullScreen = record("fullScreen"), onTheme = record1("theme"),
                onLook = record1("look"), onBiometric = record1("lock"), onOpenVoices = record("voices"), onOpenWidgets = record("widgets"),
                onOpenChangelog = record("changelog"), onTour = record("tour"), onReset = record("reset"), onSignOut = record("signOut"),
            )
        }
        tap("Edit")
        tap("Invite code")
        tap("Weekly hours")
        tap("Study started")
        expect("profile", "invite", "hours", "start")
        tap(if (board) "Parallel" else "Departures")
        expect("look(${if (board) AppLook.PARALLEL else AppLook.DEPARTURES})")
        tap("Voices")
        tap("Widgets")
        tap("What's new")
        expect("voices", "widgets", "changelog")
        tap("guide content", substring = true)
        tap("Sign out")
        expect("reset", "signOut")
    }

    @Test
    fun `weekly hours opens a new block and goes back`() {
        show { AvailabilityContent(SettingsSamples.blocks, isTech = true, goals = emptyList(), onBack = record("back"), onAdd = { calls += "add" }, onRemove = { calls += "remove" }) }
        tap("Add a block")
        assertTrue("the new block form opens in the $look look", seen("Label (optional)"))
        tap("Cancel")
        tapDescribed("Back to settings")
        expect("back")
    }

    @Test
    fun `widgets change a placed one, and what's new goes back`() {
        val placed = listOf(PlacedWidget(7, WidgetConfig()))
        show {
            Column {
                WidgetsContent(placed, emptyMap(), onBack = record("back"), onChange = { calls += "change(${it.appWidgetId})" })
            }
        }
        tap("Change")
        expect("change(7)")
        tapDescribed("Back to settings")
        expect("back")
    }

    @Test
    fun `what's new goes back`() {
        show { ChangelogContent(SettingsSamples.releases(), "0.76.0", onBack = record("back")) }
        tapDescribed("Back to settings")
        expect("back")
    }

    @Test
    fun `on the board, check-in submits, goes to sign-up and to the reset`() {
        assumeTrue(board)
        val actions = AuthActions({ calls += "email" }, { calls += "password" }, { calls += "confirm" }, record1("mode"), record("submit"))
        show { Column { BoardAuth(AuthUiState(email = "usaid@example.com", password = "twotracks"), actions) } }
        tap("Check in")
        tap("Create your space")
        tap("Forgot?")
        expect("submit", "mode(${AuthMode.SIGN_UP})", "mode(${AuthMode.RESET})")
    }

    @Test
    fun `on the board, setup picks a line, continues, signs out, and boards a space`() {
        assumeTrue(board)
        show {
            Column {
                BoardRoleStep("Usaid", PersonRole.TECH, "Asia/Qatar", onName = record1("name"), onRole = record1("role"), onContinue = record("continue"), onSignOut = record("signOut"))
                BoardSpaceStep(true, "7KQ2MX", null, false, true, onJoining = record1("joining"), onCode = record1("code"), onSubmit = record("board"), onBack = record("back"))
            }
        }
        tap("The teacher", substring = true)
        tap("Continue")
        tap("Sign out")
        expect("role(${PersonRole.TEACHER})", "continue", "signOut")
        tap("Start new")
        tap("Board the space")
        expect("joining(false)", "board")
    }

    @Test
    fun `on the board, the lock screen unlocks`() {
        assumeTrue(board)
        show { BoardLock("MON 12 OCT", "09:12", null, onUnlock = record("unlock")) }
        tapAction("Unlock with fingerprint")
        expect("unlock")
    }
}
