package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * A board is keyed to its episode by line number and to its diagrams by node
 * id, and both are typed by hand. These hold every number and id to what
 * exists, so a script edit that drops a line, or a renamed node, fails here
 * rather than lighting nothing on the phone. The geometry of the diagrams is
 * DiagramTest's.
 */
class PodcastBoardTest {

    private val boards = PodcastBoards.all

    @Test
    fun `every board belongs to an episode`() {
        assertTrue("expected at least one board", boards.isNotEmpty())
        boards.forEach { b -> assertTrue("${b.topicId} has a board but no episode", Podcasts.forTopic(b.topicId) != null) }
        assertEquals("a topic has two boards", boards.size, boards.map { it.topicId }.toSet().size)
    }

    @Test
    fun `scenes start at the first line and run in order inside the episode`() {
        boards.forEach { b ->
            val lines = Podcasts.forTopic(b.topicId)!!.lines.size
            assertEquals("${b.topicId}: the first scene must start at line 0", 0, b.scenes.first().fromLine)
            b.scenes.zipWithNext().forEach { (x, y) -> assertTrue("${b.topicId}: scene at ${y.fromLine} is not after ${x.fromLine}", y.fromLine > x.fromLine) }
            b.scenes.forEach { s -> assertTrue("${b.topicId}: scene at ${s.fromLine} is past the last line", s.fromLine < lines) }
        }
    }

    @Test
    fun `every beat falls inside its scene, one a line, in order`() {
        boards.forEach { b ->
            val lines = Podcasts.forTopic(b.topicId)!!.lines.size
            b.scenes.forEachIndexed { i, s ->
                val end = b.scenes.getOrNull(i + 1)?.fromLine ?: lines
                s.beats.forEach { beat ->
                    assertTrue("${b.topicId} '${s.heading}': beat at line ${beat.atLine} is outside ${s.fromLine}..${end - 1}", beat.atLine in s.fromLine until end)
                }
                s.beats.zipWithNext().forEach { (x, y) -> assertTrue("${b.topicId} '${s.heading}': beats at ${x.atLine} and ${y.atLine} out of order", y.atLine > x.atLine) }
            }
        }
    }

    @Test
    fun `every id names a node that is on the board by then`() {
        boards.forEach { b ->
            b.scenes.forEach { s ->
                val ids = s.diagram.nodes.map { it.id }.toSet()
                s.beats.forEach { beat ->
                    val where = "${b.topicId} '${s.heading}' line ${beat.atLine}"
                    (beat.show + beat.light).forEach { assertTrue("$where: no node '$it'", it in ids) }
                    val shown = b.frameAt(beat.atLine).shown
                    beat.light.forEach { assertTrue("$where: lights '$it' before it is shown", it in shown) }
                    beat.pulse?.let { (from, to) ->
                        assertTrue("$where: pulse from '$from' before it is shown", from in shown)
                        assertTrue("$where: pulse to '$to' before it is shown", to in shown)
                        val joined = s.diagram.edges.any { (it.from == from && it.to == to) || (it.from == to && it.to == from) }
                        assertTrue("$where: pulse runs $from to $to with no edge between them", joined)
                    }
                }
            }
        }
    }

    @Test
    fun `words fit the board`() {
        boards.forEach { b ->
            b.scenes.forEach { s ->
                assertTrue("${b.topicId}: heading too long for one line: '${s.heading}'", s.heading.length <= 32)
                s.beats.mapNotNull { it.card }.forEach { c ->
                    assertTrue("${b.topicId}: card title too long: '${c.title}'", c.title.isNotBlank() && c.title.length <= 36)
                    assertTrue("${b.topicId}: card body too long for two lines: '${c.body}'", c.body.isNotBlank() && c.body.length <= 90)
                }
            }
        }
    }

    @Test
    fun `a frame shows what has been said so far`() {
        val kv = PodcastBoards.forTopic("az-keyvault")!!
        val start = kv.frameAt(null)
        assertEquals("The question", start.scene.heading)
        assertEquals(setOf("app", "vault"), start.shown)
        assertEquals("app" to "vault", start.pulse)

        val keys = kv.frameAt(8)
        assertEquals("What the vault holds", keys.scene.heading)
        assertTrue(keys.shown.containsAll(setOf("vault", "secrets", "keys", "data")))
        assertTrue("certs" !in keys.shown)
        assertTrue("tier" !in keys.shown)
        assertEquals(setOf("keys", "data"), keys.lit)
        assertEquals("data" to "keys", keys.pulse)
    }

    @Test
    fun `a pulse runs only on its own line and a card stays until replaced`() {
        val kv = PodcastBoards.forTopic("az-keyvault")!!
        // Line 9 (the notary) has no beat: line 8's card holds, with no pulse.
        assertEquals("Keys never leave", kv.frameAt(9).card?.title)
        assertNull(kv.frameAt(9).pulse)
        // Line 19 has no beat: line 18's card is still up.
        assertEquals("Managed identity", kv.frameAt(19).card?.title)
        assertNull(kv.frameAt(19).pulse)
    }
}
