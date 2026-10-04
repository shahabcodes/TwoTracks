package com.shahaabapps.core.planner

import com.shahaabapps.core.model.Level
import com.shahaabapps.core.model.LevelItem
import com.shahaabapps.core.model.LevelItemKind
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyGoal
import java.time.LocalDate
import java.time.temporal.ChronoUnit

enum class LevelState {
    /** Start date in the future and not started early. */
    LOCKED,
    /** Open and not finished; the lowest open level is the headline. */
    OPEN,
    /** Every required item done. */
    DONE,
}

data class LevelItemProgress(
    val item: LevelItem,
    val done: Boolean,
    /** For GOAL items: hours fraction 0..1. */
    val fraction: Double?,
    /** For COUNT items: this week's number. */
    val weekCount: Int?,
)

data class LevelProgress(
    val level: Level,
    val state: LevelState,
    val items: List<LevelItemProgress>,
    val today: LocalDate,
) {
    val requiredTotal: Int get() = items.count { it.item.required && it.item.kind != LevelItemKind.COUNT }
    val requiredDone: Int get() = items.count { it.item.required && it.item.kind != LevelItemKind.COUNT && it.done }
    val isComplete: Boolean get() = requiredTotal > 0 && requiredDone == requiredTotal
    val dueDate: LocalDate? get() = Dates.parseOrNull(level.dueDateIso)
    val startDate: LocalDate? get() = Dates.parseOrNull(level.startDateIso)
    /** Days until the due date; negative when overdue. */
    val daysLeft: Long? get() = dueDate?.let { ChronoUnit.DAYS.between(today, it) }
    val isOverdue: Boolean get() = state == LevelState.OPEN && (daysLeft ?: 0) < 0
    val goalIds: List<String> get() = items.mapNotNull { it.item.goalId }
}

/**
 * Derives level states for one person. Rules:
 *  - A level is DONE when all required GOAL/TASK items are done.
 *  - A level is OPEN when not done and (start date <= today, or started early,
 *    or it is the lowest not-done level). So there is always one open level,
 *    and quarters may overlap.
 *  - Everything else is LOCKED.
 */
object LevelEngine {

    fun progress(
        levels: List<Level>,
        goals: List<StudyGoal>,
        goalProgress: Map<String, GoalProgress>,
        weekCounts: Map<String, Int>,
        today: LocalDate,
        owner: PersonRole,
    ): List<LevelProgress> {
        val goalsById = goals.associateBy { it.id }
        val mine = levels.filter { it.owner == owner }.sortedBy { it.number }
        val provisional = mine.map { level ->
            val items = level.items.map { item ->
                when (item.kind) {
                    LevelItemKind.GOAL -> {
                        val goal = item.goalId?.let { goalsById[it] }
                        val p = item.goalId?.let { goalProgress[it] }
                        LevelItemProgress(item, done = goal?.passed == true, fraction = p?.fraction, weekCount = null)
                    }
                    LevelItemKind.TASK -> LevelItemProgress(item, done = item.done, fraction = null, weekCount = null)
                    LevelItemKind.COUNT -> LevelItemProgress(item, done = false, fraction = null, weekCount = item.habitId?.let { weekCounts[it] } ?: 0)
                }
            }
            LevelProgress(level, LevelState.LOCKED, items, today)
        }
        val firstNotDone = provisional.firstOrNull { !it.isComplete }?.level?.id
        return provisional.map { p ->
            val state = when {
                p.isComplete -> LevelState.DONE
                p.level.id == firstNotDone -> LevelState.OPEN
                p.level.startedEarly -> LevelState.OPEN
                p.startDate?.let { !it.isAfter(today) } == true -> LevelState.OPEN
                else -> LevelState.LOCKED
            }
            p.copy(state = state)
        }
    }

    /** The headline level: the lowest open one, or the last level when all are done. */
    fun current(progress: List<LevelProgress>): LevelProgress? =
        progress.firstOrNull { it.state == LevelState.OPEN } ?: progress.lastOrNull()

    /** The level after the headline, if any. */
    fun next(progress: List<LevelProgress>): LevelProgress? {
        val cur = current(progress) ?: return null
        return progress.firstOrNull { it.level.number > cur.level.number }
    }

    /** Goals the planner should schedule: goals of OPEN levels that are not passed and not complete. */
    fun activeGoalIds(progress: List<LevelProgress>, goals: List<StudyGoal>, goalProgress: Map<String, GoalProgress>): Set<String> {
        val goalsById = goals.associateBy { it.id }
        return progress.filter { it.state == LevelState.OPEN }
            .flatMap { it.goalIds }
            .filter { id -> goalsById[id]?.passed != true && goalProgress[id]?.isComplete != true }
            .toSet()
    }
}
