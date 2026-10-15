package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The shape of every episode: long enough to be worth pressing play, short
 * enough for a walk, two voices taking turns, cues the render tool knows,
 * words it can say, the topic's own terms, and the topic's thing to try
 * today at the end. Whether each line is true is checked against its topic
 * by reading it; this holds everything that can be counted.
 */
class PodcastTest {
    /**
     * The AZ-204 topics that have an episode. Named, so that the list grows
     * to all 22 on purpose, one episode at a time.
     */
    private val withEpisodes = listOf("az-app-service", "az-slots-scaling", "az-functions", "az-blob-basics", "az-keyvault")

    private val az = Az204.course

    private fun topic(id: String): Topic = az.modules.flatMap { it.topics }.first { it.id == id }

    @Test
    fun `every named topic has exactly one episode, and every episode belongs to an AZ-204 topic`() {
        withEpisodes.forEach { id -> assertNotNull("no episode for $id", Podcasts.forTopic(id)) }
        val ids = az.modules.flatMap { m -> m.topics.map { it.id } }.toSet()
        Podcasts.all.forEach { assertTrue("${it.topicId} is not an AZ-204 topic", it.topicId in ids) }
        assertEquals("an episode is listed twice", Podcasts.all.size, Podcasts.all.map { it.topicId }.toSet().size)
        assertEquals("an episode exists for a topic not in the named list", withEpisodes.toSet(), Podcasts.all.map { it.topicId }.toSet())
    }

    @Test
    fun `an episode runs five to seven minutes`() {
        Podcasts.all.forEach { p ->
            assertTrue("${p.topicId} has ${p.spokenWords} words", p.spokenWords in 700..1_100)
        }
    }

    @Test
    fun `Maya opens, the two voices take turns, and no line runs on`() {
        Podcasts.all.forEach { p ->
            assertEquals("${p.topicId} should open with Maya", Host.MAYA, p.lines.first().host)
            p.lines.zipWithNext().forEach { (a, b) -> assertTrue("${p.topicId}: two lines in a row from ${a.host}: \"${b.text.take(40)}\"", a.host != b.host) }
            p.lines.forEach { l ->
                val words = PodcastCue.strip(l.text).split(" ").size
                assertTrue("${p.topicId}: a line of $words words: \"${l.text.take(50)}\"", words <= 70)
            }
        }
    }

    @Test
    fun `cues are ones the render tool knows, and a laugh stays a rarity`() {
        Podcasts.all.forEach { p ->
            val cues = p.lines.flatMap { PodcastCue.cues(it.text) }
            cues.forEach { assertTrue("${p.topicId}: unknown cue [$it]", it in PodcastCue.ALL) }
            val sounds = cues.count { it in PodcastCue.SOUNDS }
            assertTrue("${p.topicId}: $sounds laughs and sighs in ${p.spokenWords} words", sounds <= p.spokenWords / 120)
            assertTrue("${p.topicId}: no cue at all, which reads flat", cues.isNotEmpty())
        }
    }

    @Test
    fun `every line is words a voice can say`() {
        val unsayable = Regex("""[_@{}<>`#\\|]|https?:|\.(com|net|io)\b""")
        Podcasts.all.forEach { p ->
            p.lines.forEach { l -> assertTrue("${p.topicId}: \"${l.text.take(60)}\" has something a voice cannot say", !unsayable.containsMatchIn(PodcastCue.strip(l.text))) }
        }
    }

    @Test
    fun `an episode uses its topic's own terms`() {
        Podcasts.all.forEach { p ->
            val terms = topic(p.topicId).blocks.filterIsInstance<Block.Glossary>().flatMap { it.terms }.map { it.term }
            val heard = p.lines.joinToString(" ") { PodcastCue.strip(it.text) }.lowercase()
            val used = terms.count { t -> t.split(" / ").any { it.lowercase() in heard } }
            assertTrue("${p.topicId} uses $used of its ${terms.size} glossary terms", used * 2 >= terms.size)
        }
    }

    @Test
    fun `an episode ends on the topic's thing to try today`() {
        Podcasts.all.forEach { p ->
            val tryIt = topic(p.topicId).blocks.filterIsInstance<Block.TryIt>().single()
            val ending = p.lines.takeLast(3).joinToString(" ") { PodcastCue.strip(it.text) }.lowercase()
            assertTrue("${p.topicId} does not say \"today\" near the end", "today" in ending)
            val steps = tryIt.checklist.count { step -> step.lowercase().split(" ").filter { it.length > 5 }.any { it in ending } }
            assertTrue("${p.topicId} names only $steps of ${tryIt.checklist.size} try-it steps at the end", steps * 2 >= tryIt.checklist.size)
        }
    }

    @Test
    fun `the transcript drops cues and tidies the space they leave`() {
        assertEquals("Ha, right.", PodcastCue.strip("[laugh] Ha , right."))
        assertEquals("One. Two.", PodcastCue.strip("One. [pause] Two."))
        assertEquals(listOf("long pause", "sigh"), PodcastCue.cues("A [long pause] b [sigh]"))
    }
}
