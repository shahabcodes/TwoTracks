package com.shahaabapps.core.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The clock as a stand-in for how sure somebody was.
 *
 * The thresholds are deliberately far apart, so these fix the shape rather
 * than the arithmetic: a four-second answer to a paragraph is quick however
 * the reading speed is tuned, and a question answered in about the time it
 * takes to read is steady.
 */
class PaceTest {

    private val short = "Which service runs a container?"
    private val options = listOf("App Service", "Functions", "Container Apps", "Blob Storage")

    private val scenario =
        "Your team runs a public API behind a load balancer. Traffic triples every weekday morning " +
            "and falls away overnight, and the bill has to follow the traffic rather than the peak. " +
            "The team does not want to manage virtual machines or write scaling rules by hand."

    @Test
    fun `a longer question is expected to take longer`() {
        val plain = Pace.expectedSeconds(short, null, options)
        val withScenario = Pace.expectedSeconds(short, scenario, options)
        assertTrue("a scenario should add time, got $plain and $withScenario", withScenario > plain + 10)
    }

    @Test
    fun `nothing is expected to be instant`() {
        // A two-word prompt with two options still needs reading and deciding.
        assertTrue(Pace.expectedSeconds("Which?", null, listOf("Yes", "No")) >= 12)
    }

    @Test
    fun `more options means more to weigh`() {
        val two = Pace.expectedSeconds(short, null, listOf("A", "B"))
        val six = Pace.expectedSeconds(short, null, listOf("A", "B", "C", "D", "E", "F"))
        assertTrue("six options should cost more than two, got $two and $six", six > two)
    }

    @Test
    fun `an answer faster than the question can be read is quick`() {
        val expected = Pace.expectedSeconds(short, scenario, options)
        assertEquals(Pace.Verdict.QUICK, Pace.verdict(4, expected))
    }

    @Test
    fun `an answer around the expected time is steady`() {
        val expected = Pace.expectedSeconds(short, null, options)
        assertEquals(Pace.Verdict.STEADY, Pace.verdict(expected, expected))
        assertEquals(Pace.Verdict.STEADY, Pace.verdict(expected * 2, expected))
    }

    @Test
    fun `an answer that took more than twice as long was worked out`() {
        val expected = Pace.expectedSeconds(short, null, options)
        assertEquals(Pace.Verdict.SLOW, Pace.verdict(expected * 2 + 1, expected))
    }

    @Test
    fun `without a clock there is no verdict`() {
        // Every attempt recorded before sittings kept one lands here, and must
        // behave exactly as it did before there was a clock at all.
        assertEquals(Pace.Verdict.UNKNOWN, Pace.verdict(null, 30))
        assertEquals(Pace.Verdict.UNKNOWN, Pace.verdict(10, null))
        assertEquals(Pace.Verdict.UNKNOWN, Pace.verdict(0, 30))
    }
}
