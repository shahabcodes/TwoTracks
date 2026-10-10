package com.shahaabapps.core.model

/** A topic one person has marked as learned, with the minutes they spent reading it. */
data class TopicProgress(
    val id: String = "",
    val owner: PersonRole = PersonRole.TECH,
    val courseId: String = "",
    val topicId: String = "",
    val learnedAtIso: String = "",
    val minutes: Int = 0,
)

/** One sitting of a module test. */
data class QuizAttempt(
    val id: String = "",
    val owner: PersonRole = PersonRole.TECH,
    val courseId: String = "",
    val moduleId: String = "",
    val dateIso: String = "",
    val score: Int = 0,
    val total: Int = 0,
    val passed: Boolean = false,
    /** Ids of the questions answered wrongly, for the review deck. */
    val missed: List<String> = emptyList(),
    /** Ids of every question in this sitting; empty for older attempts, which means the whole module test. */
    val asked: List<String> = emptyList(),
    /** Wall-clock millis when the sitting finished, so same-day attempts keep their order. */
    val at: Long = 0L,
    /**
     * Which options were chosen, by question id.
     *
     * Not whether they were right — [missed] already says that. This is the
     * one thing a sitting never recorded, and without it a results page can
     * only say the right answer again. With it, it can say why the option you
     * actually picked was tempting, which an in-topic check could already do
     * and a mock exam could not.
     *
     * Indices into the question's own options, which are stable: the rotation
     * that spreads the answers around is keyed on the question id and applied
     * when the course is built, not per sitting.
     *
     * Empty on every attempt recorded before this was stored.
     */
    val picked: Map<String, List<Int>> = emptyMap(),
    /**
     * Seconds spent on each question, by question id.
     *
     * The minutes a paper took were already logged; this is where they went.
     * It also carries the only reading the app has of how sure someone was —
     * an answer given in four seconds was believed, one that took a minute
     * was worked out — which is what lets a confident miss be scheduled
     * differently from an unsure one.
     *
     * Empty on every attempt recorded before this was stored, and missing a
     * question the clock ran out on.
     */
    val seconds: Map<String, Int> = emptyMap(),
) {
    val percent: Int get() = if (total == 0) 0 else score * 100 / total

    /** What was picked for one question, as a set, or empty if it was never recorded. */
    fun pickedFor(questionId: String): Set<Int> = picked[questionId]?.toSet().orEmpty()
}

/** Spaced-repetition state for one vocabulary word, per person. */
data class Flashcard(
    val id: String = "",
    val owner: PersonRole = PersonRole.TECH,
    val courseId: String = "",
    val wordId: String = "",
    val step: Int = 0,
    val dueIso: String = "",
    val lastReviewIso: String = "",
    val correct: Int = 0,
    val misses: Int = 0,
)
