package com.shahaabapps.twotracks.feature.levels

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Verified
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.core.planner.LevelState
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoDisclosure
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoScaffold
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.IosColors
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.PersonPlan
import com.shahaabapps.twotracks.session.PlanSnapshot
import com.shahaabapps.twotracks.session.PlanStore
import com.shahaabapps.twotracks.ui.Routes
import com.shahaabapps.twotracks.ui.components.PopIn
import com.shahaabapps.twotracks.ui.components.SectionLabel
import com.shahaabapps.twotracks.ui.components.pulse
import com.shahaabapps.twotracks.ui.components.SegmentedChoice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class LevelsUiState(val plan: PlanSnapshot? = null, val showPartner: Boolean = false) {
    val person: PersonPlan? get() = plan?.let { if (showPartner) it.partner else it.me }
}

@HiltViewModel
class LevelsViewModel @Inject constructor(planStore: PlanStore) : ViewModel() {
    private val showPartner = MutableStateFlow(false)
    val uiState: StateFlow<LevelsUiState> = combine(planStore.plan, showPartner) { p, s -> LevelsUiState(p, s) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LevelsUiState())

    fun toggle() { showPartner.value = !showPartner.value }
}

/** Tab 3: both ladders, then the reference material from the guide under them. */
@Composable
fun LevelsScreen(
    onOpenLevel: (String) -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: LevelsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val plan = state.plan
    CupertinoScaffold(title = "Levels") {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            if (plan != null) {
                item {
                    SegmentedChoice(
                        options = listOf(false, true), selected = state.showPartner,
                        label = { if (it) plan.partner.name else plan.me.name }, onSelect = { if (it != state.showPartner) viewModel.toggle() },
                        modifier = Modifier.padding(horizontal = Spacing.listHorizontal),
                    )
                }
                val person = state.person!!
                item {
                    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        person.levels.forEachIndexed { i, lp -> LadderRow(lp, last = i == person.levels.lastIndex, onClick = { onOpenLevel(lp.level.id) }) }
                    }
                }
            }
            item { SectionLabel("From the guide") }
            item {
                CupertinoListSection {
                    CupertinoListRow("Countries", subtitle = "Six tracks, who leads, what to do", leadingIcon = Icons.Filled.Public, iconColor = IosColors.Cyan, onClick = { onNavigate(Routes.COUNTRIES) }, trailing = { CupertinoDisclosure() })
                    CupertinoListRow("Tests and scores", subtitle = "Official results against targets", leadingIcon = Icons.Filled.Verified, iconColor = IosColors.Green, onClick = { onNavigate(Routes.TESTS) }, trailing = { CupertinoDisclosure() })
                    CupertinoListRow("Documents", subtitle = "Checklist per person", leadingIcon = Icons.Filled.Description, iconColor = IosColors.Orange, onClick = { onNavigate(Routes.DOCUMENTS) }, trailing = { CupertinoDisclosure() })
                    CupertinoListRow("Budget", subtitle = "Planned versus spent", leadingIcon = Icons.Filled.AccountBalanceWallet, iconColor = IosColors.Pink, showDivider = false, onClick = { onNavigate(Routes.BUDGET) }, trailing = { CupertinoDisclosure() })
                }
            }
            item {
                Text(
                    "Rules in the guide were checked on 28 September 2026. Re-check official sites before paying fees or resigning.",
                    style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                    modifier = Modifier.padding(horizontal = Spacing.listHorizontal + Spacing.sectionStart),
                )
            }
        }
    }
}

@Composable
private fun LadderRow(lp: LevelProgress, last: Boolean, onClick: () -> Unit) {
    val color = when (lp.state) {
        LevelState.DONE -> AppTheme.colors.success
        LevelState.OPEN -> if (lp.isOverdue) AppTheme.colors.warn else AppTheme.colors.accent
        LevelState.LOCKED -> AppTheme.colors.secondaryLabel
    }
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), verticalAlignment = Alignment.Top) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(36.dp)) {
            Box(
                modifier = Modifier.pulse(enabled = lp.state == LevelState.OPEN && !lp.isOverdue).size(30.dp).clip(CircleShape)
                    .background(animateColorAsState(if (lp.state == LevelState.LOCKED) AppTheme.colors.fill else color, label = "node").value),
                contentAlignment = Alignment.Center,
            ) {
                when (lp.state) {
                    LevelState.DONE -> PopIn { Icon(Icons.Filled.Check, contentDescription = null, tint = AppTheme.colors.onSolid, modifier = Modifier.size(18.dp)) }
                    LevelState.LOCKED -> Icon(Icons.Filled.Lock, contentDescription = null, tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(15.dp))
                    LevelState.OPEN -> Text("${lp.level.number}", color = AppTheme.colors.onSolid, fontWeight = FontWeight.Bold)
                }
            }
            if (!last) Box(modifier = Modifier.width(2.dp).height(44.dp).background(AppTheme.colors.separator))
        }
        Column(modifier = Modifier.weight(1f).padding(start = 10.dp, bottom = if (last) 0.dp else 12.dp)) {
            Text(
                "Level ${lp.level.number} · ${lp.level.name}",
                style = MaterialTheme.typography.titleMedium,
                color = if (lp.state == LevelState.LOCKED) AppTheme.colors.secondaryLabel else MaterialTheme.colorScheme.onSurface,
            )
            Text(
                when (lp.state) {
                    LevelState.DONE -> "Done"
                    LevelState.OPEN -> "${lp.requiredDone} of ${lp.requiredTotal} done · " + (lp.daysLeft?.let { if (it >= 0) "$it days left" else "${-it} days overdue" } ?: "")
                    LevelState.LOCKED -> "Starts ${Clock.formatDate(lp.level.startDateIso)}"
                },
                style = MaterialTheme.typography.bodySmall, color = color,
            )
        }
        CupertinoDisclosure()
    }
}
