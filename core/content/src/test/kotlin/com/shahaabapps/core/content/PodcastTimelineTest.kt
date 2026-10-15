package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Which line the transcript lights up, and where a tap on a line seeks to. */
class PodcastTimelineTest {
    // The shape render.py writes, with indent=1 as it does.
    private val json = """
        {
         "topicId": "az-keyvault",
         "seconds": 120.5,
         "lines": [
          {"line": 0, "host": "maya", "start": 0.0, "end": 6.4},
          {"line": 1, "host": "leo", "start": 6.72, "end": 9.1},
          {"line": 2, "host": "maya", "start": 9.42, "end": 30.0}
         ]
        }
    """.trimIndent()

    private val t = PodcastTimeline.parse(json)!!

    @Test
    fun `the timing file is read as the render tool writes it`() {
        assertEquals(120.5, t.seconds, 0.0)
        assertEquals(listOf(0, 1, 2), t.spans.map { it.line })
        assertEquals(6.72, t.spans[1].start, 0.0)
    }

    @Test
    fun `the line being spoken is lit, and in a hand-over the one just finished stays lit`() {
        assertEquals(0, t.lineAt(0.0))
        assertEquals(0, t.lineAt(6.5))
        assertEquals(1, t.lineAt(6.72))
        assertEquals(1, t.lineAt(9.3))
        assertEquals(2, t.lineAt(29.9))
        assertEquals(2, t.lineAt(200.0))
    }

    @Test
    fun `a tap on a line seeks to where it starts`() {
        assertEquals(9.42, t.startOf(2)!!, 0.0)
        assertNull(t.startOf(7))
    }

    @Test
    fun `a saved position resumes only from the middle`() {
        assertFalse(t.resumable(3.0))
        assertTrue(t.resumable(40.0))
        assertFalse(t.resumable(110.0))
    }

    @Test
    fun `the real layout, a field to a line, reads the same`() {
        val real = "{\n \"topicId\": \"az-keyvault\",\n \"seconds\": 283.69,\n \"lines\": [\n  {\n   \"line\": 0,\n   \"host\": \"maya\",\n   \"start\": 0.0,\n   \"end\": 7.24\n  },\n  {\n   \"line\": 1,\n   \"host\": \"leo\",\n   \"start\": 7.56,\n   \"end\": 11.59\n  }\n ]\n}"
        val r = PodcastTimeline.parse(real)!!
        assertEquals(283.69, r.seconds, 0.0)
        assertEquals(listOf(0.0, 7.56), r.spans.map { it.start })
        assertEquals(1, r.lineAt(8.0))
    }

    @Test
    fun `a file that is not a timeline gives nothing rather than a wrong one`() {
        assertNull(PodcastTimeline.parse("{}"))
        assertNull(PodcastTimeline.parse("""{"seconds": 10, "lines": []}"""))
        val overlapping = """{"seconds": 10, "lines": [{"line": 0, "host": "maya", "start": 0, "end": 5}, {"line": 1, "host": "leo", "start": 4, "end": 8}]}"""
        assertNull(PodcastTimeline.parse(overlapping))
    }
}
