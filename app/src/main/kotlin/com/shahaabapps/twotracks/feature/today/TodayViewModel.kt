package com.shahaabapps.twotracks.feature.today

import com.shahaabapps.core.model.TopicProgress
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.CourseProgress
import com.shahaabapps.twotracks.session.Celebrations
import android.content.Context
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
    /**
     * When it starts on the reader's clock, in minutes of the day. Today puts
     * both days on one line, so the partner's sessions are moved into the
     * reader's time zone; [timeLabel] keeps the owner's own clock.
     */
    val readerStartMinutes: Int = session.startMinutes,
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

private data class BriefInputs(val dismissed: Boolean, val requested: Boolean, val whatsNew: Release?, val progress: List<TopicProgress>)

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
    /** Set when the date on Today is tapped: the brief again, whether or not it was seen this morning. */
    private val briefRequested = MutableStateFlow(false)

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
        planStore.plan, sessionsFlow, dataStore.deadlines, message, combine(briefDismissed, briefRequested, changelog.unseen, dataStore.topicProgress) { d, r, w, tp -> BriefInputs(d, r, w, tp) },
    ) { plan, sessions, deadlines, msg, (dismissed, requested, whatsNew, topicProgress) ->
        if (plan == null) return@combine TodayUiState(loading = true, message = msg)
        val goalsById = plan.me.goals.plus(plan.partner.goals).associateBy { it.id }

        fun card(p: PersonPlan): PersonCard {
            val profile = plan.session.profileFor(p.role)
            val ownerZone = Clock.zone(profile ?: plan.session.me)
            val readerZone = Clock.zone(plan.session.me)
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
                    readerStartMinutes = Clock.convert(p.today, s.startMinutes, ownerZone, readerZone).let { it.hour * 60 + it.minute },
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
        val briefDue = requested || (!dismissed && prefs.getString("shown_on", null) != todayIso)
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
     * One-tap Done, which marks the slot and nothing else.
     *
     * It used to log the slot's planned duration where no course kept the
     * time, which made a session marked done worth an hour whether or not an
     * hour had happened. Minutes now come from the courses alone — reading, a
     * test, the deck — so Done says the slot was dealt with and the hours say
     * what was actually studied.
     */
    fun done(row: SessionRow) = viewModelScope.launch {
        message.value = actions.markSessionDone(row.session.id)
            ?: "Session done. The course counts the time as you study."
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
        briefRequested.value = false
    }

    /** The brief again, from the date at the top of Today. */
    fun openBrief() { briefRequested.value = true }

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

    private fun buildBrief(p: PersonPlan, card: PersonCard, deadline: Deadline?, deadlineDays: Long?): DailyBrief =
        DailyBriefs.build(
            plan = p,
            card = card,
            hour = Clock.now(sessionStore.current()?.me).hour,
            dateLine = Clock.formatLong(p.today),
            deadline = deadline,
            deadlineDays = deadlineDays,
            progress = dataStore.topicProgress.value,
            attempts = dataStore.quizAttempts.value,
            cards = dataStore.flashcards.value,
            targetDateLabel = Clock::formatDate,
        )
}
