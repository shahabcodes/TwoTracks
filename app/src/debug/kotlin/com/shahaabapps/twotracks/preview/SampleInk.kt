package com.shahaabapps.twotracks.preview

import com.shahaabapps.core.model.InkPage
import com.shahaabapps.core.model.InkStroke
import kotlin.math.sin

/**
 * Debug builds only: a page with handwriting on it, so the note screen can
 * be looked at and compared without a stylus and without an account.
 *
 * Made rather than recorded, because what has to be judged is the drawing —
 * the weight of the line, how the ink sits on the paper, whether the two
 * looks agree — and a straight line would answer none of that. The shapes
 * are deliberately handwriting-ish: a heading rule, a couple of boxes, an
 * arrow, and a line of wobble where words would be.
 */
object SampleInk {

    private const val INK = "1C1C20"
    private const val RED = "C0392B"
    private const val BLUE = "1F5FC4"

    fun page(): InkPage = InkPage(strokes = buildList {
        add(line(INK, 5f, 80f, 150f, 700f, 150f))
        add(box(INK, 3.2f, 90f, 260f, 400f, 420f))
        add(box(INK, 3.2f, 560f, 260f, 880f, 420f))
        add(line(BLUE, 4f, 400f, 340f, 560f, 340f))
        add(line(BLUE, 4f, 520f, 310f, 560f, 340f))
        add(line(BLUE, 4f, 520f, 370f, 560f, 340f))
        add(scribble(INK, 3.2f, 120f, 520f, 620f, 18f))
        add(scribble(INK, 3.2f, 120f, 600f, 540f, 14f))
        add(scribble(RED, 5f, 120f, 720f, 420f, 26f))
        add(line(RED, 5f, 110f, 790f, 520f, 795f))
    })

    private fun line(colour: String, width: Float, x1: Float, y1: Float, x2: Float, y2: Float) =
        InkStroke(colour, width, buildList {
            // Sampled along its length rather than as two ends, so it is the
            // shape a pen actually hands over.
            for (i in 0..20) {
                val t = i / 20f
                add(x1 + (x2 - x1) * t)
                add(y1 + (y2 - y1) * t)
            }
        })

    private fun box(colour: String, width: Float, left: Float, top: Float, right: Float, bottom: Float) =
        InkStroke(colour, width, buildList {
            val corners = listOf(
                left to top, right to top, right to bottom, left to bottom, left to top,
            )
            for (i in 0 until corners.size - 1) {
                val (ax, ay) = corners[i]
                val (bx, by) = corners[i + 1]
                for (step in 0..12) {
                    val t = step / 12f
                    add(ax + (bx - ax) * t)
                    add(ay + (by - ay) * t)
                }
            }
        })

    /** A line of wobble where a line of words would be. */
    private fun scribble(colour: String, width: Float, x: Float, y: Float, length: Float, height: Float) =
        InkStroke(colour, width, buildList {
            var at = 0f
            while (at <= length) {
                add(x + at)
                add(y + sin(at / 11f) * height)
                at += 4f
            }
        })
}
