package com.shahaabapps.twotracks.feature.budget

import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.data.tracking.TrackingRepository
import com.shahaabapps.core.model.BudgetItem
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Celebrations
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.DialogButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

/** Planned and spent in one currency. */
data class CurrencyTotal(val currency: String, val planned: Double, val spent: Double)

data class BudgetUiState(
    val items: List<BudgetItem> = emptyList(),
    val myRole: PersonRole = PersonRole.TECH,
    val partnerName: String = "",
) {
    /** In the order the currencies first appear, which is the order the move spends them in. */
    val totals: List<CurrencyTotal> get() = items.groupBy { it.currency }.map { (c, l) -> CurrencyTotal(c, l.sumOf { it.planned }, l.sumOf { it.spent }) }
}

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    dataStore: CoupleDataStore,
    private val trackingRepository: TrackingRepository,
    private val celebrations: Celebrations,
) : ViewModel() {
    val uiState: StateFlow<BudgetUiState> = combine(sessionStore.session, dataStore.budget) { session, items ->
        BudgetUiState(items.sortedBy { it.order }, session?.myRole ?: PersonRole.TECH, session?.let { it.nameFor(it.partnerRole) }.orEmpty())
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
}

internal fun money(n: Double): String = "%,.0f".format(Locale.UK, n)

/** "PAID", "150 SPENT" or "PLANNED". */
internal fun spentLabel(b: BudgetItem): String = when {
    b.spent <= 0.0 -> "PLANNED"
    b.spent >= b.planned -> "PAID"
    else -> "${money(b.spent)} SPENT"
}

@Composable
fun BudgetScreen(onBack: () -> Unit, viewModel: BudgetViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BudgetContent(state, onBack = onBack, onSave = viewModel::save, onDelete = viewModel::delete)
}

/**
 * What the move costs, as the prototype draws it: a total for each currency
 * under an ink rule, then every item with a bar in its owner's colour, what
 * it is planned at, and whether it is paid.
 */
@Composable
fun BudgetContent(state: BudgetUiState, onBack: () -> Unit, onSave: (BudgetItem) -> Unit, onDelete: (String) -> Unit) {
    var editing by remember { mutableStateOf<BudgetItem?>(null) }
    DetailScaffold(
        title = "Budget", onBack = onBack, backLabel = "Back to Together", eyebrow = "What the move costs, in the currency it is paid in",
        actions = { IconButton(onClick = { editing = BudgetItem(owner = state.myRole, order = 999) }) { Icon(Icons.Filled.Add, "Add an item", tint = AppTheme.colors.accentText) } },
    ) {
        LazyColumn(contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 10.dp)) {
                    HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
                    state.totals.chunked(2).forEach { pair ->
                        Row {
                            pair.forEach { t ->
                                Column(modifier = Modifier.weight(1f).padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(t.currency, style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp), color = AppTheme.colors.secondaryLabel)
                                    Text(money(t.planned), style = MaterialTheme.typography.headlineMedium.copy(fontSize = 22.sp), fontWeight = FontWeight.SemiBold)
                                    Text("${money(t.spent)} spent", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
                                }
                            }
                            if (pair.size == 1) Box(Modifier.weight(1f))
                        }
                        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                    }
                }
            }
            item { Text("Items", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 20.dp, bottom = 4.dp)) }
            items(state.items, key = { it.id.ifBlank { it.item } }) { b ->
                val who = when (b.owner) { null -> "Both of you"; state.myRole -> "You"; else -> state.partnerName }
                val bar = b.owner?.let { AppTheme.colors.track(it == PersonRole.TECH) } ?: AppTheme.colors.secondaryLabel
                Column(modifier = Modifier.clickable(role = Role.Button, onClickLabel = "Edit") { editing = b }.padding(horizontal = Spacing.listHorizontal)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).padding(vertical = 11.dp)) {
                        Box(Modifier.width(4.dp).fillMaxHeight().background(bar))
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
                            Text(b.item, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), fontWeight = FontWeight.Medium)
                            Text(who, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
                        }
                        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("${b.currency} ${money(b.planned)}", style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 13.sp), textAlign = TextAlign.End)
                            val label = spentLabel(b)
                            Text(
                                label, style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 10.sp),
                                color = when {
                                    b.spent > b.planned -> AppTheme.colors.destructive
                                    label == "PAID" -> AppTheme.colors.success
                                    else -> AppTheme.colors.secondaryLabel
                                },
                            )
                        }
                    }
                    HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                }
            }
            item {
                Text(
                    "Fees are 2026 estimates; check the vendor's site before paying.",
                    style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                    modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 12.dp),
                )
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
                    onSave(b.copy(item = item.trim(), planned = planned.toDoubleOrNull() ?: 0.0, spent = spent.toDoubleOrNull() ?: 0.0, currency = currency.uppercase().ifBlank { "USD" }, paidOnIso = paid.ifBlank { null }))
                    editing = null
                }, enabled = item.isNotBlank()) { Text("Save") }
            },
            dismissButton = {
                Row {
                    if (b.id.isNotBlank()) DialogButton(onClick = { onDelete(b.id); editing = null }, destructive = true) { Text("Delete") }
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
