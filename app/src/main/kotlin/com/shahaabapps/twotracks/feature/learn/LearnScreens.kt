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
import androidx.compose.material.icons.filled.Schedule
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
import com.shahaabapps.core.content.CourseProgress
import com.shahaabapps.core.content.DailyStudy
import com.shahaabapps.core.content.StudyKind
import com.shahaabapps.core.content.StudyStep
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.Module
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoDisclosure
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoScaffold
import com.shahaabapps.core.ui.components.StatusChip
import com.shahaabapps.twotracks.ui.guide.GuideAnchors
import com.shahaabapps.twotracks.ui.guide.guideAnchor
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.ProgressBar
import com.shahaabapps.twotracks.ui.components.SectionLabel
import java.time.LocalDate
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import com.shahaabapps.core.content.revisits
import com.shahaabapps.core.content.mastery
import com.shahaabapps.core.content.TopicMastery
import com.shahaabapps.core.content.Mastery

/** Tab 4: the courses, with your progress and a one-tap "continue". */
@Composable
fun LearnScreen(
    onOpenCourse: (String) -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onOpenDeck: (String) -> Unit = {},
    onOpenQuiz: (String, String) -> Unit = { _, _ -> },
    viewModel: LearnViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CupertinoScaffold(title = "Learn") {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            if (state.daily.total > 0) {
                item {
                    Box(modifier = Modifier.guideAnchor(GuideAnchors.LEARN_TODAY_CARD)) {
                    TodayStudyCard(state.daily) { step ->
                        when (step.kind) {
                            StudyKind.TOPIC -> step.topicId?.let { onOpenTopic(step.courseId, it) }
                            StudyKind.WORDS -> onOpenDeck(step.courseId)
                            StudyKind.QUESTIONS -> onOpenQuiz(step.courseId, Courses.DAILY_MODULE_ID)
                        }
                    }
                    }
                }
            }
            state.courses.forEach { course ->
                val p = state.progress[course.id]
                val next = p?.nextTopic(course)
                item {
                    CupertinoCard(
                        modifier = Modifier
                            .padding(horizontal = Spacing.listHorizontal)
                            .then(if (course.id == state.courses.firstOrNull()?.id) Modifier.guideAnchor(GuideAnchors.LEARN_COURSES) else Modifier),
                        onClick = { onOpenCourse(course.id) },
                    ) {
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
                        // Questions due again today: the one tap that keeps a finished course alive.
                        val due = state.dailyDue[course.id] ?: 0
                        if (due > 0) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = Spacing.sm).fillMaxWidth().clip(Shapes.field).background(AppTheme.colors.fill).clickable { onOpenQuiz(course.id, Courses.DAILY_MODULE_ID) }.padding(horizontal = Spacing.md, vertical = 10.dp)) {
                                Icon(Icons.Filled.Schedule, contentDescription = null, tint = AppTheme.colors.accent, modifier = Modifier.size(20.dp))
                                Column(modifier = Modifier.weight(1f).padding(start = Spacing.sm)) {
                                    Text("Daily review: $due due", style = MaterialTheme.typography.titleMedium)
                                    Text("Questions coming back before they fade", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                                }
                                CupertinoDisclosure()
                            }
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

/**
 * Today's study, across every course: read one topic, clear the words that are
 * due, answer the questions that have come back round. Three rows that can all
 * be ticked, so the day has an end rather than only a middle.
 */
@Composable
private fun TodayStudyCard(daily: DailyStudy, onStep: (StudyStep) -> Unit) {
    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(if (daily.closed) "Done for today" else "Today's study", style = MaterialTheme.typography.headlineMedium)
                Text(
                    if (daily.closed) {
                        "Everything due is cleared. The rest comes back when it is meant to."
                    } else {
                        "${daily.doneCount} of ${daily.total} done · about ${daily.minutesLeft} min left"
                    },
                    style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                )
            }
            if (daily.closed) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = AppTheme.colors.success, modifier = Modifier.size(28.dp))
            }
        }
        ProgressBar(
            fraction = daily.fraction,
            color = if (daily.closed) AppTheme.colors.success else AppTheme.colors.accent,
            modifier = Modifier.padding(top = Spacing.sm),
        )
        daily.steps.forEach { step ->
            val icon = when (step.kind) {
                StudyKind.TOPIC -> Icons.Filled.MenuBook
                StudyKind.WORDS -> Icons.Filled.Style
                StudyKind.QUESTIONS -> Icons.Filled.Schedule
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = Spacing.sm).fillMaxWidth().clip(Shapes.field)
                    .background(AppTheme.colors.fill)
                    .clickable(enabled = !step.done) { onStep(step) }
                    .padding(horizontal = Spacing.md, vertical = 10.dp),
            ) {
                Icon(
                    if (step.done) Icons.Filled.CheckCircle else icon,
                    contentDescription = null,
                    tint = if (step.done) AppTheme.colors.success else AppTheme.colors.accent,
                    modifier = Modifier.size(20.dp),
                )
                Column(modifier = Modifier.weight(1f).padding(start = Spacing.sm)) {
                    Text(step.title, style = MaterialTheme.typography.titleMedium, color = if (step.done) AppTheme.colors.secondaryLabel else Color.Unspecified)
                    Text(step.detail, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                }
                if (!step.done) CupertinoDisclosure()
            }
        }
        daily.next?.let { next ->
            CupertinoPrimaryButton(
                text = "Start: ${next.title.substringBefore(":")}",
                onClick = { onStep(next) },
                modifier = Modifier.padding(top = Spacing.md),
            )
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
    val today = remember { LocalDate.now() }
    val mastery = remember(course, p, today) { if (course == null || p == null) emptyMap() else course.mastery(p, today) }
    val revisits = remember(course, p, today) { if (course == null || p == null) emptyList() else course.revisits(p, today) }
    DetailScaffold(title = course?.title ?: "Course", onBack = onBack) {
        if (course == null) return@DetailScaffold
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                    Text(course.subtitle, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                    ProgressBar(fraction = p?.courseFraction(course) ?: 0f, modifier = Modifier.padding(top = Spacing.sm))
                }
            }
            if (revisits.isNotEmpty()) {
                item { RevisitSection(course, revisits, onOpenTopic) }
            }
            course.modules.forEach { m ->
                item { ModuleSection(course, m, p, mastery, onOpenTopic, onOpenQuiz) }
            }
            item { ExamPracticeSection(course, p, state.dailyDue[courseId] ?: 0, onOpenQuiz) }
        }
    }
}

/**
 * Topics worth going back to, rather than a course you walk once.
 *
 * Everything here is derived: a topic appears because one of its own questions
 * is due again or was missed and never put right, not because anything decided
 * to nag. Missed first, then the reading left longest.
 */
@Composable
private fun RevisitSection(course: Course, revisits: List<TopicMastery>, onOpenTopic: (String, String) -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = Spacing.listHorizontal + Spacing.sectionStart, end = Spacing.listHorizontal + Spacing.sectionStart, bottom = 6.dp)) {
            Text("WORTH GOING BACK TO", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.weight(1f))
        }
        CupertinoListSection(footer = "These came back round on their own schedule. Rereading one before it is missed is cheaper than after.") {
            revisits.forEachIndexed { i, r ->
                val t = Courses.topic(course.id, r.topicId)?.second
                CupertinoListRow(
                    title = t?.title?.substringBefore(":") ?: r.topicId,
                    subtitle = buildList {
                        if (r.missed > 0) add("${r.missed} missed")
                        if (r.due > 0) add("${r.due} due again")
                        r.readOn?.let { add("read $it") }
                    }.joinToString(" · "),
                    leadingIcon = Icons.Filled.Refresh,
                    iconColor = if (r.missed > 0) AppTheme.colors.warn else AppTheme.colors.accent,
                    showDivider = i < revisits.size - 1,
                    onClick = { onOpenTopic(course.id, r.topicId) },
                    trailing = { CupertinoDisclosure() },
                )
            }
        }
    }
}

/** The mock exam, the daily review and the review set, after the modules. */
@Composable
private fun ExamPracticeSection(course: Course, p: CourseProgress?, dailyDue: Int, onOpenQuiz: (String, String) -> Unit) {
    val mock = course.mockExam
    val review = p?.reviewQuestions(course).orEmpty()
    val held = p?.questionsHeld ?: 0
    val passedAll = p != null && p.modulesPassed(course) == course.modules.count { it.available }
    Column {
        Text("EXAM PRACTICE", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(start = Spacing.listHorizontal + Spacing.sectionStart, bottom = 6.dp))
        CupertinoListSection(footer = "Questions you miss anywhere collect in the review set until you answer them correctly. Ones you get right come back on a widening schedule until they stick.") {
            if (mock != null) {
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
            }
            CupertinoListRow(
                title = "Daily review",
                subtitle = when {
                    dailyDue > 0 -> "$dailyDue due · about ${(dailyDue * 1.5).toInt().coerceAtLeast(2)} min"
                    held > 0 -> "Nothing due today · $held held"
                    else -> "Sit a test or read a topic and questions start coming back here"
                },
                leadingIcon = Icons.Filled.Schedule, iconColor = if (dailyDue > 0) AppTheme.colors.accent else AppTheme.colors.secondaryLabel,
                showDivider = true,
                onClick = if (dailyDue == 0) null else ({ onOpenQuiz(course.id, Courses.DAILY_MODULE_ID) }),
                trailing = { if (dailyDue > 0) CupertinoDisclosure() },
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

/**
 * Where a topic has got to, in 22dp. Held is a filled tick; practised and read
 * are the same ring filling up, so the difference between "I pressed the
 * button" and "I can answer its questions" is visible at a glance.
 */
@Composable
private fun MasteryPip(mt: TopicMastery?) {
    val level = mt?.level ?: Mastery.UNREAD
    val tone = when {
        mt?.needsRevisit == true -> AppTheme.colors.warn
        level == Mastery.HELD -> AppTheme.colors.success
        level == Mastery.UNREAD -> AppTheme.colors.fill
        else -> AppTheme.colors.accent
    }
    if (level == Mastery.HELD && mt?.needsRevisit != true) {
        Icon(Icons.Filled.CheckCircle, contentDescription = mt?.label ?: "Held", tint = tone, modifier = Modifier.size(22.dp))
        return
    }
    Box(modifier = Modifier.size(22.dp), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.size(22.dp).clip(CircleShape).background(AppTheme.colors.fill))
        if (level != Mastery.UNREAD) {
            CircularProgressIndicator(
                progress = { mt?.fraction ?: 0f },
                modifier = Modifier.size(22.dp),
                color = tone,
                trackColor = Color.Transparent,
                strokeWidth = 3.dp,
                strokeCap = StrokeCap.Round,
            )
        }
    }
}

@Composable
private fun ModuleSection(
    course: Course,
    m: Module,
    p: CourseProgress?,
    mastery: Map<String, TopicMastery>,
    onOpenTopic: (String, String) -> Unit,
    onOpenQuiz: (String, String) -> Unit,
) {
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
                // A rung, not a tick: reading a topic is one step of knowing it,
                // and the row says which step rather than claiming it is finished.
                val mt = mastery[t.id]
                val base = "${t.title.substringAfter(": ", "").ifBlank { "" }}${if (t.title.contains(": ")) " · " else ""}${t.minutes} min"
                CupertinoListRow(
                    title = t.title.substringBefore(":"),
                    subtitle = when {
                        mt == null || mt.level == Mastery.UNREAD -> base
                        mt.needsRevisit -> "${mt.label} · worth another look"
                        else -> "${mt.label} · ${t.minutes} min"
                    },
                    showDivider = true,
                    onClick = { onOpenTopic(course.id, t.id) },
                    trailing = { MasteryPip(mt) },
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
