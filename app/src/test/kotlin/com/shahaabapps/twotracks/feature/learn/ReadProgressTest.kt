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
