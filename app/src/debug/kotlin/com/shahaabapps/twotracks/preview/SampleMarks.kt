package com.shahaabapps.twotracks.preview

import com.shahaabapps.core.model.InkStroke
import com.shahaabapps.core.model.LessonMark
import kotlin.math.cos
import kotlin.math.sin

/**
 * Debug builds only: a lesson somebody has already been over with a pen, so
 * the marks can be looked at without a stylus and without an account.
 *
 * Underlines and a ring, because that is what marking up a lesson actually
 * is — not drawing. They are deliberately a little crooked: a mark drawn
 * with a ruler would hide the thing worth judging, which is whether a
 * hand-drawn line sits properly on the text under it.
 */
object SampleMarks {

    private const val RED = "C0392B"
    private const val BLUE = "1F5FC4"

    fun marks(): List<LessonMark> = listOf(
        LessonMark("title", underline(RED, 5f, 40f, 720f, 760f, 4f)),
        LessonMark("block-0", underline(BLUE, 4f, 60f, 880f, 300f, 6f)),
        LessonMark("block-0", ring(RED, 5f, 480f, 560f, 300f, 110f)),
        LessonMark("block-1", underline(RED, 4f, 50f, 820f, 620f, 5f)),
    )

    /** A line under a line of text, with the wobble a hand puts in it. */
    private fun underline(colour: String, width: Float, from: Float, to: Float, y: Float, wobble: Float) =
        InkStroke(colour, width, buildList {
            var at = from
            while (at <= to) {
                add(at)
                add(y + sin(at / 60f) * wobble)
                at += 12f
            }
        })

    /** Something circled, closed roughly rather than exactly. */
    private fun ring(colour: String, width: Float, cx: Float, cy: Float, rx: Float, ry: Float) =
        InkStroke(colour, width, buildList {
            var angle = 0f
            while (angle < 6.6f) {
                add(cx + cos(angle) * rx * (1f + sin(angle * 3f) * 0.04f))
                add(cy + sin(angle) * ry * (1f + cos(angle * 2f) * 0.05f))
                angle += 0.12f
            }
        })
}
