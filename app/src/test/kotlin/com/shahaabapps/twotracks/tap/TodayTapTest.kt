package com.shahaabapps.twotracks.tap

import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.twotracks.feature.today.DailyBriefSheet
import com.shahaabapps.twotracks.feature.today.TodayContent
import com.shahaabapps.twotracks.feature.week.BothWeeksContent
import com.shahaabapps.twotracks.feature.week.WeekContent
import com.shahaabapps.twotracks.preview.Samples
import com.shahaabapps.twotracks.preview.sampleBrief
import org.junit.Test

/** Today, the brief, your timetable and both weeks: what each press reaches. */
class TodayTapTest(look: AppLook) : TapTest(look) {
    @Test
    fun `today starts the next session and opens the brief, settings, the week and the level`() {
        val state = Samples.today()
        show {
            TodayContent(
                state = state, onOpenLevel = record1("level"), onOpenPartner = record("partner"), onOpenGoal = record1("goal"), onOpenWeek = record("week"),
                onOpenSettings = record("settings"), onOpenChangelog = record("changelog"), onOpenTopic = record2("topic"), onOpenQuiz = record2("quiz"),
                onOpenBrief = record("brief"), onDismissBrief = record("dismissBrief"), onDismissWhatsNew = record("dismissWhatsNew"),
                onDone = { calls += "done" }, onSkip = record1("skip"), onReopen = record1("reopen"), onMessageShown = {},
            )
        }
        tap(label("Start reading  →", "Board now"))
        expectStarting("topic(")
        tapDescribed("Settings")
        expect("settings")
        tapAction("Open the brief")
        expect("brief")
        tap(label("Your week", "Week ›"))
        expect("week")
        tapAction("Open the level")
        expect("level(${state.level!!.level.id})")
    }

    @Test
    fun `the brief starts the day or waits till later`() {
        show { DailyBriefSheet(sampleBrief(), AppTheme.colors.tech, onStart = record("start"), onDismiss = record("later")) }
        // The sheet slides up; let it finish before pressing what is on it.
        compose.mainClock.advanceTimeBy(2_000)
        compose.waitForIdle()
        tap(label("Start ${sampleBrief().task}", "Board now"))
        expect("start")
        tap("Later")
        expect("later")
    }

    @Test
    fun `the timetable moves through weeks, swaps people, re-plans and opens both weeks and the fit check`() {
        show {
            WeekContent(
                state = Samples.week(), onBack = record("back"), onOpenGoal = record1("goal"), onOpenBothWeeks = record("both"), onOpenFit = record("fit"),
                onPreviousWeek = record("previous"), onNextWeek = record("next"), onThisWeek = record("this"), onTogglePerson = record("person"), onReplan = record("replan"),
                onAssign = { _, _, _ -> calls += "assign" }, onAddBlock = { _, _, _ -> calls += "add" }, onMessageShown = {},
            )
        }
        tap(label("Both weeks", "Both lines"))
        expect("both")
        tapAction("Ways to make it fit")
        expect("fit")
        if (board) tap("‹ Last week") else tapDescribed("Previous week")
        if (board) tap("Next week ›") else tapDescribed("Next week")
        expect("previous", "next")
        tap(label("See your partner's week", "Your partner's line"))
        tap(label("Re-plan the week", "Re-plan"))
        expect("person", "replan")
    }

    @Test
    fun `both weeks goes back`() {
        show { BothWeeksContent(state = Samples.both(), onBack = record("back")) }
        tapDescribed(label("Back", "Back to your timetable"))
        expect("back")
    }
}
