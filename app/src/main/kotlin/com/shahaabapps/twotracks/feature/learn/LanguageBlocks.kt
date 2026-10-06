package com.shahaabapps.twotracks.feature.learn

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.content.Block
import com.shahaabapps.core.content.Ex
import com.shahaabapps.core.content.Line
import com.shahaabapps.core.content.Word
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import java.text.Normalizer

/** Speaker button used beside every German word and line. */
@Composable
fun SpeakButton(text: String, speaker: Speaker?, size: androidx.compose.ui.unit.Dp = 32.dp, slow: Boolean = false) {
    val enabled = speaker != null
    Box(
        modifier = Modifier.size(size).clip(CircleShape).background(if (enabled) AppTheme.colors.accent.copy(alpha = 0.14f) else AppTheme.colors.fill)
            .clickable(enabled = enabled) { speaker?.speak(text, slow) },
        contentAlignment = Alignment.Center,
    ) {
        Icon(Icons.Filled.VolumeUp, contentDescription = "Listen", tint = if (enabled) AppTheme.colors.accent else AppTheme.colors.secondaryLabel, modifier = Modifier.size(size * 0.55f))
    }
}

/** Vocabulary cards: tap to flip between German and English; speaker reads the German. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VocabBlock(block: Block.Vocab, speaker: Speaker?) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        Text("${block.words.size} words · tap a card to flip, speaker to listen", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
        block.words.forEach { w -> WordCard(w, speaker) }
    }
}

@Composable
private fun WordCard(w: Word, speaker: Speaker?) {
    var flipped by remember(w.id) { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(if (flipped) AppTheme.colors.accent.copy(alpha = 0.1f) else AppTheme.colors.card)
            .border(1.dp, if (flipped) AppTheme.colors.accent.copy(alpha = 0.4f) else AppTheme.colors.separator, RoundedCornerShape(14.dp))
            .clickable { flipped = !flipped }.padding(horizontal = Spacing.md, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        AnimatedContent(targetState = flipped, transitionSpec = { fadeIn(tween(160)) togetherWith fadeOut(tween(120)) }, label = "flip", modifier = Modifier.weight(1f)) { f ->
            if (!f) {
                Column {
                    Text(w.display, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    w.plural?.let { Text("pl. $it", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.secondaryLabel) }
                    Text(w.example, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = 2.dp))
                }
            } else {
                Column {
                    Text(w.en, style = MaterialTheme.typography.titleMedium, color = AppTheme.colors.accent, fontWeight = FontWeight.SemiBold)
                    Text(w.exampleEn, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = 2.dp))
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            SpeakButton(w.display, speaker)
            SpeakButton(w.example, speaker, size = 26.dp, slow = true)
        }
    }
}

/** A dialogue: speakers alternate sides; tap a line for the English; play per line. */
@Composable
fun DialogueBlock(block: Block.Dialogue, speaker: Speaker?) {
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(AppTheme.colors.card).padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(block.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            CupertinoTextButton(text = "Play all", onClick = { speaker?.speak(block.lines.joinToString(" . ") { it.de }) }, modifier = Modifier.width(96.dp))
        }
        val first = block.lines.firstOrNull()?.speaker
        block.lines.forEach { ln -> DialogueLine(ln, left = ln.speaker == first, speaker) }
        Text("Tap a line for the translation. Then cover it and say the line from memory.", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.secondaryLabel)
    }
}

@Composable
private fun DialogueLine(ln: Line, left: Boolean, speaker: Speaker?) {
    var show by remember(ln.de) { mutableStateOf(false) }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = if (left) Arrangement.Start else Arrangement.End, verticalAlignment = Alignment.Bottom) {
        if (!left) SpeakButton(ln.de, speaker, size = 28.dp)
        Column(
            modifier = Modifier.padding(horizontal = 6.dp).widthIn(max = 280.dp)
                .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp, bottomStart = if (left) 4.dp else 14.dp, bottomEnd = if (left) 14.dp else 4.dp))
                .background(if (left) AppTheme.colors.fill else AppTheme.colors.accent)
                .clickable { show = !show }.padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Text(ln.speaker, style = MaterialTheme.typography.labelSmall, color = if (left) AppTheme.colors.secondaryLabel else Color.White.copy(alpha = 0.8f))
            Text(ln.de, style = MaterialTheme.typography.bodyMedium, color = if (left) MaterialTheme.colorScheme.onSurface else Color.White)
            if (show) Text(ln.en, style = MaterialTheme.typography.bodySmall, color = if (left) AppTheme.colors.accent else Color.White.copy(alpha = 0.9f), modifier = Modifier.padding(top = 2.dp))
        }
        if (left) SpeakButton(ln.de, speaker, size = 28.dp)
    }
}

private fun norm(s: String): String = Normalizer.normalize(s.trim().lowercase(), Normalizer.Form.NFC).replace("ß", "ss").replace(Regex("[.!?,]"), "")

/** Checked exercises. Results are not scored; they are for practice and show the answer when wrong. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExerciseBlock(block: Block.Exercise, speaker: Speaker?, recognizer: Recognizer? = null, lang: String? = null) {
    val accent = AppTheme.colors.accent
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(accent.copy(alpha = 0.08f)).padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // German courses use the Goethe paper names; everything else uses plain English.
        val german = lang?.startsWith("de") == true
        val label = when (block.exercise) {
            is Ex.Listen -> if (german) "Hören" else "Listening"
            is Ex.Speak -> if (german) "Sprechen" else "Speaking"
            is Ex.Writing -> if (german) "Schreiben" else "Writing"
            else -> "Exercise"
        }
        Text(label, style = MaterialTheme.typography.labelMedium, color = accent, fontWeight = FontWeight.SemiBold)
        Text(block.exercise.prompt, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        when (val ex = block.exercise) {
            is Ex.Gap -> GapExercise(ex, speaker)
            is Ex.Reorder -> ReorderExercise(ex, speaker)
            is Ex.Match -> MatchExercise(ex)
            is Ex.Writing -> if (ex.keywords.isEmpty()) WritingExercise(ex) else MarkedWritingExercise(ex)
            is Ex.Listen -> ListenExercise(ex, speaker)
            is Ex.Speak -> SpeakExercise(ex, speaker, recognizer)
        }
    }
}

@Composable
private fun GapExercise(ex: Ex.Gap, speaker: Speaker?) {
    var text by remember(ex.id) { mutableStateOf("") }
    var result by remember(ex.id) { mutableStateOf<Boolean?>(null) }
    ex.audio?.let { audio ->
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            SpeakButton(audio, speaker, size = 40.dp)
            Text("Play, then type what you hear in the gap.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
        }
    }
    Text(ex.sentence.replace("___", "______"), style = MaterialTheme.typography.bodyLarge)
    ex.hint?.let { Text("Hint: $it", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.secondaryLabel) }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        CupertinoTextField(value = text, onValueChange = { text = it; result = null }, placeholder = "the missing word", modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(AppTheme.colors.accent).clickable(enabled = text.isNotBlank()) { result = ex.answers.any { norm(it) == norm(text) } },
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.Filled.Check, contentDescription = "Check", tint = Color.White) }
    }
    result?.let { ok ->
        val full = ex.sentence.replace("___", ex.answers.first())
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(if (ok) "Correct: $full" else "Not quite. Correct: $full", style = MaterialTheme.typography.bodySmall, color = if (ok) AppTheme.colors.success else AppTheme.colors.destructive, modifier = Modifier.weight(1f))
            SpeakButton(full, speaker, size = 28.dp)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ReorderExercise(ex: Ex.Reorder, speaker: Speaker?) {
    val chosen = remember(ex.id) { mutableStateListOf<Int>() }
    val done = chosen.size == ex.pieces.size
    val built = chosen.joinToString(" ") { ex.pieces[it] }
    val ok = done && norm(built) == norm(ex.answer)
    Box(modifier = Modifier.fillMaxWidth().height(44.dp).clip(RoundedCornerShape(10.dp)).background(AppTheme.colors.card).padding(horizontal = 12.dp), contentAlignment = Alignment.CenterStart) {
        Text(if (built.isEmpty()) "Tap the words in order" else built, style = MaterialTheme.typography.bodyLarge, color = if (built.isEmpty()) AppTheme.colors.secondaryLabel else MaterialTheme.colorScheme.onSurface)
    }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        ex.pieces.forEachIndexed { i, piece ->
            val used = i in chosen
            Box(
                modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(if (used) AppTheme.colors.fill.copy(alpha = 0.5f) else AppTheme.colors.card)
                    .border(1.dp, AppTheme.colors.separator, RoundedCornerShape(10.dp)).clickable(enabled = !used && !done) { chosen.add(i) }.padding(horizontal = 12.dp, vertical = 8.dp),
            ) { Text(piece, style = MaterialTheme.typography.bodyMedium, color = if (used) AppTheme.colors.secondaryLabel else MaterialTheme.colorScheme.onSurface) }
        }
    }
    if (done) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(if (ok) "Correct: ${ex.answer}" else "Correct order: ${ex.answer}", style = MaterialTheme.typography.bodySmall, color = if (ok) AppTheme.colors.success else AppTheme.colors.destructive, modifier = Modifier.weight(1f))
            SpeakButton(ex.answer, speaker, size = 28.dp)
        }
    }
    if (chosen.isNotEmpty()) {
        Text("Reset", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.accent, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { chosen.clear() }.padding(vertical = 4.dp))
    }
}

@Composable
private fun MatchExercise(ex: Ex.Match) {
    val rights = remember(ex.id) { ex.pairs.map { it.second }.shuffled(kotlin.random.Random(ex.id.hashCode())) }
    var selectedLeft by remember(ex.id) { mutableStateOf<Int?>(null) }
    val matched = remember(ex.id) { mutableStateListOf<Int>() }
    var wrong by remember(ex.id) { mutableStateOf<Int?>(null) }
    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            ex.pairs.forEachIndexed { i, p ->
                val isMatched = i in matched
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                        .background(when { isMatched -> AppTheme.colors.success.copy(alpha = 0.15f); selectedLeft == i -> AppTheme.colors.accent.copy(alpha = 0.2f); else -> AppTheme.colors.card })
                        .border(1.dp, if (selectedLeft == i) AppTheme.colors.accent else AppTheme.colors.separator, RoundedCornerShape(10.dp))
                        .clickable(enabled = !isMatched) { selectedLeft = i; wrong = null }.padding(10.dp),
                ) { Text(p.first, style = MaterialTheme.typography.bodySmall) }
            }
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            rights.forEachIndexed { j, r ->
                val leftIdx = ex.pairs.indexOfFirst { it.second == r }
                val isMatched = leftIdx in matched
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                        .background(when { isMatched -> AppTheme.colors.success.copy(alpha = 0.15f); wrong == j -> AppTheme.colors.destructive.copy(alpha = 0.15f); else -> AppTheme.colors.card })
                        .border(1.dp, AppTheme.colors.separator, RoundedCornerShape(10.dp))
                        .clickable(enabled = !isMatched && selectedLeft != null) {
                            if (leftIdx == selectedLeft) { matched.add(leftIdx); selectedLeft = null; wrong = null } else wrong = j
                        }.padding(10.dp),
                ) { Text(r, style = MaterialTheme.typography.bodySmall) }
            }
        }
    }
    if (matched.size == ex.pairs.size) Text("All matched!", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.success)
}

@Composable
private fun WritingExercise(ex: Ex.Writing) {
    var text by remember(ex.id) { mutableStateOf("") }
    var checked by remember(ex.id) { mutableStateOf(false) }
    val words = text.trim().split(Regex("\\s+")).filter { it.isNotBlank() }.size
    CupertinoTextField(value = text, onValueChange = { text = it; checked = false }, placeholder = "Write here", singleLine = false, modifier = Modifier.fillMaxWidth())
    Text("$words words · aim for ${ex.minWords}+", style = MaterialTheme.typography.labelSmall, color = if (words >= ex.minWords) AppTheme.colors.success else AppTheme.colors.secondaryLabel)
    Text("The marker looks for:", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
    val ticks = remember(ex.id) { mutableStateListOf<Int>() }
    ex.points.forEachIndexed { i, p ->
        val on = i in ticks
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.clickable { if (on) ticks.remove(i) else ticks.add(i) }) {
            Box(modifier = Modifier.size(18.dp).clip(RoundedCornerShape(5.dp)).background(if (on) AppTheme.colors.success else AppTheme.colors.fill), contentAlignment = Alignment.Center) {
                if (on) Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
            }
            Text(p, style = MaterialTheme.typography.bodySmall)
        }
    }
    if (!checked) CupertinoPrimaryButton(text = "Compare with a model answer", enabled = words >= 5, onClick = { checked = true })
    if (checked) {
        Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(AppTheme.colors.card).padding(Spacing.sm)) {
            Text("Model answer", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.accent)
            Text(ex.model, style = MaterialTheme.typography.bodyMedium, lineHeight = 21.sp)
            Text("Tick the points you covered. ${ticks.size} of ${ex.points.size} is your score in the exam's terms.", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Suppress("unused")
private fun keyboardHint() = KeyboardType.Text

@Suppress("unused")
@Composable
private fun unusedIcons() { Icon(Icons.Filled.Close, null); Spacer() }

@Composable
private fun Spacer() { Box(Modifier.width(1.dp)) }

@Suppress("unused")
@Composable
private fun unusedScroll() { Row(Modifier.horizontalScroll(rememberScrollState())) {} }
