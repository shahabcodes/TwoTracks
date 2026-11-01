package com.shahaabapps.core.model

/**
 * One stroke of handwriting: where the pen went, in what colour, how thick.
 *
 * [points] is x and y alternating rather than a list of pairs. A page of
 * handwriting is tens of thousands of numbers and a pair object each would
 * be tens of thousands of allocations on a path that runs while a finger is
 * moving; the flat array is the shape the drawing code wants anyway.
 *
 * The coordinates are in the page's own space, which [InkPage] carries the
 * size of, so the same note redraws correctly on a phone, on a tablet and
 * zoomed in — and so that a note written on one device is not stretched on
 * the other.
 */
data class InkStroke(
    /** RGB as six hex digits, no alpha and no leading hash. */
    val colour: String,
    /** Width in page units, before pressure. */
    val width: Float,
    val points: List<Float>,
) {
    val isEmpty: Boolean get() = points.size < 2
}

/**
 * A page of handwriting: the strokes, and the space their coordinates are in.
 *
 * [width] and [height] are the page's own units rather than pixels of any
 * particular screen. Everything is scaled by the ratio of the drawing area
 * to these, which is the whole of how a note survives a change of device.
 */
data class InkPage(
    val width: Float = PAGE_WIDTH,
    val height: Float = PAGE_HEIGHT,
    val strokes: List<InkStroke> = emptyList(),
) {
    companion object {
        /**
         * The page is 1000 units across and A4-proportioned down.
         *
         * A fixed, paper-shaped page rather than whatever shape the screen
         * is: a note written in landscape and read in portrait should be the
         * same note, and the only way it can be is if the page it was written
         * on has a shape of its own.
         */
        const val PAGE_WIDTH = 1000f
        const val PAGE_HEIGHT = 1414f
    }
}
