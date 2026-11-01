package com.shahaabapps.twotracks.feature.learn

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.shahaabapps.core.model.ModuleFile
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import com.shahaabapps.twotracks.ui.components.DialogButton
import java.io.File

/**
 * The question asked before an attachment is removed.
 *
 * It is the app's own dialog rather than the platform's, and the confirm is
 * held rather than tapped. Both are the same reason: the bin sits a few
 * millimetres from the row that opens the file, the file goes for both of you
 * on every device, and nothing brings it back. A press that has to be held
 * for a moment cannot be made by a thumb that was aiming elsewhere, and the
 * bar filling under the finger says plainly that something is happening and
 * that letting go stops it.
 *
 * [preview] is the file's own bytes if this phone already has them, so an
 * image is removed while looking at it rather than at its name. The dialog
 * also exposes a single ordinary click action to accessibility services,
 * because a hold is a gesture TalkBack cannot usefully pass through.
 */
@Composable
fun RemoveFileDialog(
    file: ModuleFile,
    preview: File? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    CupertinoDialog(
        onDismissRequest = onDismiss,
        icon = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.fill),
            ) {
                if (file.isImage && preview != null) {
                    AsyncImage(
                        model = preview,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(64.dp),
                    )
                } else {
                    Icon(
                        if (file.isImage) Icons.Filled.Image else Icons.Filled.Description,
                        contentDescription = null,
                        tint = AppTheme.colors.secondaryLabel,
                        modifier = Modifier.size(26.dp),
                    )
                }
            }
        },
        title = { Text("Remove ${file.name}?") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "It goes for both of you, on every device you are signed in on, and it cannot be brought back.",
                    textAlign = TextAlign.Start,
                )
                Text(
                    file.readableSize.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.colors.secondaryLabel,
                )
            }
        },
        confirmButton = { HoldToRemove(file, onConfirm) },
        dismissButton = { DialogButton(onClick = onDismiss) { Text("Keep it") } },
    )
}

/**
 * A pill that removes the file once it has been held for [HOLD_MS], filling
 * from the left as it goes and emptying again if the finger lifts early.
 */
@Composable
private fun HoldToRemove(file: ModuleFile, onConfirm: () -> Unit) {
    var holding by remember { mutableStateOf(false) }
    val fill = remember { Animatable(0f) }
    val haptic = LocalHapticFeedback.current
    val destructive = AppTheme.colors.destructive
    LaunchedEffect(holding) {
        if (!holding) {
            // Emptying faster than it filled: a lifted finger wants an answer
            // at once, and a slow drain reads as the deletion going ahead.
            fill.animateTo(0f, tween(160))
            return@LaunchedEffect
        }
        fill.animateTo(1f, tween(HOLD_MS, easing = LinearEasing))
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        onConfirm()
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(destructive.copy(alpha = 0.16f))
            // Drawn rather than laid out: a width of zero is not a width a
            // layout modifier accepts, and the bar starts at zero.
            // Part-filled, so the colour under the words changes as it goes
            // while they stay the same readable red throughout.
            .drawBehind { drawRect(destructive.copy(alpha = 0.40f), size = Size(size.width * fill.value, size.height)) }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        holding = true
                        tryAwaitRelease()
                        holding = false
                    },
                )
            }
            .semantics(mergeDescendants = true) {
                onClick(label = "Remove ${file.name} for both of you") { onConfirm(); true }
            },
    ) {
        Text(
            if (holding) "Keep holding…" else "Hold to remove",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = destructive,
        )
    }
}

/** Long enough that it cannot be an accident, short enough not to be a chore. */
private const val HOLD_MS = 900
