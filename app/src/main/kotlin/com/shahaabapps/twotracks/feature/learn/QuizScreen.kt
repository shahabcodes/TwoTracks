package com.shahaabapps.twotracks.feature.learn

import androidx.compose.ui.unit.em
import androidx.compose.ui.text.TextStyle
import com.shahaabapps.core.ui.theme.JetBrainsFamily
import com.shahaabapps.core.ui.theme.CondensedFamily
import com.shahaabapps.twotracks.ui.board.FlipTiles
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardLabel
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.ScoreBy
import com.shahaabapps.core.content.breakdown
import com.shahaabapps.core.content.weakest
import com.shahaabapps.core.planner.Pace
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoSecondaryButton
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing

@Composable
fun QuizScreen(onBack: () -> Unit, onOpenTopic: (String, String) -> Unit, viewModel: QuizViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val speakerState by viewModel.speaker.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.course?.speechLanguage) { state.course?.speechLanguage?.let { viewModel.speaker.setLanguage(it) } }
    QuizContent(
        state = state,
        speaker = viewModel.speaker.takeIf { speakerState == Speaker.State.READY },
        onBack = onBack, onOpenTopic = onOpenTopic,
        onToggle = viewModel::toggle, onCheck = viewModel::check, onNext = viewModel::next, onRetake = viewModel::retake,
    )
}

/** The mono name of the sitting, for the middle of the header. */
private fun QuizUiState.label(): String = when (kind) {
    QuizKind.MOCK -> "Mock exam"
    QuizKind.REVIEW -> "Review"
    QuizKind.DAILY -> "Daily review"
    QuizKind.MODULE -> module?.let { "Module ${it.number} test" } ?: "Test"
}

/**
 * A test, as the Parallel prototype draws it: a close button, the sitting's
 * name and where you are in it, a segment per question (green when right,
 * red when not, ink for this one), the question large, the options as
 * outlined rows, and one button at the foot. The results replace it when the
 * paper is finished.
 */
@Composable
fun QuizContent(
    state: QuizUiState,
    speaker: Speaker?,
    onBack: () -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onToggle: (Int) -> Unit,
    onCheck: () -> Unit,
    onNext: () -> Unit,
    onRetake: () -> Unit,
) {
    val module = state.module
    val quiz = state.quiz
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        Column(modifier = Modifier.fillMaxSize().padding(inner)) {
            if (quiz == null || module == null || state.course == null) {
                QuizHeader(state.label(), right = "", onClose = onBack)
                val empty = when (state.kind) {
                    QuizKind.REVIEW -> "Nothing to review" to "Every question you have missed has since been answered correctly. Take a module test or the mock exam to find new gaps."
                    QuizKind.DAILY -> "Nothing due today" to "Every question you have met is still fresh. Read a topic or sit a test, and they will come back here when they are about to fade."
                    else -> null
                }
                if (empty != null) Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal, vertical = 28.dp), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                    Text(empty.first, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.semantics { heading() })
                    Text(empty.second, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                    CupertinoPrimaryButton(text = "Back", onClick = onBack)
                }
                return@Column
            }
            if (state.finished) {
                Results(state, onRetake = onRetake, onOpenTopic = { onOpenTopic(state.course.id, it) }, onDone = onBack)
                return@Column
            }
            val q = state.question ?: return@Column
            QuizHeader(state.label(), right = state.clock ?: if (AppTheme.departures) "%02d/%02d".format(state.index + 1, state.total) else "${state.index + 1} / ${state.total}", rightWarn = (state.secondsLeft ?: Int.MAX_VALUE) < 300, onClose = onBack)
            Segments(state)
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 28.dp, bottom = Spacing.md),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                if (state.kind == QuizKind.MOCK && state.index == 0 && !state.revealed) {
                    Text("Exam conditions: no explanations until the end, ${quiz.timeLimitMinutes} minutes on the clock, pass at ${quiz.passPercent}%.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                }
                AnimatedContent(
                    targetState = q,
                    transitionSpec = { (slideInHorizontally(tween(300)) { it / 3 } + fadeIn(tween(240))) togetherWith (slideOutHorizontally(tween(220)) { -it / 3 } + fadeOut(tween(160))) },
                    label = "question",
                ) { question ->
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        question.scenario?.let { sc ->
                            Text(sc, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.fillMaxWidth().then(OutlinedPanel).padding(16.dp))
                        }
                        question.listen?.let { audio ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                                modifier = Modifier.fillMaxWidth().then(OutlinedPanel).padding(14.dp),
                            ) {
                                SpeakButton(audio, speaker, size = 44.dp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(if (state.course.speechLanguage?.startsWith("de") == true) "HÖREN" else "LISTENING", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.accentText)
                                    Text(if (state.revealed && state.instant) audio else "Play the audio, then answer. The text appears after you answer.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                                }
                            }
                        }
                        Text(
                            question.prompt,
                            style = if (AppTheme.departures) TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 34.sp, lineHeight = 36.sp, color = AppTheme.colors.ink)
                            else MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp, lineHeight = 32.sp, letterSpacing = (-0.5).sp),
                            modifier = Modifier.semantics { heading() },
                        )
                        if (question.multi) Text("Select all that apply.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            question.options.forEachIndexed { i, opt ->
                                OptionRow(
                                    text = opt, index = i, selected = i in state.selected, revealed = state.revealed,
                                    correct = i in question.correct, onClick = { onToggle(i) },
                                )
                            }
                        }
                    }
                }
                AnimatedVisibility(visible = state.revealed && state.instant, enter = expandVertically() + fadeIn()) {
                    val right = state.answers[q.id] == true
                    val tone = if (right) AppTheme.colors.success else AppTheme.colors.destructive
                    val board = AppTheme.departures
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = if (board) Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(if (right) AppTheme.colors.allowedBackground else AppTheme.colors.blockedBackground).padding(14.dp) else Modifier,
                    ) {
                        if (board) BoardLabel(if (right) "Cleared" else "Held at the gate", color = tone, size = 18.sp, weight = FontWeight.Bold, spacing = 0.08.em)
                        else HorizontalDivider(color = tone, thickness = Spacing.rule)
                        if (!board) Text(if (right) "Right." else "Not quite.", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = tone, modifier = Modifier.padding(top = 8.dp))
                        // Why the answer you picked was tempting, before why the right one is right.
                        // Being told about your own mistake teaches more than hearing the model answer again.
                        q.whyWrongFor(state.selected)?.let { why ->
                            Text(why, style = MaterialTheme.typography.bodyMedium, lineHeight = 21.sp, fontWeight = FontWeight.Medium)
                        }
                        Text(q.explanation, style = MaterialTheme.typography.bodyMedium, lineHeight = 21.sp, color = AppTheme.colors.secondaryLabel)
                    }
                }
            }
            Box(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, bottom = 28.dp).navigationBarsPadding()) {
                if (state.revealed && state.instant) {
                    CupertinoPrimaryButton(text = if (state.index + 1 >= state.total) "See results" else "Next question", onClick = onNext)
                } else {
                    val label = if (!state.instant) (if (state.index + 1 >= state.total) "Finish exam" else "Next") else "Check"
                    CupertinoPrimaryButton(text = label, enabled = state.selected.isNotEmpty(), onClick = onCheck)
                }
            }
        }
    }
}

/** The page colour inside a 1.5dp hairline, 16dp corners. */
private val OutlinedPanel: Modifier
    @Composable get() = Modifier.clip(RoundedCornerShape(16.dp)).border(Spacing.rule, AppTheme.colors.separator, RoundedCornerShape(16.dp))

@Composable
private fun QuizHeader(label: String, right: String, rightWarn: Boolean = false, onClose: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 6.dp)) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.offset(x = (-10).dp).size(44.dp).clip(CircleShape).clickable(role = Role.Button, onClickLabel = "Leave the test", onClick = onClose),
        ) { Icon(Icons.Filled.Close, contentDescription = "Leave the test", tint = AppTheme.colors.ink) }
        if (AppTheme.departures) BoardLabel("Security · $label", size = 14.sp, weight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        else Text(label.uppercase(), style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        Text(
            right, style = MaterialTheme.typography.bodyMedium.copy(fontFamily = if (AppTheme.departures) JetBrainsFamily else MonoFamily, fontSize = 13.sp), fontWeight = if (AppTheme.departures) FontWeight.Bold else FontWeight.Medium,
            color = if (rightWarn) AppTheme.colors.destructive else AppTheme.colors.ink,
            textAlign = TextAlign.End, modifier = Modifier.width(54.dp),
        )
    }
}

/** A segment per question: right in green, wrong in red, answered unmarked in ink, this one in ink, the rest hairline. */
@Composable
private fun Segments(state: QuizUiState) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier.fillMaxWidth().padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 14.dp)
            .semantics { contentDescription = "Question ${state.index + 1} of ${state.total}" },
    ) {
        state.asked.forEachIndexed { i, q ->
            val answer = state.answers[q.id]
            val c = when {
                state.instant && answer == true -> AppTheme.colors.success
                state.instant && answer == false -> AppTheme.colors.destructive
                i == state.index && AppTheme.departures -> AppTheme.colors.accent
                i <= state.index -> AppTheme.colors.ink
                else -> AppTheme.colors.separator
            }
            if (AppTheme.departures) Box(Modifier.weight(1f).height(5.dp).clip(RoundedCornerShape(2.dp)).background(c))
            else Box(Modifier.weight(1f).height(3.dp).background(c))
        }
    }
}

@Composable
private fun OptionRow(text: String, index: Int, selected: Boolean, revealed: Boolean, correct: Boolean, onClick: () -> Unit) {
    val board = AppTheme.departures
    val (border, fill, tag, tagInk) = when {
        board && revealed && correct -> listOf(AppTheme.colors.success, AppTheme.colors.allowedBackground, "CLEARED", AppTheme.colors.success)
        board && revealed && selected -> listOf(AppTheme.colors.destructive, AppTheme.colors.blockedBackground, "YOURS", AppTheme.colors.destructive)
        board && selected -> listOf(AppTheme.colors.accent, AppTheme.colors.card, "", AppTheme.colors.secondaryLabel)
        board -> listOf(AppTheme.colors.separator, AppTheme.colors.card, "", AppTheme.colors.secondaryLabel)
        revealed && correct -> listOf(AppTheme.colors.success, AppTheme.colors.success.copy(alpha = 0.08f), "RIGHT", AppTheme.colors.success)
        revealed && selected -> listOf(AppTheme.colors.destructive, AppTheme.colors.destructive.copy(alpha = 0.08f), "YOURS", AppTheme.colors.destructive)
        selected -> listOf(AppTheme.colors.ink, Color.Transparent, "", AppTheme.colors.secondaryLabel)
        else -> listOf(AppTheme.colors.separator, Color.Transparent, "", AppTheme.colors.secondaryLabel)
    }
    val shape = RoundedCornerShape(if (board) 12.dp else 14.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = if (board) 56.dp else 58.dp)
            .clip(shape)
            .background(fill as Color)
            .border(if (selected || (revealed && correct)) 2.dp else if (board) 1.dp else Spacing.rule, border as Color, shape)
            .clickable(enabled = !revealed, role = Role.Checkbox, onClick = onClick)
            .padding(horizontal = if (board) 12.dp else 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        if (board) LetterTile(('A' + index).toString(), if (selected && !revealed) AppTheme.colors.accent else AppTheme.colors.secondaryLabel, size = 28.dp)
        else Text(('A' + index).toString(), style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 13.sp), color = AppTheme.colors.secondaryLabel, modifier = Modifier.width(14.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        if ((tag as String).isNotEmpty()) Text(tag, style = MaterialTheme.typography.labelMedium, color = tagInk as Color)
    }
}

/**
 * The results, as the Parallel prototype draws them: what the sitting was,
 * the score large, whether it passed and what it took, a sentence on what
 * happens next, then what you missed, each with your answer struck through
 * and the right one under it. Where the score came from and the pace follow
 * when there is something to say.
 */
@Composable
private fun Results(state: QuizUiState, onRetake: () -> Unit, onOpenTopic: (String) -> Unit, onDone: () -> Unit) {
    val module = state.module ?: return
    val keeping = state.kind == QuizKind.DAILY || state.kind == QuizKind.REVIEW
    val done = state.passed || keeping
    Column(Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = Spacing.listHorizontal, top = 6.dp)) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.offset(x = (-10).dp).size(44.dp).clip(CircleShape).clickable(role = Role.Button, onClickLabel = "Back to the course", onClick = onDone),
            ) { Icon(Icons.Filled.Close, contentDescription = "Back to the course", tint = AppTheme.colors.ink) }
        }
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = Spacing.listHorizontal).padding(top = 8.dp, bottom = Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val board = AppTheme.departures
            val tone = if (keeping || state.passed) AppTheme.colors.success else AppTheme.colors.warn
            if (board) {
                BoardMono(((if (keeping || state.passed) "Arrived · " else "Held · ") + state.label() + if (state.kind == QuizKind.MODULE) " · ${module.title}" else "").uppercase())
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FlipTiles("%02d".format(state.correctCount), tileWidth = 58.dp, tileHeight = 76.dp, fontSize = 48.sp, color = tone)
                    Text("/ ${state.total}", color = AppTheme.colors.secondaryLabel, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 30.sp), modifier = Modifier.padding(start = 6.dp))
                }
            } else {
            Text(
                (state.label() + if (state.kind == QuizKind.MODULE) " · ${module.title}" else "").uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel,
            )
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.semantics(mergeDescendants = true) {}) {
                Text("${state.correctCount}", style = MaterialTheme.typography.displayLarge.copy(fontSize = 72.sp, lineHeight = 72.sp, letterSpacing = (-2.8).sp), fontWeight = FontWeight.Bold)
                Text("of ${state.total}", style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp), fontWeight = FontWeight.Medium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(bottom = 10.dp))
            }
            }
            val verdict = when {
                keeping -> "Review done"
                state.timedOut -> "Time is up · ${state.percent}%"
                state.passed -> "Passed · ${state.percent}%"
                else -> "Not yet · ${state.percent}%"
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                val minutes = state.seconds.values.sum() / 60
                if (board) {
                    val word = when { keeping -> "Review done"; state.timedOut -> "Time up · ${state.percent}%"; state.passed -> "Cleared · ${state.percent}%"; else -> "Held · ${state.percent}%" }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.height(28.dp).clip(RoundedCornerShape(6.dp)).background(tone).padding(horizontal = 10.dp)) {
                        BoardLabel(word, color = AppTheme.colors.groupedBackground, size = 15.sp, weight = FontWeight.Bold, spacing = 0.1.em)
                    }
                    if (!keeping) BoardMono(("${state.quiz?.passPercent ?: 0}% to pass" + if (minutes > 0) " · $minutes min" else "").uppercase())
                } else {
                Text(
                    verdict, style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp), color = tone,
                    modifier = Modifier.clip(CircleShape).background(tone.copy(alpha = 0.08f)).heightIn(min = 28.dp).padding(horizontal = 10.dp, vertical = 5.dp),
                )
                if (!keeping) Text(
                    "${state.quiz?.passPercent ?: 0}% to pass" + if (minutes > 0) " · $minutes minute${if (minutes == 1) "" else "s"}" else "",
                    style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                )
                }
            }
            Text(
                when (state.kind) {
                    QuizKind.DAILY -> if (state.missedQuestions.isEmpty()) "All held. Each one comes back a little later than last time." else "${state.missedQuestions.size} slipped; those come back tomorrow, the rest later."
                    QuizKind.REVIEW -> if (state.missedQuestions.isEmpty()) "Every missed question is now right. The review set is empty." else "${state.missedQuestions.size} still to get right; they stay in the review set and come back next time."
                    QuizKind.MOCK -> if (state.passed) "Above the ${state.quiz?.passPercent}% the real exam asks for. The minutes count as practice." else "The real exam needs ${state.quiz?.passPercent}%. The missed questions below are in your review set; reread their topics and sit it again."
                    QuizKind.MODULE -> if (state.passed) "${module.title} is ticked. The minutes are in your hours." else "You need ${state.quiz?.passPercent}%. Read the weak topics below and try again; retakes are unlimited."
                },
                style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = 6.dp),
            )

            val missed = state.missedQuestions
            if (missed.isNotEmpty()) {
                if (board) {
                    BoardLabel("Held at the gate · ${missed.size}", size = 13.sp, modifier = Modifier.padding(top = 14.dp, bottom = 8.dp).semantics { heading() })
                    Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                } else {
                HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule, modifier = Modifier.padding(top = 14.dp))
                Text("What you missed", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 6.dp).semantics { heading() })
                }
                missed.forEach { q ->
                    val pace = state.pace[q.id]
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().clickable(onClickLabel = "Reread the topic") { onOpenTopic(q.topicId) }.padding(vertical = 12.dp),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(q.prompt, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                            when (pace) {
                                Pace.Verdict.QUICK -> Text("ANSWERED FAST", style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp), color = AppTheme.colors.destructive)
                                Pace.Verdict.SLOW -> Text("WORKED OUT", style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp), color = AppTheme.colors.secondaryLabel)
                                else -> {}
                            }
                        }
                        val yours = state.picked[q.id].orEmpty().map { q.options.getOrNull(it) }.filterNotNull()
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = if (board) Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(AppTheme.colors.card).padding(horizontal = 10.dp, vertical = 8.dp) else Modifier,
                        ) {
                            if (yours.isNotEmpty()) Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("YOURS", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.destructive, modifier = Modifier.width(48.dp).padding(top = 2.dp))
                                Text(yours.joinToString(", "), style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, textDecoration = TextDecoration.LineThrough)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("RIGHT", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.success, modifier = Modifier.width(48.dp).padding(top = 2.dp))
                                Text(q.options.filterIndexed { i, _ -> i in q.correct }.joinToString(", "), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        // Why the option you picked was tempting, before why the right one is right.
                        Text(
                            q.whyWrongFor(state.picked[q.id].orEmpty()) ?: q.explanation,
                            style = MaterialTheme.typography.bodySmall, lineHeight = 19.sp, color = AppTheme.colors.secondaryLabel,
                        )
                        state.seconds[q.id]?.let { t -> Text(paceNote(t, pace).trim(), style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 11.sp), color = AppTheme.colors.secondaryLabel) }
                    }
                    HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                }
            }

            // Where the score came from, so it points somewhere: by module for a paper
            // that spans the course, by topic for one module's own test. One area is
            // just the score again, said twice.
            val pass = state.quiz?.passPercent ?: 80
            val areas = state.course
                ?.breakdown(state.asked, state.answers.filterValues { it }.keys, if (state.kind == QuizKind.MODULE) ScoreBy.TOPIC else ScoreBy.MODULE)
                .orEmpty()
            if (areas.size > 1) {
                HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule, modifier = Modifier.padding(top = 14.dp))
                Text("How it went", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 6.dp).semantics { heading() })
                // The weak area is a place to go, not a sentence about one. It is
                // shown whether or not the paper was passed: passing at 82% with
                // one area at 40% is exactly when nobody goes back on their own.
                val weak = areas.weakest(pass).map { it.id }.toSet()
                areas.forEach { a ->
                    val tone = when {
                        a.clean -> AppTheme.colors.success
                        a.percent >= pass -> AppTheme.colors.ink
                        else -> AppTheme.colors.warn
                    }
                    val byTopic = state.kind == QuizKind.MODULE
                    val destination = if (byTopic) a.id else {
                        missed.firstOrNull { q -> state.course?.let { Courses.topic(it.id, q.topicId) }?.first?.id == a.id }?.topicId
                            ?: state.course?.modules?.firstOrNull { it.id == a.id }?.topics?.firstOrNull()?.id
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth().then(if (destination != null) Modifier.clickable { onOpenTopic(destination) } else Modifier).padding(vertical = 8.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(a.label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                            if (a.id in weak) Text("START HERE  ", style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp), color = AppTheme.colors.warn)
                            Text("${a.correct} / ${a.asked}", style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily), color = tone)
                        }
                        Box(Modifier.fillMaxWidth().height(4.dp).background(AppTheme.colors.separator)) {
                            Box(Modifier.fillMaxWidth((a.percent / 100f).coerceIn(0.01f, 1f)).height(4.dp).background(tone))
                        }
                    }
                }
            }
            PaceNote(state)
            state.message?.let { Text(it, color = AppTheme.colors.destructive, style = MaterialTheme.typography.bodySmall) }
        }
        Column(Modifier.navigationBarsPadding()) {
            HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 12.dp, bottom = 28.dp)) {
                CupertinoSecondaryButton(text = "Done", onClick = onDone, modifier = Modifier.weight(1f))
                // A spaced sitting is not retaken: the schedule decides when each question returns.
                if (!keeping) CupertinoPrimaryButton(text = if (AppTheme.departures) (if (done) "Board again" else "Re-board") else if (done) "Take it again" else "Try again", onClick = onRetake, modifier = Modifier.weight(1.4f))
            }
        }
    }
}

/**
 * Where the time went.
 *
 * A paper's total was already in the logged minutes, which tells nobody
 * anything: the useful reading is the shape of it. Two answers given faster
 * than the question can be read is a different sitting from two that took
 * three minutes each, and until a sitting kept a clock per question neither
 * could be said.
 *
 * Only shown where there is a clock and more than a couple of questions.
 * Older attempts carry no seconds at all and simply do not get this.
 */
@Composable
private fun PaceNote(state: QuizUiState) {
    val timed = state.asked.filter { state.seconds[it.id] != null }
    if (timed.size < 3) return
    val snap = timed.filter { state.pace[it.id] == Pace.Verdict.QUICK }
    val worked = timed.filter { state.pace[it.id] == Pace.Verdict.SLOW }
    if (snap.isEmpty() && worked.isEmpty()) return
    val snapWrong = snap.count { state.answers[it.id] != true }
    val total = timed.sumOf { state.seconds[it.id] ?: 0 }

    HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule, modifier = Modifier.padding(top = 14.dp))
    Text("Pace", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 6.dp).semantics { heading() })
    Text("${total / 60} min ${total % 60} sec across ${timed.size} questions.", style = MaterialTheme.typography.bodyMedium)
    if (snap.isNotEmpty()) {
        Text(
            if (snapWrong > 0) {
                "${snap.size} answered faster than the question can be read, and $snapWrong of those were wrong. Those are the ones to be careful of: you were sure."
            } else {
                "${snap.size} answered faster than the question can be read. All of them right, so you know those."
            },
            style = MaterialTheme.typography.bodySmall,
            color = if (snapWrong > 0) AppTheme.colors.warn else AppTheme.colors.secondaryLabel,
        )
    }
    if (worked.isNotEmpty()) {
        Text(
            "${worked.size} took more than twice as long as they should. Those were worked out, not known, so they come back on the same gap rather than a longer one.",
            style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
        )
    }
}

private fun paceNote(seconds: Int, verdict: Pace.Verdict?): String {
    val clock = if (seconds >= 60) "${seconds / 60}m ${seconds % 60}s" else "${seconds}s"
    return when (verdict) {
        Pace.Verdict.QUICK -> "  $clock — too fast to have read it"
        Pace.Verdict.SLOW -> "  $clock — worked out"
        else -> "  $clock"
    }
}
