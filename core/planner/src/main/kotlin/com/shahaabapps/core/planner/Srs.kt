package com.shahaabapps.core.planner

import java.time.LocalDate

/**
 * Spaced repetition for the flashcard deck. A card climbs a fixed ladder of
 * intervals on each correct review and falls back to the start on a miss.
 * Simple on purpose: no ease factors to tune, and easy to explain in the app.
 */
object Srs {

    /**
     * How sure the person was, which is what the schedule actually wants to
     * know. [Pace] estimates it from the clock; an in-topic check asks and is
     * told, which is better, so a stated answer wins where there is one.
     */
    enum class Confidence { HIGH, NEUTRAL, LOW, UNKNOWN }
    /** Days until the next review after the Nth consecutive correct answer. */
    val LADDER = listOf(1L, 3L, 7L, 14L, 30L, 60L)

    const val DAILY_LIMIT = 15

    data class Card(
        /** The item this card schedules: a vocabulary word, or a quiz question. */
        val itemId: String,
        /** 0 = new, otherwise the index into [LADDER] of the last interval granted. */
        val step: Int = 0,
        val dueIso: String = "",
        val lastReviewIso: String = "",
        val correct: Int = 0,
        val misses: Int = 0,
        /**
         * The last answer was wrong and given quickly: a wrong belief rather
         * than a known gap.
         *
         * A miss already falls to the bottom of the ladder and comes back
         * tomorrow, so there is nothing left to take away from it; what this
         * changes is the order of a capped day, where a question somebody is
         * confidently wrong about should be asked before one they know they
         * do not know. Cleared by any answer that is not another quick miss.
         */
        val shaky: Boolean = false,
    ) {
        val learned: Boolean get() = step >= LADDER.size - 1
    }

    /**
     * New state after a review on [today].
     *
     * [confidence] changes two of the four outcomes. A right answer the
     * person was not sure of holds its place instead of climbing — stretching
     * the gap is what loses it. A wrong answer they were sure of is marked
     * [Card.shaky], which moves it up the queue rather than changing its
     * schedule. [Confidence.UNKNOWN] behaves exactly as this did before there
     * was any such signal.
     */
    fun review(
        card: Card,
        correct: Boolean,
        today: LocalDate,
        confidence: Confidence = Confidence.UNKNOWN,
    ): Card {
        val iso = Dates.format(today)
        if (!correct) {
            return card.copy(
                step = 0, dueIso = Dates.format(today.plusDays(1)), lastReviewIso = iso,
                misses = card.misses + 1, shaky = confidence == Confidence.HIGH,
            )
        }
        // A reconstructed answer keeps the interval it last earned rather
        // than earning a longer one.
        val held = confidence == Confidence.LOW
        val nextStep = if (held) card.step else (card.step + 1).coerceAtMost(LADDER.size - 1)
        val grantIndex = if (held) (card.step - 1).coerceIn(0, LADDER.size - 1) else card.step.coerceIn(0, LADDER.size - 1)
        return card.copy(
            step = nextStep, dueIso = Dates.format(today.plusDays(LADDER[grantIndex])), lastReviewIso = iso,
            correct = card.correct + 1, shaky = false,
        )
    }

    fun isDue(card: Card, today: LocalDate): Boolean = card.dueIso.isEmpty() || !Dates.parse(card.dueIso).isAfter(today)

    /**
     * Today's session: due cards first (oldest due first), then new cards from
     * [newItemIds] that have no state yet, up to [limit].
     */
    fun session(cards: List<Card>, newItemIds: List<String>, today: LocalDate, limit: Int = DAILY_LIMIT): List<String> {
        val byId = cards.associateBy { it.itemId }
        val due = cards.filter { isDue(it, today) && it.dueIso.isNotEmpty() }.sortedBy { it.dueIso }.map { it.itemId }
        val fresh = newItemIds.filter { it !in byId }
        return (due + fresh).distinct().take(limit)
    }

    fun dueCount(cards: List<Card>, today: LocalDate): Int = cards.count { it.dueIso.isNotEmpty() && isDue(it, today) }
}
