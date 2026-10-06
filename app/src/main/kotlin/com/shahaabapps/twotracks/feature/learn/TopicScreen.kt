package com.shahaabapps.twotracks.feature.learn

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.content.Block
import com.shahaabapps.core.content.CalloutKind
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoSecondaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.PopIn

/** One lesson: the blocks rendered in order, then "Mark as learned" which logs the reading time. */
@Composable
fun TopicScreen(
    onBack: () -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onOpenQuiz: (String, String) -> Unit,
    viewModel: TopicViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(state.message) { state.message?.let { snackbar.showSnackbar(it); viewModel.clearMessage() } }
    val topic = state.topic
    val module = state.module
    val course = state.course
    DetailScaffold(title = module?.let { "Module ${it.number}" } ?: "Topic", onBack = onBack) {
        if (topic == null || module == null || course == null) return@DetailScaffold
        Box {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(horizontal = Spacing.listHorizontal, vertical = Spacing.sm).let { PaddingValues(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = Spacing.sm, bottom = Spacing.xl * 2) }) {
                item {
                    Text("Topic ${state.index + 1} of ${state.count} · ${topic.minutes} min read", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
                    Text(topic.title, style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = Spacing.xs))
                }
                items(topic.blocks.size) { i -> BlockView(topic.blocks[i]) }
                if (topic.references.isNotEmpty()) {
                    item { ReferencesBlock(topic.references) }
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.padding(top = Spacing.md)) {
                        if (state.learned) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                                PopIn { Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = AppTheme.colors.success) }
                                Text("Learned. The minutes are in your AZ-204 hours.", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.success)
                            }
                            val nextId = state.nextTopicId
                            if (nextId != null) {
                                CupertinoPrimaryButton(text = "Next topic", onClick = { onOpenTopic(course.id, nextId) })
                            } else if (module.quiz != null) {
                                CupertinoPrimaryButton(text = "Take the module test", onClick = { onOpenQuiz(course.id, module.id) })
                            }
                            CupertinoTextButton(text = "Mark as not learned", onClick = viewModel::unlearn)
                        } else {
                            CupertinoPrimaryButton(text = "Mark as learned", onClick = viewModel::markLearned)
                            Text("Marks the topic done and adds your reading time to the ${module.title} skill.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                        }
                    }
                }
            }
            SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun BlockView(block: Block) {
    when (block) {
        is Block.Heading -> Text(block.text, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = Spacing.sm))
        is Block.Paragraph -> Text(block.text, style = MaterialTheme.typography.bodyLarge, lineHeight = 24.sp)
        is Block.Bullets -> Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            block.items.forEach { item ->
                Row(verticalAlignment = Alignment.Top) {
                    Box(modifier = Modifier.padding(top = 9.dp, end = 10.dp).size(6.dp).clip(CircleShape).background(AppTheme.colors.accent))
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
        is Block.Check -> CheckBlock(block)
        is Block.Glossary -> GlossaryBlock(block)
        is Block.Recap -> Column(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(AppTheme.colors.accent).padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text("Remember these three", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.85f))
            block.items.forEachIndexed { i, item ->
                Row(verticalAlignment = Alignment.Top) {
                    Text("${i + 1}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.width(22.dp))
                    Text(item, style = MaterialTheme.typography.bodyMedium, color = Color.White, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun Callout(block: Block.Callout) {
    val (icon, label, tint) = when (block.kind) {
        CalloutKind.PLAIN_WORDS -> Triple(Icons.Filled.Translate, "In plain words", AppTheme.colors.accent)
        CalloutKind.EXAM_TIP -> Triple(Icons.Filled.School, "Exam tip", AppTheme.colors.warn)
        CalloutKind.REMEMBER -> Triple(Icons.Filled.PushPin, "Remember this", AppTheme.colors.success)
        CalloutKind.EXAMPLE -> Triple(Icons.Filled.Lightbulb, "For Two Tracks", AppTheme.colors.secondaryLabel)
        CalloutKind.MISTAKES -> Triple(Icons.Filled.Warning, "Common mistakes", AppTheme.colors.destructive)
        CalloutKind.WORKED -> Triple(Icons.Filled.Build, "Worked example", AppTheme.colors.accent)
    }
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(tint.copy(alpha = 0.12f)).padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, color = tint, fontWeight = FontWeight.SemiBold)
        }
        Text(block.text, style = MaterialTheme.typography.bodyMedium, lineHeight = 22.sp)
    }
}

@Composable
private fun CodeBlock(block: Block.Code) {
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFF1E1B22))) {
        Text(block.language, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.55f), modifier = Modifier.padding(start = Spacing.md, top = Spacing.sm))
        Text(
            block.code, fontFamily = FontFamily.Monospace, fontSize = 12.sp, lineHeight = 18.sp, color = Color(0xFFF2E9EC),
            modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = Spacing.md, vertical = Spacing.sm),
        )
    }
}

@Composable
private fun TableBlock(block: Block.Table) {
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(AppTheme.colors.card)) {
        Row(modifier = Modifier.fillMaxWidth().background(AppTheme.colors.fill).padding(horizontal = Spacing.sm, vertical = 8.dp)) {
            block.headers.forEach { h -> Text(h, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f)) }
        }
        block.rows.forEachIndexed { i, row ->
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.sm, vertical = 8.dp)) {
                row.forEachIndexed { j, cell ->
                    Text(
                        cell, style = MaterialTheme.typography.bodySmall, lineHeight = 18.sp,
                        fontWeight = if (j == 0) FontWeight.SemiBold else FontWeight.Normal,
                        modifier = Modifier.weight(1f).padding(end = 6.dp),
                    )
                }
            }
            if (i < block.rows.lastIndex) Box(modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.sm).size(1.dp).background(AppTheme.colors.separator))
        }
    }
}

@Composable
private fun CupertinoSecondaryButtonUnused() { CupertinoSecondaryButton(text = "", onClick = {}) }

@Composable
private fun StepsBlock(block: Block.Steps) {
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.card).padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(block.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        block.steps.forEachIndexed { i, st ->
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(AppTheme.colors.accent), contentAlignment = Alignment.Center) {
                    Text("${i + 1}", style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Bold)
                }
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
                Column(modifier = Modifier.width(230.dp).clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.card).padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(c.title, style = MaterialTheme.typography.titleMedium, color = AppTheme.colors.accent, fontWeight = FontWeight.SemiBold)
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
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.success.copy(alpha = 0.1f)).padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
private fun CheckBlock(block: Block.Check) {
    var picked by androidx.compose.runtime.remember(block.question.id) { androidx.compose.runtime.mutableStateOf<Int?>(null) }
    val q = block.question
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.accent.copy(alpha = 0.08f)).padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Quick check", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.accent, fontWeight = FontWeight.SemiBold)
        Text(q.prompt, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, lineHeight = 21.sp)
        q.options.forEachIndexed { i, opt ->
            val state = when {
                picked == null -> null
                i in q.correct -> true
                i == picked -> false
                else -> null
            }
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                    .background(when (state) { true -> AppTheme.colors.success.copy(alpha = 0.15f); false -> AppTheme.colors.destructive.copy(alpha = 0.15f); null -> AppTheme.colors.card })
                    .clickable(enabled = picked == null) { picked = i }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
            ) {
                Text(('A' + i).toString(), style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, fontWeight = FontWeight.Bold)
                Text(opt, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            }
        }
        if (picked != null) {
            Text(
                (if (picked in q.correct) "Right. " else "Not quite. ") + q.explanation,
                style = MaterialTheme.typography.bodySmall, lineHeight = 19.sp,
                color = if (picked in q.correct) AppTheme.colors.success else AppTheme.colors.destructive,
            )
        }
    }
}

@Composable
private fun GlossaryBlock(block: Block.Glossary) {
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.card).padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(6.dp)) {
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
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.card).padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Read more on Microsoft Learn", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        refs.forEach { r ->
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().clickable { uri.openUri(r.url) }.padding(vertical = 4.dp),
            ) {
                Icon(Icons.Filled.OpenInNew, contentDescription = null, tint = AppTheme.colors.accent, modifier = Modifier.size(16.dp))
                Text(r.title, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.accent, modifier = Modifier.weight(1f))
            }
        }
        Text("Official pages open in your browser.", style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.secondaryLabel)
    }
}
