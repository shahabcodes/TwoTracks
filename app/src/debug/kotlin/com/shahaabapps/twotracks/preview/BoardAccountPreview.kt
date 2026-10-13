package com.shahaabapps.twotracks.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.twotracks.feature.auth.AuthActions
import com.shahaabapps.twotracks.feature.auth.AuthMode
import com.shahaabapps.twotracks.feature.auth.AuthUiState
import com.shahaabapps.twotracks.feature.auth.BoardAuth
import com.shahaabapps.twotracks.feature.auth.BoardLock
import com.shahaabapps.twotracks.feature.auth.BoardRoleStep
import com.shahaabapps.twotracks.feature.auth.BoardSpaceStep
import com.shahaabapps.twotracks.ui.board.BoardGutter

/** The Departures account screens, drawn from sample state: signin, signup, reset, role, space, join, lock. */
@Composable
internal fun BoardAccountPreview(screen: String) {
    val actions = AuthActions({}, {}, {}, {}, {})
    val page = if (screen == "lock") AppTheme.colors.deep else AppTheme.colors.groupedBackground
    Column(modifier = Modifier.fillMaxSize().background(page).systemBarsPadding().padding(horizontal = if (screen == "lock") androidx.compose.ui.unit.Dp(0f) else BoardGutter)) {
        when (screen) {
            "signin" -> BoardAuth(AuthUiState(email = "usaid@example.com", password = "twotracks"), actions)
            "signup" -> BoardAuth(AuthUiState(mode = AuthMode.SIGN_UP, email = "usaid@example.com", password = "Twotracks2026", confirmPassword = "Twotracks2026"), actions)
            "reset" -> BoardAuth(AuthUiState(mode = AuthMode.RESET, email = "usaid@example.com", infoMessage = "A reset link is on its way to usaid@example.com."), actions)
            "role" -> BoardRoleStep("Usaid", PersonRole.TECH, "Asia/Qatar", {}, {}, {}, {})
            "space" -> BoardSpaceStep(false, "", null, false, true, {}, {}, {}, {})
            "join" -> BoardSpaceStep(true, "7KQ2MX", null, false, true, {}, {}, {}, {})
            else -> BoardLock("MON 12 OCT", "09:12", null, {})
        }
    }
}

/** The morning brief, as Usaid's Monday has it. */
internal fun sampleBrief() = com.shahaabapps.twotracks.feature.today.DailyBrief(
    greeting = "Good morning, Usaid", dateLine = "Mon 12 Oct",
    task = "AZ-204 · App Service", taskDetail = "Compute · 60 min · Gate Learn",
    reasons = listOf(
        com.shahaabapps.twotracks.feature.today.BriefReason(com.shahaabapps.twotracks.feature.today.BriefKind.GOAL, "AZ-204: 12 of 120 hours. About 4 a week reaches 31 Mar 2027."),
        com.shahaabapps.twotracks.feature.today.BriefReason(com.shahaabapps.twotracks.feature.today.BriefKind.LEVEL, "Foundations: 2 of 5 done, 170 days left."),
        com.shahaabapps.twotracks.feature.today.BriefReason(com.shahaabapps.twotracks.feature.today.BriefKind.LEARN, "App Service, about 28 minutes, counts toward Compute. 18 German cards waiting."),
        com.shahaabapps.twotracks.feature.today.BriefReason(com.shahaabapps.twotracks.feature.today.BriefKind.MOVE, "Marriage in 112 days. Every session now is one less to squeeze in later."),
    ),
    streakLine = "6 days running. Keep the streak.", isRestDay = false, time = "19:00",
)
