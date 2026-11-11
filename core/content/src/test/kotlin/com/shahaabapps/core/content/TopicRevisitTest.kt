package com.shahaabapps.core.content

import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.QuizAttempt
import com.shahaabapps.core.model.TopicProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Clearing "Look again" from the topic it is on.
 *
 * The badge is earned by a topic's own questions being missed or due. Until
 * the topic check there was nowhere to answer those questions from the
 * lesson the badge sends you to: the review and the daily are drawn across a
 * whole course and both are capped, so a topic could say look again, be read
 * again, and say it still. These are the tests that the badge and the way to
 * clear it are the same set of questions.
 */
class TopicRevisitTest {

    private val course = Courses.all.first { c -> c.modules.any { it.quiz != null } }
    private val module = course.modules.first { it.quiz != null }
    private val quiz = module.quiz!!
    private val topicId = quiz.questions.first().topicId
    private val mine = quiz.questions.filter { it.topicId == topicId }
    // The same day as the sittings below. A right answer is then scheduled
    // for some day after this one and a wrong one for tomorrow, so neither
    // is due yet and what is waiting is only what the review set holds.
    private val today = LocalDate.of(2026, 11, 10)

    private fun progress(vararg attempts: QuizAttempt) = CourseProgress.of(
        PersonRole.TECH, course.id,
        listOf(TopicProgress(topicId = topicId, courseId = course.id, owner = PersonRole.TECH, learnedAtIso = "2026-11-01")),
        attempts.toList(),
    )

    private fun sitting(id: String, missed: List<String>, at: Long = 1L) = QuizAttempt(
        id = id, owner = PersonRole.TECH, courseId = course.id, moduleId = module.id,
        dateIso = "2026-11-10", score = quiz.questions.size - missed.size, total = quiz.questions.size,
        passed = missed.isEmpty(), missed = missed, asked = quiz.questions.map { it.id }, at = at,
    )

    @Test
    fun `a missed question is waiting on its own topic`() {
        val p = progress(sitting("a", missed = listOf(mine.first().id)))

        val waiting = p.topicRevisits(course, topicId, today)

        assertTrue("the missed question was not waiting on its topic", waiting.any { it.id == mine.first().id })
    }

    @Test
    fun `the topic says look again for exactly as long as something is waiting`() {
        val wrong = progress(sitting("a", missed = listOf(mine.first().id)))
        assertTrue(course.mastery(wrong, today)[topicId]!!.needsRevisit)

        // Answered right in a later sitting, which is what the topic check
        // writes: the badge has to go.
        val put = progress(sitting("a", missed = listOf(mine.first().id)), sitting("b", missed = emptyList(), at = 2L))

        assertTrue("something was still waiting", put.topicRevisits(course, topicId, today).isEmpty())
        assertTrue("the topic still said look again", !course.mastery(put, today)[topicId]!!.needsRevisit)
    }

    @Test
    fun `what is waiting is what the badge counts`() {
        // The one promise the card makes: answer these and the badge goes.
        val p = progress(sitting("a", missed = mine.take(2).map { it.id }))
        val m = course.mastery(p, today)[topicId]!!

        assertEquals(m.missed + m.due, p.topicRevisits(course, topicId, today).size)
    }

    @Test
    fun `a question in the review set is not also counted as due`() {
        // It is one question and one piece of work. Counted in both, a topic
        // with two bad answers read "2 missed · 2 due".
        val p = progress(sitting("a", missed = mine.take(2).map { it.id }))
        val m = course.mastery(p, today)[topicId]!!

        assertEquals(2, m.missed)
        assertEquals(0, m.due)
    }

    @Test
    fun `another topic's questions are not waiting on this one`() {
        val other = quiz.questions.firstOrNull { it.topicId != topicId } ?: return
        val p = progress(sitting("a", missed = listOf(other.id)))

        assertTrue(p.topicRevisits(course, topicId, today).none { it.id == other.id })
    }

    @Test
    fun `a topic with nothing waiting has nothing to check`() {
        val p = progress(sitting("a", missed = emptyList()))

        assertTrue(p.topicRevisits(course, topicId, today).isEmpty())
    }

    @Test
    fun `the check's module id carries the topic and reads back`() {
        val id = Courses.topicCheckId(topicId)

        assertEquals(topicId, Courses.topicOfCheck(id))
        assertNull("a module test was read as a topic check", Courses.topicOfCheck(module.id))
        assertNull(Courses.topicOfCheck(Courses.REVIEW_MODULE_ID))
        assertNull("an empty topic is not a check", Courses.topicOfCheck(Courses.TOPIC_CHECK_PREFIX))
        // It travels as one path segment of a route, so it cannot carry a
        // character that would end the segment.
        assertTrue("the id is not safe in a route: $id", id.none { it == '/' || it == '?' || it == '#' })
    }

    @Test
    fun `the check is a sitting of exactly the waiting questions`() {
        val p = progress(sitting("a", missed = mine.take(2).map { it.id }))
        val waiting = p.topicRevisits(course, topicId, today)
        val topic = Courses.topic(course.id, topicId)!!.second

        val built = course.topicCheckModule(topic, waiting)

        assertEquals(waiting.map { it.id }, built.quiz!!.questions.map { it.id })
        // No bar to clear: it is for putting something right, not for grading.
        assertEquals(0, built.quiz!!.passPercent)
        assertEquals(Courses.topicCheckId(topicId), built.id)
    }
}
