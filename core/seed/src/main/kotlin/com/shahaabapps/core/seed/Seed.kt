package com.shahaabapps.core.seed

import com.shahaabapps.core.model.Deadline
import com.shahaabapps.core.model.Habit
import com.shahaabapps.core.model.Level
import com.shahaabapps.core.model.StudyGoal
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Entry point for everything seeded into a new couple space. Bump [VERSION]
 * when the seed content changes; the app re-seeds spaces that are behind.
 *
 * The guide's roadmap is written against [ANCHOR_ISO] (1 October 2026). The
 * plan-relative dates (level start and due dates, item and goal target dates,
 * habit start dates and the checkpoint) are shifted so that Level 1 starts on
 * the [baseIso] handed in, normally the day the space is created or reset.
 * Real-world dates such as the marriage and birthdays never move.
 */
object Seed {
    const val VERSION = 5
    const val ANCHOR_ISO = "2026-10-01"

    val goals get() = SeedGoals.all
    val levels get() = SeedLevels.all
    val habits get() = SeedSchedule.habits
    val deadlines get() = SeedSchedule.deadlines
    val countries get() = SeedCountries.all
    val tests get() = SeedRecords.tests
    val documents get() = SeedRecords.documents
    val budget get() = SeedRecords.budget

    /** Days between the guide's anchor and [baseIso]; zero when they match. */
    fun shiftDays(baseIso: String): Long = ChronoUnit.DAYS.between(LocalDate.parse(ANCHOR_ISO), LocalDate.parse(baseIso))

    fun shift(iso: String?, days: Long): String? = iso?.let { LocalDate.parse(it).plusDays(days).toString() }

    fun goals(baseIso: String): List<StudyGoal> {
        val d = shiftDays(baseIso)
        return goals.map { it.copy(targetDateIso = shift(it.targetDateIso, d)) }
    }

    fun levels(baseIso: String): List<Level> {
        val d = shiftDays(baseIso)
        return levels.map { lvl ->
            lvl.copy(
                startDateIso = shift(lvl.startDateIso, d) ?: lvl.startDateIso,
                dueDateIso = shift(lvl.dueDateIso, d) ?: lvl.dueDateIso,
                items = lvl.items.map { it.copy(dueDateIso = shift(it.dueDateIso, d)) },
            )
        }
    }

    fun habits(baseIso: String): List<Habit> {
        val d = shiftDays(baseIso)
        return habits.map { it.copy(startDateIso = shift(it.startDateIso, d)) }
    }

    /** Only the plan checkpoint moves; marriage and birthdays are fixed in the real world. */
    fun deadlines(baseIso: String): List<Deadline> {
        val d = shiftDays(baseIso)
        return deadlines.map { if (it.id == "checkpoint") it.copy(dateIso = shift(it.dateIso, d) ?: it.dateIso) else it }
    }
}
