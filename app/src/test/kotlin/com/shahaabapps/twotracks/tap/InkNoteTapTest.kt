package com.shahaabapps.twotracks.tap

import com.shahaabapps.core.model.InkNote
import com.shahaabapps.core.model.InkPage
import com.shahaabapps.core.model.InkStroke
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.twotracks.feature.learn.INK_CANVAS
import com.shahaabapps.twotracks.feature.learn.InkNoteContent
import com.shahaabapps.twotracks.feature.learn.InkNoteUiState
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The chrome around a page of handwriting.
 *
 * The writing itself is a pen on glass and cannot be done here, but
 * everything that can lose it can: saving under the wrong name, rubbing out
 * when you meant to write, and the bin — which on this screen removes the
 * only copy of something in somebody's own hand, with nowhere to fetch it
 * back from.
 */
class InkNoteTapTest(look: AppLook) : TapTest(look) {

    private val page = InkPage(strokes = listOf(InkStroke("1C1C20", 3f, listOf(10f, 10f, 40f, 40f))))

    private val saved = InkNote(
        id = "n1", courseId = "az-204", moduleId = "az-compute", topicId = "az-app-service",
        title = "Slot swap, worked", path = "couples/c/modules/az-204/az-compute/n1",
        strokeCount = 1, createdAtIso = "2026-11-01T09:00:00Z", updatedAtIso = "2026-11-01T09:00:00Z",
    )

    private fun editor(state: InkNoteUiState) = show {
        InkNoteContent(
            state = state,
            onBack = record("back"),
            onWrite = { calls += "write(${it.strokes.size})" },
            onUndo = record("undo"),
            onTool = { calls += "tool(${if (it.erasing) "eraser" else it.colour})" },
            onSave = { calls += "save($it)" },
            onDelete = record("delete"),
        )
    }

    private fun fresh() = InkNoteUiState(heading = "App Service", page = page, loaded = true, unsaved = true)
    private fun existing() = InkNoteUiState(note = saved, heading = "App Service", page = page, loaded = true)

    @Test
    fun `a new page asks what it is before it is saved`() {
        editor(fresh())
        tapAction("Save this note")
        // Nothing is written yet: a page saved as "Note" is a page nobody
        // finds again, so the name is asked for once and asked for here.
        expect()
        assertTrue("the question is not asked in the $look look", seen("What is this note?"))
        tap("Save")
        // The lesson's own title is offered, because that is what the note
        // is nearly always about.
        expect("save(App Service)")
    }

    @Test
    fun `a page that already has a name saves without asking again`() {
        editor(existing())
        tapAction("Save this note")
        expect("save(Slot swap, worked)")
    }

    @Test
    fun `backing out of a saved page, and taking back a stroke`() {
        editor(existing())
        tapDescribed("Back")
        expect("back")
        tapAction("Undo the last stroke")
        expect("undo")
    }

    @Test
    fun `backing out with unsaved writing asks before losing it`() {
        editor(existing().copy(unsaved = true))
        tapDescribed("Back")
        // The strokes exist nowhere but this screen until they are saved,
        // and the back arrow is two centimetres from the tick.
        expect()
        assertTrue("leaving did not ask in the $look look", seen("Keep what you have written?"))
        tap("Save and close")
        expect("save(Slot swap, worked)")
    }

    @Test
    fun `going back to the page from that question changes nothing`() {
        editor(existing().copy(unsaved = true))
        tapDescribed("Back")
        tap("Keep writing")
        expect()
    }

    @Test
    fun `throwing the page away is a separate answer, and leaves`() {
        editor(existing().copy(unsaved = true))
        tapDescribed("Back")
        tap("Throw it away")
        expect("back")
    }

    @Test
    fun `the bin asks first, and says there is no copy of this`() {
        editor(existing())
        tapAction("Remove this note")
        expect()
        assertTrue("the dialog does not say what is at stake in the $look look", seen("your own handwriting"))
        tap("Remove it")
        expect("delete")
    }

    @Test
    fun `keeping it at the question leaves the page alone`() {
        editor(existing())
        tapAction("Remove this note")
        tap("Keep it")
        expect()
    }

    @Test
    fun `a page that has never been saved has nothing to remove`() {
        editor(fresh())
        assertTrue("an unsaved page offers a bin in the $look look", !clickable("Remove this note"))
    }

    @Test
    fun `the pen changes colour and turns into an eraser`() {
        editor(existing())
        tapAction("Write in red")
        expect("tool(C0392B)")
        tapAction("Rub out")
        expect("tool(eraser)")
    }

    @Test
    fun `an unsaved page says so, rather than looking saved`() {
        editor(fresh())
        assertTrue("nothing says the page is unsaved in the $look look", seen("UNSAVED"))
    }

    @Test
    fun `a page still loading does not show itself as empty`() {
        editor(InkNoteUiState(note = saved, heading = "App Service", loaded = false))
        // An empty sheet over a note that is still arriving reads as a note
        // whose contents are gone, which is the worst thing this screen
        // could say and the only thing it must never say wrongly.
        assertTrue("the canvas was drawn before the strokes arrived in the $look look", !tagged(INK_CANVAS))
    }
}
