package com.shahaabapps.twotracks.session

import com.shahaabapps.core.data.study.forRole
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.CapacityReport
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.planner.Feasibility
import com.shahaabapps.core.planner.GoalProgress
import com.shahaabapps.core.planner.LevelEngine
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.core.planner.ProgressCalculator
import com.shahaabapps.core.planner.Streaks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/** Everything derived for one person: levels, goal progress, what the planner should schedule, and plain-English status. */
data class PersonPlan(
    val role: PersonRole,
    val name: String,
    val today: LocalDate,
    val levels: List<LevelProgress>,
    val goals: List<StudyGoal>,
    val goalProgress: Map<String, GoalProgress>,
    val activeGoalIds: Set<String>,
    val capacity: CapacityReport,
    val streak: Int,
    val longestStreak: Int,
    val todayMinutes: Int,
    val weekCounts: Map<String, Int>,
) {
    val current: LevelProgress? get() = LevelEngine.current(levels)
    val next: LevelProgress? get() = LevelEngine.next(levels)
    val activeGoals: List<StudyGoal> get() = goals.filter { it.id in activeGoalIds }.sortedBy { it.priority }

    /** The one sentence Today shows, and whether it is good news. */
    val statusSentence: Pair<String, Boolean>
        get() {
            val active = activeGoalIds.mapNotNull { goalProgress[it] }.filter { it.weeklyNeedHours != null }
            if (active.isEmpty()) return "Nothing due right now. Enjoy the breather." to true
            val worst = active.minByOrNull { p ->
                when (p.feasibility) { Feasibility.DOES_NOT_FIT -> 0; Feasibility.TIGHT -> 1; else -> 2 }
            } ?: return "On track." to true
            val title = worst.goal.title
            val due = worst.targetDate?.let { Clock.formatDate(it) } ?: "its date"
            return when (worst.feasibility) {
                Feasibility.FITS -> "On track for $title by $due." to true
                Feasibility.TIGHT -> "Nearly on track for $title. A little extra each week keeps $due." to true
                Feasibility.DOES_NOT_FIT -> {
                    val forecast = worst.forecastDate?.let { Clock.formatDate(it) }
                    val extra = worst.weeklyNeedHours?.let { need -> (need - worst.recentWeeklyHours).coerceAtLeast(0.5) }
                    if (worst.hoursLogged < 3.0) {
                        val need = worst.weeklyNeedHours?.let { "About %.0f hours a week gets $title done by $due.".format(it) } ?: ""
                        "Early days for $title. $need".trim() to true
                    } else if (forecast != null) {
                        val extraText = extra?.let { "About %.0f extra hour%s a week gets you back to $due.".format(it, if (it >= 1.5) "s" else "") } ?: ""
                        "At this pace $title finishes $forecast, not $due. $extraText".trim() to false
                    } else {
                        val need = worst.weeklyNeedHours?.let { "About %.0f hours a week gets $title done by $due.".format(it) } ?: ""
                        "Nothing logged yet for $title. $need".trim() to false
                    }
                }
                else -> "On track." to true
            }
        }
}

data class PlanSnapshot(
    val session: AppSession,
    val me: PersonPlan,
    val partner: PersonPlan,
) {
    fun forRole(role: PersonRole): PersonPlan = if (role == me.role) me else partner
}

/** One shared, derived view of the plan for both people, computed from the live collections. */
@Singleton
class PlanStore @Inject constructor(
    sessionStore: SessionStore,
    dataStore: CoupleDataStore,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val plan: StateFlow<PlanSnapshot?> = combine(
        sessionStore.session, dataStore.levels, dataStore.goals, dataStore.logs,
        combine(dataStore.habitLogs, dataStore.availability) { h, a -> h to a },
    ) { session, levels, goals, logs, (habitLogs, availability) ->
        if (session == null) return@combine null
        fun person(role: PersonRole): PersonPlan {
            val profile = session.profileFor(role)
            val today = Clock.today(profile ?: session.me)
            val todayIso = Dates.format(today)
            val weekIso = Dates.format(Dates.weekStart(today))
            val roleGoals = goals.filter { it.owner == role }
            val progress = ProgressCalculator.computeAll(roleGoals, logs, today)
            val weekCounts = habitLogs.filter { it.owner == role && it.dateIso >= weekIso }.groupBy { it.habitId }.mapValues { e -> e.value.sumOf { it.value } }
            val levelProgress = LevelEngine.progress(levels, roleGoals, progress, weekCounts, today, role)
            val activeIds = LevelEngine.activeGoalIds(levelProgress, roleGoals, progress)
            val roleLogs = logs.filter { it.owner == role }
            return PersonPlan(
                role = role, name = session.nameFor(role), today = today, levels = levelProgress, goals = roleGoals,
                goalProgress = progress, activeGoalIds = activeIds,
                capacity = CapacityReport.build(availability.forRole(role).weeklyStudyMinutes, progress.filterKeys { it in activeIds }.values),
                streak = Streaks.current(roleLogs.map { it.dateIso }, today),
                longestStreak = Streaks.longest(roleLogs.map { it.dateIso }),
                todayMinutes = roleLogs.filter { it.dateIso == todayIso }.sumOf { it.minutes },
                weekCounts = weekCounts,
            )
        }
        PlanSnapshot(session = session, me = person(session.myRole), partner = person(session.partnerRole))
    }.stateIn(scope, SharingStarted.WhileSubscribed(10_000), null)
}
