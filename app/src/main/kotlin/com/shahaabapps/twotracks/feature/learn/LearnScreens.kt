package com.shahaabapps.twotracks.feature.learn

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Style
import com.shahaabapps.core.ui.theme.Shapes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Replay
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.content.Course
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.Module
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoDisclosure
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoScaffold
import com.shahaabapps.core.ui.components.StatusChip
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.ProgressBar
import com.shahaabapps.twotracks.ui.components.SectionLabel

/** Tab 4: the courses, with your progress and a one-tap "continue". */
@Composable
fun LearnScreen(onOpenCourse: (String) -> Unit, onOpenTopic: (String, String) -> Unit, onOpenDeck: (String) -> Unit = {}, viewModel: LearnViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CupertinoScaffold(title = "Learn") {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            state.courses.forEach { course ->
                val p = state.progress[course.id]
                val next = p?.nextTopic(course)
                item {
                    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), onClick = { onOpenCourse(course.id) }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(course.title, style = MaterialTheme.typography.headlineMedium)
                                Text(course.subtitle, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                            }
                            CupertinoDisclosure()
                        }
                        val done = p?.let { pr -> course.modules.sumOf { pr.moduleLearned(it) } } ?: 0
                        ProgressBar(fraction = p?.courseFraction(course) ?: 0f, modifier = Modifier.padding(top = Spacing.sm))
                        Text(
                            "$done of ${course.topicCount} topics learned · ${p?.modulesPassed(course) ?: 0} of ${course.modules.count { it.available }} tests passed",
                            style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = Spacing.xs),
                        )
                        if (next != null) {
                            CupertinoPrimaryButton(
                                text = "Continue: ${next.second.title.substringBefore(":")}",
                                onClick = { onOpenTopic(course.id, next.second.id) },
                                modifier = Modifier.padding(top = Spacing.sm),
                            )
                        }
                        state.decks[course.id]?.let { d ->
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = Spacing.sm).fillMaxWidth().clip(Shapes.field).background(AppTheme.colors.fill).clickable { onOpenDeck(course.id) }.padding(horizontal = Spacing.md, vertical = 10.dp)) {
                                Icon(Icons.Filled.Style, contentDescription = null, tint = AppTheme.colors.accent, modifier = Modifier.size(20.dp))
                                Column(modifier = Modifier.weight(1f).padding(start = Spacing.sm)) {
                                    Text("Words: ${d.due} due · ${d.fresh} new", style = MaterialTheme.typography.titleMedium)
                                    Text(if (d.size == 0 && d.fresh == 0) "Read a Words topic to start the deck" else "${d.size} in the deck · a few minutes a day", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                                }
                                CupertinoDisclosure()
                            }
                        }
                        val partner = state.partnerProgress[course.id]
                        if (partner != null && partner.learnedTopicIds.isNotEmpty()) {
                            Text(
                                "${state.partnerName}: ${course.modules.sumOf { partner.moduleLearned(it) }} topics, ${partner.modulesPassed(course)} tests passed",
                                style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = Spacing.sm),
                            )
                        }
                    }
                }
            }
        }
    }
}

/** A course: its modules in exam order, each with topics learned and the test result. */
@Composable
fun CourseScreen(
    courseId: String,
    onBack: () -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onOpenQuiz: (String, String) -> Unit,
    viewModel: LearnViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val course = Courses.byId(courseId)
    val p = state.progress[courseId]
    DetailScaffold(title = course?.title ?: "Course", onBack = onBack) {
        if (course == null) return@DetailScaffold
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                    Text(course.subtitle, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                    ProgressBar(fraction = p?.courseFraction(course) ?: 0f, modifier = Modifier.padding(top = Spacing.sm))
                }
            }
            course.modules.forEach { m ->
                item { ModuleSection(course, m, p, onOpenTopic, onOpenQuiz) }
            }
            if (course.mockExam != null) item { ExamPracticeSection(course, p, onOpenQuiz) }
        }
    }
}

/** The mock exam and the review set, after the modules. */
@Composable
private fun ExamPracticeSection(course: Course, p: CourseProgress?, onOpenQuiz: (String, String) -> Unit) {
    val mock = course.mockExam ?: return
    val review = p?.reviewQuestions(course).orEmpty()
    val passedAll = p != null && p.modulesPassed(course) == course.modules.count { it.available }
    Column {
        Text("EXAM PRACTICE", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(start = Spacing.listHorizontal + Spacing.sectionStart, bottom = 6.dp))
        CupertinoListSection(footer = "Questions you miss anywhere collect in the review set until you answer them correctly.") {
            CupertinoListRow(
                title = mock.title,
                subtitle = when {
                    p?.mockPassed == true -> "Passed, best ${p.mockBest}%"
                    p?.mockBest != null -> "Best ${p.mockBest}%, need ${mock.passPercent}%"
                    !passedAll -> "${mock.drawCount ?: mock.questions.size} questions, ${mock.timeLimitMinutes} min. Pass the module tests first for a fair reading"
                    else -> "${mock.drawCount ?: mock.questions.size} questions drawn from ${mock.questions.size}, ${mock.timeLimitMinutes} min, pass at ${mock.passPercent}%"
                },
                leadingIcon = Icons.Filled.Quiz, iconColor = if (p?.mockPassed == true) AppTheme.colors.success else AppTheme.colors.accent,
                showDivider = true,
                onClick = { onOpenQuiz(course.id, Courses.MOCK_MODULE_ID) },
                trailing = { if (p?.mockPassed == true) StatusChip("Passed", AppTheme.colors.success) else CupertinoDisclosure() },
            )
            CupertinoListRow(
                title = "Review missed questions",
                subtitle = if (review.isEmpty()) "Nothing waiting" else "${review.size} to get right · about ${(review.size * 1.5).toInt().coerceAtLeast(2)} min",
                leadingIcon = Icons.Filled.Replay, iconColor = if (review.isEmpty()) AppTheme.colors.secondaryLabel else AppTheme.colors.warn,
                showDivider = false,
                onClick = if (review.isEmpty()) null else ({ onOpenQuiz(course.id, Courses.REVIEW_MODULE_ID) }),
                trailing = { if (review.isNotEmpty()) CupertinoDisclosure() },
            )
        }
    }
}

@Composable
private fun ModuleSection(course: Course, m: Module, p: CourseProgress?, onOpenTopic: (String, String) -> Unit, onOpenQuiz: (String, String) -> Unit) {
    val learned = p?.moduleLearned(m) ?: 0
    val passed = p?.passedModuleIds?.contains(m.id) == true
    val best = p?.bestPercentByModule?.get(m.id)
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = Spacing.listHorizontal + Spacing.sectionStart, end = Spacing.listHorizontal + Spacing.sectionStart, bottom = 6.dp)) {
            Text("MODULE ${m.number} · ${m.title.uppercase()}", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.weight(1f))
            Text(m.examWeight, style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.secondaryLabel)
        }
        CupertinoListSection(footer = if (m.available) null else "${m.summary} Coming in the next update.") {
            if (!m.available) {
                CupertinoListRow(m.summary, leadingIcon = Icons.Filled.Lock, iconColor = AppTheme.colors.secondaryLabel, showDivider = false)
            }
            m.topics.forEachIndexed { i, t ->
                val done = p?.learnedTopicIds?.contains(t.id) == true
                CupertinoListRow(
                    title = t.title.substringBefore(":"),
                    subtitle = "${t.title.substringAfter(": ", "").ifBlank { "" }}${if (t.title.contains(": ")) " · " else ""}${t.minutes} min",
                    showDivider = true,
                    onClick = { onOpenTopic(course.id, t.id) },
                    trailing = {
                        if (done) Icon(Icons.Filled.CheckCircle, contentDescription = "Learned", tint = AppTheme.colors.success, modifier = Modifier.size(22.dp))
                        else Box(modifier = Modifier.size(22.dp).clip(CircleShape).background(AppTheme.colors.fill))
                    },
                )
            }
            if (m.quiz != null) {
                CupertinoListRow(
                    title = m.quiz!!.title,
                    subtitle = when {
                        passed -> "Passed, best $best%"
                        best != null -> "Best $best%, need ${m.quiz!!.passPercent}%"
                        learned < m.topics.size -> "Read the topics first (${learned} of ${m.topics.size})"
                        else -> "${m.quiz!!.questions.size} questions, pass at ${m.quiz!!.passPercent}%"
                    },
                    leadingIcon = Icons.Filled.Quiz, iconColor = if (passed) AppTheme.colors.success else AppTheme.colors.accent,
                    showDivider = false,
                    onClick = { onOpenQuiz(course.id, m.id) },
                    trailing = { if (passed) StatusChip("Passed", AppTheme.colors.success) else CupertinoDisclosure() },
                )
            }
        }
    }
}
