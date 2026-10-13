package com.shahaabapps.twotracks.feature.today

import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.twotracks.feature.week.boardMinutes
import com.shahaabapps.twotracks.feature.week.signedHours
import org.junit.Assert.assertEquals
import org.junit.Test

/** The words the Departures board prints on Today and the week. */
class BoardTodayWordsTest {
    private fun row(status: SessionStatus) = SessionRow(PlannedSession(id = "s", status = status), goal = null, componentName = "Compute", timeLabel = "19:00 to 20:00")

    @Test
    fun `a session departs, boards, runs on time or is cancelled`() {
        assertEquals(Departure.DEPARTED, departureOf(row(SessionStatus.DONE), isNext = false))
        assertEquals(Departure.CANCELLED, departureOf(row(SessionStatus.SKIPPED), isNext = false))
        assertEquals(Departure.BOARDING, departureOf(row(SessionStatus.PLANNED), isNext = true))
        assertEquals(Departure.ON_TIME, departureOf(row(SessionStatus.PLANNED), isNext = false))
    }

    @Test
    fun `the count says how much of the board is yours`() {
        assertEquals("Two of today's four are yours.", boardCountLine(2, 4))
        assertEquals("One of today's three is yours.", boardCountLine(1, 3))
        assertEquals("All of today's two are yours.", boardCountLine(2, 2))
        assertEquals("Nothing on the board today.", boardCountLine(0, 0))
    }

    @Test
    fun `minutes and margins read as the board prints them`() {
        assertEquals("1h15", boardMinutes(75))
        assertEquals("2h", boardMinutes(120))
        assertEquals("45m", boardMinutes(45))
        assertEquals("—", boardMinutes(0))
        assertEquals("+1.4h", signedHours(1.4))
        assertEquals("-2h", signedHours(-2.0))
    }
}
