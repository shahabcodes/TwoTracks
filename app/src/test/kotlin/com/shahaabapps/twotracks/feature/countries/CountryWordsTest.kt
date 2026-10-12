package com.shahaabapps.twotracks.feature.countries

import com.shahaabapps.core.model.CountryTrack
import com.shahaabapps.core.model.DocumentItem
import com.shahaabapps.core.model.GoalComponent
import com.shahaabapps.core.model.GoalKind
import com.shahaabapps.core.model.MockScore
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.model.TestRecord
import com.shahaabapps.twotracks.feature.documents.docLine
import com.shahaabapps.twotracks.feature.partner.paper
import com.shahaabapps.twotracks.feature.partner.weekLine
import com.shahaabapps.twotracks.feature.tests.bandLine
import com.shahaabapps.twotracks.feature.tests.testStatus
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

/** The words the countries, tests, documents and partner pages print. */
class CountryWordsTest {

    @Test
    fun `a chance splits into its figure, what qualifies it, and a bar`() {
        assertEquals(Chance("45–60%", "65–75% with B1 and referrals", 0.525f), chance("45-60% (65-75% with B1 + referrals)"))
        assertEquals("Under 10%", chance("Under 10%").short)
        assertEquals(0.06f, chance("Under 10%").value, 0.001f)
        assertEquals(Chance("10–20%", "without French", 0.15f), chance("10-20% without French"))
        assertEquals("without French; 45–60% with French NCLC 7", chance("15-25% without French; 45-60% with French NCLC 7").rest)
    }

    @Test
    fun `each reader sees their own chance first and the lead in their words`() {
        val de = CountryTrack(code = "DE", lead = "You", chanceTech = "45-60%", chanceTeacher = "Under 10%")
        val usaid = Reader(PersonRole.TECH, "Usaid", "Sanya")
        val sanya = Reader(PersonRole.TEACHER, "Sanya", "Usaid")
        assertEquals("45-60%", myChance(de, usaid))
        assertEquals("Under 10%", myChance(de, sanya))
        assertEquals("You lead" to PersonRole.TECH, leadOf(de, usaid))
        assertEquals("Usaid leads" to PersonRole.TECH, leadOf(de, sanya))
    }

    @Test
    fun `a test says whether it is taken and against what`() {
        val ielts = TestRecord(name = "IELTS", targetBands = linkedMapOf("Listening" to "8.0", "Reading" to "7.0", "Writing" to "7.0", "Speaking" to "8.0"))
        val today = LocalDate.of(2026, 10, 12)
        assertEquals("NOT TAKEN", testStatus(ielts, today))
        assertEquals("TARGET  L 8 · R 7 · W 7 · S 8", bandLine(ielts))
        val taken = ielts.copy(actualBands = mapOf("Listening" to "8.5", "Reading" to "7.0", "Writing" to "6.5", "Speaking" to "8.0"), takenOnIso = "2026-10-04")
        assertEquals("TAKEN 4 OCT · 1 SHORT", testStatus(taken, today))
        assertEquals("L 8.5/8 · R 7/7 · W 6.5/7 · S 8/8", bandLine(taken))
        assertEquals("TARGET  7 IN EVERY BAND", bandLine(TestRecord(targetBands = listOf("L", "R", "W", "S").associateWith { "7.0" })))
        assertEquals("EXPIRED", testStatus(taken.copy(expiresOnIso = "2026-01-01"), today))
    }

    @Test
    fun `a document says where it is used unless its detail already does`() {
        assertEquals("Attested · all countries", docLine(DocumentItem(detail = "Attested", usedFor = "All countries")))
        assertEquals("All countries", docLine(DocumentItem(usedFor = "All countries")))
        assertEquals("Germany", docLine(DocumentItem(detail = "Germany", usedFor = "Germany")))
        assertEquals("Canada ECA", docLine(DocumentItem(detail = "Canada ECA", usedFor = "Canada")))
    }

    @Test
    fun `the partner's week and last paper read plainly`() {
        assertEquals("every day so far", weekLine(3, 3))
        assertEquals("2 of 5 days so far", weekLine(2, 5))
        val goal = StudyGoal(
            id = "ielts", kind = GoalKind.LANGUAGE_TEST,
            components = listOf(GoalComponent("l", "Listening", 30.0, "8.0"), GoalComponent("r", "Reading", 30.0, "7.0"), GoalComponent("w", "Writing", 30.0, "7.0")),
        )
        val p = paper(listOf(goal), listOf(MockScore(goalId = "ielts", dateIso = "2026-10-04", scores = mapOf("l" to "7.5", "r" to "7.0", "w" to "6.5"))))!!
        assertEquals("4 Oct", p.date)
        assertEquals(listOf("8", "7", "7"), p.bands.map { it.need })
        assertEquals("Listening and Writing are the gap.", p.note)
    }
}
