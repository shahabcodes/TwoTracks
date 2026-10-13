package com.shahaabapps.twotracks.feature.today

import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.content.CourseProgress
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.WordDeck
import com.shahaabapps.core.model.Deadline
import com.shahaabapps.core.model.Flashcard
import com.shahaabapps.core.model.QuizAttempt
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.model.TopicProgress
import com.shahaabapps.twotracks.session.PersonPlan
import java.time.LocalDate

/** Which of the four icons a brief line carries. The sheet owns the icons. */
enum class BriefKind { GOAL, LEVEL, LEARN, MOVE }

/** One line of the brief: why today matters. */
data class BriefReason(val kind: BriefKind, val text: String)

/**
 * The sentence the app opens with, and the three or four reasons under it.
 *
 * This used to be six private methods on TodayViewModel that read the
 * repository's current values out of a field. That made the one piece of text
 * everybody sees first the only derivation in the app that nothing could
 * check: the plural of "1 missed question", whether a rest day says the right
 * thing, whether a review line appears when there is nothing to review. It is
 * all arithmetic over records the course layer already folds, so it belongs
 * here, taking what it needs as arguments.
 *
 * The icons stayed behind in the sheet. A brief line names its kind and the
 * sheet resolves that to an ImageVector, which is what keeps this file free of
 * Compose and therefore testable.
 */
object DailyBriefs {

    /** Missed questions across the courses this person is working on. */
    fun reviewLine(plan: PersonPlan, progress: List<TopicProgress>, attempts: List<QuizAttempt>): String? {
        val total = coursesInPlay(plan).sumOf { c ->
            CourseProgress.of(plan.role, c.id, progress, attempts).reviewQuestions(c).size
        }
        if (total == 0) return null
        return "Review: $total missed question${plural(total)} waiting on the course page. Getting them right clears them."
    }

    /** Questions the spaced schedule has brought back round. */
    fun dailyLine(plan: PersonPlan, progress: List<TopicProgress>, attempts: List<QuizAttempt>): String? {
        val total = coursesInPlay(plan).sumOf { c ->
            CourseProgress.of(plan.role, c.id, progress, attempts).dailyDueCount(plan.today)
        }
        if (total == 0) return null
        return "Daily review: $total question${plural(total)} back round on the Learn tab. " +
            "Each one you hold comes back later than last time."
    }

    /** Flashcards due today across the language courses this person has started. */
    fun wordsLine(plan: PersonPlan, progress: List<TopicProgress>, cards: List<Flashcard>): String? {
        val total = Courses.all.filter { Courses.words(it).isNotEmpty() }.sumOf { c ->
            WordDeck.dueCount(plan.role, c, cards, plan.today) + WordDeck.newCount(plan.role, c, cards, progress)
        }
        if (total == 0) return null
        // Four cards a minute, and never promise less than two: a two-card
        // deck that claims nought minutes reads as a bug.
        return "Words: $total cards waiting in the deck, about ${(total / 4).coerceAtLeast(2)} minutes. " +
            "Missed words come back until they stick."
    }

    /** The next unread topic of the first course feeding an active goal. */
    fun learnLine(plan: PersonPlan, progress: List<TopicProgress>, attempts: List<QuizAttempt>): String? {
        val course = coursesInPlay(plan).firstOrNull() ?: return null
        val cp = CourseProgress.of(plan.role, course.id, progress, attempts)
        val (module, topic) = cp.nextTopic(course)
            ?: return "${course.title}: every topic read. Retake a module test to keep it sharp."
        return "Learn tab: ${topic.title.substringBefore(":")}, about ${topic.minutes} minutes, " +
            "counts toward ${module.title}."
    }

    fun greeting(name: String, hour: Int): String = when {
        hour < 12 -> "Good morning, $name"
        hour < 18 -> "Good afternoon, $name"
        else -> "Good evening, $name"
    }

    fun streakLine(streak: Int): String = when (streak) {
        0 -> "No streak yet. Today starts one."
        1 -> "1-day streak. Make it two."
        else -> "$streak-day streak. Keep it alive."
    }

    /**
     * The whole brief.
     *
     * [hour] and [dateLine] come from the person's own clock, which is their
     * timezone and not the device's, so they are passed in rather than read.
     */
    fun build(
        plan: PersonPlan,
        card: PersonCard,
        hour: Int,
        dateLine: String,
        deadline: Deadline?,
        deadlineDays: Long?,
        progress: List<TopicProgress>,
        attempts: List<QuizAttempt>,
        cards: List<Flashcard>,
        targetDateLabel: (LocalDate) -> String,
    ): DailyBrief {
        val first = card.sessions.firstOrNull { it.session.status == SessionStatus.PLANNED }
            ?: card.sessions.firstOrNull()
        val goal = first?.goal
        val goalProgress = goal?.let { plan.goalProgress[it.id] }
        val reasons = mutableListOf<BriefReason>()

        if (goal != null && goalProgress != null) {
            val due = goalProgress.targetDate?.let(targetDateLabel)
            val need = goalProgress.weeklyNeedHours
            reasons += BriefReason(
                BriefKind.GOAL,
                buildString {
                    append("${goal.title}: %.0f of %.0f hours done".format(goalProgress.hoursLogged, goalProgress.hoursRequired))
                    if (due != null && need != null) append(". About %.0f hours a week keeps it on track for $due".format(need))
                    append(".")
                },
            )
        } else if (plan.activeGoals.isNotEmpty()) {
            val g = plan.activeGoals.first()
            val gp = plan.goalProgress[g.id]
            reasons += BriefReason(
                BriefKind.GOAL,
                "${g.title}: %.0f of %.0f hours done. A rest day keeps the streak honest; logging anything counts."
                    .format(gp?.hoursLogged ?: 0.0, gp?.hoursRequired ?: 0.0),
            )
        }

        plan.current?.let { level ->
            val left = level.daysLeft?.let { if (it >= 0) "$it days left" else "${-it} days overdue" } ?: ""
            reasons += BriefReason(
                BriefKind.LEVEL,
                "Level ${level.level.number}, ${level.level.name}: " +
                    "${level.requiredDone} of ${level.requiredTotal} done, $left. " +
                    "Finishing it unlocks Level ${level.level.number + 1} on time.",
            )
        }

        learnLine(plan, progress, attempts)?.let { reasons += BriefReason(BriefKind.LEARN, it) }
        reviewLine(plan, progress, attempts)?.let { reasons += BriefReason(BriefKind.LEARN, it) }
        dailyLine(plan, progress, attempts)?.let { reasons += BriefReason(BriefKind.LEARN, it) }
        wordsLine(plan, progress, cards)?.let { reasons += BriefReason(BriefKind.LEARN, it) }

        reasons += BriefReason(
            BriefKind.MOVE,
            if (deadline != null && deadlineDays != null) {
                "${deadline.title} in $deadlineDays days. Every session now is one less to squeeze in later."
            } else {
                "Every session now is one less to squeeze in later."
            },
        )

        return DailyBrief(
            greeting = greeting(plan.name, hour),
            dateLine = dateLine,
            task = if (first == null) "Rest day" else (goal?.title ?: "Study"),
            taskDetail = if (first == null) {
                "No slot today. Log anything you study."
            } else {
                "${first.componentName} · ${first.session.durationMinutes} min · ${first.timeLabel}"
            },
            reasons = reasons,
            streakLine = streakLine(plan.streak),
            isRestDay = first == null,
            time = first?.let { Dates.minutesToClock(it.session.startMinutes) },
        )
    }

    private fun coursesInPlay(plan: PersonPlan) =
        Courses.all.filter { c -> plan.activeGoalIds.any { c.feeds(it) } }

    private fun plural(n: Int) = if (n == 1) "" else "s"
}
