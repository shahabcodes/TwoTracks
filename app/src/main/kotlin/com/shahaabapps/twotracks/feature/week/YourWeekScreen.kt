package com.shahaabapps.twotracks.feature.week

import com.shahaabapps.twotracks.ui.components.StatTile
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.Check
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.ui.components.CupertinoDisclosure
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.PersonPlan
import com.shahaabapps.twotracks.session.PlanStore
import com.shahaabapps.twotracks.ui.Routes
import com.shahaabapps.twotracks.ui.components.DetailScaffold
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
 * Level 2 behind the status sentence: in plain words, the time you have against
 * the time your open goals need, and the three levers.
 */
@Composable
fun YourWeekScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    onOpenGoal: (String) -> Unit,
    viewModel: YourWeekViewModel = hiltViewModel(),
) {
    val me by viewModel.me.collectAsStateWithLifecycle()
    DetailScaffold(title = "Will it fit", onBack = onBack, eyebrow = "The time you have against what it needs") {
        val p = me ?: return@DetailScaffold
        val cap = p.capacity
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(bottom = Spacing.xl), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                StatTile(label = "of study slots a week", value = "%.1f h".format(cap.weeklyCapacityHours), modifier = Modifier.weight(1f))
                StatTile(label = "your open goals need", value = "%.1f h".format(cap.weeklyNeedHours), modifier = Modifier.weight(1f))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = Spacing.listHorizontal),
            ) {
                val tone = if (cap.fits) AppTheme.colors.success else AppTheme.colors.warn
                Icon(if (cap.fits) Icons.Filled.Check else Icons.Filled.WarningAmber, contentDescription = null, tint = tone, modifier = Modifier.size(18.dp))
                Text(
                    if (cap.fits) "It fits. Keep the slots and you will make the dates."
                    else "That is %.1f hours a week short. Pick one of the options below.".format(cap.gapHours),
                    style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = tone,
                )
            }

            CupertinoListSection(header = "What each goal needs") {
                val active = p.activeGoals
                active.forEachIndexed { i, g ->
                    val gp = p.goalProgress[g.id]
                    CupertinoListRow(
                        title = g.title,
                        subtitle = gp?.let { prog ->
                            listOfNotNull(
                                prog.weeklyNeedHours?.let { "%.1f h a week".format(it) },
                                prog.targetDate?.let { "by ${Clock.formatDate(it)}" },
                                "%.0f of %.0f h done".format(prog.hoursLogged, prog.hoursRequired),
                            ).joinToString(" · ")
                        },
                        showDivider = i < active.lastIndex, onClick = { onOpenGoal(g.id) }, trailing = { CupertinoDisclosure() },
                    )
                }
                if (active.isEmpty()) CupertinoListRow("No open goals", showDivider = false)
            }

            CupertinoListSection(header = "Options") {
                CupertinoListRow("Add a weekend slot", subtitle = "Two 90-minute blocks on Friday and Saturday add 3 hours a week", leadingIcon = Icons.Filled.EventRepeat, onClick = { onNavigate(Routes.AVAILABILITY) }, trailing = { CupertinoDisclosure() })
                CupertinoListRow("Move a date", subtitle = "Push a goal's complete-by date and the need per week drops", leadingIcon = Icons.Filled.Edit, onClick = { p.activeGoals.firstOrNull()?.let { onOpenGoal(it.id) } }, trailing = { CupertinoDisclosure() })
                CupertinoListRow("See the week's plan", subtitle = "Which goal goes in which slot", leadingIcon = Icons.Filled.CalendarMonth, showDivider = false, onClick = { onNavigate(Routes.WEEK) }, trailing = { CupertinoDisclosure() })
            }
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.listHorizontal + Spacing.sectionStart)) {
                Text("Pausing a goal: mark it passed from its page if you are done with it, or move it to a later level.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
            }
        }
    }
}
