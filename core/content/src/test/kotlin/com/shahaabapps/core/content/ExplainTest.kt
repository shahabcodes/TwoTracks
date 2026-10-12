package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * "In your own words" is marked by keyword, which is a blunt instrument: too
 * loose and anything passes, too tight and a correct answer in different words
 * fails. These pin both ends.
 *
 * The strongest check is that the model answer scores full marks against its
 * own keywords. If it does not, the keywords describe something the topic never
 * said.
 */
class ExplainTest {

    private val explains: List<Pair<Course, Ex.Writing>> = Courses.all.flatMap { c ->
        c.allTopics.flatMap { t -> t.blocks.filterIsInstance<Block.Exercise>().map { it.exercise } }
            .filterIsInstance<Ex.Writing>()
            .filter { it.form == WritingForm.EXPLANATION }
            .map { c to it }
    }

    @Test
    fun `every AZ-204 and IELTS topic asks for an explanation, exactly once`() {
        // German is the exception, and deliberately: it already ends its
        // topics by asking you to produce German, which is the thing being
        // learnt. Explaining a German rule in English is a different skill
        // and not one the exam tests. AZ-204 and IELTS are both courses about
        // method, where saying the method back is exactly the point.
        listOf(Az204.course, Ielts.course).forEach { c ->
            c.allTopics.forEach { t ->
                val n = t.blocks.filterIsInstance<Block.Exercise>().map { it.exercise }
                    .filterIsInstance<Ex.Writing>().count { it.form == WritingForm.EXPLANATION }
                assertEquals("${c.id}/${t.id} should ask once", 1, n)
            }
        }
    }

    @Test
    fun `the model answer scores full marks against its own keywords`() {
        assertTrue("expected explanations to exist", explains.isNotEmpty())
        explains.forEach { (c, ex) ->
            val r = WritingMarker.mark(ex, ex.model)
            val missing = ex.points.filterIndexed { i, _ -> !r.points[i] }
            assertTrue(
                "${c.id}/${ex.id}: the model answer misses its own points $missing",
                missing.isEmpty(),
            )
            assertTrue("${c.id}/${ex.id}: the model answer is shorter than it asks for", r.longEnough)
        }
    }

    @Test
    fun `an answer about something else scores nothing`() {
        // Guards the other end: keywords loose enough to match any prose would
        // make the whole exercise a participation award.
        val unrelated = """
            I went to the shops this morning and bought some bread and milk.
            The weather was cold but bright, and the bus was late as usual.
            Afterwards I sat in the kitchen and read the paper for a while.
        """.trimIndent()
        explains.forEach { (c, ex) ->
            val r = WritingMarker.mark(ex, unrelated)
            assertEquals("${c.id}/${ex.id}: unrelated prose covered ${r.covered} point(s)", 0, r.covered)
        }
    }

    @Test
    fun `an explanation is judged on its ideas, not its form`() {
        // A greeting and a sign-off are nonsense here, and four paragraphs
        // would be too; the only form check left is the length.
        explains.forEach { (c, ex) ->
            val r = WritingMarker.mark(ex, ex.model)
            assertEquals("${c.id}/${ex.id}: only the word count should be checked", 1, r.checks.size)
            assertTrue(r.checks.single().label.contains("words"))
        }
    }

    @Test
    fun `each explanation names at least three ideas, each with alternative wordings`() {
        explains.forEach { (c, ex) ->
            assertTrue("${c.id}/${ex.id}: wanted three ideas, had ${ex.points.size}", ex.points.size >= 3)
            assertEquals("${c.id}/${ex.id}: every idea needs its keywords", ex.points.size, ex.keywords.size)
            ex.keywords.forEachIndexed { i, ks ->
                assertTrue(
                    "${c.id}/${ex.id}: idea '${ex.points[i]}' has ${ks.size} wording(s); one is too strict",
                    ks.size >= 2,
                )
                assertFalse("${c.id}/${ex.id}: a blank keyword matches everything", ks.any { it.isBlank() })
            }
        }
    }

    @Test
    fun `ids are unique across every course`() {
        val ids = explains.map { it.second.id }
        assertEquals(ids.size, ids.distinct().size)
    }
}
