package com.shahaabapps.twotracks.feature.today

import com.shahaabapps.core.model.TopicProgress
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.CourseProgress
import com.shahaabapps.core.content.WordDeck
import com.shahaabapps.twotracks.session.Celebrations
import android.content.Context
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.twotracks.changelog.ChangelogStore
import com.shahaabapps.twotracks.changelog.Release
import com.shahaabapps.core.data.tracking.TrackingRepository
import com.shahaabapps.core.model.Deadline
import com.shahaabapps.core.model.HabitLog
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.model.Profile
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.planner.Streaks
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.twotracks.reminders.SessionPlanning
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.PersonPlan
import com.shahaabapps.twotracks.session.PlanStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.session.StudyActions
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

data class SessionRow(
    val session: PlannedSession,
    val goal: StudyGoal?,
    val componentName: String,
    val timeLabel: String,
    /** The next unread topic of this session's course module, when a course exists. */
    val nextTopic: NextTopic? = null,
    /** Missed questions waiting in the course's review set, shown when the topics are all read or this is practice time. */
    val review: ReviewSet? = null,
    /**
     * True when the course records this session's time itself, so the plan
     * neither asks for minutes nor adds its own. A class is never covered:
     * it happens away from the app and only a person can say how long it ran.
     */
    val timeFromCourse: Boolean = false,
)

data class NextTopic(val courseId: String, val topicId: String, val title: String, val minutes: Int)
data class ReviewSet(val courseId: String, val count: Int)

/** One person's card on Today. */
data class PersonCard(
    val role: PersonRole,
    val name: String,
    val profile: Profile?,
    val sessions: List<SessionRow>,
    val todayMinutes: Int,
    val streak: Int,
    val localTime: String,
    val isRestDay: Boolean,
)

data class TodayUiState(
    val loading: Boolean = true,
    val today: LocalDate = LocalDate.now(),
    val level: LevelProgress? = null,
    val partnerLevel: LevelProgress? = null,
    val me: PersonCard? = null,
    val partner: PersonCard? = null,
    val status: Pair<String, Boolean> = "" to true,
    val goals: List<StudyGoal> = emptyList(),
    val activeGoals: List<StudyGoal> = emptyList(),
    val nextDeadline: Deadline? = null,
    val daysToDeadline: Long? = null,
    val message: String? = null,
    /** Days in a row both of them studied, and who still owes today. */
    val togetherStreak: Int = 0,
    val longestTogether: Int = 0,
    val togetherToday: Streaks.Together = Streaks.Together.NEITHER,
    /** Set once a day on open; null after dismissal. */
    val brief: DailyBrief? = null,
    val whatsNew: Release? = null,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TodayViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    private val dataStore: CoupleDataStore,
    private val planStore: PlanStore,
    private val sessionPlanning: SessionPlanning,
    private val actions: StudyActions,
    private val trackingRepository: TrackingRepository,
    private val changelog: ChangelogStore,
    @ApplicationContext context: Context,
    private val celebrations: Celebrations,
) : ViewModel() {

    private val message = MutableStateFlow<String?>(null)
    private val prefs = context.getSharedPreferences("daily_brief", Context.MODE_PRIVATE)
    private val briefDismissed = MutableStateFlow(false)

    private val sessionsFlow = sessionStore.session.flatMapLatest { s ->
        if (s == null) flowOf(emptyList()) else {
            val today = Clock.today(s.me)
            sessionPlanning.sessionsBetween(today.minusDays(1), today.plusDays(1))
        }
    }

    init {
        viewModelScope.launch {
            sessionStore.session.collect { s -> if (s != null) sessionPlanning.ensurePlanned(Clock.today(s.me)) }
        }
    }

    val uiState: StateFlow<TodayUiState> = combine(
        planStore.plan, sessionsFlow, dataStore.deadlines, message, combine(briefDismissed, changelog.unseen, dataStore.topicProgress) { d, w, tp -> Triple(d, w, tp) },
    ) { plan, sessions, deadlines, msg, (dismissed, whatsNew, topicProgress) ->
        if (plan == null) return@combine TodayUiState(loading = true, message = msg)
        val goalsById = plan.me.goals.plus(plan.partner.goals).associateBy { it.id }

        fun card(p: PersonPlan): PersonCard {
            val profile = plan.session.profileFor(p.role)
            val todayIso = Dates.format(p.today)
            val rows = sessions.filter { it.owner == p.role && it.dateIso == todayIso }.sortedBy { it.startMinutes }.map { s ->
                val g = goalsById[s.goalId]
                SessionRow(
                    session = s, goal = g,
                    componentName = g?.components?.firstOrNull { it.id == s.componentId }?.name.orEmpty(),
                    timeLabel = "${Dates.minutesToClock(s.startMinutes)} to ${Dates.minutesToClock(s.endMinutes)}",
                    nextTopic = nextTopicFor(p.role, s.goalId, s.componentId, topicProgress),
                    review = reviewFor(p.role, s.goalId, s.componentId, topicProgress),
                    timeFromCourse = !s.isClass && Courses.covers(s.goalId, s.componentId),
                )
            }
            return PersonCard(
                role = p.role, name = p.name, profile = profile, sessions = rows, todayMinutes = p.todayMinutes,
                streak = p.streak, localTime = Clock.formatTime(Clock.now(profile ?: plan.session.me)), isRestDay = rows.isEmpty(),
            )
        }

        val next = deadlines.mapNotNull { d -> Dates.parseOrNull(d.dateIso)?.let { d to it } }
            .filter { !it.second.isBefore(plan.me.today) }
            .minByOrNull { it.second }
        val myCard = card(plan.me)
        val todayIso = Dates.format(plan.me.today)
        val briefDue = !dismissed && prefs.getString("shown_on", null) != todayIso
        TodayUiState(
            loading = false, today = plan.me.today, level = plan.me.current, partnerLevel = plan.partner.current, me = myCard, partner = card(plan.partner),
            status = plan.me.statusSentence, goals = plan.me.goals, activeGoals = plan.me.activeGoals,
            nextDeadline = next?.first, daysToDeadline = next?.let { ChronoUnit.DAYS.between(plan.me.today, it.second) }, message = msg,
            togetherStreak = plan.togetherStreak, longestTogether = plan.longestTogether, togetherToday = plan.togetherToday,
            brief = if (briefDue) buildBrief(plan.me, myCard, next?.first, next?.let { ChronoUnit.DAYS.between(plan.me.today, it.second) }) else null,
            whatsNew = whatsNew,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TodayUiState())

    /**
     * One-tap Done. Where a course keeps the time — reading, tests and the deck
     * all log as you go — the session is only marked done; logging its planned
     * duration on top would count the same study twice and flatter the pace the
     * forecast reads. Everywhere else the planned minutes are still the record.
     */
    fun done(row: SessionRow) = viewModelScope.launch {
        val s = row.session
        message.value = if (row.timeFromCourse) {
            actions.markSessionDone(s.id) ?: "Session done. The course counts the time as you study."
        } else {
            actions.logMinutes(s.goalId, s.componentId, s.durationMinutes, s.dateIso, sessionId = s.id, fromClass = s.isClass)
                ?: "Logged ${s.durationMinutes} min of ${row.goal?.title ?: "study"}"
        }
    }

    fun logMinutes(goalId: String, componentId: String, minutes: Int, note: String, sessionId: String?, fromClass: Boolean) {
        viewModelScope.launch {
            message.value = actions.logMinutes(goalId, componentId, minutes, actions.todayIso(), note, sessionId, fromClass) ?: "Logged $minutes min"
        }
    }

    fun logSplit(goalId: String, minutesByComponent: Map<String, Int>, note: String, sessionId: String?) {
        viewModelScope.launch {
            var error: String? = null
            minutesByComponent.entries.forEachIndexed { index, (componentId, minutes) ->
                val sid = if (index == 0) sessionId else null
                error = error ?: actions.logMinutes(goalId, componentId, minutes, actions.todayIso(), note, sid, fromClass = true)
            }
            message.value = error ?: "Logged ${minutesByComponent.values.sum()} min across ${minutesByComponent.size} skills"
        }
    }

    fun skip(sessionId: String) = viewModelScope.launch { message.value = actions.skipSession(sessionId) }
    fun reopen(sessionId: String) = viewModelScope.launch { message.value = actions.reopenSession(sessionId) }

    fun addHabit(habitId: String, value: Int) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        trackingRepository.addHabitLog(s.coupleId, HabitLog(owner = s.myRole, habitId = habitId, dateIso = Clock.todayIso(s.me), value = value))
        celebrations.logged("Counted")
    }

    fun clearMessage() { message.value = null }

    fun dismissBrief() {
        prefs.edit().putString("shown_on", Dates.format(Clock.today(sessionStore.current()?.me))).apply()
        briefDismissed.value = true
    }

    fun dismissWhatsNew() = changelog.markSeen()

    /** Today's one thing and three reasons it matters: this goal, this level, the move. */
    /** The next unread topic in the module that feeds [componentId], or anywhere in the course if that module is done. */
    private fun nextTopicFor(role: PersonRole, goalId: String, componentId: String, progress: List<TopicProgress>): NextTopic? {
        val course = Courses.forGoal(goalId) ?: return null
        val learned = progress.filter { it.owner == role && it.courseId == course.id }.map { it.topicId }.toSet()
        val inModule = course.modules.filter { it.componentId == componentId }.flatMap { it.topics }.firstOrNull { it.id !in learned }
        val anywhere = inModule ?: course.modules.flatMap { it.topics }.firstOrNull { it.id !in learned } ?: return null
        return NextTopic(course.id, anywhere.id, anywhere.title.substringBefore(":"), anywhere.minutes)
    }

    /** The review set for the course behind [goalId]; offered during practice sessions or once every topic is read. */
    private fun reviewFor(role: PersonRole, goalId: String, componentId: String, progress: List<TopicProgress>): ReviewSet? {
        val course = Courses.forGoal(goalId) ?: return null
        val cp = CourseProgress.of(role, course.id, progress, dataStore.quizAttempts.value)
        val count = cp.reviewQuestions(course).size
        if (count == 0) return null
        val moduleRead = course.modules.filter { it.componentId == componentId }.let { ms -> ms.isNotEmpty() && ms.all { m -> m.topics.all { it.id in cp.learnedTopicIds } } }
        if (componentId != "practice" && !moduleRead && cp.nextTopic(course) != null) return null
        return ReviewSet(course.id, count)
    }

    /** Missed questions across courses the person is working on. */
    private fun reviewLine(p: PersonPlan): String? {
        val total = Courses.all.filter { c -> p.activeGoalIds.any { c.feeds(it) } }.sumOf { c -> CourseProgress.of(p.role, c.id, dataStore.topicProgress.value, dataStore.quizAttempts.value).reviewQuestions(c).size }
        if (total == 0) return null
        return "Review: $total missed question${if (total == 1) "" else "s"} waiting on the course page. Getting them right clears them."
    }

    /** Questions the spaced schedule has brought back round, across the courses in play. */
    private fun dailyLine(p: PersonPlan): String? {
        val total = Courses.all.filter { c -> p.activeGoalIds.any { c.feeds(it) } }
            .sumOf { c -> CourseProgress.of(p.role, c.id, dataStore.topicProgress.value, dataStore.quizAttempts.value).dailyDueCount(p.today) }
        if (total == 0) return null
        return "Daily review: $total question${if (total == 1) "" else "s"} back round on the Learn tab. Each one you hold comes back later than last time."
    }

    /** Flashcards due today across language courses the person has started. */
    private fun wordsLine(p: PersonPlan): String? {
        val cards = dataStore.flashcards.value
        val progress = dataStore.topicProgress.value
        val total = Courses.all.filter { Courses.words(it).isNotEmpty() }.sumOf { c -> WordDeck.dueCount(p.role, c, cards, p.today) + WordDeck.newCount(p.role, c, cards, progress) }
        if (total == 0) return null
        return "Words: $total cards waiting in the deck, about ${(total / 4).coerceAtLeast(2)} minutes. Missed words come back until they stick."
    }

    /** The next unread topic of the course that feeds the first active goal, if any. */
    private fun learnLine(p: PersonPlan): String? {
        val course = Courses.all.firstOrNull { c -> p.activeGoalIds.any { c.feeds(it) } } ?: return null
        val progress = CourseProgress.of(p.role, course.id, dataStore.topicProgress.value, dataStore.quizAttempts.value)
        val (module, topic) = progress.nextTopic(course) ?: return "${course.title}: every topic read. Retake a module test to keep it sharp."
        return "Learn tab: ${topic.title.substringBefore(":")}, about ${topic.minutes} minutes, counts toward ${module.title}."
    }

    private fun buildBrief(p: PersonPlan, card: PersonCard, deadline: Deadline?, deadlineDays: Long?): DailyBrief {
        val hour = Clock.now(sessionStore.current()?.me).hour
        val greeting = when {
            hour < 12 -> "Good morning, ${p.name}"
            hour < 18 -> "Good afternoon, ${p.name}"
            else -> "Good evening, ${p.name}"
        }
        val first = card.sessions.firstOrNull { it.session.status == SessionStatus.PLANNED } ?: card.sessions.firstOrNull()
        val goal = first?.goal
        val gp = goal?.let { p.goalProgress[it.id] }
        val level = p.current
        val reasons = mutableListOf<Pair<ImageVector, String>>()
        if (goal != null && gp != null) {
            val due = gp.targetDate?.let { Clock.formatDate(it) }
            val need = gp.weeklyNeedHours
            reasons += BriefIcons.goal to buildString {
                append("${goal.title}: %.0f of %.0f hours done".format(gp.hoursLogged, gp.hoursRequired))
                if (due != null && need != null) append(". About %.0f hours a week keeps it on track for $due".format(need))
                append(".")
            }
        } else if (p.activeGoals.isNotEmpty()) {
            val g = p.activeGoals.first()
            val gp2 = p.goalProgress[g.id]
            reasons += BriefIcons.goal to "${g.title}: %.0f of %.0f hours done. A rest day keeps the streak honest; logging anything counts.".format(gp2?.hoursLogged ?: 0.0, gp2?.hoursRequired ?: 0.0)
        }
        if (level != null) {
            val left = level.daysLeft?.let { if (it >= 0) "$it days left" else "${-it} days overdue" } ?: ""
            reasons += BriefIcons.level to "Level ${level.level.number}, ${level.level.name}: ${level.requiredDone} of ${level.requiredTotal} done, $left. Finishing it unlocks Level ${level.level.number + 1} on time."
        }
        learnLine(p)?.let { reasons += BriefIcons.learn to it }
        reviewLine(p)?.let { reasons += BriefIcons.learn to it }
        dailyLine(p)?.let { reasons += BriefIcons.learn to it }
        wordsLine(p)?.let { reasons += BriefIcons.learn to it }
        reasons += BriefIcons.move to when {
            deadline != null && deadlineDays != null -> "${deadline.title} in $deadlineDays days. Every session now is one less to squeeze in later."
            else -> "Every session now is one less to squeeze in later."
        }
        val streak = when (p.streak) {
            0 -> "No streak yet. Today starts one."
            1 -> "1-day streak. Make it two."
            else -> "${p.streak}-day streak. Keep it alive."
        }
        return DailyBrief(
            greeting = greeting,
            dateLine = Clock.formatLong(p.today),
            task = if (first == null) "Rest day" else (goal?.title ?: "Study"),
            taskDetail = if (first == null) "No slot today. Log anything you study." else "${first.componentName} · ${first.session.durationMinutes} min · ${first.timeLabel}",
            reasons = reasons,
            streakLine = streak,
            isRestDay = first == null,
        )
    }
}
