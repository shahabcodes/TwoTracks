package com.shahaabapps.twotracks.feature.today

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoSecondaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Shapes
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.changelog.WhatsNewDialog
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.components.ProgressBar
import com.shahaabapps.twotracks.ui.components.SheetGrabber
import com.shahaabapps.twotracks.ui.guide.LocalGuide

/**
 * Today, as the Parallel prototype draws it, and nothing else on it:
 *
 *  - the date, which opens the brief, and the two of you in your colours;
 *  - the greeting, Settings, and a line on what is left for each of you;
 *  - your one thing, outlined in ink, with the level it moves and the button
 *    that starts it;
 *  - both days on your clock as one timeline, each session a dot on its
 *    owner's rail: filled when done, open while it is not.
 *
 * Marking a session done or skipping it, which the prototype does not draw,
 * lives behind a tap on your own sessions in the timeline, so the screen
 * keeps the prototype's shape and loses none of what it could do.
 */
@Composable
fun TodayScreen(
    onOpenLevel: (String) -> Unit,
    onOpenPartner: () -> Unit,
    onOpenGoal: (String) -> Unit,
    onOpenWeek: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenChangelog: () -> Unit,
    onOpenTopic: (String, String) -> Unit = { _, _ -> },
    onOpenQuiz: (String, String) -> Unit = { _, _ -> },
    viewModel: TodayViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TodayContent(
        state = state,
        onOpenLevel = onOpenLevel, onOpenPartner = onOpenPartner, onOpenGoal = onOpenGoal, onOpenWeek = onOpenWeek,
        onOpenSettings = onOpenSettings, onOpenChangelog = onOpenChangelog, onOpenTopic = onOpenTopic, onOpenQuiz = onOpenQuiz,
        onOpenBrief = viewModel::openBrief, onDismissBrief = viewModel::dismissBrief, onDismissWhatsNew = viewModel::dismissWhatsNew,
        onDone = viewModel::done, onSkip = viewModel::skip, onReopen = viewModel::reopen, onMessageShown = viewModel::clearMessage,
    )
}

/** Today drawn from its state alone, so a preview can show it without an account. */
@Composable
fun TodayContent(
    state: TodayUiState,
    onOpenLevel: (String) -> Unit,
    onOpenPartner: () -> Unit,
    onOpenGoal: (String) -> Unit,
    onOpenWeek: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenChangelog: () -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onOpenQuiz: (String, String) -> Unit,
    onOpenBrief: () -> Unit,
    onDismissBrief: () -> Unit,
    onDismissWhatsNew: () -> Unit,
    onDone: (SessionRow) -> Unit,
    onSkip: (String) -> Unit,
    onReopen: (String) -> Unit,
    onMessageShown: () -> Unit,
) {
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(state.message) { state.message?.let { snackbar.showSnackbar(it); onMessageShown() } }
    var actionsFor by remember { mutableStateOf<SessionRow?>(null) }
    val haptic = LocalHapticFeedback.current

    val me = state.me
    val partner = state.partner?.takeIf { it.profile != null }
    val meIsTech = me?.role != PersonRole.TEACHER
    val myColor = AppTheme.colors.track(meIsTech)
    val partnerColor = AppTheme.colors.track(!meIsTech)

    Box {
        LazyColumn(contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item { Header(state, myColor, partnerColor, onOpenBrief = onOpenBrief, onOpenSettings = onOpenSettings) }

            if (me != null) {
                item {
                    OneThing(
                        row = me.sessions.firstOrNull { it.session.status == SessionStatus.PLANNED },
                        me = me, state = state, color = myColor,
                        onOpenLevel = onOpenLevel, onOpenTopic = onOpenTopic, onOpenQuiz = onOpenQuiz,
                        onDone = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onDone(it) },
                    )
                }
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal - 8.dp, top = 24.dp, bottom = 8.dp),
                ) {
                    Text("Today, on your clock", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f).semantics { heading() })
                    MonoLink("Your week", onClick = onOpenWeek)
                }
            }

            val entries = buildList {
                me?.sessions?.forEach { add(TimelineEntry(it, mine = true)) }
                partner?.sessions?.forEach { add(TimelineEntry(it, mine = false)) }
            }.sortedBy { it.row.readerStartMinutes }
            val next = me?.sessions?.firstOrNull { it.session.status == SessionStatus.PLANNED }
            if (entries.isEmpty()) {
                item {
                    Text(
                        "No sessions on either clock today. Anything either of you reads still counts.",
                        style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
                        modifier = Modifier.padding(horizontal = Spacing.listHorizontal),
                    )
                }
            }
            entries.forEachIndexed { i, entry ->
                item(key = (if (entry.mine) "me-" else "partner-") + entry.row.session.id) {
                    TimelineRow(
                        entry = entry,
                        meIsTech = meIsTech,
                        last = i == entries.lastIndex,
                        isNext = entry.mine && entry.row == next,
                        partnerName = partner?.name.orEmpty(),
                        partnerZone = partner?.profile?.timezone?.substringAfterLast('/')?.replace('_', ' ').orEmpty(),
                        onClick = { if (entry.mine) actionsFor = entry.row else onOpenPartner() },
                    )
                }
            }
        }
        SnackbarHost(hostState = snackbar, modifier = Modifier.align(Alignment.BottomCenter))
    }

    actionsFor?.let { row ->
        SessionActionsSheet(
            row = row,
            onDismiss = { actionsFor = null },
            onOpenGoal = { actionsFor = null; row.goal?.let { onOpenGoal(it.id) } },
            onOpenTopic = { c, t -> actionsFor = null; onOpenTopic(c, t) },
            onOpenQuiz = { c, m -> actionsFor = null; onOpenQuiz(c, m) },
            onDone = { actionsFor = null; haptic.performHapticFeedback(HapticFeedbackType.LongPress); onDone(row) },
            onSkip = { actionsFor = null; onSkip(row.session.id) },
            onReopen = { actionsFor = null; onReopen(row.session.id) },
        )
    }

    // After an update, What's new comes first; the daily brief follows once it is dismissed.
    //
    // Neither interrupts the tour. The tour walks onto this screen by itself,
    // and a dialog opening on arrival lands on top of it, taking the taps and
    // leaving a tour that cannot be advanced or dismissed. Both of these wait
    // until it is over; neither is lost, as both are shown again next time.
    val touring = LocalGuide.current?.running == true
    val whatsNew = state.whatsNew
    val brief = state.brief
    if (touring) {
        // nothing: the tour is talking
    } else if (whatsNew != null) {
        WhatsNewDialog(release = whatsNew, onDismiss = onDismissWhatsNew, onViewMore = { onDismissWhatsNew(); onOpenChangelog() })
    } else if (brief != null && !state.loading) {
        val nextTopic = me?.sessions?.firstOrNull { it.session.status == SessionStatus.PLANNED }?.nextTopic
        DailyBriefSheet(
            brief = brief, color = myColor,
            onStart = { onDismissBrief(); nextTopic?.let { onOpenTopic(it.courseId, it.topicId) } },
            onDismiss = onDismissBrief,
        )
    }
}

private data class TimelineEntry(val row: SessionRow, val mine: Boolean)

@Composable
private fun Header(state: TodayUiState, myColor: Color, partnerColor: Color, onOpenBrief: () -> Unit, onOpenSettings: () -> Unit) {
    val me = state.me
    val partner = state.partner?.takeIf { it.profile != null }
    Column(
        modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(role = Role.Button, onClickLabel = "Open the brief", onClick = onOpenBrief).heightIn(min = 28.dp),
            ) {
                Text(
                    (state.today.format(java.time.format.DateTimeFormatter.ofPattern("EEE · d MMM", java.util.Locale.ENGLISH)) + " · brief").uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp, letterSpacing = 0.7.sp), color = AppTheme.colors.secondaryLabel,
                    maxLines = 1, softWrap = false,
                )
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(14.dp))
            }
            Box(Modifier.weight(1f).height(1.dp))
            if (me != null) Legend(me.name, myColor)
            if (partner != null) Legend(partner.name, partnerColor, Modifier.padding(start = 12.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 2.dp)) {
            val hour = Clock.now(me?.profile).hour
            Text(
                DailyBriefs.greeting(me?.name ?: "you", hour) + ".",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.weight(1f).semantics { heading() },
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.offset(x = 10.dp).size(44.dp).clip(CircleShape).clickable(role = Role.Button, onClickLabel = "Settings", onClick = onOpenSettings),
            ) { Icon(Icons.Outlined.Settings, contentDescription = "Settings", tint = AppTheme.colors.ink, modifier = Modifier.size(22.dp)) }
        }
        val mine = me?.sessions?.count { it.session.status == SessionStatus.PLANNED } ?: 0
        val theirs = partner?.sessions?.count { it.session.status == SessionStatus.PLANNED } ?: 0
        Text(todayLine(mine, theirs, partner?.name), style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
    }
}

/** "One session left for you today. Sanya has two." */
internal fun todayLine(mine: Int, theirs: Int, partnerName: String?): String {
    fun count(n: Int) = when (n) { 1 -> "one"; 2 -> "two"; 3 -> "three"; 4 -> "four"; else -> n.toString() }
    val you = when (mine) {
        0 -> "Nothing left for you today."
        1 -> "One session left for you today."
        else -> "${count(mine).replaceFirstChar { it.uppercase() }} sessions left for you today."
    }
    if (partnerName == null) return you
    return you + " " + if (theirs == 0) "$partnerName is done." else "$partnerName has ${count(theirs)}."
}

@Composable
private fun Legend(name: String, color: Color, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = modifier) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(color))
        Text(name.uppercase(), style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp, letterSpacing = 0.7.sp), color = AppTheme.colors.secondaryLabel)
    }
}

@Composable
private fun MonoLink(text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clip(CircleShape).clickable(role = Role.Button, onClick = onClick).heightIn(min = 28.dp).padding(horizontal = 4.dp),
    ) {
        Text(text.uppercase(), style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 0.66.sp), color = AppTheme.colors.accentText)
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.accentText, modifier = Modifier.size(14.dp))
    }
}

/** Your one thing: the next open session, the level it moves, and the button that starts it. */
@Composable
private fun OneThing(
    row: SessionRow?,
    me: PersonCard,
    state: TodayUiState,
    color: Color,
    onOpenLevel: (String) -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onOpenQuiz: (String, String) -> Unit,
    onDone: (SessionRow) -> Unit,
) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 22.dp)
            .fillMaxWidth()
            .clip(shape)
            .border(Spacing.rule, AppTheme.colors.ink, shape)
            .padding(18.dp),
    ) {
        Row {
            Text("YOUR ONE THING", style = MaterialTheme.typography.labelMedium, color = color, modifier = Modifier.weight(1f))
            if (row != null) {
                Text(
                    "${Dates.minutesToClock(row.session.startMinutes)} · ${row.session.durationMinutes} MIN",
                    style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel,
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val topic = row?.nextTopic
            Text(
                when {
                    row == null && me.isRestDay -> "A rest day"
                    row == null -> "Every session is done"
                    else -> (row.goal?.title ?: "Study") + " · " + (topic?.title ?: row.componentName)
                },
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 23.sp, lineHeight = 28.sp),
            )
            Text(
                when {
                    row == null -> "Nothing is planned for the rest of today. Anything you read or review still counts."
                    topic != null -> "${row.componentName}. About ${topic.minutes} minutes to read, then the checks."
                    row.review != null -> "${row.componentName}. ${row.review.count} missed question${if (row.review.count == 1) "" else "s"} waiting to come back."
                    else -> "${row.componentName} · ${row.timeLabel}. Minutes count themselves as you study."
                },
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp, lineHeight = 20.sp), color = AppTheme.colors.secondaryLabel,
            )
        }
        state.level?.let { lvl ->
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(top = 2.dp).clickable(onClickLabel = "Open the level") { onOpenLevel(lvl.level.id) },
            ) {
                Row {
                    Text("Level ${lvl.level.number} · ${lvl.level.name}", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel, modifier = Modifier.weight(1f))
                    Text("${lvl.requiredDone} / ${lvl.requiredTotal}", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, fontFamily = MonoFamily), color = AppTheme.colors.secondaryLabel)
                }
                ProgressBar(fraction = if (lvl.requiredTotal == 0) 0f else lvl.requiredDone.toFloat() / lvl.requiredTotal, color = color)
            }
        }
        if (row != null) {
            val topic = row.nextTopic
            val review = row.review
            Box(Modifier.padding(top = 6.dp)) {
                when {
                    topic != null -> CupertinoPrimaryButton(text = "Start reading  →", onClick = { onOpenTopic(topic.courseId, topic.topicId) })
                    review != null -> CupertinoPrimaryButton(text = "Review the ${review.count} missed  →", onClick = { onOpenQuiz(review.courseId, Courses.REVIEW_MODULE_ID) })
                    else -> CupertinoPrimaryButton(text = "Mark done", onClick = { onDone(row) })
                }
            }
        }
    }
}

/**
 * One session on the shared timeline: the time on your clock, both rails with
 * this session's dot on its owner's, and who and how long underneath.
 */
@Composable
private fun TimelineRow(
    entry: TimelineEntry,
    meIsTech: Boolean,
    last: Boolean,
    isNext: Boolean,
    partnerName: String,
    partnerZone: String,
    onClick: () -> Unit,
) {
    val row = entry.row
    val status = row.session.status
    val done = status == SessionStatus.DONE
    val ownerIsTech = if (entry.mine) meIsTech else !meIsTech
    val owner = AppTheme.colors.track(ownerIsTech)
    val tech = AppTheme.colors.tech
    val teacher = AppTheme.colors.teacher
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .heightIn(min = if (last) 52.dp else 56.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.listHorizontal),
    ) {
        Text(
            Dates.minutesToClock(row.readerStartMinutes),
            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFamily, fontSize = 13.sp),
            fontWeight = if (isNext) FontWeight.Medium else FontWeight.Normal,
            color = if (isNext) AppTheme.colors.ink else AppTheme.colors.secondaryLabel,
            modifier = Modifier.width(44.dp).padding(top = 3.dp),
        )
        // The developer's rail on the left and the teacher's on the right, as on the sign-in mark.
        Box(Modifier.width(26.dp).fillMaxHeight()) {
            Box(Modifier.offset(x = 7.dp).width(2.dp).then(if (last) Modifier.height(24.dp) else Modifier.fillMaxHeight()).background(tech))
            Box(Modifier.offset(x = 17.dp).width(2.dp).then(if (last) Modifier.height(24.dp) else Modifier.fillMaxHeight()).background(teacher))
            Box(
                Modifier
                    .offset(x = if (ownerIsTech) 2.dp else 12.dp, y = 4.dp)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(if (done) owner else AppTheme.colors.groupedBackground)
                    .then(if (done) Modifier else Modifier.border(2.dp, owner, CircleShape)),
            )
        }
        Column(Modifier.weight(1f).padding(bottom = if (last) 0.dp else 14.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                (row.goal?.title ?: "Study") + " · " + (row.nextTopic?.takeIf { isNext }?.title ?: row.componentName.ifBlank { "Study" }),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isNext) FontWeight.SemiBold else FontWeight.Medium,
                color = if (done || status == SessionStatus.SKIPPED) AppTheme.colors.secondaryLabel else AppTheme.colors.ink,
                textDecoration = if (done) TextDecoration.LineThrough else TextDecoration.None,
            )
            val tail = when {
                done -> " · done"
                status == SessionStatus.SKIPPED -> " · skipped"
                isNext -> " · next"
                row.session.isClass -> " · class"
                else -> ""
            }
            Text(
                if (entry.mine) "You · ${row.session.durationMinutes} min$tail"
                else "$partnerName · ${row.timeLabel.substringBefore(" to ")}" + (if (partnerZone.isNotBlank()) " in $partnerZone" else "") + " · ${row.session.durationMinutes} min$tail",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel,
            )
        }
    }
}

/** What can be done with one of your sessions: the prototype has no room for these on the screen itself. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionActionsSheet(
    row: SessionRow,
    onDismiss: () -> Unit,
    onOpenGoal: () -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onOpenQuiz: (String, String) -> Unit,
    onDone: () -> Unit,
    onSkip: () -> Unit,
    onReopen: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppTheme.colors.groupedBackground,
        shape = Shapes.sheet,
        dragHandle = { SheetGrabber() },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = Spacing.listHorizontal).padding(bottom = Spacing.md).navigationBarsPadding(),
        ) {
            Text(
                "${Dates.minutesToClock(row.session.startMinutes)} · ${row.session.durationMinutes} MIN",
                style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = 8.dp),
            )
            Text((row.goal?.title ?: "Study") + " · " + row.componentName, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.semantics { heading() })
            Box(Modifier.height(4.dp))
            when (row.session.status) {
                SessionStatus.PLANNED -> {
                    row.nextTopic?.let { t -> CupertinoPrimaryButton(text = "Read: ${t.title} · ${t.minutes} min", onClick = { onOpenTopic(t.courseId, t.topicId) }) }
                    row.review?.let { r -> CupertinoSecondaryButton(text = "Review ${r.count} missed question${if (r.count == 1) "" else "s"}", onClick = { onOpenQuiz(r.courseId, Courses.REVIEW_MODULE_ID) }) }
                    if (row.nextTopic == null) CupertinoPrimaryButton(text = "Mark done", onClick = onDone) else CupertinoSecondaryButton(text = "Mark done", onClick = onDone)
                    CupertinoTextButton(text = "Skip today", onClick = onSkip)
                }
                SessionStatus.SKIPPED -> CupertinoPrimaryButton(text = "Reopen", onClick = onReopen)
                SessionStatus.DONE -> Text("Done. The course counted the time as you studied.", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
            }
            if (row.goal != null) CupertinoTextButton(text = "Open the goal", onClick = onOpenGoal)
        }
    }
}
