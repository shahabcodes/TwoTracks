package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
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
            val questions = c.allQuestions
            assertEquals(questions.size, questions.map { it.id }.toSet().size)
        }
    }

    @Test
    fun `every course with a mock exam has one that points at real topics`() {
        courses.filter { it.mockExam != null }.forEach { c ->
            val ids = c.allTopics.map { it.id }.toSet()
            val m = c.mockExam!!
            assertTrue("${c.id} mock timing", m.timeLimitMinutes != null && !m.instantFeedback)
            // A randomised mock needs a pool bigger than the draw, or a retake is the same paper.
            val draw = m.drawCount
            assertTrue("${c.id} mock pool ${m.questions.size} vs draw $draw", draw != null && m.questions.size >= draw + 10)
            repeat(3) { assertEquals(draw, m.draw(seed = it.toLong()).size) }
            assertTrue("${c.id} two draws should differ", m.draw(seed = 1L).map { q -> q.id } != m.draw(seed = 2L).map { q -> q.id })
            m.questions.forEach { q -> assertTrue("${q.id} points at ${q.topicId}", q.topicId in ids) }
        }
    }

    @Test
    fun `the mock exam covers every module and points at real topics`() {
        val mock = Az204.course.mockExam!!
        val topicIds = Az204.course.allTopics.map { it.id }.toSet()
        assertTrue("mock has ${mock.questions.size}", mock.questions.size >= 50)
        assertTrue(mock.timeLimitMinutes != null && !mock.instantFeedback)
        mock.questions.forEach { q ->
            assertTrue("${q.id} points at ${q.topicId}", q.topicId in topicIds)
            assertTrue("${q.id} options", q.options.size >= 3 && q.correct.all { it in q.options.indices } && q.correct.isNotEmpty())
            assertTrue("${q.id} explanation", q.explanation.length > 20)
        }
        Az204.course.modules.forEach { m ->
            val ids = m.topics.map { it.id }.toSet()
            assertTrue("mock has no question for ${m.id}", mock.questions.any { it.topicId in ids })
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
        // IELTS goals are seeded with the four paper components only.
        val ielts = setOf("listening", "reading", "writing", "speaking")
        Ielts.course.modules.forEach { m -> assertTrue(m.componentId, m.componentId in ielts) }
    }

    @Test
    fun `every topic asks the reader to do something`() {
        // A topic that only explains is a page of a textbook. Each one must
        // carry at least one check or exercise, so reading is interrupted by
        // recalling. AZ-204 shipped with none of the latter; this stops that
        // happening again, in any course.
        courses.forEach { c ->
            c.allTopics.forEach { t ->
                val interactive = t.blocks.count { it is Block.Check || it is Block.Exercise }
                assertTrue("${c.id}/${t.id} has nothing to do, only prose", interactive > 0)
            }
        }
    }

    @Test
    fun `AZ-204 asks three times in every topic`() {
        // One check at the end of a 28-minute read is a reading comprehension
        // question, not retrieval practice. Three, spread through the topic,
        // mean each section is recalled before the next one starts.
        //
        // AZ-204 only, for now: its topics are long prose, where German's and
        // IELTS's are short and carry exercises of their own instead.
        Az204.course.allTopics.forEach { t ->
            val checks = t.blocks.count { it is Block.Check }
            assertTrue("az-204/${t.id} has $checks check(s), wanted 3 or more", checks >= 3)
        }
    }

    @Test
    fun `every AZ-204 module test question explains the answer you picked`() {
        // A test that only restates why the right answer is right teaches the
        // person who was already right. The useful sentence is about the
        // option you chose, so every question in a module test carries one.
        //
        // Module tests only: the mock exam runs with instantFeedback off, so
        // a note on a mock question would never be rendered. Writing content
        // nothing can show is how 44 notes sat invisible until 0.43.0.
        Az204.course.modules.mapNotNull { m -> m.quiz?.let { m to it } }.forEach { (m, quiz) ->
            assertTrue("${m.id} should give instant feedback for notes to appear", quiz.instantFeedback)
            quiz.questions.forEach { q ->
                val wrong = q.options.indices.filterNot { it in q.correct }
                assertTrue(
                    "az-204/${m.id}/${q.id} has no note on any wrong option",
                    wrong.any { it in q.whyWrong.keys },
                )
            }
        }
    }

    @Test
    fun `a warm-up guess never enters the schedule or the review set`() {
        // The one thing that would make warm-ups harmful. A guess made before
        // reading, recorded, would put every topic straight into the review
        // set and teach the schedule that you do not know what you have not
        // read yet. allQuestions is what feeds both, so it must not be there.
        val scheduled = Courses.all.flatMap { it.allQuestions }.map { it.id }.toSet()
        val warmups = Courses.all.flatMap { c ->
            c.allTopics.flatMap { t -> t.blocks.filterIsInstance<Block.Warmup>().map { it.question } }
        }
        assertTrue("expected warm-ups to exist", warmups.isNotEmpty())
        warmups.forEach { w ->
            assertFalse("${w.id} is scheduled; a guess before reading must not be", w.id in scheduled)
        }
    }

    @Test
    fun `every topic of a warmed-up course opens with one guess, before any prose`() {
        // Named rather than blanket, so bringing a course up to this is a
        // deliberate act: add it here and the rule starts holding it.
        // IELTS has none yet.
        listOf(Az204.course, GermanA1.course).forEach { course -> warmupsHold(course) }
    }

    private fun warmupsHold(course: Course) {
        course.allTopics.forEach { t ->
            val warmups = t.blocks.filterIsInstance<Block.Warmup>()
            assertEquals("${course.id}/${t.id} should open with one guess", 1, warmups.size)
            assertTrue(
                "${course.id}/${t.id}: the guess must come first, or it is not a guess",
                t.blocks.first() is Block.Warmup,
            )
            // A warm-up points at the reading; it does not do the reading's job.
            val w = warmups.single().question
            assertTrue("${course.id}/${t.id}: the warm-up needs its own topic id", w.topicId == t.id)
            assertTrue("${course.id}/${t.id}: the hint should be a pointer, not an explanation", w.explanation.length < 160)
        }
    }

    @Test
    fun `picking a wrong option reaches the note written about it`() {
        // The notes are authored by option index and B.check rotates the
        // options, so this walks the path the screen walks: pick an option,
        // ask the question for its note. 44 notes shipped unreachable once
        // because the in-topic check never asked.
        val checks = Courses.all.flatMap { c ->
            c.allTopics.flatMap { t -> t.blocks.filterIsInstance<Block.Check>().map { c.id to it.question } }
        }
        val withNotes = checks.filter { it.second.whyWrong.isNotEmpty() }
        assertTrue("expected checks carrying notes, saw ${withNotes.size}", withNotes.size > 40)
        withNotes.forEach { (courseId, q) ->
            q.whyWrong.forEach { (i, note) ->
                assertEquals("$courseId/${q.id}: option $i should answer with its own note", note, q.whyWrongFor(setOf(i)))
            }
            q.correct.forEach { i ->
                assertNull("$courseId/${q.id}: the right answer has nothing to excuse", q.whyWrongFor(setOf(i)))
            }
        }
    }

    @Test
    fun `the answer to a check is not always in the same place`() {
        // The first round of AZ-204 checks put the answer second 21 times out
        // of 22, which is a position worth guessing rather than knowing. B.check
        // rotates by the question id, so this watches that it stays spread.
        val checks = courses.flatMap { c ->
            c.allTopics.flatMap { t -> t.blocks.filterIsInstance<Block.Check>().map { it.question } }
        }.filter { it.options.size >= 3 && !it.multi }
        assertTrue("expected a decent number of checks, saw ${checks.size}", checks.size > 40)
        val bySlot = checks.groupingBy { it.correct.first() }.eachCount()
        val commonest = bySlot.values.max()
        assertTrue(
            "the answer sits in one slot ${commonest} of ${checks.size} times: $bySlot",
            commonest < checks.size * 2 / 3,
        )
    }

    @Test
    fun `a wrong-option note is attached to an option that exists and is wrong`() {
        // whyWrong is keyed by index, and B.spread rotates those keys. A note
        // pointing at the right answer, or past the end of the list, would
        // explain the wrong thing to someone who is already wrong.
        courses.forEach { c ->
            val questions = c.allTopics.flatMap { t -> t.blocks.filterIsInstance<Block.Check>().map { it.question } } +
                c.modules.mapNotNull { it.quiz }.flatMap { it.questions } +
                listOfNotNull(c.mockExam).flatMap { it.questions }
            questions.forEach { q ->
                q.whyWrong.keys.forEach { i ->
                    assertTrue("${c.id}/${q.id}: note on option $i, which is out of range", i in q.options.indices)
                    assertTrue("${c.id}/${q.id}: note on option $i, which is a correct answer", i !in q.correct)
                }
            }
        }
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
                    is Ex.Writing -> {
                        assertTrue(e.id, e.points.isNotEmpty() && e.model.isNotBlank())
                        assertTrue("${e.id} keywords per point", e.keywords.isEmpty() || (e.keywords.size == e.points.size && e.keywords.all { it.isNotEmpty() }))
                        if (e.keywords.isNotEmpty()) assertTrue("${e.id} model must pass its own marker", WritingMarker.mark(e, e.model).points.all { it })
                    }
                    is Ex.Listen -> assertTrue(e.id, e.audio.isNotBlank() && e.options.size >= 2 && e.correct in e.options.indices)
                    is Ex.Speak -> assertTrue(e.id, e.target.isNotBlank() && e.en.isNotBlank())
                }
            }
        }
    }
}
