package com.shahaabapps.twotracks.preview

import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyLog
import com.shahaabapps.core.planner.FitPlanner
import com.shahaabapps.core.planner.LevelEngine
import com.shahaabapps.core.planner.ProgressCalculator
import com.shahaabapps.core.seed.Seed
import com.shahaabapps.twotracks.feature.levels.Ladder
import com.shahaabapps.twotracks.feature.levels.LevelUiState
import com.shahaabapps.twotracks.feature.levels.LevelsUiState
import com.shahaabapps.twotracks.feature.study.ComponentDraft
import com.shahaabapps.twotracks.feature.study.FitCheck
import com.shahaabapps.twotracks.feature.study.GoalDetailUiState
import com.shahaabapps.twotracks.feature.study.GoalEditUiState
import java.time.LocalDate

/**
 * The prototype's plan, from the real seed: Usaid twelve days into Level 1
 * with the degree check and the CV done and twelve hours of AZ-204 logged.
 */
internal object PlanSamples {
    private val today: LocalDate = Samples.today
    private const val base = Seed.ANCHOR_ISO
    private val goals = Seed.goals(base)
    private val levels = Seed.levels(base).map { l ->
        if (l.id != "tech-1") l else l.copy(items = l.items.map { if (it.id == "t1-anabin" || it.id == "t1-cv") it.copy(done = true) else it })
    }
    private val az = goals.first { it.id == "az-204" }
    private val logs = az.components.take(3).mapIndexed { i, c ->
        StudyLog(id = "l$i", owner = PersonRole.TECH, dateIso = today.minusDays(i.toLong() + 1).toString(), goalId = az.id, componentId = c.id, minutes = listOf(360, 240, 120)[i])
    }
    private val progress = ProgressCalculator.computeAll(goals, logs, today)

    private fun ladder(role: PersonRole, name: String) =
        Ladder(name, role == PersonRole.TECH, LevelEngine.progress(levels, goals.filter { it.owner == role }, progress, emptyMap(), today, role))

    fun levels() = LevelsUiState(me = ladder(PersonRole.TECH, "Usaid"), partner = ladder(PersonRole.TEACHER, "Sanya"))

    fun level(): LevelUiState {
        val ladder = ladder(PersonRole.TECH, "Usaid").levels
        return LevelUiState(
            progress = ladder.first(), isMine = true, ownerName = "Usaid",
            goalsById = goals.filter { it.owner == PersonRole.TECH }.associateBy { it.id },
            next = ladder.getOrNull(1), isTech = true,
            goalHours = progress.mapValues { (_, g) -> Math.round(g.hoursLogged).toInt() to Math.round(g.hoursRequired).toInt() },
        )
    }

    fun goal() = GoalDetailUiState(
        goal = az, progress = progress.getValue(az.id), isMine = true,
        recentLogs = logs, sentence = "On track: the last two weeks averaged 5.1 h", sentenceGood = true, hoursCounted = true,
    )

    fun goalEdit() = GoalEditUiState(
        isNew = false, loaded = true, title = "AZ-204", why = az.why.ifBlank { "Azure Developer Associate for the CV." }, kind = az.kind,
        priority = "${az.priority}", targetDateIso = az.targetDateIso.orEmpty(), canDelete = true,
        components = az.components.map { ComponentDraft(it.id, it.name, "%.0f".format(it.hoursRequired), it.targetScore.orEmpty()) },
    )

    fun fit(): FitCheck {
        val blocks = listOf(1, 3, 6).map { d -> AvailabilityBlock(id = "b$d", dayOfWeek = d, startMinutes = 19 * 60, endMinutes = 20 * 60 + 30, kind = BlockKind.STUDY) }
        return FitCheck(
            result = FitPlanner.FitResult(
                fits = false, demandHours = 128.0, weeksLeft = 24.0, weeklyCapacityHours = 4.67, neededWeeklyHours = 5.3,
                extend = FitPlanner.ExtendOption(LocalDate.of(2027, 4, 28), 28),
                moreHours = FitPlanner.MoreHoursOption(30, blocks, tooLong = false),
            ),
            levelId = "tech-1", levelNumber = 1, levelName = "Foundations", levelDueIso = "2027-03-31", addedHours = 20.0,
        )
    }
}
