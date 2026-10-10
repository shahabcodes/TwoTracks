package com.shahaabapps.twotracks.feature.budget

import com.shahaabapps.twotracks.ui.components.DialogButton
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import com.shahaabapps.twotracks.session.Celebrations
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.data.tracking.TrackingRepository
import com.shahaabapps.core.model.BudgetItem
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.StatTile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BudgetUiState(
    val groups: List<Pair<String, List<BudgetItem>>> = emptyList(),
    val totalsByCurrency: List<Triple<String, Double, Double>> = emptyList(),
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    dataStore: CoupleDataStore,
    private val trackingRepository: TrackingRepository,
    private val celebrations: Celebrations,
) : ViewModel() {
    val uiState: StateFlow<BudgetUiState> = combine(sessionStore.session, dataStore.budget) { session, items ->
        val sorted = items.sortedBy { it.order }
        val groups = listOf<PersonRole?>(session?.myRole, session?.partnerRole, null).distinct().map { owner ->
            (owner?.let { session?.nameFor(it) } ?: "Shared") to sorted.filter { it.owner == owner }
        }.filter { it.second.isNotEmpty() }
        BudgetUiState(
            groups = groups,
            totalsByCurrency = items.groupBy { it.currency }.map { (c, l) -> Triple(c, l.sumOf { it.planned }, l.sumOf { it.spent }) }.sortedBy { it.first },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BudgetUiState())

    fun save(item: BudgetItem) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        trackingRepository.saveBudget(s.coupleId, item)
        celebrations.saved("Budget saved")
    }

    fun delete(id: String) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        trackingRepository.deleteBudget(s.coupleId, id)
        celebrations.removed("Budget item removed")
    }

    fun myRole(): PersonRole? = sessionStore.current()?.myRole
}

@Composable
fun BudgetScreen(onBack: () -> Unit, viewModel: BudgetViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var editing by remember { mutableStateOf<BudgetItem?>(null) }

    DetailScaffold(
        title = "Budget", onBack = onBack,
        actions = { IconButton(onClick = { editing = BudgetItem(owner = viewModel.myRole(), order = 999) }) { Icon(Icons.Filled.Add, "Add", tint = AppTheme.colors.accentText) } },
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                    state.totalsByCurrency.take(3).forEach { (c, planned, spent) ->
                        StatTile(c, "%.0f / %.0f".format(spent, planned), Modifier.weight(1f))
                    }
                }
            }
            item {
                Text("Spent / planned per currency. Fees are 2026 estimates; verify on the vendor site before paying.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(horizontal = Spacing.listHorizontal))
            }
            state.groups.forEach { (name, items) ->
                item(key = name) {
                    CupertinoListSection(header = name) {
                        items.forEachIndexed { i, b ->
                            CupertinoListRow(
                                title = b.item,
                                subtitle = "Planned ${b.currency} %.0f".format(b.planned) + (b.paidOnIso?.let { ", paid $it" } ?: ""),
                                showDivider = i < items.lastIndex,
                                onClick = { editing = b },
                                trailing = {
                                    Text(
                                        if (b.spent > 0) "%.0f".format(b.spent) else "-",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (b.spent > b.planned) AppTheme.colors.destructive else MaterialTheme.colorScheme.onSurface,
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    editing?.let { b ->
        var item by remember(b.id) { mutableStateOf(b.item) }
        var planned by remember(b.id) { mutableStateOf(if (b.planned > 0) "%.0f".format(b.planned) else "") }
        var spent by remember(b.id) { mutableStateOf(if (b.spent > 0) "%.0f".format(b.spent) else "") }
        var currency by remember(b.id) { mutableStateOf(b.currency) }
        var paid by remember(b.id) { mutableStateOf(b.paidOnIso.orEmpty()) }
        CupertinoDialog(
            onDismissRequest = { editing = null },
            confirmButton = {
                DialogButton(onClick = {
                    viewModel.save(b.copy(item = item.trim(), planned = planned.toDoubleOrNull() ?: 0.0, spent = spent.toDoubleOrNull() ?: 0.0, currency = currency.uppercase().ifBlank { "USD" }, paidOnIso = paid.ifBlank { null }))
                    editing = null
                }, enabled = item.isNotBlank()) { Text("Save") }
            },
            dismissButton = {
                Row {
                    if (b.id.isNotBlank()) DialogButton(onClick = { viewModel.delete(b.id); editing = null }, destructive = true) { Text("Delete") }
                    DialogButton(onClick = { editing = null }) { Text("Cancel") }
                }
            },
            title = { Text(if (b.id.isBlank()) "New item" else "Edit item") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    CupertinoTextField(value = item, onValueChange = { item = it }, placeholder = "Item")
                    CupertinoTextField(value = planned, onValueChange = { planned = it }, placeholder = "Planned", keyboardType = KeyboardType.Decimal)
                    CupertinoTextField(value = spent, onValueChange = { spent = it }, placeholder = "Spent", keyboardType = KeyboardType.Decimal)
                    CupertinoTextField(value = currency, onValueChange = { currency = it.take(3) }, placeholder = "Currency (USD, EUR, AUD, CAD)")
                    CupertinoTextField(value = paid, onValueChange = { paid = it }, placeholder = "Paid on (yyyy-mm-dd)")
                }
            },
        )
    }
}
