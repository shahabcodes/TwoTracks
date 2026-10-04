package com.shahaabapps.core.model

/** What a level item points at. */
enum class LevelItemKind {
    /** A study goal; done when the goal is marked passed (or hours confirmed complete). */
    GOAL,
    /** A one-off task with a checkbox. */
    TASK,
    /** An ongoing count (e.g. applications per week); shown, never blocks completion. */
    COUNT,
}

/**
 * One line in a level's checklist.
 *
 * @param goalId for GOAL items, the StudyGoal id.
 * @param habitId for COUNT items, the Habit id.
 * @param required optional items never block the level.
 * @param done for TASK items; GOAL items derive from the goal, COUNT items are never "done".
 */
data class LevelItem(
    val id: String = "",
    val kind: LevelItemKind = LevelItemKind.TASK,
    val title: String = "",
    val detail: String = "",
    val goalId: String? = null,
    val habitId: String? = null,
    val dueDateIso: String? = null,
    val required: Boolean = true,
    val done: Boolean = false,
    val doneAtIso: String? = null,
)

/**
 * A level on a person's ladder: a small set of items with a start-by and a
 * complete-by date. Only one level is the headline "current" level, but any
 * level whose start date has passed is open, so quarters may overlap as the
 * guide expects.
 *
 * @param startedEarly set when the user taps "Start next level now".
 * @param completedAtIso set when every required item was done.
 */
data class Level(
    val id: String = "",
    val owner: PersonRole = PersonRole.TECH,
    val number: Int = 1,
    val name: String = "",
    val summary: String = "",
    val startDateIso: String = "",
    val dueDateIso: String = "",
    val items: List<LevelItem> = emptyList(),
    val startedEarly: Boolean = false,
    val completedAtIso: String? = null,
    /** For the checkpoint level: the branch chosen, or null. */
    val branch: String? = null,
)
