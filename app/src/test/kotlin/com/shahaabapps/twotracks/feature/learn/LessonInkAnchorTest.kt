package com.shahaabapps.twotracks.feature.learn

import androidx.compose.ui.geometry.Offset
import com.shahaabapps.core.model.InkStroke
import com.shahaabapps.core.model.LessonMark
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Where a mark on a lesson lands after the lesson moves.
 *
 * This is the whole feature. A lesson is a column of text that is a
 * different length on a phone and on a tablet, a different length again
 * rotated, and a different length tomorrow if the lesson is edited. A mark
 * kept as a position on the screen is a mark that ends up under the wrong
 * paragraph the first time any of that happens, and an underline that moves
 * to the wrong sentence is worse than no underline at all.
 *
 * So the test for all of it is a round trip across a change: draw at a point
 * on one layout, scroll or re-measure, and the mark must come back on the
 * same block at the same place in it.
 */
class LessonInkAnchorTest {

    /** Three blocks down a 1080-wide screen: a title, a long paragraph, a table. */
    private val onScreenNow = listOf(
        InkedBlock("title", top = 0, height = 200),
        InkedBlock("block-0", top = 220, height = 600),
        InkedBlock("block-1", top = 840, height = 400),
    )
    private val width = 1080

    @Test
    fun `a mark names the block it was drawn on`() {
        val (key, _) = anchor(onScreenNow, width, Offset(540f, 400f))!!

        assertEquals("block-0", key)
    }

    @Test
    fun `and where in that block it is, in the block's own units`() {
        // Halfway across, and 180 of the paragraph's 600 pixels down it.
        val (_, at) = anchor(onScreenNow, width, Offset(540f, 400f))!!

        assertEquals(500f, at.x, 1f)
        assertEquals(300f, at.y, 1f)
    }

    @Test
    fun `the same mark comes back to the same place after scrolling`() {
        val (key, at) = anchor(onScreenNow, width, Offset(540f, 400f))!!

        // The lesson is scrolled up by 300 pixels. The block is in the same
        // place in the lesson and in a different place on the screen.
        val scrolled = onScreenNow.map { it.copy(top = it.top - 300) }
        val back = outOf(scrolled.first { it.key == key }, width, at)

        assertEquals(540f, back.x, 1f)
        assertEquals(100f, back.y, 1f)
    }

    @Test
    fun `and after the screen changes width`() {
        val (key, at) = anchor(onScreenNow, width, Offset(540f, 400f))!!

        // Turned sideways: wider, and the paragraph is shorter for it.
        val wide = 1920
        val rotated = listOf(InkedBlock(key, top = 100, height = 360))
        val back = outOf(rotated.first(), wide, at)

        // Still halfway across and still 30% down the paragraph — which is
        // the most that can be promised when the words themselves reflow.
        assertEquals(960f, back.x, 1f)
        assertEquals(100f + 108f, back.y, 1f)
    }

    @Test
    fun `a point in the gap between two blocks belongs to the one above`() {
        // 210 is in the 20-pixel gap under the title.
        val (key, _) = anchor(onScreenNow, width, Offset(100f, 210f))!!

        assertEquals("title", key)
    }

    @Test
    fun `a point past the last block belongs to the last block`() {
        val (key, at) = anchor(onScreenNow, width, Offset(100f, 2000f))!!

        assertEquals("block-1", key)
        // Past its foot, which is allowed: a stroke that runs off the end of
        // a block keeps going rather than being cut at the boundary.
        assertTrue("the point was clamped to $at rather than left where it was", at.y > LessonMark.SPAN)
    }

    @Test
    fun `a mark on a block that is not on screen is not drawn`() {
        val mark = LessonMark("block-9", InkStroke("C0392B", 4f, listOf(0f, 0f, 500f, 500f)))

        assertNull(onScreen(mark, onScreenNow, width))
    }

    @Test
    fun `a mark on a visible block is drawn where that block now is`() {
        val mark = LessonMark("block-1", InkStroke("C0392B", 4f, listOf(0f, 0f, 1000f, 1000f)))

        val points = onScreen(mark, onScreenNow, width)!!

        // Corner to corner of the table block: 0,840 to 1080,1240.
        assertEquals(listOf(0f, 840f, 1080f, 1240f), points)
    }

    @Test
    fun `nothing on screen means nothing to anchor to`() {
        // A lesson that has not been measured yet takes no marks rather than
        // putting them all on an imaginary first block.
        assertNull(anchor(emptyList(), width, Offset(10f, 10f)))
        assertNull(anchor(onScreenNow, 0, Offset(10f, 10f)))
    }

    @Test
    fun `a block measured as nothing does not divide by zero`() {
        val collapsed = listOf(InkedBlock("block-0", top = 0, height = 0))

        val (_, at) = anchor(collapsed, width, Offset(540f, 0f))!!

        assertTrue("an empty block gave $at", at.y.isFinite())
    }
}
