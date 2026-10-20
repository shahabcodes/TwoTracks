package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The exam courses' other tellings: the same shape PodcastTest holds the
 * classic AZ-204 ones to (the hosts, the length, the cues, the topic's terms,
 * the exam's take-aways, the thing to try today), and each opening in its own
 * style. German A1 has its own test.
 */
class StyledPodcastTest {
    private val styled = Podcasts.all.filter { it.series != PodcastSeries.GERMAN_A1 && it.style != PodcastStyle.CLASSIC }
    private val topics = Courses.all.flatMap { it.allTopics }.associateBy { it.id }

    private fun topic(id: String): Topic = topics.getValue(id)

    /** Who opens and explains, and who asks: Maya and Leo, or the telling's own two. */
    private fun explainer(p: Podcast) = when (p.style) {
        PodcastStyle.LATE_NIGHT, PodcastStyle.COUPLES -> Host.NOVA
        else -> Host.MAYA
    }

    private fun asker(p: Podcast) = when (p.style) {
        PodcastStyle.LATE_NIGHT -> Host.IVY
        PodcastStyle.COUPLES -> Host.RAFE
        else -> Host.LEO
    }

    @Test
    fun `there are tellings to check`() {
        assertTrue(styled.isNotEmpty())
    }

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
        val opening = mapOf(
            PodcastStyle.CASE_FILES to Regex("\\bcase\\b"),
            PodcastStyle.GOSSIP to Regex("\\b(hear|heard)\\b"),
            PodcastStyle.LATE_NIGHT to Regex("\\b(late|night|tonight)\\b"),
            PodcastStyle.COUPLES to Regex("\\b(tonight|date|darling|love)\\b"),
        )
        styled.forEach { p ->
            val start = p.lines.take(3).joinToString(" ") { PodcastCue.strip(it.text) }.lowercase()
            assertTrue("${p.episodeId} does not open as ${p.style.label}", opening.getValue(p.style).containsMatchIn(start))
        }
    }

    @Test
    fun `the explainer opens, the two voices take turns, and no line runs on`() {
        styled.forEach { p ->
            assertEquals("${p.episodeId} should open with ${explainer(p)}", explainer(p), p.lines.first().host)
            p.lines.zipWithNext().forEach { (a, b) -> assertTrue("${p.episodeId}: two lines in a row from ${a.host}", a.host != b.host) }
            p.lines.forEach { l ->
                assertTrue("${p.episodeId}: a ${l.host} line in a ${explainer(p)} and ${asker(p)} episode", l.host == explainer(p) || l.host == asker(p))
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
            val allowed = PodcastCue.SOUNDS + PodcastCue.PAUSES + if (p.style == PodcastStyle.COUPLES) setOf(PodcastCue.KISS) else emptySet()
            cues.forEach { assertTrue("${p.episodeId}: [$it] is not a cue for ${p.style.label}", it in allowed) }
            // A story laughs a little more than a lecture, still not on every line.
            val sounds = cues.count { it in PodcastCue.SOUNDS }
            assertTrue("${p.episodeId}: $sounds laughs and sighs in ${p.spokenWords} words", sounds <= p.spokenWords / 90)
        }
    }

    @Test
    fun `a couple pays for right answers in kisses, now and then`() {
        styled.filter { it.style == PodcastStyle.COUPLES }.forEach { p ->
            val kisses = p.lines.sumOf { l -> PodcastCue.cues(l.text).count { it == PodcastCue.KISS } }
            assertTrue("${p.episodeId} has $kisses kisses; a quiz of three to eight right answers earns three to eight", kisses in 3..8)
            // A kiss rewards an answer: it never opens the episode or a line.
            p.lines.forEach { l -> assertTrue("${p.episodeId}: a line opens on a kiss", !l.text.trimStart().startsWith("[${PodcastCue.KISS}]")) }
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
            // In Couples either of them may ask; elsewhere it is the one who asks.
            val asked = p.lines.takeLast(8).any { (p.style == PodcastStyle.COUPLES || it.host == asker(p)) && "exam" in PodcastCue.strip(it.text).lowercase() }
            assertTrue("${p.episodeId}: nobody asks about the exam near the end", asked)
            val tryIt = topic(p.topicId).blocks.filterIsInstance<Block.TryIt>().single()
            val ending = p.lines.takeLast(3).joinToString(" ") { PodcastCue.strip(it.text) }.lowercase()
            assertTrue("${p.episodeId} does not say \"today\" near the end", "today" in ending)
            val steps = tryIt.checklist.count { step -> step.lowercase().split(" ").filter { it.length > 5 }.any { it in ending } }
            assertTrue("${p.episodeId} names only $steps of ${tryIt.checklist.size} try-it steps", steps * 2 >= tryIt.checklist.size)
        }
    }
}
