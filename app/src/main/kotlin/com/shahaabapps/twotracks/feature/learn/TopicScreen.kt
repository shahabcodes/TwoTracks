package com.shahaabapps.twotracks.feature.learn

import androidx.compose.ui.unit.em
import androidx.compose.ui.text.TextStyle
import com.shahaabapps.core.ui.theme.CondensedFamily
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardBack
import com.shahaabapps.twotracks.ui.board.BoardGutter
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.BoxWithConstraints
import com.shahaabapps.core.ui.theme.MonoFamily
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.Role
import androidx.compose.material3.Scaffold
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.content.Block
import com.shahaabapps.core.content.CalloutKind
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.PopIn
import androidx.compose.ui.text.style.TextAlign
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.content.solved
import com.shahaabapps.core.content.accepts
import androidx.compose.foundation.border

/** One lesson: the blocks rendered in order, then "Mark as learned" which logs the reading time. */
@Composable
fun TopicScreen(
    onBack: () -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onOpenQuiz: (String, String) -> Unit,
    onListen: (String, String) -> Unit,
    onOpenNotes: (String, String) -> Unit,
    viewModel: TopicViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val speakerState by viewModel.speaker.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.course?.speechLanguage) { state.course?.speechLanguage?.let { viewModel.speaker.setLanguage(it) } }
    TopicContent(
        state = state,
        speaker = if (state.course?.speechLanguage != null && speakerState == Speaker.State.READY) viewModel.speaker else null,
        voiceMissing = speakerState == Speaker.State.MISSING_LANGUAGE,
        recognizer = viewModel.recognizer,
        onBack = onBack, onOpenTopic = onOpenTopic, onOpenQuiz = onOpenQuiz,
        onInstallVoice = viewModel.speaker::openInstallPage,
        onChecked = viewModel::checkAnswered, onConfidence = viewModel::checkConfidence,
        onMarkLearned = viewModel::markLearned, onUnlearn = viewModel::unlearn, onMessageShown = viewModel::clearMessage,
        // The card opens the telling it shows: the one playing, or the one last picked.
        onListen = { state.course?.id?.let { c -> state.topic?.id?.let { t -> onListen(c, state.listen?.episodeId?.ifEmpty { null } ?: t) } } },
        onOpenNotes = { state.course?.id?.let { c -> state.topic?.id?.let { t -> onOpenNotes(c, t) } } },
    )
}

/** A lesson drawn from its state alone, so a preview can show it without an account. */
@Composable
fun TopicContent(
    state: TopicUiState,
    speaker: Speaker?,
    voiceMissing: Boolean,
    recognizer: Recognizer?,
    onBack: () -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onOpenQuiz: (String, String) -> Unit,
    onInstallVoice: () -> Unit,
    onChecked: (com.shahaabapps.core.content.Question, Boolean, Int, Int) -> Unit,
    onConfidence: (com.shahaabapps.core.content.Question, Boolean) -> Unit,
    onMarkLearned: () -> Unit,
    onUnlearn: () -> Unit,
    onMessageShown: () -> Unit,
    onListen: () -> Unit,
    onOpenNotes: () -> Unit = {},
) {
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(state.message) { state.message?.let { snackbar.showSnackbar(it); onMessageShown() } }
    val topic = state.topic
    val module = state.module
    val course = state.course
    LessonScaffold(
        label = listOfNotNull(course?.title, module?.let { if (AppTheme.departures) "M${it.number}" else "Module ${it.number}" }).joinToString(" · "),
        index = state.index, count = state.count, onBack = onBack,
    ) {
        if (topic == null || module == null || course == null) return@LessonScaffold
        Box {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(horizontal = Spacing.listHorizontal, vertical = Spacing.sm).let { PaddingValues(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = Spacing.sm, bottom = Spacing.xl * 2) }) {
                item {
                    Text(
                        topic.title,
                        style = if (AppTheme.departures) TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 36.sp, lineHeight = 37.sp, color = AppTheme.colors.ink)
                        else MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp, lineHeight = 33.sp, letterSpacing = (-0.6).sp),
                        modifier = Modifier.padding(top = 4.dp).semantics { heading() },
                    )
                    Text(
                        "TOPIC ${state.index + 1} OF ${state.count} · ${topic.minutes} MIN",
                        style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = 8.dp),
                    )
                }
                state.listen?.let { listen -> item { com.shahaabapps.twotracks.feature.listen.ListenCard(listen, onListen) } }
                com.shahaabapps.core.content.Notes.forTopic(topic.id)?.let { sheet -> item { NotesCard(sheet, onOpenNotes) } }
                if (course.speechLanguage != null && voiceMissing) {
                    item {
                        Text(
                            "${course.languageName} voice not installed, so the speaker buttons are off. Tap here to open the download page.",
                            style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.warn,
                            modifier = Modifier.clickable(onClick = onInstallVoice),
                        )
                    }
                }
                items(topic.blocks.size) { i -> BlockView(topic.blocks[i], speaker, recognizer, course.speechLanguage, onChecked, onConfidence) }
                if (topic.references.isNotEmpty()) {
                    item { ReferencesBlock(topic.references) }
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.padding(top = Spacing.md)) {
                        if (state.learned) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                                PopIn { Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = AppTheme.colors.success) }
                                Text("Learned. The minutes are in your ${course.title} hours.", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.success)
                            }
                            val nextId = state.nextTopicId
                            if (nextId != null) {
                                CupertinoPrimaryButton(text = "Next topic", onClick = { onOpenTopic(course.id, nextId) })
                            } else if (module.quiz != null) {
                                CupertinoPrimaryButton(text = "Take the module test", onClick = { onOpenQuiz(course.id, module.id) })
                            }
                            CupertinoTextButton(text = "Mark as not learned", onClick = onUnlearn)
                        } else {
                            CupertinoPrimaryButton(text = "Mark as learned", onClick = onMarkLearned)
                            Text(
                                if (topic.blocks.any { it is Block.Vocab }) "Marks the topic done, adds the time to your hours and puts these words into your flashcard deck."
                                else "Marks the topic done and adds your reading time to the ${course.skillName(module.componentId)} skill.",
                                style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                            )
                        }
                    }
                }
            }
            SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun BlockView(
    block: Block,
    speaker: Speaker? = null,
    recognizer: Recognizer? = null,
    lang: String? = null,
    onChecked: (com.shahaabapps.core.content.Question, Boolean, Int, Int) -> Unit = { _, _, _, _ -> },
    onConfidence: (com.shahaabapps.core.content.Question, Boolean) -> Unit = { _, _ -> },
) {
    when (block) {
        is Block.Heading -> Text(block.text, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = Spacing.sm).semantics { heading() })
        is Block.Paragraph -> Text(block.text, style = MaterialTheme.typography.bodyLarge, lineHeight = 25.sp)
        is Block.Bullets -> Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            block.items.forEach { item ->
                Row(verticalAlignment = Alignment.Top) {
                    Box(modifier = Modifier.padding(top = 10.dp, end = 12.dp).size(5.dp).background(AppTheme.colors.ink))
                    Text(item, style = MaterialTheme.typography.bodyLarge, lineHeight = 24.sp, modifier = Modifier.weight(1f))
                }
            }
        }
        is Block.Callout -> Callout(block)
        is Block.Code -> CodeBlock(block)
        is Block.Table -> TableBlock(block)
        is Block.Diagram -> DiagramView(block.spec)
        is Block.Steps -> StepsBlock(block)
        is Block.Compare -> CompareBlock(block)
        is Block.TryIt -> TryItBlock(block)
        is Block.Check -> CheckBlock(block, onChecked, onConfidence)
        is Block.Warmup -> WarmupBlock(block)
        is Block.Faded -> FadedBlock(block)
        is Block.Glossary -> GlossaryBlock(block)
        is Block.Commands -> CommandsBlock(block)
        is Block.Vocab -> VocabBlock(block, speaker)
        is Block.Dialogue -> DialogueBlock(block, speaker)
        is Block.Exercise -> ExerciseBlock(block, speaker, recognizer, lang)
        // Tinted rather than filled. A solid accent slab with white prose on
        // it measured 4.02:1 on Blue in light and 1.82:1 on Meadow in dark —
        // the same number the US calendar rejected for a white date on a
        // solid day. A tint with the ordinary ink reads on every palette.
        is Block.Recap -> Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
            Text("REMEMBER THESE THREE", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.accentText, modifier = Modifier.padding(top = 4.dp))
            block.items.forEachIndexed { i, item ->
                Row(verticalAlignment = Alignment.Top) {
                    Text("%02d".format(i + 1), style = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFamily), color = AppTheme.colors.secondaryLabel, modifier = Modifier.width(30.dp))
                    Text(item, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun Callout(block: Block.Callout) {
    val (icon, label, tint) = when (block.kind) {
        CalloutKind.PLAIN_WORDS -> Triple(Icons.Filled.Translate, "In plain words", AppTheme.colors.accentText)
        CalloutKind.EXAM_TIP -> Triple(Icons.Filled.School, "Exam tip", AppTheme.colors.warn)
        CalloutKind.REMEMBER -> Triple(Icons.Filled.PushPin, "Remember this", AppTheme.colors.success)
        CalloutKind.EXAMPLE -> Triple(Icons.Filled.Lightbulb, "For Two Tracks", AppTheme.colors.secondaryLabel)
        CalloutKind.MISTAKES -> Triple(Icons.Filled.Warning, "Common mistakes", AppTheme.colors.destructive)
        CalloutKind.WORKED -> Triple(Icons.Filled.Build, "Worked example", AppTheme.colors.accentText)
    }
    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(Modifier.width(3.dp).fillMaxHeight().background(tint))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(14.dp))
                Text(label.uppercase(), style = MaterialTheme.typography.labelMedium, color = tint)
            }
            Text(block.text, style = MaterialTheme.typography.bodyMedium, lineHeight = 22.sp)
        }
    }
}

@Composable
private fun CodeBlock(block: Block.Code) {
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFF17181B))) {
        Text(block.language, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.55f), modifier = Modifier.padding(start = Spacing.md, top = Spacing.sm))
        Text(
            block.code, fontFamily = MonoFamily, fontSize = 12.sp, lineHeight = 18.sp, color = Color(0xFFECEDEF),
            modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = Spacing.md, vertical = Spacing.sm),
        )
    }
}

@Composable
private fun TableBlock(block: Block.Table) {
    // The prototype's three columns share the width. A table with more than
    // three gives each column room for its longest word and scrolls sideways,
    // because sharing the width five ways broke "masculine" across two lines.
    val columns = block.headers.size
    val wide = columns > 3
    val board = AppTheme.departures
    BoxWithConstraints(modifier = Modifier.fillMaxWidth().then(if (board) Modifier.clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.card).padding(horizontal = 14.dp, vertical = 4.dp) else Modifier)) {
        val tableWidth = if (wide) maxOf(maxWidth, 96.dp * columns + 10.dp * (columns - 1)) else maxWidth
        val cell: RowScope.() -> Modifier = { if (wide) Modifier.weight(1f).widthIn(min = 96.dp) else Modifier.weight(1f) }
        Column(modifier = if (wide) Modifier.horizontalScroll(rememberScrollState()) else Modifier) {
            Column(modifier = Modifier.width(tableWidth)) {
                if (!board) HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    block.headers.forEach { h -> Text(h.uppercase(), style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp), color = AppTheme.colors.secondaryLabel, modifier = cell()) }
                }
                HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                block.rows.forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        row.forEachIndexed { j, text ->
                            Text(
                                text, style = MaterialTheme.typography.bodySmall, lineHeight = 18.sp,
                                fontWeight = if (j == 0) FontWeight.Medium else FontWeight.Normal,
                                modifier = cell(),
                            )
                        }
                    }
                    HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                }
            }
        }
    }
}

@Composable
private fun StepsBlock(block: Block.Steps) {
    Column(modifier = Modifier.fillMaxWidth().then(Panel).padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(block.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        block.steps.forEachIndexed { i, st ->
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("%02d".format(i + 1), style = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFamily), color = AppTheme.colors.secondaryLabel, modifier = Modifier.width(26.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(st.action, style = MaterialTheme.typography.bodyMedium, lineHeight = 21.sp)
                    st.expect?.let { Text("You should see: $it", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel) }
                }
            }
        }
    }
}

@Composable
private fun CompareBlock(block: Block.Compare) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        Text(block.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            block.cards.forEach { c ->
                Column(modifier = Modifier.width(230.dp).then(Panel).padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(c.title, style = MaterialTheme.typography.titleMedium, color = AppTheme.colors.accentText, fontWeight = FontWeight.SemiBold)
                    c.points.forEach { pt ->
                        Row(verticalAlignment = Alignment.Top) {
                            Box(modifier = Modifier.padding(top = 7.dp, end = 8.dp).size(5.dp).clip(CircleShape).background(AppTheme.colors.secondaryLabel))
                            Text(pt, style = MaterialTheme.typography.bodySmall, lineHeight = 18.sp, modifier = Modifier.weight(1f))
                        }
                    }
                    Text("Pick when: ${c.pickWhen}", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.success, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}

@Composable
private fun TryItBlock(block: Block.TryIt) {
    val ticked = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateListOf<Int>() }
    Column(modifier = Modifier.fillMaxWidth().then(Panel).padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Filled.Science, contentDescription = null, tint = AppTheme.colors.success, modifier = Modifier.size(18.dp))
            Text(block.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = AppTheme.colors.success)
        }
        Text(block.intro, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
        block.checklist.forEachIndexed { i, item ->
            val on = i in ticked
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().clickable { if (on) ticked.remove(i) else ticked.add(i) },
            ) {
                Icon(if (on) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank, contentDescription = null, tint = if (on) AppTheme.colors.success else AppTheme.colors.secondaryLabel, modifier = Modifier.size(22.dp))
                Text(item, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CheckBlock(
    block: Block.Check,
    onChecked: (com.shahaabapps.core.content.Question, Boolean, Int, Int) -> Unit = { _, _, _, _ -> },
    onConfidence: (com.shahaabapps.core.content.Question, Boolean) -> Unit = { _, _ -> },
) {
    val q = block.question
    var picked by androidx.compose.runtime.remember(q.id) { androidx.compose.runtime.mutableStateOf<Int?>(null) }
    // Null until the answer is committed. Asking before the reveal is the
    // whole point: afterwards everyone remembers having been fairly sure.
    var sure by androidx.compose.runtime.remember(q.id) { androidx.compose.runtime.mutableStateOf<Boolean?>(null) }
    // When this check came into being, which in a lazy list is about when it
    // was scrolled towards rather than when the topic was opened.
    val shownAt = androidx.compose.runtime.remember(q.id) { System.currentTimeMillis() }
    val revealed = picked != null && sure != null
    val right = picked in q.correct

    val board = AppTheme.departures
    val shape = RoundedCornerShape(if (board) 16.dp else 18.dp)
    Column(
        modifier = Modifier.fillMaxWidth().clip(shape).border(Spacing.rule, if (board) AppTheme.colors.accent else AppTheme.colors.ink, shape).padding(if (board) 16.dp else 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (board) BoardLabel("Gate check", color = AppTheme.colors.accent, size = 14.sp, weight = FontWeight.Bold)
        else Text("QUICK CHECK", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.accentText)
        Text(q.prompt, style = if (board) TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 27.sp, color = AppTheme.colors.ink) else MaterialTheme.typography.titleMedium.copy(fontSize = 19.sp, lineHeight = 24.sp))

        // Asked before the answer can be seen. A confident wrong answer is the
        // most useful thing a check can find, and afterwards everyone
        // remembers having been fairly sure.
        if (!revealed) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Before you see it, how sure are you?", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    listOf(true to "Sure", false to "Not sure").forEach { (value, label) ->
                        val on = sure == value
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(10.dp))
                                .background(if (on) AppTheme.colors.accent else if (board) AppTheme.colors.card else Color.Transparent)
                                .border(if (board && !on) 1.dp else Spacing.rule, if (on) AppTheme.colors.accent else AppTheme.colors.separator, RoundedCornerShape(10.dp))
                                .selectable(selected = on, role = Role.RadioButton) { sure = value; onConfidence(q, value) },
                        ) {
                            if (board) BoardLabel(label, color = if (on) AppTheme.colors.onSolid else AppTheme.colors.ink, size = 16.sp, weight = FontWeight.Bold, spacing = 0.1.em)
                            else Text(label, style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp), color = if (on) AppTheme.colors.onSolid else AppTheme.colors.ink)
                        }
                    }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            q.options.forEachIndexed { i, opt ->
                val state = when {
                    !revealed -> null
                    i in q.correct -> true
                    i == picked -> false
                    else -> null
                }
                val (border, fill, tag, tagInk) = when {
                    board && state == true -> Quad(AppTheme.colors.success, AppTheme.colors.allowedBackground, "CLEARED", AppTheme.colors.success)
                    board && state == false -> Quad(AppTheme.colors.destructive, AppTheme.colors.blockedBackground, "YOURS", AppTheme.colors.destructive)
                    board && !revealed && picked == i -> Quad(AppTheme.colors.accent, AppTheme.colors.card, "", AppTheme.colors.secondaryLabel)
                    board -> Quad(AppTheme.colors.separator, AppTheme.colors.card, "", AppTheme.colors.secondaryLabel)
                    state == true -> Quad(AppTheme.colors.success, AppTheme.colors.success.copy(alpha = 0.08f), "RIGHT", AppTheme.colors.success)
                    state == false -> Quad(AppTheme.colors.destructive, AppTheme.colors.destructive.copy(alpha = 0.08f), "YOURS", AppTheme.colors.destructive)
                    !revealed && picked == i -> Quad(AppTheme.colors.ink, Color.Transparent, "", AppTheme.colors.secondaryLabel)
                    else -> Quad(AppTheme.colors.separator, Color.Transparent, "", AppTheme.colors.secondaryLabel)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp).clip(RoundedCornerShape(12.dp))
                        .background(fill)
                        .border(if (state != null || picked == i) 2.dp else Spacing.rule, border, RoundedCornerShape(12.dp))
                        // Recorded on the pick, as it always was: someone who answers
                        // and scrolls on should not lose the answer to a new step.
                        .clickable(enabled = !revealed) {
                            picked = i
                            val took = ((System.currentTimeMillis() - shownAt) / 1_000L).toInt().coerceAtLeast(1)
                            onChecked(q, i in q.correct, i, took)
                        }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                ) {
                    if (board) LetterTile(('A' + i).toString(), if (picked == i && !revealed) AppTheme.colors.accent else AppTheme.colors.secondaryLabel)
                    else Text(('A' + i).toString(), style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 12.sp), color = AppTheme.colors.secondaryLabel, modifier = Modifier.width(14.dp))
                    Text(opt, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                    if (tag.isNotEmpty()) Text(tag, style = MaterialTheme.typography.labelMedium, color = tagInk)
                }
            }
        }
        if (picked != null && sure == null) {
            Text("Say how sure you were to see the answer.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
        }

        if (revealed) {
            // A confident miss is worth naming. Being sure and wrong is what
            // leaves a belief in place, and it is the one worth going back for.
            val heading = when {
                board && right -> "CLEARED. ON TO THE NEXT STOP."
                board && sure == true -> "HELD. YOU WERE SURE, SO THIS COMES BACK FIRST."
                board -> "HELD. THIS COMES BACK TOMORROW."
                right && sure == true -> "Right, and you knew it."
                right -> "Right."
                sure == true -> "Not quite, and you were sure, which is the kind worth rereading."
                else -> "Not quite."
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = 4.dp)) {
                if (board) BoardLabel(heading, color = if (right) AppTheme.colors.success else AppTheme.colors.destructive, size = 18.sp, weight = FontWeight.Bold, spacing = 0.06.em)
                else Text(heading, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = if (right) AppTheme.colors.success else AppTheme.colors.destructive)
                // Why the option you picked was tempting, before why the right one
                // is right: the quiz screen has done this since 0.38, and the
                // in-topic checks were written notes nobody could see.
                q.whyWrongFor(setOfNotNull(picked))?.let { why ->
                    Text(why, style = MaterialTheme.typography.bodyMedium, lineHeight = 21.sp, fontWeight = FontWeight.Medium)
                }
                Text(q.explanation, style = MaterialTheme.typography.bodyMedium, lineHeight = 21.sp, color = AppTheme.colors.secondaryLabel)
            }
        }
    }
}

private data class Quad(val border: Color, val fill: Color, val tag: String, val tagInk: Color)

/** A block's frame: the page colour inside a 1.5dp hairline, 16dp corners. */
private val Panel: Modifier
    @Composable get() = Modifier.clip(RoundedCornerShape(16.dp)).border(Spacing.rule, AppTheme.colors.separator, RoundedCornerShape(16.dp))

/**
 * The lesson's own header, as the prototype draws it: the back button, the
 * course and module in mono across the middle, a segment per topic in the
 * module filled up to this one, and the page under it.
 */
@Composable
private fun LessonScaffold(label: String, index: Int, count: Int, onBack: () -> Unit, content: @Composable () -> Unit) {
    if (AppTheme.departures) {
        BoardLessonScaffold(label, index, count, onBack, content)
        return
    }
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        Column(modifier = Modifier.fillMaxSize().padding(inner)) {
            Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 6.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.offset(x = (-10).dp).size(44.dp).clip(CircleShape).clickable(role = Role.Button, onClickLabel = "Back to the course", onClick = onBack),
                    ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to the course", tint = AppTheme.colors.ink) }
                    Text(label.uppercase(), style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, textAlign = TextAlign.Center, maxLines = 1, modifier = Modifier.weight(1f))
                    Box(Modifier.width(34.dp))
                }
                if (count > 0) {
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp), modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Topic ${index + 1} of $count" }) {
                        repeat(count) { i -> Box(Modifier.weight(1f).height(3.dp).background(if (i <= index) AppTheme.colors.accentText else AppTheme.colors.separator)) }
                    }
                }
            }
            content()
        }
    }
}

/**
 * A guess before the reading.
 *
 * Nothing is recorded, by design: a wrong answer here is the point, not a
 * weakness to be scheduled. The explanation is withheld too, and only the
 * right option is shown — being told why would spend the reason to read on,
 * which is the whole reason this is at the top of the page.
 */
@Composable
private fun WarmupBlock(block: Block.Warmup) {
    val q = block.question
    var picked by androidx.compose.runtime.remember(q.id) { androidx.compose.runtime.mutableStateOf<Int?>(null) }
    val answered = picked != null
    val right = picked in q.correct
    // The card surface, as every other panel in the app uses. This block
    // shipped on fill at 0.6 alpha, which turns a 12% pill tint into a solid
    // mid-grey slab: the label and the hint on it measured 2.7:1 in light and
    // 2.9:1 in dark, where body text wants 4.5:1. On the card they are 6.1:1
    // and 6.5:1.
    Column(
        modifier = Modifier.fillMaxWidth().then(Panel).padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("BEFORE YOU READ", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
        Text(q.prompt, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, lineHeight = 21.sp)
        if (!answered) {
            Text("Have a guess. You are not expected to know this yet.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
        }
        q.options.forEachIndexed { i, opt ->
            val chosen = picked == i
            val correct = answered && i in q.correct
            // Drawn as the quick check draws its options: outlined, with the
            // right one and yours tagged once the guess is in.
            val (border, fill, tag, tagInk) = when {
                correct -> Quad(AppTheme.colors.success, AppTheme.colors.success.copy(alpha = 0.08f), "RIGHT", AppTheme.colors.success)
                answered && chosen -> Quad(AppTheme.colors.destructive, AppTheme.colors.destructive.copy(alpha = 0.08f), "YOURS", AppTheme.colors.destructive)
                else -> Quad(AppTheme.colors.separator, Color.Transparent, "", AppTheme.colors.secondaryLabel)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp).clip(RoundedCornerShape(12.dp))
                    .background(fill)
                    .border(if (correct || (answered && chosen)) 2.dp else Spacing.rule, border, RoundedCornerShape(12.dp))
                    .clickable(enabled = !answered) { picked = i }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
            ) {
                Text(('A' + i).toString(), style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 12.sp), color = AppTheme.colors.secondaryLabel, modifier = Modifier.width(14.dp))
                Text(opt, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                if (tag.isNotEmpty()) Text(tag, style = MaterialTheme.typography.labelMedium, color = tagInk)
            }
        }
        if (answered) {
            Text(
                if (right) "That is it. Read on and see why." else "Not that one. Reading it now is what makes the answer stick.",
                style = MaterialTheme.typography.bodySmall, lineHeight = 19.sp, fontWeight = FontWeight.SemiBold,
                // Not secondaryLabel: this is the sentence that matters when the
                // guess was wrong, so it gets the full-strength ink.
                color = if (right) AppTheme.colors.success else MaterialTheme.colorScheme.onSurface,
            )
            Text(q.explanation, style = MaterialTheme.typography.bodySmall, lineHeight = 19.sp, color = AppTheme.colors.secondaryLabel)
        }
    }
}

/**
 * A worked example with one step removed, for the reader to finish.
 *
 * Nothing is recorded: this is practice placed between reading a solution and
 * writing one, not an assessment. The answer can be revealed, because someone
 * stuck on step two should not be stuck on the rest of the topic.
 */
@Composable
private fun FadedBlock(block: Block.Faded) {
    var typed by androidx.compose.runtime.remember(block.id) { androidx.compose.runtime.mutableStateOf("") }
    var tried by androidx.compose.runtime.remember(block.id) { androidx.compose.runtime.mutableStateOf(false) }
    var shown by androidx.compose.runtime.remember(block.id) { androidx.compose.runtime.mutableStateOf(false) }
    val right = block.accepts(typed)
    val done = right || shown
    Column(
        modifier = Modifier.fillMaxWidth().then(Panel).padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("NOW YOU FINISH IT", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.accentText)
        Text(block.scenario, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, lineHeight = 21.sp)
        block.steps.forEachIndexed { i, step ->
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("${i + 1}", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, fontWeight = FontWeight.Bold)
                if (i == block.blank) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            if (done) block.solved() else step.replace("___", "______"),
                            style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp,
                            fontWeight = if (done) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (done) AppTheme.colors.success else MaterialTheme.colorScheme.onSurface,
                        )
                        if (!done) {
                            CupertinoTextField(
                                value = typed,
                                onValueChange = { typed = it; tried = false },
                                placeholder = "The missing step",
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                } else {
                    Text(step, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp, modifier = Modifier.weight(1f), color = AppTheme.colors.secondaryLabel)
                }
            }
        }
        when {
            done -> Text(block.note, style = MaterialTheme.typography.bodySmall, lineHeight = 19.sp, color = AppTheme.colors.secondaryLabel)
            tried -> Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Not that. Try once more.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.destructive, fontWeight = FontWeight.SemiBold)
                Text(
                    "Show me",
                    style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.accentText, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { shown = true },
                )
            }
            else -> CupertinoTextButton(text = "Check", onClick = { tried = true })
        }
    }
}

@Composable
private fun CommandsBlock(block: Block.Commands) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        Text(block.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        block.items.forEach { c ->
            Column(modifier = Modifier.fillMaxWidth().then(Panel).padding(bottom = Spacing.md), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                // The command itself gets the code treatment, because that is
                // what it is: something to be typed exactly, and scrollable
                // sideways rather than wrapped, so a long one stays readable
                // as one line instead of breaking where a reader would retype
                // the break.
                Text(
                    c.command, fontFamily = MonoFamily, fontSize = 12.sp, lineHeight = 18.sp,
                    color = Color(0xFFECEDEF),
                    modifier = Modifier
                        .fillMaxWidth()
                        // No corners of its own: the panel's own clip rounds
                        // the top two, so the strip meets the frame exactly.
                        .background(Color(0xFF17181B))
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = Spacing.md, vertical = Spacing.sm),
                )
                CommandLine("When", c.use)
                CommandLine("Why", c.why)
            }
        }
    }
}

/** One labelled line under a command: the same two words in front of every one of them. */
@Composable
private fun CommandLine(label: String, text: String) {
    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(horizontal = Spacing.md)) {
        Text(
            label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold,
            color = AppTheme.colors.secondaryLabel, modifier = Modifier.width(44.dp),
        )
        Text(text, style = MaterialTheme.typography.bodySmall, lineHeight = 18.sp, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun GlossaryBlock(block: Block.Glossary) {
    Column(modifier = Modifier.fillMaxWidth().then(Panel).padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Words to know", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        block.terms.forEach { t ->
            Row(verticalAlignment = Alignment.Top) {
                Text(t.term, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.width(120.dp))
                Text(t.meaning, style = MaterialTheme.typography.bodySmall, lineHeight = 18.sp, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ReferencesBlock(refs: List<com.shahaabapps.core.content.Reference>) {
    val uri = androidx.compose.ui.platform.LocalUriHandler.current
    Column(modifier = Modifier.fillMaxWidth().then(Panel).padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val host = refs.firstOrNull()?.url.orEmpty()
        val source = when {
            host.contains("learn.microsoft.com") -> "Microsoft Learn"
            host.contains("goethe.de") -> "the Goethe-Institut"
            host.contains("ielts.org") -> "IELTS"
            else -> "the official site"
        }
        Text("Read more on $source", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        refs.forEach { r ->
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().clickable { uri.openUri(r.url) }.padding(vertical = 4.dp),
            ) {
                Icon(Icons.Filled.OpenInNew, contentDescription = null, tint = AppTheme.colors.accentText, modifier = Modifier.size(16.dp))
                Text(r.title, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.accentText, modifier = Modifier.weight(1f))
            }
        }
        Text("Official pages open in your browser.", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.secondaryLabel)
    }
}

/**
 * The lesson's header on the Departures board: IN TRANSIT and where, the stop
 * you are at out of the module's, and the topics as stops on a line with this
 * one ringed.
 */
@Composable
private fun BoardLessonScaffold(label: String, index: Int, count: Int, onBack: () -> Unit, content: @Composable () -> Unit) {
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        Column(modifier = Modifier.fillMaxSize().padding(inner)) {
            Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(44.dp)) {
                    BoardBack(onBack, "Back to the course")
                    BoardLabel("In transit · $label", size = 13.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                    BoardMono(if (count > 0) "${index + 1}/$count" else "", modifier = Modifier.width(34.dp), textAlign = TextAlign.End, spacing = 0.sp)
                }
                if (count > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Stop ${index + 1} of $count" }) {
                        repeat(count) { i ->
                            when {
                                i < index -> Box(Modifier.size(10.dp).clip(CircleShape).background(AppTheme.colors.accent))
                                i == index -> Box(Modifier.size(14.dp).clip(CircleShape).background(AppTheme.colors.groupedBackground).border(3.dp, AppTheme.colors.accent, CircleShape))
                                else -> Box(Modifier.size(10.dp).clip(CircleShape).border(2.dp, AppTheme.colors.strongBorder, CircleShape))
                            }
                            if (i < count - 1) Box(Modifier.weight(1f).height(2.dp).background(if (i < index) AppTheme.colors.accent else AppTheme.colors.separator))
                        }
                    }
                }
            }
            content()
        }
    }
}

/** An option's letter on its own dark tile, as the board prints it. */
@Composable
internal fun LetterTile(letter: String, color: Color, size: androidx.compose.ui.unit.Dp = 26.dp) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size).clip(RoundedCornerShape(6.dp)).background(AppTheme.colors.deep)) {
        BoardMono(letter, color = color, size = 13.sp, weight = FontWeight.Bold, spacing = 0.sp)
    }
}
