package com.shahaabapps.twotracks.feature.learn

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import com.shahaabapps.core.model.InkPage
import com.shahaabapps.core.model.InkStroke

/**
 * What the pen is doing: a colour and a width, or rubbing out.
 *
 * The eraser is a tool rather than a white pen. A white pen only looks like
 * an eraser on white paper, and what a person means by rubbing out is the
 * stroke gone — half a letter with its middle taken out is nobody's idea of
 * it.
 */
data class InkTool(
    val colour: String = DEFAULT_COLOUR,
    val width: Float = DEFAULT_WIDTH,
    val erasing: Boolean = false,
) {
    companion object {
        const val DEFAULT_COLOUR = "1C1C20"
        const val DEFAULT_WIDTH = 3.2f
    }
}

/**
 * A sheet of paper that takes handwriting.
 *
 * Three things make this a pen surface rather than a finger one.
 *
 * It knows what is touching it. Once a stylus has been seen, fingers are
 * ignored — which is the whole of palm rejection, since a hand resting on a
 * tablet is several large touches that arrive before the nib does and would
 * otherwise draw. A device that never sees a stylus lets a finger draw, so
 * nothing is lost on a phone.
 *
 * It takes every position the system batched, not only where the nib is at
 * the frame boundary. A fast stroke is five or six positions a frame, and
 * keeping one of them turns a curve into a corner.
 *
 * And it draws in page units rather than pixels: in and out through
 * [InkPage]'s own 1000-wide, paper-shaped space, so a note written on a
 * phone is the same note on a tablet, and the same note zoomed.
 */
@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun InkCanvas(
    page: InkPage,
    tool: InkTool,
    onPage: (InkPage) -> Unit,
    modifier: Modifier = Modifier,
    paper: Color = Color.White,
) {
    /** The stroke under the nib, in page units, redrawn on its own as it grows. */
    var live by remember { mutableStateOf<List<Float>>(emptyList()) }
    var liveWidth by remember { mutableStateOf(tool.width) }
    var sawStylus by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    BoxWithConstraints(modifier = modifier) {
        // From the constraints rather than from a reported size, so the very
        // first stroke is scaled correctly: a size that arrives after the
        // first touch would put that stroke somewhere else on the page.
        val widthPx = with(density) { maxWidth.toPx() }
        val scale = if (widthPx > 0f) page.width / widthPx else 1f
        val out = if (scale > 0f) 1f / scale else 1f

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(paper)
                .testTag(INK_CANVAS)
                .pointerInput(tool, page, scale) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val stylus = down.type == PointerType.Stylus || down.type == PointerType.Eraser
                        if (stylus) sawStylus = true
                        if (sawStylus && !stylus) return@awaitEachGesture
                        // The side button on an S Pen arrives as its own
                        // pointer type, so holding it rubs out without the
                        // tool on screen having to be changed first.
                        val erasing = tool.erasing || down.type == PointerType.Eraser
                        val id = down.id
                        val points = mutableListOf<Float>()
                        var pressure = 0f
                        var readings = 0

                        fun add(at: Offset, force: Float) {
                            points.add(at.x * scale)
                            points.add(at.y * scale)
                            // Pressure is averaged over the stroke rather
                            // than kept per point: a stroke has one width in
                            // the file, so a note reloaded is exactly the
                            // note that was drawn, and a line pressed harder
                            // is still a heavier line than one that was not.
                            if (force > 0f) { pressure += force; readings++ }
                        }

                        add(down.position, down.pressure)
                        down.consume()
                        live = points.toList()
                        liveWidth = tool.width
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == id } ?: break
                            // Every position the system batched since the
                            // last frame. They carry no pressure of their
                            // own, so they take the frame's, which is what
                            // the average they feed wants anyway.
                            for (historical in change.historical) add(historical.position, change.pressure)
                            add(change.position, change.pressure)
                            change.consume()
                            if (!erasing) live = points.toList()
                            if (!change.pressed) break
                        }

                        val mean = if (readings > 0) pressure / readings else 1f
                        if (erasing) {
                            val kept = page.strokes.filterNot { near(it, points, ERASER_REACH) }
                            if (kept.size != page.strokes.size) onPage(page.copy(strokes = kept))
                        } else if (points.size >= 4) {
                            val drawn = InkStroke(tool.colour, pressed(tool.width, mean), points.toList())
                            onPage(page.copy(strokes = page.strokes + drawn))
                        }
                        live = emptyList()
                    }
                },
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                for (stroke in page.strokes) draw(stroke, out)
                if (live.size >= 4) draw(InkStroke(tool.colour, liveWidth, live), out)
            }
        }
    }
}

/**
 * The width a stroke is actually written at, given how hard it was pressed.
 *
 * Between two thirds and one and a half times what the pen is set to.
 * Unbounded pressure would make a hard full stop into a blot and a light
 * one into nothing at all, and a device that reports no pressure reports
 * 1.0, which lands exactly on the width the pen was set to.
 */
internal fun pressed(width: Float, pressure: Float): Float =
    width * pressure.coerceIn(0.66f, 1.5f)

/**
 * Whether [stroke] passes within [reach] page units of the eraser's [path].
 *
 * Against the line between each pair of points, not against the points. A
 * stroke drawn quickly is sampled sparsely — a fast line across a tablet
 * can be two points and nothing in between — and comparing points alone
 * means the eraser goes straight through the middle of it and takes
 * nothing, which is the worst way for an eraser to fail: it looks like the
 * app ignored you.
 */
internal fun near(stroke: InkStroke, path: List<Float>, reach: Float): Boolean {
    val square = reach * reach
    if (stroke.points.size < 2 || path.size < 2) return false
    var j = 0
    while (j + 1 < path.size) {
        val px = path[j]
        val py = path[j + 1]
        if (stroke.points.size == 2) {
            val dx = stroke.points[0] - px
            val dy = stroke.points[1] - py
            if (dx * dx + dy * dy <= square) return true
        }
        var i = 0
        while (i + 3 < stroke.points.size) {
            if (toSegment(px, py, stroke.points[i], stroke.points[i + 1], stroke.points[i + 2], stroke.points[i + 3]) <= square) {
                return true
            }
            i += 2
        }
        j += 2
    }
    return false
}

/** The square of the distance from a point to the segment a-b. */
private fun toSegment(px: Float, py: Float, ax: Float, ay: Float, bx: Float, by: Float): Float {
    val vx = bx - ax
    val vy = by - ay
    val length = vx * vx + vy * vy
    // Where along the segment the nearest point is, clamped to its ends so
    // that the answer is the distance to the segment and not to the whole
    // infinite line it lies on.
    val t = if (length <= 0f) 0f else (((px - ax) * vx + (py - ay) * vy) / length).coerceIn(0f, 1f)
    val dx = px - (ax + t * vx)
    val dy = py - (ay + t * vy)
    return dx * dx + dy * dy
}

/** One stroke, scaled from page units out to the pixels of this canvas. */
private fun DrawScope.draw(stroke: InkStroke, out: Float) {
    if (stroke.isEmpty) return
    val path = Path()
    path.moveTo(stroke.points[0] * out, stroke.points[1] * out)
    var i = 2
    while (i + 1 < stroke.points.size) {
        path.lineTo(stroke.points[i] * out, stroke.points[i + 1] * out)
        i += 2
    }
    drawPath(
        path = path,
        color = colourOf(stroke.colour),
        style = Stroke(width = stroke.width * out, cap = StrokeCap.Round, join = StrokeJoin.Round),
    )
}

/**
 * Six hex digits to a colour, falling back to ink rather than to nothing.
 *
 * The length is checked rather than left to the parse. "" parses perfectly
 * well as 0xff, which is a transparent blue — handwriting that is there in
 * the file and invisible on the page, and reads as a note that failed to
 * load rather than as a colour that was written down oddly.
 */
internal fun colourOf(hex: String): Color {
    val digits = hex.removePrefix("#")
    if (digits.length != 6) return INK
    return runCatching { Color(("ff$digits").toLong(16)) }.getOrElse { INK }
}

private val INK = Color(0xFF1C1C20)

/** How close the eraser has to pass to take a stroke, in page units. */
internal const val ERASER_REACH = 14f

const val INK_CANVAS = "ink-canvas"
