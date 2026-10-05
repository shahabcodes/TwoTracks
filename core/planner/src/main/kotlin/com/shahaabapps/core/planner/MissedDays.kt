package com.shahaabapps.core.planner

import com.shahaabapps.core.model.Availability
import com.shahaabapps.core.model.BlockKind
import java.time.LocalDate
import kotlin.math.ceil

/**
 * Looks back over recent days and marks each one: studied, missed (a planned
 * study day with nothing logged), a rest day, or still to come. Rest days are
 * the weekdays with no study or class block in the template, so a free Friday
 * is never a missed day. Days before the plan started are rest days too.
 */
object MissedDays {

    enum class DayState { LOGGED, MISSED, REST, UPCOMING }

    data class Day(val date: LocalDate, val state: DayState, val plannedMinutes: Int)

    data class Report(
        val days: List<Day>,
        val missedCount: Int,
        /** Study time the template had set aside on the missed days. */
        val lostHours: Double,
        /** Calendar days the plan slips at the template's weekly pace. */
        val delayDays: Long,
    )

    fun report(
        today: LocalDate,
        availability: Availability,
        loggedDates: Set<String>,
        planStartIso: String?,
        daysBack: Int = 28,
    ): Report {
        val planStart = Dates.parseOrNull(planStartIso)
        val minutesByWeekday = availability.blocks
            .filter { it.kind != BlockKind.WORK }
            .groupBy { it.dayOfWeek }
            .mapValues { (_, b) -> b.sumOf { it.durationMinutes } }
        val days = ((daysBack - 1).toLong() downTo 0L).map { off ->
            val date = today.minusDays(off)
            val planned = minutesByWeekday[date.dayOfWeek.value] ?: 0
            val logged = Dates.format(date) in loggedDates
            val state = when {
                logged -> DayState.LOGGED
                !date.isBefore(today) -> DayState.UPCOMING
                planned <= 0 -> DayState.REST
                planStart != null && date.isBefore(planStart) -> DayState.REST
                else -> DayState.MISSED
            }
            Day(date, state, planned)
        }
        val missed = days.filter { it.state == DayState.MISSED }
        val lostHours = missed.sumOf { it.plannedMinutes } / 60.0
        val weeklyHours = minutesByWeekday.values.sum() / 60.0
        val delay = if (weeklyHours <= 0.0 || lostHours <= 0.0) 0L else ceil(lostHours / (weeklyHours / 7.0)).toLong()
        return Report(days, missed.size, lostHours, delay)
    }
}
