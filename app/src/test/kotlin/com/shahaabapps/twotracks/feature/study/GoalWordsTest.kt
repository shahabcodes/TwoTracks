package com.shahaabapps.twotracks.feature.study

import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.core.model.CertTier
import com.shahaabapps.core.model.GoalKind
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.FitPlanner
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

/** The words a goal, its form and the fit sheet print. */
class GoalWordsTest {

    @Test
    fun `the eyebrow gives kind, tier and fee`() {
        assertEquals("Certification · Tier 1 · USD 165", goalEyebrow(StudyGoal(kind = GoalKind.CERTIFICATION, tier = CertTier.TIER_1, feeUsd = 165)))
        assertEquals("Language level", goalEyebrow(StudyGoal(kind = GoalKind.LANGUAGE_LEVEL)))
        assertEquals("Goal · Passed", goalEyebrow(StudyGoal(passed = true)))
    }

    @Test
    fun `the form totals the hours typed so far`() {
        assertEquals("120 h total", totalHours(listOf(ComponentDraft("a", "A", "30", ""), ComponentDraft("b", "B", "90", ""))))
        assertEquals("7.5 h total", totalHours(listOf(ComponentDraft("a", "A", "7.5", ""), ComponentDraft("b", "B", "", ""))))
    }

    private fun fit(extend: Boolean, more: Boolean) = FitCheck(
        result = FitPlanner.FitResult(
            fits = false, demandHours = 128.0, weeksLeft = 24.0, weeklyCapacityHours = 4.67, neededWeeklyHours = 5.3,
            extend = if (extend) FitPlanner.ExtendOption(LocalDate.of(2027, 4, 28), 28) else null,
            moreHours = if (more) FitPlanner.MoreHoursOption(30, listOf(AvailabilityBlock(id = "b", dayOfWeek = 1, startMinutes = 19 * 60, endMinutes = 20 * 60 + 30, kind = BlockKind.STUDY)), tooLong = false) else null,
        ),
        levelId = "tech-1", levelNumber = 1, levelName = "Foundations", levelDueIso = "2027-03-31", addedHours = 20.0,
    )

    @Test
    fun `the sheet offers what the planner found, and saving anyway last`() {
        val all = fitOptions(fit(extend = true, more = true))
        assertEquals(listOf(FitChoice.EXTEND, FitChoice.MORE_HOURS, FitChoice.SAVE_ANYWAY), all.map { it.choice })
        assertEquals("Move the level to 28 Apr 2027", all[0].title)
        assertEquals("Move it and save", all[0].cta)
        assertEquals(listOf(FitChoice.SAVE_ANYWAY), fitOptions(fit(extend = false, more = false)).map { it.choice })
    }

    @Test
    fun `moving four weeks says four weeks`() =
        assertEquals(true, fitOptions(fit(extend = true, more = false))[0].detail.startsWith("Four weeks more."))
}
