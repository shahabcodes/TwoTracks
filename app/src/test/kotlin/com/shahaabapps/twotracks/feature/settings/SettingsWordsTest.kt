package com.shahaabapps.twotracks.feature.settings

import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.twotracks.changelog.shortDate
import org.junit.Assert.assertEquals
import org.junit.Test

/** The words Settings and the pages under it print. */
class SettingsWordsTest {

    @Test
    fun `weekly hours drop a needless decimal`() {
        assertEquals("9.4 h a week", weeklyHoursText(564))
        assertEquals("9 h a week", weeklyHoursText(540))
    }

    @Test
    fun `the widgets row counts what is placed`() {
        assertEquals("None on your home screen", widgetsText(0))
        assertEquals("2 on your home screen", widgetsText(2))
        assertEquals("What each one shows", widgetsText(null))
    }

    @Test
    fun `voices are named by language and what the course reads in it`() {
        assertEquals("GERMAN · FOR WORDS, DIALOGUES AND HÖREN", voiceHeading(Courses.byId("de-a1")!!))
        assertEquals("German, English", voicesText())
        assertEquals("0.85×", rateLabel(0.85f))
    }

    private fun block(day: Int, from: Int, to: Int, kind: BlockKind) = AvailabilityBlock(id = "$day$from", dayOfWeek = day, startMinutes = from * 60, endMinutes = to * 60, kind = kind)

    @Test
    fun `the week grid spans six to eleven at night, and further when a block does`() {
        assertEquals(6..23, gridHours(listOf(block(1, 8, 17, BlockKind.WORK))))
        assertEquals(5..24, gridHours(listOf(block(1, 5, 6, BlockKind.STUDY), block(2, 22, 24, BlockKind.STUDY))))
        assertEquals("1.5 h", kindHours(listOf(AvailabilityBlock(dayOfWeek = 1, startMinutes = 60, endMinutes = 150, kind = BlockKind.STUDY)), BlockKind.STUDY))
        assertEquals("45 h", kindHours(List(5) { block(it + 1, 8, 17, BlockKind.WORK) }, BlockKind.WORK))
    }

    @Test
    fun `a changelog date reads as a day and month`() {
        assertEquals("12 Oct", shortDate("2026-10-12"))
        assertEquals("soon", shortDate("soon"))
    }
}
