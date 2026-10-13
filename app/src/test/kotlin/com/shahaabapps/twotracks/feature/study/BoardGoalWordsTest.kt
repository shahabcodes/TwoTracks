package com.shahaabapps.twotracks.feature.study

import org.junit.Assert.assertEquals
import org.junit.Test

/** The figures the Departures board prints on a goal, its form and the fit check. */
class BoardGoalWordsTest {
    @Test
    fun `the legs card totals the hours typed so far`() {
        val parts = listOf(ComponentDraft("a", "Compute", "30", ""), ComponentDraft("b", "Storage", "25", ""), ComponentDraft("c", "New", "", ""))
        assertEquals("55H", boardTotal(parts))
        assertEquals("2.5H", boardTotal(listOf(ComponentDraft("a", "x", "2.5", ""))))
    }

    @Test
    fun `hours on the goal's cards drop a nought after the point`() {
        assertEquals("12h", goalHours(12.0))
        assertEquals("4.5h", goalHours(4.5))
        assertEquals("2h", goalHours(1.98))
        assertEquals("120h", goalHours(119.6))
    }

    @Test
    fun `the fit bars print hours in capitals`() {
        assertEquals("128H", boardHours(128.0))
        assertEquals("4.5H", boardHours(4.5))
    }
}
