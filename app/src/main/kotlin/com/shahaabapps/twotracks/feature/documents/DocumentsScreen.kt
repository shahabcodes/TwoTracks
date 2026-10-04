package com.shahaabapps.twotracks.feature.documents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.data.tracking.TrackingRepository
import com.shahaabapps.core.model.DocStatus
import com.shahaabapps.core.model.DocumentItem
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.components.StatusChip
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.ProgressBar
import com.shahaabapps.twotracks.ui.components.SegmentedChoice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DocumentsUiState(
    val role: PersonRole = PersonRole.TECH,
    val names: Map<PersonRole, String> = emptyMap(),
    val groups: List<Pair<String, List<DocumentItem>>> = emptyList(),
    val done: Int = 0,
    val total: Int = 0,
)

@HiltViewModel
class DocumentsViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    dataStore: CoupleDataStore,
    private val trackingRepository: TrackingRepository,
) : ViewModel() {
    private val role = MutableStateFlow<PersonRole?>(null)

    val uiState: StateFlow<DocumentsUiState> = combine(sessionStore.session, dataStore.documents, role) { session, docs, r ->
        val viewing = r ?: session?.myRole ?: PersonRole.TECH
        val mine = docs.filter { it.owner == viewing }.sortedBy { it.order }
        DocumentsUiState(
            role = viewing,
            names = PersonRole.entries.associateWith { session?.nameFor(it) ?: it.name },
            groups = mine.groupBy { it.usedFor }.map { (k, v) -> k to v },
            done = mine.count { it.status == DocStatus.DONE }, total = mine.size,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DocumentsUiState())

    fun setRole(r: PersonRole) { role.value = r }

    fun save(doc: DocumentItem) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        trackingRepository.saveDocument(s.coupleId, doc)
    }

    fun cycle(doc: DocumentItem) = save(
        doc.copy(
            status = when (doc.status) {
                DocStatus.NOT_STARTED -> DocStatus.IN_PROGRESS
                DocStatus.IN_PROGRESS -> DocStatus.DONE
                DocStatus.DONE -> DocStatus.NOT_STARTED
            },
        ),
    )
}

@Composable
fun DocumentsScreen(onBack: () -> Unit, viewModel: DocumentsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var editing by remember { mutableStateOf<DocumentItem?>(null) }

    DetailScaffold(title = "Documents", onBack = onBack) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    SegmentedChoice(options = PersonRole.entries, selected = state.role, label = { state.names[it] ?: it.name }, onSelect = viewModel::setRole)
                    ProgressBar(fraction = if (state.total == 0) 0f else state.done.toFloat() / state.total)
                    Text("${state.done} of ${state.total} ready. Tap a row to advance it; long titles open the editor.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                }
            }
            state.groups.forEach { (usedFor, docs) ->
                item(key = usedFor) {
                    CupertinoListSection(header = usedFor) {
                        docs.forEachIndexed { i, d ->
                            val (label, color) = when (d.status) {
                                DocStatus.NOT_STARTED -> "Not started" to AppTheme.colors.secondaryLabel
                                DocStatus.IN_PROGRESS -> "In progress" to AppTheme.colors.warn
                                DocStatus.DONE -> "Done" to AppTheme.colors.success
                            }
                            CupertinoListRow(
                                title = d.name,
                                subtitle = listOfNotNull(d.detail.ifBlank { null }, d.note.ifBlank { null }, d.expiresOnIso?.let { "expires $it" }).joinToString(". "),
                                showDivider = i < docs.lastIndex,
                                onClick = { viewModel.cycle(d) },
                                trailing = {
                                    StatusChip(label, color, modifier = Modifier.padding(end = Spacing.xs))
                                    TextButton(onClick = { editing = d }) { Text("Edit") }
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    editing?.let { d ->
        var note by remember(d.id) { mutableStateOf(d.note) }
        var expires by remember(d.id) { mutableStateOf(d.expiresOnIso.orEmpty()) }
        AlertDialog(
            onDismissRequest = { editing = null },
            confirmButton = { TextButton(onClick = { viewModel.save(d.copy(note = note, expiresOnIso = expires.ifBlank { null })); editing = null }) { Text("Save") } },
            dismissButton = { TextButton(onClick = { editing = null }) { Text("Cancel") } },
            title = { Text(d.name) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    CupertinoTextField(value = note, onValueChange = { note = it }, placeholder = "Note, e.g. reference number, where it is")
                    CupertinoTextField(value = expires, onValueChange = { expires = it }, placeholder = "Expires on (yyyy-mm-dd)")
                }
            },
        )
    }
}
