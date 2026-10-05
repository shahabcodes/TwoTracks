package com.shahaabapps.core.planner

import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.ceil
import kotlin.math.max

/**
 * Answers "does this still fit?" when hours are added to a level, and works
 * out the two ways to make it fit: push the level's due date out, or lengthen
 * the daily study blocks.
 *
 * Demand is the remaining hours of every required goal in the level including
 * the goal being saved. Capacity is the weekly study minutes of the template
 * times the weeks left until the level's due date.
 */
object FitPlanner {

    /** A day's study should stay at or under this once a block is lengthened. */
    const val MAX_STUDY_MINUTES_PER_DAY = 150

    /** Lengthened blocks never run past this clock time. */
    const val LATEST_END_MINUTES = 23 * 60

    data class ExtendOption(
        val newDueDate: LocalDate,
        /** Days the level (and the levels after it) move. */
        val shiftDays: Long,
    )

    data class MoreHoursOption(
        val extraMinutesPerDay: Int,
        /** The template's STUDY blocks with their end times pushed out. */
        val newBlocks: List<AvailabilityBlock>,
        /** True when a lengthened day would exceed [MAX_STUDY_MINUTES_PER_DAY] or run past [LATEST_END_MINUTES]. */
        val tooLong: Boolean,
    ) {
        val extraWeeklyHours: Double get() = newBlocks.sumOf { it.durationMinutes } / 60.0 - originalWeeklyHours
        internal var originalWeeklyHours: Double = 0.0
    }

    data class FitResult(
        val fits: Boolean,
        val demandHours: Double,
        val weeksLeft: Double,
        val weeklyCapacityHours: Double,
        /** Hours a week the level needs from today to land on its due date. */
        val neededWeeklyHours: Double,
        val extend: ExtendOption?,
        val moreHours: MoreHoursOption?,
    ) {
        val gapWeeklyHours: Double get() = max(0.0, neededWeeklyHours - weeklyCapacityHours)
    }

    /**
     * @param otherRemainingHours remaining hours of the level's other required goals.
     * @param goalRemainingHours remaining hours of the goal being saved, after the edit.
     * @param blocks the person's weekly template (WORK blocks are ignored).
     */
    fun check(
        today: LocalDate,
        levelDueDate: LocalDate,
        otherRemainingHours: Double,
        goalRemainingHours: Double,
        blocks: List<AvailabilityBlock>,
    ): FitResult {
        val usable = blocks.filter { it.kind != BlockKind.WORK }
        val weeklyCapacityHours = usable.sumOf { it.durationMinutes } / 60.0
        val daysLeft = max(0L, ChronoUnit.DAYS.between(today, levelDueDate))
        val weeksLeft = daysLeft / 7.0
        val demand = max(0.0, otherRemainingHours) + max(0.0, goalRemainingHours)
        val capacityHours = weeklyCapacityHours * weeksLeft
        val neededWeekly = if (weeksLeft > 0.0) demand / weeksLeft else Double.POSITIVE_INFINITY
        val fits = demand <= capacityHours + 1e-9

        val extend = if (!fits && weeklyCapacityHours > 0.0) {
            val weeksNeeded = ceil(demand / weeklyCapacityHours).toLong()
            val newDue = today.plusWeeks(weeksNeeded)
            if (newDue.isAfter(levelDueDate)) ExtendOption(newDue, ChronoUnit.DAYS.between(levelDueDate, newDue)) else null
        } else null

        val moreHours = if (!fits && weeksLeft > 0.0) moreHours(demand / weeksLeft - weeklyCapacityHours, usable, weeklyCapacityHours) else null

        return FitResult(
            fits = fits,
            demandHours = demand,
            weeksLeft = weeksLeft,
            weeklyCapacityHours = weeklyCapacityHours,
            neededWeeklyHours = if (neededWeekly.isFinite()) neededWeekly else demand,
            extend = extend,
            moreHours = moreHours,
        )
    }

    private fun moreHours(extraWeeklyHours: Double, usable: List<AvailabilityBlock>, originalWeeklyHours: Double): MoreHoursOption? {
        val study = usable.filter { it.kind == BlockKind.STUDY }
        val days = study.map { it.dayOfWeek }.distinct()
        if (days.isEmpty() || extraWeeklyHours <= 0.0) return null
        val perDay = ceil(extraWeeklyHours * 60.0 / days.size / 5.0).toInt() * 5
        // Lengthen the last study block of each day.
        val lastPerDay = study.groupBy { it.dayOfWeek }.mapValues { (_, b) -> b.maxBy { it.endMinutes }.id }.values.toSet()
        val newBlocks = study.map { b -> if (b.id in lastPerDay) b.copy(endMinutes = b.endMinutes + perDay) else b }
        val minutesPerDay = newBlocks.groupBy { it.dayOfWeek }.mapValues { (_, b) -> b.sumOf { it.durationMinutes } }
        val tooLong = minutesPerDay.values.any { it > MAX_STUDY_MINUTES_PER_DAY } || newBlocks.any { it.endMinutes > LATEST_END_MINUTES }
        return MoreHoursOption(perDay, newBlocks, tooLong).also { it.originalWeeklyHours = originalWeeklyHours }
    }
}
