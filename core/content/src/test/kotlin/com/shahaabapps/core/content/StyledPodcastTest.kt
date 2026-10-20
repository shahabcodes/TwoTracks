package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The styled AZ-204 episodes: the same shape PodcastTest holds the classic
 * ones to (the hosts, the length, the cues, the topic's terms, the exam's
 * take-aways, the thing to try today), and each opening in its own style.
 */
class StyledPodcastTest {
    private val az = Az204.course
    private val styled = Podcasts.all.filter { it.series == PodcastSeries.AZ204 && it.style != PodcastStyle.CLASSIC }

    private fun topic(id: String): Topic = az.allTopics.first { it.id == id }

    @Test
    fun `a style sits beside a topic's classic episode, once`() {
        styled.forEach { p ->
            assertTrue("${p.episodeId}: ${p.topicId} has no classic episode to sit beside", Podcasts.forTopic(p.topicId) != null)
        }
        assertEquals("a topic has the same style twice", styled.size, styled.map { it.episodeId }.toSet().size)
        assertEquals("episode ids collide", Podcasts.all.size, Podcasts.all.map { it.episodeId }.toSet().size)
    }

    @Test
    fun `it opens in its style`() {
        val opening = mapOf(PodcastStyle.CASE_FILES to Regex("\\bcase\\b"), PodcastStyle.GOSSIP to Regex("\\b(hear|heard)\\b"))
        styled.forEach { p ->
            val start = p.lines.take(3).joinToString(" ") { PodcastCue.strip(it.text) }.lowercase()
            assertTrue("${p.episodeId} does not open as ${p.style.label}", opening.getValue(p.style).containsMatchIn(start))
        }
    }

    @Test
    fun `Maya opens, the two voices take turns, and no line runs on`() {
        styled.forEach { p ->
            assertEquals("${p.episodeId} should open with Maya", Host.MAYA, p.lines.first().host)
            p.lines.zipWithNext().forEach { (a, b) -> assertTrue("${p.episodeId}: two lines in a row from ${a.host}", a.host != b.host) }
            p.lines.forEach { l ->
                assertTrue("${p.episodeId}: not a Maya or Leo line", l.host != Host.JONAS)
                val words = PodcastCue.strip(l.text).split(" ").size
                assertTrue("${p.episodeId}: a line of $words words: \"${l.text.take(50)}\"", words <= 70)
            }
        }
    }

    @Test
    fun `an episode runs six to nine minutes`() {
        styled.forEach { p -> assertTrue("${p.episodeId} has ${p.spokenWords} words", p.spokenWords in 800..1_400) }
    }

    @Test
    fun `cues are ones the render tool knows, and a laugh stays a treat`() {
        styled.forEach { p ->
            val cues = p.lines.flatMap { PodcastCue.cues(it.text) }
            cues.forEach { assertTrue("${p.episodeId}: unknown cue [$it]", it in PodcastCue.SOUNDS + PodcastCue.PAUSES) }
            // A story laughs a little more than a lecture, still not on every line.
            val sounds = cues.count { it in PodcastCue.SOUNDS }
            assertTrue("${p.episodeId}: $sounds laughs and sighs in ${p.spokenWords} words", sounds <= p.spokenWords / 90)
        }
    }

    @Test
    fun `every line is words a voice can say`() {
        val unsayable = Regex("""[_@{}<>`#\\|«»]|https?:|\.(com|net|io)\b""")
        styled.forEach { p ->
            p.lines.forEach { l -> assertTrue("${p.episodeId}: \"${l.text.take(60)}\"", !unsayable.containsMatchIn(PodcastCue.strip(l.text))) }
        }
    }

    @Test
    fun `an episode uses its topic's own terms`() {
        styled.forEach { p ->
            val terms = topic(p.topicId).blocks.filterIsInstance<Block.Glossary>().flatMap { it.terms }.map { it.term }
            val heard = p.lines.joinToString(" ") { PodcastCue.strip(it.text) }.lowercase()
            val used = terms.count { t -> t.split(" / ").any { it.lowercase() in heard } }
            assertTrue("${p.episodeId} uses $used of its ${terms.size} glossary terms", used * 2 >= terms.size)
        }
    }

    @Test
    fun `it closes on the exam's take-aways and the topic's thing to try today`() {
        styled.forEach { p ->
            val asked = p.lines.takeLast(8).any { it.host == Host.LEO && "exam" in PodcastCue.strip(it.text).lowercase() }
            assertTrue("${p.episodeId}: Leo never asks about the exam near the end", asked)
            val tryIt = topic(p.topicId).blocks.filterIsInstance<Block.TryIt>().single()
            val ending = p.lines.takeLast(3).joinToString(" ") { PodcastCue.strip(it.text) }.lowercase()
            assertTrue("${p.episodeId} does not say \"today\" near the end", "today" in ending)
            val steps = tryIt.checklist.count { step -> step.lowercase().split(" ").filter { it.length > 5 }.any { it in ending } }
            assertTrue("${p.episodeId} names only $steps of ${tryIt.checklist.size} try-it steps", steps * 2 >= tryIt.checklist.size)
        }
    }
}
