package com.shahaabapps.twotracks.feature.listen

import com.shahaabapps.core.content.Host
import com.shahaabapps.core.content.PodcastLine
import com.shahaabapps.core.content.PodcastTimeline
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SubtitlesTest {
    private val long = "A secret is hidden text you can read back. A key never leaves the vault, so the app asks the vault to sign for it. That is the whole difference, and it matters on the exam."
    private val lines = listOf(PodcastLine(Host.MAYA, "Right. And then?"), PodcastLine(Host.LEO, long))
    private val timeline = PodcastTimeline(20.0, listOf(PodcastTimeline.Span(0, 0.0, 2.0), PodcastTimeline.Span(1, 2.0, 20.0)))

    @Test
    fun `a short line is one piece`() {
        assertEquals(listOf("Right. And then?"), Subtitles.pieces("Right. And then?"))
    }

    @Test
    fun `a long line breaks between sentences, every piece short enough`() {
        val pieces = Subtitles.pieces(long)
        assertTrue(pieces.size >= 2)
        assertTrue(pieces.all { it.length <= Subtitles.MAX })
        assertEquals(long, pieces.joinToString(" "))
    }

    @Test
    fun `the piece follows the time through the line`() {
        val start = Subtitles.at(lines, timeline, 2.5)!!
        val end = Subtitles.at(lines, timeline, 19.5)!!
        assertEquals(Host.LEO, start.host)
        assertTrue(start.text.startsWith("A secret"))
        assertTrue(end.text.endsWith("on the exam."))
    }

    @Test
    fun `before the first line there is nothing to show`() {
        assertNull(Subtitles.at(lines, PodcastTimeline(20.0, listOf(PodcastTimeline.Span(0, 1.0, 2.0))), 0.5))
    }
}
