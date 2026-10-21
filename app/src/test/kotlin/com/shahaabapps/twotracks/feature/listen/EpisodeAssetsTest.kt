package com.shahaabapps.twotracks.feature.listen

import com.shahaabapps.core.content.Az204
import com.shahaabapps.core.content.GermanA1
import com.shahaabapps.core.content.PodcastSeries
import com.shahaabapps.core.content.PodcastStyle
import com.shahaabapps.core.content.PodcastTimeline
import com.shahaabapps.core.content.Podcasts
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * The episodes the APK knows. It carries each one's timing, never its audio:
 * the sound is in the couple space's Firebase Storage, readable only by its
 * members (EpisodeAudio, firebase/storage.rules), so it is not public with the
 * code or the APK. A script edited after its render would leave the
 * transcript lighting the wrong line for the rest of the episode, and nothing
 * on a screen would say so; here it fails the build.
 */
class EpisodeAssetsTest {
    private val dir = File("src/main/assets/${Episodes.DIR}")

    private fun installed(id: String) = File(dir, "$id.json").isFile

    @Test
    fun `no audio ships in the app`() {
        val audio = dir.listFiles { f -> f.extension.lowercase() in setOf("ogg", "opus", "mp3", "m4a", "wav") }.orEmpty().map { it.name }
        assertTrue("audio would be public in the APK: $audio; it belongs in Firebase Storage (tools/podcast/upload.ps1)", audio.isEmpty())
    }

    @Test
    fun `every AZ-204 topic has its episode in the app`() {
        val missing = Az204.course.modules.flatMap { it.topics }.map { it.id }.filterNot(::installed)
        assertTrue("no episode in the app for $missing; render it and run tools/podcast/install.sh", missing.isEmpty())
    }

    @Test
    fun `every German A1 topic has its episode in the app`() {
        val missing = GermanA1.course.allTopics.map { it.id }.filterNot(::installed)
        assertTrue("no episode in the app for $missing; run render_de.py and render.py, then tools/podcast/install.sh", missing.isEmpty())
    }

    @Test
    fun `every AZ-204 telling is in the app`() {
        // AI-200's and AZ-305's episodes arrive as they render; the player only
        // offers the tellings whose timing is here, so a partly rendered course
        // shows fewer choices rather than a choice that plays nothing.
        val missing = Podcasts.all.filter { it.series == PodcastSeries.AZ204 && it.style != PodcastStyle.CLASSIC }.map { it.episodeId }.filterNot(::installed)
        assertTrue("no episode in the app for $missing; render them and run tools/podcast/install.sh", missing.isEmpty())
    }

    @Test
    fun `each episode's timing matches its script line for line`() {
        val timings = dir.listFiles { f -> f.extension == "json" }.orEmpty()
        assertTrue("no episodes found in $dir", timings.isNotEmpty())
        for (file in timings) {
            val id = file.nameWithoutExtension
            val podcast = Podcasts.episode(id)
            assertNotNull("$id has a timing in the app but no script", podcast)
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
    fun `the speeds are the ones the system draws, stepping round in order`() {
        assertEquals(listOf(0.8f, 1.0f, 1.2f, 1.5f, 1.8f, 2.0f), ListenSpeeds.ALL)
        assertEquals(1.2f, ListenSpeeds.next(1.0f))
        assertEquals(0.8f, ListenSpeeds.next(2.0f))
        assertEquals(1.5f, ListenSpeeds.nearest(1.45f))
        assertEquals("1×", ListenSpeeds.label(1f))
        assertEquals("1.5×", ListenSpeeds.label(1.5f))
    }
}
