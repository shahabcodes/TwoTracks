package com.shahaabapps.twotracks.feature.learn

import androidx.compose.ui.unit.IntSize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The arithmetic that keeps a pinched page under the fingers.
 *
 * This is the whole of the bug it was written for: zooming re-measures the
 * column from its own top left, so without moving the scroll to match, the
 * page crawls away towards that corner however carefully the fingers pinch.
 * The fix is a round trip — screen point to a place on a page, zoom, that
 * place back to a screen point — and a round trip is a thing a test can
 * check exactly, which no amount of pinching an emulator can.
 */
class PageRulerTest {

    /** Three A4 pages, 1000px wide at 1x, 20px apart: each one 1415 tall. */
    private val ruler = PageRuler(List(3) { IntSize(595, 842) }, frameWidth = 1000, gap = 20)

    private val pageHeight = 1000f * 842 / 595

    @Test
    fun `a page is as tall as its own proportions, and grows with the zoom`() {
        assertEquals(pageHeight, ruler.pageHeight(0, 1f), 0.5f)
        assertEquals(pageHeight * 2, ruler.pageHeight(0, 2f), 0.5f)
    }

    @Test
    fun `the second page starts a page and a gap down the column`() {
        assertEquals(pageHeight + 20f, ruler.offsetOf(1, 0f, 1f), 0.5f)
        assertEquals((pageHeight + 20f) * 2 + 100f, ruler.offsetOf(2, 100f, 1f), 1f)
    }

    @Test
    fun `a point down the column names the page it is on`() {
        val (page, within) = ruler.locate(pageHeight + 20f + 300f, 1f)
        assertEquals(1, page)
        assertEquals(300f, within, 1f)
    }

    @Test
    fun `offsetOf and locate are each other's reverse`() {
        val there = ruler.offsetOf(2, 450f, 1.75f)
        val (page, within) = ruler.locate(there, 1.75f)
        assertEquals(2, page)
        assertEquals(450f, within, 1f)
    }

    @Test
    fun `zooming in holds the point under the fingers where it was`() {
        // Halfway down the first page, with a finger halfway down a 2000px
        // screen, zooming from 1x to 2x.
        val (page, offset) = ruler.anchor(index = 0, offset = 0, focus = 1000f, from = 1f, to = 2f)

        // What was 1000px into the column is now 2000px into it, and the
        // finger is still 1000px down the screen, so the column must start
        // 1000px in.
        assertEquals(0, page)
        assertEquals(1000f, offset.toFloat(), 2f)
    }

    @Test
    fun `zooming out holds it too, and crosses back over a page boundary`() {
        // The screen starts 400px into page 2 at 2x; the finger is at the very
        // top of the screen, so the point it is on is that same place. At 1x
        // everything above it halves, so the column has to start higher up —
        // which, for the top of page 2, means page 1 is back in view.
        val (page, offset) = ruler.anchor(index = 1, offset = 400, focus = 0f, from = 2f, to = 1f)
        val landed = ruler.offsetOf(page, offset.toFloat(), 1f)

        // The point was 200px into page 1 (400px at 2x), and it is where the
        // screen now starts.
        assertEquals(ruler.offsetOf(1, 200f, 1f), landed, 2f)
    }

    @Test
    fun `a zoom at the very top of the first page does not scroll above it`() {
        val (page, offset) = ruler.anchor(index = 0, offset = 0, focus = 0f, from = 1f, to = 3f)

        assertEquals(0, page)
        assertEquals(0, offset)
    }

    @Test
    fun `the round trip holds for a point deep in the document`() {
        // Page 3, a third of the way down, with the finger low on the screen.
        val before = ruler.offsetOf(2, pageHeight / 3, 1f)
        val focus = 1400f
        val (page, offset) = ruler.anchor(
            index = ruler.locate(before - focus, 1f).first,
            offset = ruler.locate(before - focus, 1f).second.toInt(),
            focus = focus, from = 1f, to = 2.5f,
        )
        val after = ruler.offsetOf(page, offset.toFloat(), 2.5f) + focus

        // The same place on the same page, now at 2.5x — within a few
        // pixels, because a column's scroll position is a whole number of
        // them and the fraction thrown away there is what the zoom multiplies.
        val (landedPage, landedWithin) = ruler.locate(after, 2.5f)
        assertEquals(2, landedPage)
        assertEquals(pageHeight / 3 * 2.5f, landedWithin, 5f)
    }

    @Test
    fun `an empty document answers rather than throwing`() {
        val none = PageRuler(emptyList(), frameWidth = 1000, gap = 20)

        assertEquals(0, none.pageCount)
        assertEquals(0f, none.pageHeight(0, 2f), 0f)
        assertTrue("an empty document located somewhere odd", none.locate(500f, 1f) == 0 to 0f)
    }
}
