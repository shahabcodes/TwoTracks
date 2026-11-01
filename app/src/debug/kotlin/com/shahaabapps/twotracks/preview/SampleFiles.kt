package com.shahaabapps.twotracks.preview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.File

/**
 * Debug builds only: an attachment on disk, so the viewer can be drawn and
 * compared without an account, a bucket, or anything to upload.
 *
 * Both are made here rather than shipped as assets because what matters is
 * that they are a real PNG and a real multi-page PDF — the renderer is the
 * part being looked at, and a stand-in picture would not go near it.
 */
object SampleFiles {

    /** A photographed whiteboard, more or less: a sketch on an off-white field. */
    fun image(context: Context): File = write(context, "sample-whiteboard.png") { file ->
        val bitmap = Bitmap.createBitmap(1400, 1000, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.rgb(246, 244, 238))
        val ink = Paint().apply {
            color = Color.rgb(28, 28, 32); strokeWidth = 7f; isAntiAlias = true
            style = Paint.Style.STROKE
        }
        val text = Paint().apply { color = Color.rgb(28, 28, 32); textSize = 56f; isAntiAlias = true }
        val blue = Paint(ink).apply { color = Color.rgb(32, 92, 196) }
        canvas.drawText("App Service → Slots", 90f, 140f, text)
        canvas.drawRoundRect(90f, 240f, 560f, 420f, 18f, 18f, ink)
        canvas.drawText("staging", 150f, 350f, text)
        canvas.drawRoundRect(840f, 240f, 1310f, 420f, 18f, 18f, ink)
        canvas.drawText("production", 890f, 350f, text)
        canvas.drawLine(560f, 330f, 840f, 330f, blue)
        canvas.drawLine(790f, 300f, 840f, 330f, blue)
        canvas.drawLine(790f, 360f, 840f, 330f, blue)
        canvas.drawText("swap: warm, then flip", 560f, 480f, text.apply { textSize = 44f })
        canvas.drawLine(90f, 640f, 1310f, 640f, ink)
        canvas.drawText("settings marked sticky stay behind", 90f, 740f, text)
        file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        bitmap.recycle()
    }

    /** Three pages of something that reads like an exam guide. */
    fun pdf(context: Context): File = write(context, "sample-exam-guide.pdf") { file ->
        val document = PdfDocument()
        val heading = Paint().apply { color = Color.BLACK; textSize = 26f; isAntiAlias = true }
        val body = Paint().apply { color = Color.rgb(50, 50, 55); textSize = 13f; isAntiAlias = true }
        val rule = Paint().apply { color = Color.rgb(200, 200, 205); strokeWidth = 1f }
        PAGES.forEachIndexed { index, page ->
            // A4 at 72dpi, which is what the renderer treats as a point.
            val info = PdfDocument.PageInfo.Builder(595, 842, index + 1).create()
            val started = document.startPage(info)
            val canvas = started.canvas
            canvas.drawColor(Color.WHITE)
            canvas.drawText(page.first, 56f, 90f, heading)
            canvas.drawLine(56f, 108f, 539f, 108f, rule)
            page.second.forEachIndexed { line, written ->
                canvas.drawText(written, 56f, 146f + line * 26f, body)
            }
            canvas.drawText("${index + 1} of ${PAGES.size}", 505f, 800f, body)
            document.finishPage(started)
        }
        file.outputStream().use { document.writeTo(it) }
        document.close()
    }

    private fun write(context: Context, name: String, fill: (File) -> Unit): File {
        val file = File(context.cacheDir, name)
        // Written once per install: the preview is launched over and over
        // while a screenshot is got right, and redrawing is not the point.
        if (file.length() == 0L) fill(file)
        return file
    }

    private val PAGES = listOf(
        "Compute on Azure" to listOf(
            "App Service runs code you hand over; containers run images you build.",
            "A plan is the hardware. Apps on one plan share it, and scale together.",
            "Slots are whole copies of the app on the same plan, swapped when warm.",
            "Functions bill per execution on Consumption, per instance on Premium.",
            "Durable Functions keep state in storage, so an orchestration survives.",
        ),
        "Where state lives" to listOf(
            "Cosmos DB: partition key decides the physical layout, and is forever.",
            "Request units are the single currency: reads, writes, queries, all RU.",
            "Change feed is an ordered log per partition, read at your own pace.",
            "Redis is a cache with eviction, not a database. Expect misses.",
            "Blob tiers: hot, cool, cold, archive — rehydration takes hours.",
        ),
        "Messaging" to listOf(
            "Service Bus for work that must not be lost: sessions, dead letters.",
            "Event Grid for notifications about something that already happened.",
            "Event Hubs for a firehose read by partition and checkpointed.",
            "Peek-lock plus a renewed lock is how a slow handler stays safe.",
        ),
    )
}
