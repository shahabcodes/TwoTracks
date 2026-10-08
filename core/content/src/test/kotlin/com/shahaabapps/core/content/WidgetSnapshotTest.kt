package com.shahaabapps.core.content

import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.TopicProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * The flat value the home-screen widgets read. It is a cache of what the app
 * already knows, so the tests that matter are about it not drifting: the same
 * word all day, a different one tomorrow, and never a word nobody has met.
 */
class WidgetSnapshotTest {

    private val me = PersonRole.TECH
    private val today = LocalDate.of(2026, 10, 8)
    private val german = GermanA1.course
    private val courses = listOf(Az204.course, german)

    private fun learned(course: Course, ids: List<String>) =
        ids.map { TopicProgress(owner = me, courseId = course.id, topicId = it, learnedAtIso = "2026-10-01") }

    private fun vocabTopics(n: Int) =
        german.allTopics.filter { t -> t.blocks.filterIsInstance<Block.Vocab>().isNotEmpty() }.take(n)

    private fun snapshot(progress: List<TopicProgress>, on: LocalDate = today): WidgetSnapshot {
        val byCourse = courses.associate { it.id to CourseProgress.of(me, it.id, progress, emptyList()) }
        return WidgetSnapshot.of(
            me, courses, byCourse, progress, emptyList(), emptyList(),
            togetherStreak = 4, togetherState = 3, partnerName = "Sanya", today = on,
        )
    }

    @Test
    fun `with nothing read there is no word to show`() {
        val s = snapshot(emptyList())
        assertFalse(s.hasWord)
        assertEquals("", s.word)
    }

    @Test
    fun `the word comes from the words this person has met`() {
        val topics = vocabTopics(2)
        val met = topics.flatMap { t -> t.blocks.filterIsInstance<Block.Vocab>().flatMap { it.words } }.map { it.id }.toSet()
        val s = snapshot(learned(german, topics.map { it.id }))
        assertTrue("a word should be chosen", s.hasWord)
        val chosen = Courses.words(german).first { it.display == s.word }
        assertTrue("${chosen.id} was never met", chosen.id in met)
        assertEquals(chosen.en, s.wordMeaning)
        assertEquals(german.id, s.wordCourseId)
    }

    @Test
    fun `the same word all day, a different one tomorrow`() {
        val progress = learned(german, vocabTopics(3).map { it.id })
        assertEquals(snapshot(progress).word, snapshot(progress).word)
        // Over a fortnight the choice must actually move, not sit on one word.
        val overTime = (0..13).map { snapshot(progress, today.plusDays(it.toLong())).word }
        assertTrue("the word should turn over, saw ${overTime.distinct().size} distinct", overTime.distinct().size > 1)
    }

    @Test
    fun `the choice does not depend on the order rows arrived in`() {
        val progress = learned(german, vocabTopics(3).map { it.id })
        assertEquals(snapshot(progress).word, snapshot(progress.reversed()).word)
    }

    @Test
    fun `a word met by one person is not offered to the other`() {
        val theirs = learned(german, vocabTopics(2).map { it.id }).map { it.copy(owner = PersonRole.TEACHER) }
        assertNull(WidgetSnapshot.wordOfDay(me, courses, theirs, today))
        assertNotNull(WidgetSnapshot.wordOfDay(PersonRole.TEACHER, courses, theirs, today))
    }

    @Test
    fun `the loop and the shared run are carried through unchanged`() {
        val s = snapshot(emptyList())
        assertEquals(4, s.togetherStreak)
        assertEquals(3, s.togetherState)
        assertEquals("Sanya", s.partnerName)
        assertTrue("a fresh account still has a topic to read", s.stepsTotal > 0)
        assertEquals(today.toString(), s.dateIso)
    }

    @Test
    fun `a snapshot knows when it is looking at yesterday`() {
        val s = snapshot(emptyList())
        assertFalse(s.stale(today))
        assertTrue(s.stale(today.plusDays(1)))
        // An empty snapshot has never been written, so it is not stale, it is absent.
        assertFalse(WidgetSnapshot().stale(today))
    }

    @Test
    fun `an empty snapshot is safe to render`() {
        val s = WidgetSnapshot()
        assertFalse(s.hasWord)
        assertFalse(s.loopClosed)
        assertEquals(0, s.stepsTotal)
        assertEquals("", s.nextLabel)
    }
}
