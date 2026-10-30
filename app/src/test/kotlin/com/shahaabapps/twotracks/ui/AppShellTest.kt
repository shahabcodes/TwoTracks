package com.shahaabapps.twotracks.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
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
 * The frame at a tablet's width: the tabs stand on the leading edge instead of
 * lying along the foot, and the playing bar is still there and still clear of
 * them.
 *
 * Every assertion here is about where things are, not whether they exist,
 * because existence passes in both layouts — the same three tabs are composed
 * either way, with the same labels. The bottom bar lays them out side by side
 * near the foot of the window; the rail lays them out one above the other
 * against the leading edge. Only bounds tell those apart, so bounds are what
 * is asserted: "Learn" starts within the rail's width of the left edge, and
 * sits below "Today" rather than beside it. Run against the old layout both of
 * those fail — in a 840dp window the bottom bar puts "Learn" around 380dp from
 * the left, level with "Today".
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class, qualifiers = "w840dp-h1280dp-xhdpi")
class AppShellTest {

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

    private fun show(selectedTab: String? = Routes.TODAY, playing: NowPlaying? = this.playing) {
        compose.setContent {
            AppTheme(themeMode = ThemeMode.LIGHT, look = AppLook.PARALLEL) {
                AppShell(
                    playing = playing,
                    selectedTab = selectedTab,
                    onOpen = {},
                    onPlayPause = {},
                    onForward30 = {},
                    onTab = {},
                ) { padding ->
                    Box(Modifier.fillMaxSize()) { Text("PAGE", modifier = Modifier.fillMaxWidth()) }
                }
            }
        }
    }

    @Test
    fun `at 840dp the tabs stand in a rail on the leading edge, not along the foot`() {
        show()

        val today = compose.onNodeWithText("Today").getBoundsInRoot()
        val learn = compose.onNodeWithText("Learn").getBoundsInRoot()
        val root = compose.onRoot().getBoundsInRoot()

        assertTrue(
            "Learn starts ${learn.left} from the leading edge, so the tabs are still spread across a bottom bar",
            learn.left < 100.dp,
        )
        assertTrue(
            "Learn (${learn.top}..${learn.bottom}) is not below Today (${today.top}..${today.bottom}), so the tabs are side by side",
            today.bottom <= learn.top,
        )
        assertTrue(
            "the tabs reach ${learn.bottom}, past the foot of a ${root.bottom} window",
            learn.bottom <= root.bottom,
        )
    }

    @Test
    fun `with the rail, the playing bar is on screen and clear of it`() {
        show()

        compose.onNode(hasText(playing.title, substring = true)).assertIsDisplayed()
        val bar = compose.onNode(hasText(playing.title, substring = true)).getBoundsInRoot()
        val learn = compose.onNodeWithText("Learn").getBoundsInRoot()
        val root = compose.onRoot().getBoundsInRoot()

        assertTrue("the playing bar is taller than nothing", bar.bottom > bar.top)
        assertTrue("the playing bar runs past the foot of the window", bar.bottom <= root.bottom)
        assertTrue(
            "the playing bar starts at ${bar.left}, over the rail which reaches ${learn.right}",
            bar.left >= learn.right,
        )
    }

    @Test
    fun `on a screen with no tabs there is no rail, and the playing bar still shows`() {
        show(selectedTab = null)

        compose.onNodeWithText("Learn").assertDoesNotExist()
        compose.onNode(hasText(playing.title, substring = true)).assertIsDisplayed()
    }
}

/**
 * The same frame in a phone-sized window: the tabs stay exactly where they
 * were, along the foot, with the playing bar above them.
 *
 * This is the other half of the break, and it fails against a rail drawn
 * unconditionally: there the tabs would be stacked against the left edge in
 * the top half of the window, so both the "side by side" and the "in the
 * bottom half" assertions go.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class, qualifiers = "w411dp-h891dp-xxhdpi")
class AppShellPhoneTest {

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

    private fun show() {
        compose.setContent {
            AppTheme(themeMode = ThemeMode.LIGHT, look = AppLook.PARALLEL) {
                AppShell(
                    playing = playing,
                    selectedTab = Routes.TODAY,
                    onOpen = {},
                    onPlayPause = {},
                    onForward30 = {},
                    onTab = {},
                ) { Box(Modifier.fillMaxSize()) { Text("PAGE") } }
            }
        }
    }

    @Test
    fun `below 840dp the tabs are still a bottom bar`() {
        show()

        val today = compose.onNodeWithText("Today").getBoundsInRoot()
        val learn = compose.onNodeWithText("Learn").getBoundsInRoot()
        val root = compose.onRoot().getBoundsInRoot()

        assertTrue(
            "Learn (${learn.left}) is not to the right of Today (${today.right}), so the tabs are in a rail",
            today.right <= learn.left,
        )
        assertTrue(
            "the tabs start at ${today.top}, in the top half of a ${root.bottom} window",
            today.top > root.bottom / 2,
        )
    }

    @Test
    fun `below 840dp the playing bar is still above the tabs`() {
        show()

        val bar = compose.onNode(hasText(playing.title, substring = true)).getBoundsInRoot()
        val tabs = compose.onNodeWithText("Today").getBoundsInRoot()

        assertTrue(
            "the playing bar's foot (${bar.bottom}) runs past the top of the tabs (${tabs.top})",
            bar.bottom <= tabs.top,
        )
    }
}
