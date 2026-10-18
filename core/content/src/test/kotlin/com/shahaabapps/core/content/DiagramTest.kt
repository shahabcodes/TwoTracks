package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Diagrams are hand-placed on a unit grid, which means a diagram is a small
 * piece of geometry somebody typed, and the usual mistakes are geometric: a
 * box whose edge falls outside the canvas, two boxes on top of each other, an
 * edge naming a node that was renamed.
 *
 * The third rule here is the one that caused the trouble. A label is laid out
 * inside its box, so a long one in a small box either shrinks to a smudge or,
 * before [DiagramView] learned to constrain its height, spilled straight over
 * the shape's edges. [smallestReadableSp] is a deliberately rough model of
 * that layout — rough because the real measurement needs Android, and enough
 * because it only has to catch a box that is plainly too small for its words.
 */
class DiagramTest {

    private val diagrams: List<Triple<String, String, DiagramSpec>> = Courses.all.flatMap { c ->
        c.allTopics.flatMap { t ->
            t.blocks.filterIsInstance<Block.Diagram>().map { Triple(c.id, t.id, it.spec) }
        }
    } + PodcastBoards.all.flatMap { b ->
        // The podcast boards draw the same specs, so they are held to the same geometry.
        b.scenes.map { s -> Triple("podcast", "${b.topicId} '${s.heading}'", s.diagram) }
    }

    @Test
    fun `there are diagrams to check`() {
        assertTrue("expected the courses to carry diagrams", diagrams.size > 50)
    }

    @Test
    fun `every node sits inside the canvas`() {
        diagrams.forEach { (courseId, topicId, spec) ->
            spec.nodes.forEach { n ->
                val where = "$courseId/$topicId [${n.id}]"
                assertTrue("$where: left edge at ${n.x - n.w / 2}", n.x - n.w / 2 >= -0.001f)
                assertTrue("$where: right edge at ${n.x + n.w / 2}", n.x + n.w / 2 <= 1.001f)
                assertTrue("$where: top edge at ${n.y - n.h / 2}", n.y - n.h / 2 >= -0.001f)
                assertTrue("$where: bottom edge at ${n.y + n.h / 2}", n.y + n.h / 2 <= 1.001f)
            }
        }
    }

    @Test
    fun `no two nodes overlap`() {
        diagrams.forEach { (courseId, topicId, spec) ->
            for (i in spec.nodes.indices) {
                for (j in i + 1 until spec.nodes.size) {
                    val a = spec.nodes[i]
                    val b = spec.nodes[j]
                    // A USER's label is drawn below its shape, so two of them
                    // side by side are allowed to sit closer than their boxes.
                    if (a.kind == NodeKind.USER || b.kind == NodeKind.USER) continue
                    val apart = kotlin.math.abs(a.x - b.x) >= (a.w + b.w) / 2 - 0.002f ||
                        kotlin.math.abs(a.y - b.y) >= (a.h + b.h) / 2 - 0.002f
                    assertTrue("$courseId/$topicId: [${a.id}] and [${b.id}] overlap", apart)
                }
            }
        }
    }

    @Test
    fun `every edge joins two nodes that exist`() {
        diagrams.forEach { (courseId, topicId, spec) ->
            val ids = spec.nodes.map { it.id }.toSet()
            assertEquals("$courseId/$topicId has duplicate node ids", spec.nodes.size, ids.size)
            spec.edges.forEach { e ->
                assertTrue("$courseId/$topicId: edge from unknown node '${e.from}'", e.from in ids)
                assertTrue("$courseId/$topicId: edge to unknown node '${e.to}'", e.to in ids)
                assertTrue("$courseId/$topicId: edge joins ${e.from} to itself", e.from != e.to)
            }
        }
    }

    @Test
    fun `every label fits its box at a readable size`() {
        // The whole point of the exercise: 100 of 404 nodes could not hold
        // their own labels, which is what put words outside the shapes.
        val cramped = diagrams.flatMap { (courseId, topicId, spec) ->
            spec.nodes.mapNotNull { n ->
                val sp = smallestReadableSp(n, spec.aspect)
                if (sp >= MIN_SP) null else "$courseId/$topicId [${n.id}] would need ${sp}sp: \"${n.label}\""
            }
        }
        assertTrue("${cramped.size} node(s) too small for their words:\n" + cramped.joinToString("\n"), cramped.isEmpty())
    }

    @Test
    fun `a caption says what the picture means`() {
        diagrams.forEach { (courseId, topicId, spec) ->
            assertTrue("$courseId/$topicId has no caption", spec.caption.isNotBlank())
            assertTrue("$courseId/$topicId: the caption is a label, not a sentence", spec.caption.length > 25)
            assertTrue("$courseId/$topicId is a diagram with no nodes", spec.nodes.isNotEmpty())
            assertTrue("$courseId/$topicId: aspect ${spec.aspect} is outside anything sensible", spec.aspect in 0.2f..1.3f)
        }
    }

    private companion object {
        /** Below this the type is a grey smudge rather than a label. */
        const val MIN_SP = 9f

        /** The width a diagram gets inside a card on a phone, in dp. */
        const val CARD_DP = 310f

        /**
         * The fraction of a node's box its text may use, per shape. These
         * mirror [com.shahaabapps.twotracks.feature.learn.DiagramView]: a box
         * is nearly all usable, a cylinder's body is inset by its end caps,
         * and a cloud is only solid near its middle.
         */
        fun fractions(kind: NodeKind): Pair<Float, Float> = when (kind) {
            NodeKind.CYLINDER -> 0.84f to 0.68f
            NodeKind.CLOUD -> 0.60f to 0.50f
            NodeKind.USER -> 1.35f to 0.52f
            else -> 0.90f to 0.86f
        }

        private fun fitsAt(text: String, sp: Float, boxW: Float, boxH: Float, semibold: Boolean): Boolean {
            val charW = sp * if (semibold) 0.55f else 0.52f
            val perLine = (boxW / charW).toInt().coerceAtLeast(1)
            // Counting characters assumes wrapping falls wherever it likes,
            // which is the optimistic case: real wrapping breaks at spaces and
            // needs at least as many lines.
            val lines = (text.length + perLine - 1) / perLine
            return lines * sp * 1.3f <= boxH
        }

        /**
         * The largest size, down to 7sp, at which this node's label and
         * sub-label both fit; 0 if neither ever does.
         */
        fun smallestReadableSp(n: DNode, aspect: Float): Float {
            val (wFrac, hFrac) = fractions(n.kind)
            val boxW = n.w * CARD_DP * wFrac
            val boxH = n.h * CARD_DP * aspect * hFrac
            val labelH = if (n.sub != null) boxH * 0.62f else boxH
            var sp = 12f
            while (sp > 7f) {
                val ok = fitsAt(n.label, sp, boxW, labelH, true) &&
                    (n.sub?.let { fitsAt(it, sp * 0.84f, boxW, boxH - labelH, false) } ?: true)
                if (ok) return sp
                sp -= 0.5f
            }
            return 0f
        }
    }
}
