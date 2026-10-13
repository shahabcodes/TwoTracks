package com.shahaabapps.twotracks.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.twotracks.feature.learn.LearnContent
import com.shahaabapps.twotracks.feature.levels.LevelsContent
import com.shahaabapps.twotracks.feature.settings.SettingsContent
import com.shahaabapps.twotracks.feature.us.UsContent
import com.shahaabapps.twotracks.ui.guide.GuideAnchors
import com.shahaabapps.twotracks.ui.guide.GuideOverlay
import com.shahaabapps.twotracks.ui.guide.GuideState
import com.shahaabapps.twotracks.ui.guide.LocalGuide
import com.shahaabapps.twotracks.ui.guide.guideAnchor
import com.shahaabapps.twotracks.ui.guide.guideSteps

/**
 * The tour at step [number] (1-based), over the screen that step belongs to,
 * drawn from sample state, with a stand-in tab bar for the steps that ring a
 * tab. For checking where the card goes against the ring.
 */
@Composable
internal fun GuidePreview(number: Int) {
    val guide = remember {
        GuideState(guideSteps(learnRoute = "learn", todayRoute = "today", levelsRoute = "levels", usRoute = "us", settingsRoute = "settings"))
            .apply { start(); repeat((number - 1).coerceAtLeast(0)) { next() } }
    }
    val anchor = guide.step?.anchor.orEmpty()
    CompositionLocalProvider(LocalGuide provides guide) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                Box(Modifier.weight(1f)) {
                    when {
                        anchor.startsWith("set.") -> SettingsContent(
                            SettingsSamples.settings(), remember { SnackbarHostState() }, onBack = {}, onEditProfile = {}, onCopyInvite = {}, onOpenAvailability = {},
                            onEditStartDate = {}, onOpenFullScreen = {}, onTheme = {}, onLook = {}, onBiometric = {}, onOpenVoices = {}, onOpenWidgets = {}, onOpenChangelog = {},
                            onTour = {}, onReset = {}, onSignOut = {},
                        )
                        anchor == GuideAnchors.LEVELS_LADDER -> LevelsContent(PlanSamples.levels(), onBack = {}, onOpenLevel = {}, onShowPartner = {})
                        anchor == GuideAnchors.US_LEVELS || anchor == GuideAnchors.TAB_US -> UsContent(TogetherSamples.us(), onNavigate = {}, onOpenLevel = {})
                        else -> LearnContent(LearnSamples.learn(), onOpenCourse = {}, onOpenTopic = { _, _ -> }, onOpenDeck = {}, onOpenQuiz = { _, _ -> })
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().background(AppTheme.colors.groupedBackground).navigationBarsPadding().height(64.dp),
                ) {
                    listOf(GuideAnchors.TAB_TODAY to "Today", GuideAnchors.TAB_LEARN to "Learn", GuideAnchors.TAB_US to "Route").forEach { (key, name) ->
                        Text(name, color = AppTheme.colors.ink, textAlign = TextAlign.Center, modifier = Modifier.guideAnchor(key).padding(horizontal = 16.dp, vertical = 12.dp))
                    }
                }
            }
            GuideOverlay(guide)
        }
    }
}
