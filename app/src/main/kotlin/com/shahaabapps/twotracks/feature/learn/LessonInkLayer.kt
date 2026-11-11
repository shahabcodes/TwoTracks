package com.shahaabapps.twotracks.feature.learn

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import com.shahaabapps.core.model.InkStroke
import com.shahaabapps.core.model.LessonMark

/**
 * One block of the lesson, as it sits on the screen at this moment.
 *
 * [top] and [height] are pixels in the viewport; a block scrolled off the
 * top has a negative [top], which is correct and useful — a mark on it is
 * drawn partly on screen rather than not at all.
 */
internal data class InkedBlock(val key: String, val top: Int, val height: Int)

/** The blocks of [state] that are on screen now, in order. */
internal fun blocksOf(state: LazyListState): List<InkedBlock> =
    state.layoutInfo.visibleItemsInfo.mapNotNull { item ->
        val key = item.key as? String ?: return@mapNotNull null
        InkedBlock(key, item.offset, item.size)
    }

/**
 * Which block a point on the screen is on, and where in that block it is.
 *
 * The answer is in the block's own 0-to-[LessonMark.SPAN] space across and
 * down, so it survives the lesson being a different length tomorrow. A point
 * past the last block or in the gap between two belongs to the block above
 * it, the way a word at the foot of a page belongs to that page.
 */
internal fun anchor(blocks: List<InkedBlock>, width: Int, at: Offset): Pair<String, Offset>? {
    if (blocks.isEmpty() || width <= 0) return null
    val on = blocks.firstOrNull { at.y >= it.top && at.y < it.top + it.height }
        ?: blocks.lastOrNull { it.top <= at.y }
        ?: blocks.first()
    return on.key to into(on, width, at)
}

/** A viewport point in [block]'s own space. */
internal fun into(block: InkedBlock, width: Int, at: Offset): Offset {
    val height = block.height.coerceAtLeast(1)
    return Offset(
        at.x / width * LessonMark.SPAN,
        (at.y - block.top) / height * LessonMark.SPAN,
    )
}

/** And back again: a point in [block]'s space, on the screen as it is now. */
internal fun outOf(block: InkedBlock, width: Int, point: Offset): Offset = Offset(
    point.x / LessonMark.SPAN * width,
    block.top + point.y / LessonMark.SPAN * block.height,
)

/**
 * A mark's points where they are on the screen now, or null when the block
 * it belongs to is not on screen.
 */
internal fun onScreen(mark: LessonMark, blocks: List<InkedBlock>, width: Int): List<Float>? {
    val block = blocks.firstOrNull { it.key == mark.key } ?: return null
    val out = ArrayList<Float>(mark.stroke.points.size)
    var i = 0
    while (i + 1 < mark.stroke.points.size) {
        val at = outOf(block, width, Offset(mark.stroke.points[i], mark.stroke.points[i + 1]))
        out.add(at.x)
        out.add(at.y)
        i += 2
    }
    return out
}

/**
 * The marks drawn over a lesson, and the pen that puts them there.
 *
 * It sits over the lesson rather than inside it, and takes no input at all
 * until [pen] is on — so a lesson being read is a lesson being read, with
 * nothing between the finger and the text.
 *
 * With the pen out, one finger draws and **two fingers scroll**. That is the
 * idiom every drawing app uses and it is the only one that works here: the
 * alternative is putting the pen away every time you want to move down the
 * page, which for marking up a lesson is most of the time. Where there is a
 * stylus, it draws and one finger still scrolls, because a tablet with a pen
 * in your hand should not need the convention at all.
 */
@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
internal fun LessonInkLayer(
    marks: List<LessonMark>,
    pen: Boolean,
    tool: InkTool,
    listState: LazyListState,
    onMark: (LessonMark) -> Unit,
    onErase: (List<LessonMark>) -> Unit,
    modifier: Modifier = Modifier,
) {
    /** The stroke under the nib, in viewport pixels, drawn as it grows. */
    var live by remember { mutableStateOf<List<Float>>(emptyList()) }
    var sawStylus by remember { mutableStateOf(false) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .testTag(LESSON_INK_LAYER)
            .then(
                if (!pen) Modifier else Modifier.pointerInput(tool, marks, listState) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val stylus = down.type == PointerType.Stylus || down.type == PointerType.Eraser
                        if (stylus) sawStylus = true
                        // A finger on a page that has seen a pen is the hand
                        // holding the tablet, or a scroll. Either way it is
                        // not a mark.
                        if (sawStylus && !stylus) return@awaitEachGesture

                        val blocks = blocksOf(listState)
                        val width = size.width
                        val erasing = tool.erasing || down.type == PointerType.Eraser
                        val id = down.id
                        val points = mutableListOf(down.position.x, down.position.y)
                        var pressure = down.pressure
                        var readings = 1
                        down.consume()
                        live = points.toList()

                        var handedOver = false
                        while (true) {
                            val event = awaitPointerEvent()
                            // A second finger means scrolling, not a wider
                            // stroke: the mark so far is abandoned and the
                            // events stop being consumed, so the lesson
                            // under this takes the gesture from here.
                            if (!sawStylus && event.changes.count { it.pressed } > 1) {
                                handedOver = true
                                break
                            }
                            val change = event.changes.firstOrNull { it.id == id } ?: break
                            for (historical in change.historical) {
                                points.add(historical.position.x)
                                points.add(historical.position.y)
                            }
                            points.add(change.position.x)
                            points.add(change.position.y)
                            pressure += change.pressure
                            readings++
                            change.consume()
                            if (!erasing) live = points.toList()
                            if (!change.pressed) break
                        }
                        live = emptyList()
                        if (handedOver) return@awaitEachGesture

                        if (erasing) {
                            val taken = marks.filter { mark ->
                                val there = onScreen(mark, blocks, width) ?: return@filter false
                                // In pixels, because both sides of this
                                // comparison are where they are on screen.
                                near(InkStroke(mark.stroke.colour, mark.stroke.width, there), points, ERASER_REACH / LessonMark.SPAN * width)
                            }
                            if (taken.isNotEmpty()) onErase(taken)
                        } else if (points.size >= 4) {
                            val (key, start) = anchor(blocks, width, Offset(points[0], points[1]))
                                ?: return@awaitEachGesture
                            val block = blocks.first { it.key == key }
                            val held = ArrayList<Float>(points.size)
                            held.add(start.x)
                            held.add(start.y)
                            var i = 2
                            while (i + 1 < points.size) {
                                val at = into(block, width, Offset(points[i], points[i + 1]))
                                held.add(at.x)
                                held.add(at.y)
                                i += 2
                            }
                            // The nib is already in the same units the block
                            // is measured in — a thousand across, whatever
                            // the screen — so a mark is the same weight on a
                            // phone and on a tablet, and nothing is scaled
                            // here that would have to be scaled back.
                            val weight = pressed(tool.width, pressure / readings)
                            onMark(LessonMark(key, InkStroke(tool.colour, weight, held)))
                        }
                    }
                },
            ),
    ) {
        val blocks = blocksOf(listState)
        val width = size.width.toInt()
        for (mark in marks) {
            val there = onScreen(mark, blocks, width) ?: continue
            draw(mark.stroke.colour, mark.stroke.width / LessonMark.SPAN * width, there)
        }
        if (live.size >= 4) draw(tool.colour, tool.width / LessonMark.SPAN * width, live)
    }
}

private fun DrawScope.draw(colour: String, width: Float, points: List<Float>) {
    if (points.size < 4) return
    val path = Path()
    path.moveTo(points[0], points[1])
    var i = 2
    while (i + 1 < points.size) {
        path.lineTo(points[i], points[i + 1])
        i += 2
    }
    drawPath(
        path = path,
        color = colourOf(colour),
        style = Stroke(width = width.coerceAtLeast(1f), cap = StrokeCap.Round, join = StrokeJoin.Round),
    )
}

const val LESSON_INK_LAYER = "lesson-ink-layer"
