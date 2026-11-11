package com.shahaabapps.core.model

/**
 * One stroke marked onto a lesson, and which part of the lesson it is on.
 *
 * [key] names the block it belongs to — a paragraph, a table, the title —
 * rather than a position down the screen, and the stroke's coordinates are
 * in that block's own space: 0 to 1000 across its width and 0 to 1000 down
 * its height. Everything about marking up a lesson follows from that choice.
 *
 * A lesson is not a page. It is a column of text that is a different length
 * on a phone and a tablet, a different length again rotated, and a different
 * length tomorrow if the lesson is edited. A mark kept as "1,400 pixels down
 * the screen" is a mark that lands in the wrong paragraph the first time any
 * of that changes, which is the one thing an underline must never do. Kept
 * against its block, it stays on the sentence it was drawn under.
 *
 * What it cannot do is survive the text inside a block reflowing: five lines
 * at one width are seven at another, and the mark stretches with the block
 * rather than following the words. That is the honest limit of annotating
 * reflowing text, and it is why the block is the smallest thing worth
 * anchoring to.
 */
data class LessonMark(
    val key: String,
    val stroke: InkStroke,
) {
    companion object {
        /** Both axes of a block run 0 to this, whatever size it is on screen. */
        const val SPAN = 1000f
    }
}

/**
 * The record of one person's marks on one lesson.
 *
 * One document per reader per lesson, at a known id, so there is no query
 * and no index: the screen asks for exactly the document it wants. The
 * strokes themselves are in Storage under [path], the same split the
 * attachments and the handwritten notes use and for the same reason — a
 * marked-up lesson is tens of thousands of coordinates, and the screen has
 * to know whether there are any long before it needs them.
 *
 * Marks belong to the person who made them. The two of you read the same
 * lessons, and somebody else's circling over your paragraph is somebody
 * else's thinking written on your page.
 */
data class LessonInk(
    val id: String = "",
    val courseId: String = "",
    val moduleId: String = "",
    val topicId: String = "",
    /** The uid whose marks these are. */
    val owner: String = "",
    val path: String = "",
    val strokeCount: Int = 0,
    val updatedAtIso: String = "",
)
