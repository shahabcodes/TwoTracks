package com.shahaabapps.twotracks.tap

import com.shahaabapps.core.model.ModuleFile
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.twotracks.feature.learn.FileViewerContent
import com.shahaabapps.twotracks.feature.learn.FileViewerUiState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The screen that shows an attachment: its chrome, its arrows, and the
 * question it asks before removing anything.
 *
 * The picture itself is not under test here — a bitmap needs a real decoder
 * and a PDF a real renderer, neither of which exists off the device. What is
 * under test is everything around it, which is where a press can go wrong.
 */
class FileViewerTapTest(look: AppLook) : TapTest(look) {

    private fun image(id: String, name: String) = ModuleFile(
        id = id, courseId = "az-204", moduleId = "az-compute", name = name,
        mimeType = "image/jpeg", sizeBytes = 812_000,
        path = "couples/c/modules/az-204/az-compute/$id",
        uploadedBy = "u1", uploadedAtIso = "2026-11-01T09:00:00Z",
    )

    private val one = image("f1", "whiteboard.jpg")
    private val two = image("f2", "slots.jpg")

    private fun viewer(state: FileViewerUiState) = show {
        FileViewerContent(
            state = state,
            onBack = record("back"),
            onPrevious = record("previous"),
            onNext = record("next"),
            onOpenElsewhere = record("elsewhere"),
            onDelete = record("delete"),
        )
    }

    @Test
    fun `the viewer goes back, hands off, and asks before removing`() {
        viewer(FileViewerUiState(file = one, siblings = listOf(one)))
        tapDescribed("Back")
        expect("back")
        tapAction("Open whiteboard.jpg in another app")
        expect("elsewhere")
        // The bin over a picture is the easiest press in the app to make by
        // accident, so it opens the question and nothing else.
        tapAction("Remove whiteboard.jpg")
        expect()
        tapAction("Remove whiteboard.jpg for both of you")
        expect("delete")
    }

    @Test
    fun `one file of several says where it is, and moves`() {
        viewer(FileViewerUiState(file = two, siblings = listOf(one, two)))
        assertTrue("the position is not shown in the $look look", seen("2 / 2"))
        tapAction("Previous file")
        expect("previous")
        // Last of the two: there is nowhere further to go, so there is no
        // arrow to press rather than an arrow that does nothing.
        assertFalse("a next arrow is offered at the end in the $look look", clickable("Next file"))
    }

    @Test
    fun `a single file is shown without the arrows`() {
        viewer(FileViewerUiState(file = one, siblings = listOf(one)))
        assertFalse("the arrows are offered for one file in the $look look", clickable("Next file"))
        assertFalse("the arrows are offered for one file in the $look look", clickable("Previous file"))
    }

    @Test
    fun `a download in flight says what it is fetching`() {
        viewer(FileViewerUiState(file = one, siblings = listOf(one), downloading = 0.3f))
        assertTrue("no sign of the download in the $look look", seen("Fetching whiteboard.jpg"))
    }

    @Test
    fun `a file that will not come down says so`() {
        viewer(FileViewerUiState(file = one, siblings = listOf(one), error = "Could not download whiteboard.jpg"))
        assertTrue("the failure is silent in the $look look", seen("Could not download whiteboard.jpg"))
    }
}
