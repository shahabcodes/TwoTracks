package com.shahaabapps.core.planner

import com.shahaabapps.core.model.GoalComponent
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.model.StudyLog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class ProgressCalculatorTest {

    private val today: LocalDate = LocalDate.of(2026, 10, 4)

    private val ielts = StudyGoal(
        id = "ielts",
        owner = PersonRole.TEACHER,
        title = "IELTS Academic",
        targetDateIso = "2027-01-09", // 97 days away
        components = listOf(
            GoalComponent("l", "Listening", 30.0),
            GoalComponent("r", "Reading", 40.0),
            GoalComponent("w", "Writing", 50.0),
            GoalComponent("s", "Speaking", 40.0),
        ),
    )

    private fun log(date: String, component: String, minutes: Int) =
        StudyLog(id = "$date-$component-$minutes", owner = PersonRole.TEACHER, dateIso = date, goalId = "ielts", componentId = component, minutes = minutes)

    @Test
    fun `hours are summed per component and remaining is budget minus logged`() {
        val logs = listOf(log("2026-10-01", "r", 60), log("2026-10-02", "r", 90), log("2026-10-02", "w", 30))
        val p = ProgressCalculator.compute(ielts, logs, today)
        assertEquals(2.5, p.components.first { it.component.id == "r" }.hoursLogged, 1e-9)
        assertEquals(37.5, p.components.first { it.component.id == "r" }.remainingHours, 1e-9)
        assertEquals(160.0, p.hoursRequired, 1e-9)
        assertEquals(3.0, p.hoursLogged, 1e-9)
        assertEquals(157.0, p.remainingHours, 1e-9)
    }

    @Test
    fun `weekly need divides remaining hours by weeks to target`() {
        val p = ProgressCalculator.compute(ielts, emptyList(), today)
        assertEquals(97L, p.daysToTarget)
        assertEquals(160.0 / (97.0 / 7.0), p.weeklyNeedHours!!, 1e-6)
    }

    @Test
    fun `recent pace uses last 14 days and feasibility compares to need`() {
        val logs = (0L until 14L).map { offset ->
            log(Dates.format(today.minusDays(offset)), "l", 86)
        }
        val p = ProgressCalculator.compute(ielts, logs, today)
        assertEquals(10.03, p.recentWeeklyHours, 0.05)
        // Need is about 11.5 h/week; 10 h/week is within 75% so TIGHT.
        assertEquals(Feasibility.TIGHT, p.feasibility)
    }

    @Test
    fun `no logs means no forecast and does not fit`() {
        val p = ProgressCalculator.compute(ielts, emptyList(), today)
        assertNull(p.forecastDate)
        assertEquals(Feasibility.DOES_NOT_FIT, p.feasibility)
    }

    @Test
    fun `forecast date projects remaining hours at recent pace`() {
        val logs = (0L until 14L).map { log(Dates.format(today.minusDays(it)), "w", 60) } // 7 h/week
        val p = ProgressCalculator.compute(ielts, logs, today)
        // 146 h remaining at 7 h/week = 20.857 weeks = 146 days
        assertEquals(today.plusDays(146), p.forecastDate)
    }

    @Test
    fun `capacity report sums needs across active goals`() {
        val german = ielts.copy(id = "de", title = "German", targetDateIso = "2027-10-01")
        val progress = ProgressCalculator.computeAll(listOf(ielts, german), emptyList(), today)
        val report = CapacityReport.build(weeklyCapacityMinutes = 6 * 60, progress.values)
        assertEquals(6.0, report.weeklyCapacityHours, 1e-9)
        assertEquals(progress.values.sumOf { it.weeklyNeedHours!! }, report.weeklyNeedHours, 1e-9)
        assertEquals(false, report.fits)
    }
}
