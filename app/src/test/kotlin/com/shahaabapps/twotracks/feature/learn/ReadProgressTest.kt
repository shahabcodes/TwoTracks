package com.shahaabapps.twotracks.feature.learn

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The percentage of a lesson read.
 *
 * Only the blocks on screen have ever been measured, so the number is an
 * estimate of a length nobody knows. What has to be true of it is small and
 * firm: it starts at nothing, it ends at everything, it never goes backwards
 * while the reader goes forwards, and it is never a figure that would make
 * somebody scroll looking for text that is not there.
 */
class ReadProgressTest {

    /** A twenty-block lesson, four blocks of 300 on a 1200-tall screen. */
    private fun at(firstIndex: Int, scrolledInto: Int, total: Int = 20): Int {
        val pitch = 300
        val visible = (0..4).map { Seen(firstIndex + it, it * pitch - scrolledInto, pitch) }
        return readPercent(total, visible, viewportStart = 0, viewportEnd = 1200)
    }

    @Test
    fun `an unscrolled lesson is nothing read`() {
        assertEquals(0, at(firstIndex = 0, scrolledInto = 0))
    }

    @Test
    fun `the foot of the lesson is all of it read`() {
        // The last block is fully on screen, so there is nothing below it.
        val visible = listOf(
            Seen(17, 0, 300),
            Seen(18, 310, 300),
            Seen(19, 620, 300),
        )

        assertEquals(100, readPercent(20, visible, viewportStart = 0, viewportEnd = 1200))
    }

    @Test
    fun `the middle of the lesson is about the middle`() {
        // Ten blocks of twenty are above the top of the screen, and a screen
        // holds four of them: 3000 of the 4800 that can be scrolled.
        val percent = at(firstIndex = 10, scrolledInto = 0)

        assertTrue("the middle of the lesson read as $percent%", percent in 58..68)
    }

    @Test
    fun `it never goes backwards as the reader goes forwards`() {
        var last = -1
        for (index in 0..15) {
            for (into in 0 until 300 step 100) {
                val percent = at(index, into)
                assertTrue("$percent% at block $index + $into came after $last%", percent >= last)
                last = percent
            }
        }
        assertTrue("the end of the walk only reached $last%", last > 90)
    }

    @Test
    fun `a lesson that fits on one screen has nothing left to read`() {
        val visible = listOf(Seen(0, 0, 400), Seen(1, 410, 300))

        assertEquals(100, readPercent(2, visible, viewportStart = 0, viewportEnd = 1200))
    }

    @Test
    fun `one tall block on screen still gives a number`() {
        // A single table taller than the screen: there is no pitch between
        // two tops to take, so its own height has to stand in for one.
        val visible = listOf(Seen(2, -500, 2000))

        val percent = readPercent(10, visible, viewportStart = 0, viewportEnd = 1200)

        assertTrue("one tall block gave $percent%", percent in 1..99)
    }

    @Test
    fun `a lesson not yet measured reads as nothing rather than failing`() {
        assertEquals(0, readPercent(0, emptyList(), 0, 1200))
        assertEquals(0, readPercent(20, emptyList(), 0, 1200))
        // A viewport of no height happens for a frame on rotation.
        assertEquals(0, readPercent(20, listOf(Seen(0, 0, 300)), 0, 0))
    }

    @Test
    fun `blocks measured as nothing do not divide by zero`() {
        val visible = listOf(Seen(0, 0, 0), Seen(1, 0, 0))

        assertEquals(0, readPercent(20, visible, viewportStart = 0, viewportEnd = 1200))
    }

    @Test
    fun `the number is never above a hundred or below nothing`() {
        // Overscrolled at the top, which a fling can reach for a frame.
        assertEquals(0, readPercent(20, listOf(Seen(0, 120, 300), Seen(1, 430, 300)), 0, 1200))
        // And past the foot, bounced.
        val past = readPercent(20, listOf(Seen(18, -400, 300), Seen(19, -90, 300)), 0, 1200)
        assertEquals(100, past)
    }
}

/**
 * The rail: the thumb's length and where it rides.
 *
 * The shape has to say two things at once — where in the lesson this screen
 * is, and how much of it is behind the reader — and it only says them if the
 * thumb's foot lands exactly where the read fraction does at both ends of
 * the rail.
 */
class ReadRailTest {

    private val rail = 1_000f
    private val minThumb = 40f

    @Test
    fun `the thumb is as much of the rail as the screen is of the lesson`() {
        val quarter = railFor(rail, share = 0.25f, read = 0f, minThumb = minThumb)

        assertEquals(250f, quarter.thumbLength, 0.5f)
    }

    @Test
    fun `an unscrolled lesson has the thumb at the top`() {
        val at = railFor(rail, share = 0.25f, read = 0f, minThumb = minThumb)

        assertEquals(0f, at.thumbTop, 0.01f)
    }

    @Test
    fun `a finished lesson has the thumb resting on the foot of the rail`() {
        // The whole promise of the shape: everything above the thumb is read,
        // so at the end there is nothing of the rail left unfilled.
        val at = railFor(rail, share = 0.25f, read = 1f, minThumb = minThumb)

        assertEquals(rail, at.thumbTop + at.thumbLength, 0.01f)
    }

    @Test
    fun `the thumb never gets too small to see`() {
        // A forty-screen lesson would otherwise give it two pixels.
        val at = railFor(rail, share = 0.002f, read = 0.5f, minThumb = minThumb)

        assertEquals(minThumb, at.thumbLength, 0.01f)
        // And it still has the whole rail to travel.
        assertEquals((rail - minThumb) / 2f, at.thumbTop, 0.5f)
    }

    @Test
    fun `a lesson on one screen fills the rail`() {
        val at = railFor(rail, share = 1f, read = 0f, minThumb = minThumb)

        assertEquals(rail, at.thumbLength, 0.01f)
        assertEquals(0f, at.thumbTop, 0.01f)
    }

    @Test
    fun `a rail with no height, and figures off the end of it, stay on the rail`() {
        // A frame on rotation, and overscroll either way.
        assertEquals(Rail(0f, 0f), railFor(0f, share = 0.5f, read = 0.5f, minThumb = minThumb))
        val under = railFor(rail, share = 0.25f, read = -0.4f, minThumb = minThumb)
        val over = railFor(rail, share = 0.25f, read = 1.4f, minThumb = minThumb)
        assertEquals(0f, under.thumbTop, 0.01f)
        assertEquals(rail, over.thumbTop + over.thumbLength, 0.01f)
        // A thumb longer than the rail it is on is not drawn.
        val wide = railFor(rail, share = 4f, read = 0.5f, minThumb = 4_000f)
        assertEquals(rail, wide.thumbLength, 0.01f)
    }

    @Test
    fun `the thumb moves down the rail as the reader moves down the lesson`() {
        var last = -1f
        for (step in 0..20) {
            val at = railFor(rail, share = 0.25f, read = step / 20f, minThumb = minThumb)
            assertTrue("the thumb went back up at $step", at.thumbTop >= last)
            last = at.thumbTop
        }
    }

    @Test
    fun `a screenful of a twenty block lesson is a fifth of the rail`() {
        // Four blocks of 300 on a 1200-tall screen: 1200 of 6000.
        val visible = (0..4).map { Seen(it, it * 300, 300) }

        assertEquals(0.2f, viewportShare(20, visible, 0, 1200), 0.01f)
    }

    @Test
    fun `a lesson with nothing to scroll gives the whole rail to the thumb`() {
        // Two short blocks, a tall screen; and the frames where nothing has
        // been measured yet, where a stub of a thumb would be a lie about a
        // length nobody knows.
        assertEquals(1f, viewportShare(2, listOf(Seen(0, 0, 400), Seen(1, 410, 300)), 0, 1200), 0.01f)
        assertEquals(1f, viewportShare(20, emptyList(), 0, 1200), 0.01f)
        assertEquals(1f, viewportShare(0, emptyList(), 0, 1200), 0.01f)
        assertEquals(1f, viewportShare(20, listOf(Seen(0, 0, 300)), 0, 0), 0.01f)
        assertEquals(1f, viewportShare(20, listOf(Seen(0, 0, 0), Seen(1, 0, 0)), 0, 1200), 0.01f)
    }

    @Test
    fun `one tall block gives a thumb shorter than the rail`() {
        // A single table taller than the screen.
        val share = viewportShare(10, listOf(Seen(2, -500, 2000)), 0, 1200)

        assertTrue("one tall block gave a share of $share", share > 0f && share < 1f)
    }
}
