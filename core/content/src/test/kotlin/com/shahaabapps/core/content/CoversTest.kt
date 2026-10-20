package com.shahaabapps.core.content

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Which goal components a course keeps the time for. The plan reads this to
 * decide whether to ask for minutes: where a course records them as you study,
 * logging a session's planned duration on top would count it twice.
 */
class CoversTest {

    @Test
    fun `a course covers every component its available modules feed`() {
        Courses.all.forEach { c ->
            c.modules.filter { it.available }.forEach { m ->
                assertTrue("${c.id} should cover ${m.componentId}", c.covers(m.componentId))
            }
        }
    }

    @Test
    fun `every course covers practice, because every sitting logs there`() {
        Courses.all.forEach { c -> assertTrue("${c.id} practice", c.covers("practice")) }
    }

    @Test
    fun `a component no module feeds is not covered`() {
        val az = Az204.course
        assertFalse(az.covers("vocabulary"))
        assertFalse(az.covers("speaking"))
        assertFalse(az.covers(""))
    }

    @Test
    fun `a module with no content covers nothing, so its hours stay a person's to log`() {
        val empty = Module("m-empty", 99, "Later", "Not written yet", "", "forecasting", emptyList(), null)
        val course = Az204.course.let { it.copy(modules = it.modules + empty) }
        assertFalse("an unavailable module must not claim its component", course.covers("forecasting"))
        assertTrue("the real ones still count", course.covers(course.modules.first { it.available }.componentId))
    }

    @Test
    fun `the goal lookup covers every goal the shipped courses feed, and nothing else`() {
        // A course can feed several goals: German A1 covers the partner's copy,
        // and the one IELTS course covers General Training for both of them and
        // the Academic paper. All six are recorded by a course.
        listOf(
            "az-204" to "compute",
            "ai-200" to "containers",
            "de-a1" to "vocabulary",
            "de-a1-teacher" to "vocabulary",
            "ielts-general-tech" to "reading",
            "ielts-academic" to "reading",
            "ielts-general-teacher" to "reading",
        ).forEach { (goal, component) -> assertTrue("$goal should be covered", Courses.covers(goal, component)) }

        // The rest of the roadmap is still waiting for a course, and until each
        // one exists, logging by hand is the only way those hours ever move.
        listOf(
            "de-a2", "de-b1", "psm-1", "ckad", "az-400", "java-se-21", "aws-dva",
            "dp-300", "ai-102", "terraform", "naati-tech", "naati-teacher", "french",
        ).forEach {
            assertFalse("$it has no course yet", Courses.covers(it, "reading"))
            assertFalse("$it has no course yet", Courses.covers(it, "practice"))
        }
    }

    @Test
    fun `every goal a course feeds is covered, so none is left asking by hand`() {
        // Guards the pairing itself: adding a goal to extraGoalIds must not
        // leave it logging manually while its sibling logs from the course.
        Courses.all.forEach { c ->
            c.goalIds.forEach { goal ->
                assertTrue("${c.id} feeds $goal but does not cover its practice", Courses.covers(goal, "practice"))
            }
        }
    }

    @Test
    fun `an unknown goal is never covered`() {
        assertFalse(Courses.covers("not-a-goal", "practice"))
    }
}
