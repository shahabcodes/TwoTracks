package com.shahaabapps.core.content

import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.TopicProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
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
    fun `with nothing read it still has something to teach`() {
        // The opening topic of each course, which is what is about to be read
        // anyway. A blank card teaches nobody anything.
        val s = snapshot(emptyList())
        assertTrue("a fresh account should still get an item", s.hasWord)
        assertTrue(s.wordMeaning.isNotBlank())
        assertTrue("it should name the topic it came from", s.wordTopicId.isNotBlank())
    }

    @Test
    fun `a course with no vocabulary offers its glossary terms`() {
        // AZ-204 has no Block.Vocab anywhere, so before this the widget was
        // empty for good for anyone studying only a certification.
        val az = Az204.course
        assertTrue("AZ-204 should have no vocabulary", Courses.words(az).isEmpty())
        val topics = az.allTopics.filter { t -> t.blocks.filterIsInstance<Block.Glossary>().isNotEmpty() }.take(3)
        assertTrue("AZ-204 should define terms", topics.isNotEmpty())
        val item = WidgetSnapshot.dayItem(me, listOf(az), learned(az, topics.map { it.id }), today)
        assertNotNull(item)
        assertTrue("an AZ-204 item must be a term", item!!.isTerm)
        assertEquals(az.id, item.courseId)
        val terms = Courses.terms(az).associateBy { it.term }
        assertEquals(terms[item.display]?.meaning, item.meaning)
        // A term belongs to the topic that defines it, which is where tapping goes.
        assertTrue(item.topicId in topics.map { it.id })
    }

    @Test
    fun `a term carries no example sentence and a word does`() {
        val vocab = vocabTopics(2)
        val word = WidgetSnapshot.dayItem(me, listOf(german), learned(german, vocab.map { it.id }), today)
        assertNotNull(word)
        assertFalse(word!!.isTerm)
        val az = Az204.course
        val glossary = az.allTopics.filter { t -> t.blocks.filterIsInstance<Block.Glossary>().isNotEmpty() }.take(2)
        val term = WidgetSnapshot.dayItem(me, listOf(az), learned(az, glossary.map { it.id }), today)
        assertEquals("", term!!.example)
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
    fun `what one person has met is not offered to the other`() {
        val topics = vocabTopics(2)
        val theirs = learned(german, topics.map { it.id }).map { it.copy(owner = PersonRole.TEACHER) }
        val openers = courses.mapNotNull { it.allTopics.firstOrNull()?.id }.toSet()
        // Both get something, but it is drawn from their own reading: the one
        // who read nothing falls back to the topic each course opens with.
        val mine = (0..20).map { WidgetSnapshot.dayItem(me, courses, theirs, today.plusDays(it.toLong()))!! }
        assertTrue("mine should only come from the opening topics", mine.all { it.topicId in openers })
        val theirIds = topics.map { it.id }.toSet()
        val theirs20 = (0..20).map { WidgetSnapshot.dayItem(PersonRole.TEACHER, courses, theirs, today.plusDays(it.toLong()))!! }
        assertTrue("theirs should come from what they read", theirs20.all { it.topicId in theirIds })
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
        assertTrue(s.fresh(today))
        assertFalse("yesterday's counts are not today's answer", s.fresh(today.plusDays(1)))
        // An empty snapshot has never been written, so it is not stale, it is absent.
        assertFalse(WidgetSnapshot().stale(today))
    }

    @Test
    fun `an unwritten snapshot is not a quiet day`() {
        // The distinction the widgets turn on: never written reads as "open the
        // app", not as "nothing due", or a broken widget looks like a calm one.
        val s = WidgetSnapshot()
        assertFalse(s.synced)
        assertFalse(s.fresh(today))
        assertTrue(snapshot(emptyList()).synced)
    }

    @Test
    fun `the next step names which kind of work it is`() {
        // A widget cannot route without this: the words and questions steps
        // carry no topic id, so a blank kind and a blank topic read alike and
        // the card lands the reader on Today instead of the deck it names.
        val fresh = snapshot(emptyList())
        assertEquals(StudyKind.TOPIC.name, fresh.nextKind)
        assertTrue(fresh.nextTopicId.isNotBlank())
        assertTrue(fresh.nextCourseId.isNotBlank())

        // Every topic with vocabulary read, so reading is done for today and
        // the words that came with it are what is left.
        val read = vocabTopics(3).map {
            TopicProgress(owner = me, courseId = german.id, topicId = it.id, learnedAtIso = today.toString())
        }
        val s = snapshot(read)
        assertEquals(StudyKind.WORDS.name, s.nextKind)
        assertEquals(german.id, s.nextCourseId)
        // The step it points at has no topic, which is the whole reason the
        // kind has to be carried separately.
        assertEquals("", s.nextTopicId)
    }

    @Test
    fun `a closed day names no next step at all`() {
        val s = WidgetSnapshot()
        assertEquals("", s.nextKind)
    }

    @Test
    fun `an empty snapshot is safe to render`() {
        val s = WidgetSnapshot()
        assertFalse(s.hasWord)
        assertFalse(s.loopClosed)
        assertEquals(0, s.stepsTotal)
        assertEquals("", s.nextLabel)
        assertFalse(s.wordIsTerm)
        assertEquals("", s.wordTopicId)
    }
}
