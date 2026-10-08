package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Per-option feedback: why the wrong answer you picked was tempting.
 *
 * The notes are keyed by index into the options, and [B.spread] rotates the
 * options to stop the right answer always sitting in the same slot. If the
 * keys did not rotate with everything else, an explanation would attach to an
 * answer it was never written about — quietly, and only for some questions.
 */
class WhyWrongTest {

    private fun question(id: String) = B.q(
        id, "t", "Which tier is cheapest with deployment slots?",
        listOf("Free", "Basic", "Standard", "Premium"), setOf(2),
        "Slots start at Standard.",
        whyWrong = mapOf(
            0 to "Free has no slots and no Always On.",
            1 to "Basic gives dedicated workers, but slots start one tier higher.",
            3 to "Premium has slots, but it is not the cheapest that does.",
        ),
    )

    @Test
    fun `rotating the options carries the notes with them`() {
        // Four questions, so spread rotates each by a different amount.
        val spread = B.spread(List(4) { question("q$it") })
        spread.forEach { q ->
            val right = q.correct.single()
            assertEquals("the right answer is still Standard", "Standard", q.options[right])
            q.whyWrong.forEach { (i, note) ->
                assertTrue("a note must not sit on the right answer", i != right)
                val expected = when (q.options[i]) {
                    "Free" -> "Free has no slots and no Always On."
                    "Basic" -> "Basic gives dedicated workers, but slots start one tier higher."
                    "Premium" -> "Premium has slots, but it is not the cheapest that does."
                    else -> error("unexpected option ${q.options[i]}")
                }
                assertEquals("the note must follow its own option", expected, note)
            }
            assertEquals(3, q.whyWrong.size)
        }
    }

    @Test
    fun `the note shown is the one for the option actually picked`() {
        val q = question("q")
        assertEquals("Free has no slots and no Always On.", q.whyWrongFor(setOf(0)))
        assertEquals("Premium has slots, but it is not the cheapest that does.", q.whyWrongFor(setOf(3)))
    }

    @Test
    fun `a right answer has nothing to explain away`() {
        assertNull(question("q").whyWrongFor(setOf(2)))
    }

    @Test
    fun `nothing is shown for an option with no note, or for no answer at all`() {
        val q = B.q(
            "q", "t", "Pick one", listOf("a", "b", "c"), setOf(0), "because",
            whyWrong = mapOf(1 to "b is tempting"),
        )
        assertEquals("b is tempting", q.whyWrongFor(setOf(1)))
        assertNull("no note was written for c", q.whyWrongFor(setOf(2)))
        assertNull("nothing picked", q.whyWrongFor(emptySet()))
    }

    @Test
    fun `a multi-select answer gets the general explanation, not a per-option one`() {
        // With several boxes ticked there is no single trap to name.
        val q = B.q(
            "q", "t", "Pick two", listOf("a", "b", "c"), setOf(0, 1), "because",
            whyWrong = mapOf(2 to "c is tempting"),
        )
        assertNull(q.whyWrongFor(setOf(1, 2)))
    }

    @Test
    fun `every note across the shipped courses points at a real wrong option`() {
        // Guards the content: a note on the right answer, or on an index that
        // does not exist, would be shown at the wrong moment or never.
        Courses.all.forEach { c ->
            c.allQuestions.forEach { q ->
                q.whyWrong.forEach { (i, note) ->
                    assertTrue("${q.id} note index $i is out of range", i in q.options.indices)
                    assertTrue("${q.id} has a note on its correct answer", i !in q.correct)
                    assertTrue("${q.id} note $i is empty", note.isNotBlank())
                }
            }
        }
    }
}
