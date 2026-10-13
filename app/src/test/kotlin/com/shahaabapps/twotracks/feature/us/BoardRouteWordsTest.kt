package com.shahaabapps.twotracks.feature.us

import com.shahaabapps.core.model.DocStatus
import com.shahaabapps.core.model.Level
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.TestRecord
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.core.planner.LevelState
import com.shahaabapps.twotracks.feature.countries.boardChance
import com.shahaabapps.twotracks.feature.countries.destinationsLine
import com.shahaabapps.twotracks.feature.documents.boardDocStatus
import com.shahaabapps.twotracks.feature.documents.documentsSummary
import com.shahaabapps.twotracks.feature.partner.bandName
import com.shahaabapps.twotracks.feature.partner.partnerLine
import com.shahaabapps.twotracks.feature.partner.partnerPlace
import com.shahaabapps.twotracks.feature.tests.boardBands
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

/** The words the Departures board prints across Route and the pages it opens. */
class BoardRouteWordsTest {
    private fun stop(state: LevelState, number: Int = 1, due: String = "2027-03-31") = LevelProgress(
        level = Level(id = "l$number", number = number, name = "Foundations", startDateIso = "2026-10-01", dueDateIso = due),
        state = state, items = emptyList(), today = LocalDate.of(2026, 10, 12),
    )

    @Test
    fun `the run counts days on both lines`() {
        assertEquals("6 DAYS RUNNING · BOTH LINES", runningLine(6))
        assertEquals("1 DAY RUNNING · BOTH LINES", runningLine(1))
        assertEquals("NO RUN YET · BOTH LINES", runningLine(0))
    }

    @Test
    fun `a stop on the two lines says where it stands`() {
        assertEquals("NOW · 0 OF 0", lineTag(stop(LevelState.OPEN)))
        assertEquals("DELAYED · 0 OF 0", lineTag(stop(LevelState.OPEN, due = "2026-09-30")))
        assertEquals("ARRIVED", lineTag(stop(LevelState.DONE)))
        assertEquals("LEVEL 3", lineTag(stop(LevelState.LOCKED, number = 3)))
    }

    @Test
    fun `a destination's chip carries the leader's initial, or nothing for a rule`() {
        assertEquals("U", leadMark(MoveRow("DE", "Germany", "Usaid", PersonRole.TECH)))
        assertEquals("S", leadMark(MoveRow("CA", "Canada", "Sanya", PersonRole.TEACHER)))
        assertEquals("", leadMark(MoveRow("AU", "Australia", "Higher scorer", null)))
    }

    @Test
    fun `the partner page names the role, the subjects and the place`() {
        val goals = listOf(GoalBar("IELTS Academic", "34/120 h", 0.28f), GoalBar("German A1", "15/150 h", 0.1f))
        assertEquals("The teacher · IELTS and German", partnerLine("THE TEACHER · KARACHI 11:12", goals))
        assertEquals("The developer", partnerLine("THE DEVELOPER · DOHA 09:12", emptyList()))
        assertEquals("KARACHI 11:12", partnerPlace("THE TEACHER · KARACHI · 11:12"))
        assertEquals("LISTEN", bandName("Listening"))
        assertEquals("SPEAK", bandName("Speaking"))
    }

    @Test
    fun `chances and the destinations line read as the board prints them`() {
        assertEquals("45–60%", boardChance("45-60%"))
        assertEquals("<10%", boardChance("Under 10% (joins on your visa)"))
        assertEquals("6 DESTINATIONS · ONE OF THEM HOME", destinationsLine(6))
    }

    @Test
    fun `a test's chip prints its bands without the word target`() {
        val ielts = TestRecord(name = "IELTS Academic", targetBands = linkedMapOf("Listening" to "8", "Reading" to "7", "Writing" to "7", "Speaking" to "8"))
        assertEquals("L8 · R7 · W7 · S8", boardBands(ielts))
        val same = TestRecord(name = "IELTS General", targetBands = linkedMapOf("Listening" to "7", "Reading" to "7", "Writing" to "7", "Speaking" to "7"))
        assertEquals("7 IN EVERY BAND", boardBands(same))
    }

    @Test
    fun `documents are not yet, in transit or cleared`() {
        assertEquals("Not yet", boardDocStatus(DocStatus.NOT_STARTED))
        assertEquals("In transit", boardDocStatus(DocStatus.IN_PROGRESS))
        assertEquals("Cleared", boardDocStatus(DocStatus.DONE))
        assertEquals("5 OF 12 CLEARED · SHOWING 6", documentsSummary(5, 12, 6))
    }
}
