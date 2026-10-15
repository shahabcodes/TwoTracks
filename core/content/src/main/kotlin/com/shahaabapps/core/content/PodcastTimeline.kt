package com.shahaabapps.core.content

/**
 * When each line of an episode is heard, as the render tool measured it.
 *
 * The tool writes this beside each audio file (out/<topic>.json), and the
 * player reads it to light up the line being spoken and to let a tap on a
 * line seek to it. Lines are in order and do not overlap; the gaps between
 * them are the pauses where one host hands over to the other.
 */
data class PodcastTimeline(val seconds: Double, val spans: List<Span>) {
    data class Span(val line: Int, val start: Double, val end: Double)

    /**
     * The line to light at [position] seconds: the one being spoken, or in a
     * gap between two the one just finished, so the highlight does not blink
     * off at every hand-over. Null before the first line starts.
     */
    fun lineAt(position: Double): Int? {
        if (spans.isEmpty() || position < spans.first().start) return null
        // Spans are sorted by start, so the last one started is the answer.
        var lo = 0
        var hi = spans.lastIndex
        while (lo < hi) {
            val mid = (lo + hi + 1) / 2
            if (spans[mid].start <= position) lo = mid else hi = mid - 1
        }
        return spans[lo].line
    }

    /** Where to seek for a tap on [line], or null for a line the audio does not have. */
    fun startOf(line: Int): Double? = spans.firstOrNull { it.line == line }?.start

    /**
     * Whether a saved position is worth resuming from, rather than starting
     * again: past the first line, and not so close to the end that resuming
     * would play a second of goodbye.
     */
    fun resumable(position: Double): Boolean = position > (spans.firstOrNull()?.end ?: 0.0) && position < seconds - 15.0

    companion object {
        private val number = """-?\d+(?:\.\d+)?"""
        private val total = Regex(""""seconds"\s*:\s*($number)""")
        private val span = Regex("""\{\s*"line"\s*:\s*(\d+)\s*,\s*"host"\s*:\s*"[a-z]+"\s*,\s*"start"\s*:\s*($number)\s*,\s*"end"\s*:\s*($number)\s*}""")

        /**
         * Reads the render tool's timing file. Written by hand rather than with a
         * JSON library because the module has none and the format is this small;
         * anything it does not recognise gives null rather than a wrong timeline.
         */
        fun parse(json: String): PodcastTimeline? {
            val seconds = total.find(json)?.groupValues?.get(1)?.toDoubleOrNull() ?: return null
            val spans = span.findAll(json).map { m ->
                Span(m.groupValues[1].toInt(), m.groupValues[2].toDouble(), m.groupValues[3].toDouble())
            }.sortedBy { it.start }.toList()
            if (spans.isEmpty() || spans.zipWithNext().any { (a, b) -> b.start < a.end }) return null
            return PodcastTimeline(seconds, spans)
        }
    }
}
