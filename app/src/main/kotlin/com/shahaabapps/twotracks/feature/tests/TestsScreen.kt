package com.shahaabapps.twotracks.feature.tests

import com.shahaabapps.twotracks.session.Celebrations
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.data.tracking.TrackingRepository
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.TestRecord
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.components.StatusChip
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.SectionLabel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TestsUiState(
    val byRole: List<Pair<String, List<TestRecord>>> = emptyList(),
    val myRole: PersonRole = PersonRole.TECH,
)

@HiltViewModel
class TestsViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    dataStore: CoupleDataStore,
    private val trackingRepository: TrackingRepository,
    private val celebrations: Celebrations,
) : ViewModel() {
    val uiState: StateFlow<TestsUiState> = combine(sessionStore.session, dataStore.tests) { session, tests ->
        val roles = listOfNotNull(session?.myRole, session?.partnerRole).ifEmpty { PersonRole.entries }
        TestsUiState(
            byRole = roles.map { r -> (session?.nameFor(r) ?: r.name) to tests.filter { it.owner == r }.sortedBy { it.order } },
            myRole = session?.myRole ?: PersonRole.TECH,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TestsUiState())

    fun save(test: TestRecord) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        trackingRepository.saveTest(s.coupleId, test)
        celebrations.saved("Result saved")
    }
}

@Composable
fun TestsScreen(onBack: () -> Unit, viewModel: TestsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var editing by remember { mutableStateOf<TestRecord?>(null) }

    DetailScaffold(title = "Tests and scores", onBack = onBack) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            state.byRole.forEach { (name, tests) ->
                item { SectionLabel(name) }
                items(tests, key = { it.id }) { t ->
                    val taken = t.actualBands.isNotEmpty()
                    val expired = Dates.parseOrNull(t.expiresOnIso)?.isBefore(java.time.LocalDate.now()) == true
                    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), onClick = { editing = t }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(t.name, style = MaterialTheme.typography.titleMedium)
                                Text(t.purpose, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                            }
                            when {
                                expired -> StatusChip("Expired", AppTheme.colors.destructive)
                                taken -> StatusChip("Taken", AppTheme.colors.success)
                                else -> StatusChip("Planned", AppTheme.colors.secondaryLabel)
                            }
                        }
                        Row(modifier = Modifier.padding(top = Spacing.sm), horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                            t.targetBands.forEach { (k, target) ->
                                val actual = t.actualBands[k]
                                Column {
                                    Text(k, style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
                                    Text(
                                        actual?.let { "$it / $target" } ?: target,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = when {
                                            actual == null -> MaterialTheme.colorScheme.onSurface
                                            meets(actual, target) -> AppTheme.colors.success
                                            else -> AppTheme.colors.destructive
                                        },
                                    )
                                }
                            }
                        }
                        if (taken) {
                            Text(
                                "Taken ${Clock.formatDate(t.takenOnIso)}" + (t.expiresOnIso?.let { ", valid until ${Clock.formatDate(it)}" } ?: ""),
                                style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = Spacing.xs),
                            )
                        }
                    }
                }
            }
        }
    }

    editing?.let { t ->
        val actual = remember(t.id) { mutableStateOf(t.targetBands.keys.associateWith { t.actualBands[it].orEmpty() }) }
        var taken by remember(t.id) { mutableStateOf(t.takenOnIso.orEmpty()) }
        var expires by remember(t.id) { mutableStateOf(t.expiresOnIso.orEmpty()) }
        AlertDialog(
            onDismissRequest = { editing = null },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.save(
                        t.copy(
                            actualBands = actual.value.filterValues { it.isNotBlank() },
                            takenOnIso = taken.ifBlank { null }, expiresOnIso = expires.ifBlank { null },
                        ),
                    )
                    editing = null
                }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { editing = null }) { Text("Cancel") } },
            title = { Text(t.name) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    t.targetBands.forEach { (k, target) ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                            Text("$k (target $target)", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                            CupertinoTextField(value = actual.value[k].orEmpty(), onValueChange = { v -> actual.value = actual.value + (k to v) }, placeholder = "score", modifier = Modifier.width(90.dp))
                        }
                    }
                    CupertinoTextField(value = taken, onValueChange = { taken = it }, placeholder = "Taken on (yyyy-mm-dd)")
                    CupertinoTextField(value = expires, onValueChange = { expires = it }, placeholder = "Valid until (yyyy-mm-dd)")
                    Text("IELTS is valid 2 years (3 for Australian visas).", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.fillMaxWidth())
                }
            },
        )
    }
}

private fun meets(actual: String, target: String): Boolean {
    val a = actual.filter { it.isDigit() || it == '.' }.toDoubleOrNull()
    val t = target.filter { it.isDigit() || it == '.' }.toDoubleOrNull()
    return if (a != null && t != null) a >= t else actual.equals(target, ignoreCase = true)
}
