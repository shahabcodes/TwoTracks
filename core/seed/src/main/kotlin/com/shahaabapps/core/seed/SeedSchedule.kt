package com.shahaabapps.core.seed

import com.shahaabapps.core.model.Availability
import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.core.model.Deadline
import com.shahaabapps.core.model.Habit
import com.shahaabapps.core.model.PersonRole

/** Weekly templates from PLAN.md section 1, plus habits and countdown deadlines. */
object SeedSchedule {

    private fun hm(h: Int, m: Int = 0) = h * 60 + m

    /** Sun-Thu work 07:00-15:30, study 16:15-17:30. */
    val techAvailability = Availability(
        owner = PersonRole.TECH,
        blocks = listOf(7, 1, 2, 3, 4).flatMap { day ->
            listOf(
                AvailabilityBlock(id = "tech-work-$day", dayOfWeek = day, startMinutes = hm(7), endMinutes = hm(15, 30), kind = BlockKind.WORK, label = "Work"),
                AvailabilityBlock(id = "tech-study-$day", dayOfWeek = day, startMinutes = hm(16, 15), endMinutes = hm(17, 30), kind = BlockKind.STUDY, label = "Study slot"),
            )
        },
    )

    /** Mon-Fri school 08:00-14:00, IELTS class 16:00-18:00. */
    val teacherAvailability = Availability(
        owner = PersonRole.TEACHER,
        blocks = (1..5).flatMap { day ->
            listOf(
                AvailabilityBlock(id = "teacher-school-$day", dayOfWeek = day, startMinutes = hm(8), endMinutes = hm(14), kind = BlockKind.WORK, label = "School"),
                AvailabilityBlock(id = "teacher-class-$day", dayOfWeek = day, startMinutes = hm(16), endMinutes = hm(18), kind = BlockKind.CLASS, label = "IELTS class", goalId = "ielts-academic"),
            )
        },
    )

    fun availabilityFor(role: PersonRole): Availability =
        if (role == PersonRole.TECH) techAvailability else teacherAvailability

    val defaultTimezone = mapOf(
        PersonRole.TECH to "Asia/Qatar",
        PersonRole.TEACHER to "Asia/Karachi",
    )

    val habits: List<Habit> = listOf(
        Habit(id = "applications", owner = PersonRole.TECH, name = "Job applications", unit = "applications", weeklyTarget = 12, startDateIso = "2027-01-01"),
        Habit(id = "github", owner = PersonRole.TECH, name = "GitHub project sessions", unit = "sessions", weeklyTarget = 2),
        Habit(id = "teaching-days", owner = PersonRole.TEACHER, name = "Teaching days (formal contract)", unit = "days", totalTarget = 220),
    )

    val deadlines: List<Deadline> = listOf(
        Deadline(id = "marriage", title = "Marriage", dateIso = "2027-02-01", detail = "Register and attest the certificate right after."),
        Deadline(id = "her-25", title = "She turns 25", dateIso = "2027-07-01", owner = PersonRole.TEACHER, detail = "Top Australian age band from here."),
        Deadline(id = "checkpoint", title = "Checkpoint: European offer?", dateIso = "2027-09-30", detail = "Offer: apply for both visas together. No offer: Australia EOI, her Canada profile, NZ teaching jobs."),
        Deadline(id = "you-30", title = "You turn 30", dateIso = "2029-02-01", owner = PersonRole.TECH, detail = "Canada age points fall; Dutch under-30 salary threshold ends."),
        Deadline(id = "au-top-band", title = "Australia top age band ends (you)", dateIso = "2032-02-01", owner = PersonRole.TECH),
    )
}
