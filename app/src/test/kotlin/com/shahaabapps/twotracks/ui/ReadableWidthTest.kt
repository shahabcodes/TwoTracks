package com.shahaabapps.twotracks.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.ThemeMode
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * How much of a wide window a page is given, and how much page colour is
 * allowed to show either side of it.
 *
 * Every assertion here is about the left inset of a child that asked to fill
 * the width, because that one number is the whole rule: the column is centred,
 * so the inset is the gutter. A tablet held upright must land on exactly the
 * layout it had before the gutter was capped, which is what the 800dp test is
 * for. Turned on its side the gutter must stop growing, which is what the
 * 1280dp test is for, and it fails against the old cap by 240dp.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class)
class ReadableWidthTest {

    @get:Rule
    val compose = createComposeRule()

    private fun show(wide: Boolean = false) {
        compose.setContent {
            AppTheme(themeMode = ThemeMode.LIGHT, look = AppLook.PARALLEL) {
                ReadableColumn(wide = wide) {
                    Box(Modifier.fillMaxWidth().height(100.dp)) { Text("PAGE") }
                }
            }
        }
    }

    /** The gutter the page actually got, read off where its left edge landed. */
    private fun gutter() = compose.onNodeWithText("PAGE").getBoundsInRoot().left

    @Test
    @Config(qualifiers = "w800dp-h1280dp-xhdpi")
    fun `a tablet held upright keeps the readable column, unchanged`() {
        show()

        val window = compose.onRoot().getBoundsInRoot().let { it.right - it.left }
        assertTrue("the window under test is $window, no wider than the cap", window > ReadableWidth)
        // 800 less the 720dp measure is 40dp either side, which is also the
        // most gutter allowed: upright the two rules agree, and this is the
        // test that says the new one moved nothing here.
        assertTrue("the column starts at ${gutter()} in a $window window", gutter() in 39.dp..41.dp)
    }

    @Test
    @Config(qualifiers = "w1280dp-h800dp-xhdpi")
    fun `the same tablet on its side does not strand the page in the middle`() {
        show()

        // The old rule put this at 280dp, which is a phone screen in a frame.
        assertTrue("the column starts at ${gutter()} on a 1280dp screen", gutter() <= MaxGutter + 1.dp)
    }

    @Test
    @Config(qualifiers = "w400dp-h800dp-xhdpi")
    fun `a phone is given its whole screen`() {
        show()

        assertTrue("a phone page was indented to ${gutter()}", gutter() < 1.dp)
    }

    @Test
    @Config(qualifiers = "w1280dp-h800dp-xhdpi")
    fun `a screen that opts out keeps the whole width`() {
        show(wide = true)

        assertTrue("an opted-out screen was indented to ${gutter()}", gutter() < 1.dp)
    }
}
