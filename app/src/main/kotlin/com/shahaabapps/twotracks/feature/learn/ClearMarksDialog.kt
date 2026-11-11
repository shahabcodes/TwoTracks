package com.shahaabapps.twotracks.feature.learn

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import com.shahaabapps.twotracks.ui.components.DialogButton

/**
 * The question before a marked-up lesson is wiped clean.
 *
 * The eraser takes one stroke at a time and the bin takes the lot, which is
 * why only the bin asks. What it says is the part that is easy to forget
 * while looking at one screenful: the marks further down the lesson go too.
 */
@Composable
fun ClearMarksDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    CupertinoDialog(
        onDismissRequest = onDismiss,
        title = { Text("Clear your marks?") },
        text = {
            Text(
                "Every mark on this lesson goes, including the ones further down that you cannot see from here. " +
                    "The lesson itself is untouched, and nothing of your partner's is.",
            )
        },
        confirmButton = { DialogButton(destructive = true, onClick = onConfirm) { Text("Clear them") } },
        dismissButton = { DialogButton(onClick = onDismiss) { Text("Keep them") } },
    )
}
