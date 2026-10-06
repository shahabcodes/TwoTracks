package com.shahaabapps.core.planner

import com.shahaabapps.core.model.GoalComponent
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.model.StudyLog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class CourseStateTest {
    private val today = LocalDate.of(2026, 10, 6)
    private val goal = StudyGoal(
        id = "az-204", owner = PersonRole.TECH, title = "AZ-204",
        components = listOf(GoalComponent("compute", "Compute", 15.0), GoalComponent("storage", "Storage", 10.0)),
    )

    @Test
    fun `a component finished by the course is complete with no hours logged`() {
        val p = ProgressCalculator.compute(goal, emptyList(), today, CourseState(completedComponentIds = setOf("compute")))
        val compute = p.components.first { it.component.id == "compute" }
        assertTrue(compute.isComplete)
        assertEquals(1.0, compute.fraction, 1e-9)
        assertEquals(0.0, compute.remainingHours, 1e-9)
        assertFalse(p.isComplete)
        assertEquals(10.0, p.remainingHours, 1e-9)
    }

    @Test
    fun `course hours replace the seeded budget`() {
        val p = ProgressCalculator.compute(goal, listOf(StudyLog(owner = PersonRole.TECH, dateIso = "2026-10-06", goalId = "az-204", componentId = "compute", minutes = 60)), today,
            CourseState(hoursByComponent = mapOf("compute" to 2.0)))
        val compute = p.components.first { it.component.id == "compute" }
        assertEquals(2.0, compute.hoursRequired, 1e-9)
        assertEquals(0.5, compute.fraction, 1e-9)
        assertEquals(1.0, compute.remainingHours, 1e-9)
    }

    @Test
    fun `course fraction lifts the bar above the hours fraction but never hides logged hours`() {
        val p = ProgressCalculator.compute(goal, emptyList(), today, CourseState(fractionByComponent = mapOf("compute" to 0.6)))
        assertEquals(0.6, p.components.first { it.component.id == "compute" }.fraction, 1e-9)
        assertFalse(p.components.first { it.component.id == "compute" }.isComplete)
    }

    @Test
    fun `all components finished by the course completes the goal`() {
        val p = ProgressCalculator.compute(goal, emptyList(), today, CourseState(completedComponentIds = setOf("compute", "storage")))
        assertTrue(p.isComplete)
        assertEquals(Feasibility.DONE, p.feasibility)
    }
}
