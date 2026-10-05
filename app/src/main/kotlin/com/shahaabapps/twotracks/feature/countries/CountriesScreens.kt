package com.shahaabapps.twotracks.feature.countries

import com.shahaabapps.twotracks.session.Celebrations
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.data.tracking.TrackingRepository
import com.shahaabapps.core.model.CountryTodo
import com.shahaabapps.core.model.CountryTrack
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.ProgressBar
import com.shahaabapps.twotracks.ui.components.SectionLabel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionStore: SessionStore,
    dataStore: CoupleDataStore,
    private val trackingRepository: TrackingRepository,
    private val celebrations: Celebrations,
) : ViewModel() {
    private val code: String? = savedStateHandle.get<String>("code")

    val countries: StateFlow<List<CountryTrack>> = dataStore.countries.map { it.sortedBy { c -> c.order } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val country: StateFlow<CountryTrack?> = dataStore.countries.map { list -> list.firstOrNull { it.code == code } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun toggleTodo(c: CountryTrack, todo: CountryTodo) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        trackingRepository.saveCountry(s.coupleId, c.copy(todos = c.todos.map { if (it.id == todo.id) it.copy(done = !it.done) else it }))
        if (!todo.done) celebrations.saved("Done")
    }

    fun clearNotes(c: CountryTrack) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        trackingRepository.saveCountry(s.coupleId, c.copy(notes = ""))
        celebrations.removed("Notes cleared")
    }

    fun saveNotes(c: CountryTrack, notes: String) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        trackingRepository.saveCountry(s.coupleId, c.copy(notes = notes))
        celebrations.saved("Notes saved")
    }
}

@Composable
fun CountriesScreen(onBack: () -> Unit, onOpen: (String) -> Unit, viewModel: CountriesViewModel = hiltViewModel()) {
    val countries by viewModel.countries.collectAsStateWithLifecycle()
    DetailScaffold(title = "Countries", onBack = onBack) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            items(countries, key = { it.code }) { c ->
                val done = c.todos.count { it.done }
                CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), onClick = { onOpen(c.code) }) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                        Text(c.flag, fontSize = 32.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(c.name, style = MaterialTheme.typography.titleMedium)
                            Text(c.tagline, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                        }
                    }
                    Row(modifier = Modifier.padding(top = Spacing.sm), horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                        Fact("Lead", c.lead)
                        Fact("Citizenship", c.citizenship)
                        Fact("PR", c.permanentResidence)
                    }
                    ProgressBar(fraction = if (c.todos.isEmpty()) 0f else done.toFloat() / c.todos.size, modifier = Modifier.padding(top = Spacing.sm))
                    Text("$done of ${c.todos.size} to-dos done", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
                }
            }
        }
    }
}

@Composable
private fun Fact(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, maxLines = 2)
    }
}

@Composable
fun CountryDetailScreen(code: String, onBack: () -> Unit, viewModel: CountriesViewModel = hiltViewModel()) {
    val country by viewModel.country.collectAsStateWithLifecycle()
    val c = country
    DetailScaffold(title = c?.let { "${it.flag} ${it.name}" } ?: code, onBack = onBack) {
        if (c == null) return@DetailScaffold
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    Text(c.tagline, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
                    Text("Chance, tech as main applicant: ${c.chanceTech}", style = MaterialTheme.typography.bodyMedium)
                    Text("Chance, teacher as main applicant: ${c.chanceTeacher}", style = MaterialTheme.typography.bodyMedium)
                }
            }
            item { SectionLabel("Key facts") }
            item {
                CupertinoListSection {
                    c.keyFacts.forEachIndexed { i, (k, v) -> CupertinoListRow(title = k, subtitle = v, showDivider = i < c.keyFacts.lastIndex) }
                }
            }
            item { SectionLabel("What to do") }
            item {
                CupertinoListSection {
                    c.todos.forEachIndexed { i, t ->
                        CupertinoListRow(
                            title = t.text, showDivider = i < c.todos.lastIndex, onClick = { viewModel.toggleTodo(c, t) },
                            trailing = {
                                Icon(
                                    if (t.done) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank, contentDescription = null,
                                    tint = if (t.done) AppTheme.colors.success else AppTheme.colors.secondaryLabel,
                                )
                            },
                        )
                    }
                }
            }
            item { SectionLabel("Risks to watch") }
            item {
                CupertinoListSection {
                    c.risks.forEachIndexed { i, r -> CupertinoListRow(title = r, showDivider = i < c.risks.lastIndex) }
                }
            }
            item { SectionLabel("Notes") }
            item {
                var text by androidx.compose.runtime.remember(c.code) { androidx.compose.runtime.mutableStateOf(c.notes) }
                Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                    CupertinoTextField(value = text, onValueChange = { text = it }, placeholder = "Your notes, e.g. recruiters, salary quotes", singleLine = false, modifier = Modifier.fillMaxWidth())
                    Row {
                        com.shahaabapps.core.ui.components.CupertinoTextButton(text = "Save notes", onClick = { viewModel.saveNotes(c, text) }, modifier = Modifier.weight(1f))
                        if (c.notes.isNotBlank()) {
                            com.shahaabapps.core.ui.components.CupertinoTextButton(text = "Clear notes", destructive = true, onClick = { text = ""; viewModel.clearNotes(c) }, modifier = Modifier.weight(1f))
                        }
                    }
                    if (c.notes.isNotBlank()) {
                        Text("Saved with this country in your shared space. Both of you can read them here.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                    }
                }
            }
        }
    }
}
