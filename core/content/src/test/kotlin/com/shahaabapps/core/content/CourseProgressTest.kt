package com.shahaabapps.core.content

import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.QuizAttempt
import com.shahaabapps.core.planner.QuestionSrs
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The review set collects every question answered wrongly, from a module test,
 * the mock exam, a review sitting or a one-question check inside a topic, and
 * releases it as soon as a later sitting gets the same question right.
 */
class CourseProgressTest {

    private val me = PersonRole.TECH
    private val course = Courses.byId("az-204")!!
    private val checkQuestion = course.allTopics.flatMap { it.blocks }.filterIsInstance<Block.Check>().first().question

    private fun attempt(moduleId: String, asked: List<String>, missed: List<String>, at: Long, date: String = "2026-10-06") =
        QuizAttempt(
            id = "a$at", owner = me, courseId = course.id, moduleId = moduleId, dateIso = date,
            score = asked.size - missed.size, total = asked.size, passed = missed.isEmpty(),
            missed = missed, asked = asked, at = at,
        )

    private fun progressFor(vararg attempts: QuizAttempt) =
        CourseProgress.of(me, course.id, emptyList(), attempts.toList())

    // ------------------------------------------------------------- daily review

    private val module = course.modules.first { it.quiz != null }
    private val qs = module.quiz!!.questions

    @Test
    fun `a question answered right comes back on the schedule, not at once`() {
        val p = progressFor(attempt(module.id, qs.take(2).map { it.id }, emptyList(), 1, date = "2026-10-01"))
        assertEquals(0, p.dailyDueCount(LocalDate.of(2026, 10, 1)))
        assertEquals(2, p.dailyDueCount(LocalDate.of(2026, 10, 2)))
        assertEquals(qs.take(2).map { it.id }.toSet(), p.dailyDue(course, LocalDate.of(2026, 10, 2)).map { it.id }.toSet())
    }

    @Test
    fun `a question in the review set is not also due for a daily review`() {
        val p = progressFor(attempt(module.id, qs.take(2).map { it.id }, listOf(qs[0].id), 1, date = "2026-10-01"))
        assertTrue(qs[0].id in p.missedQuestionIds)
        // Both are scheduled, but the missed one belongs to the review set today.
        assertEquals(listOf(qs[1].id), p.dailyDue(course, LocalDate.of(2026, 10, 2)).map { it.id })
        assertEquals(1, p.dailyDueCount(LocalDate.of(2026, 10, 2)))
    }

    @Test
    fun `a question never asked is never due`() {
        val p = progressFor(attempt(module.id, listOf(qs[0].id), emptyList(), 1, date = "2026-10-01"))
        val due = p.dailyDue(course, LocalDate.of(2027, 1, 1)).map { it.id }
        assertEquals(listOf(qs[0].id), due)
        assertFalse(qs[1].id in due)
    }

    @Test
    fun `an inline check seeds the schedule, so reading alone fills the daily review`() {
        val p = progressFor(attempt(Courses.CHECK_MODULE_ID, listOf(checkQuestion.id), emptyList(), 1, date = "2026-10-01"))
        assertEquals(listOf(checkQuestion.id), p.dailyDue(course, LocalDate.of(2026, 10, 2)).map { it.id })
    }

    @Test
    fun `the daily sitting holds to its limit while the count tells the truth`() {
        val many = qs.map { it.id } + course.mockExam!!.questions.map { it.id }
        val p = progressFor(attempt(Courses.MOCK_MODULE_ID, many.distinct(), emptyList(), 1, date = "2026-10-01"))
        val tomorrow = LocalDate.of(2026, 10, 2)
        assertEquals(QuestionSrs.DAILY_LIMIT, p.dailyDue(course, tomorrow).size)
        assertTrue("the count should not be capped", p.dailyDueCount(tomorrow) > QuestionSrs.DAILY_LIMIT)
    }

    @Test
    fun `a daily sitting advances the schedule and its misses join the review set`() {
        val first = attempt(module.id, qs.take(2).map { it.id }, emptyList(), 1, date = "2026-10-01")
        val daily = attempt(Courses.DAILY_MODULE_ID, qs.take(2).map { it.id }, listOf(qs[0].id), 2, date = "2026-10-02")
        val p = progressFor(first, daily)
        assertTrue("a miss in a daily sitting still needs getting right", qs[0].id in p.missedQuestionIds)
        // The one held climbs the ladder, so it is not due again the next day.
        assertEquals(0, p.dailyDueCount(LocalDate.of(2026, 10, 3)))
        assertEquals(listOf(qs[1].id), p.dailyDue(course, LocalDate.of(2026, 10, 5)).map { it.id })
    }

    @Test
    fun `a daily sitting is not a passed module`() {
        val p = progressFor(attempt(Courses.DAILY_MODULE_ID, listOf(qs[0].id), emptyList(), 1))
        assertEquals(0, p.modulesPassed(course))
    }

    @Test
    fun `a missed check question joins the review set`() {
        val p = progressFor(attempt(Courses.CHECK_MODULE_ID, listOf(checkQuestion.id), listOf(checkQuestion.id), 1))
        assertTrue("the check should be waiting for review", checkQuestion.id in p.missedQuestionIds)
        assertEquals(listOf(checkQuestion.id), p.reviewQuestions(course).map { it.id })
    }

    @Test
    fun `answering the same check correctly later clears it`() {
        val p = progressFor(
            attempt(Courses.CHECK_MODULE_ID, listOf(checkQuestion.id), listOf(checkQuestion.id), 1),
            attempt(Courses.CHECK_MODULE_ID, listOf(checkQuestion.id), emptyList(), 2),
        )
        assertFalse("a later right answer clears it", checkQuestion.id in p.missedQuestionIds)
    }

    @Test
    fun `a check answered right first time never enters the review set`() {
        val p = progressFor(attempt(Courses.CHECK_MODULE_ID, listOf(checkQuestion.id), emptyList(), 1))
        assertTrue(p.missedQuestionIds.isEmpty())
    }

    @Test
    fun `same-day attempts resolve in the order they were taken`() {
        val q = course.modules.first { it.quiz != null }.quiz!!.questions.first().id
        val module = course.modules.first { it.quiz != null }.id
        val wrongThenRight = progressFor(
            attempt(module, listOf(q), listOf(q), 1),
            attempt(module, listOf(q), emptyList(), 2),
        )
        val rightThenWrong = progressFor(
            attempt(module, listOf(q), emptyList(), 1),
            attempt(module, listOf(q), listOf(q), 2),
        )
        assertFalse("wrong then right should clear", q in wrongThenRight.missedQuestionIds)
        assertTrue("right then wrong should collect", q in rightThenWrong.missedQuestionIds)
    }

    @Test
    fun `a check attempt does not count as a passed module`() {
        val p = progressFor(attempt(Courses.CHECK_MODULE_ID, listOf(checkQuestion.id), emptyList(), 1))
        assertEquals(0, p.modulesPassed(course))
        assertFalse(p.mockPassed)
    }

    @Test
    fun `a randomised mock only reviews the questions it actually asked`() {
        val mock = course.mockExam!!
        val asked = mock.questions.take(5).map { it.id }
        val p = progressFor(attempt(Courses.MOCK_MODULE_ID, asked, listOf(asked[1]), 1))
        assertEquals(listOf(asked[1]), p.reviewQuestions(course).map { it.id })
    }
}
