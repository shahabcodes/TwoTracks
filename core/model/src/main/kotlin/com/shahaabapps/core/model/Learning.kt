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
) {
    val percent: Int get() = if (total == 0) 0 else score * 100 / total
}
