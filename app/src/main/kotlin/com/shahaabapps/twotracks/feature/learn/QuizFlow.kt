package com.shahaabapps.twotracks.feature.learn

/**
 * What answering a question does to a sitting.
 *
 * These were three methods on QuizViewModel, which cannot be built without a
 * SavedStateHandle and four collaborators, so the one place the app turns
 * taps into the records everything else derives from had nothing checking it.
 * A sitting produces the missed list that fills the review set, the picked
 * map the results page reads an option's note from, and the seconds the
 * spaced schedule uses to tell a confident answer from a worked-out one. All
 * three are written here.
 *
 * They are pure: a state in, a state out. The view model keeps the clock and
 * the saving.
 */
object QuizFlow {

    /**
     * Choosing an option.
     *
     * A multi-select question toggles, so a second tap on the same option
     * takes it off again. A single-select replaces, because a reader who
     * changes their mind should not end up having picked two.
     */
    fun toggled(state: QuizUiState, option: Int): QuizUiState {
        // Once the answer is shown the question is over; a tap after that
        // would rewrite what was recorded.
        if (state.revealed) return state
        val question = state.question ?: return state
        val next = when {
            !question.multi -> setOf(option)
            option in state.selected -> state.selected - option
            else -> state.selected + option
        }
        return state.copy(selected = next)
    }

    /**
     * Recording the answer.
     *
     * [tookSeconds] is measured to this point and not to Next: with instant
     * feedback the explanation is read after answering, and reading it is not
     * deciding. Nothing happens without a selection, so a question skipped by
     * the clock has no entry anywhere and counts as missed.
     */
    fun answered(state: QuizUiState, tookSeconds: Int): QuizUiState {
        val question = state.question ?: return state
        if (state.selected.isEmpty()) return state
        if (state.revealed) return state
        return state.copy(
            revealed = true,
            answers = state.answers + (question.id to (state.selected == question.correct)),
            picked = state.picked + (question.id to state.selected),
            seconds = state.seconds + (question.id to tookSeconds.coerceAtLeast(1)),
        )
    }

    /** Moving on, or finishing if that was the last question. */
    fun advanced(state: QuizUiState): QuizUiState =
        if (state.index + 1 >= state.total) {
            state.copy(finished = true)
        } else {
            state.copy(index = state.index + 1, selected = emptySet(), revealed = false)
        }
}
