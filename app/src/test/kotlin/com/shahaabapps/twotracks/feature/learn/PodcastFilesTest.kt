package com.shahaabapps.twotracks.feature.learn

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.IOException

/** Episodes download once, land whole or not at all, and play from the phone after. */
class PodcastFilesTest {
    @get:Rule val tmp = TemporaryFolder()

    private val timing = """{"topicId": "az-keyvault", "seconds": 20.0, "lines": [{"line": 0, "host": "maya", "start": 0.0, "end": 9.0}]}"""

    @Test
    fun `an episode downloads its timing and audio from the release, then counts as on the phone`() {
        val asked = mutableListOf<String>()
        val files = PodcastFiles(tmp.root) { url, into ->
            asked += url
            into.writeText(if (url.endsWith(".json")) timing else "OggS-audio")
        }
        assertFalse(files.isDownloaded("az-keyvault"))
        val t = files.download("az-keyvault")
        assertEquals(20.0, t.seconds, 0.0)
        assertEquals(listOf("${PodcastFiles.RELEASE}/az-keyvault.json", "${PodcastFiles.RELEASE}/az-keyvault.ogg"), asked)
        assertTrue(files.isDownloaded("az-keyvault"))
        assertNotNull(files.timeline("az-keyvault"))
        assertEquals("OggS-audio", files.audio("az-keyvault").readText())
    }

    @Test
    fun `a download that fails halfway leaves nothing that looks finished`() {
        val files = PodcastFiles(tmp.root) { url, into ->
            if (url.endsWith(".json")) into.writeText(timing) else {
                into.writeText("half an episo")
                throw IOException("connection dropped")
            }
        }
        try {
            files.download("az-keyvault")
            fail("the dropped connection should have been reported")
        } catch (e: IOException) {
            // expected
        }
        assertFalse(files.isDownloaded("az-keyvault"))
        assertFalse(files.audio("az-keyvault").exists())
        assertTrue("no .part file is left behind", tmp.root.listFiles()!!.none { it.name.endsWith(".part") })
    }

    @Test
    fun `timing that is not a timeline is refused before the audio is fetched`() {
        var audioAsked = false
        val files = PodcastFiles(tmp.root) { url, into ->
            if (url.endsWith(".ogg")) audioAsked = true
            into.writeText(if (url.endsWith(".json")) "<html>Not Found</html>" else "audio")
        }
        try {
            files.download("az-keyvault")
            fail("an HTML error page is not a timeline")
        } catch (e: IOException) {
            // expected
        }
        assertFalse(audioAsked)
        assertFalse(files.isDownloaded("az-keyvault"))
    }

    @Test
    fun `forgetting an episode removes both files`() {
        val files = PodcastFiles(tmp.root) { url, into -> into.writeText(if (url.endsWith(".json")) timing else "audio") }
        files.download("az-keyvault")
        files.forget("az-keyvault")
        assertFalse(files.isDownloaded("az-keyvault"))
        assertTrue(tmp.root.listFiles()!!.isEmpty())
    }
}
