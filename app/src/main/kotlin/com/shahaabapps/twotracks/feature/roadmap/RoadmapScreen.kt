package com.shahaabapps.twotracks.feature.roadmap

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.data.tracking.TrackingRepository
import com.shahaabapps.core.model.Milestone
import com.shahaabapps.core.model.MilestoneStatus
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.StatusChip
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.SegmentedChoice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoadmapUiState(
    val quarters: List<Pair<String, List<Milestone>>> = emptyList(),
    val filter: PersonRole? = null,
    val myRole: PersonRole = PersonRole.TECH,
    val names: Map<PersonRole, String> = emptyMap(),
)

@HiltViewModel
class RoadmapViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    dataStore: CoupleDataStore,
    private val trackingRepository: TrackingRepository,
) : ViewModel() {
    private val filter = MutableStateFlow<PersonRole?>(null)

    val uiState: StateFlow<RoadmapUiState> = combine(sessionStore.session, dataStore.milestones, filter) { session, milestones, f ->
        val visible = milestones.filter { f == null || it.owner == f }
        RoadmapUiState(
            quarters = visible.groupBy { it.quarterOrder to it.quarter }.toSortedMap(compareBy { it.first }).map { (k, v) -> k.second to v.sortedBy { it.id } },
            filter = f, myRole = session?.myRole ?: PersonRole.TECH,
            names = PersonRole.entries.associateWith { session?.nameFor(it) ?: it.name },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RoadmapUiState())

    fun setFilter(role: PersonRole?) { filter.value = role }

    fun cycleStatus(m: Milestone) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        val next = when (m.status) {
            MilestoneStatus.TODO -> MilestoneStatus.IN_PROGRESS
            MilestoneStatus.IN_PROGRESS -> MilestoneStatus.DONE
            MilestoneStatus.DONE -> MilestoneStatus.BLOCKED
            MilestoneStatus.BLOCKED -> MilestoneStatus.TODO
        }
        trackingRepository.saveMilestone(s.coupleId, m.copy(status = next))
    }
}

@Composable
fun RoadmapScreen(onBack: () -> Unit, viewModel: RoadmapViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DetailScaffold(title = "Roadmap", onBack = onBack) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.lg), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                SegmentedChoice(
                    options = listOf<PersonRole?>(null, PersonRole.TECH, PersonRole.TEACHER), selected = state.filter,
                    label = { it?.let { r -> state.names[r] } ?: "Both" }, onSelect = viewModel::setFilter,
                    modifier = Modifier.padding(horizontal = Spacing.listHorizontal),
                )
            }
            item {
                Text(
                    "Tap a milestone to move it: to do, in progress, done, blocked.",
                    style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                    modifier = Modifier.padding(horizontal = Spacing.listHorizontal),
                )
            }
            state.quarters.forEach { (quarter, items) ->
                item(key = quarter) {
                    CupertinoListSection(header = quarter) {
                        items.forEachIndexed { i, m ->
                            val (icon, color) = when (m.status) {
                                MilestoneStatus.TODO -> Icons.Filled.RadioButtonUnchecked to AppTheme.colors.secondaryLabel
                                MilestoneStatus.IN_PROGRESS -> Icons.Filled.Timelapse to AppTheme.colors.warn
                                MilestoneStatus.DONE -> Icons.Filled.CheckCircle to AppTheme.colors.success
                                MilestoneStatus.BLOCKED -> Icons.Filled.Block to AppTheme.colors.destructive
                            }
                            CupertinoListRow(
                                title = m.title,
                                subtitle = listOfNotNull(state.names[m.owner], m.countryCode, m.dueDateIso).joinToString("  "),
                                showDivider = i < items.lastIndex,
                                onClick = { viewModel.cycleStatus(m) },
                                trailing = {
                                    if (m.isCheckpoint) StatusChip("Checkpoint", AppTheme.colors.warn)
                                    Icon(icon, contentDescription = m.status.name, tint = color, modifier = Modifier.padding(start = Spacing.sm))
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
