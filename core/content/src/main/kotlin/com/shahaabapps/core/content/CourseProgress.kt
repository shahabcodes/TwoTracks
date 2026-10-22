package com.shahaabapps.core.content

import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.QuizAttempt
import com.shahaabapps.core.model.TopicProgress
import com.shahaabapps.core.planner.QuestionSrs
import com.shahaabapps.core.planner.Srs
import java.time.LocalDate

/** One person's view of a course: what is learned, what is passed. */
data class CourseProgress(
    val learnedTopicIds: Set<String>,
    val passedModuleIds: Set<String>,
    val bestPercentByModule: Map<String, Int>,
    val missedQuestionIds: Set<String>,
    /** Spaced-repetition state per question, folded from the sittings on record. */
    val questionCards: Map<String, Srs.Card> = emptyMap(),
    /** When each topic was read, so spacing can offer the ones left longest. */
    val learnedOn: Map<String, String> = emptyMap(),
) {
    fun moduleLearned(m: Module): Int = m.topics.count { it.id in learnedTopicIds }
    fun moduleFraction(m: Module): Float = if (m.topics.isEmpty()) 0f else moduleLearned(m).toFloat() / m.topics.size
    fun courseFraction(c: Course): Float {
        val total = c.modules.sumOf { it.topics.size }
        return if (total == 0) 0f else c.modules.sumOf { moduleLearned(it) }.toFloat() / total
    }
    fun nextTopic(c: Course): Pair<Module, Topic>? {
        for (m in c.modules) m.topics.firstOrNull { it.id !in learnedTopicIds }?.let { return m to it }
        return null
    }
    /** Module tests passed, leaving out the mock exam and review sittings. */
    fun modulesPassed(c: Course): Int = c.modules.count { it.id in passedModuleIds }
    /** The missed questions that still need a correct answer, in course order. */
    fun reviewQuestions(c: Course): List<Question> = c.allQuestions.filter { it.id in missedQuestionIds }

    /**
     * Questions due again today, longest overdue first. The review set comes
     * first and is skipped here: getting a wrong answer right is more urgent
     * than keeping a right one, and a question should not be asked twice in a day.
     */
    fun dailyDue(c: Course, today: LocalDate, limit: Int = QuestionSrs.DAILY_LIMIT): List<Question> {
        val byId = c.allQuestions.associateBy { it.id }
        return QuestionSrs.due(questionCards, today, skip = missedQuestionIds, limit = limit).mapNotNull { byId[it] }
    }

    fun dailyDueCount(today: LocalDate): Int = QuestionSrs.dueCount(questionCards, today, skip = missedQuestionIds)

    /** Questions held at the top of the schedule, for a sense of what has stuck. */
    val questionsHeld: Int get() = QuestionSrs.matureCount(questionCards)
    /** The best score across every mock exam of the course. */
    val mockBest: Int? get() = bestPercentByModule.filterKeys { Courses.isMock(it) }.values.maxOrNull()
    /** Any of the course's mock exams passed. */
    val mockPassed: Boolean get() = passedModuleIds.any { Courses.isMock(it) }

    companion object {
        fun of(owner: PersonRole, courseId: String, progress: List<TopicProgress>, attempts: List<QuizAttempt>): CourseProgress {
            val mine = progress.filter { it.owner == owner && it.courseId == courseId }
            val att = attempts.filter { it.owner == owner && it.courseId == courseId }
            val passed = att.filter { it.passed }.map { it.moduleId }.toSet()
            val best = att.groupBy { it.moduleId }.mapValues { (_, a) -> a.maxOf { it.percent } }
            // A question joins the review set when missed and leaves it when a later sitting
            // (module test, mock exam or review) asks it again and gets it right.
            val course = Courses.byId(courseId)
            val missed = linkedSetOf<String>()
            att.sortedWith(compareBy({ it.dateIso }, { it.at }, { it.id })).forEach { a ->
                val asked = a.asked.ifEmpty { askedByDefault(course, a.moduleId) }
                missed -= asked.toSet()
                missed += a.missed
            }
            return CourseProgress(
                mine.map { it.topicId }.toSet(), passed, best, missed,
                questionCards = QuestionSrs.states(
                    att, owner, courseId,
                    course?.allQuestions?.map { it.id }?.toSet().orEmpty(),
                    course?.allQuestions?.associate { it.id to it.expectedSeconds }.orEmpty(),
                ),
                // Latest reading wins, so a revisit moves the topic down the queue.
                learnedOn = mine.filter { it.learnedAtIso.isNotBlank() }
                    .groupBy { it.topicId }
                    .mapValues { (_, rows) -> rows.maxOf { it.learnedAtIso } },
            )
        }

        /** Older attempts did not record what was asked: assume the whole test of that module. */
        private fun askedByDefault(course: Course?, moduleId: String): List<String> = when {
            Courses.isMock(moduleId) -> course?.mockModule(moduleId)?.quiz?.questions?.map { it.id }.orEmpty()
            moduleId == Courses.REVIEW_MODULE_ID || moduleId == Courses.CHECK_MODULE_ID || moduleId == Courses.DAILY_MODULE_ID -> emptyList()
            else -> course?.modules?.firstOrNull { it.id == moduleId }?.quiz?.questions?.map { it.id }.orEmpty()
        }
    }
}
