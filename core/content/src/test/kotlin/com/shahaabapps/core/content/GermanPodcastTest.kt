package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The shape of every German A1 episode: Maya explains and Leo asks in
 * English, Jonas says all the German, every German line carries its English,
 * there is enough to say back, and it ends on the Goethe exam and the topic's
 * thing to try today. Whether Jonas's German agrees is GermanGrammarTest's.
 */
class GermanPodcastTest {
    /** Every German topic has an episode: the course's own list, so a new topic needs one too. */
    private val withEpisodes = GermanA1.course.allTopics.map { it.id }

    private val course = GermanA1.course
    private val episodes = Podcasts.all.filter { it.series == PodcastSeries.GERMAN_A1 }

    private fun topic(id: String): Topic = course.allTopics.first { it.id == id }

    @Test
    fun `the named topics have their episodes, and every German episode belongs to a German topic`() {
        val ids = course.allTopics.map { it.id }.toSet()
        episodes.forEach { assertTrue("${it.topicId} is not a German A1 topic", it.topicId in ids) }
        assertEquals(withEpisodes.toSet(), episodes.map { it.topicId }.toSet())
    }

    @Test
    fun `only Jonas speaks German, and every German line has its English`() {
        episodes.forEach { p ->
            p.lines.forEach { l ->
                if (l.host == Host.JONAS) {
                    assertTrue("${p.topicId}: Jonas line with no English: \"${l.text.take(50)}\"", !l.translation.isNullOrBlank())
                } else {
                    assertTrue("${p.topicId}: ${l.host} line carries a translation", l.translation == null)
                }
            }
        }
    }

    @Test
    fun `Maya opens, nobody speaks twice in a row, and no line runs on`() {
        episodes.forEach { p ->
            assertEquals("${p.topicId} should open with Maya", Host.MAYA, p.lines.first().host)
            p.lines.zipWithNext().forEach { (a, b) -> assertTrue("${p.topicId}: two lines in a row from ${a.host}", a.host != b.host) }
            p.lines.forEach { l ->
                val words = PodcastCue.strip(l.text).split(" ").size
                assertTrue("${p.topicId}: a line of $words words", words <= 70)
            }
        }
    }

    @Test
    fun `an episode runs four to eight minutes and gives plenty to say back`() {
        episodes.forEach { p ->
            assertTrue("${p.topicId} has ${p.spokenWords} words", p.spokenWords in 500..1_100)
            val drills = p.lines.count { PodcastCue.REPEAT in PodcastCue.cues(it.text) }
            assertTrue("${p.topicId} has $drills repeat-after-me lines", drills >= 6)
            p.lines.filter { PodcastCue.REPEAT in PodcastCue.cues(it.text) }.forEach {
                assertEquals("${p.topicId}: only Jonas drills", Host.JONAS, it.host)
                assertTrue("${p.topicId}: [repeat] goes at the end of the line", it.text.trimEnd().endsWith("[${PodcastCue.REPEAT}]"))
            }
        }
    }

    @Test
    fun `cues are ones the render tool knows, and a laugh stays a rarity`() {
        episodes.forEach { p ->
            val cues = p.lines.flatMap { PodcastCue.cues(it.text) }
            cues.forEach { assertTrue("${p.topicId}: unknown cue [$it]", it in PodcastCue.ALL) }
            val sounds = cues.count { it in PodcastCue.SOUNDS }
            assertTrue("${p.topicId}: $sounds laughs and sighs", sounds <= p.spokenWords / 120)
        }
    }

    @Test
    fun `every line is words a voice can say`() {
        val unsayable = Regex("""[_@{}<>`#\\|]|https?:|\.(com|net|io|de)\b""")
        episodes.forEach { p -> p.lines.forEach { l -> assertTrue("${p.topicId}: \"${l.text.take(60)}\"", !unsayable.containsMatchIn(PodcastCue.strip(l.text))) } }
    }

    @Test
    fun `an episode uses its topic's own terms`() {
        episodes.forEach { p ->
            val terms = topic(p.topicId).blocks.filterIsInstance<Block.Glossary>().flatMap { it.terms }.map { it.term }
            val heard = p.lines.joinToString(" ") { PodcastCue.strip(it.text) }.lowercase()
            val used = terms.count { t -> t.split(" / ").any { it.lowercase() in heard } }
            assertTrue("${p.topicId} uses $used of its ${terms.size} glossary terms", used * 2 >= terms.size)
        }
    }

    @Test
    fun `an episode closes with the Goethe exam and the topic's thing to try today`() {
        episodes.forEach { p ->
            val asked = p.lines.takeLast(8).any { it.host == Host.LEO && "exam" in PodcastCue.strip(it.text).lowercase() }
            assertTrue("${p.topicId}: Leo never asks about the exam near the end", asked)
            val tryIt = topic(p.topicId).blocks.filterIsInstance<Block.TryIt>().single()
            val ending = p.lines.takeLast(4).joinToString(" ") { PodcastCue.strip(it.text) }.lowercase()
            assertTrue("${p.topicId} does not say \"today\" near the end", "today" in ending)
            // A step is named by one of its longer words; a step made only of
            // short words ("Said who you are") has to be said whole, less its
            // first word, which the episode says as an instruction ("say who
            // you are").
            val steps = tryIt.checklist.count { step ->
                val words = step.lowercase().split(" ")
                val long = words.filter { it.length > 5 }
                if (long.isEmpty()) words.drop(1).joinToString(" ") in ending else long.any { it in ending }
            }
            assertTrue("${p.topicId} names only $steps of ${tryIt.checklist.size} try-it steps", steps * 2 >= tryIt.checklist.size)
        }
    }
}
