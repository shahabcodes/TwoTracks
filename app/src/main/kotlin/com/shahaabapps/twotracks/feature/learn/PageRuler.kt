package com.shahaabapps.twotracks.feature.learn

import androidx.compose.ui.unit.IntSize
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Where every page of a PDF sits in the column, at a given zoom.
 *
 * A pinch has to leave the thing under the fingers under the fingers, and
 * that is arithmetic, not a gesture: the point being pinched is somewhere
 * down a column of pages, and after the zoom the column has to be scrolled so
 * that the same point lands back under the same fingers. Doing it by feel —
 * scaling the scroll offset and hoping — is what makes a viewer zoom towards
 * its own top left corner whatever the fingers do.
 *
 * Every page's proportions are known from the moment the file opens, so the
 * column's whole length is known too, and the two conversions this needs
 * ([offsetOf] and [locate]) are exact rather than estimated. They are also
 * plain functions of three numbers, which is how they come to be tested.
 */
internal class PageRuler(
    private val sizes: List<IntSize>,
    /** The width of a page at 1x, in pixels. */
    private val frameWidth: Int,
    /** The gap drawn between two pages, which does not scale with the zoom. */
    private val gap: Int,
) {

    val pageCount: Int get() = sizes.size

    /** The height on screen of one page at [zoom]. */
    fun pageHeight(index: Int, zoom: Float): Float {
        val size = sizes.getOrNull(index) ?: return 0f
        if (size.width <= 0) return 0f
        return frameWidth * zoom * size.height / size.width
    }

    /**
     * How far down the whole column a point [within] pixels into page [index]
     * is, counting the gaps between the pages above it.
     */
    fun offsetOf(index: Int, within: Float, zoom: Float): Float {
        val at = index.coerceIn(0, (pageCount - 1).coerceAtLeast(0))
        var total = 0f
        for (i in 0 until at) total += pageHeight(i, zoom) + gap
        return total + within
    }

    /**
     * The reverse: which page a point [absolute] pixels down the column is on,
     * and how far into it. A point in the gap between two pages belongs to the
     * page above, and a point past either end is clamped to that end, because
     * a pinch at the very top of the first page still has to land somewhere.
     */
    fun locate(absolute: Float, zoom: Float): Pair<Int, Float> {
        if (pageCount == 0) return 0 to 0f
        if (absolute <= 0f) return 0 to absolute
        var left = absolute
        for (i in 0 until pageCount) {
            val height = pageHeight(i, zoom)
            if (left < height + gap || i == pageCount - 1) return i to left
            left -= height + gap
        }
        return pageCount - 1 to left
    }

    /**
     * Where to scroll the column to so that the point last seen at [focus]
     * pixels down the screen is there again after the zoom changes.
     *
     * [index] and [offset] are the column's own scroll position before the
     * zoom — the first visible page and how far into it the screen starts.
     * The answer is in the same two terms, ready to be scrolled to, and never
     * negative: above the first page there is nothing to show.
     */
    fun anchor(index: Int, offset: Int, focus: Float, from: Float, to: Float): Pair<Int, Int> {
        val (page, within) = locate(offsetOf(index, offset + focus, from), from)
        // The point keeps its place on the page it is on, so it scales with
        // the page; the gaps above it do not, which is why this goes back
        // through the ruler rather than multiplying the scroll position.
        val target = offsetOf(page, within * (to / from), to) - focus
        if (target <= 0f) return 0 to 0
        val (newPage, newWithin) = locate(target, to)
        return newPage to newWithin.toInt().coerceAtLeast(0)
    }
}

/** However small the frame, a page is not shrunk past a third of its width. */
internal const val MIN_PDF_ZOOM = 0.33f

/** The zoom is rendered at quarter steps. */
internal const val RENDER_STEP = 0.25f

/** A render wider than this costs memory no screen can show. */
internal const val MAX_RENDER_PX = 2400

/** About 24MB as ARGB_8888, for one page of the two or three kept alive. */
internal const val MAX_RENDER_PIXELS = 6_000_000f

/**
 * The zoom at which the whole of a page of [size] sits in a frame this big,
 * or 1 when the frame is tall enough for it already.
 *
 * Never below [MIN_PDF_ZOOM]: a page shrunk past that is a grey rectangle,
 * and a person who pinched that far wanted the next page, not this one made
 * unreadable.
 */
internal fun fitPage(size: IntSize?, frameWidth: Int, frameHeight: Int): Float {
    if (size == null || size.width <= 0 || frameWidth <= 0) return 1f
    val atFitWidth = frameWidth.toFloat() * size.height / size.width
    if (atFitWidth <= 0f) return 1f
    return (frameHeight / atFitWidth).coerceIn(MIN_PDF_ZOOM, 1f)
}

/**
 * The zoom rounded up to the next [RENDER_STEP], which is what gets drawn.
 *
 * Up rather than to the nearest, so the bitmap is never narrower than the
 * page it is drawn into: shrinking a render loses nothing an eye can see,
 * and stretching one is exactly the blur a person pinched in to get rid of.
 */
internal fun stepped(scale: Float): Float =
    (ceil(scale / RENDER_STEP) * RENDER_STEP).coerceAtLeast(RENDER_STEP)

/**
 * How wide to render a page of [size], given the [wanted] width on screen.
 *
 * Width alone is not a budget: a page is as tall as its own proportions make
 * it, and an A4 sheet 2400 across is 3400 down, which is 32MB of bitmap for
 * one page of the two or three the column keeps alive. The area is the real
 * limit, so a tall page is rendered a little narrower rather than being
 * allowed to cost several times what a short one does.
 */
internal fun renderWidthFor(wanted: Float, size: IntSize): Int {
    val ratio = (size.height.toFloat() / size.width.coerceAtLeast(1)).coerceAtLeast(0.01f)
    val byArea = sqrt(MAX_RENDER_PIXELS / ratio)
    return minOf(wanted, byArea, MAX_RENDER_PX.toFloat()).roundToInt().coerceAtLeast(1)
}

/** "1.5", without a locale that would make it "1,5". */
internal fun oneDecimal(value: Float): String {
    val tenths = (value * 10).roundToInt()
    return "${tenths / 10}.${tenths % 10}"
}
