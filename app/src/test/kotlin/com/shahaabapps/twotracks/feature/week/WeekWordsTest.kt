package com.shahaabapps.twotracks.feature.week

import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyLog
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

/** The words and sums on Your week and Both weeks. */
class WeekWordsTest {

    @Test
    fun `a week range names the month once, or both when it crosses one`() {
        assertEquals("12 – 18 Oct", weekRange(LocalDate.of(2026, 10, 12)))
        assertEquals("28 Sep – 4 Oct", weekRange(LocalDate.of(2026, 9, 28)))
    }

    @Test
    fun `spare time reads as words`() {
        assertEquals("20 minutes", spareWords(20 / 60.0))
        assertEquals("an hour", spareWords(1.0))
        assertEquals("an hour and a half", spareWords(1.4))
        assertEquals("2.5 hours", spareWords(2.5))
    }

    @Test
    fun `short minutes`() {
        assertEquals("—", minutesShort(0))
        assertEquals("45m", minutesShort(45))
        assertEquals("2h", minutesShort(120))
        assertEquals("1h 15m", minutesShort(75))
    }

    @Test
    fun `a person's week totals their own logs by day and by goal`() {
        val days = (6 downTo 0).map { LocalDate.of(2026, 10, 12).minusDays(it.toLong()) }
        val logs = listOf(
            StudyLog(owner = PersonRole.TECH, dateIso = "2026-10-12", goalId = "az", minutes = 60),
            StudyLog(owner = PersonRole.TECH, dateIso = "2026-10-12", goalId = "de", minutes = 30),
            StudyLog(owner = PersonRole.TECH, dateIso = "2026-10-06", goalId = "az", minutes = 90),
            StudyLog(owner = PersonRole.TECH, dateIso = "2026-10-01", goalId = "az", minutes = 500),
            StudyLog(owner = PersonRole.TEACHER, dateIso = "2026-10-12", goalId = "ielts", minutes = 40),
        )
        val week = personWeek(PersonRole.TECH, "Usaid", days, logs, mapOf("az" to "AZ-204", "de" to "German"))
        assertEquals(180, week.totalMinutes)
        assertEquals(listOf(90, 0, 0, 0, 0, 0, 90), week.minutesByDay)
        assertEquals("AZ-204 2.5 · German 0.5", week.byGoal)
    }
}
