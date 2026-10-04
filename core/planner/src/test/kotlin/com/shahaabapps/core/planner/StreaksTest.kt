package com.shahaabapps.core.planner

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class StreaksTest {
    private val today = LocalDate.of(2026, 10, 4)

    @Test
    fun `streak counts back from today`() {
        assertEquals(3, Streaks.current(listOf("2026-10-04", "2026-10-03", "2026-10-02", "2026-09-30"), today))
    }

    @Test
    fun `streak survives a day without a log for today`() {
        assertEquals(2, Streaks.current(listOf("2026-10-03", "2026-10-02"), today))
    }

    @Test
    fun `streak is zero when the last log is two days old`() {
        assertEquals(0, Streaks.current(listOf("2026-10-02"), today))
    }

    @Test
    fun `longest streak`() {
        assertEquals(4, Streaks.longest(listOf("2026-09-01", "2026-09-02", "2026-09-03", "2026-09-04", "2026-09-10", "2026-09-11")))
    }
}
