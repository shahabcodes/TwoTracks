package com.shahaabapps.twotracks.feature.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.model.BudgetItem
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.CondensedFamily
import com.shahaabapps.twotracks.ui.board.BoardBack
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardLink
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardTitle
import com.shahaabapps.twotracks.ui.board.WhoMark

/**
 * The budget on the Departures board, as its prototype draws it: a card for
 * each currency with what is planned and spent, then every fare with a mark
 * in its owner's colour, what it is planned at, and whether it is paid. Tap
 * one to edit it; ADD makes a new one.
 */
@Composable
internal fun BoardBudget(state: BudgetUiState, onBack: () -> Unit, onEdit: (BudgetItem) -> Unit) {
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(inner), contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().height(44.dp)) {
                        BoardBack(onBack, "Back to Route")
                        Box(Modifier.weight(1f))
                        BoardLink("+ Add", { onEdit(BudgetItem(owner = state.myRole, order = 999)) }, size = 15.sp)
                    }
                    BoardMono("FARES · IN THE CURRENCY THEY ARE PAID IN")
                    BoardTitle("Budget", size = 52.sp)
                }
            }
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.totals.chunked(2).forEach { pair ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.height(IntrinsicSize.Min)) {
                            pair.forEach { t ->
                                Column(
                                    modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(12.dp)).background(AppTheme.colors.card).padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalArrangement = Arrangement.spacedBy(2.dp),
                                ) {
                                    BoardLabel(t.currency, size = 13.sp, weight = FontWeight.Bold, spacing = 0.12.em)
                                    Text(money(t.planned), color = AppTheme.colors.ink, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp))
                                    BoardMono("${money(t.spent)} SPENT", color = if (t.spent > 0) AppTheme.colors.warn else AppTheme.colors.secondaryLabel, spacing = 0.sp)
                                }
                            }
                            if (pair.size == 1) Box(Modifier.weight(1f))
                        }
                    }
                }
            }
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 18.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                        Box(Modifier.width(8.dp))
                        BoardLabel("Item", spacing = 0.12.em, modifier = Modifier.weight(1f))
                        BoardLabel("Fare", spacing = 0.12.em, textAlign = TextAlign.End, modifier = Modifier.width(110.dp))
                    }
                    Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                }
            }
            items(state.items, key = { it.id.ifBlank { it.item } }) { b ->
                val who = when (b.owner) { null -> "Both of you"; state.myRole -> "You"; else -> state.partnerName }
                val mark = b.owner?.let { AppTheme.colors.track(it == PersonRole.TECH) } ?: AppTheme.colors.secondaryLabel
                Column(modifier = Modifier.clickable(role = Role.Button, onClickLabel = "Edit") { onEdit(b) }.padding(horizontal = BoardGutter)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
                        WhoMark(mark)
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
                            BoardText(b.item, color = AppTheme.colors.ink, size = 14.sp, weight = FontWeight.Medium)
                            BoardText(who, size = 12.sp)
                        }
                        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.width(110.dp)) {
                            BoardMono("${b.currency} ${money(b.planned)}", color = AppTheme.colors.ink, size = 13.sp, weight = FontWeight.Bold, spacing = 0.sp, textAlign = TextAlign.End)
                            val label = spentLabel(b)
                            BoardLabel(
                                label, size = 12.sp, weight = FontWeight.Bold, spacing = 0.08.em, textAlign = TextAlign.End,
                                color = when {
                                    b.spent > b.planned -> AppTheme.colors.destructive
                                    label == "PAID" -> AppTheme.colors.success
                                    b.spent > 0 -> AppTheme.colors.warn
                                    else -> AppTheme.colors.secondaryLabel
                                },
                            )
                        }
                    }
                    Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                }
            }
            item {
                BoardText(
                    "Fees are 2026 estimates; check the vendor's site before paying.", size = 12.sp,
                    modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 12.dp),
                )
            }
        }
    }
}
