package com.shahaabapps.twotracks.feature.listen

import com.shahaabapps.core.content.Host
import com.shahaabapps.core.content.PodcastCue
import com.shahaabapps.core.content.PodcastLine
import com.shahaabapps.core.content.PodcastTimeline

/** The words on screen at a moment: who is speaking, and the part of their line being said. */
data class Subtitle(val line: Int, val host: Host, val text: String, val translation: String?)

/**
 * Subtitles from the script and its timing. A line can run to several
 * sentences, more than fits under a board, so it is shown a piece at a time:
 * whole sentences, a long one broken at a comma, each piece on screen for its
 * share of the line's time by length. The timing only knows where lines start
 * and end, so within a line this is an estimate, close enough to read along.
 */
object Subtitles {
    /** About two lines of a phone's width at the player's size. */
    const val MAX = 90

    fun at(lines: List<PodcastLine>, timeline: PodcastTimeline?, position: Double): Subtitle? {
        val index = timeline?.lineAt(position) ?: return null
        val line = lines.getOrNull(index) ?: return null
        val span = timeline.spans.firstOrNull { it.line == index } ?: return null
        val pieces = pieces(PodcastCue.strip(line.text))
        if (pieces.isEmpty()) return null
        val length = span.end - span.start
        val through = if (length > 0) ((position - span.start) / length).coerceIn(0.0, 1.0) else 0.0
        return Subtitle(index, line.host, pieceAt(pieces, through), line.translation)
    }

    /** The piece being said [through] (0 to 1) the line, each piece taking time by its length. */
    fun pieceAt(pieces: List<String>, through: Double): String {
        val total = pieces.sumOf { it.length }.toDouble()
        var reached = 0.0
        for (piece in pieces) {
            reached += piece.length
            if (through * total < reached) return piece
        }
        return pieces.last()
    }

    /** The line in pieces of at most about [MAX] characters, breaking between sentences, then at commas. */
    fun pieces(text: String): List<String> {
        val parts = text.trim().split(SENTENCE).flatMap { sentence ->
            if (sentence.length <= MAX) listOf(sentence) else sentence.split(CLAUSE)
        }.map { it.trim() }.filter { it.isNotEmpty() }
        // Short neighbours share the screen, so a quick "Right. And then?" is one piece.
        val joined = mutableListOf<String>()
        for (part in parts) {
            val last = joined.lastOrNull()
            if (last != null && last.length + 1 + part.length <= MAX) joined[joined.lastIndex] = "$last $part" else joined += part
        }
        return joined
    }

    private val SENTENCE = Regex("(?<=[.!?…])\\s+")
    private val CLAUSE = Regex("(?<=[,;:—])\\s+")
}
