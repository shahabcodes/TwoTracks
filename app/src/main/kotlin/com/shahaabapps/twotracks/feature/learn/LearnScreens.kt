package com.shahaabapps.twotracks.feature.learn

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.content.Course
import com.shahaabapps.core.content.CourseProgress
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.Mastery
import com.shahaabapps.core.content.Module
import com.shahaabapps.core.content.StudyKind
import com.shahaabapps.core.content.TopicMastery
import com.shahaabapps.core.content.mastery
import com.shahaabapps.core.content.revisits
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.components.CupertinoScaffold
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.guide.GuideAnchors
import com.shahaabapps.twotracks.ui.guide.guideAnchor
import java.time.LocalDate

/** The Learn tab. */
@Composable
fun LearnScreen(
    onOpenCourse: (String) -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onOpenDeck: (String) -> Unit = {},
    onOpenQuiz: (String, String) -> Unit = { _, _ -> },
    viewModel: LearnViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LearnContent(state, onOpenCourse, onOpenTopic, onOpenDeck, onOpenQuiz, onToggleFavorite = viewModel::toggleFavorite)
}

/**
 * Learn, as the Parallel prototype draws it: how many courses and how many
 * are yours, two tiles for what has come back round (the daily review and
 * the words), then each course with a bar for each of you on it and the next
 * topic to continue with.
 *
 * The "today's study" card this replaces had three steps; two of them are
 * the tiles and the third is each course's Continue.
 */
@Composable
fun LearnContent(
    state: LearnUiState,
    onOpenCourse: (String) -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onOpenDeck: (String) -> Unit,
    onOpenQuiz: (String, String) -> Unit,
    onToggleFavorite: (String) -> Unit = {},
) {
    val meIsTech = state.myRole == PersonRole.TECH
    val myColor = AppTheme.colors.track(meIsTech)
    val partnerColor = AppTheme.colors.track(!meIsTech)
    if (AppTheme.departures) { BoardLearnBody(state, onOpenCourse, onOpenTopic, onOpenDeck, onOpenQuiz, onToggleFavorite); return }
    val mine = state.courses.count { state.myRole in state.owners[it.id].orEmpty() }
    CupertinoScaffold(title = "Learn", eyebrow = coursesLine(state.courses.size, mine)) {
        LazyColumn(contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                val questions = state.daily.steps.firstOrNull { it.kind == StudyKind.QUESTIONS }
                val words = state.daily.steps.firstOrNull { it.kind == StudyKind.WORDS }
                val due = state.dailyDue.values.sum()
                val reviewCourse = questions?.courseId ?: state.dailyDue.maxByOrNull { it.value }?.key
                val deckDue = state.decks.values.sumOf { it.due + it.fresh }
                val deckCourse = words?.courseId ?: state.decks.keys.firstOrNull()
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 12.dp).guideAnchor(GuideAnchors.LEARN_TODAY_CARD),
                ) {
                    Tile(
                        label = "Daily review", value = "$due",
                        detail = if (due == 0) "nothing back round today" else "question${if (due == 1) "" else "s"} back round, about ${(due * 0.8).toInt().coerceAtLeast(1)} min",
                        labelColor = myColor, emphasized = due > 0,
                        onClick = reviewCourse?.takeIf { due > 0 }?.let { c -> { onOpenQuiz(c, Courses.DAILY_MODULE_ID) } },
                        modifier = Modifier.weight(1f),
                    )
                    Tile(
                        label = "Words", value = "$deckDue",
                        detail = if (deckDue == 0) "nothing due in the deck" else "German card${if (deckDue == 1) "" else "s"} due, about ${(deckDue / 4).coerceAtLeast(1)} min",
                        labelColor = AppTheme.colors.secondaryLabel, emphasized = false,
                        onClick = deckCourse?.let { c -> { onOpenDeck(c) } },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            item {
                HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule, modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 24.dp))
            }
            state.courses.forEachIndexed { i, course ->
                item(key = course.id) {
                    val owners = state.owners[course.id].orEmpty()
                    val forMe = state.myRole in owners
                    val forPartner = owners.any { it != state.myRole }
                    val p = state.progress[course.id]
                    val partner = state.partnerProgress[course.id]
                    val next = p?.nextTopic(course)
                    CourseRow(
                        course = course,
                        starred = course.id in state.favorites,
                        onStar = { onToggleFavorite(course.id) },
                        who = when {
                            forMe && forPartner -> "Both"
                            forMe -> "You"
                            forPartner -> state.partnerName
                            else -> ""
                        },
                        bars = buildList {
                            if (forMe || !forPartner) add(Triple("You", p, myColor))
                            if (forPartner) add(Triple(state.partnerName, partner, partnerColor))
                        },
                        next = when {
                            forMe && next != null -> "Continue: ${next.second.title.substringBefore(":")}"
                            forMe -> "Every topic read · see the course"
                            else -> "See ${state.partnerName}'s progress"
                        },
                        nextColor = if (forMe) myColor else partnerColor,
                        onOpen = { onOpenCourse(course.id) },
                        onNext = { if (forMe && next != null) onOpenTopic(course.id, next.second.id) else onOpenCourse(course.id) },
                        modifier = if (i == 0) Modifier.guideAnchor(GuideAnchors.LEARN_COURSES) else Modifier,
                    )
                }
            }
        }
    }
}

/** The exam a course leads to, short enough for the line above its title. */
internal fun examName(course: com.shahaabapps.core.content.Course): String = when (course.id) {
    "az-204" -> "Microsoft AZ-204"
    "ai-200" -> "Microsoft AI-200"
    "az-305" -> "Microsoft AZ-305"
    "de-a1" -> "Goethe-Zertifikat A1"
    "ielts" -> "IELTS Academic and General"
    else -> course.title
}

/** "THREE COURSES · TWO OF THEM YOURS" */
internal fun coursesLine(total: Int, mine: Int): String {
    fun word(n: Int) = when (n) { 0 -> "none"; 1 -> "one"; 2 -> "two"; 3 -> "three"; 4 -> "four"; 5 -> "five"; else -> n.toString() }
    val head = "${word(total)} course${if (total == 1) "" else "s"}"
    return when {
        mine == total -> "$head · all of them yours"
        mine == 0 -> "$head · none of them yours yet"
        else -> "$head · ${word(mine)} of them yours"
    }
}

@Composable
private fun Tile(label: String, value: String, detail: String, labelColor: Color, emphasized: Boolean, onClick: (() -> Unit)?, modifier: Modifier) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .heightIn(min = 104.dp)
            .clip(shape)
            .border(Spacing.rule, if (emphasized) AppTheme.colors.ink else AppTheme.colors.separator, shape)
            .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier)
            .padding(14.dp),
    ) {
        Text(label.uppercase(), style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp), color = labelColor)
        Text(value, style = MaterialTheme.typography.headlineMedium.copy(fontSize = 26.sp))
        Text(detail, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 16.sp), color = AppTheme.colors.secondaryLabel)
    }
}

@Composable
private fun CourseRow(
    course: Course,
    starred: Boolean,
    onStar: () -> Unit,
    who: String,
    bars: List<Triple<String, CourseProgress?, Color>>,
    next: String,
    nextColor: Color,
    onOpen: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(horizontal = Spacing.listHorizontal)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth().clickable(onClick = onOpen).padding(vertical = 18.dp),
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Text(course.title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f).semantics { heading() })
                FavoriteStar(course, starred, onStar, AppTheme.colors.accent, AppTheme.colors.secondaryLabel)
                if (who.isNotBlank()) Text(who.uppercase(), style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(start = 12.dp, top = 4.dp))
            }
            Text(course.subtitle, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
            bars.forEach { (name, p, color) ->
                val done = p?.let { pr -> course.modules.sumOf { pr.moduleLearned(it) } } ?: 0
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(name, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel, modifier = Modifier.width(52.dp), maxLines = 1)
                    Box(Modifier.weight(1f).height(4.dp).background(AppTheme.colors.separator)) {
                        Box(Modifier.fillMaxWidth((done.toFloat() / course.topicCount.coerceAtLeast(1)).coerceIn(0.02f, 1f)).height(4.dp).background(color))
                    }
                    Text("$done / ${course.topicCount}", style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 11.sp), color = AppTheme.colors.secondaryLabel, modifier = Modifier.width(60.dp))
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.clip(CircleShape).clickable(role = Role.Button, onClick = onNext).heightIn(min = 32.dp),
            ) {
                Text(next, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = nextColor)
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = nextColor, modifier = Modifier.size(14.dp))
            }
        }
        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
    }
}

// ------------------------------------------------------------------ course

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
    CourseContent(courseId, state, LocalDate.now(), onBack, onOpenTopic, onOpenQuiz)
}

/**
 * A course, as the Parallel prototype draws it: what it leads to and how far
 * each of you has got, the one topic most worth going back to, then the
 * modules as numbered rows with a pip per topic for each of you. The module
 * you are in opens to its topics and its test; any other opens on a tap. The
 * paper practice is last, outlined.
 */
@Composable
fun CourseContent(
    courseId: String,
    state: LearnUiState,
    today: LocalDate,
    onBack: () -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onOpenQuiz: (String, String) -> Unit,
) {
    val course = Courses.byId(courseId)
    val p = state.progress[courseId]
    val partner = state.partnerProgress[courseId]
    val owners = state.owners[courseId].orEmpty()
    val meIsTech = state.myRole == PersonRole.TECH
    val myColor = AppTheme.colors.track(meIsTech)
    val partnerColor = AppTheme.colors.track(!meIsTech)
    val showPartner = owners.any { it != state.myRole }
    val showMe = state.myRole in owners || !showPartner
    val mastery = remember(course, p, today) { if (course == null || p == null) emptyMap() else course.mastery(p, today) }
    val revisits = remember(course, p, today) { if (course == null || p == null) emptyList() else course.revisits(p, today) }
    val nextTopic = course?.let { p?.nextTopic(it) }
    var open by rememberSaveable(courseId) { mutableStateOf(nextTopic?.first?.id ?: course?.modules?.firstOrNull()?.id) }
    if (AppTheme.departures && course != null) { BoardCourseBody(course, state, today, onBack, onOpenTopic, onOpenQuiz); return }

    DetailScaffold(
        title = course?.title ?: "Course",
        onBack = onBack,
        eyebrow = course?.let { "${examName(it)} · ${it.modules.size} modules" },
    ) {
        if (course == null) return@DetailScaffold
        LazyColumn(contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, bottom = 18.dp)) {
                    if (showMe) Who("You", p, course, myColor)
                    if (showPartner) Who(state.partnerName, partner, course, partnerColor)
                }
            }
            revisits.firstOrNull()?.let { r ->
                item {
                    val t = Courses.topic(course.id, r.topicId)?.second
                    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
                        Row(
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth().clickable { onOpenTopic(course.id, r.topicId) }.padding(vertical = 14.dp),
                        ) {
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text("WORTH GOING BACK TO", style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp), color = myColor)
                                Text(
                                    (t?.title?.substringBefore(":") ?: r.topicId) + buildList {
                                        if (r.missed > 0) add("${r.missed} missed")
                                        if (r.due > 0) add("${r.due} due again")
                                    }.joinToString(" · ", prefix = " · ").takeIf { r.missed > 0 || r.due > 0 }.orEmpty(),
                                    style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold,
                                )
                                if (revisits.size > 1) Text("and ${revisits.size - 1} more", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                            }
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.ink, modifier = Modifier.size(18.dp))
                        }
                        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                    }
                }
            }
            course.modules.forEach { m ->
                item(key = m.id) {
                    ModuleRow(
                        course = course, m = m, p = p, partner = partner.takeIf { showPartner },
                        mastery = mastery, isOpen = open == m.id, nextTopicId = nextTopic?.second?.id,
                        myColor = myColor, partnerColor = partnerColor,
                        onToggle = { open = if (open == m.id) null else m.id },
                        onOpenTopic = onOpenTopic, onOpenQuiz = onOpenQuiz,
                    )
                }
            }
            item { PaperPractice(course, p, state.dailyDue[courseId] ?: 0, onOpenQuiz) }
        }
    }
}

@Composable
private fun Who(name: String, p: CourseProgress?, course: Course, color: Color) {
    val done = p?.let { pr -> course.modules.sumOf { pr.moduleLearned(it) } } ?: 0
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(color))
        Text("$name $done / ${course.topicCount}", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
    }
}

/** A module: its number, its title, a pip per topic for each of you, and when open, its topics and test. */
@Composable
private fun ModuleRow(
    course: Course,
    m: Module,
    p: CourseProgress?,
    partner: CourseProgress?,
    mastery: Map<String, TopicMastery>,
    isOpen: Boolean,
    nextTopicId: String?,
    myColor: Color,
    partnerColor: Color,
    onToggle: () -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onOpenQuiz: (String, String) -> Unit,
) {
    val mine = p?.moduleLearned(m) ?: 0
    val theirs = partner?.moduleLearned(m) ?: 0
    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().clickable(onClickLabel = if (isOpen) "Close the module" else "Open the module", onClick = onToggle).padding(vertical = 14.dp),
        ) {
            Text("%02d".format(m.number), style = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFamily, fontSize = 13.sp), color = AppTheme.colors.secondaryLabel, modifier = Modifier.width(28.dp))
            Text(m.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = if (m.available) AppTheme.colors.ink else AppTheme.colors.secondaryLabel, modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.semantics { contentDescription = "You $mine of ${m.topics.size}" + if (partner != null) ", partner $theirs of ${m.topics.size}" else "" },
            ) {
                repeat(m.topics.size.coerceAtMost(6)) { k ->
                    val c = when {
                        k < mine -> myColor
                        partner != null && k < theirs -> partnerColor
                        else -> AppTheme.colors.separator
                    }
                    Box(Modifier.size(8.dp).clip(RoundedCornerShape(2.dp)).background(c))
                }
            }
        }
        if (isOpen) {
            Column(modifier = Modifier.padding(start = 40.dp, bottom = 6.dp)) {
                if (!m.available) {
                    Text("${m.summary} Coming in the next update.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(bottom = 10.dp))
                }
                m.topics.forEach { t ->
                    val mt = mastery[t.id]
                    val isNext = t.id == nextTopicId
                    val (status, statusColor) = when {
                        mt?.needsRevisit == true -> "LOOK AGAIN" to AppTheme.colors.warn
                        isNext -> "NEXT · ${t.minutes} MIN" to myColor
                        mt != null && mt.level == Mastery.HELD -> "HELD" to AppTheme.colors.success
                        mt != null && mt.level != Mastery.UNREAD -> "READ" to AppTheme.colors.success
                        else -> "${t.minutes} MIN" to AppTheme.colors.secondaryLabel
                    }
                    TopicLine(
                        title = t.title.substringBefore(":").let { kind -> t.title.substringAfter(": ", "").takeIf { it.isNotBlank() }?.let { "$kind · $it" } ?: kind },
                        status = status, statusColor = statusColor, titleColor = if (isNext) myColor else AppTheme.colors.ink, bold = isNext,
                        onClick = { onOpenTopic(course.id, t.id) },
                    )
                }
                m.quiz?.let { q ->
                    val passed = p?.passedModuleIds?.contains(m.id) == true
                    val best = p?.bestPercentByModule?.get(m.id)
                    TopicLine(
                        title = q.title,
                        status = when {
                            passed -> "PASSED · $best%"
                            best != null -> "BEST $best% · NEED ${q.passPercent}%"
                            mine < m.topics.size -> "AFTER THE TOPICS"
                            else -> "${q.questions.size} QUESTIONS"
                        },
                        statusColor = if (passed) AppTheme.colors.success else AppTheme.colors.secondaryLabel,
                        titleColor = AppTheme.colors.ink, bold = false,
                        onClick = { onOpenQuiz(course.id, m.id) },
                    )
                }
            }
        }
        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
    }
}

@Composable
private fun TopicLine(title: String, status: String, statusColor: Color, titleColor: Color, bold: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth().heightIn(min = 44.dp).clickable(onClick = onClick),
    ) {
        Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal, color = titleColor, modifier = Modifier.weight(1f))
        Text(status, style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp, letterSpacing = 0.4.sp), color = statusColor, maxLines = 1)
    }
}

/** The mock exam, outlined, and under it the review sets when anything is waiting in them. */
@Composable
private fun PaperPractice(course: Course, p: CourseProgress?, dailyDue: Int, onOpenQuiz: (String, String) -> Unit) {
    val mock = course.mockExam
    val review = p?.reviewQuestions(course).orEmpty()
    val passedAll = p != null && p.modulesPassed(course) == course.modules.count { it.available }
    Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        if (mock != null) {
            val shape = RoundedCornerShape(16.dp)
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth().clip(shape).border(Spacing.rule, AppTheme.colors.ink, shape)
                    .clickable { onOpenQuiz(course.id, Courses.MOCK_MODULE_ID) }.padding(16.dp),
            ) {
                Text("PAPER PRACTICE", style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp), color = AppTheme.colors.secondaryLabel)
                Text(mock.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Text(
                    when {
                        p?.mockPassed == true -> "Passed, best ${p.mockBest}%."
                        p?.mockBest != null -> "Best ${p.mockBest}%, need ${mock.passPercent}%."
                        !passedAll -> "${mock.drawCount ?: mock.questions.size} questions, ${mock.timeLimitMinutes} minutes. Pass the module tests first for a fair reading."
                        else -> "${mock.drawCount ?: mock.questions.size} questions, ${mock.timeLimitMinutes} minutes, pass at ${mock.passPercent}%."
                    },
                    style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                )
            }
        }
        if (dailyDue > 0) PracticeLink("Daily review · $dailyDue due") { onOpenQuiz(course.id, Courses.DAILY_MODULE_ID) }
        if (review.isNotEmpty()) PracticeLink("Review ${review.size} missed question${if (review.size == 1) "" else "s"}") { onOpenQuiz(course.id, Courses.REVIEW_MODULE_ID) }
    }
}

@Composable
private fun PracticeLink(text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.clip(CircleShape).clickable(role = Role.Button, onClick = onClick).heightIn(min = 44.dp),
    ) {
        Text(text, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = AppTheme.colors.accentText)
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = AppTheme.colors.accentText, modifier = Modifier.size(14.dp))
    }
}

/**
 * The star on a course: filled when it is a favourite, which Learn lists
 * first, hollow when not; a tap turns it over. Drawn as a character so both
 * looks set it in their own type.
 */
@Composable
internal fun FavoriteStar(course: Course, starred: Boolean, onToggle: () -> Unit, on: Color, off: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(36.dp).clip(CircleShape)
            .clickable(role = Role.Button, onClickLabel = if (starred) "Unstar ${course.title}" else "Star ${course.title}", onClick = onToggle),
    ) {
        Text(if (starred) "★" else "☆", fontSize = 22.sp, color = if (starred) on else off)
    }
}
