package com.shahaabapps.core.planner

import com.shahaabapps.core.model.Availability
import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.core.model.PersonRole
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class MissedDaysTest {

    // Sun-Thu 75 min study; Fri and Sat free.
    private val availability = Availability(
        owner = PersonRole.TECH,
        blocks = listOf(7, 1, 2, 3, 4).map { d -> AvailabilityBlock(id = "s$d", dayOfWeek = d, startMinutes = 975, endMinutes = 1050, kind = BlockKind.STUDY) },
    )
    private val today = LocalDate.of(2026, 10, 12) // Monday

    @Test
    fun `rest days and days before the plan are never missed`() {
        val r = MissedDays.report(today, availability, loggedDates = emptySet(), planStartIso = "2026-10-05", daysBack = 14)
        // Plan started Mon 5 Oct: Mon-Thu 5..8 and Sun 11 are study days = 5 missed; Fri 9, Sat 10 rest; today upcoming.
        assertEquals(5, r.missedCount)
        assertEquals(MissedDays.DayState.REST, r.days.first { it.date == LocalDate.of(2026, 10, 9) }.state)
        assertEquals(MissedDays.DayState.REST, r.days.first { it.date == LocalDate.of(2026, 10, 1) }.state)
        assertEquals(MissedDays.DayState.UPCOMING, r.days.last().state)
    }

    @Test
    fun `logged days count as studied and lost hours follow the template`() {
        val r = MissedDays.report(today, availability, loggedDates = setOf("2026-10-05", "2026-10-06"), planStartIso = "2026-10-05", daysBack = 14)
        assertEquals(3, r.missedCount)
        assertEquals(3 * 75 / 60.0, r.lostHours, 1e-9)
        // 3.75 h lost at 6.25 h/week = 0.89 h/day -> 5 days.
        assertEquals(5L, r.delayDays)
        assertEquals(MissedDays.DayState.LOGGED, r.days.first { it.date == LocalDate.of(2026, 10, 6) }.state)
    }

    @Test
    fun `no template means no missed days`() {
        val r = MissedDays.report(today, Availability(owner = PersonRole.TECH), emptySet(), "2026-10-01")
        assertEquals(0, r.missedCount)
        assertEquals(0L, r.delayDays)
    }
}
