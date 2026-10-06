package com.shahaabapps.core.content

import java.text.Normalizer

/**
 * Marks a Schreiben answer the way the Goethe A1 marker does, in miniature:
 * one mark per content point covered, plus the form points the examiner
 * checks (a greeting, a sign-off, enough words). The result tells the learner
 * what is missing in words they can act on.
 */
object WritingMarker {

    data class Result(
        val points: List<Boolean>,
        val wordCount: Int,
        val hasGreeting: Boolean,
        val hasClosing: Boolean,
        val longEnough: Boolean,
    ) {
        val covered: Int get() = points.count { it }
        /** Content points plus one for each form point, as the exam's own band would roughly count. */
        val score: Int get() = covered + (if (hasGreeting) 1 else 0) + (if (hasClosing) 1 else 0)
        val maxScore: Int get() = points.size + 2
    }

    private val greetings = listOf("hallo", "liebe ", "lieber ", "guten tag", "sehr geehrte", "hi ")
    private val closings = listOf("viele grüsse", "liebe grüsse", "herzliche grüsse", "freundlichen grüssen", "bis bald", "bis später", "bis morgen", "bis dann", "tschüss", "dein ", "deine ", "ihr ", "ihre ", "gruss", "grüsse")

    fun normalise(s: String): String = Normalizer.normalize(s.lowercase(), Normalizer.Form.NFC).replace("ß", "ss").replace(Regex("[.!?,;:()\"]"), " ").replace(Regex("\\s+"), " ").trim()

    fun mark(ex: Ex.Writing, text: String): Result {
        val n = " " + normalise(text) + " "
        val words = n.trim().split(" ").filter { it.isNotBlank() }.size
        val points = if (ex.keywords.isEmpty()) ex.points.map { false } else ex.keywords.map { ks -> ks.any { k -> n.contains(normalise(k)) } }
        return Result(
            points = points,
            wordCount = words,
            hasGreeting = greetings.any { n.trimStart().startsWith(it.trim()) || n.contains(" $it") || n.contains(it) },
            hasClosing = closings.any { n.contains(it) },
            longEnough = words >= ex.minWords,
        )
    }
}
