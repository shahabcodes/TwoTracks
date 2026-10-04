package com.shahaabapps.core.planner

import com.shahaabapps.core.model.GoalComponent
import com.shahaabapps.core.model.Level
import com.shahaabapps.core.model.LevelItem
import com.shahaabapps.core.model.LevelItemKind
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyGoal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class LevelEngineTest {

    private val today = LocalDate.of(2026, 10, 4)
    private val german = StudyGoal(id = "de-a1", owner = PersonRole.TECH, title = "German A1", components = listOf(GoalComponent("g", "Grammar", 10.0)))
    private val az = StudyGoal(id = "az-204", owner = PersonRole.TECH, title = "AZ-204", components = listOf(GoalComponent("c", "Compute", 10.0)))
    private val a2 = StudyGoal(id = "de-a2", owner = PersonRole.TECH, title = "German A2", components = listOf(GoalComponent("g", "Grammar", 10.0)))

    private val l1 = Level(
        id = "l1", owner = PersonRole.TECH, number = 1, name = "Foundations", startDateIso = "2026-10-01", dueDateIso = "2027-03-31",
        items = listOf(
            LevelItem("i1", LevelItemKind.GOAL, "German A1", goalId = "de-a1"),
            LevelItem("i2", LevelItemKind.GOAL, "AZ-204", goalId = "az-204"),
            LevelItem("i3", LevelItemKind.TASK, "anabin check"),
            LevelItem("i4", LevelItemKind.GOAL, "PSM I", goalId = "psm", required = false),
        ),
    )
    private val l2 = Level(
        id = "l2", owner = PersonRole.TECH, number = 2, name = "Launch", startDateIso = "2027-01-01", dueDateIso = "2027-03-31",
        items = listOf(LevelItem("j1", LevelItemKind.GOAL, "German A2", goalId = "de-a2"), LevelItem("j2", LevelItemKind.COUNT, "Applications", habitId = "apps")),
    )
    private val l3 = l2.copy(id = "l3", number = 3, name = "Widen", startDateIso = "2027-04-01", dueDateIso = "2027-06-30", items = listOf(LevelItem("k1", LevelItemKind.TASK, "ACS")))

    private fun run(goals: List<StudyGoal>, levels: List<Level> = listOf(l1, l2, l3), on: LocalDate = today) =
        LevelEngine.progress(levels, goals, ProgressCalculator.computeAll(goals, emptyList(), on), mapOf("apps" to 3), on, PersonRole.TECH)

    @Test
    fun `first level is open, later ones locked before their start`() {
        val p = run(listOf(german, az, a2))
        assertEquals(listOf(LevelState.OPEN, LevelState.LOCKED, LevelState.LOCKED), p.map { it.state })
        assertEquals("l1", LevelEngine.current(p)!!.level.id)
        assertEquals("l2", LevelEngine.next(p)!!.level.id)
        assertEquals(3, p[0].requiredTotal) // optional PSM I excluded
        assertEquals(0, p[0].requiredDone)
    }

    @Test
    fun `a level whose start date has passed opens even if the previous is unfinished`() {
        val p = run(listOf(german, az, a2), on = LocalDate.of(2027, 1, 15))
        assertEquals(listOf(LevelState.OPEN, LevelState.OPEN, LevelState.LOCKED), p.map { it.state })
        assertEquals("l1", LevelEngine.current(p)!!.level.id)
        assertTrue(LevelEngine.activeGoalIds(p, listOf(german, az, a2), emptyMap()).containsAll(listOf("de-a1", "az-204", "de-a2")))
    }

    @Test
    fun `level completes when required goals are passed and tasks done, then the next becomes current`() {
        val goals = listOf(german.copy(passed = true), az.copy(passed = true), a2)
        val levels = listOf(l1.copy(items = l1.items.map { if (it.id == "i3") it.copy(done = true) else it }), l2, l3)
        val p = run(goals, levels)
        assertEquals(LevelState.DONE, p[0].state)
        assertEquals(LevelState.OPEN, p[1].state)
        assertEquals("l2", LevelEngine.current(p)!!.level.id)
        assertEquals(setOf("de-a2"), LevelEngine.activeGoalIds(p, goals, emptyMap()))
    }

    @Test
    fun `started early opens a locked level and count items never block`() {
        val p = run(listOf(german, az, a2), levels = listOf(l1, l2.copy(startedEarly = true), l3))
        assertEquals(LevelState.OPEN, p[1].state)
        assertEquals(1, p[1].requiredTotal)
        assertEquals(3, p[1].items.first { it.item.kind == LevelItemKind.COUNT }.weekCount)
    }

    @Test
    fun `overdue is flagged with negative days left`() {
        val p = run(listOf(german, az, a2), on = LocalDate.of(2027, 4, 10))
        assertTrue(p[0].isOverdue)
        assertTrue(p[0].daysLeft!! < 0)
    }
}
