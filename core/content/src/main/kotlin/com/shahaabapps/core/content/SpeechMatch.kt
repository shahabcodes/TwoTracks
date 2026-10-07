package com.shahaabapps.core.content

/**
 * How much of a spoken sentence the phone heard back, word by word.
 *
 * Deliberately forgiving: it asks which of the target's words turned up
 * somewhere in what was heard, not whether the sentence matched end to end.
 * A recogniser drops articles, runs words together and guesses at endings, and
 * a learner who said the sentence correctly should not be marked down for it.
 */
object SpeechMatch {

    data class Word(val word: String, val matched: Boolean)

    data class Result(val words: List<Word>) {
        val hits: Int get() = words.count { it.matched }
        val percent: Int get() = if (words.isEmpty()) 0 else hits * 100 / words.size
        /** The bar the UI calls "Sehr gut": enough of the sentence landed. */
        val good: Boolean get() = words.isNotEmpty() && percent >= 80
    }

    /**
     * [target] is what the exercise asked for, [heard] what the recogniser
     * returned. Both are normalised the same way the writing marker normalises
     * text, so case, punctuation and ß versus ss never decide the answer.
     */
    fun of(target: String, heard: String): Result {
        val said = WritingMarker.normalise(heard).split(" ").filter { it.isNotBlank() }.toSet()
        val words = WritingMarker.normalise(target).split(" ").filter { it.isNotBlank() }
        return Result(words.map { Word(it, it in said) })
    }
}
