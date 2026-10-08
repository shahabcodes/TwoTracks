package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * A faded example is marked by matching one typed step, which has the same
 * two failure modes as any keyword marking: too strict and a correct answer
 * in different words is rejected, too loose and anything passes.
 */
class FadedTest {

    private val faded: List<Pair<Course, Block.Faded>> = Courses.all.flatMap { c ->
        c.allTopics.flatMap { t -> t.blocks.filterIsInstance<Block.Faded>() }.map { c to it }
    }

    @Test
    fun `every AZ-204 topic fades its worked example, exactly once`() {
        Az204.course.allTopics.forEach { t ->
            val n = t.blocks.filterIsInstance<Block.Faded>().size
            assertEquals("az-204/${t.id} should fade once", 1, n)
        }
    }

    @Test
    fun `the faded step follows the worked example it fades`() {
        // The fade only reads as one movement if it is next to the solution it
        // takes a step out of. At the end of the topic it is just another gap.
        Az204.course.allTopics.forEach { t ->
            val worked = t.blocks.indexOfFirst { it is Block.Callout && it.kind == CalloutKind.WORKED }
            val fade = t.blocks.indexOfFirst { it is Block.Faded }
            assertTrue("az-204/${t.id} should have a worked example", worked >= 0)
            assertEquals("az-204/${t.id}: the fade should come straight after it", worked + 1, fade)
        }
    }

    @Test
    fun `the blank is a real step and it has a blank in it`() {
        faded.forEach { (c, f) ->
            assertTrue("${c.id}/${f.id}: blank ${f.blank} is outside the steps", f.blank in f.steps.indices)
            assertTrue("${c.id}/${f.id}: the blank step has no ___ in it", f.steps[f.blank].contains("___"))
            // Two given steps and one blank: a single step with a hole is a gap,
            // not a faded solution.
            assertTrue("${c.id}/${f.id}: wanted three steps, had ${f.steps.size}", f.steps.size >= 3)
            f.steps.forEachIndexed { i, s ->
                if (i != f.blank) assertFalse("${c.id}/${f.id}: step $i also has a blank", s.contains("___"))
            }
            assertTrue("${c.id}/${f.id}: needs at least one answer", f.answers.isNotEmpty())
            assertFalse("${c.id}/${f.id}: a blank answer accepts everything", f.answers.any { it.isBlank() })
        }
    }

    @Test
    fun `every answer it lists is accepted`() {
        faded.forEach { (c, f) ->
            f.answers.forEach { a ->
                assertTrue("${c.id}/${f.id}: its own answer '$a' was rejected", f.accepts(a))
            }
        }
    }

    @Test
    fun `capitals, punctuation and surrounding words do not matter`() {
        faded.forEach { (c, f) ->
            val a = f.answers.first()
            assertTrue("${c.id}/${f.id}: capitals mattered", f.accepts(a.uppercase()))
            assertTrue("${c.id}/${f.id}: a full stop mattered", f.accepts("$a."))
            assertTrue("${c.id}/${f.id}: a sentence around it mattered", f.accepts("I think it is $a here"))
        }
    }

    @Test
    fun `an empty or unrelated answer is not accepted`() {
        faded.forEach { (c, f) ->
            assertFalse("${c.id}/${f.id}: empty was accepted", f.accepts(""))
            assertFalse("${c.id}/${f.id}: whitespace was accepted", f.accepts("   "))
            assertFalse(
                "${c.id}/${f.id}: unrelated prose was accepted",
                f.accepts("I went to the shops and bought some bread"),
            )
        }
    }

    @Test
    fun `a partial word does not answer for the whole one`() {
        // "cool" must not answer a step that wanted "cooldown", which is what
        // a plain substring match would allow.
        val f = Block.Faded(
            id = "t1", scenario = "s",
            steps = listOf("one", "the pause is the ___", "three"),
            blank = 1, answers = listOf("cooldown"), note = "n",
        )
        assertTrue(f.accepts("cooldown"))
        assertFalse("a prefix should not pass", f.accepts("cool"))
        assertFalse("a longer word should not pass", f.accepts("cooldowns"))
    }

    @Test
    fun `solved fills the blank with the first answer`() {
        faded.forEach { (c, f) ->
            val s = f.solved()
            assertFalse("${c.id}/${f.id}: the reveal still has a blank in it", s.contains("___"))
            assertTrue("${c.id}/${f.id}: the reveal should contain the answer", s.contains(f.answers.first()))
        }
    }

    @Test
    fun `ids are unique and nothing here is scheduled`() {
        val ids = faded.map { it.second.id }
        assertEquals(ids.size, ids.distinct().size)
        // Practice between reading a solution and writing one, not an
        // assessment: none of it feeds the spaced schedule.
        val scheduled = Courses.all.flatMap { it.allQuestions }.map { it.id }.toSet()
        ids.forEach { assertFalse("$it is scheduled", it in scheduled) }
    }
}
