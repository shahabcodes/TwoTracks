package com.shahaabapps.twotracks.feature.learn

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * The reading clock, and what it is allowed to count.
 *
 * The number it produces is shown back to the reader as what their reading
 * took and is rounded into the hours behind their course, so the one thing
 * it must never do is count time nobody was reading. A clock started when a
 * screen opens and read when it closes counts the walk to the kitchen, the
 * phone in a pocket and the night the app was left open.
 */
class ReadingClockTest {

    private var now = 1_000_000L
    private val clock = ReadingClock { now }

    private fun pass(seconds: Int) { now += seconds * 1_000L }

    @Test
    fun `an unopened lesson has taken no time`() {
        assertEquals(0, clock.seconds())
    }

    @Test
    fun `it counts while the lesson is on screen`() {
        clock.resumed()
        pass(90)

        assertEquals(90, clock.seconds())
        assertEquals(1, clock.minutes())
    }

    @Test
    fun `it counts nothing while the lesson is away`() {
        clock.resumed()
        pass(60)
        clock.paused()

        // An hour in a pocket.
        pass(3_600)

        assertEquals(60, clock.seconds())
    }

    @Test
    fun `two sittings add up, and the gap between them does not`() {
        clock.resumed(); pass(120); clock.paused()
        pass(86_400)
        clock.resumed(); pass(45)

        assertEquals(165, clock.seconds())
    }

    @Test
    fun `coming back to a lesson already on screen does not start a second clock`() {
        clock.resumed()
        pass(30)
        clock.resumed()
        pass(30)

        assertEquals(60, clock.seconds())
    }

    @Test
    fun `pausing a clock that is not running changes nothing`() {
        clock.resumed(); pass(30); clock.paused()
        clock.paused()
        clock.paused()

        assertEquals(30, clock.seconds())
    }

    @Test
    fun `the clock going backwards does not take reading away`() {
        // A phone's wall clock can be put back, by hand or by the network
        // correcting it, in the middle of a sitting. The header reads this
        // clock every second while the lesson is up, which is what the read
        // below stands for: what has been counted by then is kept.
        clock.resumed()
        pass(100)
        assertEquals(100, clock.seconds())

        now -= 500_000L

        assertEquals(100, clock.seconds())
        pass(10)
        assertEquals(110, clock.seconds())
    }

    @Test
    fun `minutes round down, the way a kitchen clock does`() {
        clock.resumed()
        pass(119)

        assertEquals(1, clock.minutes())
        assertEquals(119, clock.seconds())
    }
}

/** How a length of reading is put into words, and onto the header's clock. */
class ReadingWordsTest {

    @Test
    fun `minutes and seconds, as the reader was promised`() {
        assertEquals("20 min 30 sec", readingWords(20 * 60 + 30))
    }

    @Test
    fun `a round number of minutes does not say nought seconds`() {
        assertEquals("20 min", readingWords(1_200))
    }

    @Test
    fun `under a minute is seconds alone`() {
        assertEquals("45 sec", readingWords(45))
        assertEquals("1 sec", readingWords(1))
    }

    @Test
    fun `past an hour the seconds stop earning their place`() {
        assertEquals("1 hr 5 min", readingWords(3_900))
        assertEquals("1 hr", readingWords(3_600))
        assertEquals("2 hr 30 min", readingWords(9_000 + 1))
    }

    @Test
    fun `the line is only drawn when there is something to say`() {
        assertNull("a lesson never finished claimed a last reading", lastReadingLine(0))
        assertNull(lastReadingLine(-1))
        assertEquals("You read this in 20 min 30 sec last time.", lastReadingLine(20 * 60 + 30))
        assertNotNull(lastReadingLine(1))
    }

    @Test
    fun `the running clock is written the way a clock counting up is`() {
        assertEquals("0:00", readingClockFace(0))
        assertEquals("0:07", readingClockFace(7))
        assertEquals("4:12", readingClockFace(252))
        assertEquals("59:59", readingClockFace(3_599))
        assertEquals("1:04:12", readingClockFace(3_852))
        // Never a negative, whatever it is handed.
        assertEquals("0:00", readingClockFace(-5))
    }
}
