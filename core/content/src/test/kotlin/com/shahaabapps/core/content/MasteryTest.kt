package com.shahaabapps.core.content

import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.QuizAttempt
import com.shahaabapps.core.model.TopicProgress
import com.shahaabapps.core.planner.Srs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Mastery replaces a tick with something earned. The tests that matter are
 * that nothing can be claimed without a record behind it, and that a topic
 * whose questions have come back round is offered again rather than counted
 * as finished.
 */
class MasteryTest {

    private val me = PersonRole.TECH
    private val today = LocalDate.of(2026, 10, 8)
    private val course = Az204.course
    private val module = course.modules.first()
    private val topic = module.topics.first()

    private fun read(topicId: String, on: String = "2026-09-01") =
        TopicProgress(owner = me, courseId = course.id, topicId = topicId, learnedAtIso = on)

    /** A sitting of this topic's module test, answering the named questions right. */
    private fun sitting(right: List<String>, wrong: List<String>, on: String, at: Long = 1) = QuizAttempt(
        id = "a$at-$on", owner = me, courseId = course.id, moduleId = module.id, dateIso = on,
        score = right.size, total = right.size + wrong.size, passed = wrong.isEmpty(),
        missed = wrong, asked = right + wrong, at = at,
    )

    private fun progress(topics: List<TopicProgress>, attempts: List<QuizAttempt>) =
        CourseProgress.of(me, course.id, topics, attempts)

    private fun mastery(topics: List<TopicProgress>, attempts: List<QuizAttempt> = emptyList(), on: LocalDate = today) =
        course.mastery(progress(topics, attempts), on)

    private fun questionsOf(topicId: String) = course.allQuestions.filter { it.topicId == topicId }.map { it.id }

    @Test
    fun `a topic nobody has opened is unread`() {
        val m = mastery(emptyList())
        assertEquals(Mastery.UNREAD, m[topic.id]!!.level)
        assertEquals(0f, m[topic.id]!!.fraction, 0.001f)
        assertFalse("nothing to revisit in a topic never read", m[topic.id]!!.needsRevisit)
    }

    @Test
    fun `reading a topic is not the same as knowing it`() {
        // The whole point: pressing the button at the bottom of the page gets
        // you one rung, not a tick.
        val m = mastery(listOf(read(topic.id)))[topic.id]!!
        assertEquals(Mastery.READ, m.level)
        assertTrue("read is not most of the way there", m.fraction < 0.5f)
        assertEquals(LocalDate.of(2026, 9, 1), m.readOn)
    }

    @Test
    fun `answering its questions correctly earns practised`() {
        val qs = questionsOf(topic.id)
        assertTrue("the topic should have questions of its own", qs.isNotEmpty())
        val m = mastery(listOf(read(topic.id)), listOf(sitting(right = qs, wrong = emptyList(), on = "2026-09-02")))[topic.id]!!
        assertEquals(Mastery.PRACTISED, m.level)
        assertEquals(qs.size, m.met)
        assertEquals(qs.size, m.questions)
        assertEquals(0, m.missed)
    }

    @Test
    fun `held needs every question of the topic to survive the ladder`() {
        val qs = questionsOf(topic.id)
        // Enough correct sittings, spaced far enough apart, to climb the ladder.
        var day = LocalDate.of(2026, 1, 1)
        val attempts = mutableListOf<QuizAttempt>()
        repeat(Srs.LADDER.size + 1) { i ->
            attempts += sitting(right = qs, wrong = emptyList(), on = day.toString(), at = i + 1L)
            day = day.plusDays(Srs.LADDER.last().toLong() + 1)
        }
        val m = mastery(listOf(read(topic.id)), attempts, on = day)[topic.id]!!
        assertEquals("every question held should put the topic at the top", Mastery.HELD, m.level)
        assertEquals(m.questions, m.held)
        assertEquals(1f, m.fraction, 0.001f)

        // One question left behind keeps the topic off the top rung: every
        // sitting but the first asks only the others, so it never climbs.
        val behind = attempts.mapIndexed { i, a ->
            if (i == 0) a else a.copy(asked = qs.drop(1), score = qs.size - 1, total = qs.size - 1)
        }
        val held = mastery(listOf(read(topic.id)), behind, on = day)[topic.id]!!
        assertEquals(Mastery.PRACTISED, held.level)
        assertEquals("one question short of the whole topic", held.questions - 1, held.held)
    }

    @Test
    fun `a missed question makes its topic worth revisiting`() {
        val qs = questionsOf(topic.id)
        val m = mastery(
            listOf(read(topic.id)),
            listOf(sitting(right = qs.drop(1), wrong = qs.take(1), on = "2026-09-02")),
        )[topic.id]!!
        assertEquals(1, m.missed)
        assertTrue(m.needsRevisit)
    }

    @Test
    fun `revisits put the missed first, then the reading left longest`() {
        val topics = module.topics.take(3)
        val rows = listOf(
            read(topics[0].id, on = "2026-01-01"),
            read(topics[1].id, on = "2026-06-01"),
            read(topics[2].id, on = "2026-03-01"),
        )
        // Every topic has something due; only the middle one has a miss.
        val attempts = topics.mapIndexed { i, t ->
            val qs = questionsOf(t.id)
            sitting(
                right = if (i == 1) qs.drop(1) else qs,
                wrong = if (i == 1) qs.take(1) else emptyList(),
                on = "2026-06-01", at = i + 1L,
            )
        }
        val p = progress(rows, attempts)
        val order = course.revisits(p, today, limit = 3).map { it.topicId }
        assertEquals("the miss comes first whatever its date", topics[1].id, order.first())
        // Then by reading date, oldest first.
        assertEquals(listOf(topics[0].id, topics[2].id), order.drop(1))
    }

    @Test
    fun `a topic with nothing due and nothing missed is left alone`() {
        val qs = questionsOf(topic.id)
        val p = progress(listOf(read(topic.id)), listOf(sitting(right = qs, wrong = emptyList(), on = today.toString())))
        // Answered right today, so the schedule has pushed it into the future.
        assertTrue("nothing should be offered for revisiting", course.revisits(p, today).isEmpty())
    }

    @Test
    fun `a topic with no questions of its own tops out at read`() {
        val bare = course.allTopics.firstOrNull { t -> course.allQuestions.none { it.topicId == t.id } }
        if (bare == null) return // every topic is covered, which is the stronger state
        val m = mastery(listOf(read(bare.id)))[bare.id]!!
        assertEquals(Mastery.READ, m.level)
        assertEquals("reading it is as far as it can go", 1f, m.fraction, 0.001f)
    }

    @Test
    fun `one person's reading is not another's`() {
        val theirs = listOf(read(topic.id).copy(owner = PersonRole.TEACHER))
        assertEquals(Mastery.UNREAD, mastery(theirs)[topic.id]!!.level)
        val ours = course.mastery(CourseProgress.of(PersonRole.TEACHER, course.id, theirs, emptyList()), today)
        assertEquals(Mastery.READ, ours[topic.id]!!.level)
    }

    @Test
    fun `every topic in every course gets a standing`() {
        Courses.all.forEach { c ->
            val m = c.mastery(CourseProgress.of(me, c.id, emptyList(), emptyList()), today)
            assertEquals("${c.id} should cover every topic", c.allTopics.size, m.size)
        }
    }
}
