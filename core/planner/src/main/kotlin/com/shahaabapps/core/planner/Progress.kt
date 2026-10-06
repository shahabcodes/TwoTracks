package com.shahaabapps.core.planner

import com.shahaabapps.core.model.GoalComponent
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.model.StudyLog
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.ceil
import kotlin.math.max

enum class Feasibility {
    /** No target date set: nothing to compare against. */
    NO_TARGET,
    /** All components complete. */
    DONE,
    /** Recent pace meets or beats the weekly need. */
    FITS,
    /** Recent pace is within 75% of the weekly need. */
    TIGHT,
    /** Recent pace is well below the weekly need, or no study yet with a near date. */
    DOES_NOT_FIT,
}

/**
 * How a course (the Learn tab) reports on one goal: which components it has
 * finished outright, and the hours its content sets for each component.
 * Finished components count as complete whatever the hours say, and content
 * budgets replace the seeded ones so the plan follows the course.
 */
data class CourseState(
    val completedComponentIds: Set<String> = emptySet(),
    val hoursByComponent: Map<String, Double> = emptyMap(),
    /** Learned fraction per component, from the course, for display when hours are not the measure. */
    val fractionByComponent: Map<String, Double> = emptyMap(),
) {
    companion object { val NONE = CourseState() }
}

data class ComponentProgress(
    val component: GoalComponent,
    val minutesLogged: Int,
    /** True when the course finished this component (every topic learned and the test passed). */
    val finishedByCourse: Boolean = false,
    /** Hours the course content sets, replacing the seeded budget when present. */
    val courseHours: Double? = null,
    /** Course learned fraction, used for the bar when the course drives this component. */
    val courseFraction: Double? = null,
) {
    val hoursLogged: Double get() = minutesLogged / 60.0
    val hoursRequired: Double get() = courseHours ?: component.hoursRequired
    val fraction: Double
        get() = when {
            finishedByCourse -> 1.0
            courseFraction != null -> maxOf(courseFraction, if (hoursRequired <= 0.0) 0.0 else (hoursLogged / hoursRequired).coerceIn(0.0, 1.0))
            hoursRequired <= 0.0 -> 1.0
            else -> (hoursLogged / hoursRequired).coerceIn(0.0, 1.0)
        }
    val remainingHours: Double get() = if (finishedByCourse) 0.0 else max(0.0, hoursRequired - hoursLogged)
    val isComplete: Boolean get() = finishedByCourse || (hoursRequired > 0.0 && hoursLogged >= hoursRequired)
}

data class GoalProgress(
    val goal: StudyGoal,
    val components: List<ComponentProgress>,
    val today: LocalDate,
    /** Average hours per week over the last 14 days. */
    val recentWeeklyHours: Double = 0.0,
) {
    val hoursLogged: Double get() = components.sumOf { it.hoursLogged }
    val hoursRequired: Double get() = components.sumOf { it.hoursRequired }
    val remainingHours: Double get() = components.sumOf { it.remainingHours }
    val fraction: Double
        get() = if (hoursRequired <= 0.0) 0.0 else (hoursLogged / hoursRequired).coerceIn(0.0, 1.0)
    val isComplete: Boolean get() = hoursRequired > 0.0 && remainingHours <= 0.0

    val targetDate: LocalDate? get() = Dates.parseOrNull(goal.targetDateIso)

    /** Days from today until the target date (negative when overdue), or null. */
    val daysToTarget: Long? get() = targetDate?.let { ChronoUnit.DAYS.between(today, it) }

    /** Hours per week needed from today to finish by the target date. */
    val weeklyNeedHours: Double?
        get() {
            val days = daysToTarget ?: return null
            if (remainingHours <= 0.0) return 0.0
            val weeks = max(days, 1L) / 7.0
            return remainingHours / weeks
        }

    val dailyNeedHours: Double? get() = weeklyNeedHours?.let { it / 7.0 }

    /** Finish date at the recent pace, or null when there is no pace. */
    val forecastDate: LocalDate?
        get() {
            if (isComplete) return today
            if (recentWeeklyHours <= 0.0) return null
            val weeks = remainingHours / recentWeeklyHours
            return today.plusDays(ceil(weeks * 7).toLong())
        }

    val feasibility: Feasibility
        get() {
            if (isComplete) return Feasibility.DONE
            val need = weeklyNeedHours ?: return Feasibility.NO_TARGET
            if (need <= 0.0) return Feasibility.DONE
            val days = daysToTarget ?: 0L
            if (days < 0) return Feasibility.DOES_NOT_FIT
            return when {
                recentWeeklyHours >= need -> Feasibility.FITS
                recentWeeklyHours >= need * 0.75 -> Feasibility.TIGHT
                else -> Feasibility.DOES_NOT_FIT
            }
        }
}

object ProgressCalculator {

    fun compute(goal: StudyGoal, logs: List<StudyLog>, today: LocalDate, course: CourseState = CourseState.NONE): GoalProgress {
        val goalLogs = logs.filter { it.goalId == goal.id }
        val byComponent = goalLogs.groupBy { it.componentId }
        val components = goal.components.map { c ->
            ComponentProgress(
                c, byComponent[c.id]?.sumOf { it.minutes } ?: 0,
                finishedByCourse = c.id in course.completedComponentIds,
                courseHours = course.hoursByComponent[c.id],
                courseFraction = course.fractionByComponent[c.id],
            )
        }
        val since = today.minusDays(13)
        val recentMinutes = goalLogs.filter { log ->
            val d = Dates.parseOrNull(log.dateIso) ?: return@filter false
            !d.isBefore(since) && !d.isAfter(today)
        }.sumOf { it.minutes }
        return GoalProgress(goal, components, today, recentWeeklyHours = recentMinutes / 60.0 / 2.0)
    }

    fun computeAll(goals: List<StudyGoal>, logs: List<StudyLog>, today: LocalDate, courses: Map<String, CourseState> = emptyMap()): Map<String, GoalProgress> =
        goals.associate { it.id to compute(it, logs, today, courses[it.id] ?: CourseState.NONE) }
}

/** Whole-person view: weekly capacity against the sum of weekly needs. */
data class CapacityReport(
    val weeklyCapacityHours: Double,
    val weeklyNeedHours: Double,
    val perGoalNeed: List<Pair<StudyGoal, Double>>,
) {
    val gapHours: Double get() = weeklyNeedHours - weeklyCapacityHours
    val ratio: Double get() = if (weeklyCapacityHours <= 0.0) Double.POSITIVE_INFINITY else weeklyNeedHours / weeklyCapacityHours
    val fits: Boolean get() = gapHours <= 0.0

    companion object {
        fun build(weeklyCapacityMinutes: Int, progress: Collection<GoalProgress>): CapacityReport {
            val active = progress.filter { it.goal.active && !it.isComplete }
            val perGoal = active.map { it.goal to (it.weeklyNeedHours ?: 0.0) }
            return CapacityReport(
                weeklyCapacityHours = weeklyCapacityMinutes / 60.0,
                weeklyNeedHours = perGoal.sumOf { it.second },
                perGoalNeed = perGoal.sortedByDescending { it.second },
            )
        }
    }
}
