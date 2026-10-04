package com.shahaabapps.core.planner

import com.shahaabapps.core.model.Availability
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.model.StudyGoal
import java.time.LocalDate

/**
 * Fills a week's STUDY blocks with the active goals that need the most time,
 * and maps CLASS blocks to their fixed goal.
 *
 * Allocation is greedy: each STUDY block goes to the goal with the largest
 * unmet weekly need (minutes needed this week minus minutes already allocated),
 * ties broken by priority. Within a goal, the two least-complete components
 * alternate so lopsided study is visible and corrected.
 *
 * Sessions already DONE or SKIPPED are kept as they are; PLANNED ones are
 * regenerated. Session ids are deterministic (date + block) so re-planning
 * never duplicates.
 */
object SessionPlanner {

    fun sessionId(dateIso: String, blockId: String): String = "${dateIso}_$blockId"

    fun planWeek(
        weekStart: LocalDate,
        availability: Availability,
        goals: List<StudyGoal>,
        progress: Map<String, GoalProgress>,
        existing: List<PlannedSession>,
        override: Map<String, String> = emptyMap(),
    ): List<PlannedSession> {
        val owner = availability.owner
        val activeGoals = goals
            .filter { it.owner == owner && it.active && !(progress[it.id]?.isComplete ?: false) }
            .sortedBy { it.priority }
        val kept = existing.filter { it.status != SessionStatus.PLANNED }.associateBy { it.id }

        // Weekly need in minutes per goal; goals without a target date get a
        // fallback equal to their remaining hours spread over 12 weeks.
        val needMinutes = activeGoals.associate { goal ->
            val p = progress[goal.id]
            val weeklyHours = p?.weeklyNeedHours ?: ((p?.remainingHours ?: goal.totalHoursRequired) / 12.0)
            goal.id to (weeklyHours * 60).toInt()
        }
        val allocated = activeGoals.associate { it.id to 0 }.toMutableMap()
        val componentCursor = mutableMapOf<String, Int>()

        val blocksByDay = availability.blocks
            .filter { it.kind != BlockKind.WORK }
            .groupBy { it.dayOfWeek }

        val result = mutableListOf<PlannedSession>()
        for (day in Dates.daysOfWeek(weekStart)) {
            val dateIso = Dates.format(day)
            val blocks = blocksByDay[day.dayOfWeek.value].orEmpty().sortedBy { it.startMinutes }
            for (block in blocks) {
                val id = sessionId(dateIso, block.id)
                val keptSession = kept[id]
                if (keptSession != null) {
                    result += keptSession
                    continue
                }
                val isClass = block.kind == BlockKind.CLASS
                val goalId = when {
                    override[id] != null -> override[id]
                    isClass -> block.goalId
                    else -> pickGoal(activeGoals, needMinutes, allocated)
                } ?: continue
                val goal = goals.firstOrNull { it.id == goalId } ?: continue
                val componentId = pickComponent(goal, progress[goal.id], componentCursor)
                allocated[goalId] = (allocated[goalId] ?: 0) + block.durationMinutes
                result += PlannedSession(
                    id = id,
                    owner = owner,
                    dateIso = dateIso,
                    blockId = block.id,
                    startMinutes = block.startMinutes,
                    endMinutes = block.endMinutes,
                    goalId = goalId,
                    componentId = componentId,
                    status = SessionStatus.PLANNED,
                    isClass = isClass,
                )
            }
        }
        return result
    }

    private fun pickGoal(
        goals: List<StudyGoal>,
        need: Map<String, Int>,
        allocated: Map<String, Int>,
    ): String? {
        if (goals.isEmpty()) return null
        // Every active goal gets at least one block per week (in priority order)
        // before the remaining blocks go to the largest unmet need. Without this,
        // a capacity-starved week would starve small goals like a 60-hour cert.
        val unallocated = goals.filter { (allocated[it.id] ?: 0) == 0 }
        if (unallocated.isNotEmpty()) return unallocated.minBy { it.priority }.id
        return goals.maxWithOrNull(
            compareBy<StudyGoal> { (need[it.id] ?: 0) - (allocated[it.id] ?: 0) }
                .thenByDescending { it.priority },
        )?.id
    }

    private fun pickComponent(
        goal: StudyGoal,
        progress: GoalProgress?,
        cursor: MutableMap<String, Int>,
    ): String {
        if (goal.components.isEmpty()) return ""
        val open = progress?.components?.filter { !it.isComplete }?.map { it.component } ?: goal.components
        val candidates = open.ifEmpty { goal.components }
        val sorted = candidates.sortedBy { c ->
            progress?.components?.firstOrNull { it.component.id == c.id }?.fraction ?: 0.0
        }
        val pool = sorted.take(2)
        val idx = cursor.getOrDefault(goal.id, 0)
        cursor[goal.id] = idx + 1
        return pool[idx % pool.size].id
    }
}
