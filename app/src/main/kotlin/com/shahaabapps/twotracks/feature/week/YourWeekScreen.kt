package com.shahaabapps.twotracks.feature.week

import androidx.compose.ui.unit.em
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import com.shahaabapps.twotracks.ui.board.BoardTitle
import com.shahaabapps.twotracks.ui.board.BoardStat
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardBack
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.PersonPlan
import com.shahaabapps.twotracks.session.PlanStore
import com.shahaabapps.twotracks.ui.Routes
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.StatTile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class YourWeekViewModel @Inject constructor(planStore: PlanStore) : ViewModel() {
    val me: StateFlow<PersonPlan?> = planStore.plan.map { it?.me }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}

/**
 * Behind the status sentence: in plain words, the time you have against the
 * time your open goals need, then each goal's share and the levers. Drawn in
 * the same ruled rows as the rest of the Parallel screens; the prototype has
 * no page of its own for this.
 */
@Composable
fun YourWeekScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    onOpenGoal: (String) -> Unit,
    viewModel: YourWeekViewModel = hiltViewModel(),
) {
    val me by viewModel.me.collectAsStateWithLifecycle()
    val board = AppTheme.departures
    val body: @Composable () -> Unit = body@{
        val p = me ?: return@body
        val cap = p.capacity
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(bottom = 40.dp)) {
            if (board) {
                // No prototype has this page; on the board it takes the board's header and figures.
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp, bottom = 14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(Modifier.height(44.dp), contentAlignment = Alignment.CenterStart) { BoardBack(onBack, "Back to the timetable") }
                    BoardMono("THE TIME YOU HAVE AGAINST WHAT IT NEEDS")
                    BoardTitle("Will it fit", size = 52.sp)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(horizontal = BoardGutter).height(IntrinsicSize.Min)) {
                    BoardStat("%.1fh".format(cap.weeklyCapacityHours), "Study slots a week", modifier = Modifier.weight(1f).fillMaxHeight())
                    BoardStat("%.1fh".format(cap.weeklyNeedHours), "Your goals need", color = if (cap.fits) AppTheme.colors.ink else AppTheme.colors.warn, modifier = Modifier.weight(1f).fillMaxHeight())
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                    StatTile(label = "of study slots a week", value = "%.1f h".format(cap.weeklyCapacityHours), modifier = Modifier.weight(1f))
                    StatTile(label = "your open goals need", value = "%.1f h".format(cap.weeklyNeedHours), modifier = Modifier.weight(1f))
                }
            }
            Row(
                verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 12.dp),
            ) {
                val tone = if (cap.fits) AppTheme.colors.success else AppTheme.colors.warn
                Icon(if (cap.fits) Icons.Filled.Check else Icons.Filled.Info, contentDescription = null, tint = tone, modifier = Modifier.padding(top = 2.dp).size(16.dp))
                Text(
                    if (cap.fits) "It fits. Keep the slots and you will make the dates."
                    else "That is %.1f hours a week short. Pick one of the options below.".format(cap.gapHours),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), fontWeight = FontWeight.SemiBold, color = tone,
                )
            }

            Section("What each goal needs") {
                val active = p.activeGoals
                if (active.isEmpty()) Line("No open goals", null, null)
                active.forEach { g ->
                    val gp = p.goalProgress[g.id]
                    Line(
                        g.title,
                        gp?.let { prog ->
                            listOfNotNull(
                                prog.weeklyNeedHours?.let { "%.1f h a week".format(it) },
                                prog.targetDate?.let { "by ${Clock.formatDate(it)}" },
                                "%.0f of %.0f h done".format(prog.hoursLogged, prog.hoursRequired),
                            ).joinToString(" · ")
                        },
                    ) { onOpenGoal(g.id) }
                }
            }
            Section("Options") {
                Line("Add a weekend slot", "Two 90-minute blocks on Friday and Saturday add 3 hours a week") { onNavigate(Routes.AVAILABILITY) }
                Line("Move a date", "Push a goal's complete-by date and the need per week drops") { p.activeGoals.firstOrNull()?.let { onOpenGoal(it.id) } }
                Line("See the week's plan", "Which goal goes in which slot") { onNavigate(Routes.WEEK) }
            }
            Text(
                "Pausing a goal: mark it passed from its page if you are done with it, or move it to a later level.",
                style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 12.dp),
            )
        }
    }
    if (board) Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner -> Box(Modifier.padding(inner)) { body() } }
    else DetailScaffold(title = "Will it fit", onBack = onBack, eyebrow = "The time you have against what it needs") { body() }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 22.dp)) {
        if (AppTheme.departures) {
            BoardLabel(title, size = 13.sp, spacing = 0.12.em, modifier = Modifier.padding(bottom = 8.dp))
            HorizontalDivider(color = AppTheme.colors.separator, thickness = 1.dp)
        } else {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))
            HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
        }
        content()
    }
}

@Composable
private fun Line(title: String, subtitle: String?, onClick: (() -> Unit)?) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier),
        ) {
            Column(modifier = Modifier.weight(1f).padding(vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                subtitle?.let { Text(it, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel) }
            }
            if (onClick != null) Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(16.dp))
        }
        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
    }
}
