package com.shahaabapps.twotracks.feature.study

import com.shahaabapps.core.model.CertTier
import com.shahaabapps.core.model.GoalComponent
import com.shahaabapps.core.model.GoalKind
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyGoal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The goal form is the one place a person types something that everything
 * else then divides by.
 *
 * An hour budget is the denominator of every progress bar the goal appears
 * in, the planner's weekly need, and the fit check that decides whether a
 * level can still be finished on time. A component id is the key a logged
 * minute is matched against. These are the ways a form can quietly produce a
 * record that is wrong rather than refused.
 */
class GoalDraftsTest {

    private fun draft(name: String = "Listening", hours: String = "40", target: String = "", id: String = "listening") =
        ComponentDraft(id, name, hours, target)

    private fun state(
        title: String = "IELTS 7",
        components: List<ComponentDraft> = listOf(draft()),
        targetDateIso: String = "",
        saving: Boolean = false,
    ) = GoalEditUiState(title = title, components = components, targetDateIso = targetDateIso, saving = saving)

    // ---- what may be saved ----------------------------------------------

    @Test
    fun `a goal needs a title and at least one component`() {
        assertTrue(GoalDrafts.canSave(state()))
        assertFalse(GoalDrafts.canSave(state(title = "")))
        assertFalse(GoalDrafts.canSave(state(title = "   ")))
        assertFalse(GoalDrafts.canSave(state(components = emptyList())))
    }

    @Test
    fun `a component with no hours cannot be saved`() {
        // Nought hours is a division by zero in every bar this goal appears
        // in, so it is refused rather than stored and worked around later.
        assertFalse(GoalDrafts.canSave(state(components = listOf(draft(hours = "0")))))
        assertFalse(GoalDrafts.canSave(state(components = listOf(draft(hours = "")))))
        assertFalse(GoalDrafts.canSave(state(components = listOf(draft(hours = "lots")))))
        assertFalse(GoalDrafts.canSave(state(components = listOf(draft(hours = "-5")))))
        assertTrue(GoalDrafts.canSave(state(components = listOf(draft(hours = "0.5")))))
    }

    @Test
    fun `one bad component is enough to stop the save`() {
        val mixed = listOf(draft(), draft(name = "Reading", hours = "", id = "reading"))
        assertFalse(GoalDrafts.canSave(state(components = mixed)))
    }

    @Test
    fun `a target date is optional, but a typed one has to parse`() {
        assertTrue(GoalDrafts.canSave(state(targetDateIso = "")))
        assertTrue(GoalDrafts.canSave(state(targetDateIso = "2026-06-01")))
        assertFalse(GoalDrafts.canSave(state(targetDateIso = "June 2026")))
        assertFalse(GoalDrafts.canSave(state(targetDateIso = "2026-13-01")))
        assertFalse(GoalDrafts.canSave(state(targetDateIso = "01/06/2026")))
    }

    @Test
    fun `a save already in flight cannot be started again`() {
        assertFalse(GoalDrafts.canSave(state(saving = true)))
    }

    // ---- the fields -----------------------------------------------------

    @Test
    fun `an hour budget loses its decimal point when it does not need one`() {
        assertEquals("40", GoalDrafts.hoursField(40.0))
        assertEquals("40.5", GoalDrafts.hoursField(40.5))
        assertEquals("0", GoalDrafts.hoursField(0.0))
    }

    @Test
    fun `priority takes digits only, and no more than three`() {
        assertEquals("50", GoalDrafts.priorityField("50"))
        assertEquals("50", GoalDrafts.priorityField("5a0"))
        assertEquals("123", GoalDrafts.priorityField("12345"))
        assertEquals("", GoalDrafts.priorityField("high"))
    }

    @Test
    fun `a component id made from a name is usable as a key`() {
        assertEquals("speaking", GoalDrafts.slug("Speaking"))
        assertEquals("writing-task-1", GoalDrafts.slug("Writing Task 1"))
    }

    // ---- what gets stored -----------------------------------------------

    @Test
    fun `a new goal is trimmed, parsed and owned by whoever is signed in`() {
        val s = state(title = "  IELTS 7  ", components = listOf(draft(name = " Listening ", hours = "40.5", target = "7.0")))
            .copy(subtitle = " Academic ", why = " Registration ", kind = GoalKind.LANGUAGE_TEST, priority = "7", active = false)
        val goal = GoalDrafts.build(s, PersonRole.TEACHER, original = null)
        assertEquals("IELTS 7", goal.title)
        assertEquals("Academic", goal.subtitle)
        assertEquals("Registration", goal.why)
        assertEquals(GoalKind.LANGUAGE_TEST, goal.kind)
        assertEquals(PersonRole.TEACHER, goal.owner)
        assertEquals(7, goal.priority)
        assertFalse(goal.active)
        assertEquals(1, goal.components.size)
        assertEquals("Listening", goal.components[0].name)
        assertEquals(40.5, goal.components[0].hoursRequired, 0.0)
        assertEquals("7.0", goal.components[0].targetScore)
    }

    @Test
    fun `a blank target date and a blank score are stored as nothing, not as empty text`() {
        // Everything downstream asks whether these are null. An empty string
        // would read as a date that exists and cannot be parsed.
        val goal = GoalDrafts.build(state(targetDateIso = ""), PersonRole.TECH, original = null)
        assertNull(goal.targetDateIso)
        assertNull(goal.components[0].targetScore)
    }

    @Test
    fun `a priority that was typed as nonsense falls back rather than crashing`() {
        val goal = GoalDrafts.build(state().copy(priority = ""), PersonRole.TECH, original = null)
        assertEquals(50, goal.priority)
    }

    @Test
    fun `a component with no id of its own takes one from its name`() {
        val goal = GoalDrafts.build(
            state(components = listOf(draft(id = "", name = "Writing Task 1"))),
            PersonRole.TECH, original = null,
        )
        assertEquals("writing-task-1", goal.components[0].id)
    }

    @Test
    fun `editing a goal keeps what the form never showed`() {
        // The form has no field for passed, the ladder or the fee, and an
        // edit that dropped them would quietly un-pass a finished goal.
        val original = StudyGoal(
            id = "g1", owner = PersonRole.TEACHER, title = "IELTS 7", kind = GoalKind.LANGUAGE_TEST,
            components = listOf(GoalComponent("listening", "Listening", 40.0, "7.0")),
            tier = CertTier.TIER_2, feeUsd = 250, ladder = "IELTS", ladderStep = 2,
            passed = true, passedAtIso = "2026-06-10",
        )
        val goal = GoalDrafts.build(state(title = "IELTS 7.5"), PersonRole.TEACHER, original)
        assertEquals("g1", goal.id)
        assertEquals("IELTS 7.5", goal.title)
        assertTrue(goal.passed)
        assertEquals("2026-06-10", goal.passedAtIso)
        assertEquals(CertTier.TIER_2, goal.tier)
        assertEquals(250, goal.feeUsd)
        assertEquals("IELTS", goal.ladder)
        assertEquals(2, goal.ladderStep)
    }

    @Test
    fun `an edited goal is no longer running on the seeded hour budgets`() {
        // defaultHours is what lets the plan rewrite the budgets. Once
        // somebody has been in this form, it must stop doing that.
        val original = StudyGoal(id = "g1", owner = PersonRole.TECH, title = "AZ-204", defaultHours = true)
        assertFalse(GoalDrafts.build(state(), PersonRole.TECH, original).defaultHours)
        assertFalse(GoalDrafts.build(state(), PersonRole.TECH, null).defaultHours)
    }

    @Test
    fun `a preset replaces the components with ones that already have keys`() {
        val preset = GoalDrafts.preset(listOf("Listening", "Reading", "Writing", "Speaking"), 40)
        assertEquals(4, preset.size)
        assertEquals(listOf("listening", "reading", "writing", "speaking"), preset.map { it.id })
        assertTrue(preset.all { it.hours == "40" })
        assertTrue(GoalDrafts.canSave(state(components = preset)))
    }
}
