package com.shahaabapps.twotracks.tap

import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.twotracks.preview.GuidePreview
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performSemanticsAction
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** The tour's own buttons: Next, Back and Skip, over the screen a step belongs to. */
class GuideTapTest(look: AppLook) : TapTest(look) {
    /**
     * The tour's own button, matched by its exact case: on the board the
     * screen behind prints NEXT in capitals, and a looser match pressed that.
     */
    private fun tour(label: String) {
        compose.onNode(hasText(label) and hasClickAction()).performSemanticsAction(SemanticsActions.OnClick)
        compose.waitForIdle()
    }

    @Test
    fun `the tour steps on, steps back and can be skipped`() {
        show { GuidePreview(3) }
        assertTrue("step 3 shows in the $look look", seen("3 OF 21"))
        tour("Next")
        assertTrue("Next reaches step 4 in the $look look", seen("4 OF 21"))
        tour("Back")
        tour("Back")
        assertTrue("Back reaches step 2 in the $look look", seen("2 OF 21"))
        tour("Skip")
        compose.mainClock.advanceTimeBy(1_000)
        assertFalse("Skip ends the tour in the $look look", seen(" OF 21"))
    }
}
