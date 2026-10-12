package com.shahaabapps.twotracks.feature.us

import com.shahaabapps.core.model.BudgetItem
import com.shahaabapps.core.model.CountryTrack
import com.shahaabapps.core.model.Deadline
import com.shahaabapps.core.model.DocStatus
import com.shahaabapps.core.model.DocumentItem
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.TestRecord
import com.shahaabapps.core.planner.MissedDays
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

/** The words Together prints. */
class TogetherWordsTest {
    private val today = LocalDate.of(2026, 10, 12)

    @Test
    fun `a zone is named by the city people say`() {
        assertEquals("Doha", cityOf("Asia/Qatar"))
        assertEquals("Karachi", cityOf("Asia/Karachi"))
        assertEquals("New York", cityOf("America/New_York"))
        assertEquals("DOHA · 09:12", place("Asia/Qatar", ZonedDateTime.of(2026, 10, 12, 9, 12, 0, 0, ZoneId.of("Asia/Qatar"))))
    }

    @Test
    fun `a long certification name is cut to its code, a plain name is kept`() {
        assertEquals("AZ-204", shortTitle("AZ-204 Azure Developer Associate"))
        assertEquals("German A1", shortTitle("German A1"))
        assertEquals("IELTS Academic", shortTitle("IELTS Academic"))
    }

    @Test
    fun `the week runs to today and marks who studied`() {
        val week = lastWeek(today, setOf("2026-10-12", "2026-10-11"), setOf("2026-10-11"))
        assertEquals(7, week.size)
        assertEquals("T", week.first().label)
        assertEquals(WeekDay("M", me = true, partner = false, isToday = true), week.last())
        assertEquals(WeekDay("S", me = true, partner = true, isToday = false), week[5])
    }

    @Test
    fun `the next date counts down`() {
        assertEquals("MARRIAGE · 112 DAYS", dateLine(Deadline(title = "Marriage", dateIso = "2027-02-01"), today))
        assertEquals("CHECKPOINT · TOMORROW", dateLine(Deadline(title = "Checkpoint: European offer?", dateIso = "2026-10-13"), today))
    }

    @Test
    fun `the lead is named from the reader's side`() {
        assertEquals(MoveRow("DE", "Germany", "Usaid", PersonRole.TECH), moveRow(CountryTrack(code = "DE", name = "Germany", lead = "You"), "Usaid", "Sanya"))
        assertEquals(MoveRow("CA", "Canada", "Sanya", PersonRole.TEACHER), moveRow(CountryTrack(code = "CA", name = "Canada", lead = "Her"), "Usaid", "Sanya"))
        assertEquals(MoveRow("AU", "Australia", "Higher scorer", null), moveRow(CountryTrack(code = "AU", name = "Australia", lead = "Higher scorer"), "Usaid", "Sanya"))
    }

    @Test
    fun `the plan rows sum up what is behind them`() {
        assertEquals("5 of 12 done", documentsLine(List(12) { i -> DocumentItem(id = "$i", status = if (i < 5) DocStatus.DONE else DocStatus.NOT_STARTED) }))
        assertEquals("USD, EUR, AUD and CAD", budgetLine(listOf("USD", "EUR", "USD", "AUD", "CAD").map { BudgetItem(currency = it) }))
        assertEquals("IELTS Academic · German · AZ-204", testsLine(listOf(TestRecord(name = "IELTS Academic", order = 1), TestRecord(name = "German (Goethe / telc)", order = 2), TestRecord(name = "AZ-204 exam", order = 3))))
    }

    @Test
    fun `missed days are only mentioned when there are some`() {
        assertNull(missedLine(MissedDays.Report(emptyList(), 0, 0.0, 0)))
        assertEquals("2 missed days · lands about 3 days later", missedLine(MissedDays.Report(emptyList(), 2, 1.5, 3)))
    }
}
