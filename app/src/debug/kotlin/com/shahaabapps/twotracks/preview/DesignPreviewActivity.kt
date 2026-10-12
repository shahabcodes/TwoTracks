package com.shahaabapps.twotracks.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.core.model.Level
import com.shahaabapps.core.model.LevelItem
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.model.Profile
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.CapacityReport
import com.shahaabapps.core.planner.LevelItemProgress
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.core.planner.LevelState
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.ThemeMode
import com.shahaabapps.twotracks.feature.today.NextTopic
import com.shahaabapps.twotracks.feature.today.PersonCard
import com.shahaabapps.twotracks.feature.today.SessionRow
import com.shahaabapps.twotracks.feature.today.TodayContent
import com.shahaabapps.twotracks.feature.today.TodayUiState
import com.shahaabapps.twotracks.feature.week.BlockRow
import com.shahaabapps.twotracks.feature.week.BothWeeksContent
import com.shahaabapps.twotracks.feature.week.BothWeeksUiState
import com.shahaabapps.twotracks.feature.week.DayPlan
import com.shahaabapps.twotracks.feature.week.PersonWeek
import com.shahaabapps.twotracks.feature.week.WeekContent
import com.shahaabapps.twotracks.feature.week.WeekUiState
import java.time.LocalDate

/**
 * Debug builds only: the redesigned screens drawn from the prototype's sample
 * data, with no account and no Firestore, so they can be put next to the
 * prototype and compared.
 *
 *   adb shell am start -n com.shahaabapps.twotracks/.preview.DesignPreviewActivity --es screen today [--ez dark true]
 *
 * Screens: today, week, both.
 */
class DesignPreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val screen = intent?.getStringExtra("screen") ?: "today"
        val dark = intent?.getBooleanExtra("dark", false) == true
        setContent {
            AppTheme(themeMode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    when (screen) {
                        "week" -> WeekContent(
                            state = Samples.week(), onBack = {}, onOpenGoal = {}, onOpenBothWeeks = {}, onOpenFit = {},
                            onPreviousWeek = {}, onNextWeek = {}, onThisWeek = {}, onTogglePerson = {}, onReplan = {},
                            onAssign = { _, _, _ -> }, onAddBlock = { _, _, _ -> }, onMessageShown = {},
                        )
                        "both" -> BothWeeksContent(state = Samples.both(), onBack = {})
                        else -> TodayContent(
                            state = Samples.today(), onOpenLevel = {}, onOpenPartner = {}, onOpenGoal = {}, onOpenWeek = {},
                            onOpenSettings = {}, onOpenChangelog = {}, onOpenTopic = { _, _ -> }, onOpenQuiz = { _, _ -> },
                            onOpenBrief = {}, onDismissBrief = {}, onDismissWhatsNew = {}, onDone = {}, onSkip = {}, onReopen = {}, onMessageShown = {},
                        )
                    }
                }
            }
        }
    }
}

/** The prototype's Monday, 12 October 2026, for Usaid in Doha and Sanya in Karachi. */
private object Samples {
    val today: LocalDate = LocalDate.of(2026, 10, 12)
    private val usaid = Profile(uid = "u", name = "Usaid", role = PersonRole.TECH, timezone = "Asia/Qatar")
    private val sanya = Profile(uid = "s", name = "Sanya", role = PersonRole.TEACHER, timezone = "Asia/Karachi")
    private val az = StudyGoal(id = "az", owner = PersonRole.TECH, title = "AZ-204")
    private val deU = StudyGoal(id = "de-u", owner = PersonRole.TECH, title = "German A1")
    private val ielts = StudyGoal(id = "ielts", owner = PersonRole.TEACHER, title = "IELTS")
    private val deS = StudyGoal(id = "de-s", owner = PersonRole.TEACHER, title = "German A1")

    private fun row(id: String, owner: PersonRole, goal: StudyGoal, component: String, start: Int, minutes: Int, status: SessionStatus = SessionStatus.PLANNED, reader: Int = start, isClass: Boolean = false, topic: NextTopic? = null) =
        SessionRow(
            session = PlannedSession(id = id, owner = owner, dateIso = today.toString(), startMinutes = start, endMinutes = start + minutes, status = status, isClass = isClass),
            goal = goal, componentName = component,
            timeLabel = "%02d:%02d to %02d:%02d".format(start / 60, start % 60, (start + minutes) / 60, (start + minutes) % 60),
            nextTopic = topic, readerStartMinutes = reader,
        )

    private fun level(name: String, done: Int, total: Int, due: String) = LevelProgress(
        level = Level(id = name, number = 1, name = name, dueDateIso = due),
        state = LevelState.OPEN,
        items = (0 until total).map { LevelItemProgress(LevelItem(id = "i$it", required = true), done = it < done, fraction = null, weekCount = null) },
        today = today,
    )

    fun today() = TodayUiState(
        loading = false,
        today = today,
        level = level("Foundations", 2, 5, "2027-03-31"),
        partnerLevel = level("Build the base", 1, 3, "2026-12-31"),
        me = PersonCard(
            role = PersonRole.TECH, name = "Usaid", profile = usaid, todayMinutes = 20, streak = 6, localTime = "09:12", isRestDay = false,
            sessions = listOf(
                row("u1", PersonRole.TECH, deU, "Words", 7 * 60, 20, SessionStatus.DONE),
                row("u2", PersonRole.TECH, az, "Compute", 19 * 60, 60, topic = NextTopic("az204", "app-service", "App Service", 28)),
            ),
        ),
        partner = PersonCard(
            role = PersonRole.TEACHER, name = "Sanya", profile = sanya, todayMinutes = 0, streak = 6, localTime = "11:12", isRestDay = false,
            sessions = listOf(
                row("s1", PersonRole.TEACHER, ielts, "Listening class", 16 * 60, 90, reader = 14 * 60, isClass = true),
                row("s2", PersonRole.TEACHER, deS, "Grammar", 23 * 60 + 30, 30, reader = 21 * 60 + 30),
            ),
        ),
        togetherStreak = 6,
    )

    fun week(): WeekUiState {
        val start = today
        fun block(day: Int, from: Int, to: Int, kind: BlockKind = BlockKind.STUDY) = AvailabilityBlock(id = "$day-$from", dayOfWeek = day, startMinutes = from, endMinutes = to, kind = kind)
        fun study(day: Int, from: Int, minutes: Int, goal: StudyGoal, component: String, status: SessionStatus = SessionStatus.PLANNED) =
            BlockRow(block(day, from, from + minutes), PlannedSession(id = "$day-$from", startMinutes = from, endMinutes = from + minutes, status = status), goal, component)
        val work = BlockRow(block(0, 8 * 60, 17 * 60, BlockKind.WORK), null, null, "")
        val days = listOf(
            listOf(study(1, 7 * 60, 20, deU, "Words", SessionStatus.DONE), work, study(1, 19 * 60, 60, az, "App Service")),
            listOf(work, study(2, 19 * 60, 60, az, "Deployment slots")),
            listOf(study(3, 7 * 60, 30, deU, "Grammar"), work, study(3, 19 * 60, 60, az, "Functions")),
            listOf(work, study(4, 19 * 60, 90, az, "Containers")),
            emptyList(),
            listOf(study(6, 10 * 60, 120, az, "Module 1 test"), study(6, 16 * 60, 45, deU, "Dialogue")),
            listOf(study(7, 7 * 60, 20, deU, "Words"), work, study(7, 19 * 60, 60, az, "Blob storage")),
        ).mapIndexed { i, blocks -> DayPlan(date = start.plusDays(i.toLong()), isToday = i == 0, blocks = blocks) }
        return WeekUiState(
            weekStart = start, days = days, viewingRole = PersonRole.TECH, viewingName = "Usaid", isMe = true,
            capacity = CapacityReport(weeklyCapacityHours = 9.4, weeklyNeedHours = 8.0, perGoalNeed = emptyList()),
            plannedMinutes = 564, doneMinutes = 20,
        )
    }

    fun both() = BothWeeksUiState(
        days = (6 downTo 0).map { today.minusDays(it.toLong()) },
        me = PersonWeek(PersonRole.TECH, "Usaid", 505, "AZ-204 5.5 · German A1 2.9", listOf(75, 60, 90, 30, 150, 80, 20)),
        partner = PersonWeek(PersonRole.TEACHER, "Sanya", 415, "IELTS 5.4 · German A1 1.5", listOf(120, 45, 120, 40, 60, 30, 0)),
        togetherStreak = 6,
        partnerName = "Sanya",
    )
}
