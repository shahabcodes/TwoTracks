package com.shahaabapps.twotracks.feature.listen

import com.shahaabapps.core.content.Az204
import com.shahaabapps.core.content.GermanA1
import com.shahaabapps.core.content.PodcastTimeline
import com.shahaabapps.core.content.PodcastStyle
import com.shahaabapps.core.content.Podcasts
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * The episodes the APK carries. A script edited after its render would
 * leave the transcript lighting the wrong line for the rest of the episode,
 * and nothing on a screen would say so; here it fails the build.
 */
class EpisodeAssetsTest {
    private val dir = File("src/main/assets/${Episodes.DIR}")

    /**
     * Rendered on a laptop GPU a few minutes an episode, so these follow in a
     * later build. Named, so the list can only shrink: a topic here that has
     * its episode fails too, and an empty list is the finished course.
     */
    private val stillRendering = emptySet<String>()

    private fun installed(id: String) = File(dir, "$id.ogg").isFile && File(dir, "$id.json").isFile

    @Test
    fun `every AZ-204 topic has its episode in the app, or is named as still rendering`() {
        val topics = Az204.course.modules.flatMap { it.topics }.map { it.id }
        val missing = topics.filterNot(::installed).filterNot { it in stillRendering }
        assertTrue("no episode in the app for $missing; render it and run tools/podcast/install.sh", missing.isEmpty())
        val done = stillRendering.filter(::installed)
        assertTrue("$done have their episodes now; take them off the list", done.isEmpty())
        assertTrue("$stillRendering are not AZ-204 topics", topics.containsAll(stillRendering))
    }

    @Test
    fun `every German A1 topic has its episode in the app`() {
        val missing = GermanA1.course.allTopics.map { it.id }.filterNot(::installed)
        assertTrue("no episode in the app for $missing; run render_de.py and render.py, then tools/podcast/install.sh", missing.isEmpty())
    }

    @Test
    fun `every styled episode is in the app`() {
        // A style is only written once it is rendered too: a script without its
        // audio would be a choice in the player that plays nothing.
        val missing = Podcasts.all.filter { it.style != PodcastStyle.CLASSIC }.map { it.episodeId }.filterNot(::installed)
        assertTrue("no audio in the app for $missing; render them and run tools/podcast/install.sh", missing.isEmpty())
    }

    @Test
    fun `each episode's timing matches its script line for line`() {
        val timings = dir.listFiles { f -> f.extension == "json" }.orEmpty()
        assertTrue("no episodes found in $dir", timings.isNotEmpty())
        for (file in timings) {
            val id = file.nameWithoutExtension
            val podcast = Podcasts.episode(id)
            assertNotNull("$id has audio in the app but no script", podcast)
            val text = file.readText()
            assertTrue("$id: the timing names another topic", text.contains("\"topicId\": \"$id\"") || text.contains("\"topicId\":\"$id\""))
            val timeline = PodcastTimeline.parse(text)
            assertNotNull("$id: the timing does not read as a timeline", timeline)
            assertEquals(
                "$id: the script has ${podcast!!.lines.size} lines and the render ${timeline!!.spans.size}; render it again",
                podcast.lines.indices.toList(), timeline.spans.map { it.line },
            )
        }
    }

    @Test
    fun `each episode's audio is Opus, as the player expects`() {
        for (file in dir.listFiles { f -> f.extension == "ogg" }.orEmpty()) {
            val head = file.inputStream().use { it.readNBytes(64) }
            assertEquals("${file.name} is not an Ogg file", "OggS", String(head, 0, 4, Charsets.US_ASCII))
            assertTrue("${file.name} is Ogg but not Opus", String(head, Charsets.ISO_8859_1).contains("OpusHead"))
            assertTrue("${file.name} has a timing beside it", File(dir, "${file.nameWithoutExtension}.json").isFile)
        }
    }

    @Test
    fun `the speeds are the ones the system draws, stepping round in order`() {
        assertEquals(listOf(0.8f, 1.0f, 1.2f, 1.5f, 1.8f, 2.0f), ListenSpeeds.ALL)
        assertEquals(1.2f, ListenSpeeds.next(1.0f))
        assertEquals(0.8f, ListenSpeeds.next(2.0f))
        assertEquals(1.5f, ListenSpeeds.nearest(1.45f))
        assertEquals("1×", ListenSpeeds.label(1f))
        assertEquals("1.5×", ListenSpeeds.label(1.5f))
    }
}
