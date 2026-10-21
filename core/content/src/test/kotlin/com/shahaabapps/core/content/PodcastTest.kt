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
    private val withEpisodes = listOf("az-app-service", "az-slots-scaling", "az-functions", "az-durable", "az-containers", "az-apim", "az-eventgrid", "az-eventhubs", "az-servicebus", "az-appinsights", "az-availability", "az-redis", "az-cdn", "az-identity", "az-msal", "az-graph", "az-keyvault", "az-appconfig", "az-blob-basics", "az-blob-sdk", "az-cosmos-model", "az-cosmos-sdk")

    private val az = Az204.course

    /**
     * This test holds the exam courses' classic episodes (AZ-204, AI-200);
     * StyledPodcastTest holds the other styles, GermanPodcastTest the German ones.
     */
    private val classic = Podcasts.all.filter { it.series != PodcastSeries.GERMAN_A1 && it.style == PodcastStyle.CLASSIC }
    private val azEpisodes = classic.filter { it.series == PodcastSeries.AZ204 }
    private val topics = Courses.all.flatMap { it.allTopics }.associateBy { it.id }

    private fun topic(id: String): Topic = topics.getValue(id)

    @Test
    fun `every AI-200 topic has all five tellings`() {
        Ai200.course.allTopics.forEach { t ->
            assertEquals("${t.id}'s tellings", PodcastStyle.entries.toSet(), Podcasts.styles(t.id).map { it.style }.toSet())
            Podcasts.styles(t.id).forEach { assertEquals("${it.episodeId} is not in the AI-200 series", PodcastSeries.AI200, it.series) }
        }
    }

    @Test
    fun `an exam course's episodes belong to its own series`() {
        mapOf(PodcastSeries.AZ204 to Az204.course, PodcastSeries.AI200 to Ai200.course).forEach { (series, course) ->
            val ids = course.allTopics.map { it.id }.toSet()
            classic.filter { it.series == series }.forEach { assertTrue("${it.topicId} is not a ${course.id} topic", it.topicId in ids) }
        }
    }

    @Test
    fun `every AZ-204 topic has an episode`() {
        val topics = az.modules.flatMap { m -> m.topics.map { it.id } }
        assertEquals("the named list has fallen behind the course", topics.toSet(), withEpisodes.toSet())
    }

    @Test
    fun `every named topic has exactly one episode, and every episode belongs to an AZ-204 topic`() {
        withEpisodes.forEach { id -> assertNotNull("no episode for $id", Podcasts.forTopic(id)) }
        val ids = az.modules.flatMap { m -> m.topics.map { it.id } }.toSet()
        azEpisodes.forEach { assertTrue("${it.topicId} is not an AZ-204 topic", it.topicId in ids) }
        assertEquals("an episode is listed twice", azEpisodes.size, azEpisodes.map { it.topicId }.toSet().size)
        assertEquals("an episode exists for a topic not in the named list", withEpisodes.toSet(), azEpisodes.map { it.topicId }.toSet())
    }

    @Test
    fun `an episode runs six to nine minutes`() {
        // Longer than the first versions: written for a beginner, every term
        // is explained, with simple-app examples and the exam's take-aways.
        classic.forEach { p ->
            assertTrue("${p.topicId} has ${p.spokenWords} words", p.spokenWords in 800..1_400)
        }
    }

    @Test
    fun `an episode shows when to use what with simple apps`() {
        val example = Regex("""\b(say you're building|imagine you're building|you're building|for example|picture a|say your app|a small app)\b""")
        classic.forEach { p ->
            val heard = p.lines.joinToString(" ") { PodcastCue.strip(it.text) }.lowercase()
            val found = example.findAll(heard).count()
            assertTrue("${p.topicId} has $found simple-app examples", found >= 2)
        }
    }

    @Test
    fun `an episode closes with the exam's take-aways`() {
        classic.forEach { p ->
            val asked = p.lines.takeLast(8).any { it.host == Host.LEO && "exam" in PodcastCue.strip(it.text).lowercase() }
            assertTrue("${p.topicId}: Leo never asks what to remember for the exam near the end", asked)
        }
    }

    @Test
    fun `Maya opens, the two voices take turns, and no line runs on`() {
        classic.forEach { p ->
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
        classic.forEach { p ->
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
        classic.forEach { p ->
            p.lines.forEach { l -> assertTrue("${p.topicId}: \"${l.text.take(60)}\" has something a voice cannot say", !unsayable.containsMatchIn(PodcastCue.strip(l.text))) }
        }
    }

    @Test
    fun `an episode uses its topic's own terms`() {
        classic.forEach { p ->
            val terms = topic(p.topicId).blocks.filterIsInstance<Block.Glossary>().flatMap { it.terms }.map { it.term }
            val heard = p.lines.joinToString(" ") { PodcastCue.strip(it.text) }.lowercase()
            val used = terms.count { t -> t.split(" / ").any { it.lowercase() in heard } }
            assertTrue("${p.topicId} uses $used of its ${terms.size} glossary terms", used * 2 >= terms.size)
        }
    }

    @Test
    fun `an episode ends on the topic's thing to try today`() {
        classic.forEach { p ->
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
