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
import androidx.compose.runtime.LaunchedEffect
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
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.Question
import com.shahaabapps.core.content.ScoreBy
import com.shahaabapps.core.content.breakdown
import com.shahaabapps.core.content.weakest
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
    val speakerState by viewModel.speaker.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.course?.speechLanguage) { state.course?.speechLanguage?.let { viewModel.speaker.setLanguage(it) } }
    val speaker = viewModel.speaker.takeIf { speakerState == Speaker.State.READY }
    val title = when (state.kind) {
        QuizKind.MOCK -> "Mock exam"
        QuizKind.REVIEW -> "Review"
        QuizKind.DAILY -> "Daily review"
        QuizKind.MODULE -> module?.let { "${it.title} test" } ?: "Test"
    }
    DetailScaffold(title = title, onBack = onBack, backLabel = "Exit") {
        if (quiz == null || module == null || state.course == null) {
            val empty = when (state.kind) {
                QuizKind.REVIEW -> "Nothing to review" to "Every question you have missed has since been answered correctly. Take a module test or the mock exam to find new gaps."
                QuizKind.DAILY -> "Nothing due today" to "Every question you have met is still fresh. Read a topic or sit a test, and they will come back here when they are about to fade."
                else -> null
            }
            if (empty != null) Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                Text(empty.first, style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold)
                Text(empty.second, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                CupertinoPrimaryButton(text = "Back", onClick = onBack)
            }
            return@DetailScaffold
        }
        if (state.finished) {
            Results(state, onRetake = viewModel::retake, onOpenTopic = { onOpenTopic(state.course!!.id, it) }, onDone = onBack)
            return@DetailScaffold
        }
        val q = state.question ?: return@DetailScaffold
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = Spacing.listHorizontal).padding(bottom = Spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Question ${state.index + 1} of ${state.total}", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.weight(1f))
                state.clock?.let { c ->
                    val low = (state.secondsLeft ?: 0) < 300
                    Text(c, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = if (low) AppTheme.colors.destructive else AppTheme.colors.secondaryLabel)
                }
            }
            ProgressBar(fraction = state.index.toFloat() / state.total)
            if (state.kind == QuizKind.MOCK && state.index == 0 && !state.revealed) {
                Text("Exam conditions: no explanations until the end, ${state.quiz?.timeLimitMinutes} minutes on the clock, pass at ${state.quiz?.passPercent}%.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
            }
            AnimatedContent(
                targetState = q,
                transitionSpec = { (slideInHorizontally(tween(300)) { it / 3 } + fadeIn(tween(240))) togetherWith (slideOutHorizontally(tween(220)) { -it / 3 } + fadeOut(tween(160))) },
                label = "question",
            ) { question ->
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                    question.scenario?.let { sc ->
                        CupertinoCard { Text(sc, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel) }
                    }
                    question.listen?.let { audio ->
                        CupertinoCard {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                                SpeakButton(audio, speaker, size = 44.dp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(if (state.course?.speechLanguage?.startsWith("de") == true) "Hören" else "Listening", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.accent, fontWeight = FontWeight.SemiBold)
                                    Text(if (state.revealed && state.instant) audio else "Play the audio, then answer. The text appears after you answer.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                                }
                            }
                        }
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
            AnimatedVisibility(visible = state.revealed && state.instant, enter = expandVertically() + fadeIn()) {
                val right = state.answers[q.id] == true
                Column(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background((if (right) AppTheme.colors.success else AppTheme.colors.destructive).copy(alpha = 0.12f)).padding(Spacing.md),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(if (right) "Correct" else "Not quite", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = if (right) AppTheme.colors.success else AppTheme.colors.destructive)
                    // Why the answer you picked was tempting, before why the right one is right.
                    // Being told about your own mistake teaches more than hearing the model answer again.
                    q.whyWrongFor(state.selected)?.let { why ->
                        Text(why, style = MaterialTheme.typography.bodyMedium, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Text(q.explanation, style = MaterialTheme.typography.bodyMedium, lineHeight = 22.sp)
                }
            }
            if (state.revealed && state.instant) {
                CupertinoPrimaryButton(text = if (state.index + 1 >= state.total) "See results" else "Next question", onClick = viewModel::next)
            } else {
                val label = if (!state.instant) (if (state.index + 1 >= state.total) "Finish exam" else "Next") else "Check"
                CupertinoPrimaryButton(text = label, enabled = state.selected.isNotEmpty(), onClick = viewModel::check)
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
        val headline = when {
            state.kind == QuizKind.DAILY -> "Review done"
            state.kind == QuizKind.REVIEW -> "Review done"
            state.timedOut -> "Time is up"
            state.passed -> "Passed"
            else -> "Not yet"
        }
        val keeping = state.kind == QuizKind.DAILY || state.kind == QuizKind.REVIEW
        val tone = if (keeping || state.passed) AppTheme.colors.success else AppTheme.colors.warn
        Text(headline, style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, color = tone)
        Text(
            when (state.kind) {
                QuizKind.DAILY -> if (state.missedQuestions.isEmpty()) {
                    "All held. Each one comes back a little later than last time."
                } else {
                    "${state.missedQuestions.size} slipped; those come back tomorrow, the rest later."
                }
                QuizKind.REVIEW -> if (state.missedQuestions.isEmpty()) "Every missed question is now right. The review set is empty." else "${state.missedQuestions.size} still to get right; they stay in the review set and come back next time."
                QuizKind.MOCK -> if (state.passed) "Above the ${state.quiz?.passPercent}% the real exam asks for. The minutes count as practice." else "The real exam needs ${state.quiz?.passPercent}%. The missed questions below are in your review set; reread their topics and sit it again."
                QuizKind.MODULE -> if (state.passed) "${module.title} is ticked. The minutes are in your hours." else "You need ${state.quiz?.passPercent}%. Read the weak topics below and try again; retakes are unlimited."
            },
            style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            StatTile(label = "Score", value = "${state.percent}%", modifier = Modifier.weight(1f), valueColor = if (state.passed) AppTheme.colors.success else AppTheme.colors.warn)
            StatTile(label = "Correct", value = "${state.correctCount} of ${state.total}", modifier = Modifier.weight(1f))
        }
        // Where the score came from, so it points somewhere: by module for a paper
        // that spans the course, by topic for one module's own test.
        val pass = state.quiz?.passPercent ?: 80
        val areas = state.course
            ?.breakdown(state.asked, state.answers.filterValues { it }.keys, if (state.kind == QuizKind.MODULE) ScoreBy.TOPIC else ScoreBy.MODULE)
            .orEmpty()
        // One area is just the score again, said twice.
        if (areas.size > 1) {
            Text("How it went", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
            CupertinoCard {
                areas.forEachIndexed { i, a ->
                    val tone = when {
                        a.clean -> AppTheme.colors.success
                        a.percent >= pass -> AppTheme.colors.accent
                        else -> AppTheme.colors.warn
                    }
                    Column(modifier = Modifier.padding(top = if (i == 0) 0.dp else Spacing.sm)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(a.label, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                            Text("${a.correct} of ${a.asked}", style = MaterialTheme.typography.bodySmall, color = tone, fontWeight = FontWeight.SemiBold)
                        }
                        ProgressBar(fraction = a.percent / 100f, color = tone, height = 6.dp, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
            // The weak area is a place to go, not a sentence about one. It is
            // shown whether or not the paper was passed: passing at 82% with
            // one area at 40% is exactly when nobody goes back on their own.
            val weak = areas.weakest(pass)
            if (weak.isNotEmpty()) {
                val byTopic = state.kind == QuizKind.MODULE
                Text(
                    if (state.passed) "Passed, but these were thin" else "Start here",
                    style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold,
                )
                weak.take(2).forEach { a ->
                    // By topic the area is already a destination. By module,
                    // the first topic it actually lost a question in is the
                    // honest place to land.
                    val destination = if (byTopic) a.id else {
                        state.missedQuestions
                            .firstOrNull { q -> state.course?.let { Courses.topic(it.id, q.topicId) }?.first?.id == a.id }
                            ?.topicId
                            ?: state.course?.modules?.firstOrNull { it.id == a.id }?.topics?.firstOrNull()?.id
                    }
                    CupertinoCard(onClick = destination?.let { { onOpenTopic(it) } }) {
                        Text(a.label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(
                            "${a.correct} of ${a.asked} · tap to reread",
                            style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                        )
                    }
                }
            }
        }
        val missed = state.missedQuestions
        if (missed.isNotEmpty()) {
            Text("Review these", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
            val byTopic = missed.groupBy { it.topicId }
            byTopic.forEach { (topicId, qs) ->
                val real = state.course?.let { c -> Courses.topic(c.id, topicId) }
                val short = (real?.second ?: module.topics.firstOrNull { it.id == topicId })?.title?.substringBefore(":") ?: topicId
                val title = if (real != null && real.first.id != module.id) "${real.first.title} · $short" else short
                CupertinoCard(onClick = { onOpenTopic(topicId) }) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("${qs.size} missed · tap to reread", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                    qs.forEach { q ->
                        Text("· ${q.prompt}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
                        if (!state.instant) Text("  ${q.options.filterIndexed { i, _ -> i in q.correct }.joinToString(", ")}. ${q.explanation}", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(start = 10.dp))
                    }
                }
            }
        }
        state.message?.let { Text(it, color = AppTheme.colors.destructive, style = MaterialTheme.typography.bodySmall) }
        val done = state.passed || keeping
        if (done) CupertinoPrimaryButton(text = "Back to the course", onClick = onDone) else CupertinoPrimaryButton(text = "Try again", onClick = onRetake)
        // A spaced sitting is not retaken: the schedule decides when each question returns.
        if (done) { if (!keeping) CupertinoSecondaryButton(text = "Take it again", onClick = onRetake) } else CupertinoTextButton(text = "Back to the course", onClick = onDone)
    }
}

@Suppress("unused")
private fun unusedQuestion(q: Question) = q.id

@Suppress("unused")
@Composable
private fun unusedNumber() = AnimatedNumber(value = 0, style = MaterialTheme.typography.bodyMedium)
