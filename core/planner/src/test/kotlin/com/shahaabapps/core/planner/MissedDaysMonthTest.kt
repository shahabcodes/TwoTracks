package com.shahaabapps.core.planner

import com.shahaabapps.core.model.Availability
import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

/**
 * One calendar month, day by day.
 *
 * The dot grid this replaced counted back twenty-eight days from today, so
 * each column held whichever weekday it happened to land on. A calendar has
 * to be anchored to the month and the weekday instead, or the one thing it is
 * for — noticing that it is always Thursdays you miss — cannot be read off it.
 */
class MissedDaysMonthTest {

    // Mondays and Thursdays only.
    private val availability = Availability(
        blocks = listOf(
            AvailabilityBlock(id = "mon", dayOfWeek = 1, startMinutes = 18 * 60, endMinutes = 19 * 60, kind = BlockKind.STUDY),
            AvailabilityBlock(id = "thu", dayOfWeek = 4, startMinutes = 18 * 60, endMinutes = 19 * 60, kind = BlockKind.STUDY),
        ),
    )
    private val october = YearMonth.of(2026, 10)
    private val today = LocalDate.of(2026, 10, 15)

    private fun month(logged: Set<String> = emptySet(), planStart: String? = null) =
        MissedDays.month(october, today, availability, logged, planStart)

    @Test
    fun `it covers the whole month, not a window ending today`() {
        val days = month()
        assertEquals(31, days.size)
        assertEquals(LocalDate.of(2026, 10, 1), days.first().date)
        assertEquals(LocalDate.of(2026, 10, 31), days.last().date)
    }

    @Test
    fun `days after today are still to come, not rest days`() {
        // The month runs past today, which the old window never did. Marking
        // them as rest would say every future Thursday is a day off.
        val days = month()
        val future = days.filter { it.date.isAfter(today) }
        assertTrue(future.isNotEmpty())
        assertTrue(future.all { it.state == MissedDays.DayState.UPCOMING })
    }

    @Test
    fun `only planned weekdays can be missed`() {
        val days = month().filter { it.date.isBefore(today) }
        val missed = days.filter { it.state == MissedDays.DayState.MISSED }
        assertTrue(missed.isNotEmpty())
        assertTrue(missed.all { it.date.dayOfWeek == DayOfWeek.MONDAY || it.date.dayOfWeek == DayOfWeek.THURSDAY })
        // Everything else before today is a rest day.
        assertTrue(days.filterNot { it.date.dayOfWeek == DayOfWeek.MONDAY || it.date.dayOfWeek == DayOfWeek.THURSDAY }
            .all { it.state == MissedDays.DayState.REST })
    }

    @Test
    fun `a day studied is a day studied, planned or not`() {
        // A Sunday is a rest day, but studying on it still counts.
        val days = month(logged = setOf("2026-10-04", "2026-10-05"))
        assertEquals(MissedDays.DayState.LOGGED, days.first { it.date.dayOfMonth == 4 }.state)
        assertEquals(MissedDays.DayState.LOGGED, days.first { it.date.dayOfMonth == 5 }.state)
    }

    @Test
    fun `nothing before the start date is held against you`() {
        val withoutStart = month().count { it.state == MissedDays.DayState.MISSED }
        val withStart = month(planStart = "2026-10-12").count { it.state == MissedDays.DayState.MISSED }
        assertTrue("a later start can only reduce the misses", withStart < withoutStart)
        assertTrue(month(planStart = "2026-10-12").none { it.date.dayOfMonth < 12 && it.state == MissedDays.DayState.MISSED })
    }

    @Test
    fun `the month lays out under the right weekdays`() {
        // What the calendar draws: blanks for the days before the 1st, so
        // every column is one weekday all the way down.
        val days = month()
        val leading = days.first().date.dayOfWeek.value - 1
        assertEquals(3, leading) // 1 Oct 2026 is a Thursday
        val cells = List<MissedDays.Day?>(leading) { null } + days
        cells.chunked(7).forEach { week ->
            week.forEachIndexed { column, day ->
                if (day != null) assertEquals(column + 1, day.date.dayOfWeek.value)
            }
        }
    }
}
