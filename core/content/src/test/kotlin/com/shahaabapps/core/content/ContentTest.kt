package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentTest {

    private val courses = Courses.all

    @Test
    fun `ids are unique across modules, topics and questions`() {
        courses.forEach { c ->
            assertEquals(c.modules.size, c.modules.map { it.id }.toSet().size)
            val topics = c.modules.flatMap { it.topics }
            assertEquals(topics.size, topics.map { it.id }.toSet().size)
            val questions = c.modules.mapNotNull { it.quiz }.flatMap { it.questions } + c.modules.flatMap { it.topics }.flatMap { it.blocks }.filterIsInstance<Block.Check>().map { it.question }
            assertEquals(questions.size, questions.map { it.id }.toSet().size)
        }
    }

    @Test
    fun `every available module has at least ten questions that point at its own topics`() {
        courses.forEach { c ->
            c.modules.filter { it.available }.forEach { m ->
                val quiz = m.quiz
                assertTrue("${m.id} has no quiz", quiz != null)
                assertTrue("${m.id} has ${quiz!!.questions.size} questions", quiz.questions.size >= 10)
                val topicIds = m.topics.map { it.id }.toSet()
                quiz.questions.forEach { q ->
                    assertTrue("${q.id} points at ${q.topicId}", q.topicId in topicIds)
                    assertTrue("${q.id} has no correct answer", q.correct.isNotEmpty())
                    assertTrue("${q.id} correct index out of range", q.correct.all { it in q.options.indices })
                    // Richtig/Falsch is the real Lesen format, so two options are allowed there.
                    assertTrue("${q.id} needs 3+ options", q.options.size >= 3 || q.options == listOf("Richtig", "Falsch"))
                    assertTrue("${q.id} needs an explanation", q.explanation.length > 20)
                }
            }
        }
    }

    @Test
    fun `every topic ends with a recap and has at least one diagram`() {
        courses.forEach { c ->
            c.modules.flatMap { it.topics }.forEach { t ->
                assertTrue("${t.id} should end with a recap", t.blocks.last() is Block.Recap)
                assertTrue("${t.id} needs a diagram", t.blocks.any { it is Block.Diagram })
                assertTrue("${t.id} minutes", t.minutes in 5..60)
                assertTrue("${t.id} needs official references", t.references.isNotEmpty())
                t.references.forEach { r -> assertTrue("${t.id}: ${r.url}", r.url.startsWith("https://learn.microsoft.com/") || r.url.startsWith("https://www.goethe.de/") || r.url.startsWith("https://ielts.org/")) }
                assertTrue("${t.id} needs a glossary", t.blocks.any { it is Block.Glossary })
                t.blocks.filterIsInstance<Block.Check>().forEach { c -> assertTrue("${c.question.id} topic", c.question.topicId == t.id) }
            }
        }
    }

    @Test
    fun `diagram edges reference existing nodes`() {
        courses.forEach { c ->
            c.modules.flatMap { it.topics }.flatMap { it.blocks }.filterIsInstance<Block.Diagram>().forEach { d ->
                val ids = d.spec.nodes.map { it.id }.toSet()
                assertEquals(ids.size, d.spec.nodes.size)
                d.spec.edges.forEach { e ->
                    assertTrue("${d.spec.caption}: ${e.from}", e.from in ids)
                    assertTrue("${d.spec.caption}: ${e.to}", e.to in ids)
                }
                d.spec.nodes.forEach { n -> assertTrue("${d.spec.caption}: ${n.id} off grid", n.x in 0f..1f && n.y in 0f..1f) }
            }
        }
    }

    @Test
    fun `module components exist on the course goal in the seed`() {
        // AZ-204 skills seeded in core:seed: compute, storage, security, monitoring, connect, practice.
        val known = setOf("compute", "storage", "security", "monitoring", "connect", "practice")
        Az204.course.modules.forEach { m -> assertTrue(m.componentId, m.componentId in known) }
    }

    @Test
    fun `vocabulary ids are unique and exercises are well formed`() {
        courses.forEach { c ->
            val words = Courses.words(c)
            assertEquals(words.size, words.map { it.id }.toSet().size)
            words.forEach { w -> assertTrue(w.id, w.de.isNotBlank() && w.en.isNotBlank() && w.example.isNotBlank()) }
            val exercises = c.modules.flatMap { it.topics }.flatMap { it.blocks }.filterIsInstance<Block.Exercise>().map { it.exercise }
            assertEquals(exercises.size, exercises.map { it.id }.toSet().size)
            exercises.forEach { e ->
                when (e) {
                    is Ex.Gap -> { assertTrue(e.id, e.sentence.contains("___")); assertTrue(e.id, e.answers.isNotEmpty()) }
                    is Ex.Reorder -> assertEquals(e.id, e.pieces.sorted(), e.answer.split(" ").sorted())
                    is Ex.Match -> assertTrue(e.id, e.pairs.size >= 3)
                    is Ex.Writing -> assertTrue(e.id, e.points.isNotEmpty() && e.model.isNotBlank())
                }
            }
        }
    }
}
