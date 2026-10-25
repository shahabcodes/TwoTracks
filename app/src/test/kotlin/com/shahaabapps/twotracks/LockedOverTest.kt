package com.shahaabapps.twotracks

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Locking the phone must not lose your place.
 *
 * The lock used to be an early return that rendered instead of the app, which
 * took the navigation graph out of the composition and threw away the back
 * stack with it: a glance at a notification and you came back to Today rather
 * than the lesson you were reading. This holds the lock to being a layer over
 * the app, by navigating away from the start destination, locking, unlocking,
 * and insisting you are still where you were.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class, qualifiers = "w411dp-h891dp-xxhdpi")
class LockedOverTest {

    @get:Rule
    val compose = createComposeRule()

    private lateinit var setLocked: (Boolean) -> Unit
    private var destination: String? = null

    private fun show() {
        compose.setContent {
            var locked by remember { mutableStateOf(false) }
            setLocked = { locked = it }
            LockedOver(locked = locked, lock = { Text("Two Tracks is locked") }) {
                val navController = rememberNavController()
                destination = navController.currentBackStackEntry?.destination?.route
                NavHost(navController, startDestination = "today") {
                    composable("today") {
                        Text("Today", modifier = Modifier.clickable { navController.navigate("lesson") })
                    }
                    composable("lesson") { Text("A lesson") }
                }
            }
        }
    }

    @Test
    fun `unlocking leaves you where you were, not back at the start`() {
        show()
        compose.onNodeWithText("Today").performClick()
        compose.onNodeWithText("A lesson").assertIsDisplayed()

        setLocked(true)
        compose.waitForIdle()
        compose.onNodeWithTag(LOCK_OVERLAY).assertIsDisplayed()

        setLocked(false)
        compose.waitForIdle()
        compose.onNodeWithText("A lesson").assertIsDisplayed()
    }

    @Test
    fun `the lock covers the app while it is up`() {
        show()
        setLocked(true)
        compose.waitForIdle()

        compose.onNodeWithText("Two Tracks is locked").assertIsDisplayed()
        compose.onNodeWithTag(LOCK_OVERLAY).assertIsDisplayed()
    }

    @Test
    fun `the screen underneath is still there, so its state survives`() {
        show()
        compose.onNodeWithText("Today").performClick()
        compose.waitForIdle()
        setLocked(true)
        compose.waitForIdle()

        // Composed, covered, and still on the destination it was on: that is
        // what keeps the back stack alive across a lock.
        assertEquals("lesson", destination)
    }
}
