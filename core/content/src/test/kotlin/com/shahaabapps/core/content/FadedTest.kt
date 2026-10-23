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

    /**
     * Where a fade is required. AZ-204 is every topic: its topics are long
     * prose that ends in a problem. The other two are the topics whose job is
     * to produce something — a letter, a report, an essay, a spoken turn —
     * because that is where reading a model and writing one are furthest
     * apart. Naming them means a topic added to those modules later has to be
     * written up to the rule rather than quietly skipping it.
     */
    private val mustFade: List<Pair<Course, Topic>> =
        Az204.course.allTopics.map { Az204.course to it } +
            Ai200.course.allTopics.map { Ai200.course to it } +
            Az305.course.allTopics.map { Az305.course to it } +
            Psm1.course.allTopics.map { Psm1.course to it } +
            GermanA1.course.allTopics
                .filter { it.id.endsWith("-practice") || it.id == "de11-forms" || it.id == "de11-messages" }
                .map { GermanA1.course to it } +
            Ielts.course.allTopics
                .filter { it.id.startsWith("ie4-") || it.id.startsWith("ie5-") || it.id.startsWith("ie6-") || it.id.startsWith("ie7-") }
                .map { Ielts.course to it }

    @Test
    fun `every topic that should fade a worked example does, exactly once`() {
        // The counts are asserted so that a filter that silently stops
        // matching cannot turn this test into a no-op.
        assertEquals("az-204 topics", 22, mustFade.count { it.first.id == Az204.course.id })
        assertEquals("ai-200 topics", 19, mustFade.count { it.first.id == Ai200.course.id })
        assertEquals("az-305 topics", 14, mustFade.count { it.first.id == Az305.course.id })
        assertEquals("psm-1 topics", 15, mustFade.count { it.first.id == Psm1.course.id })
        assertEquals("German writing topics", 12, mustFade.count { it.first.id == GermanA1.course.id })
        assertEquals("IELTS writing and speaking topics", 13, mustFade.count { it.first.id == Ielts.course.id })
        mustFade.forEach { (c, t) ->
            val n = t.blocks.filterIsInstance<Block.Faded>().size
            assertEquals("${c.id}/${t.id} should fade once", 1, n)
        }
    }

    @Test
    fun `the faded step follows the worked example it fades`() {
        // The fade only reads as one movement if it is next to the solution it
        // takes a step out of. At the end of the topic it is just another gap.
        mustFade.forEach { (c, t) ->
            val fade = t.blocks.indexOfFirst { it is Block.Faded }
            val worked = t.blocks.withIndex()
                .lastOrNull { (i, b) -> i < fade && b is Block.Callout && b.kind == CalloutKind.WORKED }
                ?.index ?: -1
            assertTrue("${c.id}/${t.id} should have a worked example before its fade", worked >= 0)
            assertEquals("${c.id}/${t.id}: the fade should come straight after it", worked + 1, fade)
        }
    }

    @Test
    fun `the blank is a real step and it has a blank in it`() {
        faded.forEach { (c, f) ->
            assertTrue("${c.id}/${f.id}: blank ${f.blank} is outside the steps", f.blank in f.steps.indices)
            // One hole, because the screen draws one field: a step with two
            // holes asks for two answers and takes one.
            assertEquals(
                "${c.id}/${f.id}: the blank step should have exactly one ___",
                1, Regex("_{3,}").findAll(f.steps[f.blank]).count(),
            )
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
