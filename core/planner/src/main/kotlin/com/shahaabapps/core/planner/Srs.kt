package com.shahaabapps.core.planner

import java.time.LocalDate

/**
 * Spaced repetition for the flashcard deck. A card climbs a fixed ladder of
 * intervals on each correct review and falls back to the start on a miss.
 * Simple on purpose: no ease factors to tune, and easy to explain in the app.
 */
object Srs {
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
    ) {
        val learned: Boolean get() = step >= LADDER.size - 1
    }

    /** New state after a review on [today]. */
    fun review(card: Card, correct: Boolean, today: LocalDate): Card {
        val iso = Dates.format(today)
        return if (correct) {
            val nextStep = (card.step + 1).coerceAtMost(LADDER.size - 1)
            card.copy(step = nextStep, dueIso = Dates.format(today.plusDays(LADDER[card.step.coerceIn(0, LADDER.size - 1)])), lastReviewIso = iso, correct = card.correct + 1)
        } else {
            card.copy(step = 0, dueIso = Dates.format(today.plusDays(1)), lastReviewIso = iso, misses = card.misses + 1)
        }
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
