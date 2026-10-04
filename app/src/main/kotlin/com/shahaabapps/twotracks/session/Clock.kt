package com.shahaabapps.twotracks.session

import com.shahaabapps.core.model.Profile
import com.shahaabapps.core.planner.Dates
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Timezone-aware "now" helpers. Each person lives in their own zone. */
object Clock {
    fun zone(profile: Profile?): ZoneId =
        runCatching { ZoneId.of(profile?.timezone ?: "UTC") }.getOrDefault(ZoneId.systemDefault())

    fun now(profile: Profile?): ZonedDateTime = ZonedDateTime.now(zone(profile))

    fun today(profile: Profile?): LocalDate = now(profile).toLocalDate()

    fun todayIso(profile: Profile?): String = Dates.format(today(profile))

    fun minutesOfDay(profile: Profile?): Int = now(profile).let { it.hour * 60 + it.minute }

    /** A moment in one zone shown on the viewer's clock, e.g. partner's class end. */
    fun convert(date: LocalDate, minutesOfDay: Int, from: ZoneId, to: ZoneId): ZonedDateTime =
        LocalDateTime.of(date, java.time.LocalTime.of(minutesOfDay / 60, minutesOfDay % 60))
            .atZone(from)
            .withZoneSameInstant(to)

    private val dayMonth = DateTimeFormatter.ofPattern("EEE d MMM", Locale.ENGLISH)
    private val longDate = DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.ENGLISH)
    private val monthYear = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH)
    private val time = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)

    fun formatDayMonth(date: LocalDate): String = date.format(dayMonth)
    fun formatLong(date: LocalDate): String = date.format(longDate)
    fun formatDate(date: LocalDate): String = date.format(monthYear)
    fun formatDate(iso: String?): String = Dates.parseOrNull(iso)?.format(monthYear) ?: "-"
    fun formatDateIso(date: LocalDate): String = Dates.format(date)
    fun formatTime(zdt: ZonedDateTime): String = zdt.format(time)
}
