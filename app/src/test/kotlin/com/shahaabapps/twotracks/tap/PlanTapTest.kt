package com.shahaabapps.twotracks.tap

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import com.shahaabapps.core.model.GoalKind
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.twotracks.feature.levels.LevelContent
import com.shahaabapps.twotracks.feature.levels.LevelsContent
import com.shahaabapps.twotracks.feature.study.FitChoice
import com.shahaabapps.twotracks.feature.study.FitSheet
import com.shahaabapps.twotracks.feature.study.GoalDetailContent
import com.shahaabapps.twotracks.feature.study.GoalEditContent
import com.shahaabapps.twotracks.preview.PlanSamples
import org.junit.Assert.assertTrue
import org.junit.Test

/** The levels, a level, a goal, the goal form and the fit check: what each press reaches. */
class PlanTapTest(look: AppLook) : TapTest(look) {
    @Test
    fun `the levels swap to your partner's line and open a level`() {
        val state = PlanSamples.levels()
        show { LevelsContent(state, onBack = record("back"), onOpenLevel = record1("level"), onShowPartner = record1("partner")) }
        tap(label("Sanya", "Sanya · Edu"))
        expect("partner(true)")
        tap("Launch applications", substring = true)
        expect("level(${state.me!!.levels[1].level.id})")
    }

    @Test
    fun `a level opens a goal, ticks a task, adds a goal and boards the next one early`() {
        val state = PlanSamples.level()
        val goal = state.progress!!.items.first { it.item.goalId in state.goalsById }.item
        val task = state.progress!!.items.first { it.item.kind == com.shahaabapps.core.model.LevelItemKind.TASK && !it.done }.item
        show {
            LevelContent(
                state, onBack = record("back"), onOpenGoal = record1("goal"), onNewGoal = record("newGoal"), onToggleTask = { calls += "toggle(${it.id})" },
                onAddTask = { _, _, _ -> calls += "addTask" }, onRemove = { calls += "remove(${it.id})" },
                onStartNext = record("startNext"), onChooseBranch = record1("branch"), onMessageShown = {},
            )
        }
        tap(goal.title)
        expect("goal(${goal.goalId})")
        tap(task.title)
        expect("toggle(${task.id})")
        tap(label("Add a goal to this level", "Add a goal"))
        tap("Goal with hours")
        expect("newGoal")
        tap(label("Start Level 2 early", "Board stop 02 early"))
        expect("startNext")
    }

    @Test
    fun `a goal edits, adds a practice score, marks itself passed and goes back`() {
        show {
            GoalDetailContent(
                PlanSamples.goal(), remember { SnackbarHostState() }, onBack = record("back"), onEdit = record("edit"), onOpenCourse = record1("course"),
                onSetPassed = record1("passed"), onAddMock = { _, _ -> calls += "mock" }, onDeleteMock = { calls += "deleteMock" }, onDeleteLog = { calls += "deleteLog" },
            )
        }
        tap("Edit")
        expect("edit")
        tap("Add one")
        assertTrue("the practice score form opens in the $look look", seen("Practice result"))
        tap("Cancel")
        tap("Mark as passed")
        tap("Yes, passed")
        expect("passed(true)")
        tapDescribed(label("Back", "Back to the level"))
        expect("back")
    }

    @Test
    fun `the goal form changes class, adds and removes a leg, saves and cancels`() {
        val state = PlanSamples.goalEdit()
        show {
            GoalEditContent(
                state, onCancel = record("cancel"), onTitle = record1("title"), onWhy = record1("why"), onKind = record1("kind"), onTargetDate = record1("date"),
                onPriority = record1("priority"), onRequiredForLevel = record1("required"), onComponent = { _, _, _, _ -> calls += "component" },
                onAddComponent = record("addLeg"), onRemoveComponent = record1("removeLeg"), onPreset = { _, _ -> calls += "preset" }, onSave = record("save"), onDelete = record("delete"),
            )
        }
        tap("Custom")
        expect("kind(${GoalKind.CUSTOM})")
        tap(label("+ Add a part", "+ Add a leg"))
        expect("addLeg")
        tapDescribed("Remove ${state.components.first().name}")
        expect("removeLeg(0)")
        tap("Save")
        expect("save")
        tap("Cancel")
        expect("cancel")
    }

    @Test
    fun `the fit check does the chosen way out, or goes back`() {
        show { FitSheet(PlanSamples.fit(), onChoose = record1("choose"), onDismiss = record("back")) }
        tap("Save anyway", index = 0)
        tap("Save anyway", index = 1)
        expect("choose(${FitChoice.SAVE_ANYWAY})")
        tap("Back")
        expect("back")
    }
}
