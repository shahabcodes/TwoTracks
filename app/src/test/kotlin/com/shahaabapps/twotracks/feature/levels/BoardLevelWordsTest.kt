package com.shahaabapps.twotracks.feature.levels

import com.shahaabapps.core.model.Level
import com.shahaabapps.core.model.LevelItem
import com.shahaabapps.core.model.LevelItemKind
import com.shahaabapps.core.planner.LevelItemProgress
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.core.planner.LevelState
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

/** The words the Departures board prints on the levels and a level. */
class BoardLevelWordsTest {
    private val today = LocalDate.of(2026, 10, 12)

    private fun progress(state: LevelState, due: String, done: Int = 0, total: Int = 0, number: Int = 1) = LevelProgress(
        level = Level(id = "l$number", number = number, name = "Foundations", startDateIso = "2026-10-01", dueDateIso = due),
        state = state,
        items = List(total) { i -> LevelItemProgress(LevelItem(id = "t$i", kind = LevelItemKind.TASK, title = "t$i"), done = i < done, fraction = null, weekCount = null) },
        today = today,
    )

    @Test
    fun `a stop is boarding, delayed, arrived or scheduled`() {
        assertEquals("NOW · 2 OF 5", boardLevelState(progress(LevelState.OPEN, "2027-03-31", done = 2, total = 5)))
        assertEquals("DELAYED · 1 OF 3", boardLevelState(progress(LevelState.OPEN, "2026-09-30", done = 1, total = 3)))
        assertEquals("ARRIVED", boardLevelState(progress(LevelState.DONE, "2027-03-31")))
        assertEquals("SCHEDULED", boardLevelState(progress(LevelState.LOCKED, "2027-06-30")))
    }

    @Test
    fun `the stop line numbers the stop and says whose it is`() {
        assertEquals("STOP 01 · NOW BOARDING", boardStopLine(progress(LevelState.OPEN, "2027-03-31"), owner = null))
        assertEquals("STOP 02 · SCHEDULED", boardStopLine(progress(LevelState.LOCKED, "2027-06-30", number = 2), owner = null))
        assertEquals("SANYA'S STOP 01 · DELAYED", boardStopLine(progress(LevelState.OPEN, "2026-09-30"), owner = "Sanya"))
    }

    @Test
    fun `a tab names the person and their line`() {
        assertEquals("Usaid · Dev", whoseLine("Usaid", isTech = true))
        assertEquals("Sanya · Edu", whoseLine("Sanya", isTech = false))
    }

    @Test
    fun `a goal's chip carries the exam's prefix or the language's code`() {
        assertEquals("AZ", goalCode("AZ-204 passed"))
        assertEquals("DE", goalCode("German A1"))
        assertEquals("IE", goalCode("IELTS Academic 7"))
        assertEquals("PS", goalCode("PSM I (optional)"))
        assertEquals("GO", goalCode("  "))
    }
}
