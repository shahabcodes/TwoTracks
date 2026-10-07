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

    /** How a pair stands today: who has studied and who has not. */
    enum class Together { NEITHER, ONLY_ME, ONLY_THEM, BOTH }

    /**
     * Days in a row when *both* people studied — not one or the other, both.
     *
     * A day only counts when each of them logged something, which is what
     * makes it a shared run rather than two private ones: it cannot be kept
     * alive alone, and that is the whole point of it.
     *
     * Today's absence is forgiven exactly as it is for one person: the day is
     * not over, so a pair who both studied yesterday still have their run.
     */
    fun together(mine: Collection<String>, theirs: Collection<String>, today: LocalDate): Int =
        current(bothStudied(mine, theirs), today)

    fun longestTogether(mine: Collection<String>, theirs: Collection<String>): Int =
        longest(bothStudied(mine, theirs))

    /** Who still owes today, which is the line worth showing before the count. */
    fun togetherToday(mine: Collection<String>, theirs: Collection<String>, today: LocalDate): Together {
        val iso = Dates.format(today)
        val meDone = iso in mine
        val themDone = iso in theirs
        return when {
            meDone && themDone -> Together.BOTH
            meDone -> Together.ONLY_ME
            themDone -> Together.ONLY_THEM
            else -> Together.NEITHER
        }
    }

    /** The dates both of them studied on, as ISO strings. */
    private fun bothStudied(mine: Collection<String>, theirs: Collection<String>): Set<String> {
        val a = mine.mapNotNull { Dates.parseOrNull(it) }.toHashSet()
        val b = theirs.mapNotNull { Dates.parseOrNull(it) }.toHashSet()
        a.retainAll(b)
        return a.map { Dates.format(it) }.toSet()
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
