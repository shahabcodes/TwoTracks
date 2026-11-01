package com.shahaabapps.twotracks.tap

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performScrollToNode
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.ThemeMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Draws a real screen from its sample state, presses its controls, and
 * checks which callback each press reached and with what. Every test runs
 * once in each look, so the Departures board and the Parallel page are held
 * to the same wiring: a button that does one thing in one look and another
 * thing, or nothing, in the other fails here.
 *
 * Labels are matched without regard to case, because the board prints in
 * capitals what Parallel prints in sentence case. Where the two looks word a
 * control differently, the test says both.
 */
@RunWith(ParameterizedRobolectricTestRunner::class)
// A plain Application: the screens drawn here are the stateless ones, and the
// real one starts Hilt and Firebase, which have nothing to do with a tap.
@Config(sdk = [34], application = android.app.Application::class, qualifiers = "w411dp-h891dp-xxhdpi")
abstract class TapTest(protected val look: AppLook) {
    @get:Rule val compose = createComposeRule()

    /** What the screen asked for, in the order it asked. */
    protected val calls = mutableListOf<String>()

    /** A callback that records its name, and its arguments if it has any. */
    protected fun record(name: String): () -> Unit = { calls += name }
    protected fun <A> record1(name: String): (A) -> Unit = { a -> calls += "$name($a)" }
    protected fun <A, B> record2(name: String): (A, B) -> Unit = { a, b -> calls += "$name($a, $b)" }

    protected val board: Boolean get() = look == AppLook.DEPARTURES

    /** The label in this look: Parallel's wording, or the board's. */
    protected fun label(parallel: String, departures: String = parallel): String = if (board) departures else parallel

    protected fun show(content: @Composable () -> Unit) {
        compose.setContent { AppTheme(themeMode = ThemeMode.LIGHT, look = look) { content() } }
        compose.waitForIdle()
    }

    /** The node whose text is [text], scrolled into view if a list holds it. */
    protected fun node(text: String, index: Int = 0, substring: Boolean = false): SemanticsNodeInteraction =
        find(hasText(text, substring = substring, ignoreCase = true), index)

    protected fun described(description: String, index: Int = 0): SemanticsNodeInteraction =
        find(hasContentDescription(description, substring = false, ignoreCase = true), index)

    /**
     * Scrolls whichever list holds the node until it is on screen, which a
     * press needs: a lazy list composes a little beyond its edge, and a press
     * on a row it has composed but not shown lands outside the window.
     */
    private fun find(matcher: SemanticsMatcher, index: Int): SemanticsNodeInteraction {
        val lists = compose.onAllNodes(hasScrollAction()).fetchSemanticsNodes()
        for (i in lists.indices) {
            if (runCatching { compose.onAllNodes(hasScrollAction())[i].performScrollToNode(matcher) }.isSuccess) break
        }
        compose.waitForIdle()
        return compose.onAllNodes(matcher)[index]
    }

    protected fun tap(text: String, index: Int = 0, substring: Boolean = false) {
        node(text, index, substring).press()
        compose.waitForIdle()
    }

    /**
     * Replaces what is in the only field on screen with [text].
     *
     * By what it can be typed into rather than by what it says: the name in
     * a rename field is also the name on the row behind the dialog, and
     * matching on the text finds whichever of the two came first.
     */
    protected fun type(text: String) {
        compose.onNode(hasSetTextAction()).performTextReplacement(text)
        compose.waitForIdle()
    }

    protected fun tapDescribed(description: String, index: Int = 0) {
        described(description, index).press()
        compose.waitForIdle()
    }

    /** Whether any control on screen has its click labelled [label]. */
    protected fun clickable(label: String): Boolean =
        compose.onAllNodes(clickLabel(label)).fetchSemanticsNodes().isNotEmpty()

    private fun clickLabel(label: String) =
        SemanticsMatcher("click label $label") { it.config.getOrNull(SemanticsActions.OnClick)?.label == label }

    /** Presses the control whose click is labelled [label] for accessibility, as an icon-only one is. */
    protected fun tapAction(label: String, index: Int = 0) {
        find(clickLabel(label), index).press()
        compose.waitForIdle()
    }

/**
     * Runs the control's own click action rather than a simulated finger. What
     * is under test is which callback a control is wired to, and a finger cannot
     * reach a bottom sheet's window under Robolectric.
     */
    private fun SemanticsNodeInteraction.press() = performSemanticsAction(SemanticsActions.OnClick)

    protected fun seen(text: String): Boolean = compose.onAllNodesWithText(text, ignoreCase = true, substring = true).fetchSemanticsNodes().isNotEmpty()

    /** The calls so far are exactly [expected], and the record starts again. */
    protected fun expect(vararg expected: String) {
        assertEquals("in the $look look", expected.toList(), calls.toList())
        calls.clear()
    }

    /** One call was made, and it starts with [prefix]: for an argument the sample decides. */
    protected fun expectStarting(prefix: String) {
        assertEquals("in the $look look: $calls", 1, calls.size)
        assertTrue("in the $look look: ${calls.first()} does not start with $prefix", calls.first().startsWith(prefix))
        calls.clear()
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun looks(): List<Array<Any>> = listOf(arrayOf(AppLook.PARALLEL), arrayOf(AppLook.DEPARTURES))
    }
}

