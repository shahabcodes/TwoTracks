package com.shahaabapps.core.content

/**
 * How one area of a course went in a single sitting. An area is a module when
 * the paper spans the course, or a topic when it is one module's own test.
 */
data class ScoreGroup(val id: String, val label: String, val correct: Int, val asked: Int) {
    val percent: Int get() = if (asked == 0) 0 else correct * 100 / asked
    /** Everything right: worth saying plainly rather than as a percentage. */
    val clean: Boolean get() = asked > 0 && correct == asked
}

/** Whether a sitting is broken down by module or by topic. */
enum class ScoreBy { MODULE, TOPIC }

/**
 * How a sitting went area by area, so a score becomes a study instruction:
 * a mock exam reads as "Security 4 of 10, Storage 9 of 10" instead of 72%.
 *
 * [asked] is the paper as it was sat, which for a randomised draw is only part
 * of the pool. [correctIds] holds the questions answered correctly; anything
 * asked and absent from it counts as missed, so a paper the clock cut short
 * reads as wrong rather than quietly shrinking the denominator.
 *
 * Areas come back in course order, never the paper's shuffled order, so the
 * same sitting always reads the same way. An area the paper never asked about
 * is left out, as is a question whose topic does not belong to this course.
 */
fun Course.breakdown(asked: List<Question>, correctIds: Set<String>, by: ScoreBy): List<ScoreGroup> {
    if (asked.isEmpty()) return emptyList()
    val labels = LinkedHashMap<String, String>()
    val areaOfTopic = HashMap<String, String>()
    modules.forEach { m ->
        m.topics.forEach { t ->
            val area = if (by == ScoreBy.MODULE) m.id else t.id
            labels.putIfAbsent(area, if (by == ScoreBy.MODULE) m.title else t.title.substringBefore(":"))
            areaOfTopic[t.id] = area
        }
    }
    val correct = HashMap<String, Int>()
    val total = HashMap<String, Int>()
    asked.forEach { q ->
        val area = areaOfTopic[q.topicId] ?: return@forEach
        total[area] = (total[area] ?: 0) + 1
        if (q.id in correctIds) correct[area] = (correct[area] ?: 0) + 1
    }
    return labels.mapNotNull { (area, label) ->
        val n = total[area] ?: return@mapNotNull null
        ScoreGroup(area, label, correct[area] ?: 0, n)
    }
}

/**
 * The areas worth rereading, weakest first: anything under [passPercent].
 * Ties keep course order so the list is stable between sittings.
 */
fun List<ScoreGroup>.weakest(passPercent: Int): List<ScoreGroup> =
    filter { it.percent < passPercent }.sortedBy { it.percent }
