package com.shahaabapps.twotracks.feature.learn

import com.shahaabapps.core.ui.theme.MonoFamily
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    DeckContent(
        state = state, speaker = speaker.takeIf { state.canSpeak }, onBack = onBack,
        onFlip = viewModel::flip, onAnswer = viewModel::answer, onPractiseAnyway = viewModel::practiseAnyway, onAgain = viewModel::again,
        onInstallVoice = speaker::openInstallPage,
    )
}

/**
 * The word deck, as the Parallel prototype draws it: a close button, the
 * course and where you are, a thin bar, then one card outlined in ink that
 * turns over when tapped, buttons to hear it, and Again or Got it at the
 * foot with when each one brings the card back.
 */
@Composable
fun DeckContent(
    state: DeckUiState,
    speaker: Speaker?,
    onBack: () -> Unit,
    onFlip: () -> Unit,
    onAnswer: (Boolean) -> Unit,
    onPractiseAnyway: () -> Unit,
    onAgain: () -> Unit,
    onInstallVoice: () -> Unit,
) {
    val course = state.course
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        Column(modifier = Modifier.fillMaxSize().padding(inner)) {
            val count = if (state.session.isEmpty() || state.finished) "" else "${(state.index + 1).coerceAtMost(state.session.size)} / ${state.session.size}"
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 6.dp)) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.offset(x = (-10).dp).size(44.dp).clip(CircleShape).clickable(role = Role.Button, onClickLabel = "Close the deck", onClick = onBack),
                ) { Icon(Icons.Filled.Close, contentDescription = "Close the deck", tint = AppTheme.colors.ink) }
                Text(((course?.title ?: "") + " · words").uppercase(), style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                Text(count, style = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFamily, fontSize = 13.sp), fontWeight = FontWeight.Medium, textAlign = TextAlign.End, modifier = Modifier.width(54.dp))
            }
            if (course == null) return@Column
            if (state.session.isNotEmpty() && !state.finished) {
                Box(Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 14.dp).fillMaxWidth().height(3.dp).background(AppTheme.colors.separator)) {
                    Box(Modifier.fillMaxWidth(((state.index + 1).toFloat() / state.session.size).coerceIn(0f, 1f)).height(3.dp).background(AppTheme.colors.accentText))
                }
            }
            when {
                state.finished -> Finished(state, onBack, onAgain)
                state.session.isEmpty() -> Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal, vertical = 28.dp), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                    Text("Nothing due", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.semantics { heading() })
                    Text("Every word you have met is scheduled for later. Read a Words topic in ${course.title} to add more, or come back tomorrow.", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                    CupertinoSecondaryButton(text = "Practise 10 anyway", onClick = onPractiseAnyway)
                }
                else -> {
                    val w = state.current ?: return@Column
                    Column(modifier = Modifier.weight(1f).padding(Spacing.listHorizontal), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        AnimatedContent(
                            targetState = w.id,
                            transitionSpec = { (slideInHorizontally(tween(260)) { it / 3 } + fadeIn(tween(200))) togetherWith (slideOutHorizontally(tween(200)) { -it / 3 } + fadeOut(tween(140))) },
                            label = "card", modifier = Modifier.weight(1f),
                        ) { _ ->
                            val shape = RoundedCornerShape(22.dp)
                            Column(
                                modifier = Modifier.fillMaxSize().clip(shape).border(Spacing.rule, AppTheme.colors.ink, shape)
                                    .clickable(onClickLabel = if (state.flipped) "Show the German side" else "Show the meaning", onClick = onFlip).padding(24.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(if (state.flipped) "BACK" else "FRONT", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    w.article?.let { Text(it, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp), color = AppTheme.colors.secondaryLabel) }
                                    Text(w.de, style = MaterialTheme.typography.displayLarge.copy(fontSize = 48.sp, lineHeight = 50.sp, letterSpacing = (-1.4).sp), fontWeight = FontWeight.Bold)
                                    w.plural?.let { Text("pl. $it", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel) }
                                }
                                if (state.flipped) {
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline, modifier = Modifier.padding(bottom = 8.dp))
                                        Text(w.en, style = MaterialTheme.typography.headlineMedium.copy(fontSize = 26.sp), color = AppTheme.colors.accentText)
                                        Text(w.example, style = MaterialTheme.typography.bodyLarge, lineHeight = 22.sp)
                                        Text(w.exampleEn, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                                    }
                                } else {
                                    Text("Say it, then tap to check", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                                }
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally), modifier = Modifier.fillMaxWidth()) {
                            SpeakButton(w.display, speaker, size = 52.dp)
                            SpeakButton(w.example, speaker, size = 52.dp, slow = true)
                        }
                        if (speaker == null) {
                            Text(
                                if (state.speakerState == Speaker.State.MISSING_LANGUAGE) "${course.languageName} voice not installed. Tap to open the download page." else "Text-to-speech is not available on this phone.",
                                style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.warn, textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().clickable(enabled = state.speakerState == Speaker.State.MISSING_LANGUAGE, onClick = onInstallVoice),
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, bottom = 28.dp).navigationBarsPadding()) {
                        AnswerButton("Again", "back tomorrow", primary = false, modifier = Modifier.weight(1f)) { onAnswer(false) }
                        AnswerButton("Got it", "back in ${state.nextIntervalDays} day${if (state.nextIntervalDays == 1L) "" else "s"}", primary = true, modifier = Modifier.weight(1f)) { onAnswer(true) }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnswerButton(label: String, detail: String, primary: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
        modifier = modifier.height(56.dp).clip(shape)
            .background(if (primary) AppTheme.colors.accent else Color.Transparent)
            .then(if (primary) Modifier else Modifier.border(Spacing.rule, AppTheme.colors.separator, shape))
            .clickable(role = Role.Button, onClick = onClick),
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp), color = if (primary) AppTheme.colors.onSolid else AppTheme.colors.ink)
        Text(detail, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = if (primary) AppTheme.colors.onSolid.copy(alpha = 0.75f) else AppTheme.colors.secondaryLabel)
    }
}

@Composable
private fun Finished(state: DeckUiState, onDone: () -> Unit, onAgain: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal, vertical = 28.dp), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        Text("Done for today", style = MaterialTheme.typography.headlineLarge, color = AppTheme.colors.success, modifier = Modifier.semantics { heading() })
        Text("${state.minutes} minutes logged to Vocabulary. The words you knew moved up the ladder; the others come back tomorrow.", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            StatTile(label = "knew", value = "${state.known}", modifier = Modifier.weight(1f), valueColor = AppTheme.colors.success)
            StatTile(label = "not yet", value = "${state.missed}", modifier = Modifier.weight(1f), valueColor = AppTheme.colors.warn)
            StatTile(label = "in the deck", value = "${state.deckSize}", modifier = Modifier.weight(1f))
        }
        CupertinoPrimaryButton(text = "Back", onClick = onDone)
        CupertinoSecondaryButton(text = "Go again with the missed ones", enabled = state.missed > 0, onClick = onAgain)
    }
}
