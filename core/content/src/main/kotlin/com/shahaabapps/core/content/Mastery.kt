package com.shahaabapps.core.content

import com.shahaabapps.core.planner.Srs
import java.time.LocalDate

/**
 * How well a topic is known.
 *
 * "Learned" was a tick: you pressed a button at the bottom of the page and the
 * topic was done for ever. That is a reading log, not a measure of knowing
 * something, and it is why a course can read as 80% complete while most of it
 * has quietly faded.
 *
 * The rungs are earned by what the records already show, so nothing new is
 * stored and nothing can be ticked by hand:
 *
 * - [UNREAD]    — never opened.
 * - [READ]      — read, but none of its questions answered correctly yet.
 * - [PRACTISED] — its questions have been answered correctly at least once.
 * - [HELD]      — its questions have survived the whole spaced ladder.
 */
enum class Mastery { UNREAD, READ, PRACTISED, HELD }

/**
 * One topic's standing, and why.
 *
 * [needsRevisit] is the other half of the same idea: a topic high on the ladder
 * can still have questions the schedule has brought back round, and those are
 * worth rereading before they are missed rather than after.
 */
data class TopicMastery(
    val topicId: String,
    val level: Mastery,
    /** Questions that belong to this topic, whether or not they have been met. */
    val questions: Int,
    /** Of those, how many this person has actually answered at some point. */
    val met: Int,
    /** Of those, how many are currently held at the top of the ladder. */
    val held: Int,
    /** Its questions the spaced schedule has brought back round today. */
    val due: Int,
    /** Its questions sitting in the review set, missed and not yet put right. */
    val missed: Int,
    val readOn: LocalDate?,
) {
    /** Worth going back to: something of its own is due again, or was missed and never fixed. */
    val needsRevisit: Boolean get() = level != Mastery.UNREAD && (due > 0 || missed > 0)

    /**
     * How far up the ladder, for a bar. A topic with no questions of its own
     * stops at [READ], so reading it is as far as it can go and the bar says so.
     */
    val fraction: Float
        get() = when {
            level == Mastery.UNREAD -> 0f
            // Nothing asks about this topic, so reading it is the whole of it.
            questions == 0 -> 1f
            level == Mastery.READ -> 0.34f
            level == Mastery.PRACTISED -> 0.34f + 0.66f * (held.toFloat() / questions).coerceIn(0f, 1f)
            else -> 1f
        }

    val label: String
        get() = when {
            level == Mastery.UNREAD -> "Not read"
            level == Mastery.READ -> if (questions == 0) "Read" else "Read, not yet tested"
            level == Mastery.PRACTISED -> "Practised"
            else -> "Held"
        }
}

/**
 * Every topic's standing, folded from the same records the rest of the app
 * reads. A question belongs to a topic through [Question.topicId], so a module
 * test and a mock exam both feed the topics they drew from.
 */
fun Course.mastery(p: CourseProgress, today: LocalDate): Map<String, TopicMastery> {
    val cardsByTopic = HashMap<String, MutableList<Srs.Card>>()
    val topicOfQuestion = HashMap<String, String>()
    val questionsPerTopic = HashMap<String, Int>()
    allQuestions.forEach {
        topicOfQuestion[it.id] = it.topicId
        questionsPerTopic[it.topicId] = (questionsPerTopic[it.topicId] ?: 0) + 1
    }
    p.questionCards.forEach { (qid, card) ->
        val topic = topicOfQuestion[qid] ?: return@forEach
        cardsByTopic.getOrPut(topic) { mutableListOf() } += card
    }
    val missedByTopic = p.missedQuestionIds.groupingBy { topicOfQuestion[it].orEmpty() }.eachCount()

    return allTopics.associate { t ->
        val read = t.id in p.learnedTopicIds
        val cards = cardsByTopic[t.id].orEmpty()
        val held = cards.count { it.learned }
        val anyRight = cards.any { it.correct > 0 }
        val due = cards.count { it.dueIso.isNotEmpty() && Srs.isDue(it, today) }
        val missed = missedByTopic[t.id] ?: 0
        val total = questionsPerTopic[t.id] ?: 0
        // Held only when every question the topic has was met and survived the
        // ladder. One still climbing, or one never seen, means it is not yet
        // put away — otherwise answering a single question would top it out.
        val level = when {
            !read -> Mastery.UNREAD
            total > 0 && held == total -> Mastery.HELD
            anyRight -> Mastery.PRACTISED
            else -> Mastery.READ
        }
        t.id to TopicMastery(
            topicId = t.id,
            level = level,
            questions = total,
            met = cards.size,
            held = held,
            due = due,
            missed = missed,
            readOn = p.learnedOn[t.id]?.let { runCatching { LocalDate.parse(it) }.getOrNull() },
        )
    }
}

/**
 * The topics worth going back to, the ones left longest first.
 *
 * This is what spacing the reading means in practice: rather than a course
 * being a queue you walk once, the topics whose questions have come back round
 * are offered again, oldest reading first so nothing sits at the bottom for ever.
 */
fun Course.revisits(p: CourseProgress, today: LocalDate, limit: Int = 3): List<TopicMastery> =
    mastery(p, today).values
        .filter { it.needsRevisit }
        .sortedWith(compareByDescending<TopicMastery> { it.missed > 0 }.thenBy { it.readOn ?: LocalDate.MIN }.thenBy { it.topicId })
        .take(limit)
