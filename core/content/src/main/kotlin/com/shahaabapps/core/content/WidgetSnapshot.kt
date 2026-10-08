package com.shahaabapps.core.content

import com.shahaabapps.core.model.Flashcard
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.QuizAttempt
import com.shahaabapps.core.model.TopicProgress
import java.time.LocalDate

/**
 * Everything the home-screen widgets show, in one small flat value.
 *
 * A widget renders in milliseconds, offline, with no signed-in session, so it
 * cannot read Firestore the way the app does. The app writes this snapshot
 * whenever it recomputes the real thing and the widgets read only this. It is
 * a cache, never a source of truth: every field here is derived from data the
 * app already holds, and a stale or missing snapshot costs a wrong number on
 * a widget, never a wrong number in the app.
 */
data class WidgetSnapshot(
    /** Today's loop. */
    val stepsDone: Int = 0,
    val stepsTotal: Int = 0,
    val minutesLeft: Int = 0,
    /** The next thing to do, already worded for a small space. */
    val nextLabel: String = "",
    val nextCourseId: String = "",
    val nextTopicId: String = "",
    /** Questions the spaced schedule has brought back round, across all courses. */
    val questionsDue: Int = 0,
    val questionsCourseId: String = "",
    /** One word to meet on the home screen. */
    val word: String = "",
    val wordMeaning: String = "",
    val wordExample: String = "",
    val wordCourseId: String = "",
    /** The shared run. */
    val togetherStreak: Int = 0,
    val partnerName: String = "",
    /** 0 neither, 1 only me, 2 only them, 3 both. Kept as an Int so the snapshot stays flat. */
    val togetherState: Int = 0,
    /** The day this was written, so a widget can tell that it is looking at yesterday. */
    val dateIso: String = "",
) {
    val loopClosed: Boolean get() = stepsTotal > 0 && stepsDone == stepsTotal
    val hasWord: Boolean get() = word.isNotBlank()

    /** True when the snapshot was written on a different day and its counts may have moved on. */
    fun stale(today: LocalDate): Boolean = dateIso.isNotBlank() && dateIso != today.toString()

    companion object {
        /**
         * Builds the snapshot from the same data the app shows, so the widgets
         * cannot drift from the screens. [wordOfDay] is picked by the date
         * rather than at random, so it is the same word all day and a new one
         * tomorrow.
         */
        fun of(
            owner: PersonRole,
            courses: List<Course>,
            progressByCourse: Map<String, CourseProgress>,
            topicProgress: List<TopicProgress>,
            attempts: List<QuizAttempt>,
            flashcards: List<Flashcard>,
            togetherStreak: Int,
            togetherState: Int,
            partnerName: String,
            today: LocalDate,
        ): WidgetSnapshot {
            val daily = DailyStudy.of(owner, courses, progressByCourse, topicProgress, attempts, flashcards, today)
            val next = daily.next
            val questions = courses
                .map { it.id to (progressByCourse[it.id]?.dailyDueCount(today) ?: 0) }
                .maxByOrNull { it.second }
            val word = wordOfDay(owner, courses, topicProgress, today)
            return WidgetSnapshot(
                stepsDone = daily.doneCount,
                stepsTotal = daily.total,
                minutesLeft = daily.minutesLeft,
                nextLabel = next?.title.orEmpty(),
                nextCourseId = next?.courseId.orEmpty(),
                nextTopicId = next?.topicId.orEmpty(),
                questionsDue = questions?.second ?: 0,
                questionsCourseId = if ((questions?.second ?: 0) > 0) questions!!.first else "",
                word = word?.display.orEmpty(),
                wordMeaning = word?.en.orEmpty(),
                wordExample = word?.example.orEmpty(),
                wordCourseId = word?.let { w -> courses.firstOrNull { c -> Courses.words(c).any { it.id == w.id } }?.id }.orEmpty(),
                togetherStreak = togetherStreak,
                partnerName = partnerName,
                togetherState = togetherState,
                dateIso = today.toString(),
            )
        }

        /**
         * One word for the day, chosen from the words this person has actually
         * met. Picking by the date keeps it steady from breakfast to bedtime
         * and turns it over at midnight, which is what makes seeing it twenty
         * times a day worth anything.
         */
        fun wordOfDay(
            owner: PersonRole,
            courses: List<Course>,
            topicProgress: List<TopicProgress>,
            today: LocalDate,
        ): Word? {
            val met = courses.flatMap { c -> WordDeck.metWordIds(owner, c, topicProgress).map { c to it } }
            if (met.isEmpty()) return null
            val byId = courses.flatMap { Courses.words(it) }.associateBy { it.id }
            // Sorted so the choice does not depend on the order rows arrived in.
            val ids = met.map { it.second }.distinct().sorted()
            return byId[ids[(today.toEpochDay().mod(ids.size))]]
        }
    }
}
