package com.shahaabapps.twotracks.feature.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.shahaabapps.core.model.InkNote
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import com.shahaabapps.twotracks.ui.components.DialogButton

/**
 * What to call a page of handwriting, asked the first time it is saved.
 *
 * [suggestion] is the lesson's own title, offered because that is what the
 * note is nearly always about and because a dialog that opens with the
 * answer already in it is one press rather than a sentence typed on a
 * tablet with a pen in the other hand.
 */
@Composable
fun NameNoteDialog(suggestion: String, onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
    var typed by remember { mutableStateOf(suggestion) }
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) { runCatching { focus.requestFocus() } }
    val wanted = typed.trim()

    CupertinoDialog(
        onDismissRequest = onDismiss,
        title = { Text("What is this note?") },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                Text("It is kept with this lesson for both of you, and you can write on it again later.")
                CupertinoTextField(
                    value = typed,
                    onValueChange = { typed = it },
                    placeholder = "Worked example",
                    label = "NAME",
                    modifier = Modifier.fillMaxWidth().focusRequester(focus),
                )
            }
        },
        confirmButton = {
            DialogButton(enabled = wanted.isNotEmpty(), onClick = { onConfirm(wanted) }) { Text("Save") }
        },
        dismissButton = { DialogButton(onClick = onDismiss) { Text("Not yet") } },
    )
}

/**
 * The question before a page of handwriting goes.
 *
 * Plainer than the one the attachments ask, and deliberately: an attachment
 * came from somewhere and could be found again, and this came from nobody
 * but the person being asked. What it says is what is actually at stake.
 */
@Composable
fun RemoveNoteDialog(note: InkNote, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    CupertinoDialog(
        onDismissRequest = onDismiss,
        title = { Text("Remove ${note.title}?") },
        text = {
            Text(
                "This is your own handwriting, and there is no copy of it anywhere. " +
                    "It goes for both of you, on every device, and nothing brings it back.",
            )
        },
        confirmButton = {
            DialogButton(destructive = true, onClick = onConfirm) { Text("Remove it") }
        },
        dismissButton = { DialogButton(onClick = onDismiss) { Text("Keep it") } },
    )
}

/**
 * Giving a page of handwriting another title.
 *
 * The same shape as the attachments' rename, and for the same reason: the
 * strokes are stored under the note's id, so the title is the only thing
 * that moves and there is nothing here that can be lost.
 */
@Composable
fun RenameNoteDialog(note: InkNote, onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
    var typed by remember { mutableStateOf(note.title) }
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) { runCatching { focus.requestFocus() } }
    val wanted = typed.trim()

    CupertinoDialog(
        onDismissRequest = onDismiss,
        title = { Text("What should it be called?") },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                Text("Only the title changes. What is written on the page stays exactly as it is.")
                CupertinoTextField(
                    value = typed,
                    onValueChange = { typed = it },
                    placeholder = note.title,
                    label = "NAME",
                    modifier = Modifier.fillMaxWidth().focusRequester(focus),
                )
            }
        },
        confirmButton = {
            DialogButton(enabled = wanted.isNotEmpty() && wanted != note.title, onClick = { onConfirm(wanted) }) {
                Text("Rename")
            }
        },
        dismissButton = { DialogButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

/**
 * The question on the way out of a page with unsaved handwriting on it.
 *
 * Three answers rather than two, because all three are things a person
 * genuinely means here: keep what is written, throw away what is written,
 * or go back to the page because the arrow was a mistake. A two-answer
 * dialog would make one of those into the other.
 */
@Composable
fun LeaveNoteDialog(named: Boolean, onSave: () -> Unit, onDiscard: () -> Unit, onDismiss: () -> Unit) {
    CupertinoDialog(
        onDismissRequest = onDismiss,
        title = { Text("Keep what you have written?") },
        text = {
            Text(
                if (named) "There is writing on this page that has not been saved yet."
                else "This page has never been saved, so leaving now loses everything on it.",
            )
        },
        confirmButton = { DialogButton(onClick = onSave) { Text("Save and close") } },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                DialogButton(onClick = onDismiss) { Text("Keep writing") }
                DialogButton(destructive = true, onClick = onDiscard) { Text("Throw it away") }
            }
        },
    )
}
