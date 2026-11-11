package com.shahaabapps.twotracks.tap

import com.shahaabapps.core.model.InkStroke
import com.shahaabapps.core.model.LessonMark
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.twotracks.feature.learn.LESSON_INK_TOOLS
import com.shahaabapps.twotracks.feature.learn.TopicContent
import com.shahaabapps.twotracks.preview.LearnSamples
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The pen on a lesson: taking it out, what it offers, and putting it away.
 *
 * The strokes themselves are a finger on glass and belong to the anchoring
 * tests. What is here is the part a press can get wrong — chiefly that a
 * lesson being read takes no marks until the pen is out, because the
 * difference between a drag that scrolls and a drag that draws is the one
 * thing nobody should have to discover by accident.
 */
class LessonInkTapTest(look: AppLook) : TapTest(look) {

    private val mark = LessonMark("block-0", InkStroke("C0392B", 4f, listOf(100f, 100f, 400f, 120f)))

    private fun lesson(marks: List<LessonMark> = emptyList()) = show {
        TopicContent(
            LearnSamples.topic().copy(marks = marks),
            speaker = null, voiceMissing = false, recognizer = null,
            onBack = record("back"), onOpenTopic = record2("topic"), onOpenQuiz = record2("quiz"),
            onInstallVoice = record("voice"),
            onChecked = { _, _, _, _ -> calls += "checked" }, onConfidence = { _, _ -> calls += "confidence" },
            onMarkLearned = record("learned"), onUnlearn = record("unlearn"), onMessageShown = {},
            onListen = record("listen"), onOpenNotes = record("notes"), onOpenFiles = record("files"),
            onUndoMark = record("undo"), onClearMarks = record("clear"), onPenAway = record("away"),
        )
    }

    @Test
    fun `a lesson is offered the pen, and is not holding it`() {
        lesson()

        assertTrue("the pen is not offered in the $look look", clickable("Mark up this lesson"))
        // No tools until it is out: the lesson is for reading first.
        assertFalse("the pen was already out in the $look look", tagged(LESSON_INK_TOOLS))
    }

    @Test
    fun `taking the pen out brings the inks with it`() {
        lesson()
        tapAction("Mark up this lesson")

        assertTrue("no tools came out with the pen in the $look look", tagged(LESSON_INK_TOOLS))
        assertTrue("there is no ink to choose in the $look look", clickable("Write in red"))
        assertTrue("there is no nib to choose in the $look look", clickable("A broad nib"))
        assertTrue("there is no eraser in the $look look", clickable("Rub out"))
    }

    @Test
    fun `putting it away writes what was marked`() {
        lesson()
        tapAction("Mark up this lesson")
        expect()
        tapAction("Put the pen away")
        // Saved on the way down rather than a second later: putting the pen
        // down is usually the last thing done before leaving the lesson.
        expect("away")
    }

    @Test
    fun `the last mark can be taken back`() {
        lesson(listOf(mark))
        tapAction("Mark up this lesson")
        tapAction("Take back the last mark")

        expect("undo")
    }

    @Test
    fun `clearing every mark asks first`() {
        lesson(listOf(mark))
        tapAction("Mark up this lesson")
        tapAction("Clear every mark on this lesson")
        expect()
        assertTrue("the question does not say what goes in the $look look", seen("including the ones further down"))
        tap("Clear them")

        expect("clear")
    }

    @Test
    fun `keeping them at the question leaves them alone`() {
        lesson(listOf(mark))
        tapAction("Mark up this lesson")
        tapAction("Clear every mark on this lesson")
        tap("Keep them")

        expect()
    }

    @Test
    fun `an unmarked lesson is not offered a bin`() {
        lesson()
        tapAction("Mark up this lesson")

        // Nothing to clear, so nothing that asks whether to clear it.
        assertFalse("a bin was offered with nothing to bin in the $look look", clickable("Clear every mark on this lesson"))
    }
}
