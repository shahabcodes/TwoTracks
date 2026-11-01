package com.shahaabapps.twotracks.feature.learn

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File

/**
 * A PDF on this phone, as pages the screen can draw.
 *
 * [PdfRenderer] is a thin wrapper over a native renderer with two rules that
 * shape everything here: it needs a real file descriptor rather than a stream,
 * which is why the file is downloaded before this is opened, and it will only
 * have one page open at a time, which is why every render goes through [lock].
 * Sizes are read once at open so the screen can reserve the right height for a
 * page before its pixels exist, and the list does not jump as they arrive.
 */
class PdfSource private constructor(
    private val descriptor: ParcelFileDescriptor,
    private val renderer: PdfRenderer,
    /** Each page's size in PDF points, in page order. */
    val sizes: List<IntSize>,
) : AutoCloseable {

    val pageCount: Int get() = sizes.size

    private val lock = Mutex()

    /**
     * One page as a bitmap [widthPx] wide, its height following the page's own
     * proportions. Null if the page cannot be drawn, which a damaged file can
     * do one page at a time rather than all at once.
     */
    suspend fun render(index: Int, widthPx: Int): Bitmap? = lock.withLock {
        withContext(Dispatchers.IO) {
            runCatching {
                renderer.openPage(index).use { page ->
                    val height = (widthPx.toFloat() * page.height / page.width).toInt().coerceAtLeast(1)
                    val bitmap = Bitmap.createBitmap(widthPx, height, Bitmap.Config.ARGB_8888)
                    // A PDF page is paper: what it does not draw is white, not
                    // transparent, and an unfilled bitmap shows the surround
                    // through the margins.
                    bitmap.eraseColor(Color.WHITE)
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    bitmap
                }
            }.getOrNull()
        }
    }

    override fun close() {
        runCatching { renderer.close() }
        runCatching { descriptor.close() }
    }

    companion object {
        /**
         * Opens [file], or returns null when it is not a PDF this phone can
         * draw — damaged, truncated, or locked with a password, which the
         * renderer reports by refusing to open at all.
         */
        fun open(file: File): PdfSource? = runCatching {
            val descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = runCatching { PdfRenderer(descriptor) }
                .getOrElse { descriptor.close(); throw it }
            val sizes = (0 until renderer.pageCount).map { i ->
                renderer.openPage(i).use { IntSize(it.width, it.height) }
            }
            PdfSource(descriptor, renderer, sizes)
        }.getOrNull()
    }
}
