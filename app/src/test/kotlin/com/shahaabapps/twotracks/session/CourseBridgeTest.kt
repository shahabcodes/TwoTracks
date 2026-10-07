package com.shahaabapps.twotracks.session

import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.QuizAttempt
import com.shahaabapps.core.model.TopicProgress
import org.junit.Assert.assertEquals
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

    // ------------------------------------------------- components several modules feed

    private val ielts = Courses.byId("ielts")!!
    private val readingModules = ielts.modules.filter { it.componentId == "reading" }

    private fun learned(courseId: String, topicIds: List<String>) =
        topicIds.map { TopicProgress(owner = me, courseId = courseId, topicId = it, learnedAtIso = "2026-10-06") }

    private fun passes(courseId: String, moduleIds: List<String>) =
        moduleIds.map { QuizAttempt(owner = me, courseId = courseId, moduleId = it, dateIso = "2026-10-06", score = 20, total = 20, passed = true) }

    @Test
    fun `IELTS reading really is fed by more than one module`() {
        // The rest of these tests are worthless if it ever stops being true.
        assertTrue("reading spans ${readingModules.size} module(s)", readingModules.size > 1)
    }

    @Test
    fun `finishing one module of a shared component does not complete it`() {
        val first = readingModules.first()
        val state = CourseBridge.statesFor(me, learned(ielts.id, first.topics.map { it.id }), passes(ielts.id, listOf(first.id)))
            .getValue(ielts.goalId)
        assertFalse("one of ${readingModules.size} reading modules is not all of reading", "reading" in state.completedComponentIds)
        val fraction = state.fractionByComponent.getValue("reading")
        assertTrue("reading should read as part done, was $fraction", fraction > 0.0 && fraction < 1.0)
        assertTrue("reading should still hold hours", state.remainingHoursByComponent.getValue("reading") > 0.0)
    }

    @Test
    fun `a shared component completes when every module feeding it is finished`() {
        val state = CourseBridge.statesFor(
            me,
            learned(ielts.id, readingModules.flatMap { m -> m.topics.map { it.id } }),
            passes(ielts.id, readingModules.map { it.id }),
        ).getValue(ielts.goalId)
        assertTrue("reading" in state.completedComponentIds)
        assertEquals(1.0, state.fractionByComponent.getValue("reading"), 1e-9)
        assertEquals(0.0, state.remainingHoursByComponent.getValue("reading"), 1e-9)
    }

    @Test
    fun `the last module of a shared component cannot stand for the whole group`() {
        // The old aggregation kept whichever module came last, so finishing only
        // that one reported the component as done.
        val last = readingModules.last()
        val state = CourseBridge.statesFor(me, learned(ielts.id, last.topics.map { it.id }), passes(ielts.id, listOf(last.id)))
            .getValue(ielts.goalId)
        assertTrue("reading should not be complete", "reading" !in state.completedComponentIds)
        assertTrue("reading should not read as whole", state.fractionByComponent.getValue("reading") < 1.0)
    }

    // ---------------------------------------------------------- hours still outstanding

    @Test
    fun `remaining hours shrink as a module is worked through and empty when it is finished`() {
        val m = course.modules.first { it.available }
        fun left(progress: List<TopicProgress>, attempts: List<QuizAttempt>) =
            CourseBridge.statesFor(me, progress, attempts).getValue(course.goalId).remainingHoursByComponent.getValue(m.componentId)

        val untouched = left(emptyList(), emptyList())
        val partRead = left(learned(course.id, m.topics.take(m.topics.size / 2).map { it.id }), emptyList())
        assertTrue("reading topics should reduce what is left: $untouched then $partRead", partRead < untouched)
        assertTrue("a part-read module still holds hours", partRead > 0.0)
        assertEquals(0.0, left(allTopicsLearned(), allModuleTestsPassed()), 1e-9)
    }

    @Test
    fun `practice hours shrink with each test and only empty once the mock is passed`() {
        fun practice(attempts: List<QuizAttempt>) =
            CourseBridge.statesFor(me, allTopicsLearned(), attempts).getValue(course.goalId).remainingHoursByComponent.getValue("practice")

        assertEquals(course.practiceHours, practice(emptyList()), 1e-9)
        val afterTests = practice(allModuleTestsPassed())
        assertTrue("tests should eat into practice", afterTests < course.practiceHours)
        assertTrue("the mock is still outstanding, so practice is not empty: $afterTests", afterTests > 0.0)
        assertEquals(0.0, practice(allModuleTestsPassed() + pass(Courses.MOCK_MODULE_ID)), 1e-9)
    }

    @Test
    fun `an untouched course has every component's whole budget still to do`() {
        val state = CourseBridge.statesFor(me, emptyList(), emptyList()).getValue(course.goalId)
        state.hoursByComponent.forEach { (component, budget) ->
            assertEquals("$component should start with its whole budget", budget, state.remainingHoursByComponent.getValue(component), 1e-9)
        }
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
