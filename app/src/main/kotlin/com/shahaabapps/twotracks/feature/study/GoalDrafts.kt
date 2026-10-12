package com.shahaabapps.twotracks.feature.study

import com.shahaabapps.core.model.GoalComponent
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyGoal
import java.time.LocalDate
import java.util.Locale

/**
 * Turning what was typed into a goal, and deciding whether it may be saved.
 *
 * These were private methods on GoalEditViewModel, which needs a
 * SavedStateHandle and six collaborators to build. That left the only place
 * the app converts a form into a stored record with nothing checking it, and
 * the mistakes it can make are quiet ones: an hour budget of nought saved
 * because the field held a word, a component id with a space in it that then
 * never matches a logged minute, a blank target date stored as an empty
 * string where everything downstream expects null.
 *
 * The view model keeps the loading, the fit check and the saving.
 */
object GoalDrafts {

    /** Trim a stored budget back to something that fits in a text field. */
    fun hoursField(hours: Double): String =
        if (hours % 1.0 == 0.0) hours.toInt().toString() else hours.toString()

    /** Priority is a number up to three digits; anything else typed is dropped. */
    fun priorityField(typed: String): String = typed.filter { it.isDigit() }.take(3)

    /** A component's id when it has none yet: its own name, usable as a key. */
    fun slug(name: String): String = name.lowercase(Locale.ENGLISH).replace(' ', '-')

    /**
     * Whether the form may be saved.
     *
     * Every component needs a name and a budget above nought, because a
     * component with no hours divides by zero in every progress bar it
     * appears in. A target date is optional, but if one is typed it has to
     * parse: a date that does not is worse than none, since the roadmap would
     * silently drop the goal.
     */
    fun canSave(state: GoalEditUiState): Boolean =
        state.title.isNotBlank() &&
            !state.saving &&
            state.components.isNotEmpty() &&
            state.components.all { it.name.isNotBlank() && (it.hours.toDoubleOrNull() ?: 0.0) > 0.0 } &&
            (state.targetDateIso.isBlank() || parsesAsDate(state.targetDateIso))

    private fun parsesAsDate(iso: String): Boolean = runCatching { LocalDate.parse(iso) }.isSuccess

    /**
     * The goal as it will be stored.
     *
     * [original] is the goal being edited, so anything the form does not show
     * — whether it has been passed, which ladder it belongs to — survives the
     * edit. A new goal starts from a blank one owned by whoever is signed in.
     */
    fun build(state: GoalEditUiState, owner: PersonRole, original: StudyGoal?): StudyGoal {
        val base = original ?: StudyGoal(id = "", owner = owner)
        return base.copy(
            title = state.title.trim(),
            subtitle = state.subtitle.trim(),
            why = state.why.trim(),
            kind = state.kind,
            priority = state.priority.toIntOrNull() ?: 50,
            targetDateIso = state.targetDateIso.ifBlank { null },
            active = state.active,
            components = state.components.map { c ->
                GoalComponent(
                    id = c.id.ifBlank { slug(c.name) },
                    name = c.name.trim(),
                    hoursRequired = c.hours.toDoubleOrNull() ?: 0.0,
                    targetScore = c.targetScore.ifBlank { null },
                )
            },
            // Once somebody has edited the budgets they are no longer the
            // seeded defaults, and the plan stops rewriting them.
            defaultHours = false,
        )
    }

    /** Replace the components with a preset, e.g. the four IELTS papers. */
    fun preset(names: List<String>, hoursEach: Int): List<ComponentDraft> =
        names.map { ComponentDraft(slug(it), it, hoursEach.toString(), "") }
}
