package com.shahaabapps.twotracks.feature.levels

import com.shahaabapps.core.model.Level
import com.shahaabapps.core.model.LevelItem
import com.shahaabapps.core.model.LevelItemKind
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.LevelItemProgress
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.core.planner.LevelState
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

/** The words the ladder and a level page print. */
class LevelWordsTest {
    private val today = LocalDate.of(2026, 10, 12)

    private fun progress(state: LevelState, start: String, due: String, done: Int = 0, total: Int = 0, number: Int = 1) = LevelProgress(
        level = Level(id = "l$number", number = number, name = "Foundations", startDateIso = start, dueDateIso = due),
        state = state,
        items = List(total) { i -> LevelItemProgress(LevelItem(id = "t$i", kind = LevelItemKind.TASK, title = "t$i"), done = i < done, fraction = null, weekCount = null) },
        today = today,
    )

    @Test
    fun `an open level says now and how many are done`() =
        assertEquals("NOW · 2 OF 5", levelState(progress(LevelState.OPEN, "2026-10-01", "2027-03-31", done = 2, total = 5)))

    @Test
    fun `an open level past its date says so`() =
        assertEquals("OVERDUE · 1 OF 3", levelState(progress(LevelState.OPEN, "2026-07-01", "2026-09-30", done = 1, total = 3)))

    @Test
    fun `a later level says later and when it opens`() {
        val p = progress(LevelState.LOCKED, "2027-01-01", "2027-03-31")
        assertEquals("LATER", levelState(p))
        assertEquals("OPENS 1 JAN 2027", levelDates(p, last = false))
    }

    @Test
    fun `a span within one year names the year once`() {
        assertEquals("1 OCT – 31 DEC 2026", levelDates(progress(LevelState.OPEN, "2026-10-01", "2026-12-31"), last = false))
        assertEquals("1 OCT 2026 – 31 MAR 2027", levelDates(progress(LevelState.OPEN, "2026-10-01", "2027-03-31"), last = false))
    }

    @Test
    fun `the checkpoint gives the date it has to be reached by`() =
        assertEquals("BY 31 DEC 2027", levelDates(progress(LevelState.LOCKED, "2027-10-01", "2027-12-31", number = 5), last = true))

    @Test
    fun `the eyebrow names whose level it is when it is not yours`() {
        val p = progress(LevelState.OPEN, "2026-10-01", "2027-03-31")
        assertEquals("Level 1 · now", levelEyebrow(p, null))
        assertEquals("Sanya's level 1 · now", levelEyebrow(p, "Sanya"))
    }

    @Test
    fun `a goal row gives hours and its date, an optional one says so first`() {
        val goal = StudyGoal(id = "az", title = "AZ-204", targetDateIso = "2027-03-31")
        val state = LevelUiState(goalsById = mapOf("az" to goal), goalHours = mapOf("az" to (12 to 120)))
        val required = LevelItemProgress(LevelItem(id = "i", kind = LevelItemKind.GOAL, title = "AZ-204 passed", goalId = "az"), done = false, fraction = 0.1, weekCount = null)
        assertEquals("Goal · 12 of 120 h · by 31 Mar 2027", itemLine(required, state))
        val optional = required.copy(item = required.item.copy(required = false, detail = "Two weeks of evenings"))
        assertEquals("Optional · 12 of 120 h · by 31 Mar 2027 · Two weeks of evenings", itemLine(optional, state))
    }
}
