package com.shahaabapps.core.model

/**
 * A page of handwriting kept against a lesson: worked arithmetic, a diagram
 * redrawn by hand, the thing that only makes sense in your own writing.
 *
 * The strokes are not in here. They go to the couple's Storage beside the
 * attachments, and this is the Firestore half — the title, where it belongs
 * and when it was last written on — which is what a list reads and sorts
 * without fetching a single stroke. [path] is the one link between them,
 * written once and never recomputed.
 */
data class InkNote(
    val id: String = "",
    val courseId: String = "",
    val moduleId: String = "",
    /** The lesson it belongs to, or blank for the module as a whole. */
    val topicId: String = "",
    /** What the writer called it, which is the whole of how it is found again. */
    val title: String = "",
    /** Where the strokes are in Storage, relative to the bucket root. */
    val path: String = "",
    /** How many strokes are on it, so a list can say how full a page is. */
    val strokeCount: Int = 0,
    val createdBy: String = "",
    val createdAtIso: String = "",
    /** Changed every time it is written on, which is what the list sorts by. */
    val updatedAtIso: String = "",
) {
    /** "12 strokes", or "empty" for a page nothing has been written on yet. */
    val readableSize: String get() = when (strokeCount) {
        0 -> "empty"
        1 -> "1 stroke"
        else -> "$strokeCount strokes"
    }
}
