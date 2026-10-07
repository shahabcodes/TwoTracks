package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScoreTest {

    private val course = Az204.course

    /** A paper covering the whole course, two questions per module, taken from the mock pool. */
    private fun wholeCourse(): List<Question> =
        course.modules.filter { it.available }.flatMap { m ->
            val topicIds = m.topics.map { it.id }.toSet()
            course.mockExam!!.questions.filter { it.topicId in topicIds }.take(2)
        }

    @Test
    fun `a course-wide paper is grouped by module in course order`() {
        val asked = wholeCourse()
        val groups = course.breakdown(asked, asked.map { it.id }.toSet(), ScoreBy.MODULE)
        assertEquals(course.modules.filter { it.available }.map { it.id }, groups.map { it.id })
        assertTrue(groups.all { it.clean && it.percent == 100 })
        assertEquals(asked.size, groups.sumOf { it.asked })
    }

    @Test
    fun `a module test is grouped by topic`() {
        val module = course.modules.first { it.quiz != null }
        val asked = module.quiz!!.questions
        val groups = course.breakdown(asked, emptySet(), ScoreBy.TOPIC)
        assertTrue(groups.isNotEmpty())
        // Every area belongs to the module under test, and keeps the module's topic order.
        val order = module.topics.map { it.id }
        assertEquals(groups.map { it.id }, order.filter { it in groups.map { g -> g.id } })
        assertTrue(groups.all { it.correct == 0 && !it.clean })
    }

    @Test
    fun `an area the paper never asked about is left out`() {
        val module = course.modules.first { it.quiz != null }
        val asked = module.quiz!!.questions
        val groups = course.breakdown(asked, emptySet(), ScoreBy.MODULE)
        assertEquals(listOf(module.id), groups.map { it.id })
    }

    @Test
    fun `questions asked but never answered count as missed, not as unasked`() {
        val asked = wholeCourse()
        // The clock ran out after the first question: everything else is missed.
        val groups = course.breakdown(asked, setOf(asked.first().id), ScoreBy.MODULE)
        assertEquals(asked.size, groups.sumOf { it.asked })
        assertEquals(1, groups.sumOf { it.correct })
        assertEquals(1, groups.first().correct)
    }

    @Test
    fun `a question whose topic is not in the course is ignored`() {
        val asked = wholeCourse()
        val stray = asked.first().copy(id = "stray", topicId = "not-a-topic")
        val groups = course.breakdown(asked + stray, asked.map { it.id }.toSet(), ScoreBy.MODULE)
        assertEquals(asked.size, groups.sumOf { it.asked })
    }

    @Test
    fun `an empty paper has no areas`() {
        assertEquals(emptyList<ScoreGroup>(), course.breakdown(emptyList(), emptySet(), ScoreBy.MODULE))
    }

    @Test
    fun `percent floors and a part-right area is not clean`() {
        assertEquals(33, ScoreGroup("a", "A", correct = 1, asked = 3).percent)
        assertEquals(0, ScoreGroup("a", "A", correct = 0, asked = 0).percent)
        assertTrue(!ScoreGroup("a", "A", correct = 1, asked = 3).clean)
        assertTrue(!ScoreGroup("a", "A", correct = 0, asked = 0).clean)
        assertTrue(ScoreGroup("a", "A", correct = 3, asked = 3).clean)
    }

    @Test
    fun `weakest lists the areas under the pass mark, worst first`() {
        val groups = listOf(
            ScoreGroup("a", "A", correct = 9, asked = 10),
            ScoreGroup("b", "B", correct = 2, asked = 10),
            ScoreGroup("c", "C", correct = 5, asked = 10),
        )
        assertEquals(listOf("b", "c"), groups.weakest(passPercent = 80).map { it.id })
        assertEquals(emptyList<String>(), groups.weakest(passPercent = 0).map { it.id })
    }
}
