package com.shahaabapps.core.planner

import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.QuizAttempt
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class QuestionSrsTest {

    private val me = PersonRole.TECH
    private val known = setOf("q1", "q2", "q3")

    private fun sitting(
        date: String,
        asked: List<String>,
        missed: List<String> = emptyList(),
        at: Long = 0,
        owner: PersonRole = me,
        courseId: String = "az-204",
    ) = QuizAttempt(
        id = "$date-$at", owner = owner, courseId = courseId, moduleId = "m1", dateIso = date,
        score = asked.size - missed.size, total = asked.size, passed = missed.isEmpty(),
        missed = missed, asked = asked, at = at,
    )

    private fun states(vararg attempts: QuizAttempt) = QuestionSrs.states(attempts.toList(), me, "az-204", known)

    @Test
    fun `a right answer schedules the question a day out, the next one three days out`() {
        val one = states(sitting("2026-10-01", listOf("q1")))
        assertEquals("2026-10-02", one.getValue("q1").dueIso)
        assertEquals(1, one.getValue("q1").step)

        val two = states(sitting("2026-10-01", listOf("q1")), sitting("2026-10-02", listOf("q1")))
        assertEquals("2026-10-05", two.getValue("q1").dueIso)
        assertEquals(2, two.getValue("q1").step)
        assertEquals(2, two.getValue("q1").correct)
    }

    @Test
    fun `a miss drops the question back to tomorrow however high it had climbed`() {
        val s = states(
            sitting("2026-10-01", listOf("q1")),
            sitting("2026-10-02", listOf("q1")),
            sitting("2026-10-05", listOf("q1"), missed = listOf("q1")),
        )
        assertEquals("2026-10-06", s.getValue("q1").dueIso)
        assertEquals(0, s.getValue("q1").step)
        assertEquals(1, s.getValue("q1").misses)
    }

    @Test
    fun `sittings are replayed in order even when they arrive out of order`() {
        val later = sitting("2026-10-02", listOf("q1"))
        val earlier = sitting("2026-10-01", listOf("q1"))
        assertEquals(states(earlier, later).getValue("q1"), states(later, earlier).getValue("q1"))
    }

    @Test
    fun `two sittings on the same day are ordered by when they happened`() {
        val morning = sitting("2026-10-01", listOf("q1"), at = 1)
        val evening = sitting("2026-10-01", listOf("q1"), missed = listOf("q1"), at = 2)
        // The evening miss is the last word, so the card is back at the bottom.
        assertEquals(0, states(morning, evening).getValue("q1").step)
        assertEquals(1, states(morning, evening).getValue("q1").misses)
    }

    @Test
    fun `a question no sitting asked has no card`() {
        val s = states(sitting("2026-10-01", listOf("q1")))
        assertTrue("q2" !in s)
        assertEquals(1, s.size)
    }

    @Test
    fun `a sitting from before asked was recorded is ignored`() {
        // Crediting a whole test from one such row would schedule questions never seen.
        val legacy = sitting("2026-10-01", asked = emptyList(), missed = listOf("q1"))
        assertEquals(emptyMap<String, Srs.Card>(), states(legacy))
    }

    @Test
    fun `another person's and another course's sittings do not count`() {
        val theirs = sitting("2026-10-01", listOf("q1"), owner = PersonRole.TEACHER)
        val elsewhere = sitting("2026-10-01", listOf("q2"), courseId = "ielts")
        assertEquals(emptyMap<String, Srs.Card>(), states(theirs, elsewhere))
    }

    @Test
    fun `a question edited out of the content drops out of the queue`() {
        val s = QuestionSrs.states(listOf(sitting("2026-10-01", listOf("q1", "gone"))), me, "az-204", known)
        assertEquals(setOf("q1"), s.keys)
    }

    // ------------------------------------------------------------------- the queue

    private val today = LocalDate.of(2026, 10, 5)

    private fun card(id: String, due: String, step: Int = 1) = id to Srs.Card(id, step = step, dueIso = due)

    @Test
    fun `due gives the longest overdue first and holds to the limit`() {
        val states = mapOf(card("q1", "2026-10-04"), card("q2", "2026-10-01"), card("q3", "2026-10-05"))
        assertEquals(listOf("q2", "q1", "q3"), QuestionSrs.due(states, today))
        assertEquals(listOf("q2"), QuestionSrs.due(states, today, limit = 1))
    }

    @Test
    fun `nothing due in the future comes up`() {
        val states = mapOf(card("q1", "2026-10-06"), card("q2", "2026-10-05"))
        assertEquals(listOf("q2"), QuestionSrs.due(states, today))
        assertEquals(1, QuestionSrs.dueCount(states, today))
    }

    @Test
    fun `a question waiting in the review set is not also asked today`() {
        val states = mapOf(card("q1", "2026-10-01"), card("q2", "2026-10-01"))
        assertEquals(listOf("q2"), QuestionSrs.due(states, today, skip = setOf("q1")))
        assertEquals(1, QuestionSrs.dueCount(states, today, skip = setOf("q1")))
    }

    @Test
    fun `the count ignores the daily limit so it does not lie`() {
        val states = (1..25).associate { card("q$it", "2026-10-01") }
        assertEquals(QuestionSrs.DAILY_LIMIT, QuestionSrs.due(states, today).size)
        assertEquals(25, QuestionSrs.dueCount(states, today))
    }

    @Test
    fun `a question answered right all the way up the ladder counts as held`() {
        val dates = listOf("2026-10-01", "2026-10-02", "2026-10-05", "2026-10-12", "2026-10-26")
        val s = states(*dates.map { sitting(it, listOf("q1")) }.toTypedArray())
        assertEquals(Srs.LADDER.size - 1, s.getValue("q1").step)
        assertTrue(s.getValue("q1").learned)
        assertEquals(1, QuestionSrs.matureCount(s))
    }
}
