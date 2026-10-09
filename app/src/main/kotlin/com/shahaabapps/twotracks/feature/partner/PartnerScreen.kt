package com.shahaabapps.twotracks.feature.partner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.planner.LevelState
import com.shahaabapps.core.ui.components.Avatar
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoDisclosure
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.StatusChip
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Shapes
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.reminders.SessionPlanning
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.PersonPlan
import com.shahaabapps.twotracks.session.PlanSnapshot
import com.shahaabapps.twotracks.session.PlanStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.ProgressBar
import com.shahaabapps.twotracks.ui.components.SectionLabel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class PartnerUiState(
    val plan: PlanSnapshot? = null,
    val partner: PersonPlan? = null,
    val todaySessions: List<PlannedSession> = emptyList(),
    val localTime: String = "",
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PartnerViewModel @Inject constructor(
    sessionStore: SessionStore,
    planStore: PlanStore,
    sessionPlanning: SessionPlanning,
) : ViewModel() {
    private val sessions = sessionStore.session.flatMapLatest { s ->
        if (s == null) flowOf(emptyList()) else {
            val today = Clock.today(s.partner ?: s.me)
            sessionPlanning.sessionsBetween(today.minusDays(1), today.plusDays(1))
        }
    }

    val uiState: StateFlow<PartnerUiState> = combine(planStore.plan, sessions) { plan, sessions ->
        if (plan == null) return@combine PartnerUiState()
        val p = plan.partner
        val profile = plan.session.partner
        val todayIso = Dates.format(p.today)
        PartnerUiState(
            plan = plan, partner = p,
            todaySessions = sessions.filter { it.owner == p.role && it.dateIso == todayIso }.sortedBy { it.startMinutes },
            localTime = Clock.formatTime(Clock.now(profile ?: plan.session.me)),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PartnerUiState())
}

/**
 * Everything about your partner, read-only: today's plan, their level line,
 * their pace sentence, open goals with bars, streaks and counts, and their
 * whole ladder. Every row drills into the same pages you use, in view mode.
 */
@Composable
fun PartnerScreen(
    onBack: () -> Unit,
    onOpenLevel: (String) -> Unit,
    onOpenGoal: (String) -> Unit,
    viewModel: PartnerViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val p = state.partner
    val plan = state.plan
    DetailScaffold(title = p?.name ?: "Partner", onBack = onBack) {
        if (p == null || plan == null) return@DetailScaffold
        val profile = plan.session.partner
        val goalsById = p.goals.associateBy { it.id }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                        Avatar(name = p.name, photoUrl = profile?.photoUrl, size = 48.dp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(p.name, style = MaterialTheme.typography.headlineMedium)
                            Text(
                                if (profile == null) "Not joined yet" else "${state.localTime} there · ${p.streak}-day streak · longest ${p.longestStreak}",
                                style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                            )
                        }
                    }
                    Text(
                        "You can see everything here. Only ${p.name} can change it.",
                        style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = Spacing.sm),
                    )
                }
            }

            p.current?.let { lvl ->
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = Spacing.listHorizontal)
                            .fillMaxWidth()
                            .clip(Shapes.card)
                            .background(AppTheme.colors.accent.copy(alpha = 0.12f))
                            .clickable { onOpenLevel(lvl.level.id) }
                            .padding(horizontal = Spacing.md, vertical = 12.dp),
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Level ${lvl.level.number} · ${lvl.level.name}", style = MaterialTheme.typography.titleMedium, color = AppTheme.colors.accent)
                            Text(
                                "${lvl.requiredDone} of ${lvl.requiredTotal} done · " + (lvl.daysLeft?.let { if (it >= 0) "$it days left" else "${-it} days overdue" } ?: ""),
                                style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                            )
                            ProgressBar(fraction = if (lvl.requiredTotal == 0) 0f else lvl.requiredDone.toFloat() / lvl.requiredTotal, height = 5.dp, modifier = Modifier.padding(top = 6.dp))
                        }
                        CupertinoDisclosure()
                    }
                }
            }

            item {
                val (sentence, good) = p.statusSentence
                Text(
                    sentence, style = MaterialTheme.typography.bodyMedium, color = if (good) AppTheme.colors.success else AppTheme.colors.warn,
                    modifier = Modifier.padding(horizontal = Spacing.listHorizontal + Spacing.sectionStart),
                )
            }

            item { SectionLabel("Today") }
            item {
                CupertinoListSection {
                    if (state.todaySessions.isEmpty()) {
                        CupertinoListRow("Rest day", subtitle = "${p.todayMinutes} min logged today", showDivider = false)
                    }
                    state.todaySessions.forEachIndexed { i, s ->
                        val g = goalsById[s.goalId]
                        val done = s.status == SessionStatus.DONE
                        CupertinoListRow(
                            title = g?.title ?: "Study",
                            subtitle = "${g?.components?.firstOrNull { it.id == s.componentId }?.name.orEmpty()} · ${Dates.minutesToClock(s.startMinutes)} to ${Dates.minutesToClock(s.endMinutes)}" + if (s.isClass) " · class" else "",
                            showDivider = i < state.todaySessions.lastIndex,
                            onClick = g?.let { { onOpenGoal(it.id) } },
                            trailing = {
                                Icon(
                                    if (done) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked, contentDescription = null,
                                    tint = if (done) AppTheme.colors.success else AppTheme.colors.secondaryLabel,
                                )
                            },
                        )
                    }
                }
            }

            item { SectionLabel("Open goals") }
            item {
                CupertinoListSection {
                    val active = p.activeGoals
                    if (active.isEmpty()) CupertinoListRow("No open goals", showDivider = false)
                    active.forEachIndexed { i, g ->
                        val gp = p.goalProgress[g.id]
                        // Hours only where a course records them; the rest of
                        // the roadmap is measured by its steps and would show
                        // a bar that can never move.
                        val counted = Courses.forGoal(g.id) != null
                        CupertinoListRow(
                            title = g.title,
                            subtitle = gp?.let {
                                val due = it.targetDate?.let { d -> "by ${Clock.formatDate(d)}" }
                                if (counted) "%.0f of %.0f h".format(it.hoursLogged, it.hoursRequired) + (due?.let { d -> " · $d" } ?: "")
                                else due
                            },
                            showDivider = i < active.lastIndex,
                            onClick = { onOpenGoal(g.id) },
                            trailing = { CupertinoDisclosure() },
                        )
                        if (counted) {
                            gp?.let { ProgressBar(fraction = it.fraction.toFloat(), height = 4.dp, modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = if (i < active.lastIndex) 8.dp else 12.dp)) }
                        }
                    }
                }
            }

            if (p.weekCounts.isNotEmpty()) {
                item { SectionLabel("This week") }
                item {
                    CupertinoListSection {
                        val entries = p.weekCounts.entries.toList()
                        entries.forEachIndexed { i, (habitId, n) ->
                            CupertinoListRow(habitId.replace('-', ' ').replaceFirstChar { it.uppercase() }, showDivider = i < entries.lastIndex, trailing = { Text("$n", style = MaterialTheme.typography.titleMedium) })
                        }
                    }
                }
            }

            item { SectionLabel("All levels") }
            item {
                CupertinoListSection {
                    p.levels.forEachIndexed { i, lp ->
                        CupertinoListRow(
                            title = "Level ${lp.level.number} · ${lp.level.name}",
                            subtitle = when (lp.state) {
                                LevelState.DONE -> "Done"
                                LevelState.OPEN -> "${lp.requiredDone} of ${lp.requiredTotal} done · complete by ${Clock.formatDate(lp.level.dueDateIso)}"
                                LevelState.LOCKED -> "Starts ${Clock.formatDate(lp.level.startDateIso)}"
                            },
                            showDivider = i < p.levels.lastIndex,
                            onClick = { onOpenLevel(lp.level.id) },
                            trailing = {
                                when (lp.state) {
                                    LevelState.DONE -> StatusChip("Done", AppTheme.colors.success)
                                    LevelState.OPEN -> StatusChip(if (lp.isOverdue) "Overdue" else "Open", if (lp.isOverdue) AppTheme.colors.warn else AppTheme.colors.accent)
                                    LevelState.LOCKED -> Icon(Icons.Filled.Lock, contentDescription = null, tint = AppTheme.colors.secondaryLabel)
                                }
                                CupertinoDisclosure()
                            },
                        )
                    }
                }
            }
        }
    }
}
