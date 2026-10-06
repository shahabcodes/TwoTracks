package com.shahaabapps.twotracks.feature.learn

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoSecondaryButton
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.ProgressBar
import com.shahaabapps.twotracks.ui.components.StatTile

/**
 * The flashcard session: one word at a time, German first with sound, flip
 * for the English, then "I knew it" or "Not yet". Progress saves at the end
 * and the minutes log to Vocabulary.
 */
@Composable
fun DeckScreen(onBack: () -> Unit, viewModel: DeckViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val speaker = viewModel.speaker
    LaunchedEffect(state.course?.speechLanguage) { state.course?.speechLanguage?.let { speaker.setLanguage(it) } }
    DetailScaffold(title = "Words", onBack = onBack, backLabel = "Exit") {
        val course = state.course ?: return@DetailScaffold
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = Spacing.listHorizontal).padding(bottom = Spacing.xl), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
            when {
                state.finished -> Finished(state, onBack, viewModel::again)
                state.session.isEmpty() -> {
                    Text("Nothing due", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold)
                    Text("Every word you have met is scheduled for later. Read a Words topic in ${course.title} to add more, or come back tomorrow.", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                    CupertinoSecondaryButton(text = "Practise 10 anyway", onClick = viewModel::practiseAnyway)
                }
                else -> {
                    val w = state.current ?: return@Column
                    Text("Card ${state.index + 1} of ${state.session.size} · ${state.dueCount} due · ${state.newCount} new", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
                    ProgressBar(fraction = state.index.toFloat() / state.session.size)
                    AnimatedContent(targetState = w.id, transitionSpec = { (slideInHorizontally(tween(260)) { it / 3 } + fadeIn(tween(200))) togetherWith (slideOutHorizontally(tween(200)) { -it / 3 } + fadeOut(tween(140))) }, label = "card") { _ ->
                        Column(
                            modifier = Modifier.fillMaxWidth().heightIn(min = 260.dp).clip(RoundedCornerShape(24.dp))
                                .background(if (state.flipped) AppTheme.colors.accent else AppTheme.colors.card)
                                .clickable { viewModel.flip() }.padding(Spacing.lg),
                            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                        ) {
                            if (!state.flipped) {
                                Text(w.display, style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                w.plural?.let { Text("pl. $it", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel) }
                                Row(modifier = Modifier.padding(top = Spacing.md), horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                                    SpeakButton(w.display, speaker.takeIf { state.canSpeak }, size = 44.dp)
                                    SpeakButton(w.example, speaker.takeIf { state.canSpeak }, size = 44.dp, slow = true)
                                }
                                Text("Tap to flip", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = Spacing.md))
                            } else {
                                Text(w.en, style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
                                Text(w.example, style = MaterialTheme.typography.bodyMedium, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.padding(top = Spacing.md), lineHeight = 22.sp)
                                Text(w.exampleEn, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.85f), textAlign = TextAlign.Center)
                            }
                        }
                    }
                    if (state.flipped) {
                        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                            Box(modifier = Modifier.weight(1f)) { CupertinoSecondaryButton(text = "Not yet", onClick = { viewModel.answer(false) }) }
                            Box(modifier = Modifier.weight(1f)) { CupertinoPrimaryButton(text = "I knew it", onClick = { viewModel.answer(true) }) }
                        }
                        Text("Known words come back in ${state.nextIntervalDays} day${if (state.nextIntervalDays == 1L) "" else "s"}; missed ones tomorrow.", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.secondaryLabel)
                    } else {
                        CupertinoPrimaryButton(text = "Show the English", onClick = viewModel::flip)
                    }
                    if (!state.canSpeak) {
                        Text(
                            if (state.speakerState == Speaker.State.MISSING_LANGUAGE) "German voice not installed. Tap to open the download page." else "Text-to-speech is not available on this phone.",
                            style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.warn,
                            modifier = Modifier.clickable(enabled = state.speakerState == Speaker.State.MISSING_LANGUAGE) { speaker.openInstallPage() },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Finished(state: DeckUiState, onDone: () -> Unit, onAgain: () -> Unit) {
    Text("Done for today", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, color = AppTheme.colors.success)
    Text("${state.minutes} minutes logged to Vocabulary. The words you knew moved up the ladder; the others come back tomorrow.", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        StatTile(label = "Knew", value = "${state.known}", modifier = Modifier.weight(1f), valueColor = AppTheme.colors.success)
        StatTile(label = "Not yet", value = "${state.missed}", modifier = Modifier.weight(1f), valueColor = AppTheme.colors.warn)
        StatTile(label = "In deck", value = "${state.deckSize}", modifier = Modifier.weight(1f))
    }
    CupertinoPrimaryButton(text = "Back", onClick = onDone)
    CupertinoSecondaryButton(text = "Go again with the missed ones", enabled = state.missed > 0, onClick = onAgain)
}
