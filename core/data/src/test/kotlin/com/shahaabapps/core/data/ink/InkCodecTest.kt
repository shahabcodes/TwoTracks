package com.shahaabapps.core.data.ink

import com.shahaabapps.core.model.InkPage
import com.shahaabapps.core.model.InkStroke
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Writing a page of handwriting out and reading it back.
 *
 * This is the one place a note can be lost. The strokes are the only copy of
 * something nobody else has — it is the person's own handwriting, not a file
 * they could fetch again — so the round trip is held to the coordinates, and
 * a damaged file is held to giving back everything that is still readable
 * rather than nothing at all.
 */
class InkCodecTest {

    private val page = InkPage(
        strokes = listOf(
            InkStroke("1C1C20", 3.2f, listOf(10f, 20.5f, 11.25f, 21f)),
            InkStroke("C0392B", 6f, listOf(0f, 0f, 999.9f, 1413.8f)),
        ),
    )

    @Test
    fun `a page comes back as the page that went in`() {
        val back = InkCodec.decode(InkCodec.encode(page))

        assertEquals(page.width, back.width, 0.01f)
        assertEquals(page.height, back.height, 0.01f)
        assertEquals(2, back.strokes.size)
        assertEquals("1C1C20", back.strokes[0].colour)
        assertEquals(3.2f, back.strokes[0].width, 0.01f)
        // A tenth of a page unit is a fifth of a pixel on a phone: the
        // rounding is deliberate, and this says how much of it there is.
        assertEquals(listOf(10f, 20.5f, 11.3f, 21f), back.strokes[0].points)
        assertEquals(listOf(0f, 0f, 999.9f, 1413.8f), back.strokes[1].points)
    }

    @Test
    fun `a stroke that went off the left edge keeps its sign`() {
        // -0.5 rounds to -5 tenths, whose integer part is zero: written
        // carelessly this comes back as 0.5, on the other side of the page.
        val off = InkPage(strokes = listOf(InkStroke("1C1C20", 2f, listOf(-0.5f, -12.3f, 4f, 4f))))

        val back = InkCodec.decode(InkCodec.encode(off))

        assertEquals(listOf(-0.5f, -12.3f, 4f, 4f), back.strokes[0].points)
    }

    @Test
    fun `an empty page is still a page`() {
        val back = InkCodec.decode(InkCodec.encode(InkPage()))

        assertTrue("an empty page came back with strokes on it", back.strokes.isEmpty())
        assertEquals(InkPage.PAGE_WIDTH, back.width, 0.01f)
    }

    @Test
    fun `a damaged line costs that stroke and nothing else`() {
        val text = """
            ink1
            size 1000 1414
            s 1C1C20 3 10,10 11,11
            s broken
            nonsense
            s C0392B 2 40,40 41,41
        """.trimIndent()

        val back = InkCodec.decode(text)

        // Ninety-nine good strokes are not worth throwing away over one bad
        // one, and there is no second copy of them anywhere.
        assertEquals(2, back.strokes.size)
        assertEquals("1C1C20", back.strokes[0].colour)
        assertEquals("C0392B", back.strokes[1].colour)
    }

    @Test
    fun `a coordinate that is not a number is skipped, not guessed at`() {
        val back = InkCodec.decode("ink1\nsize 1000 1414\ns 1C1C20 3 10,10 x,y 12,12\n")

        assertEquals(listOf(10f, 10f, 12f, 12f), back.strokes[0].points)
    }

    @Test
    fun `a stroke with one point is not written out`() {
        // A tap is not a stroke: it draws nothing and it would read back as
        // a stroke with half a coordinate.
        val text = InkCodec.encode(InkPage(strokes = listOf(InkStroke("1C1C20", 3f, listOf(5f)))))

        assertTrue("a one-point stroke was written: $text", !text.contains("s 1C1C20"))
    }

    @Test
    fun `nothing at all decodes to an empty page rather than throwing`() {
        assertEquals(0, InkCodec.decode("").strokes.size)
        assertEquals(0, InkCodec.decode("not an ink file").strokes.size)
    }

    @Test
    fun `the file stays small enough to be worth sending`() {
        // A long page: 200 strokes of 60 points. This is the reason the
        // format is numbers and spaces rather than JSON.
        val long = InkPage(
            strokes = List(200) { s ->
                InkStroke("1C1C20", 3.2f, List(120) { i -> (i * 7 + s) % 1000 + 0.5f })
            },
        )

        val size = InkCodec.encode(long).length

        assertTrue("a 200-stroke page is $size characters", size < 400_000)
    }
}
