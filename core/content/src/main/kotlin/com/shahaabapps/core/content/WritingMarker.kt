package com.shahaabapps.core.content

import java.text.Normalizer

/**
 * Marks a writing task the way an examiner does, in miniature: one mark per
 * content point covered, plus the form checks for the kind of text. A letter
 * needs a greeting and a sign-off; an essay needs paragraphs, linking words
 * and a stated view; a report needs an overview, figures and comparison. The
 * result says what is missing in words the learner can act on.
 */
object WritingMarker {

    data class Check(val label: String, val ok: Boolean)

    data class Result(
        val points: List<Boolean>,
        val wordCount: Int,
        val longEnough: Boolean,
        val checks: List<Check>,
    ) {
        val covered: Int get() = points.count { it }
        val score: Int get() = covered + checks.count { it.ok }
        val maxScore: Int get() = points.size + checks.size
    }

    private val greetings = listOf("hallo", "liebe ", "lieber ", "guten tag", "sehr geehrte", "hi ", "dear ", "hello ")
    private val closings = listOf(
        "viele grüsse", "liebe grüsse", "herzliche grüsse", "freundlichen grüssen", "bis bald", "bis später", "bis morgen", "bis dann", "tschüss", "dein ", "deine ", "ihr ", "ihre ", "gruss", "grüsse",
        "yours sincerely", "yours faithfully", "kind regards", "best regards", "best wishes", "regards,", "all the best", "yours,", "love,", "see you soon",
    )
    private val linking = listOf(
        "however", "in addition", "furthermore", "moreover", "on the other hand", "for example", "for instance", "as a result", "therefore", "in conclusion", "to sum up", "firstly", "secondly", "finally", "although", "whereas", "while", "in contrast", "consequently", "overall",
    )
    private val opinion = listOf("i believe", "in my opinion", "i think", "i agree", "i disagree", "in my view", "it seems to me", "i am convinced", "personally")
    private val overview = listOf("overall", "in general", "generally", "it is clear that", "the most striking", "in summary", "as can be seen")
    private val comparison = listOf("higher", "lower", "more than", "less than", "compared", "whereas", "while", "in contrast", "the highest", "the lowest", "twice", "double", "similar", "respectively", "rose", "fell", "increased", "decreased", "peaked")

    /**
     * Lower case, ß folded to ss, punctuation reduced to spaces, so that only
     * the words decide a match.
     *
     * The comma belongs in that set as much as the full stop. Without it a
     * content point beginning "Hallo," never matched the keyword "hallo", and
     * a sentence spoken back perfectly scored short because its first word
     * still carried the comma from the prompt. German quotation marks and
     * dashes are here for the same reason. Hyphens are left alone: they join
     * compound words rather than separate them.
     */
    fun normalise(s: String): String = Normalizer.normalize(s.lowercase(), Normalizer.Form.NFC)
        .replace("ß", "ss")
        .replace(Regex("[.,!?;:()\\[\\]\"’'„“”«»…–—]"), " ")
        .replace(Regex("[ 	]+"), " ").trim()

    private fun paragraphs(text: String): Int = text.trim().split(Regex("\\n\\s*\\n|\\n")).count { it.isNotBlank() }

    fun mark(ex: Ex.Writing, text: String): Result {
        val n = " " + normalise(text).replace("\n", " ") + " "
        val words = n.trim().split(" ").filter { it.isNotBlank() }.size
        val points = if (ex.keywords.isEmpty()) ex.points.map { false } else ex.keywords.map { ks -> ks.any { k -> n.contains(normalise(k)) } }
        val checks = when (ex.form) {
            WritingForm.LETTER -> listOf(
                Check("A greeting (Dear …, Hello …, Liebe/Lieber …)", greetings.any { n.contains(it) }),
                Check("A sign-off (Yours sincerely, Best wishes, Viele Grüße …)", closings.any { n.contains(it) }),
            )
            WritingForm.ESSAY -> listOf(
                Check("At least four paragraphs: introduction, two body, conclusion", paragraphs(text) >= 4),
                Check("Linking words (however, in addition, for example, in conclusion)", linking.count { n.contains(it) } >= 3),
                Check("A clear position (I believe, in my opinion, I agree)", opinion.any { n.contains(it) }),
            )
            WritingForm.REPORT -> listOf(
                Check("At least three paragraphs: introduction, overview, details", paragraphs(text) >= 3),
                Check("An overview sentence (Overall, In general)", overview.any { n.contains(it) }),
                Check("Figures from the data (numbers or percentages)", Regex("\\d").containsMatchIn(n)),
                Check("Comparison language (higher than, whereas, the highest, rose, fell)", comparison.count { n.contains(it) } >= 2),
            )
        } + Check("At least ${ex.minWords} words", words >= ex.minWords)
        return Result(points = points, wordCount = words, longEnough = words >= ex.minWords, checks = checks)
    }
}
