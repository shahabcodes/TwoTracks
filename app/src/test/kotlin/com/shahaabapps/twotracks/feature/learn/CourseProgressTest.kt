package com.shahaabapps.twotracks.feature.learn

import com.shahaabapps.core.content.Block
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.QuizAttempt
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
