package com.shahaabapps.twotracks.widget

import com.shahaabapps.core.content.WidgetSnapshot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * What a widget says, which two renderers read.
 *
 * The widget is Glance and the preview in Settings is ordinary Compose, and
 * they cannot share a composable. They share this, so it is the one place
 * worth holding: a change here moves both, and a disagreement between them
 * can only be a layout bug rather than a difference of opinion about what
 * the widget means.
 */
class WidgetFaceTest {

    private val today = LocalDate.parse("2026-10-11")

    private fun snapshot(
        dateIso: String = "2026-10-11",
        stepsDone: Int = 0,
        stepsTotal: Int = 0,
        minutesLeft: Int = 0,
        nextLabel: String = "",
        questionsDue: Int = 0,
        questionsCourseId: String = "",
        word: String = "",
        wordMeaning: String = "",
        wordExample: String = "",
        togetherState: Int = 0,
        togetherStreak: Int = 0,
        partnerName: String = "",
    ) = WidgetSnapshot(
        dateIso = dateIso, stepsDone = stepsDone, stepsTotal = stepsTotal,
        minutesLeft = minutesLeft, nextLabel = nextLabel,
        questionsDue = questionsDue, questionsCourseId = questionsCourseId,
        word = word, wordMeaning = wordMeaning, wordExample = wordExample,
        togetherState = togetherState, togetherStreak = togetherStreak, partnerName = partnerName,
    )

    private fun face(content: WidgetContent, s: WidgetSnapshot) =
        s.faceFor(WidgetConfig(content = content), today)

    @Test
    fun `a stale snapshot says so rather than reading as an empty day`() {
        // The one thing this must never do. "Nothing to do" and "I have not
        // been told anything" look identical on a home screen, and only one
        // of them means you are finished.
        val stale = snapshot(dateIso = "2026-10-04")
        listOf(WidgetContent.TODAY_STUDY, WidgetContent.DAILY_REVIEW, WidgetContent.TOGETHER).forEach {
            assertEquals("$it should ask to be opened", "Open TwoTracks", face(it, stale).headline)
        }
    }

    @Test
    fun `the word of the day survives a stale snapshot, because a word keeps`() {
        val stale = snapshot(dateIso = "2026-10-04", word = "Bahnhof", wordMeaning = "station")
        val f = face(WidgetContent.WORD_OF_DAY, stale)
        assertEquals("Bahnhof", f.headline)
        assertEquals("station", f.body)
    }

    @Test
    fun `today counts what is done against what is due`() {
        val f = face(WidgetContent.TODAY_STUDY, snapshot(stepsDone = 1, stepsTotal = 3, minutesLeft = 20, nextLabel = "Read: Slots"))
        assertEquals("1 of 3 done", f.headline)
        assertEquals("20 min left · Read: Slots", f.body)
        assertEquals(1, f.pipsFilled)
        assertEquals(3, f.pipsTotal)
    }

    @Test
    fun `a day with nothing queued has nothing to count`() {
        val f = face(WidgetContent.TODAY_STUDY, snapshot(stepsTotal = 0))
        assertEquals("Nothing queued", f.headline)
        assertTrue("no dots when there is nothing due", !f.hasPips)
    }

    @Test
    fun `review points somewhere even with nothing due`() {
        // "Nothing to review" is true for most of a course, and a widget that
        // only ever says so is one worth removing.
        val f = face(WidgetContent.DAILY_REVIEW, snapshot(nextLabel = "Read: Blobs"))
        assertEquals("Nothing due", f.headline)
        assertEquals("Next up · Read: Blobs", f.body)
    }

    @Test
    fun `the example line is dropped when there is none`() {
        val with = face(WidgetContent.WORD_OF_DAY, snapshot(word = "a", wordMeaning = "b", wordExample = "c"))
        assertEquals("c", with.extra)
        val without = face(WidgetContent.WORD_OF_DAY, snapshot(word = "a", wordMeaning = "b"))
        assertNull(without.extra)
    }

    @Test
    fun `together names the partner rather than saying they`() {
        val f = face(WidgetContent.TOGETHER, snapshot(togetherState = 1, partnerName = "Sanya"))
        assertEquals("Your part is done", f.headline)
        assertEquals("Sanya has not studied yet.", f.body)
    }

    @Test
    fun `an unnamed partner is still addressed`() {
        val f = face(WidgetContent.TOGETHER, snapshot(togetherState = 1))
        assertEquals("Your partner has not studied yet.", f.body)
    }

    @Test
    fun `a shared run of more than a week still fits seven dots`() {
        val f = face(WidgetContent.TOGETHER, snapshot(togetherState = 3, togetherStreak = 30))
        assertEquals("30 days running", f.headline)
        assertEquals(7, f.pipsFilled)
        assertEquals(7, f.pipsTotal)
    }

    // ----------------------------------------------------------- the colours

    @Test
    fun `a forced theme carries its own ink`() {
        // The bug this guards: keeping the phone's text over a forced
        // background drew dark on dark and the headline all but vanished.
        val dark = paletteFor(WidgetConfig(theme = WidgetTheme.DARK), themed = white, themedStrong = black, themedSoft = grey)
        assertEquals(white, dark.strong)
        val light = paletteFor(WidgetConfig(theme = WidgetTheme.LIGHT), themed = black, themedStrong = white, themedSoft = grey)
        assertEquals(near(0xFF1C1C1E), light.strong)
    }

    @Test
    fun `a chosen dot colour shows before anything is done`() {
        // Tinting only the filled dots left the first day of a course showing
        // a row of grey, which reads as the choice having done nothing.
        val p = paletteFor(WidgetConfig(accent = WidgetAccent.MEADOW), themed = white, themedStrong = black, themedSoft = grey)
        assertEquals(WidgetAccent.MEADOW.color, p.pipOn)
        assertEquals(WidgetAccent.MEADOW.color?.copy(alpha = 0.3f), p.pipOff)
    }

    @Test
    fun `following the look leaves the dots to the theme`() {
        val p = paletteFor(WidgetConfig(), themed = white, themedStrong = black, themedSoft = grey)
        assertEquals(black, p.pipOn)
    }

    private val white = androidx.compose.ui.graphics.Color.White
    private val black = androidx.compose.ui.graphics.Color.Black
    private val grey = androidx.compose.ui.graphics.Color.Gray
    private fun near(v: Long) = androidx.compose.ui.graphics.Color(v)
}
