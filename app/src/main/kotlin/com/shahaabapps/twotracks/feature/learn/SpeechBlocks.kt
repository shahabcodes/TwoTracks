package com.shahaabapps.twotracks.feature.learn

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.content.Ex
import com.shahaabapps.core.content.WritingMarker
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing

/** Hören: a play button, the options, and the transcript only after answering. Two plays, like the exam. */
@Composable
fun ListenExercise(ex: Ex.Listen, speaker: Speaker?) {
    var plays by remember(ex.id) { mutableIntStateOf(0) }
    var picked by remember(ex.id) { mutableStateOf<Int?>(null) }
    val answered = picked != null
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        val can = speaker != null && (plays < 2 || answered)
        Box(
            modifier = Modifier.size(52.dp).clip(CircleShape).background(if (can) AppTheme.colors.accent else AppTheme.colors.fill).clickable(enabled = can) { plays++; speaker?.speak(ex.audio) },
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.Filled.PlayArrow, contentDescription = "Play", tint = if (can) Color.White else AppTheme.colors.secondaryLabel, modifier = Modifier.size(30.dp)) }
        Column {
            Text(if (plays == 0) "Play the audio" else if (!answered) "Played $plays of 2" else "Play again as often as you like", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            if (speaker == null) Text("Voice not ready; see the note at the end of the topic.", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.warn)
        }
    }
    Text(ex.question, style = MaterialTheme.typography.bodyLarge)
    ex.options.forEachIndexed { i, opt ->
        val right = i == ex.correct
        val tint = when {
            answered && right -> AppTheme.colors.success
            answered && picked == i -> AppTheme.colors.destructive
            else -> Color.Transparent
        }
        Row(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(if (tint == Color.Transparent) AppTheme.colors.card else tint.copy(alpha = 0.12f))
                .border(1.dp, if (tint == Color.Transparent) AppTheme.colors.separator else tint, RoundedCornerShape(10.dp))
                .clickable(enabled = !answered && plays > 0) { picked = i }.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (answered && right) Icon(Icons.Filled.Check, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
            else if (answered && picked == i) Icon(Icons.Filled.Close, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
            Text(opt, style = MaterialTheme.typography.bodyMedium)
        }
    }
    if (answered) {
        Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(AppTheme.colors.card).padding(Spacing.sm)) {
            Text(if (picked == ex.correct) "Richtig" else "Not quite", style = MaterialTheme.typography.labelMedium, color = if (picked == ex.correct) AppTheme.colors.success else AppTheme.colors.destructive, fontWeight = FontWeight.SemiBold)
            Text("You heard: ${ex.audio}", style = MaterialTheme.typography.bodySmall, lineHeight = 20.sp, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

/** Sprechen: hear the model, say it into the phone, see which words landed. */
@Composable
fun SpeakExercise(ex: Ex.Speak, speaker: Speaker?, recognizer: Recognizer?) {
    val context = LocalContext.current
    val state by (recognizer?.state ?: remember { kotlinx.coroutines.flow.MutableStateFlow<Recognizer.State>(Recognizer.State.Unavailable) }).collectAsStateWithLifecycle()
    var heard by remember(ex.id) { mutableStateOf<String?>(null) }
    var selfChecked by remember(ex.id) { mutableStateOf(false) }
    var granted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) }
    val ask = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { ok -> granted = ok; if (ok) recognizer?.listen(ex.id) }
    val usable = recognizer != null && recognizer.available
    DisposableEffect(ex.id) { onDispose { recognizer?.reset(ex.id) } }
    // One recogniser serves every card on the page: read only what this one asked for.
    val mine = state.owner == ex.id
    if (mine && state is Recognizer.State.Heard && heard == null) heard = (state as Recognizer.State.Heard).text

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(ex.target, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(ex.en, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
        }
        SpeakButton(ex.target, speaker, size = 40.dp)
        SpeakButton(ex.target, speaker, size = 30.dp, slow = true)
    }
    val listening = mine && state is Recognizer.State.Listening
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        Box(
            modifier = Modifier.size(56.dp).clip(CircleShape).background(when { !usable -> AppTheme.colors.fill; listening -> AppTheme.colors.destructive; else -> AppTheme.colors.accent })
                .clickable(enabled = usable && !listening) { heard = null; if (granted) recognizer?.listen(ex.id) else ask.launch(Manifest.permission.RECORD_AUDIO) },
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.Filled.Mic, contentDescription = "Speak", tint = if (usable) Color.White else AppTheme.colors.secondaryLabel, modifier = Modifier.size(28.dp)) }
        Text(
            when {
                !usable -> "Speech recognition is not available on this phone. Say it aloud, then tick below."
                listening -> "Listening… say it now"
                mine && state is Recognizer.State.Failed -> (state as Recognizer.State.Failed).reason
                heard != null -> "Heard you. Tap the mic to try again."
                else -> "Tap the mic, then say the sentence"
            },
            style = MaterialTheme.typography.bodySmall, color = if (mine && state is Recognizer.State.Failed) AppTheme.colors.warn else AppTheme.colors.secondaryLabel, modifier = Modifier.weight(1f),
        )
    }
    heard?.let { h ->
        val match = SpeechMatch.of(ex.target, h)
        val pct = match.percent
        Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(AppTheme.colors.card).padding(Spacing.sm), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(if (pct >= 80) "Sehr gut: $pct% of the words" else if (pct >= 50) "Getting there: $pct% of the words" else "Try once more: $pct% of the words", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = if (pct >= 80) AppTheme.colors.success else AppTheme.colors.warn)
            Text("You said: $h", style = MaterialTheme.typography.bodySmall)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                match.words.forEach { w ->
                    Text(w.word, style = MaterialTheme.typography.bodySmall, color = if (w.matched) AppTheme.colors.success else AppTheme.colors.destructive, fontWeight = if (w.matched) FontWeight.Normal else FontWeight.SemiBold)
                }
            }
        }
    }
    if (!usable) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.clickable { selfChecked = !selfChecked }) {
            Box(modifier = Modifier.size(18.dp).clip(RoundedCornerShape(5.dp)).background(if (selfChecked) AppTheme.colors.success else AppTheme.colors.fill), contentAlignment = Alignment.Center) {
                if (selfChecked) Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
            }
            Text("I said it aloud, twice, without reading", style = MaterialTheme.typography.bodySmall)
        }
    }
}

/** Schreiben: the app marks content points by keyword, plus greeting, sign-off and length, then shows the model. */
@Composable
fun MarkedWritingExercise(ex: Ex.Writing) {
    var text by remember(ex.id) { mutableStateOf("") }
    var result by remember(ex.id) { mutableStateOf<WritingMarker.Result?>(null) }
    val words = text.trim().split(Regex("\\s+")).filter { it.isNotBlank() }.size
    CupertinoTextField(value = text, onValueChange = { text = it; result = null }, placeholder = if (ex.form == com.shahaabapps.core.content.WritingForm.LETTER) "Write here" else "Write here. Press return twice between paragraphs.", singleLine = false, modifier = Modifier.fillMaxWidth())
    Text("$words words · aim for ${ex.minWords}+", style = MaterialTheme.typography.labelSmall, color = if (words >= ex.minWords) AppTheme.colors.success else AppTheme.colors.secondaryLabel)
    if (result == null) {
        val formHint = when (ex.form) { com.shahaabapps.core.content.WritingForm.LETTER -> "a greeting and a sign-off"; com.shahaabapps.core.content.WritingForm.ESSAY -> "paragraphs, linking words and a clear position"; com.shahaabapps.core.content.WritingForm.REPORT -> "an overview, figures and comparisons" }
        Text("The marker looks for: ${ex.points.joinToString(", ")}; plus $formHint.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
        CupertinoPrimaryButton(text = "Mark my text", enabled = words >= 5, onClick = { result = WritingMarker.mark(ex, text) })
    }
    result?.let { r ->
        Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(AppTheme.colors.card).padding(Spacing.sm), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("${r.score} of ${r.maxScore} points", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = if (r.score >= r.maxScore - 1) AppTheme.colors.success else AppTheme.colors.warn)
            ex.points.forEachIndexed { i, p -> MarkRow(p, r.points[i]) }
            r.checks.forEach { c -> MarkRow(c.label, c.ok) }
            Text("Model answer", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.accent, modifier = Modifier.padding(top = 4.dp))
            Text(ex.model, style = MaterialTheme.typography.bodyMedium, lineHeight = 21.sp)
            CupertinoTextButton(text = "Edit and mark again", onClick = { result = null })
        }
    }
}

@Composable
private fun MarkRow(label: String, ok: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(if (ok) Icons.Filled.Check else Icons.Filled.Close, contentDescription = null, tint = if (ok) AppTheme.colors.success else AppTheme.colors.destructive, modifier = Modifier.size(16.dp))
        Text(label, style = MaterialTheme.typography.bodySmall, color = if (ok) MaterialTheme.colorScheme.onSurface else AppTheme.colors.destructive)
    }
}
