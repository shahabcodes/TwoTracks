package com.shahaabapps.twotracks.feature.today

import org.junit.Assert.assertEquals
import org.junit.Test

/** The line under Today's greeting. */
class TodayLineTest {

    @Test
    fun `says what is left for each of you`() =
        assertEquals("One session left for you today. Sanya has two.", todayLine(1, 2, "Sanya"))

    @Test
    fun `says when either of you is done`() {
        assertEquals("Nothing left for you today. Sanya is done.", todayLine(0, 0, "Sanya"))
        assertEquals("Three sessions left for you today. Usaid has one.", todayLine(3, 1, "Usaid"))
    }

    @Test
    fun `leaves the partner out until they have joined`() =
        assertEquals("Two sessions left for you today.", todayLine(2, 0, null))
}
