package com.shahaabapps.core.data.ink

import com.shahaabapps.core.data.ink.InkCodec.appendStroke
import com.shahaabapps.core.data.ink.InkCodec.readStroke
import com.shahaabapps.core.model.LessonMark

/**
 * Reading and writing the marks on a lesson.
 *
 * One line per stroke, as [InkCodec] writes a page, with the block the
 * stroke belongs to in front of the colour:
 *
 *     mark1
 *     m block-7 C0392B 4 120,300 128,305 ...
 *
 * The key is written as-is and so must not contain a space, which is why
 * the keys the lesson gives its blocks are made rather than taken from the
 * text. A line whose key is missing or whose numbers are unreadable is
 * dropped and the rest of the page is kept: a lesson with one lost
 * underline is still a marked-up lesson.
 */
object LessonInkCodec {

    private const val HEADER = "mark1"

    fun encode(marks: List<LessonMark>): String = buildString {
        append(HEADER).append('\n')
        for (mark in marks) {
            if (mark.key.isBlank() || mark.key.contains(' ')) continue
            appendStroke(mark.stroke, "m", mark.key)
        }
    }

    fun decode(text: String): List<LessonMark> {
        val marks = mutableListOf<LessonMark>()
        for (line in text.lineSequence()) {
            val parts = line.trim().split(' ')
            if (parts.size < 5 || parts[0] != "m") continue
            val stroke = readStroke(parts, 2) ?: continue
            marks.add(LessonMark(parts[1], stroke))
        }
        return marks
    }
}
