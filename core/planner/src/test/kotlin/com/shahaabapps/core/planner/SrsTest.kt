package com.shahaabapps.core.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class SrsTest {
    private val d0 = LocalDate.of(2026, 10, 6)

    @Test
    fun `a new card climbs the ladder on correct answers`() {
        var c = Srs.Card("haus")
        c = Srs.review(c, true, d0)
        assertEquals("2026-10-07", c.dueIso)
        assertEquals(1, c.step)
        c = Srs.review(c, true, d0.plusDays(1))
        assertEquals("2026-10-10", c.dueIso)
        c = Srs.review(c, true, d0.plusDays(4))
        assertEquals("2026-10-17", c.dueIso)
        assertEquals(3, c.correct)
    }

    @Test
    fun `a miss resets to the start and comes back tomorrow`() {
        var c = Srs.Card("haus", step = 3, dueIso = "2026-10-06")
        c = Srs.review(c, false, d0)
        assertEquals(0, c.step)
        assertEquals("2026-10-07", c.dueIso)
        assertEquals(1, c.misses)
    }

    @Test
    fun `session takes due cards first then new words up to the limit`() {
        val cards = listOf(
            Srs.Card("a", step = 1, dueIso = "2026-10-05"),
            Srs.Card("b", step = 1, dueIso = "2026-10-06"),
            Srs.Card("c", step = 2, dueIso = "2026-10-09"),
        )
        val s = Srs.session(cards, listOf("a", "b", "c", "d", "e", "f"), d0, limit = 4)
        assertEquals(listOf("a", "b", "d", "e"), s)
        assertEquals(2, Srs.dueCount(cards, d0))
    }

    @Test
    fun `the top of the ladder stays at the top`() {
        var c = Srs.Card("x", step = Srs.LADDER.size - 1, dueIso = "2026-10-06")
        assertTrue(c.learned)
        c = Srs.review(c, true, d0)
        assertEquals(Srs.LADDER.size - 1, c.step)
        assertEquals("2026-12-05", c.dueIso)
        assertFalse(Srs.isDue(c, d0))
    }
}
