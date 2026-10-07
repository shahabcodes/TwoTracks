package com.shahaabapps.core.planner

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

/**
 * The shared run: days when both of them studied. It cannot be kept alive by
 * one person, which is the whole point — it is the only thing in the app that
 * asks something of the pair rather than of each of them separately.
 */
class TogetherStreakTest {

    private val today = LocalDate.of(2026, 10, 7)

    @Test
    fun `a day counts only when both studied`() {
        val mine = listOf("2026-10-07", "2026-10-06", "2026-10-05")
        val theirs = listOf("2026-10-07", "2026-10-06", "2026-10-05")
        assertEquals(3, Streaks.together(mine, theirs, today))
    }

    @Test
    fun `one person studying alone does not keep the run going`() {
        val mine = listOf("2026-10-07", "2026-10-06", "2026-10-05", "2026-10-04")
        val theirs = listOf("2026-10-07", "2026-10-06", "2026-10-04")
        // They missed the 5th, so the shared run is only the 6th and 7th.
        assertEquals(2, Streaks.together(mine, theirs, today))
        // Each of them still has a longer run of their own, untouched.
        assertEquals(4, Streaks.current(mine, today))
        assertEquals(2, Streaks.current(theirs, today))
    }

    @Test
    fun `the run survives the day until the day is over`() {
        // Neither has studied today yet; yesterday they both did.
        val both = listOf("2026-10-06", "2026-10-05")
        assertEquals(2, Streaks.together(both, both, today))
    }

    @Test
    fun `a day only one of them studied is not a shared day`() {
        // She studied the 7th and 5th, he the 6th: three days of study, no shared one.
        assertEquals(0, Streaks.together(listOf("2026-10-06"), listOf("2026-10-07", "2026-10-05"), today))
    }

    @Test
    fun `two people who have never overlapped have no run`() {
        assertEquals(0, Streaks.together(listOf("2026-10-07", "2026-10-05"), listOf("2026-10-06", "2026-10-04"), today))
    }

    @Test
    fun `nobody studying is a run of nothing`() {
        assertEquals(0, Streaks.together(emptyList(), emptyList(), today))
        assertEquals(0, Streaks.together(listOf("2026-10-07"), emptyList(), today))
    }

    @Test
    fun `duplicate logs on one day do not inflate the run`() {
        // Several sessions in a day is still one day.
        val mine = listOf("2026-10-07", "2026-10-07", "2026-10-06")
        val theirs = listOf("2026-10-07", "2026-10-06", "2026-10-06", "2026-10-06")
        assertEquals(2, Streaks.together(mine, theirs, today))
    }

    @Test
    fun `an unparseable date is ignored rather than counted`() {
        val mine = listOf("2026-10-07", "not a date", "2026-10-06")
        val theirs = listOf("2026-10-07", "2026-10-06", "")
        assertEquals(2, Streaks.together(mine, theirs, today))
    }

    @Test
    fun `the longest shared run looks back over everything`() {
        val mine = listOf("2026-09-01", "2026-09-02", "2026-09-03", "2026-09-04", "2026-10-01")
        val theirs = listOf("2026-09-02", "2026-09-03", "2026-09-04", "2026-10-01")
        assertEquals(3, Streaks.longestTogether(mine, theirs))
    }

    // ------------------------------------------------------------ who owes today

    @Test
    fun `today reads as both, one or neither`() {
        val iso = "2026-10-07"
        assertEquals(Streaks.Together.BOTH, Streaks.togetherToday(listOf(iso), listOf(iso), today))
        assertEquals(Streaks.Together.ONLY_ME, Streaks.togetherToday(listOf(iso), listOf("2026-10-06"), today))
        assertEquals(Streaks.Together.ONLY_THEM, Streaks.togetherToday(listOf("2026-10-06"), listOf(iso), today))
        assertEquals(Streaks.Together.NEITHER, Streaks.togetherToday(listOf("2026-10-06"), listOf("2026-10-06"), today))
        assertEquals(Streaks.Together.NEITHER, Streaks.togetherToday(emptyList(), emptyList(), today))
    }

    @Test
    fun `yesterday's work does not count as today's`() {
        assertEquals(Streaks.Together.NEITHER, Streaks.togetherToday(listOf("2026-10-06"), listOf("2026-10-06"), today))
        // but it does keep the run alive, which is the pair of behaviours that matters
        assertEquals(1, Streaks.together(listOf("2026-10-06"), listOf("2026-10-06"), today))
    }
}
