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

    // ------------------------------------------- what is left comes from the course

    private fun log(componentId: String, minutes: Int) =
        StudyLog(owner = PersonRole.TECH, dateIso = "2026-10-06", goalId = "az-204", componentId = componentId, minutes = minutes)

    @Test
    fun `the course's estimate of what is left outranks budget minus logged`() {
        // Much of a 30-hour budget is work the app never logs, so an hour on the
        // page says nothing about the 4 hours of content actually left.
        val p = ProgressCalculator.compute(
            goal, listOf(log("compute", 60)), today,
            CourseState(hoursByComponent = mapOf("compute" to 30.0), remainingHoursByComponent = mapOf("compute" to 4.0)),
        )
        val compute = p.components.first { it.component.id == "compute" }
        assertEquals(4.0, compute.remainingHours, 1e-9)
        assertFalse(compute.isComplete)
    }

    @Test
    fun `a component the course has emptied is complete however few hours were logged`() {
        val p = ProgressCalculator.compute(
            goal, emptyList(), today,
            CourseState(hoursByComponent = mapOf("compute" to 30.0), remainingHoursByComponent = mapOf("compute" to 0.0)),
        )
        val compute = p.components.first { it.component.id == "compute" }
        assertTrue(compute.isComplete)
        assertEquals(0.0, compute.remainingHours, 1e-9)
    }

    @Test
    fun `a component no course tracks still works off the seeded budget`() {
        val p = ProgressCalculator.compute(
            goal, listOf(log("storage", 300)), today,
            CourseState(remainingHoursByComponent = mapOf("compute" to 1.0)),
        )
        val storage = p.components.first { it.component.id == "storage" }
        assertEquals(5.0, storage.remainingHours, 1e-9)
    }

    @Test
    fun `the goal's own bar follows the course rather than logged hours`() {
        // Compute is done, storage untouched: 15 of the 25 budgeted hours.
        val p = ProgressCalculator.compute(goal, emptyList(), today, CourseState(completedComponentIds = setOf("compute")))
        assertEquals(0.6, p.fraction, 1e-9)
    }

    @Test
    fun `a goal whose components carry no hours falls back to counting them`() {
        val hourless = goal.copy(components = listOf(GoalComponent("a", "A", 0.0), GoalComponent("b", "B", 0.0)))
        val p = ProgressCalculator.compute(hourless, emptyList(), today, CourseState(completedComponentIds = setOf("a")))
        assertEquals(0.5, p.fraction, 1e-9)
    }

    @Test
    fun `the weekly need and the forecast follow what the course has left`() {
        val dated = goal.copy(targetDateIso = today.plusDays(7).toString())
        val p = ProgressCalculator.compute(
            dated, emptyList(), today,
            CourseState(
                hoursByComponent = mapOf("compute" to 40.0, "storage" to 40.0),
                remainingHoursByComponent = mapOf("compute" to 3.5, "storage" to 3.5),
            ),
        )
        // 7 hours left over one week, not the 80 the budget would have demanded.
        assertEquals(7.0, p.remainingHours, 1e-9)
        assertEquals(7.0, p.weeklyNeedHours!!, 1e-9)
    }

    @Test
    fun `a pace that covers what the course has left reads as feasible`() {
        val dated = goal.copy(targetDateIso = today.plusDays(14).toString())
        val state = CourseState(
            hoursByComponent = mapOf("compute" to 40.0, "storage" to 40.0),
            remainingHoursByComponent = mapOf("compute" to 2.0, "storage" to 2.0),
        )
        // Four hours left, two weeks to go, and three hours a week going in.
        val logs = (0..13).map { StudyLog(owner = PersonRole.TECH, dateIso = today.minusDays(it.toLong()).toString(), goalId = "az-204", componentId = "compute", minutes = 26) }
        val p = ProgressCalculator.compute(dated, logs, today, state)
        assertEquals(Feasibility.FITS, p.feasibility)
    }
}
