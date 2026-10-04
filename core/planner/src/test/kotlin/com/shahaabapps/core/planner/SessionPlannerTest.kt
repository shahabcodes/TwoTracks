package com.shahaabapps.core.planner

import com.shahaabapps.core.model.Availability
import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.core.model.GoalComponent
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.model.StudyGoal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class SessionPlannerTest {

    private val weekStart: LocalDate = LocalDate.of(2026, 10, 5) // Monday

    private val techAvailability = Availability(
        owner = PersonRole.TECH,
        blocks = listOf(7, 1, 2, 3, 4).map { day ->
            AvailabilityBlock(id = "study$day", dayOfWeek = day, startMinutes = 16 * 60 + 15, endMinutes = 17 * 60 + 30, kind = BlockKind.STUDY)
        } + AvailabilityBlock(id = "work1", dayOfWeek = 1, startMinutes = 7 * 60, endMinutes = 15 * 60 + 30, kind = BlockKind.WORK),
    )

    private val german = StudyGoal(
        id = "de-a1", owner = PersonRole.TECH, title = "German A1", priority = 10, targetDateIso = "2026-12-31",
        components = listOf(GoalComponent("v", "Vocabulary", 30.0), GoalComponent("g", "Grammar", 30.0), GoalComponent("l", "Listening", 20.0), GoalComponent("s", "Speaking", 20.0)),
    )
    private val az204 = StudyGoal(
        id = "az204", owner = PersonRole.TECH, title = "AZ-204", priority = 20, targetDateIso = "2027-03-31",
        components = listOf(GoalComponent("c", "Compute", 15.0), GoalComponent("st", "Storage", 10.0)),
    )
    private val inactive = az204.copy(id = "ckad", title = "CKAD", active = false)

    @Test
    fun `all study blocks get a session and inactive goals are never scheduled`() {
        val goals = listOf(german, az204, inactive)
        val progress = ProgressCalculator.computeAll(goals, emptyList(), weekStart)
        val sessions = SessionPlanner.planWeek(weekStart, techAvailability, goals, progress, emptyList())
        assertEquals(5, sessions.size)
        assertTrue(sessions.none { it.goalId == "ckad" })
        assertTrue(sessions.all { it.status == SessionStatus.PLANNED && !it.isClass })
    }

    @Test
    fun `goal with the larger weekly need gets more blocks`() {
        val goals = listOf(german, az204)
        val progress = ProgressCalculator.computeAll(goals, emptyList(), weekStart)
        val sessions = SessionPlanner.planWeek(weekStart, techAvailability, goals, progress, emptyList())
        val germanCount = sessions.count { it.goalId == "de-a1" }
        val azCount = sessions.count { it.goalId == "az204" }
        assertTrue("german=$germanCount az=$azCount", germanCount > azCount)
        assertTrue(azCount >= 1)
    }

    @Test
    fun `class blocks map to their fixed goal and done sessions are preserved`() {
        val ielts = StudyGoal(
            id = "ielts", owner = PersonRole.TEACHER, title = "IELTS Academic", targetDateIso = "2027-01-31",
            components = listOf(GoalComponent("l", "Listening", 30.0), GoalComponent("r", "Reading", 40.0)),
        )
        val availability = Availability(
            owner = PersonRole.TEACHER,
            blocks = (1..5).map { day ->
                AvailabilityBlock(id = "class$day", dayOfWeek = day, startMinutes = 16 * 60, endMinutes = 18 * 60, kind = BlockKind.CLASS, goalId = "ielts")
            },
        )
        val done = PlannedSession(
            id = SessionPlanner.sessionId("2026-10-05", "class1"), owner = PersonRole.TEACHER, dateIso = "2026-10-05",
            blockId = "class1", startMinutes = 960, endMinutes = 1080, goalId = "ielts", componentId = "r", status = SessionStatus.DONE, isClass = true,
        )
        val progress = ProgressCalculator.computeAll(listOf(ielts), emptyList(), weekStart)
        val sessions = SessionPlanner.planWeek(weekStart, availability, listOf(ielts), progress, listOf(done))
        assertEquals(5, sessions.size)
        assertTrue(sessions.all { it.goalId == "ielts" && it.isClass })
        assertEquals(SessionStatus.DONE, sessions.first { it.id == done.id }.status)
        assertEquals("r", sessions.first { it.id == done.id }.componentId)
    }

    @Test
    fun `override pins a block to a chosen goal`() {
        val goals = listOf(german, az204)
        val progress = ProgressCalculator.computeAll(goals, emptyList(), weekStart)
        val id = SessionPlanner.sessionId("2026-10-05", "study1")
        val sessions = SessionPlanner.planWeek(weekStart, techAvailability, goals, progress, emptyList(), override = mapOf(id to "az204"))
        assertEquals("az204", sessions.first { it.id == id }.goalId)
    }
}
