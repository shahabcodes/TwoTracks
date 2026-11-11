package com.shahaabapps.core.data.ink

import com.shahaabapps.core.model.InkStroke
import com.shahaabapps.core.model.LessonMark
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Writing a marked-up lesson out and reading it back.
 *
 * Each mark carries the block it belongs to, and that key is the one field
 * that cannot be approximately right: a mark whose key is lost is a mark
 * that cannot be drawn anywhere, and a mark whose key is read as the wrong
 * block is an underline under the wrong sentence.
 */
class LessonInkCodecTest {

    private val marks = listOf(
        LessonMark("title", InkStroke("C0392B", 4f, listOf(100f, 50.5f, 300f, 52f))),
        LessonMark("block-7", InkStroke("1F5FC4", 6f, listOf(0f, 0f, 999.9f, 1000f))),
    )

    @Test
    fun `marks come back on the blocks they were drawn on`() {
        val back = LessonInkCodec.decode(LessonInkCodec.encode(marks))

        assertEquals(2, back.size)
        assertEquals("title", back[0].key)
        assertEquals("block-7", back[1].key)
        assertEquals("C0392B", back[0].stroke.colour)
        assertEquals(4f, back[0].stroke.width, 0.01f)
        assertEquals(listOf(100f, 50.5f, 300f, 52f), back[0].stroke.points)
        assertEquals(listOf(0f, 0f, 999.9f, 1000f), back[1].stroke.points)
    }

    @Test
    fun `a mark with no block is not written at all`() {
        // Nowhere to draw it and nothing to anchor it to: writing it would
        // only mean reading it back and dropping it later.
        val text = LessonInkCodec.encode(listOf(LessonMark("", InkStroke("1C1C20", 3f, listOf(1f, 1f, 2f, 2f)))))

        assertTrue("a keyless mark was written: $text", LessonInkCodec.decode(text).isEmpty())
    }

    @Test
    fun `a key with a space in it cannot be written, because it cannot be read`() {
        // The format is space-separated, so a key with a space would read
        // back as a key and a colour that are both wrong.
        val text = LessonInkCodec.encode(listOf(LessonMark("block 7", InkStroke("1C1C20", 3f, listOf(1f, 1f, 2f, 2f)))))

        assertTrue("a key with a space was written: $text", LessonInkCodec.decode(text).isEmpty())
    }

    @Test
    fun `a damaged line costs that mark and nothing else`() {
        val text = """
            mark1
            m title C0392B 4 10,10 11,11
            m broken
            rubbish
            m block-2 1F5FC4 2 40,40 41,41
        """.trimIndent()

        val back = LessonInkCodec.decode(text)

        assertEquals(2, back.size)
        assertEquals("title", back[0].key)
        assertEquals("block-2", back[1].key)
    }

    @Test
    fun `an unmarked lesson reads as no marks rather than failing`() {
        assertTrue(LessonInkCodec.decode("").isEmpty())
        assertTrue(LessonInkCodec.decode("mark1\n").isEmpty())
        assertTrue(LessonInkCodec.encode(emptyList()).isNotBlank())
    }
}
