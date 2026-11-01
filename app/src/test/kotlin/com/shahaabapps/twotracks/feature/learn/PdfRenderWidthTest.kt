package com.shahaabapps.twotracks.feature.learn

import androidx.compose.ui.unit.IntSize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The arithmetic behind a pinched PDF page.
 *
 * A pinch moves the zoom every frame, and two things must not follow it there:
 * the renderer, which would be asked for a new bitmap sixty times a second,
 * and memory, which a page rendered at whatever width the fingers reached
 * would run through in three pages. Both are plain functions, so both can be
 * held to a number here rather than judged by eye on a tablet.
 */
class PdfRenderWidthTest {

    @Test
    fun `the rendered width steps rather than following the fingers`() {
        // Anything inside a quarter step renders at the same width, so the
        // renderer is asked for a page once per step and not once per frame.
        // The step is always the one above, so the page is drawn from a
        // bitmap shrunk a little rather than one stretched a little.
        assertEquals(1f, stepped(1.0f), EPSILON)
        assertEquals(1.25f, stepped(1.1f), EPSILON)
        assertEquals(1.25f, stepped(1.2f), EPSILON)
        assertEquals(1.5f, stepped(1.26f), EPSILON)
        assertEquals(3f, stepped(3f), EPSILON)
    }

    @Test
    fun `a page is never rendered narrower than the screen`() {
        // Below 1x there is no zoom to speak of, and a page rendered at half
        // the frame would be a blurred page for no saving.
        assertEquals(RENDER_STEP, stepped(0f), EPSILON)
    }

    @Test
    fun `a tall page is rendered narrower rather than costing more`() {
        val a4 = IntSize(595, 842)
        val wide = renderWidthFor(4000f, a4)
        assertTrue("an A4 page was rendered $wide across", wide < MAX_RENDER_PX)
        val pixels = wide.toLong() * (wide * 842L / 595L)
        assertTrue("one page is $pixels pixels", pixels <= MAX_RENDER_PIXELS.toLong() + wide)
    }

    @Test
    fun `a short page is still capped by width`() {
        // A page wider than it is tall has area to spare, so the width cap is
        // what stops it: a render no screen can show is wasted either way.
        assertEquals(MAX_RENDER_PX, renderWidthFor(9000f, IntSize(1000, 100)))
    }

    @Test
    fun `the width the screen asks for is honoured when it is modest`() {
        assertEquals(1600, renderWidthFor(1600f, IntSize(595, 842)))
    }

    @Test
    fun `a page taller than the frame can be pinched down to the whole of it`() {
        // A4 across a 1280x800dp tablet on its side: the page is 1811 tall at
        // fit-width, so the whole of it is 0.44 of that.
        val a4 = IntSize(595, 842)
        assertEquals(0.44f, fitPage(a4, 1280, 800), 0.01f)
    }

    @Test
    fun `a page the frame already holds does not zoom out at all`() {
        // Upright, a page is taller than wide and the frame is too: there is
        // nothing to pull back to, and 1x stays the floor.
        assertEquals(1f, fitPage(IntSize(595, 842), 800, 1900), EPSILON)
        assertEquals(1f, fitPage(null, 800, 1900), EPSILON)
    }

    @Test
    fun `a page is not shrunk to nothing in a letterbox`() {
        // A very tall page in a very short frame: the floor holds rather than
        // handing back a grey sliver.
        assertEquals(MIN_PDF_ZOOM, fitPage(IntSize(595, 5000), 1280, 200), EPSILON)
    }

    @Test
    fun `the zoom reads as a number, not a comma`() {
        assertEquals("1.5", oneDecimal(1.5f))
        assertEquals("2.0", oneDecimal(1.97f))
        assertEquals("3.0", oneDecimal(3f))
    }

    private companion object {
        const val EPSILON = 0.0001f
    }
}
