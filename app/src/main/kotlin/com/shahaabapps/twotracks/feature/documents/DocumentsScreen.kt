package com.shahaabapps.twotracks.feature.documents

import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.data.tracking.TrackingRepository
import com.shahaabapps.core.model.DocStatus
import com.shahaabapps.core.model.DocumentItem
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Celebrations
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.DialogButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Whose documents are showing: everyone's, or one of you. */
enum class DocFilter { ALL, MINE, THEIRS }

data class DocumentsUiState(
    val myRole: PersonRole = PersonRole.TECH,
    val partnerName: String = "",
    val filter: DocFilter = DocFilter.ALL,
    val all: List<DocumentItem> = emptyList(),
) {
    val shown: List<DocumentItem> get() = when (filter) {
        DocFilter.ALL -> all
        DocFilter.MINE -> all.filter { it.owner == myRole }
        DocFilter.THEIRS -> all.filter { it.owner != myRole }
    }
    val done: Int get() = all.count { it.status == DocStatus.DONE }
}

@HiltViewModel
class DocumentsViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    dataStore: CoupleDataStore,
    private val trackingRepository: TrackingRepository,
    private val celebrations: Celebrations,
) : ViewModel() {
    private val filter = MutableStateFlow(DocFilter.ALL)

    val uiState: StateFlow<DocumentsUiState> = combine(sessionStore.session, dataStore.documents, filter) { session, docs, f ->
        DocumentsUiState(
            myRole = session?.myRole ?: PersonRole.TECH, partnerName = session?.let { it.nameFor(it.partnerRole) }.orEmpty(),
            filter = f, all = docs.sortedWith(compareBy<DocumentItem> { it.order }.thenBy { it.owner }),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DocumentsUiState())

    fun setFilter(f: DocFilter) { filter.value = f }

    fun save(doc: DocumentItem) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        trackingRepository.saveDocument(s.coupleId, doc)
        celebrations.saved("Document updated")
    }

    fun cycle(doc: DocumentItem) = save(doc.copy(status = next(doc.status)))
}

internal fun next(s: DocStatus): DocStatus = when (s) {
    DocStatus.NOT_STARTED -> DocStatus.IN_PROGRESS
    DocStatus.IN_PROGRESS -> DocStatus.DONE
    DocStatus.DONE -> DocStatus.NOT_STARTED
}

/** "Attested · all countries", with a note and an expiry when there are any. */
internal fun docLine(d: DocumentItem): String {
    // Where it is used, unless the detail already says so; "All countries"
    // reads as part of the sentence after a detail, a country name does not.
    val usedFor = d.usedFor.trim().takeUnless { it.isBlank() || d.detail.contains(it, ignoreCase = true) }
        ?.let { if (d.detail.isNotBlank() && it.startsWith("All ")) it.replaceFirstChar { c -> c.lowercase() } else it }
    return listOfNotNull(d.detail.ifBlank { null }, usedFor, d.note.ifBlank { null }, d.expiresOnIso?.let { "expires ${Clock.formatDate(it)}" }).joinToString(" · ")
}

@Composable
fun DocumentsScreen(onBack: () -> Unit, viewModel: DocumentsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DocumentsContent(state, onBack = onBack, onFilter = viewModel::setFilter, onCycle = viewModel::cycle, onSave = viewModel::save)
}

/**
 * Both of your documents, as the prototype draws them: how many are done,
 * a filter for whose, and each paper with a bar in its owner's colour and
 * a status pill that moves on when tapped. Tapping the row opens its note
 * and expiry.
 */
@Composable
fun DocumentsContent(state: DocumentsUiState, onBack: () -> Unit, onFilter: (DocFilter) -> Unit, onCycle: (DocumentItem) -> Unit, onSave: (DocumentItem) -> Unit) {
    var editing by remember { mutableStateOf<DocumentItem?>(null) }
    val shown = state.shown
    if (AppTheme.departures) BoardDocuments(state, onBack, onFilter, onCycle) { editing = it } else DetailScaffold(title = "Documents", onBack = onBack, backLabel = "Back to Together", eyebrow = "${state.done} of ${state.all.size} done · showing ${shown.size}") {
        LazyColumn(contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 2.dp)) {
                    listOf(DocFilter.ALL to "All", DocFilter.MINE to "Yours", DocFilter.THEIRS to "${state.partnerName}'s").forEach { (f, label) ->
                        val on = state.filter == f
                        val shape = RoundedCornerShape(18.dp)
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.height(36.dp).clip(shape).background(if (on) AppTheme.colors.ink else Color.Transparent)
                                .border(Spacing.rule, if (on) AppTheme.colors.ink else AppTheme.colors.separator, shape)
                                .clickable(role = Role.Tab) { onFilter(f) }.padding(horizontal = 14.dp),
                        ) { Text(label, style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp), fontWeight = FontWeight.Medium, color = if (on) AppTheme.colors.groupedBackground else AppTheme.colors.ink) }
                    }
                }
            }
            item { HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule, modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 14.dp)) }
            items(shown, key = { it.id }) { d ->
                Column(modifier = Modifier.clickable(role = Role.Button, onClickLabel = "Edit the note and expiry") { editing = d }.padding(horizontal = Spacing.listHorizontal)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).heightIn(min = 64.dp).padding(vertical = 10.dp),
                    ) {
                        Box(Modifier.width(4.dp).fillMaxHeight().background(AppTheme.colors.track(d.owner == PersonRole.TECH)))
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
                            Text(d.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            val line = docLine(d)
                            if (line.isNotBlank()) Text(line, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
                        }
                        StatusPill(d, onClick = { onCycle(d) })
                    }
                    HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                }
            }
        }
    }

    editing?.let { d ->
        var note by remember(d.id) { mutableStateOf(d.note) }
        var expires by remember(d.id) { mutableStateOf(d.expiresOnIso.orEmpty()) }
        CupertinoDialog(
            onDismissRequest = { editing = null },
            confirmButton = { DialogButton(onClick = { onSave(d.copy(note = note, expiresOnIso = expires.ifBlank { null })); editing = null }) { Text("Save") } },
            dismissButton = { DialogButton(onClick = { editing = null }) { Text("Cancel") } },
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

@Composable
private fun StatusPill(d: DocumentItem, onClick: () -> Unit) {
    val (label, ink) = when (d.status) {
        DocStatus.NOT_STARTED -> "NOT STARTED" to AppTheme.colors.secondaryLabel
        DocStatus.IN_PROGRESS -> "IN PROGRESS" to AppTheme.colors.warn
        DocStatus.DONE -> "DONE" to AppTheme.colors.success
    }
    val shape = RoundedCornerShape(16.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.height(32.dp).clip(shape)
            .background(if (d.status == DocStatus.DONE) AppTheme.colors.success.copy(alpha = 0.06f) else Color.Transparent)
            .border(Spacing.rule, if (d.status == DocStatus.NOT_STARTED) AppTheme.colors.separator else ink, shape)
            .semantics { contentDescription = "${d.name}: ${label.lowercase()}. Tap to change" }
            .clickable(role = Role.Button, onClick = onClick).padding(horizontal = 10.dp),
    ) { Text(label, style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 10.sp, letterSpacing = 0.6.sp), color = ink, maxLines = 1) }
}
