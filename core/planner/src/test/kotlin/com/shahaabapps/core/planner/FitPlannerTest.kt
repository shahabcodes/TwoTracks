package com.shahaabapps.core.planner

import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class FitPlannerTest {

    private val today = LocalDate.of(2026, 10, 5)
    private val due = LocalDate.of(2027, 3, 31) // 177 days, ~25.3 weeks

    /** Sun-Thu 16:15-17:30 = 75 min x 5 = 6.25 h/week. */
    private val blocks = listOf(7, 1, 2, 3, 4).map { d ->
        AvailabilityBlock(id = "s$d", dayOfWeek = d, startMinutes = 16 * 60 + 15, endMinutes = 17 * 60 + 30, kind = BlockKind.STUDY)
    } + AvailabilityBlock(id = "w1", dayOfWeek = 1, startMinutes = 7 * 60, endMinutes = 15 * 60 + 30, kind = BlockKind.WORK)

    @Test
    fun `fits when demand is within capacity until the due date`() {
        val r = FitPlanner.check(today, due, otherRemainingHours = 100.0, goalRemainingHours = 40.0, blocks = blocks)
        assertTrue(r.fits)
        assertNull(r.extend)
        assertNull(r.moreHours)
        assertEquals(6.25, r.weeklyCapacityHours, 1e-9)
    }

    @Test
    fun `extend option lands on the first week that holds all the hours`() {
        // 120 + 100 = 220 h at 6.25 h/week = 35.2 -> 36 weeks after today.
        val r = FitPlanner.check(today, due, otherRemainingHours = 120.0, goalRemainingHours = 100.0, blocks = blocks)
        assertFalse(r.fits)
        val ext = assertNotNull(r.extend)
        assertEquals(today.plusWeeks(36), ext!!.newDueDate)
        assertTrue(ext.shiftDays > 0)
        assertEquals(ext.shiftDays, java.time.temporal.ChronoUnit.DAYS.between(due, ext.newDueDate))
    }

    @Test
    fun `more hours option lengthens the daily blocks in 5 minute steps`() {
        val r = FitPlanner.check(today, due, otherRemainingHours = 120.0, goalRemainingHours = 60.0, blocks = blocks)
        assertFalse(r.fits)
        val more = assertNotNull(r.moreHours)
        // need 180 h / 25.29 weeks = 7.12 h/week; gap 0.87 h = 52 min over 5 days -> 10.4 -> 15 min a day.
        assertEquals(15, more!!.extraMinutesPerDay)
        assertEquals(5, more.newBlocks.size)
        assertTrue(more.newBlocks.all { it.endMinutes == 17 * 60 + 45 })
        assertFalse(more.tooLong)
        assertEquals(1.25, more.extraWeeklyHours, 1e-9)
    }

    @Test
    fun `more hours flags days that would get too long`() {
        val r = FitPlanner.check(today, due, otherRemainingHours = 300.0, goalRemainingHours = 100.0, blocks = blocks)
        val more = assertNotNull(r.moreHours)
        assertTrue(more!!.tooLong)
    }

    @Test
    fun `no study blocks gives no options`() {
        val r = FitPlanner.check(today, due, otherRemainingHours = 10.0, goalRemainingHours = 10.0, blocks = emptyList())
        assertFalse(r.fits)
        assertNull(r.extend)
        assertNull(r.moreHours)
    }

    @Test
    fun `due date already passed still offers an extension`() {
        val r = FitPlanner.check(today, today.minusDays(3), otherRemainingHours = 5.0, goalRemainingHours = 10.0, blocks = blocks)
        assertFalse(r.fits)
        assertEquals(0.0, r.weeksLeft, 1e-9)
        assertNotNull(r.extend)
        assertNull(r.moreHours)
    }

    private fun <T> assertNotNull(value: T?): T? { org.junit.Assert.assertNotNull(value); return value }
}
