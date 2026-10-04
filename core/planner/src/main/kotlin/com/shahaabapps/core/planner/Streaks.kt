package com.shahaabapps.core.planner

import java.time.LocalDate

object Streaks {

    /**
     * Consecutive days with at least one study log, counted backwards from today.
     * A missing entry for today does not break the streak if yesterday has one,
     * so the streak survives until the end of the day.
     */
    fun current(logDatesIso: Collection<String>, today: LocalDate): Int {
        val dates = logDatesIso.mapNotNull { Dates.parseOrNull(it) }.toHashSet()
        if (dates.isEmpty()) return 0
        var cursor = if (today in dates) today else today.minusDays(1)
        var streak = 0
        while (cursor in dates) {
            streak++
            cursor = cursor.minusDays(1)
        }
        return streak
    }

    fun longest(logDatesIso: Collection<String>): Int {
        val dates = logDatesIso.mapNotNull { Dates.parseOrNull(it) }.toSortedSet()
        var best = 0
        var run = 0
        var prev: LocalDate? = null
        for (d in dates) {
            run = if (prev != null && prev.plusDays(1) == d) run + 1 else 1
            best = maxOf(best, run)
            prev = d
        }
        return best
    }
}
