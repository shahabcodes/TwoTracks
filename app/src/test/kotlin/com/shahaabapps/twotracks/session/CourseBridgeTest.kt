package com.shahaabapps.twotracks.session

import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.QuizAttempt
import com.shahaabapps.core.model.TopicProgress
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The practice component is only finished when every module test and the
 * course's mock exam have been passed. These tests lock that in for the real
 * shipped courses, so a new course cannot quietly skip the mock.
 */
class CourseBridgeTest {

    private val me = PersonRole.TECH
    private val course = Courses.byId("az-204")!!

    /** Every topic of the course marked learned. */
    private fun allTopicsLearned(): List<TopicProgress> =
        course.allTopics.map { TopicProgress(owner = me, courseId = course.id, topicId = it.id, learnedAtIso = "2026-10-06") }

    private fun pass(moduleId: String) =
        QuizAttempt(owner = me, courseId = course.id, moduleId = moduleId, dateIso = "2026-10-06", score = 20, total = 20, passed = true)

    private fun allModuleTestsPassed(): List<QuizAttempt> = course.modules.filter { it.available }.map { pass(it.id) }

    @Test
    fun `practice is not complete until the mock exam is passed`() {
        val states = CourseBridge.statesFor(me, allTopicsLearned(), allModuleTestsPassed())
        val state = states.getValue(course.goalId)
        assertFalse("practice should wait for the mock", "practice" in state.completedComponentIds)
        assertTrue("module components should be complete", "compute" in state.completedComponentIds)
    }

    @Test
    fun `practice completes once the mock exam is passed`() {
        val attempts = allModuleTestsPassed() + pass(Courses.MOCK_MODULE_ID)
        val state = CourseBridge.statesFor(me, allTopicsLearned(), attempts).getValue(course.goalId)
        assertTrue("practice should be complete", "practice" in state.completedComponentIds)
    }

    @Test
    fun `a failed mock attempt does not complete practice`() {
        val failed = QuizAttempt(owner = me, courseId = course.id, moduleId = Courses.MOCK_MODULE_ID, dateIso = "2026-10-06", score = 20, total = 50, passed = false)
        val state = CourseBridge.statesFor(me, allTopicsLearned(), allModuleTestsPassed() + failed).getValue(course.goalId)
        assertFalse("a failed mock is not a pass", "practice" in state.completedComponentIds)
    }

    @Test
    fun `the course is only finished when the mock exam is passed`() {
        val topics = allTopicsLearned()
        assertFalse(CourseBridge.courseFinished(course, me, topics, allModuleTestsPassed()))
        assertTrue(CourseBridge.courseFinished(course, me, topics, allModuleTestsPassed() + pass(Courses.MOCK_MODULE_ID)))
    }

    @Test
    fun `another person's passes do not count`() {
        val theirs = (allModuleTestsPassed() + pass(Courses.MOCK_MODULE_ID)).map { it.copy(owner = PersonRole.TEACHER) }
        val state = CourseBridge.statesFor(me, allTopicsLearned(), theirs).getValue(course.goalId)
        assertFalse("practice belongs to each person", "practice" in state.completedComponentIds)
    }

    @Test
    fun `every shipped course with a mock exam is covered by the rule`() {
        Courses.all.filter { it.mockExam != null }.forEach { c ->
            val passes = c.modules.filter { it.available }.map {
                QuizAttempt(owner = me, courseId = c.id, moduleId = it.id, dateIso = "2026-10-06", score = 20, total = 20, passed = true)
            }
            assertFalse("${c.id} practice should wait for its mock", CourseBridge.mockPassed(c, me, passes))
            val withMock = passes + QuizAttempt(owner = me, courseId = c.id, moduleId = Courses.MOCK_MODULE_ID, dateIso = "2026-10-06", score = 40, total = 40, passed = true)
            assertTrue("${c.id} mock pass should count", CourseBridge.mockPassed(c, me, withMock))
        }
    }
}
