package com.shahaabapps.twotracks.feature.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoScaffold
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.feature.study.FeasibilityChip
import com.shahaabapps.twotracks.feature.study.PaceLine
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.components.ProgressBar
import com.shahaabapps.twotracks.ui.components.SectionLabel
import com.shahaabapps.twotracks.ui.components.SegmentedChoice
import com.shahaabapps.twotracks.ui.components.StatTile

@Composable
fun ProgressScreen(viewModel: ProgressViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val person = if (state.showPartner) state.partner else state.me

    CupertinoScaffold(title = "Progress") {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                SegmentedChoice(
                    options = listOf(false, true), selected = state.showPartner,
                    label = { if (it) state.partner?.name ?: "Partner" else "Me" },
                    onSelect = { if (it != state.showPartner) viewModel.toggle() },
                    modifier = Modifier.padding(horizontal = Spacing.listHorizontal),
                )
            }
            if (person != null) {
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        StatTile("Total studied", "%.0f h".format(person.totalHours), Modifier.weight(1f))
                        StatTile("Active budget", "%.0f h".format(person.requiredHours), Modifier.weight(1f))
                        StatTile("Streak", "${person.streak}d", Modifier.weight(1f), valueColor = AppTheme.colors.accent)
                    }
                }
                item { SectionLabel("Hours per week, last 8 weeks") }
                item {
                    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        val max = (person.weeks.maxOfOrNull { it.total } ?: 0).coerceAtLeast(60)
                        Row(modifier = Modifier.fillMaxWidth().height(120.dp), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.Bottom) {
                            person.weeks.forEach { w ->
                                Column(modifier = Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("%.1f".format(w.total / 60.0), style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(fraction = (w.total.toFloat() / max).coerceIn(0.03f, 0.85f))
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (w.total > 0) AppTheme.colors.accent else MaterialTheme.colorScheme.surfaceVariant),
                                    )
                                }
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth().padding(top = Spacing.xs)) {
                            Text(Clock.formatDayMonth(person.weeks.first().weekStart), style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.weight(1f))
                            Text("this week", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
                        }
                        Text("Longest streak ${person.longestStreak} days", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = Spacing.sm))
                    }
                }
                item { SectionLabel("Goals") }
                item {
                    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        if (person.goals.isEmpty()) Text("No active goals.", color = AppTheme.colors.secondaryLabel)
                        person.goals.forEachIndexed { i, p ->
                            Column(modifier = Modifier.padding(top = if (i == 0) 0.dp else Spacing.md)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(p.goal.title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                                    FeasibilityChip(p)
                                }
                                ProgressBar(fraction = p.fraction.toFloat(), modifier = Modifier.padding(vertical = Spacing.xs), height = 8.dp)
                                Text("%.1f / %.0f h".format(p.hoursLogged, p.hoursRequired), style = MaterialTheme.typography.bodySmall)
                                PaceLine(p)
                            }
                        }
                    }
                }
                if (person.habits.isNotEmpty()) {
                    item { SectionLabel("Counts") }
                    item {
                        CupertinoListSection {
                            person.habits.forEachIndexed { i, h ->
                                CupertinoListRow(
                                    title = h.habit.name,
                                    subtitle = listOfNotNull(
                                        h.habit.weeklyTarget?.let { "${h.thisWeek} / $it this week" },
                                        h.habit.totalTarget?.let { "${h.total} / $it total" } ?: "${h.total} total",
                                    ).joinToString(", "),
                                    showDivider = i < person.habits.lastIndex,
                                )
                            }
                        }
                    }
                }
            }
            if (state.deadlines.isNotEmpty()) {
                item { SectionLabel("Countdowns") }
                item {
                    CupertinoListSection {
                        state.deadlines.forEachIndexed { i, d ->
                            CupertinoListRow(
                                title = d.deadline.title,
                                subtitle = Clock.formatDate(d.deadline.dateIso) + if (d.deadline.detail.isNotBlank()) ". ${d.deadline.detail}" else "",
                                showDivider = i < state.deadlines.lastIndex,
                                trailing = {
                                    Text(
                                        if (d.days >= 0) "${d.days} d" else "${-d.days} d ago",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (d.days < 30) AppTheme.colors.warn else AppTheme.colors.accent,
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
