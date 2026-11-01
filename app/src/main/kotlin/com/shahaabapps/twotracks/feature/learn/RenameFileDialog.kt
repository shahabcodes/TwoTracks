package com.shahaabapps.twotracks.feature.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.shahaabapps.core.model.ModuleFile
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import com.shahaabapps.twotracks.ui.components.DialogButton
import androidx.compose.material3.Text

/**
 * Giving an attachment a better name than the one it arrived with.
 *
 * Worth having because of what these files actually are: `IMG_20261101_
 * 094312.jpg` and `Scan 7.pdf` are what a camera and a scanner call things,
 * and a list of eleven of those is a list nobody can revise from. Only the
 * record changes — the bytes are stored under the file's id — so this is a
 * safe edit in a screen where the neighbouring one is not.
 *
 * The whole name is in the field, extension and all, rather than the stem
 * with the extension put back afterwards. Hiding it would be tidier to type
 * into and would also mean the app quietly deciding that `scan.pdf.pdf` and
 * a name somebody deliberately ended in a dot are mistakes.
 */
@Composable
fun RenameFileDialog(file: ModuleFile, onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
    var typed by remember { mutableStateOf(file.name) }
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) { runCatching { focus.requestFocus() } }
    val wanted = typed.trim()

    CupertinoDialog(
        onDismissRequest = onDismiss,
        title = { Text("What should it be called?") },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                Text(
                    "Only the name changes. The file itself, and where it is attached, stay exactly as they are.",
                )
                CupertinoTextField(
                    value = typed,
                    onValueChange = { typed = it },
                    placeholder = file.name,
                    label = "NAME",
                    modifier = Modifier.fillMaxWidth().focusRequester(focus),
                )
            }
        },
        confirmButton = {
            DialogButton(
                enabled = wanted.isNotEmpty() && wanted != file.name,
                onClick = { onConfirm(wanted) },
            ) {
                Text("Rename")
            }
        },
        dismissButton = { DialogButton(onClick = onDismiss) { Text("Cancel") } },
    )
}
