package com.shahaabapps.twotracks.feature.study

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.Feasibility
import com.shahaabapps.core.planner.GoalProgress
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoScaffold
import com.shahaabapps.core.ui.components.CupertinoSwitch
import com.shahaabapps.core.ui.components.StatusChip
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.ProgressBar
import com.shahaabapps.twotracks.ui.components.SectionLabel
import com.shahaabapps.twotracks.ui.components.SegmentedChoice

@Composable
fun StudyScreen(
    onOpenGoal: (String) -> Unit,
    onNewGoal: () -> Unit,
    viewModel: StudyViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CupertinoScaffold(title = "Study") {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                Row(
                    modifier = Modifier.padding(horizontal = Spacing.listHorizontal),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                ) {
                    SegmentedChoice(
                        options = listOf(true, false), selected = state.isMe,
                        label = { if (it) "Me" else "Partner" }, onSelect = { if (it != state.isMe) viewModel.togglePerson() },
                        modifier = Modifier.weight(1f),
                    )
                    if (state.isMe) Chip(text = "+ Goal", onClick = onNewGoal)
                }
            }
            state.capacity?.let { cap ->
                item {
                    Text(
                        text = "Active goals need %.1f h/week; your template gives %.1f h.".format(cap.weeklyNeedHours, cap.weeklyCapacityHours),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (cap.fits) AppTheme.colors.secondaryLabel else AppTheme.colors.warn,
                        modifier = Modifier.padding(horizontal = Spacing.listHorizontal),
                    )
                }
            }
            section("Active", state.active, state.isMe, onOpenGoal, viewModel::setActive)
            section("Not started (switch on to schedule)", state.inactive, state.isMe, onOpenGoal, viewModel::setActive)
            section("Completed", state.done, state.isMe, onOpenGoal, viewModel::setActive)
        }
    }
}

private fun androidx.compose.foundation.lazy.LazyListScope.section(
    title: String,
    cards: List<GoalCard>,
    editable: Boolean,
    onOpenGoal: (String) -> Unit,
    onSetActive: (StudyGoal, Boolean) -> Unit,
) {
    if (cards.isEmpty()) return
    item { SectionLabel(title) }
    items(cards, key = { it.goal.id }) { card ->
        GoalCardView(card, editable, onOpenGoal, onSetActive)
    }
}

@Composable
private fun GoalCardView(card: GoalCard, editable: Boolean, onOpenGoal: (String) -> Unit, onSetActive: (StudyGoal, Boolean) -> Unit) {
    val p = card.progress
    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), onClick = { onOpenGoal(card.goal.id) }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(card.goal.title, style = MaterialTheme.typography.titleMedium)
                Text(card.goal.subtitle, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
            }
            if (editable && !p.isComplete) {
                CupertinoSwitch(checked = card.goal.active, onCheckedChange = { onSetActive(card.goal, it) })
            }
        }
        ProgressBar(fraction = p.fraction.toFloat(), modifier = Modifier.padding(top = Spacing.sm, bottom = Spacing.xs))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.fillMaxWidth()) {
            Text(
                "%.1f / %.0f h".format(p.hoursLogged, p.hoursRequired),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
            )
            FeasibilityChip(p)
        }
        PaceLine(p)
    }
}

@Composable
fun FeasibilityChip(p: GoalProgress) {
    val (label, color) = feasibilityLabel(p.feasibility)
    if (label != null) StatusChip(label, color)
}

@Composable
fun feasibilityLabel(f: Feasibility): Pair<String?, Color> = when (f) {
    Feasibility.FITS -> "On track" to AppTheme.colors.success
    Feasibility.TIGHT -> "Tight" to AppTheme.colors.warn
    Feasibility.DOES_NOT_FIT -> "Behind" to AppTheme.colors.destructive
    Feasibility.DONE -> "Done" to AppTheme.colors.success
    Feasibility.NO_TARGET -> null to AppTheme.colors.secondaryLabel
}

@Composable
fun PaceLine(p: GoalProgress) {
    val parts = buildList {
        p.weeklyNeedHours?.let { if (it > 0) add("need %.1f h/wk".format(it)) }
        if (p.recentWeeklyHours > 0) add("doing %.1f h/wk".format(p.recentWeeklyHours))
        p.targetDate?.let { add("by ${Clock.formatDate(it)}") }
        p.forecastDate?.let { if (!p.isComplete) add("forecast ${Clock.formatDate(it)}") }
    }
    if (parts.isNotEmpty()) {
        Text(parts.joinToString(", "), style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
    }
}
