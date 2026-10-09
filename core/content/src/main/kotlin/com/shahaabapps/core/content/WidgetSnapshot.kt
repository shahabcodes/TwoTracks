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
    /**
     * Which of the three kinds of work the next step is, as [StudyKind.name],
     * or empty when the day is closed. A topic names a topic to open, but the
     * words and questions steps have no topic at all, and without the kind a
     * widget cannot tell "nothing left to do" from "the deck is what is left".
     */
    val nextKind: String = "",
    /** Questions the spaced schedule has brought back round, across all courses. */
    val questionsDue: Int = 0,
    val questionsCourseId: String = "",
    /** One thing to meet on the home screen: a vocabulary word, or a term a course defines. */
    val word: String = "",
    val wordMeaning: String = "",
    val wordExample: String = "",
    val wordCourseId: String = "",
    val wordTopicId: String = "",
    /** True when it came from a glossary rather than a vocabulary list, which changes where tapping goes. */
    val wordIsTerm: Boolean = false,
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

    /**
     * True once the app has written a snapshot at all. An unwritten one is not
     * a quiet day, it is a widget that has never been filled in, and the two
     * must not read the same on the home screen.
     */
    val synced: Boolean get() = dateIso.isNotBlank()

    /** True when the snapshot was written on a different day and its counts may have moved on. */
    fun stale(today: LocalDate): Boolean = dateIso.isNotBlank() && dateIso != today.toString()

    /** True when today's counts can be trusted: written, and written today. */
    fun fresh(today: LocalDate): Boolean = synced && !stale(today)

    companion object {
        /**
         * Builds the snapshot from the same data the app shows, so the widgets
         * cannot drift from the screens. [dayItem] is picked by the date
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
            val word = dayItem(owner, courses, topicProgress, today)
            return WidgetSnapshot(
                stepsDone = daily.doneCount,
                stepsTotal = daily.total,
                minutesLeft = daily.minutesLeft,
                nextLabel = next?.title.orEmpty(),
                nextCourseId = next?.courseId.orEmpty(),
                nextTopicId = next?.topicId.orEmpty(),
                nextKind = next?.kind?.name.orEmpty(),
                questionsDue = questions?.second ?: 0,
                questionsCourseId = if ((questions?.second ?: 0) > 0) questions!!.first else "",
                word = word?.display.orEmpty(),
                wordMeaning = word?.meaning.orEmpty(),
                wordExample = word?.example.orEmpty(),
                wordCourseId = word?.courseId.orEmpty(),
                wordTopicId = word?.topicId.orEmpty(),
                wordIsTerm = word?.isTerm == true,
                togetherStreak = togetherStreak,
                partnerName = partnerName,
                togetherState = togetherState,
                dateIso = today.toString(),
            )
        }

        /**
         * One thing for the day, chosen from what this person has met.
         * Picking by the date keeps it steady from breakfast to bedtime and
         * turns it over at midnight, which is what makes seeing it twenty
         * times a day worth anything.
         *
         * It draws on glossary terms as well as vocabulary, because only
         * German has vocabulary: for someone working through a certification
         * this was otherwise empty for good, and every one of those topics
         * already defines its terms.
         */
        fun dayItem(
            owner: PersonRole,
            courses: List<Course>,
            topicProgress: List<TopicProgress>,
            today: LocalDate,
        ): DayItem? {
            val learned = topicProgress.filter { it.owner == owner }
                .groupBy({ it.courseId }, { it.topicId })
                .mapValues { (_, ids) -> ids.toSet() }
            val met = courses.flatMap { c -> itemsIn(c, c.allTopics.filter { it.id in learned[c.id].orEmpty() }) }
            // Nothing marked learned yet is still no reason to show an empty
            // card: the terms of the topic each course opens with are the ones
            // about to be needed anyway.
            val pool = met.ifEmpty { courses.flatMap { c -> itemsIn(c, c.allTopics.take(1)) } }
            if (pool.isEmpty()) return null
            // Sorted so the choice does not depend on the order rows arrived in.
            val ordered = pool.distinctBy { it.courseId to it.display }
                .sortedWith(compareBy({ it.courseId }, { it.display }))
            return ordered[today.toEpochDay().mod(ordered.size)]
        }

        private fun itemsIn(course: Course, topics: List<Topic>): List<DayItem> = topics.flatMap { t ->
            t.blocks.filterIsInstance<Block.Vocab>().flatMap { it.words }.map { w ->
                DayItem(w.display, w.en, w.example, course.id, t.id, isTerm = false)
            } + t.blocks.filterIsInstance<Block.Glossary>().flatMap { it.terms }.map { term ->
                DayItem(term.term, term.meaning, "", course.id, t.id, isTerm = true)
            }
        }
    }
}

/**
 * The one thing a widget shows all day. A vocabulary word carries an example
 * sentence and belongs to a deck; a term carries neither and belongs to the
 * topic that defines it, which is where tapping should land.
 */
data class DayItem(
    val display: String,
    val meaning: String,
    val example: String,
    val courseId: String,
    val topicId: String,
    val isTerm: Boolean,
)
