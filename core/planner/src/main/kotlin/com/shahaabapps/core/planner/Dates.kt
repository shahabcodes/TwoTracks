package com.shahaabapps.core.planner

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

/** ISO yyyy-MM-dd helpers shared by the planner and the app. */
object Dates {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun parse(iso: String): LocalDate = LocalDate.parse(iso, formatter)

    fun parseOrNull(iso: String?): LocalDate? =
        iso?.let { runCatching { parse(it) }.getOrNull() }

    fun format(date: LocalDate): String = date.format(formatter)

    /** Monday of the week containing [date]. */
    fun weekStart(date: LocalDate): LocalDate =
        date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    fun daysOfWeek(weekStart: LocalDate): List<LocalDate> = (0L..6L).map { weekStart.plusDays(it) }

    fun minutesToClock(minutes: Int): String {
        val h = minutes / 60
        val m = minutes % 60
        return "%02d:%02d".format(h, m)
    }
}
