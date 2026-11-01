package com.shahaabapps.twotracks.feature.learn

import androidx.compose.ui.graphics.Color
import com.shahaabapps.core.model.InkStroke
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The three decisions the pen makes that are not drawing.
 *
 * What the eraser takes, how hard a line pressed harder comes out, and what
 * a colour that is not a colour becomes. None of them needs a screen, and
 * all three are the kind of thing that is wrong by a factor rather than
 * visibly: a stroke that survives the eraser, a nib that blots, a note that
 * loads as nothing because one field was saved oddly.
 */
class InkCanvasTest {

    private fun stroke(vararg points: Float) = InkStroke("1C1C20", 3f, points.toList())

    @Test
    fun `the eraser takes a stroke it passed through`() {
        val written = stroke(100f, 100f, 200f, 100f)
        val rubbed = listOf(150f, 104f)

        assertTrue("the eraser went over the stroke and left it", near(written, rubbed, ERASER_REACH))
    }

    @Test
    fun `the eraser leaves a stroke it only passed near`() {
        val written = stroke(100f, 100f, 200f, 100f)
        // 40 page units away, which is about 8mm on a phone: close, and not
        // what anybody meant to rub out.
        val past = listOf(150f, 140f)

        assertFalse("a stroke was taken from under the eraser", near(written, past, ERASER_REACH))
    }

    @Test
    fun `the eraser reaches as far as it says and no further`() {
        val written = stroke(0f, 0f)

        assertTrue(near(written, listOf(0f, ERASER_REACH - 0.1f), ERASER_REACH))
        assertFalse(near(written, listOf(0f, ERASER_REACH + 0.1f), ERASER_REACH))
    }

    @Test
    fun `an empty page gives the eraser nothing to take`() {
        assertFalse(near(stroke(), listOf(0f, 0f), ERASER_REACH))
        assertFalse(near(stroke(10f, 10f), emptyList(), ERASER_REACH))
    }

    @Test
    fun `a pen that reports no pressure writes at the width it is set to`() {
        // A device without a pressure-sensitive digitiser reports 1.0, and
        // that has to land exactly on the chosen nib rather than near it.
        assertEquals(3.2f, pressed(3.2f, 1f), 0.0001f)
    }

    @Test
    fun `pressing harder writes a heavier line, within reason`() {
        assertTrue("pressing harder did not thicken the line", pressed(3f, 1.4f) > pressed(3f, 1f))
        assertTrue("pressing lightly did not thin the line", pressed(3f, 0.7f) < pressed(3f, 1f))
    }

    @Test
    fun `a blot and a line of nothing are both out of reach`() {
        // Unbounded pressure turns a hard full stop into a blot and a light
        // one into nothing; both are worse than a line of even weight.
        assertEquals(3f * 1.5f, pressed(3f, 9f), 0.0001f)
        assertEquals(3f * 0.66f, pressed(3f, 0f), 0.0001f)
    }

    @Test
    fun `a colour reads the same with or without its hash`() {
        assertEquals(Color(0xFFC0392B), colourOf("C0392B"))
        assertEquals(Color(0xFFC0392B), colourOf("#C0392B"))
    }

    @Test
    fun `a colour that is not one comes back as ink, not as nothing`() {
        // Invisible handwriting would read as a note that failed to load,
        // and the strokes are still all there.
        assertEquals(Color(0xFF1C1C20), colourOf("not a colour"))
        assertEquals(Color(0xFF1C1C20), colourOf(""))
    }
}
