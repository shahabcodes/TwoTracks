package com.shahaabapps.core.planner

import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.QuizAttempt
import java.time.LocalDate

/**
 * Spaced repetition over quiz questions, so a course keeps giving a person
 * something to do after the last module test is passed. Vocabulary already had
 * this through the flashcard deck; a certification or an exam course had only
 * the review set, which drains to empty and then stays empty.
 *
 * Nothing new is stored. The schedule is folded from the attempts already on
 * record: every sitting lists what it asked and what was missed, which is
 * exactly the history [Srs] needs. That means no migration, no second source
 * of truth to drift, and a queue that is already populated from every test
 * the person has ever sat.
 *
 * Only questions a sitting actually asked take part, so the daily queue never
 * shows a module's test questions before that test has been sat — the test
 * stays an assessment rather than a memory check.
 */
object QuestionSrs {

    /** A day's worth of spaced review, shorter than the word deck's because questions take longer. */
    const val DAILY_LIMIT = 10

    /**
     * The scheduling state of every question this person has answered in this
     * course, replayed through [Srs] in the order the sittings happened.
     *
     * [knownQuestionIds] keeps questions that have since been edited out of the
     * content from lingering in the queue. Attempts recorded before sittings
     * listed what they asked are skipped: there is no way to tell what was seen,
     * and crediting a whole test from one old row would schedule questions the
     * person never read.
     *
     * [expectedSeconds] is how long each question ought to take, which turns
     * the clock a sitting recorded into a reading of how sure the answer was.
     * Leave it out and every answer is paced [Pace.Verdict.UNKNOWN], which is
     * also what every attempt recorded before sittings kept a clock gets.
     */
    fun states(
        attempts: List<QuizAttempt>,
        owner: PersonRole,
        courseId: String,
        knownQuestionIds: Set<String>,
        expectedSeconds: Map<String, Int> = emptyMap(),
    ): Map<String, Srs.Card> {
        val cards = LinkedHashMap<String, Srs.Card>()
        attempts.asSequence()
            .filter { it.owner == owner && it.courseId == courseId && it.asked.isNotEmpty() }
            .sortedWith(compareBy({ it.dateIso }, { it.at }, { it.id }))
            .forEach { attempt ->
                val day = Dates.parseOrNull(attempt.dateIso) ?: return@forEach
                val missed = attempt.missed.toSet()
                attempt.asked.filter { it in knownQuestionIds }.forEach { id ->
                    // Asked beats inferred: an in-topic check is told how sure
                    // the person was, and the clock is only ever a guess at it.
                    val confidence = attempt.sure[id]
                        ?.let { if (it) Srs.Confidence.HIGH else Srs.Confidence.LOW }
                        ?: with(Pace) { Pace.verdict(attempt.seconds[id], expectedSeconds[id]).asConfidence() }
                    cards[id] = Srs.review(cards[id] ?: Srs.Card(id), correct = id !in missed, today = day, confidence = confidence)
                }
            }
        return cards
    }

    /**
     * The questions due for a spaced review on [today], the longest overdue
     * first. [skip] holds the questions already queued somewhere more urgent —
     * the review set, which is for getting a wrong answer right rather than for
     * keeping a right one — so the same question is never asked twice in a day.
     *
     * Within one day's worth, the ones last got wrong quickly come first: a
     * day is capped at [DAILY_LIMIT], so what the cap leaves out matters, and
     * a wrong belief left unasked stays a wrong belief.
     */
    fun due(
        states: Map<String, Srs.Card>,
        today: LocalDate,
        skip: Set<String> = emptySet(),
        limit: Int = DAILY_LIMIT,
    ): List<String> = states.values.asSequence()
        .filter { it.dueIso.isNotEmpty() && Srs.isDue(it, today) && it.itemId !in skip }
        .sortedWith(compareBy({ it.dueIso }, { !it.shaky }, { it.itemId }))
        .map { it.itemId }
        .take(limit)
        .toList()

    /** How many are waiting, ignoring the daily limit so the count does not lie. */
    fun dueCount(states: Map<String, Srs.Card>, today: LocalDate, skip: Set<String> = emptySet()): Int =
        states.values.count { it.dueIso.isNotEmpty() && Srs.isDue(it, today) && it.itemId !in skip }

    /** Questions whose schedule has reached the top of the ladder: held, for a readiness read. */
    fun matureCount(states: Map<String, Srs.Card>): Int = states.values.count { it.learned }
}
