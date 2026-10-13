package com.shahaabapps.twotracks.ui.guide

import androidx.compose.ui.geometry.Rect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Where the tour's card goes against the ring: beside it, never over it. */
class GuidePlacementTest {
    private val safeTop = 100f
    private val safeBottom = 2_200f
    private val gap = 40f

    @Test
    fun `a card that fits below the ring goes just below it`() {
        val p = placeCard(Rect(0f, 300f, 1_000f, 500f), natural = 600f, safeTop, safeBottom, gap)
        assertTrue(p.below)
        assertEquals(540f, p.top)
    }

    @Test
    fun `a card that only fits above goes just above, its foot a gap clear of the ring`() {
        val p = placeCard(Rect(0f, 1_700f, 1_000f, 1_900f), natural = 600f, safeTop, safeBottom, gap)
        assertFalse(p.below)
        assertEquals(1_700f - gap - 600f, p.top)
    }

    @Test
    fun `a card that fits neither side takes the roomier one and is capped to it`() {
        val p = placeCard(Rect(0f, 900f, 1_000f, 1_500f), natural = 1_200f, safeTop, safeBottom, gap)
        assertFalse(p.below)
        assertEquals(900f - gap - safeTop, p.room)
        assertEquals(safeTop, p.top)
    }

    @Test
    fun `with nothing ringed the card sits at the foot`() {
        val p = placeCard(Rect.Zero, natural = 500f, safeTop, safeBottom, gap)
        assertTrue(p.below)
        assertEquals(safeBottom - 500f, p.top)
    }

    @Test
    fun `wherever the ring is and however tall the card, it never covers the ring or leaves the safe area`() {
        for (ringTop in 120 until 2_100 step 37) {
            for (ringHeight in listOf(60, 180, 400, 900)) {
                val hole = Rect(0f, ringTop.toFloat(), 1_000f, (ringTop + ringHeight).toFloat().coerceAtMost(safeBottom))
                for (natural in listOf(250f, 500f, 900f, 1_600f)) {
                    val p = placeCard(hole, natural, safeTop, safeBottom, gap)
                    val height = natural.coerceAtMost(p.room)
                    val top = p.top
                    val bottom = top + height
                    assertTrue("card $top..$bottom overlaps ring ${hole.top}..${hole.bottom}", bottom <= hole.top - gap + 0.5f || top >= hole.bottom + gap - 0.5f)
                    if (height > 0f) assertTrue("card $top..$bottom leaves $safeTop..$safeBottom", top >= safeTop - 0.5f && bottom <= safeBottom + 0.5f)
                }
            }
        }
    }

    @Test
    fun `a ring too tall to leave either side room still gets a card big enough to press`() {
        val p = placeCard(Rect(0f, 150f, 1_000f, 2_150f), natural = 500f, safeTop, safeBottom, gap, minRoom = 400f)
        assertTrue(p.covers)
        assertTrue("room ${p.room}", p.room >= 400f)
        assertEquals(safeBottom - 400f.coerceAtMost(500f), p.top)
        assertFalse(placeCard(Rect(0f, 300f, 1_000f, 500f), natural = 600f, safeTop, safeBottom, gap, minRoom = 400f).covers)
    }
}
