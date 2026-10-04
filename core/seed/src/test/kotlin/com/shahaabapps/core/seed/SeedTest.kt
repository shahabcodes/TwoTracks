package com.shahaabapps.core.seed

import com.shahaabapps.core.model.PersonRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SeedTest {

    @Test
    fun `ids are unique within each collection`() {
        assertEquals(Seed.goals.size, Seed.goals.map { it.id }.toSet().size)
        assertEquals(Seed.levels.size, Seed.levels.map { it.id }.toSet().size)
        assertEquals(Seed.documents.size, Seed.documents.map { it.id }.toSet().size)
        assertEquals(Seed.budget.size, Seed.budget.map { it.id }.toSet().size)
        assertEquals(Seed.tests.size, Seed.tests.map { it.id }.toSet().size)
        assertEquals(6, Seed.countries.size)
    }

    @Test
    fun `every goal has components with positive hours`() {
        Seed.goals.forEach { goal ->
            assertTrue(goal.title, goal.components.isNotEmpty())
            assertTrue(goal.title, goal.components.all { it.hoursRequired > 0 })
            assertEquals(goal.title, goal.components.size, goal.components.map { it.id }.toSet().size)
        }
    }

    @Test
    fun `level items reference existing goals and habits, and each ladder has five levels`() {
        val goalIds = Seed.goals.map { it.id }.toSet()
        val habitIds = Seed.habits.map { it.id }.toSet()
        Seed.levels.forEach { level ->
            assertEquals(level.name, level.items.size, level.items.map { it.id }.toSet().size)
            level.items.forEach { item ->
                item.goalId?.let { assertTrue("${level.name}: $it", it in goalIds) }
                item.habitId?.let { assertTrue("${level.name}: $it", it in habitIds) }
            }
        }
        PersonRole.entries.forEach { role -> assertEquals(listOf(1, 2, 3, 4, 5), Seed.levels.filter { it.owner == role }.map { it.number }) }
    }

    @Test
    fun `class blocks reference an existing goal`() {
        val goalIds = Seed.goals.map { it.id }.toSet()
        SeedSchedule.teacherAvailability.blocks.mapNotNull { it.goalId }.forEach { assertTrue(it, it in goalIds) }
    }

    @Test
    fun `seeded capacity matches the plan`() {
        assertEquals(5 * 75, SeedSchedule.techAvailability.weeklyStudyMinutes)
        assertEquals(5 * 120, SeedSchedule.teacherAvailability.weeklyStudyMinutes)
        assertTrue(Seed.goals.any { it.owner == PersonRole.TEACHER && it.active })
    }
}
