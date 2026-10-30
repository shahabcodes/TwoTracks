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
 * A screen stops widening at [ReadableWidth] and centres, and a screen that
 * has opted out does not.
 *
 * Both assertions are about the width a child that asks to fill the width
 * actually gets, which is the only thing that changed: before this, a screen
 * in a 1000dp window was 1000dp of prose. The first test fails against that
 * old behaviour by exactly the amount the cap takes off; the second one
 * guards the other direction, so that "cap everything" cannot pass either.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class, qualifiers = "w1000dp-h800dp-xhdpi")
class ReadableWidthTest {

    @get:Rule
    val compose = createComposeRule()

    private fun show(wide: Boolean) {
        compose.setContent {
            AppTheme(themeMode = ThemeMode.LIGHT, look = AppLook.PARALLEL) {
                ReadableColumn(wide = wide) {
                    Box(Modifier.fillMaxWidth().height(100.dp)) { Text("PAGE") }
                }
            }
        }
    }

    @Test
    fun `prose stops at the readable width and sits centred`() {
        show(wide = false)

        val root = compose.onRoot().getBoundsInRoot()
        val page = compose.onNodeWithText("PAGE").getBoundsInRoot()
        // The Text is wrapped by the Box that fills the capped width, so its
        // own bounds are the text's; the cap is read off where that Box's
        // parent put it, which is the left inset.
        val windowWidth = root.right - root.left
        assertTrue("the window under test is wider than the cap", windowWidth > ReadableWidth)
        assertTrue(
            "content starts at ${page.left}, flush with the edge of a $windowWidth window, so nothing is capped",
            page.left >= (windowWidth - ReadableWidth) / 2 - 1.dp,
        )
    }

    @Test
    fun `a screen that opts out keeps the whole width`() {
        show(wide = true)

        val page = compose.onNodeWithText("PAGE").getBoundsInRoot()
        assertTrue("an opted-out screen was indented to ${page.left}", page.left < 1.dp)
    }
}
