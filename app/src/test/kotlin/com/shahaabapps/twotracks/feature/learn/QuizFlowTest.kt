package com.shahaabapps.twotracks.feature.learn

import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.Question
import com.shahaabapps.core.content.Quiz
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * A sitting is where taps become the records the rest of the app derives from.
 *
 * The missed list fills the review set. The picked map is what the results
 * page reads an option's note out of, which is the only reason a mock exam can
 * say why the answer you chose was tempting. The seconds are what let a
 * confident miss be scheduled differently from an unsure one. All three are
 * written by three state transitions that, until they were lifted out of
 * QuizViewModel, nothing could reach.
 */
class QuizFlowTest {

    // multi is derived: more than one right answer makes it select-all.
    private fun question(id: String, correct: Set<Int>) = Question(
        id = id, topicId = "t", prompt = "p", scenario = null,
        options = listOf("a", "b", "c", "d"), correct = correct, explanation = "e",
    )

    private fun sitting(vararg questions: Question) = QuizUiState(
        quiz = Quiz(id = "q", title = "Test", passPercent = 60, questions = questions.toList()),
        asked = questions.toList(),
    )

    // ---- choosing -------------------------------------------------------

    @Test
    fun `a single-answer question replaces rather than adds`() {
        // Changing your mind must not leave you having picked two, which
        // would be recorded as wrong however right the second one was.
        var s = sitting(question("q1", setOf(1)))
        s = QuizFlow.toggled(s, 0)
        s = QuizFlow.toggled(s, 2)
        assertEquals(setOf(2), s.selected)
    }

    @Test
    fun `a multi-answer question toggles each option`() {
        var s = sitting(question("q1", setOf(0, 2)))
        s = QuizFlow.toggled(s, 0)
        s = QuizFlow.toggled(s, 2)
        assertEquals(setOf(0, 2), s.selected)
        s = QuizFlow.toggled(s, 0)
        assertEquals(setOf(2), s.selected)
    }

    @Test
    fun `nothing can be changed once the answer is shown`() {
        var s = sitting(question("q1", setOf(1)))
        s = QuizFlow.toggled(s, 0)
        s = QuizFlow.answered(s, 5)
        val after = QuizFlow.toggled(s, 3)
        assertSame(s, after)
        assertEquals(setOf(0), after.selected)
    }

    // ---- answering ------------------------------------------------------

    @Test
    fun `answering records what was picked, whether it was right, and how long it took`() {
        var s = sitting(question("q1", setOf(1)))
        s = QuizFlow.toggled(s, 1)
        s = QuizFlow.answered(s, 37)
        assertTrue(s.revealed)
        assertEquals(mapOf("q1" to true), s.answers)
        assertEquals(mapOf("q1" to setOf(1)), s.picked)
        assertEquals(mapOf("q1" to 37), s.seconds)
    }

    @Test
    fun `a wrong answer is still recorded as picked, which is the point of it`() {
        // missed already says it was wrong. picked is what the results page
        // needs to show the note written about that option.
        var s = sitting(question("q1", setOf(1)))
        s = QuizFlow.toggled(s, 3)
        s = QuizFlow.answered(s, 4)
        assertEquals(mapOf("q1" to false), s.answers)
        assertEquals(mapOf("q1" to setOf(3)), s.picked)
    }

    @Test
    fun `answering nothing records nothing`() {
        val s = sitting(question("q1", setOf(1)))
        assertSame(s, QuizFlow.answered(s, 10))
    }

    @Test
    fun `answering twice does not overwrite the first answer`() {
        var s = sitting(question("q1", setOf(1)))
        s = QuizFlow.toggled(s, 1)
        s = QuizFlow.answered(s, 12)
        val again = QuizFlow.answered(s, 99)
        assertSame(s, again)
        assertEquals(mapOf("q1" to 12), again.seconds)
    }

    @Test
    fun `a question answered instantly still counts as one second`() {
        // The clock is whole seconds, so a fast tap rounds to nought, and a
        // nought would read as a question that was never shown.
        var s = sitting(question("q1", setOf(1)))
        s = QuizFlow.toggled(s, 1)
        s = QuizFlow.answered(s, 0)
        assertEquals(mapOf("q1" to 1), s.seconds)
    }

    // ---- moving on ------------------------------------------------------

    @Test
    fun `moving on clears the selection and the reveal`() {
        var s = sitting(question("q1", setOf(1)), question("q2", setOf(2)))
        s = QuizFlow.toggled(s, 1)
        s = QuizFlow.answered(s, 8)
        s = QuizFlow.advanced(s)
        assertEquals(1, s.index)
        assertTrue(s.selected.isEmpty())
        assertFalse(s.revealed)
        assertFalse(s.finished)
        // and what was recorded survives
        assertEquals(mapOf("q1" to setOf(1)), s.picked)
    }

    @Test
    fun `moving on from the last question finishes the sitting`() {
        var s = sitting(question("q1", setOf(1)))
        s = QuizFlow.toggled(s, 1)
        s = QuizFlow.answered(s, 8)
        s = QuizFlow.advanced(s)
        assertTrue(s.finished)
        assertEquals(0, s.index)
    }

    // ---- a whole paper --------------------------------------------------

    @Test
    fun `a sitting that runs out of time counts the unreached questions as missed`() {
        // The timer sets finished without advancing, so questions after the
        // one on screen were never answered and must not be scored right.
        var s = sitting(question("q1", setOf(0)), question("q2", setOf(1)), question("q3", setOf(2)))
        s = QuizFlow.answered(QuizFlow.toggled(s, 0), 5)
        s = QuizFlow.advanced(s)
        s = s.copy(finished = true, timedOut = true)
        assertEquals(1, s.correctCount)
        assertEquals(listOf("q2", "q3"), s.missedQuestions.map { it.id })
        assertEquals(33, s.percent)
    }

    @Test
    fun `a full sitting of a real module test scores and records every question`() {
        val module = Courses.all.first { it.id == "de-a1" }.modules.first()
        val quiz = module.quiz!!
        var s = QuizUiState(quiz = quiz, asked = quiz.questions)
        quiz.questions.forEach { q ->
            // Answer every one correctly, one option at a time for the
            // multi-answer ones.
            q.correct.forEach { option -> s = QuizFlow.toggled(s, option) }
            s = QuizFlow.answered(s, 20)
            s = QuizFlow.advanced(s)
        }
        assertTrue(s.finished)
        assertEquals(quiz.questions.size, s.correctCount)
        assertEquals(100, s.percent)
        assertTrue(s.passed)
        assertTrue(s.missedQuestions.isEmpty())
        assertEquals(quiz.questions.size, s.picked.size)
        assertEquals(quiz.questions.size, s.seconds.size)
        assertEquals(quiz.questions.map { it.id }.toSet(), s.picked.keys)
    }
}
