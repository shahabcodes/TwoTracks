package com.shahaabapps.core.content

import com.shahaabapps.core.model.Flashcard
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.QuizAttempt
import com.shahaabapps.core.model.TopicProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class DailyStudyTest {

    private val me = PersonRole.TECH
    private val today = LocalDate.of(2026, 10, 7)
    private val iso = "2026-10-07"
    private val az = Az204.course
    private val german = GermanA1.course

    private fun learned(course: Course, topicIds: List<String>, date: String = "2026-10-01") =
        topicIds.map { TopicProgress(owner = me, courseId = course.id, topicId = it, learnedAtIso = date) }

    private fun sitting(course: Course, moduleId: String, asked: List<String>, missed: List<String> = emptyList(), date: String = iso) =
        QuizAttempt(
            id = "a-$moduleId-$date", owner = me, courseId = course.id, moduleId = moduleId, dateIso = date,
            score = asked.size - missed.size, total = asked.size, passed = missed.isEmpty(),
            missed = missed, asked = asked, at = 1,
        )

    private fun build(
        progress: List<TopicProgress> = emptyList(),
        attempts: List<QuizAttempt> = emptyList(),
        cards: List<Flashcard> = emptyList(),
        courses: List<Course> = listOf(az, german),
    ): DailyStudy {
        val byCourse = courses.associate { it.id to CourseProgress.of(me, it.id, progress, attempts) }
        return DailyStudy.of(me, courses, byCourse, progress, attempts, cards, today)
    }

    @Test
    fun `a fresh day offers the first unread topic and nothing else`() {
        val d = build()
        assertEquals(listOf(StudyKind.TOPIC), d.steps.map { it.kind })
        val step = d.steps.single()
        assertFalse(step.done)
        assertEquals(az.id, step.courseId)
        assertEquals(az.allTopics.first().id, step.topicId)
        assertTrue("the card should quote some minutes", step.minutes > 0)
        assertFalse(d.closed)
    }

    @Test
    fun `reading a topic today ticks the reading step`() {
        val d = build(progress = learned(az, listOf(az.allTopics.first().id), date = iso))
        val step = d.steps.single { it.kind == StudyKind.TOPIC }
        assertTrue(step.done)
        assertEquals("Done today", step.detail)
        assertEquals(0, d.minutesLeft)
        assertTrue("one step, and it is done", d.closed)
    }

    @Test
    fun `a topic read on an earlier day does not tick today`() {
        val d = build(progress = learned(az, listOf(az.allTopics.first().id), date = "2026-10-06"))
        assertFalse(d.steps.single { it.kind == StudyKind.TOPIC }.done)
        assertFalse(d.closed)
    }

    @Test
    fun `words appear once a vocabulary topic has been read`() {
        // Reading a German Words topic makes its words new cards, so the deck has work.
        val vocabTopic = german.allTopics.first { t -> t.blocks.filterIsInstance<Block.Vocab>().isNotEmpty() }
        val d = build(progress = learned(german, listOf(vocabTopic.id)))
        val words = d.steps.single { it.kind == StudyKind.WORDS }
        assertFalse(words.done)
        assertEquals(german.id, words.courseId)
        assertTrue("the new words should be counted, was '${words.title}'", words.title.startsWith("Words: "))
    }

    @Test
    fun `no words due and none reviewed means no words step at all`() {
        // Nothing read, so no word has been met: the card must not promise work.
        assertTrue(build().steps.none { it.kind == StudyKind.WORDS })
    }

    @Test
    fun `questions appear when the schedule brings them back and tick when sat today`() {
        val module = az.modules.first { it.quiz != null }
        val asked = module.quiz!!.questions.take(3).map { it.id }
        // Passed a few days ago, so they are due again now.
        val past = sitting(az, module.id, asked, date = "2026-10-01")
        val due = build(attempts = listOf(past))
        val q = due.steps.single { it.kind == StudyKind.QUESTIONS }
        assertFalse(q.done)
        assertEquals(az.id, q.courseId)
        assertEquals("Questions: 3 due", q.title)

        // Sitting the daily review today clears them and ticks the step.
        val reviewed = build(attempts = listOf(past, sitting(az, Courses.DAILY_MODULE_ID, asked)))
        val ticked = reviewed.steps.single { it.kind == StudyKind.QUESTIONS }
        assertTrue(ticked.done)
        assertEquals("Done today", ticked.detail)
    }

    @Test
    fun `a daily sitting that missed questions leaves them to the review set, not to today`() {
        val module = az.modules.first { it.quiz != null }
        val asked = module.quiz!!.questions.take(2).map { it.id }
        val past = sitting(az, module.id, asked, date = "2026-10-01")
        val slipped = sitting(az, Courses.DAILY_MODULE_ID, asked, missed = listOf(asked.first()))
        val d = build(attempts = listOf(past, slipped))
        // The step is still ticked: the sitting happened. The missed one is the review set's job.
        val q = d.steps.single { it.kind == StudyKind.QUESTIONS }
        assertTrue(q.done)
    }

    @Test
    fun `the next step is the first one not done, and the day closes when all are`() {
        val module = az.modules.first { it.quiz != null }
        val asked = module.quiz!!.questions.take(2).map { it.id }
        val past = sitting(az, module.id, asked, date = "2026-10-01")

        val partly = build(progress = learned(az, listOf(az.allTopics.first().id), date = iso), attempts = listOf(past))
        assertEquals(StudyKind.QUESTIONS, partly.next?.kind)
        assertEquals(1, partly.doneCount)
        assertEquals(2, partly.total)
        assertFalse(partly.closed)

        val all = build(
            progress = learned(az, listOf(az.allTopics.first().id), date = iso),
            attempts = listOf(past, sitting(az, Courses.DAILY_MODULE_ID, asked)),
        )
        assertNull(all.next)
        assertTrue(all.closed)
        assertEquals(1f, all.fraction, 1e-6f)
        assertEquals(0, all.minutesLeft)
    }

    @Test
    fun `another person's work does not close my day`() {
        val theirs = learned(az, listOf(az.allTopics.first().id), date = iso).map { it.copy(owner = PersonRole.TEACHER) }
        val d = build(progress = theirs)
        assertFalse("their reading is not mine", d.steps.single { it.kind == StudyKind.TOPIC }.done)
    }

    @Test
    fun `an empty loop is not a closed one`() {
        // No courses at all: nothing to do is not the same as a day completed.
        val d = DailyStudy.of(me, emptyList(), emptyMap(), emptyList(), emptyList(), emptyList(), today)
        assertEquals(0, d.total)
        assertFalse(d.closed)
        assertNull(d.next)
        assertEquals(0f, d.fraction, 1e-6f)
    }

    @Test
    fun `the minute estimates match the figures the rest of the app quotes`() {
        assertEquals(3, DailyStudy.wordMinutes(12))
        assertEquals(2, DailyStudy.wordMinutes(1))
        assertEquals(15, DailyStudy.questionMinutes(10))
        assertEquals(2, DailyStudy.questionMinutes(1))
    }
}
