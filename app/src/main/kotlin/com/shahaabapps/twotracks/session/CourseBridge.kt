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

    /** CourseState per goal id, for one person. */
    fun statesFor(owner: PersonRole, progress: List<TopicProgress>, attempts: List<QuizAttempt>): Map<String, CourseState> =
        Courses.all.flatMap { course -> course.goalIds.map { it to course } }.associate { (goalId, course) ->
            val statuses = moduleStatuses(course, owner, progress, attempts)
            goalId to CourseState(
                completedComponentIds = statuses.filter { it.finished }.map { it.module.componentId }.toSet() +
                    // Practice is done when every available module test is passed and, where the
                    // course has one, the mock exam has been passed too.
                    (if (statuses.filter { it.module.available }.let { it.isNotEmpty() && it.all { s -> s.passed } } &&
                            mockPassed(course, owner, attempts)) setOf("practice") else emptySet()),
                // Only components with content get a content budget; the rest keep their seeded hours.
                hoursByComponent = course.hoursByComponent().filterKeys { k -> k == "practice" || statuses.any { it.module.componentId == k && it.module.available } },
                fractionByComponent = statuses.filter { it.module.available }.associate { it.module.componentId to it.fraction.toDouble() },
            )
        }

    /** True when every available module of the course is finished; the goal can be auto-passed. */
    fun courseFinished(course: Course, owner: PersonRole, progress: List<TopicProgress>, attempts: List<QuizAttempt>): Boolean {
        val statuses = moduleStatuses(course, owner, progress, attempts).filter { it.module.available }
        return statuses.isNotEmpty() && statuses.all { it.finished } && course.modules.all { it.available } &&
            mockPassed(course, owner, attempts)
    }
}
