package com.shahaabapps.core.data.ink

import com.shahaabapps.core.model.InkPage
import com.shahaabapps.core.model.InkStroke

/**
 * Reading and writing a page of handwriting as text.
 *
 * Text rather than JSON, and hand-written rather than generated. The format
 * is almost all numbers — a page is one line per stroke and tens of
 * thousands of coordinates — so the braces and quotes of JSON would be most
 * of the bytes, and the parser being thirty lines of Kotlin with no library
 * under it is what lets the round trip be tested on a plain JVM rather than
 * on a device.
 *
 * Unparseable lines are skipped rather than thrown over. A page with one
 * damaged stroke is still a page of somebody's handwriting, and losing the
 * other ninety-nine would be the worse answer.
 */
object InkCodec {

    private const val HEADER = "ink1"

    fun encode(page: InkPage): String = buildString {
        append(HEADER).append('\n')
        append("size ").append(trim(page.width)).append(' ').append(trim(page.height)).append('\n')
        for (stroke in page.strokes) {
            if (stroke.isEmpty) continue
            append("s ").append(stroke.colour).append(' ').append(trim(stroke.width))
            var i = 0
            while (i + 1 < stroke.points.size) {
                append(' ').append(trim(stroke.points[i])).append(',').append(trim(stroke.points[i + 1]))
                i += 2
            }
            append('\n')
        }
    }

    fun decode(text: String): InkPage {
        var width = InkPage.PAGE_WIDTH
        var height = InkPage.PAGE_HEIGHT
        val strokes = mutableListOf<InkStroke>()
        for (line in text.lineSequence()) {
            val parts = line.trim().split(' ')
            when {
                parts.size >= 3 && parts[0] == "size" -> {
                    width = parts[1].toFloatOrNull() ?: width
                    height = parts[2].toFloatOrNull() ?: height
                }
                parts.size >= 4 && parts[0] == "s" -> {
                    val points = ArrayList<Float>((parts.size - 3) * 2)
                    for (i in 3 until parts.size) {
                        val comma = parts[i].indexOf(',')
                        if (comma <= 0) continue
                        val x = parts[i].substring(0, comma).toFloatOrNull() ?: continue
                        val y = parts[i].substring(comma + 1).toFloatOrNull() ?: continue
                        points.add(x)
                        points.add(y)
                    }
                    val stroke = InkStroke(
                        colour = parts[1].removePrefix("#"),
                        width = parts[2].toFloatOrNull() ?: 3f,
                        points = points,
                    )
                    if (!stroke.isEmpty) strokes.add(stroke)
                }
            }
        }
        return InkPage(width, height, strokes)
    }

    /**
     * A coordinate as short as it can be without being wrong: a tenth of a
     * page unit is a fifth of a pixel on a phone, and the digits after it
     * are several hundred kilobytes of nothing across a page.
     */
    private fun trim(value: Float): String {
        val tenths = Math.round(value * 10f)
        if (tenths % 10 == 0) return (tenths / 10).toString()
        // Built from the absolute value and signed back: -0.5 rounds to -5
        // tenths, whose integer part is 0, and "0.5" is the wrong side of
        // the page.
        val sign = if (tenths < 0) "-" else ""
        val size = Math.abs(tenths)
        return "$sign${size / 10}.${size % 10}"
    }
}
