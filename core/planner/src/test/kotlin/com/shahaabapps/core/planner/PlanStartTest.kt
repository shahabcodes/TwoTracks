package com.shahaabapps.core.planner

import com.shahaabapps.core.model.Availability
import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

/**
 * Which day the plan starts counting from, and what that does to the days
 * behind it. The case this exists for is a space made in advance: the levels
 * were rebased to the day it was created, so without a date of the person's
 * own, every planned slot since then reads as missed.
 */
class PlanStartTest {

    private val levels = listOf("2026-09-01", "2026-10-01", "2026-11-01")

    @Test
    fun `the day the person named wins, even when it is later than the plan`() {
        assertEquals("2026-10-20", PlanStart.effective("2026-10-20", levels))
    }

    @Test
    fun `without one, the earliest level start is used`() {
        assertEquals("2026-09-01", PlanStart.effective(null, levels))
        assertEquals("2026-09-01", PlanStart.effective("", levels))
    }

    @Test
    fun `nothing to go on stays null, which counts nothing as missed`() {
        assertNull(PlanStart.effective(null, emptyList()))
        assertNull(PlanStart.effective(null, listOf("", "")))
    }

    @Test
    fun `days before the start are rest, not missed`() {
        // Someone who set the space up on the 1st but starts on the 8th, with
        // every weekday planned and nothing studied yet.
        val availability = Availability(
            blocks = (1..7).map {
                AvailabilityBlock(id = "b$it", dayOfWeek = it, startMinutes = 18 * 60, endMinutes = 19 * 60, kind = BlockKind.STUDY)
            },
        )
        val today = LocalDate.of(2026, 10, 10)
        val start = PlanStart.effective("2026-10-08", listOf("2026-10-01"))

        val report = MissedDays.report(today, availability, loggedDates = emptySet(), planStartIso = start, daysBack = 14)

        // Only the 8th and 9th can be held against them; the 10th is today.
        assertEquals(2, report.missedCount)
        val before = report.days.filter { it.date.isBefore(LocalDate.of(2026, 10, 8)) }
        assertEquals(emptyList<MissedDays.Day>(), before.filter { it.state == MissedDays.DayState.MISSED })

        // And without the date, the same fortnight is a wall of missed days.
        val withoutDate = MissedDays.report(today, availability, emptySet(), PlanStart.effective(null, listOf("2026-10-01")), daysBack = 14)
        assertEquals(9, withoutDate.missedCount)
    }
}
