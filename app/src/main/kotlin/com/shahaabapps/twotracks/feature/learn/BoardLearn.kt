package com.shahaabapps.twotracks.feature.learn

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.content.Course
import com.shahaabapps.core.content.CourseProgress
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.Mastery
import com.shahaabapps.core.content.StudyKind
import com.shahaabapps.core.content.mastery
import com.shahaabapps.core.content.revisits
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.CondensedFamily
import com.shahaabapps.twotracks.ui.board.BoardBack
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardTitle
import com.shahaabapps.twotracks.ui.guide.GuideAnchors
import com.shahaabapps.twotracks.ui.guide.guideAnchor
import java.time.LocalDate
import java.util.Locale

/** The two letters a course runs under on the board: AZ, DE, IE. */
internal fun lineCode(course: Course): String = when {
    course.id == "az-305" -> "SA"
    course.id.startsWith("az") -> "AZ"
    course.id.startsWith("ai") -> "AI"
    course.id.startsWith("de") -> "DE"
    course.id.startsWith("ielts") -> "IE"
    else -> course.title.take(2).uppercase(Locale.ROOT)
}

/** The exam a course leads to, as short as the board's header prints it. */
internal fun boardExam(course: Course): String = when (course.id) {
    "az-204" -> "AZ-204"
    "ai-200" -> "AI-200"
    "az-305" -> "AZ-305"
    "de-a1" -> "Goethe A1"
    "ielts" -> "IELTS"
    else -> course.title
}

/** "3 SERVICES · 2 ON YOUR LINE" */
internal fun servicesLine(total: Int, mine: Int): String = "$total service${if (total == 1) "" else "s"} · ${if (mine == 0) "none" else "$mine"} on your line".uppercase(Locale.ROOT)

/** "5 modules, then a mock exam", or "Sanya's line · 5 modules" for a course that is only your partner's. */
internal fun serviceLine(course: Course, partnerOnly: String?): String {
    val modules = "${course.modules.size} module${if (course.modules.size == 1) "" else "s"}"
    val rest = if (course.mockExam != null) ", then a mock exam" else ""
    return if (partnerOnly != null) "$partnerOnly's line · $modules" else modules + rest
}

private fun learned(p: CourseProgress?, course: Course) = p?.let { pr -> course.modules.sumOf { pr.moduleLearned(it) } } ?: 0

/**
 * Learn on the Departures board, as its prototype draws it: the daily review
 * and the words as two tiles, then each course as a service with its line
 * code, how far each of you has got and where it goes next.
 */
@Composable
internal fun BoardLearnBody(
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
    val mine = state.courses.count { state.myRole in state.owners[it.id].orEmpty() }
    LazyColumn(modifier = Modifier.fillMaxSize().background(AppTheme.colors.groupedBackground), contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            Column(modifier = Modifier.statusBarsPadding().padding(start = BoardGutter, end = BoardGutter, top = 18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                BoardMono(servicesLine(state.courses.size, mine))
                BoardTitle("Learn", size = 52.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }
        item {
            val questions = state.daily.steps.firstOrNull { it.kind == StudyKind.QUESTIONS }
            val words = state.daily.steps.firstOrNull { it.kind == StudyKind.WORDS }
            val due = state.dailyDue.values.sum()
            val reviewCourse = questions?.courseId ?: state.dailyDue.maxByOrNull { it.value }?.key
            val deckDue = state.decks.values.sumOf { it.due + it.fresh }
            val deckCourse = words?.courseId ?: state.decks.keys.firstOrNull()
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 16.dp).guideAnchor(GuideAnchors.LEARN_TODAY_CARD)) {
                Tile(
                    "Daily review", "%02d".format(due), if (due == 0) "nothing due today" else "question${if (due == 1) "" else "s"} due · ${(due * 0.8).toInt().coerceAtLeast(1)} min",
                    lit = due > 0, color = myColor, modifier = Modifier.weight(1f),
                    onClick = reviewCourse?.takeIf { due > 0 }?.let { c -> { onOpenQuiz(c, Courses.DAILY_MODULE_ID) } },
                )
                Tile(
                    "Words", "%02d".format(deckDue), if (deckDue == 0) "nothing due in the deck" else "German card${if (deckDue == 1) "" else "s"} due · ${(deckDue / 4).coerceAtLeast(1)} min",
                    lit = false, color = myColor, modifier = Modifier.weight(1f),
                    onClick = deckCourse?.let { c -> { onOpenDeck(c) } },
                )
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 20.dp, bottom = 8.dp)) {
                BoardLabel("Line", spacing = 0.12.em, modifier = Modifier.width(48.dp))
                BoardLabel("Service", spacing = 0.12.em, modifier = Modifier.weight(1f))
                BoardLabel("Done", spacing = 0.12.em, modifier = Modifier.width(60.dp), textAlign = TextAlign.End)
            }
            Box(Modifier.padding(horizontal = BoardGutter).fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
        }
        state.courses.forEachIndexed { i, course ->
            item(key = course.id) {
                val owners = state.owners[course.id].orEmpty()
                val forMe = state.myRole in owners
                val forPartner = owners.any { it != state.myRole }
                val p = state.progress[course.id]
                val partner = state.partnerProgress[course.id]
                val next = p?.nextTopic(course)
                val color = when {
                    forMe && forPartner -> lerp(myColor, AppTheme.colors.ink, 0.35f)
                    forMe || !forPartner -> myColor
                    else -> partnerColor
                }
                val lead = if (forMe || !forPartner) learned(p, course) else learned(partner, course)
                Column(modifier = Modifier.padding(horizontal = BoardGutter).then(if (i == 0) Modifier.guideAnchor(GuideAnchors.LEARN_COURSES) else Modifier)) {
                    Column(modifier = Modifier.fillMaxWidth().clickable { onOpenCourse(course.id) }.padding(vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.width(48.dp).height(26.dp).clip(RoundedCornerShape(6.dp)).background(color)) {
                                BoardLabel(lineCode(course), color = AppTheme.colors.groupedBackground, size = 14.sp, weight = FontWeight.Bold, spacing = 0.06.em)
                            }
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(course.title, color = AppTheme.colors.ink, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 22.sp))
                                BoardText(serviceLine(course, if (forMe || !forPartner) null else state.partnerName), size = 12.sp)
                            }
                            BoardMono("${(lead * 100 / course.topicCount.coerceAtLeast(1))}%", color = AppTheme.colors.ink, size = 14.sp, weight = FontWeight.Bold, spacing = 0.sp, modifier = Modifier.width(60.dp), textAlign = TextAlign.End)
                            FavoriteStar(course, course.id in state.favorites, { onToggleFavorite(course.id) }, AppTheme.colors.accent, AppTheme.colors.secondaryLabel)
                        }
                        if (forMe || !forPartner) Bar("You", learned(p, course), course.topicCount, myColor)
                        if (forPartner) Bar(state.partnerName, learned(partner, course), course.topicCount, partnerColor)
                        Row(
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(start = 58.dp).heightIn(min = 28.dp).clip(RoundedCornerShape(4.dp))
                                .clickable(role = Role.Button) { if (forMe && next != null) onOpenTopic(course.id, next.second.id) else onOpenCourse(course.id) },
                        ) {
                            BoardLabel(if (!forMe) "View" else if (next != null) "Next" else "Done", color = color, size = 13.sp, weight = FontWeight.Bold)
                            BoardText(
                                when {
                                    !forMe -> "See ${state.partnerName}'s progress"
                                    next != null -> next.second.title.substringBefore(":").let { k -> next.second.title.substringAfter(": ", "").takeIf { it.isNotBlank() }?.let { "Module ${next.first.number} · $k" } ?: k }
                                    else -> "Every topic read"
                                },
                                color = AppTheme.colors.ink.copy(alpha = 0.92f), size = 13.sp,
                            )
                        }
                    }
                    Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                }
            }
        }
    }
}

@Composable
private fun Tile(label: String, value: String, detail: String, lit: Boolean, color: Color, modifier: Modifier, onClick: (() -> Unit)?) {
    val shape = RoundedCornerShape(14.dp)
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.clip(shape).background(AppTheme.colors.card).border(1.5.dp, if (lit) color else AppTheme.colors.separator, shape)
            .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier).padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
        BoardLabel(label, color = if (lit) color else AppTheme.colors.secondaryLabel, size = 13.sp, weight = FontWeight.Bold, spacing = 0.12.em)
        BoardMono(value, color = AppTheme.colors.ink, size = 30.sp, weight = FontWeight.Bold, spacing = 0.sp)
        BoardText(detail, size = 12.sp)
    }
}

@Composable
private fun Bar(name: String, done: Int, total: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        BoardLabel(name, spacing = 0.1.em, modifier = Modifier.width(48.dp))
        Box(Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)).background(AppTheme.colors.separator)) {
            Box(Modifier.fillMaxWidth((done.toFloat() / total.coerceAtLeast(1)).coerceIn(0.02f, 1f)).height(6.dp).clip(RoundedCornerShape(3.dp)).background(color))
        }
        BoardMono("$done/$total", spacing = 0.sp, modifier = Modifier.width(60.dp), textAlign = TextAlign.End)
    }
}

/**
 * A course on the board, as its prototype draws it: the line and its stops,
 * how far each of you has got, the one stop worth a return trip, then the
 * modules as stops on a line with the one you are at open to its topics,
 * and the paper practice at the terminal.
 */
@Composable
internal fun BoardCourseBody(
    course: Course,
    state: LearnUiState,
    today: LocalDate,
    onBack: () -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onOpenQuiz: (String, String) -> Unit,
) {
    val p = state.progress[course.id]
    val partner = state.partnerProgress[course.id]
    val owners = state.owners[course.id].orEmpty()
    val meIsTech = state.myRole == PersonRole.TECH
    val myColor = AppTheme.colors.track(meIsTech)
    val partnerColor = AppTheme.colors.track(!meIsTech)
    val showPartner = owners.any { it != state.myRole }
    val showMe = state.myRole in owners || !showPartner
    val mastery = remember(course, p, today) { if (p == null) emptyMap() else course.mastery(p, today) }
    val revisits = remember(course, p, today) { if (p == null) emptyList() else course.revisits(p, today) }
    val nextTopic = p?.nextTopic(course)
    val hereIndex = course.modules.indexOfFirst { it.id == nextTopic?.first?.id }.takeIf { it >= 0 } ?: course.modules.size
    var open by rememberSaveable(course.id) { mutableStateOf(nextTopic?.first?.id) }

    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(inner), contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(Modifier.height(44.dp), contentAlignment = Alignment.CenterStart) { BoardBack(onBack, "Back to Learn") }
                    BoardMono("LINE ${lineCode(course)} · ${course.modules.size} STOPS · ${boardExam(course).uppercase(Locale.ROOT)}")
                    BoardTitle(course.title, size = 52.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        if (showMe) BoardMono("YOU ${learned(p, course)}/${course.topicCount}", color = myColor, size = 12.sp, spacing = 0.sp)
                        if (showPartner) BoardMono("${state.partnerName.uppercase(Locale.ROOT)} ${learned(partner, course)}/${course.topicCount}", color = partnerColor, size = 12.sp, spacing = 0.sp)
                    }
                }
            }
            revisits.firstOrNull()?.let { r ->
                item {
                    val t = Courses.topic(course.id, r.topicId)?.second
                    Row(
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 14.dp).fillMaxWidth().height(IntrinsicSize.Min)
                            .clip(RoundedCornerShape(14.dp)).background(lerp(AppTheme.colors.groupedBackground, AppTheme.colors.accent, 0.1f))
                            .clickable { onOpenTopic(course.id, r.topicId) },
                    ) {
                        Box(Modifier.width(4.dp).fillMaxHeight().background(AppTheme.colors.accent))
                        Column(Modifier.weight(1f).padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            BoardLabel("Delayed · worth a return trip", color = AppTheme.colors.accent, size = 13.sp, weight = FontWeight.Bold, spacing = 0.12.em)
                            BoardText(
                                (t?.title?.substringBefore(":") ?: r.topicId) + buildList {
                                    if (r.missed > 0) add("${r.missed} missed")
                                    if (r.due > 0) add("${r.due} due again")
                                }.joinToString(" · ", prefix = " · ").takeIf { r.missed > 0 || r.due > 0 }.orEmpty() + if (revisits.size > 1) ", and ${revisits.size - 1} more" else "",
                                color = AppTheme.colors.ink, weight = FontWeight.SemiBold,
                            )
                        }
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.accent, modifier = Modifier.padding(end = 12.dp).size(18.dp))
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
            course.modules.forEachIndexed { i, m ->
                item(key = m.id) {
                    val passed = i < hereIndex
                    val here = i == hereIndex
                    val mine = p?.moduleLearned(m) ?: 0
                    val theirs = partner?.moduleLearned(m) ?: 0
                    val cleared = mine >= m.topics.size && (m.quiz == null || p?.passedModuleIds?.contains(m.id) == true)
                    val isOpen = open == m.id
                    Row(modifier = Modifier.padding(horizontal = BoardGutter).fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(Modifier.width(22.dp).fillMaxHeight()) {
                            Box(Modifier.offset(x = 9.dp).width(4.dp).fillMaxHeight().background(if (passed) myColor else AppTheme.colors.separator))
                            Box(
                                Modifier.offset(x = 3.dp, y = 13.dp).size(16.dp).clip(CircleShape)
                                    .background(if (passed) myColor else AppTheme.colors.groupedBackground)
                                    .border(3.dp, if (passed || here) myColor else AppTheme.colors.strongBorder, CircleShape),
                            )
                        }
                        Column(Modifier.weight(1f).padding(vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.heightIn(min = 24.dp).clickable(onClickLabel = if (isOpen) "Close the stop" else "Open the stop") { open = if (isOpen) null else m.id },
                            ) {
                                BoardMono("%02d".format(m.number), size = 12.sp, spacing = 0.sp)
                                BoardText(m.title, color = if (passed || here) AppTheme.colors.ink else AppTheme.colors.secondaryLabel, weight = if (here) FontWeight.SemiBold else FontWeight.Medium, modifier = Modifier.weight(1f))
                                val (tag, tagInk) = when {
                                    here -> "You are here" to myColor
                                    passed && cleared -> "Cleared" to AppTheme.colors.success
                                    passed -> "$mine/${m.topics.size}" to myColor
                                    showPartner && theirs > 0 -> "${state.partnerName} $theirs/${m.topics.size}" to partnerColor
                                    !m.available -> "Soon" to AppTheme.colors.secondaryLabel
                                    else -> "" to AppTheme.colors.secondaryLabel
                                }
                                if (tag.isNotEmpty()) BoardLabel(tag, color = tagInk, weight = FontWeight.Bold, spacing = 0.1.em)
                            }
                            if (isOpen) {
                                Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(AppTheme.colors.card).padding(horizontal = 12.dp, vertical = 2.dp)) {
                                    if (!m.available) BoardText("${m.summary} Coming in the next update.", size = 13.sp, modifier = Modifier.padding(vertical = 10.dp))
                                    m.topics.forEach { t ->
                                        val mt = mastery[t.id]
                                        val isNext = t.id == nextTopic?.second?.id
                                        val (status, statusInk) = when {
                                            mt?.needsRevisit == true -> "Look again" to AppTheme.colors.warn
                                            isNext -> "Next · ${t.minutes} min" to myColor
                                            mt != null && mt.level == Mastery.HELD -> "Held" to AppTheme.colors.success
                                            mt != null && mt.level != Mastery.UNREAD -> "Read" to AppTheme.colors.success
                                            else -> "${t.minutes} min" to AppTheme.colors.secondaryLabel
                                        }
                                        Stop(
                                            t.title.substringBefore(":").let { kind -> t.title.substringAfter(": ", "").takeIf { it.isNotBlank() }?.let { "$kind · $it" } ?: kind },
                                            status, statusInk, if (isNext) myColor else AppTheme.colors.ink,
                                        ) { onOpenTopic(course.id, t.id) }
                                    }
                                    m.quiz?.let { q ->
                                        val passedQuiz = p?.passedModuleIds?.contains(m.id) == true
                                        val best = p?.bestPercentByModule?.get(m.id)
                                        Stop(
                                            q.title,
                                            when {
                                                passedQuiz -> "Passed · $best%"
                                                best != null -> "Best $best% · need ${q.passPercent}%"
                                                mine < m.topics.size -> "After topics"
                                                else -> "${q.questions.size} questions"
                                            },
                                            if (passedQuiz) AppTheme.colors.success else AppTheme.colors.secondaryLabel, AppTheme.colors.ink, last = true,
                                        ) { onOpenQuiz(course.id, m.id) }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item { Terminal(course, p, state.dailyDue[course.id] ?: 0, onOpenQuiz) }
        }
    }
}

@Composable
private fun Stop(title: String, status: String, statusInk: Color, titleInk: Color, last: Boolean = false, onClick: () -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().heightIn(min = 44.dp).clickable(onClick = onClick)) {
            BoardText(title, color = titleInk, size = 14.sp, modifier = Modifier.weight(1f))
            BoardLabel(status, color = statusInk, weight = FontWeight.Bold, spacing = 0.1.em)
        }
        if (!last) Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
    }
}

/** The paper practice at the end of the line, outlined, and the review sets under it when anything waits there. */
@Composable
private fun Terminal(course: Course, p: CourseProgress?, dailyDue: Int, onOpenQuiz: (String, String) -> Unit) {
    val mock = course.mockExam
    val review = p?.reviewQuestions(course).orEmpty()
    val passedAll = p != null && p.modulesPassed(course) == course.modules.count { it.available }
    Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        if (mock != null) {
            val shape = RoundedCornerShape(14.dp)
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth().clip(shape).border(2.dp, AppTheme.colors.ink, shape).clickable { onOpenQuiz(course.id, Courses.MOCK_MODULE_ID) }.padding(14.dp),
            ) {
                BoardLabel("Terminal · paper practice", size = 13.sp, weight = FontWeight.Bold, spacing = 0.12.em)
                Text(mock.title, color = AppTheme.colors.ink, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 24.sp))
                BoardText(
                    when {
                        p?.mockPassed == true -> "Passed, best ${p.mockBest}%."
                        p?.mockBest != null -> "Best ${p.mockBest}%, need ${mock.passPercent}%."
                        !passedAll -> "${mock.drawCount ?: mock.questions.size} questions, ${mock.timeLimitMinutes} minutes. Clear the module tests first for a fair reading."
                        else -> "${mock.drawCount ?: mock.questions.size} questions, ${mock.timeLimitMinutes} minutes, pass at ${mock.passPercent}%."
                    },
                    size = 13.sp,
                )
            }
        }
        // Not in the prototype: the review sets, when there is anything in them.
        if (dailyDue > 0) TerminalLink("Daily review · $dailyDue due") { onOpenQuiz(course.id, Courses.DAILY_MODULE_ID) }
        if (review.isNotEmpty()) TerminalLink("Review ${review.size} missed") { onOpenQuiz(course.id, Courses.REVIEW_MODULE_ID) }
    }
}

@Composable
private fun TerminalLink(text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.heightIn(min = 44.dp).clip(RoundedCornerShape(6.dp)).clickable(role = Role.Button, onClick = onClick),
    ) {
        BoardLabel(text, color = AppTheme.colors.accent, size = 14.sp, weight = FontWeight.Bold, spacing = 0.1.em)
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.accent, modifier = Modifier.size(16.dp))
    }
}
