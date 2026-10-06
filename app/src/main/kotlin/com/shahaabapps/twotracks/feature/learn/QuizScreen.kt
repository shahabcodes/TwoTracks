package com.shahaabapps.twotracks.feature.learn

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.content.Question
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoSecondaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.AnimatedNumber
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.ProgressBar
import com.shahaabapps.twotracks.ui.components.StatTile

/** One question per screen, an explanation after each answer, a results page at the end. */
@Composable
fun QuizScreen(onBack: () -> Unit, onOpenTopic: (String, String) -> Unit, viewModel: QuizViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val module = state.module
    val quiz = state.quiz
    DetailScaffold(title = module?.let { "${it.title} test" } ?: "Test", onBack = onBack, backLabel = "Exit") {
        if (quiz == null || module == null || state.course == null) return@DetailScaffold
        if (state.finished) {
            Results(state, onRetake = viewModel::retake, onOpenTopic = { onOpenTopic(state.course!!.id, it) }, onDone = onBack)
            return@DetailScaffold
        }
        val q = state.question ?: return@DetailScaffold
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = Spacing.listHorizontal).padding(bottom = Spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            Text("Question ${state.index + 1} of ${state.total}", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
            ProgressBar(fraction = state.index.toFloat() / state.total)
            AnimatedContent(
                targetState = q,
                transitionSpec = { (slideInHorizontally(tween(300)) { it / 3 } + fadeIn(tween(240))) togetherWith (slideOutHorizontally(tween(220)) { -it / 3 } + fadeOut(tween(160))) },
                label = "question",
            ) { question ->
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                    question.scenario?.let { sc ->
                        CupertinoCard { Text(sc, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel) }
                    }
                    Text(question.prompt, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold, lineHeight = 28.sp)
                    if (question.multi) Text("Select all that apply.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                    question.options.forEachIndexed { i, opt ->
                        OptionRow(
                            text = opt, index = i, selected = i in state.selected, revealed = state.revealed,
                            correct = i in question.correct, multi = question.multi, onClick = { viewModel.toggle(i) },
                        )
                    }
                }
            }
            AnimatedVisibility(visible = state.revealed, enter = expandVertically() + fadeIn()) {
                val right = state.answers[q.id] == true
                Column(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background((if (right) AppTheme.colors.success else AppTheme.colors.destructive).copy(alpha = 0.12f)).padding(Spacing.md),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(if (right) "Correct" else "Not quite", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = if (right) AppTheme.colors.success else AppTheme.colors.destructive)
                    Text(q.explanation, style = MaterialTheme.typography.bodyMedium, lineHeight = 22.sp)
                }
            }
            if (state.revealed) {
                CupertinoPrimaryButton(text = if (state.index + 1 >= state.total) "See results" else "Next question", onClick = viewModel::next)
            } else {
                CupertinoPrimaryButton(text = "Check", enabled = state.selected.isNotEmpty(), onClick = viewModel::check)
            }
        }
    }
}

@Composable
private fun OptionRow(text: String, index: Int, selected: Boolean, revealed: Boolean, correct: Boolean, multi: Boolean, onClick: () -> Unit) {
    val accent = AppTheme.colors.accent
    val tint = when {
        revealed && correct -> AppTheme.colors.success
        revealed && selected && !correct -> AppTheme.colors.destructive
        selected -> accent
        else -> Color.Transparent
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (tint == Color.Transparent) AppTheme.colors.card else tint.copy(alpha = 0.12f))
            .border(1.5.dp, if (tint == Color.Transparent) AppTheme.colors.separator else tint, RoundedCornerShape(14.dp))
            .clickable(enabled = !revealed, onClick = onClick)
            .padding(horizontal = Spacing.md, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        Box(
            modifier = Modifier.size(26.dp).clip(if (multi) RoundedCornerShape(7.dp) else CircleShape)
                .background(if (selected || (revealed && correct)) (if (tint == Color.Transparent) accent else tint) else AppTheme.colors.fill),
            contentAlignment = Alignment.Center,
        ) {
            when {
                revealed && correct -> Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                revealed && selected -> Icon(Icons.Filled.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                else -> Text(('A' + index).toString(), style = MaterialTheme.typography.labelMedium, color = if (selected) Color.White else AppTheme.colors.secondaryLabel, fontWeight = FontWeight.Bold)
            }
        }
        Text(text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun Results(state: QuizUiState, onRetake: () -> Unit, onOpenTopic: (String) -> Unit, onDone: () -> Unit) {
    val module = state.module ?: return
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = Spacing.listHorizontal).padding(bottom = Spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Text(if (state.passed) "Passed" else "Not yet", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, color = if (state.passed) AppTheme.colors.success else AppTheme.colors.warn)
        Text(
            if (state.passed) "${module.title} is ticked. The minutes are in your hours." else "You need ${state.quiz?.passPercent}%. Read the weak topics below and try again; retakes are unlimited.",
            style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            StatTile(label = "Score", value = "${state.percent}%", modifier = Modifier.weight(1f), valueColor = if (state.passed) AppTheme.colors.success else AppTheme.colors.warn)
            StatTile(label = "Correct", value = "${state.correctCount} of ${state.total}", modifier = Modifier.weight(1f))
        }
        val missed = state.missedQuestions
        if (missed.isNotEmpty()) {
            Text("Review these", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
            val byTopic = missed.groupBy { it.topicId }
            byTopic.forEach { (topicId, qs) ->
                val title = module.topics.firstOrNull { it.id == topicId }?.title?.substringBefore(":") ?: topicId
                CupertinoCard(onClick = { onOpenTopic(topicId) }) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("${qs.size} missed · tap to reread", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                    qs.forEach { q -> Text("· ${q.prompt}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp)) }
                }
            }
        }
        state.message?.let { Text(it, color = AppTheme.colors.destructive, style = MaterialTheme.typography.bodySmall) }
        if (state.passed) CupertinoPrimaryButton(text = "Back to the course", onClick = onDone) else CupertinoPrimaryButton(text = "Try again", onClick = onRetake)
        if (state.passed) CupertinoSecondaryButton(text = "Take it again", onClick = onRetake) else CupertinoTextButton(text = "Back to the course", onClick = onDone)
    }
}

@Suppress("unused")
private fun unusedQuestion(q: Question) = q.id

@Suppress("unused")
@Composable
private fun unusedNumber() = AnimatedNumber(value = 0, style = MaterialTheme.typography.bodyMedium)
