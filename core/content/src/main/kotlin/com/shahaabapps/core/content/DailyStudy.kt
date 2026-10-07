package com.shahaabapps.core.content

import com.shahaabapps.core.model.Flashcard
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.QuizAttempt
import com.shahaabapps.core.model.TopicProgress
import com.shahaabapps.core.planner.Srs
import java.time.LocalDate

/** Pure helpers for the vocabulary deck, shared by the deck screen and the day's loop. */
object WordDeck {
    fun cardsFor(owner: PersonRole, course: Course, all: List<Flashcard>): List<Srs.Card> =
        all.filter { it.owner == owner && it.courseId == course.id }
            .map { Srs.Card(it.wordId, it.step, it.dueIso, it.lastReviewIso, it.correct, it.misses) }

    /** Words the person has met: every word in a topic they marked learned. */
    fun metWordIds(owner: PersonRole, course: Course, progress: List<TopicProgress>): List<String> {
        val learned = progress.filter { it.owner == owner && it.courseId == course.id }.map { it.topicId }.toSet()
        return course.modules.flatMap { m -> m.topics.filter { it.id in learned } }
            .flatMap { t -> t.blocks.filterIsInstance<Block.Vocab>().flatMap { it.words } }.map { it.id }
    }

    fun dueCount(owner: PersonRole, course: Course, all: List<Flashcard>, today: LocalDate): Int =
        Srs.dueCount(cardsFor(owner, course, all), today)

    fun newCount(owner: PersonRole, course: Course, all: List<Flashcard>, progress: List<TopicProgress>): Int {
        val have = all.filter { it.owner == owner && it.courseId == course.id }.map { it.wordId }.toSet()
        return metWordIds(owner, course, progress).count { it !in have }
    }
}

/** The three kinds of work a study day is made of. */
enum class StudyKind { TOPIC, WORDS, QUESTIONS }

/**
 * One thing to do today. [done] is whether it has already been done today,
 * which is read back from the day's own records rather than stored: a topic
 * marked learned today, a card reviewed today, a daily sitting dated today.
 */
data class StudyStep(
    val kind: StudyKind,
    val courseId: String,
    val title: String,
    val detail: String,
    val minutes: Int,
    val done: Boolean,
    /** The topic to open, for [StudyKind.TOPIC]. */
    val topicId: String? = null,
)

/**
 * Today's study, across every course at once: read one topic, clear the words
 * that are due, answer the questions that have come back round. A day with all
 * three done is a day that can be closed, which is the whole point — a loop
 * you can finish beats a list you can only add to.
 *
 * Nothing about the day is stored. Each step's [StudyStep.done] is read from
 * records the app already writes, so the loop cannot drift from reality and
 * survives a reinstall.
 */
data class DailyStudy(val steps: List<StudyStep> = emptyList()) {
    /** Minutes of work still outstanding, by the same rough rates the rest of the app quotes. */
    val minutesLeft: Int get() = steps.filterNot { it.done }.sumOf { it.minutes }
    val doneCount: Int get() = steps.count { it.done }
    val total: Int get() = steps.size
    /** The next thing to do, or null when the day is closed. */
    val next: StudyStep? get() = steps.firstOrNull { !it.done }
    /** True only when there was something to do and all of it is done. */
    val closed: Boolean get() = steps.isNotEmpty() && steps.all { it.done }
    val fraction: Float get() = if (steps.isEmpty()) 0f else doneCount.toFloat() / steps.size

    companion object {
        /** Minutes a count of due words is worth, matching the figure the daily brief quotes. */
        internal fun wordMinutes(count: Int): Int = (count / 4).coerceAtLeast(2)

        /** Minutes a count of questions is worth, matching the figure the review rows quote. */
        internal fun questionMinutes(count: Int): Int = (count * 1.5).toInt().coerceAtLeast(2)

        /**
         * Builds today's loop. A step appears when it is outstanding or was
         * done today, and not otherwise: a day with no words due and none
         * reviewed has no words step, so the count never promises work that
         * does not exist.
         */
        fun of(
            owner: PersonRole,
            courses: List<Course>,
            progressByCourse: Map<String, CourseProgress>,
            topicProgress: List<TopicProgress>,
            attempts: List<QuizAttempt>,
            flashcards: List<Flashcard>,
            today: LocalDate,
        ): DailyStudy {
            val iso = today.toString()
            val steps = mutableListOf<StudyStep>()

            // Reading: the next unread topic, from the first course that still has one.
            val readToday = topicProgress.any { it.owner == owner && it.learnedAtIso == iso }
            val nextTopic = courses.firstNotNullOfOrNull { c ->
                progressByCourse[c.id]?.nextTopic(c)?.let { (m, t) -> Triple(c, m, t) }
            }
            if (readToday || nextTopic != null) {
                val t = nextTopic?.third
                steps += StudyStep(
                    kind = StudyKind.TOPIC,
                    courseId = nextTopic?.first?.id.orEmpty(),
                    title = if (readToday) "Read a topic" else "Read: ${t?.title?.substringBefore(":").orEmpty()}",
                    detail = if (readToday) "Done today" else "${nextTopic?.first?.title} · ${t?.minutes ?: 0} min",
                    minutes = t?.minutes ?: 0,
                    done = readToday,
                    topicId = t?.id,
                )
            }

            // Words: due and newly met cards across every course that has vocabulary.
            val vocabCourses = courses.filter { Courses.words(it).isNotEmpty() }
            val wordsDue = vocabCourses.sumOf { c ->
                WordDeck.dueCount(owner, c, flashcards, today) + WordDeck.newCount(owner, c, flashcards, topicProgress)
            }
            val wordsDoneToday = flashcards.any { it.owner == owner && it.lastReviewIso == iso }
            if (wordsDue > 0 || wordsDoneToday) {
                // Where to go: the course with the most waiting, else whichever was reviewed.
                val deck = vocabCourses.maxByOrNull { c ->
                    WordDeck.dueCount(owner, c, flashcards, today) + WordDeck.newCount(owner, c, flashcards, topicProgress)
                }
                steps += StudyStep(
                    kind = StudyKind.WORDS,
                    courseId = deck?.id.orEmpty(),
                    title = if (wordsDue == 0) "Words" else "Words: $wordsDue waiting",
                    detail = if (wordsDue == 0) "Done today" else "${deck?.title} deck · about ${wordMinutes(wordsDue)} min",
                    minutes = wordMinutes(wordsDue),
                    done = wordsDue == 0 && wordsDoneToday,
                )
            }

            // Questions: whatever the spaced schedule has brought back round.
            val questionsDue = courses.sumOf { c -> progressByCourse[c.id]?.dailyDueCount(today) ?: 0 }
            val reviewedToday = attempts.any {
                it.owner == owner && it.moduleId == Courses.DAILY_MODULE_ID && it.dateIso == iso
            }
            if (questionsDue > 0 || reviewedToday) {
                val course = courses.maxByOrNull { c -> progressByCourse[c.id]?.dailyDueCount(today) ?: 0 }
                steps += StudyStep(
                    kind = StudyKind.QUESTIONS,
                    courseId = course?.id.orEmpty(),
                    title = if (questionsDue == 0) "Questions" else "Questions: $questionsDue due",
                    detail = if (questionsDue == 0) "Done today" else "${course?.title} · about ${questionMinutes(questionsDue)} min",
                    minutes = questionMinutes(questionsDue),
                    done = questionsDue == 0 && reviewedToday,
                )
            }
            return DailyStudy(steps)
        }
    }
}
