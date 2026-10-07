package com.shahaabapps.twotracks.session

import com.shahaabapps.core.content.Course
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.Module
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.QuizAttempt
import com.shahaabapps.core.model.TopicProgress
import com.shahaabapps.core.planner.CourseState

/**
 * Turns Learn-tab progress into the [CourseState] the planner understands:
 * a module is finished when every topic is learned and its test passed;
 * content budgets replace seeded hours; the learned fraction feeds the bars.
 */
object CourseBridge {

    data class ModuleStatus(val module: Module, val learned: Int, val total: Int, val passed: Boolean, val bestPercent: Int?) {
        val fraction: Float get() = if (total == 0) 0f else learned.toFloat() / total
        val finished: Boolean get() = total > 0 && learned == total && passed
    }

    fun moduleStatuses(course: Course, owner: PersonRole, progress: List<TopicProgress>, attempts: List<QuizAttempt>): List<ModuleStatus> {
        val learned = progress.filter { it.owner == owner && it.courseId == course.id }.map { it.topicId }.toSet()
        val mine = attempts.filter { it.owner == owner && it.courseId == course.id }
        return course.modules.map { m ->
            val att = mine.filter { it.moduleId == m.id }
            ModuleStatus(
                module = m,
                learned = m.topics.count { it.id in learned },
                total = m.topics.size,
                passed = att.any { it.passed },
                bestPercent = att.maxOfOrNull { it.percent },
            )
        }
    }

    /** True when the course's mock exam has been passed, or there is no mock exam to pass. */
    fun mockPassed(course: Course, owner: PersonRole, attempts: List<QuizAttempt>): Boolean =
        course.mockExam == null || attempts.any { it.owner == owner && it.courseId == course.id && it.moduleId == Courses.MOCK_MODULE_ID && it.passed }

    /**
     * Each topic of a module and the module's own test count as one unit of
     * its work, so reading a topic and passing the test both move the bar and
     * a module only reads as whole when both are done.
     */
    private fun units(s: ModuleStatus): Int = s.total + 1
    private fun unitsDone(s: ModuleStatus): Int = s.learned + if (s.passed) 1 else 0

    /**
     * Across every module feeding one component. Several modules share a
     * component — IELTS reading spans three of them — so this sums over the
     * group rather than letting the last module stand for the lot.
     */
    private fun groupFraction(ms: List<ModuleStatus>): Double {
        val total = ms.sumOf { units(it) }
        return if (total == 0) 0.0 else ms.sumOf { unitsDone(it) }.toDouble() / total
    }

    /** The content's own minute estimate for the part of a module not done yet. */
    private fun remainingMinutes(s: ModuleStatus): Double =
        if (s.finished) 0.0 else s.module.studyMinutes * (1.0 - unitsDone(s).toDouble() / units(s))

    /** Practice is the module tests plus, where the course has one, the mock exam. */
    private fun practiceFraction(course: Course, statuses: List<ModuleStatus>, owner: PersonRole, attempts: List<QuizAttempt>): Double {
        val hasMock = course.mockExam != null
        val total = statuses.size + if (hasMock) 1 else 0
        if (total == 0) return 0.0
        val done = statuses.count { it.passed } + if (hasMock && mockPassed(course, owner, attempts)) 1 else 0
        return done.toDouble() / total
    }

    /** CourseState per goal id, for one person. */
    fun statesFor(owner: PersonRole, progress: List<TopicProgress>, attempts: List<QuizAttempt>): Map<String, CourseState> =
        Courses.all.flatMap { course -> course.goalIds.map { it to course } }.associate { (goalId, course) ->
            val statuses = moduleStatuses(course, owner, progress, attempts).filter { it.module.available }
            val byComponent = statuses.groupBy { it.module.componentId }
            val practice = practiceFraction(course, statuses, owner, attempts)
            goalId to CourseState(
                // A component is finished only when every module feeding it is finished, and
                // practice when every module test is passed and, where there is one, the mock too.
                completedComponentIds = byComponent.filterValues { ms -> ms.all { it.finished } }.keys +
                    (if (statuses.isNotEmpty() && practice >= 1.0) setOf("practice") else emptySet()),
                // Only components with content get a content budget; the rest keep their seeded hours.
                hoursByComponent = course.hoursByComponent().filterKeys { it == "practice" || it in byComponent.keys },
                fractionByComponent = byComponent.mapValues { (_, ms) -> groupFraction(ms) } + ("practice" to practice),
                remainingHoursByComponent = byComponent.mapValues { (_, ms) -> ms.sumOf { remainingMinutes(it) } / 60.0 } +
                    ("practice" to course.practiceHours * (1.0 - practice)),
            )
        }

    /** True when every available module of the course is finished; the goal can be auto-passed. */
    fun courseFinished(course: Course, owner: PersonRole, progress: List<TopicProgress>, attempts: List<QuizAttempt>): Boolean {
        val statuses = moduleStatuses(course, owner, progress, attempts).filter { it.module.available }
        return statuses.isNotEmpty() && statuses.all { it.finished } && course.modules.all { it.available } &&
            mockPassed(course, owner, attempts)
    }
}
