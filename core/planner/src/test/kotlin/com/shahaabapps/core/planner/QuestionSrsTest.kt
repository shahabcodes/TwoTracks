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
        seconds: Map<String, Int> = emptyMap(),
        sure: Map<String, Boolean> = emptyMap(),
    ) = QuizAttempt(
        id = "$date-$at", owner = owner, courseId = courseId, moduleId = "m1", dateIso = date,
        score = asked.size - missed.size, total = asked.size, passed = missed.isEmpty(),
        missed = missed, asked = asked, at = at, seconds = seconds, sure = sure,
    )

    /** Every question in these tests is reckoned to take half a minute. */
    private val expected = known.associateWith { 30 }

    private fun paced(vararg attempts: QuizAttempt) =
        QuestionSrs.states(attempts.toList(), me, "az-204", known, expected)

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

    // ------------------------------------------------------------ the clock

    @Test
    fun `a right answer that was worked out holds its place instead of climbing`() {
        // Two clean sittings put q1 on step 2, due three days out. A third
        // that takes four times as long as it should is not knowledge: the
        // step stays and the gap repeats rather than stretching to seven.
        val climbed = paced(
            sitting("2026-10-01", listOf("q1"), seconds = mapOf("q1" to 30)),
            sitting("2026-10-02", listOf("q1"), seconds = mapOf("q1" to 30)),
        )
        assertEquals(2, climbed.getValue("q1").step)
        assertEquals("2026-10-05", climbed.getValue("q1").dueIso)

        val held = paced(
            sitting("2026-10-01", listOf("q1"), seconds = mapOf("q1" to 30)),
            sitting("2026-10-02", listOf("q1"), seconds = mapOf("q1" to 30)),
            sitting("2026-10-05", listOf("q1"), at = 1, seconds = mapOf("q1" to 120)),
        )
        assertEquals("the step should not climb on a worked-out answer", 2, held.getValue("q1").step)
        assertEquals("2026-10-08", held.getValue("q1").dueIso)
        assertEquals("it was still right", 3, held.getValue("q1").correct)
    }

    @Test
    fun `a wrong answer given too fast to have read the question is marked`() {
        val snap = paced(sitting("2026-10-01", listOf("q1"), missed = listOf("q1"), seconds = mapOf("q1" to 3)))
        assertTrue(snap.getValue("q1").shaky)

        val considered = paced(sitting("2026-10-01", listOf("q1"), missed = listOf("q1"), seconds = mapOf("q1" to 40)))
        assertTrue("a wrong answer that took its time is a known gap", !considered.getValue("q1").shaky)
    }

    @Test
    fun `a confident miss is asked before an unsure one`() {
        // Both fall to the bottom and come back tomorrow, so the schedule
        // cannot separate them. The order of a capped day can.
        val states = paced(
            sitting("2026-10-01", listOf("q1", "q2"), missed = listOf("q1", "q2"), seconds = mapOf("q1" to 40, "q2" to 3)),
        )
        assertEquals(listOf("q2", "q1"), QuestionSrs.due(states, LocalDate.parse("2026-10-02")))
    }

    @Test
    fun `the mark clears once the question is got right`() {
        val states = paced(
            sitting("2026-10-01", listOf("q1"), missed = listOf("q1"), seconds = mapOf("q1" to 3)),
            sitting("2026-10-02", listOf("q1"), seconds = mapOf("q1" to 30)),
        )
        assertTrue(!states.getValue("q1").shaky)
    }

    @Test
    fun `a sitting with no clock schedules exactly as it always did`() {
        // Every attempt recorded before this existed, and this must not move.
        val old = states(
            sitting("2026-10-01", listOf("q1")),
            sitting("2026-10-02", listOf("q1")),
        )
        val sameWithExpectations = paced(
            sitting("2026-10-01", listOf("q1")),
            sitting("2026-10-02", listOf("q1")),
        )
        assertEquals(old.getValue("q1"), sameWithExpectations.getValue("q1"))
        assertEquals(2, old.getValue("q1").step)
        assertEquals("2026-10-05", old.getValue("q1").dueIso)
    }

    // ------------------------------------------------- the answer they gave

    @Test
    fun `being sure and wrong marks the question, however long it took`() {
        // The clock would call forty seconds a considered answer. The person
        // said they were sure, and what they said wins.
        val states = paced(
            sitting("2026-10-01", listOf("q1"), missed = listOf("q1"), seconds = mapOf("q1" to 40), sure = mapOf("q1" to true)),
        )
        assertTrue(states.getValue("q1").shaky)
    }

    @Test
    fun `saying you were not sure holds a right answer in place`() {
        // Three seconds is quick enough for the clock to call it known. The
        // person said otherwise, so the gap does not stretch.
        val climbed = paced(
            sitting("2026-10-01", listOf("q1"), seconds = mapOf("q1" to 30)),
            sitting("2026-10-02", listOf("q1"), seconds = mapOf("q1" to 30)),
        )
        assertEquals(2, climbed.getValue("q1").step)
        assertEquals("2026-10-05", climbed.getValue("q1").dueIso)

        val held = paced(
            sitting("2026-10-01", listOf("q1"), seconds = mapOf("q1" to 30)),
            sitting("2026-10-02", listOf("q1"), seconds = mapOf("q1" to 30)),
            sitting("2026-10-05", listOf("q1"), at = 1, seconds = mapOf("q1" to 3), sure = mapOf("q1" to false)),
        )
        assertEquals("the step should not climb on an answer they were unsure of", 2, held.getValue("q1").step)
        assertEquals("2026-10-08", held.getValue("q1").dueIso)
    }

    @Test
    fun `a quick wrong answer they said they were unsure of is not marked`() {
        // The inverse of the clock's guess, which is the point of asking.
        val states = paced(
            sitting("2026-10-01", listOf("q1"), missed = listOf("q1"), seconds = mapOf("q1" to 2), sure = mapOf("q1" to false)),
        )
        assertTrue("said they were guessing, so it is a known gap", !states.getValue("q1").shaky)
    }

    @Test
    fun `with nothing said the clock is still used`() {
        val states = paced(
            sitting("2026-10-01", listOf("q1"), missed = listOf("q1"), seconds = mapOf("q1" to 2)),
        )
        assertTrue(states.getValue("q1").shaky)
    }
}
