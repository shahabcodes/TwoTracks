package com.shahaabapps.twotracks.feature.today

import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.model.Deadline
import com.shahaabapps.core.model.Flashcard
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.model.QuizAttempt
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.model.TopicProgress
import com.shahaabapps.core.planner.CapacityReport
import com.shahaabapps.twotracks.session.PersonPlan
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * The brief is the first thing the app says each day, and until now it was the
 * one derivation nothing could check: six private methods on a view model,
 * reading the repository's current values out of a field.
 *
 * These are the things it would get wrong quietly — a line offered when there
 * is nothing behind it, a plural, a rest day that reads as a missed one.
 */
class DailyBriefsTest {

    private val today = LocalDate.of(2026, 3, 9)
    private val german = Courses.all.first { it.id == "de-a1" }

    private fun plan(
        activeGoalIds: Set<String> = emptySet(),
        streak: Int = 0,
        name: String = "Usaid",
    ) = PersonPlan(
        role = PersonRole.TECH,
        name = name,
        today = today,
        levels = emptyList(),
        goals = emptyList(),
        goalProgress = emptyMap(),
        activeGoalIds = activeGoalIds,
        capacity = CapacityReport(0.0, 0.0, emptyList()),
        streak = streak,
        longestStreak = streak,
        todayMinutes = 0,
        weekCounts = emptyMap(),
    )

    private fun card(sessions: List<SessionRow> = emptyList()) = PersonCard(
        role = PersonRole.TECH, name = "Usaid", profile = null, sessions = sessions,
        todayMinutes = 0, streak = 0, localTime = "09:00", isRestDay = sessions.isEmpty(),
    )

    private fun row(status: SessionStatus, goal: StudyGoal? = null, minutes: Int = 60) = SessionRow(
        session = PlannedSession(
            id = "s", owner = PersonRole.TECH, dateIso = "2026-03-09", blockId = "b",
            startMinutes = 540, endMinutes = 540 + minutes, goalId = goal?.id.orEmpty(),
            componentId = "grammar", status = status,
        ),
        goal = goal,
        componentName = "Grammar",
        timeLabel = "09:00",
    )

    /** The goal id the German course feeds, so a plan can be made to care about it. */
    private val germanGoalId = Courses.all.first { it.id == "de-a1" }.goalId

    // ---- the lines -------------------------------------------------------

    @Test
    fun `a line is absent when there is nothing behind it`() {
        // The brief used to be able to say "Review: 0 missed questions".
        val p = plan(activeGoalIds = setOf(germanGoalId))
        assertNull(DailyBriefs.reviewLine(p, emptyList(), emptyList()))
        assertNull(DailyBriefs.dailyLine(p, emptyList(), emptyList()))
        assertNull(DailyBriefs.wordsLine(p, emptyList(), emptyList()))
    }

    @Test
    fun `no active goal means no learn line at all`() {
        val p = plan(activeGoalIds = emptySet())
        assertNull(DailyBriefs.learnLine(p, emptyList(), emptyList()))
        assertNull(DailyBriefs.reviewLine(p, emptyList(), emptyList()))
    }

    @Test
    fun `the learn line names the next unread topic and the module it counts toward`() {
        val p = plan(activeGoalIds = setOf(germanGoalId))
        val line = DailyBriefs.learnLine(p, emptyList(), emptyList())!!
        val first = german.modules.first().topics.first()
        assertTrue(line, line.startsWith("Learn tab: "))
        assertTrue(line, line.contains(first.title.substringBefore(":")))
        assertTrue(line, line.contains("${first.minutes} minutes"))
        assertTrue(line, line.contains(german.modules.first().title))
    }

    @Test
    fun `a course that is fully read says so rather than going quiet`() {
        val p = plan(activeGoalIds = setOf(germanGoalId))
        val everything = german.allTopics.map {
            TopicProgress(id = it.id, owner = PersonRole.TECH, courseId = german.id, topicId = it.id, learnedAtIso = "2026-03-08")
        }
        val line = DailyBriefs.learnLine(p, everything, emptyList())!!
        assertTrue(line, line.contains("every topic read"))
    }

    @Test
    fun `one missed question is singular and two are not`() {
        val p = plan(activeGoalIds = setOf(germanGoalId))
        val q = german.modules.first().quiz!!.questions
        fun attempt(missed: List<String>) = listOf(
            QuizAttempt(
                id = "a", owner = PersonRole.TECH, courseId = german.id, moduleId = german.modules.first().id,
                dateIso = "2026-03-08", score = 1, total = q.size, passed = false, missed = missed,
            ),
        )
        val one = DailyBriefs.reviewLine(p, emptyList(), attempt(listOf(q[0].id)))!!
        val two = DailyBriefs.reviewLine(p, emptyList(), attempt(listOf(q[0].id, q[1].id)))!!
        assertTrue(one, one.contains("1 missed question "))
        assertTrue(two, two.contains("2 missed questions "))
    }

    @Test
    fun `the words line never promises less than two minutes`() {
        // Four cards a minute, so three cards would round to nought and read
        // as a bug on a screen that is asking for your attention.
        val p = plan(activeGoalIds = setOf(germanGoalId))
        val wordId = Courses.words(german).first().id
        val cards = listOf(
            Flashcard(id = "f1", owner = PersonRole.TECH, courseId = german.id, wordId = wordId, dueIso = "2026-03-01"),
        )
        val line = DailyBriefs.wordsLine(p, emptyList(), cards)!!
        assertTrue(line, line.contains("about 2 minutes"))
    }

    @Test
    fun `the greeting follows the hour on that person's clock`() {
        assertEquals("Good morning, Sanya", DailyBriefs.greeting("Sanya", 0))
        assertEquals("Good morning, Sanya", DailyBriefs.greeting("Sanya", 11))
        assertEquals("Good afternoon, Sanya", DailyBriefs.greeting("Sanya", 12))
        assertEquals("Good afternoon, Sanya", DailyBriefs.greeting("Sanya", 17))
        assertEquals("Good evening, Sanya", DailyBriefs.greeting("Sanya", 18))
        assertEquals("Good evening, Sanya", DailyBriefs.greeting("Sanya", 23))
    }

    @Test
    fun `the streak line reads for nought, one and more`() {
        assertEquals("No streak yet. Today starts one.", DailyBriefs.streakLine(0))
        assertEquals("1-day streak. Make it two.", DailyBriefs.streakLine(1))
        assertEquals("2-day streak. Keep it alive.", DailyBriefs.streakLine(2))
    }

    // ---- the whole brief -------------------------------------------------

    private fun build(
        plan: PersonPlan,
        card: PersonCard,
        deadline: Deadline? = null,
        deadlineDays: Long? = null,
        progress: List<TopicProgress> = emptyList(),
        attempts: List<QuizAttempt> = emptyList(),
        cards: List<Flashcard> = emptyList(),
    ) = DailyBriefs.build(
        plan = plan, card = card, hour = 9, dateLine = "Monday, 9 March",
        deadline = deadline, deadlineDays = deadlineDays,
        progress = progress, attempts = attempts, cards = cards,
        targetDateLabel = { "9 Mar 2026" },
    )

    @Test
    fun `a day with no session is a rest day, and says what still counts`() {
        val brief = build(plan(), card(emptyList()))
        assertTrue(brief.isRestDay)
        assertEquals("Rest day", brief.task)
        assertTrue(brief.taskDetail, brief.taskDetail.contains("Log anything you study"))
    }

    @Test
    fun `a planned session is preferred over one already done`() {
        // The card lists the day in order, so a morning session that is
        // finished must not become the headline for the afternoon.
        val goal = StudyGoal(id = "g", owner = PersonRole.TECH, title = "German A1")
        val brief = build(plan(), card(listOf(row(SessionStatus.DONE), row(SessionStatus.PLANNED, goal, minutes = 45))))
        assertFalse(brief.isRestDay)
        assertEquals("German A1", brief.task)
        assertTrue(brief.taskDetail, brief.taskDetail.contains("45 min"))
    }

    @Test
    fun `a day whose only session is finished still shows it rather than claiming a rest day`() {
        val brief = build(plan(), card(listOf(row(SessionStatus.DONE))))
        assertFalse(brief.isRestDay)
        assertEquals("Study", brief.task)
    }

    @Test
    fun `the last reason is always the move, and it names a deadline when there is one`() {
        val plain = build(plan(), card(emptyList()))
        assertEquals(BriefKind.MOVE, plain.reasons.last().kind)
        assertTrue(plain.reasons.last().text, plain.reasons.last().text.startsWith("Every session now"))

        val dated = build(
            plan(), card(emptyList()),
            deadline = Deadline(id = "d", title = "Book the test", dateIso = "2026-04-01"),
            deadlineDays = 23,
        )
        assertTrue(dated.reasons.last().text, dated.reasons.last().text.startsWith("Book the test in 23 days."))
    }

    @Test
    fun `the learn lines appear in a fixed order under the goal and the level`() {
        val p = plan(activeGoalIds = setOf(germanGoalId))
        val wordId = Courses.words(german).first().id
        val brief = build(
            p, card(emptyList()),
            cards = listOf(Flashcard(id = "f", owner = PersonRole.TECH, courseId = german.id, wordId = wordId, dueIso = "2026-03-01")),
        )
        val learn = brief.reasons.filter { it.kind == BriefKind.LEARN }
        assertEquals(2, learn.size)
        assertTrue(learn[0].text, learn[0].text.startsWith("Learn tab:"))
        assertTrue(learn[1].text, learn[1].text.startsWith("Words:"))
    }

    @Test
    fun `an empty plan still produces a brief rather than nothing`() {
        // A space on its first morning has no goals, no levels and no
        // sessions, and the sheet is shown all the same.
        val brief = build(plan(), card(emptyList()))
        assertEquals("Good morning, Usaid", brief.greeting)
        assertEquals("Monday, 9 March", brief.dateLine)
        assertTrue(brief.reasons.isNotEmpty())
        assertEquals("No streak yet. Today starts one.", brief.streakLine)
    }
}
