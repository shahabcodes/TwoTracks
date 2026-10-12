package com.shahaabapps.twotracks.session

import com.shahaabapps.core.model.Profile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

/**
 * Two people, two countries, one shared screen.
 *
 * Every date in this app is somebody's local date: the streak, the day a
 * session belongs to, what "today" means on the Today tab. Clock is the only
 * place that decides which, it is used from every view model, and it had no
 * tests. The failure it is there to prevent is silent — a date one day out
 * breaks a streak, and nothing says why.
 */
class ClockTest {

    private fun person(zone: String?) = Profile(uid = "u", name = "n", timezone = zone ?: "")

    @Test
    fun `each person gets their own zone`() {
        assertEquals(ZoneId.of("Asia/Qatar"), Clock.zone(person("Asia/Qatar")))
        assertEquals(ZoneId.of("Asia/Karachi"), Clock.zone(person("Asia/Karachi")))
    }

    @Test
    fun `a missing or nonsense zone falls back instead of throwing`() {
        // A profile is created before anyone picks a timezone, and the field
        // is whatever was typed. Throwing here would take the whole screen.
        //
        // The two fallbacks are not the same, which is worth writing down
        // rather than tidying: no profile at all means nobody is loaded yet,
        // and UTC is a neutral answer for a screen that is about to be
        // replaced. A profile with an unusable zone is a real person whose
        // field is wrong, and the device they are holding is the better guess.
        assertEquals(ZoneId.of("UTC"), Clock.zone(null))
        assertEquals(ZoneId.systemDefault(), Clock.zone(person("Mars/Olympus")))
        assertEquals(ZoneId.systemDefault(), Clock.zone(person("")))
    }

    @Test
    fun `a time in one zone is shown on the other person's clock`() {
        // Sanya's class ends at 14:00 in Karachi. Pakistan is UTC+5 and Qatar
        // is UTC+3, so Usaid sees 12:00, two hours earlier, on the same day.
        val karachi = ZoneId.of("Asia/Karachi")
        val doha = ZoneId.of("Asia/Qatar")
        val shown = Clock.convert(LocalDate.of(2026, 3, 10), 14 * 60, karachi, doha)
        assertEquals(12, shown.hour)
        assertEquals(0, shown.minute)
        assertEquals(LocalDate.of(2026, 3, 10), shown.toLocalDate())
        // And back the other way, which is the direction Sanya's screen uses.
        val backAgain = Clock.convert(LocalDate.of(2026, 3, 10), 12 * 60, doha, karachi)
        assertEquals(14, backAgain.hour)
    }

    @Test
    fun `a late session can land on the next day for the other person`() {
        // 23:30 in Doha is 01:30 the next morning in Karachi. A card that drew
        // the time without the date would say the session is tonight.
        val shown = Clock.convert(LocalDate.of(2026, 3, 10), 23 * 60 + 30, ZoneId.of("Asia/Qatar"), ZoneId.of("Asia/Karachi"))
        assertEquals(LocalDate.of(2026, 3, 11), shown.toLocalDate())
        assertEquals(1, shown.hour)
        assertEquals(30, shown.minute)
        // And a very early session runs the other way: 01:00 in Karachi is
        // 23:00 the evening before in Doha.
        val earlier = Clock.convert(LocalDate.of(2026, 3, 10), 60, ZoneId.of("Asia/Karachi"), ZoneId.of("Asia/Qatar"))
        assertEquals(LocalDate.of(2026, 3, 9), earlier.toLocalDate())
        assertEquals(23, earlier.hour)
    }

    @Test
    fun `a conversion across a daylight-saving change keeps the wall clock honest`() {
        // Germany moves to summer time on 29 March 2026. Qatar never does, so
        // the gap between Doha and Berlin is two hours before and one after.
        val doha = ZoneId.of("Asia/Qatar")
        val berlin = ZoneId.of("Europe/Berlin")
        val before = Clock.convert(LocalDate.of(2026, 3, 28), 12 * 60, doha, berlin)
        val after = Clock.convert(LocalDate.of(2026, 3, 30), 12 * 60, doha, berlin)
        assertEquals(10, before.hour)
        assertEquals(11, after.hour)
        assertNotEquals(before.offset, after.offset)
    }

    @Test
    fun `minutes of day is the same number the availability template uses`() {
        // Blocks are stored as minutes from local midnight, so this has to be
        // in the person's zone and not the device's.
        val minutes = Clock.minutesOfDay(person("Asia/Qatar"))
        assertTrue("saw $minutes", minutes in 0..1439)
        val sameMoment = Clock.now(person("Asia/Qatar"))
        assertEquals(sameMoment.hour * 60 + sameMoment.minute, minutes)
    }

    @Test
    fun `today follows the person, not the device`() {
        // At the moment this runs the two may be on different dates, but each
        // one's date always matches its own zone.
        listOf("Pacific/Kiritimati", "Pacific/Niue").forEach { z ->
            assertEquals(java.time.ZonedDateTime.now(ZoneId.of(z)).toLocalDate(), Clock.today(person(z)))
        }
    }

    @Test
    fun `dates are formatted in English however the device is set`() {
        // The app's text is English, and a device in another locale would
        // otherwise print a month name nothing else in the screen matches.
        val date = LocalDate.of(2026, 3, 9)
        assertEquals("Mon 9 Mar", Clock.formatDayMonth(date))
        assertEquals("Monday, 9 March", Clock.formatLong(date))
        assertEquals("9 Mar 2026", Clock.formatDate(date))
        assertEquals("2026-03-09", Clock.formatDateIso(date))
    }

    @Test
    fun `an unparseable date reads as a dash rather than today`() {
        // These come from stored strings, which may be empty on a record from
        // before the field existed. Falling back to today would be a lie.
        assertEquals("-", Clock.formatDate(null as String?))
        assertEquals("-", Clock.formatDate(""))
        assertEquals("-", Clock.formatDate("not a date"))
        assertEquals("9 Mar 2026", Clock.formatDate("2026-03-09"))
    }
}
