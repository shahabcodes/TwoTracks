package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * A notes sheet is revision material, which means its failure mode is not
 * being wrong but being thin: a list of product names with no definitions, or
 * commands with no situations beside them, is a page nobody can revise from
 * and nobody notices is useless until the exam.
 *
 * So the shape is a test. Every sheet carries the words, the picture, the
 * choice, the commands and the recap, and a command carries the moment it is
 * the right command — the part the exam actually asks about.
 *
 * The geometry of a sheet's diagrams is not checked here: [DiagramTest] reads
 * them with every other diagram in the app, which is the point of putting
 * them in [Block.Diagram] rather than inventing a second picture type.
 */
class NotesTest {

    private val sheets = Notes.all
    private val topicIds = Courses.all.flatMap { it.allTopics }.map { it.id }.toSet()

    @Test
    fun `there are sheets to check`() {
        assertTrue("expected notes sheets to exist", sheets.size >= 5)
    }

    @Test
    fun `a sheet belongs to a topic that exists, exactly once`() {
        sheets.forEach { s ->
            assertTrue("notes for unknown topic '${s.topicId}'", s.topicId in topicIds)
        }
        val ids = sheets.map { it.topicId }
        assertEquals("two sheets for the same topic: ${ids.groupBy { it }.filterValues { it.size > 1 }.keys}", ids.size, ids.toSet().size)
    }

    @Test
    fun `a headline says what the topic is, in a sentence`() {
        sheets.forEach { s ->
            // Long enough to be a claim about the topic rather than its title
            // again, which is what the first drafts of these were.
            assertTrue("${s.topicId}: headline is too short to say anything: \"${s.headline}\"", s.headline.length > 70)
            assertTrue("${s.topicId}: headline is not one line", !s.headline.contains("\n"))
        }
    }

    @Test
    fun `a sheet defines the words the exam leans on`() {
        sheets.forEach { s ->
            assertTrue("${s.topicId}: ${s.definitions.size} definition(s), expected at least four", s.definitions.size >= 4)
            s.definitions.forEach { t ->
                assertTrue("${s.topicId} [${t.term}]: a definition, not a label", t.meaning.length > 30)
                assertTrue("${s.topicId}: an unnamed term", t.term.isNotBlank())
            }
        }
    }

    @Test
    fun `a sheet carries a picture and the table that settles a choice`() {
        sheets.forEach { s ->
            assertTrue("${s.topicId}: no diagram", s.pictures >= 1)
            assertTrue("${s.topicId}: no table or comparison", s.tables >= 1)
        }
    }

    @Test
    fun `a sheet ends with a recap`() {
        sheets.forEach { s ->
            val last = s.blocks.lastOrNull()
            assertTrue("${s.topicId}: the last block is ${last?.javaClass?.simpleName}, not a recap", last is Block.Recap)
        }
    }

    /**
     * The courses whose sheets carry commands. Named, rather than inferred, so
     * that bringing a fourth course up to this rule is a deliberate act — the
     * convention the rest of this module's tests follow. German and IELTS are
     * absent on purpose: there is no command to type for either.
     */
    private val commandCourses = setOf("ai-200", "az-204")

    @Test
    fun `an Azure sheet carries commands, each with when and why`() {
        val azure = sheets.filter { s -> courseOf(s.topicId) in commandCourses }
        assertTrue("expected sheets in $commandCourses", azure.isNotEmpty())
        azure.forEach { s ->
            assertTrue("${s.topicId}: ${s.commands.size} command(s), expected at least three", s.commands.size >= 3)
            s.commands.forEach { c ->
                assertTrue(
                    "${s.topicId}: \"${c.command}\" is not something you can type",
                    TOOLS.any { c.command.startsWith("$it ") },
                )
                // The whole reason the three are stored apart: a command with
                // no situation beside it is the thing this sheet exists to fix.
                assertTrue("${s.topicId} [${c.command}]: 'when' does not describe a moment", c.use.length > 20)
                assertTrue("${s.topicId} [${c.command}]: 'why' does not explain anything", c.why.length > 40)
                assertTrue("${s.topicId} [${c.command}]: 'when' should read as a situation", c.use.endsWith("."))
            }
        }
    }

    /**
     * The courses whose every topic has a sheet. Named, not inferred, for the
     * same reason: a fifth course is brought up to this rule deliberately or
     * not at all. German and IELTS have no sheets yet and are absent here
     * rather than silently exempt — the lesson screen simply offers no notes
     * link for them.
     */
    private val coveredCourses = setOf("ai-200", "az-204")

    @Test
    fun `every topic in a covered course has a sheet`() {
        val have = sheets.map { it.topicId }.toSet()
        val missing = Courses.all
            .filter { it.id in coveredCourses }
            .flatMap { c -> c.allTopics.map { c.id to it } }
            .filter { (_, t) -> t.id !in have }
            .map { (cid, t) -> "$cid/${t.id}" }
        assertTrue("${missing.size} topic(s) with no notes sheet: $missing", missing.isEmpty())
    }

    @Test
    fun `a sheet names the traps`() {
        sheets.forEach { s ->
            val mistakes = s.blocks.filterIsInstance<Block.Callout>().filter { it.kind == CalloutKind.MISTAKES }
            assertTrue("${s.topicId}: no mistakes block", mistakes.isNotEmpty())
        }
    }

    /** Which course a topic belongs to, for the rules scoped to a course. */
    private fun courseOf(topicId: String): String? =
        Courses.all.firstOrNull { c -> c.allTopics.any { it.id == topicId } }?.id

    private companion object {
        /** What a line on a sheet may start with: things that exist on a command line. */
        val TOOLS = listOf("az", "kubectl", "docker", "dotnet", "func", "helm", "psql", "sqlcmd", "redis-cli")
    }
}
