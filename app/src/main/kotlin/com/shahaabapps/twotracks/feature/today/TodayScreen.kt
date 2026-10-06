package com.shahaabapps.twotracks.feature.today

import com.shahaabapps.core.ui.components.CupertinoSecondaryButton
import androidx.compose.foundation.layout.fillMaxSize
import com.shahaabapps.twotracks.ui.components.AuroraLayer
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.ui.components.Avatar
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoDisclosure
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.components.Hero
import com.shahaabapps.core.ui.components.StatusChip
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Shapes
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.changelog.WhatsNewDialog
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.components.AnimatedSwap
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.LogMinutesSheet
import com.shahaabapps.twotracks.ui.components.LogTarget
import com.shahaabapps.twotracks.ui.components.ProgressBar

/**
 * Level 1 of the app: the current level line, your card, your partner's card,
 * one status sentence, streaks and the next deadline. Everything else is a tap away.
 */
@Composable
fun TodayScreen(
    onOpenLevel: (String) -> Unit,
    onOpenPartner: () -> Unit,
    onOpenGoal: (String) -> Unit,
    onOpenWeek: () -> Unit,
    onOpenChangelog: () -> Unit,
    onOpenTopic: (String, String) -> Unit = { _, _ -> },
    viewModel: TodayViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var logTarget by remember { mutableStateOf<LogTarget?>(null) }
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(state.message) { state.message?.let { snackbar.showSnackbar(it); viewModel.clearMessage() } }

    val me = state.me
    Box {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                Hero(title = "Today", subtitle = Clock.formatLong(state.today), backdrop = { AuroraLayer(modifier = Modifier.fillMaxSize(), sky = false, grid = false, intensity = 0.8f, seed = 3) }) {
                    state.level?.let { lvl ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = Spacing.sm)
                                .clip(Shapes.card)
                                .background(Color.White.copy(alpha = 0.18f))
                                .clickable { onOpenLevel(lvl.level.id) }
                                .padding(horizontal = Spacing.md, vertical = 12.dp),
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Level ${lvl.level.number} · ${lvl.level.name}", style = MaterialTheme.typography.titleMedium, color = Color.White)
                                Text(
                                    "${lvl.requiredDone} of ${lvl.requiredTotal} done · " + when {
                                        lvl.daysLeft == null -> ""
                                        lvl.daysLeft!! < 0 -> "${-lvl.daysLeft!!} days overdue"
                                        else -> "${lvl.daysLeft} days left"
                                    },
                                    style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.9f),
                                )
                                ProgressBar(
                                    fraction = if (lvl.requiredTotal == 0) 0f else lvl.requiredDone.toFloat() / lvl.requiredTotal,
                                    color = Color.White, height = 5.dp, modifier = Modifier.padding(top = 6.dp),
                                )
                            }
                            Text("›", color = Color.White, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(start = Spacing.sm))
                        }
                    }
                }
            }

            // My card
            if (me != null) {
                item {
                    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        Text("You", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
                        if (me.isRestDay) {
                            Text("Rest day", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 2.dp))
                            Text("No slot today. Log something anyway if you studied.", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                            if (state.activeGoals.isNotEmpty()) {
                                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.padding(top = Spacing.sm)) {
                                    state.activeGoals.take(3).forEach { g ->
                                        Chip(text = "Log ${g.title}", onClick = { logTarget = LogTarget(goalId = g.id, componentId = null, dateIso = Clock.formatDateIso(state.today)) })
                                    }
                                }
                            }
                        }
                        me.sessions.forEachIndexed { i, row -> SessionBlock(row, first = i == 0, onOpenGoal = onOpenGoal, onOpenTopic = onOpenTopic, onDone = { viewModel.done(row) }, onSkip = { viewModel.skip(row.session.id) }, onReopen = { viewModel.reopen(row.session.id) }, onLog = {
                            logTarget = LogTarget(goalId = row.session.goalId, componentId = row.session.componentId, sessionId = row.session.id, defaultMinutes = row.session.durationMinutes, dateIso = row.session.dateIso, isClass = row.session.isClass)
                        }) }
                        Text(
                            "${me.todayMinutes} min today · ${me.streak}-day streak",
                            style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = Spacing.sm),
                        )
                    }
                }
            }

            // Status sentence
            item {
                val (sentence, good) = state.status
                if (sentence.isNotBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = Spacing.listHorizontal)
                            .fillMaxWidth()
                            .clip(Shapes.card)
                            .background(animateColorAsState(if (good) AppTheme.colors.allowedBackground else AppTheme.colors.warn.copy(alpha = 0.12f), label = "statusBg").value)
                            .clickable(onClick = onOpenWeek)
                            .padding(horizontal = Spacing.md, vertical = 12.dp),
                    ) {
                        Text(sentence, style = MaterialTheme.typography.bodyMedium, color = if (good) AppTheme.colors.success else AppTheme.colors.warn, modifier = Modifier.weight(1f))
                        CupertinoDisclosure()
                    }
                }
            }

            // Partner card
            state.partner?.let { p ->
                item {
                    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), onClick = if (p.profile != null) onOpenPartner else null) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                            Avatar(name = p.profile?.name ?: "?", photoUrl = p.profile?.photoUrl, size = 40.dp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(p.profile?.name?.ifBlank { null } ?: "Partner", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
                                if (p.profile == null) {
                                    Text("Not joined yet", style = MaterialTheme.typography.titleMedium)
                                    Text("Share the invite code from US, then Settings", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                                } else if (p.isRestDay) {
                                    Text("Rest day", style = MaterialTheme.typography.titleMedium)
                                    Text("${p.todayMinutes} min today · ${p.streak}-day streak · ${p.localTime} there", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                                } else {
                                    val first = p.sessions.first()
                                    Text("${first.goal?.title ?: "Study"} · ${first.timeLabel}", style = MaterialTheme.typography.titleMedium)
                                    Text("${p.todayMinutes} min today · ${p.streak}-day streak · ${p.localTime} there", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                                }
                            }
                            if (p.profile != null && p.sessions.isNotEmpty()) {
                                val done = p.sessions.any { it.session.status == SessionStatus.DONE }
                                Icon(if (done) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked, contentDescription = null, tint = if (done) AppTheme.colors.success else AppTheme.colors.secondaryLabel)
                            }
                            if (p.profile != null) CupertinoDisclosure()
                        }
                        state.partnerLevel?.let { lvl ->
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = Spacing.sm)) {
                                Text("Level ${lvl.level.number} · ${lvl.level.name}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                                Text("${lvl.requiredDone} of ${lvl.requiredTotal}", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                            }
                            ProgressBar(fraction = if (lvl.requiredTotal == 0) 0f else lvl.requiredDone.toFloat() / lvl.requiredTotal, height = 4.dp, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            }

            // Next deadline
            state.nextDeadline?.let { d ->
                item {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = Spacing.listHorizontal + Spacing.sectionStart)) {
                        Text(d.title, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.weight(1f))
                        Text("in ${state.daysToDeadline ?: 0} days", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = AppTheme.colors.accent)
                    }
                }
            }
        }
        SnackbarHost(hostState = snackbar, modifier = Modifier.align(Alignment.BottomCenter))
    }

    logTarget?.let { target ->
        LogMinutesSheet(
            target = target, goals = state.goals, onDismiss = { logTarget = null },
            onLog = { g, c, m, n -> viewModel.logMinutes(g, c, m, n, target.sessionId, target.isClass); logTarget = null },
            onLogSplit = { g, map, n -> viewModel.logSplit(g, map, n, target.sessionId); logTarget = null },
        )
    }

    // After an update, What's new comes first; the daily brief follows once it is dismissed.
    val whatsNew = state.whatsNew
    val brief = state.brief
    if (whatsNew != null) {
        WhatsNewDialog(release = whatsNew, onDismiss = viewModel::dismissWhatsNew, onViewMore = { viewModel.dismissWhatsNew(); onOpenChangelog() })
    } else if (brief != null && !state.loading) {
        DailyBriefSheet(brief = brief, onStart = viewModel::dismissBrief, onDismiss = viewModel::dismissBrief)
    }
}

@Composable
private fun SessionBlock(
    row: SessionRow,
    first: Boolean,
    onOpenGoal: (String) -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onDone: () -> Unit,
    onSkip: () -> Unit,
    onReopen: () -> Unit,
    onLog: () -> Unit,
) {
    val done = row.session.status == SessionStatus.DONE
    val skipped = row.session.status == SessionStatus.SKIPPED
    val haptic = LocalHapticFeedback.current
    Column(modifier = Modifier.padding(top = if (first) 2.dp else Spacing.md).animateContentSize(spring(dampingRatio = 0.8f, stiffness = 400f))) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { row.goal?.let { onOpenGoal(it.id) } }) {
            Column(modifier = Modifier.weight(1f)) {
                Text(row.goal?.title ?: "Study", style = MaterialTheme.typography.headlineMedium)
                Text(
                    "${row.componentName} · ${row.session.durationMinutes} min · ${row.timeLabel}",
                    style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
                )
            }
            AnimatedSwap(row.session.status) { status ->
                when {
                    status == SessionStatus.DONE -> StatusChip("Done", AppTheme.colors.success)
                    status == SessionStatus.SKIPPED -> StatusChip("Skipped", AppTheme.colors.warn)
                    row.session.isClass -> StatusChip("Class", AppTheme.colors.accent)
                    else -> Box {}
                }
            }
        }
        if (!done && !skipped) {
            CupertinoPrimaryButton(
                text = if (row.session.isClass) "Done, log class" else "Done",
                onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); if (row.session.isClass) onLog() else onDone() },
                modifier = Modifier.padding(top = Spacing.sm),
            )
            row.nextTopic?.let { t ->
                CupertinoSecondaryButton(text = "Read: ${t.title} (${t.minutes} min)", onClick = { onOpenTopic(t.courseId, t.topicId) }, modifier = Modifier.padding(top = Spacing.sm))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.padding(top = Spacing.md)) {
                Chip(text = "Different time", onClick = onLog)
                Chip(text = "Skip today", onClick = onSkip)
            }
        } else if (skipped) {
            CupertinoTextButton(text = "Reopen", onClick = onReopen)
        } else {
            CupertinoTextButton(text = "Add more time", onClick = onLog)
        }
    }
}
