package com.shahaabapps.twotracks.feature.settings

import org.junit.Assert.assertEquals
import org.junit.Test

/** The words the Departures board prints on Settings. */
class BoardSettingsWordsTest {
    @Test
    fun `the profile card names the line and keeps the zone`() {
        assertEquals("LINE DEV · ASIA/QATAR", boardProfileLine(isTech = true, profileLine = "The developer · Asia/Qatar"))
        assertEquals("LINE EDU · ASIA/KARACHI", boardProfileLine(isTech = false, profileLine = "The teacher · Asia/Karachi"))
        assertEquals("LINE DEV", boardProfileLine(isTech = true, profileLine = "The developer"))
    }

    @Test
    fun `weekly hours drop the words and keep the figure`() {
        assertEquals("9.4H", boardWeekly(weeklyHoursText(564)))
        assertEquals("10H", boardWeekly(weeklyHoursText(600)))
    }
}
