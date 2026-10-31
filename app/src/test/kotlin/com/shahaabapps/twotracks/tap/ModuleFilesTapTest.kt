package com.shahaabapps.twotracks.tap

import com.shahaabapps.core.model.ModuleFile
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.twotracks.feature.learn.ModuleFilesContent
import com.shahaabapps.twotracks.feature.learn.ModuleFilesUiState
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The files kept against a module: adding one, opening one, removing one.
 *
 * Open and remove sit on the same row a few millimetres apart, and the
 * difference between them is a file nobody gets back, so both looks are held
 * to which callback each press reaches.
 */
class ModuleFilesTapTest(look: AppLook) : TapTest(look) {

    private val diagram = ModuleFile(
        id = "f1", courseId = "az-204", moduleId = "az-compute", name = "exam-guide.pdf",
        mimeType = "application/pdf", sizeBytes = 2_400_000, path = "couples/c/modules/az-204/az-compute/f1",
        uploadedBy = "u1", uploadedAtIso = "2026-11-01T09:00:00Z",
    )

    private fun files(state: ModuleFilesUiState) = show {
        ModuleFilesContent(
            state, onBack = record("back"), onAdd = record("add"),
            onOpen = { calls += "open(${it.id})" },
            onDelete = { calls += "delete(${it.id})" },
        )
    }

    private fun loaded(vararg list: ModuleFile) =
        ModuleFilesUiState(courseTitle = "AZ-204", moduleTitle = "Compute", files = list.toList(), loaded = true)

    @Test
    fun `an empty module offers to add a file, and goes back`() {
        files(loaded())
        tap("Add a file")
        expect("add")
        tapDescribed("Back")
        expect("back")
    }

    @Test
    fun `pressing a file opens it, and the bin beside it does not`() {
        files(loaded(diagram))
        tapAction("Open exam-guide.pdf")
        expect("open(f1)")
        // The bin asks first: nothing is deleted until the dialog is answered.
        tapAction("Remove exam-guide.pdf")
        expect()
        tap("Remove")
        expect("delete(f1)")
    }

    @Test
    fun `keeping a file at the question leaves it alone`() {
        files(loaded(diagram))
        tapAction("Remove exam-guide.pdf")
        tap("Keep it")
        expect()
    }

    @Test
    fun `a file says how big it is`() {
        files(loaded(diagram))
        // 2.4 MB, not 2400000: the row is read, not parsed.
        assertTrue("the size is not shown in the $look look", seen("2.4 MB"))
    }

    @Test
    fun `an upload in flight says so`() {
        files(loaded().copy(uploading = 0.4f))
        // Nothing to press. An upload with no sign of progress reads as a
        // hang, which is the whole reason the fraction is in the state.
        assertTrue("no sign of the upload in the $look look", seen("Uploading"))
    }
}
