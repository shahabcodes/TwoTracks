package com.shahaabapps.twotracks.ui

import androidx.compose.material3.Scaffold
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.ThemeMode
import com.shahaabapps.twotracks.feature.listen.NowPlaying
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * The playing bar sits above the tabs, not behind them.
 *
 * This is here because of a bug that was invisible to the obvious test.
 * Scaffold measures its bottom bar slot as a multi-child layout and places
 * every child at the same top edge, so the bar and the tabs, written as
 * siblings, were laid out one on top of the other. The tab bar draws second
 * over an opaque background, so the playing bar was painted out of sight on
 * every tabbed screen — Today, Learn and Together — while still appearing on
 * screens with no tabs, which is why it looked as though the feature worked.
 *
 * Asking whether the bar exists, or even whether it is displayed, passes in
 * both layouts: an overdrawn node is still attached and still has a size. The
 * only assertion that tells the two apart is where they sit, so this test
 * compares their bounds and insists the bar ends before the tabs begin.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class, qualifiers = "w411dp-h891dp-xxhdpi")
class BottomBarsTest {

    @get:Rule
    val compose = createComposeRule()

    private val playing = NowPlaying(
        episodeId = "ai-app",
        title = "Zaiqa: the app we build all course",
        playing = true,
        position = 60.0,
        duration = 480.0,
        speed = 1f,
    )

    private fun show(selectedTab: String?) {
        compose.setContent {
            AppTheme(themeMode = ThemeMode.LIGHT, look = AppLook.PARALLEL) {
                Scaffold(
                    bottomBar = {
                        BottomBars(
                            playing = playing,
                            selectedTab = selectedTab,
                            onOpen = {},
                            onPlayPause = {},
                            onForward30 = {},
                            onTab = {},
                        )
                    },
                ) { }
            }
        }
    }

    @Test
    fun `the playing bar finishes above the tabs, not underneath them`() {
        show(selectedTab = Routes.TODAY)

        val bar = compose.onNode(hasText(playing.title, substring = true)).getBoundsInRoot()
        val tabs = compose.onNodeWithText("Today", ignoreCase = true).getBoundsInRoot()

        assertTrue(
            "the playing bar's foot (${bar.bottom}) runs past the top of the tabs (${tabs.top}), so the tabs cover it",
            bar.bottom <= tabs.top,
        )
    }

    @Test
    fun `the playing bar is on screen on a tabbed route`() {
        show(selectedTab = Routes.TODAY)

        compose.onNode(hasText(playing.title, substring = true)).assertIsDisplayed()
        val bar = compose.onNode(hasText(playing.title, substring = true)).getBoundsInRoot()
        val root = compose.onRoot().getBoundsInRoot()
        assertTrue("the playing bar is taller than nothing", bar.bottom.value > bar.top.value)
        assertTrue("the playing bar is inside the window", bar.bottom <= root.bottom)
    }

    @Test
    fun `with no tabs the bar still shows, at the foot of the screen`() {
        show(selectedTab = null)

        compose.onNode(hasText(playing.title, substring = true)).assertIsDisplayed()
    }
}
