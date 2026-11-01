package com.shahaabapps.core.model

/**
 * A file one of you attached to a module: a diagram from the exam guide, a
 * cheat sheet, a photo of a whiteboard, the PDF a colleague sent.
 *
 * The bytes live in the couple's own Storage folder and only the two of you
 * can read them; this record is the Firestore half, which is what the app
 * lists, sorts and deletes. [path] is the one link between the two, and is
 * written once at upload and never recomputed, so moving the layout later
 * cannot orphan what is already there.
 */
data class ModuleFile(
    val id: String = "",
    val courseId: String = "",
    val moduleId: String = "",
    /** What the file was called when it was picked, shown as the row's title. */
    val name: String = "",
    val mimeType: String = "",
    val sizeBytes: Long = 0,
    /** Where the bytes are in Storage, relative to the bucket root. */
    val path: String = "",
    /** The uid that uploaded it, so the list can say who added what. */
    val uploadedBy: String = "",
    val uploadedAtIso: String = "",
) {
    /** True for something the app can show as a picture rather than hand to another app. */
    val isImage: Boolean get() = mimeType.startsWith("image/")

    /**
     * Some providers hand back a blank or generic type for a file they are
     * only passing through, so the name is the fallback: a .pdf that arrived
     * as application/octet-stream is still a .pdf.
     */
    val isPdf: Boolean get() = mimeType == "application/pdf" || name.endsWith(".pdf", ignoreCase = true)

    /** True when the app draws this itself, instead of handing it to another app. */
    val isViewable: Boolean get() = isImage || isPdf

    /** "2.4 MB", or "812 KB" — what a person wants to read, not bytes. */
    val readableSize: String get() = when {
        sizeBytes >= 1_000_000 -> "%.1f MB".format(sizeBytes / 1_000_000.0)
        sizeBytes >= 1_000 -> "${sizeBytes / 1_000} KB"
        else -> "$sizeBytes B"
    }
}
