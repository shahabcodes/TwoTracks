package com.shahaabapps.twotracks.feature.documents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.model.DocStatus
import com.shahaabapps.core.model.DocumentItem
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.twotracks.ui.board.BoardBack
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardSegments
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardTitle
import com.shahaabapps.twotracks.ui.board.WhoMark
import java.util.Locale

/** NOT YET, IN TRANSIT, CLEARED: where a document is, as the board says it. */
internal fun boardDocStatus(s: DocStatus): String = when (s) {
    DocStatus.NOT_STARTED -> "Not yet"
    DocStatus.IN_PROGRESS -> "In transit"
    DocStatus.DONE -> "Cleared"
}

/** "5 OF 12 CLEARED · SHOWING 12" */
internal fun documentsSummary(done: Int, total: Int, shown: Int): String = "$done of $total cleared · showing $shown".uppercase(Locale.ROOT)

/**
 * The documents on the Departures board, as their prototype draws them:
 * how many are cleared, whose as three tabs, and each paper with a mark in
 * its owner's colour and a status that moves on when tapped. Tapping the
 * row opens its note and expiry.
 */
@Composable
internal fun BoardDocuments(state: DocumentsUiState, onBack: () -> Unit, onFilter: (DocFilter) -> Unit, onCycle: (DocumentItem) -> Unit, onEdit: (DocumentItem) -> Unit) {
    val shown = state.shown
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(inner), contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(Modifier.height(44.dp), contentAlignment = Alignment.CenterStart) { BoardBack(onBack, "Back to Route") }
                    BoardMono(documentsSummary(state.done, state.all.size, shown.size))
                    BoardTitle("Documents", size = 52.sp)
                    BoardSegments(
                        listOf(DocFilter.ALL to "All", DocFilter.MINE to "Yours", DocFilter.THEIRS to state.partnerName.ifBlank { "Theirs" }),
                        selected = state.filter, onSelect = onFilter, modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
            item { Box(Modifier.height(12.dp)) }
            items(shown, key = { it.id }) { d ->
                Column(modifier = Modifier.clickable(role = Role.Button, onClickLabel = "Edit the note and expiry") { onEdit(d) }.padding(horizontal = BoardGutter)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp).padding(vertical = 10.dp),
                    ) {
                        WhoMark(AppTheme.colors.track(d.owner == PersonRole.TECH))
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
                            BoardText(d.name, color = AppTheme.colors.ink, weight = FontWeight.Medium)
                            val line = docLine(d)
                            if (line.isNotBlank()) BoardText(line, size = 12.sp)
                        }
                        Status(d) { onCycle(d) }
                    }
                    Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                }
            }
        }
    }
}

@Composable
private fun Status(d: DocumentItem, onClick: () -> Unit) {
    val ink = when (d.status) {
        DocStatus.NOT_STARTED -> AppTheme.colors.secondaryLabel
        DocStatus.IN_PROGRESS -> AppTheme.colors.warn
        DocStatus.DONE -> AppTheme.colors.success
    }
    val fill = when (d.status) {
        DocStatus.NOT_STARTED -> Color.Transparent
        DocStatus.IN_PROGRESS -> lerp(AppTheme.colors.groupedBackground, AppTheme.colors.warn, 0.1f)
        DocStatus.DONE -> AppTheme.colors.allowedBackground
    }
    val label = boardDocStatus(d.status)
    val shape = RoundedCornerShape(8.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.widthIn(min = 86.dp).height(34.dp).clip(shape).background(fill)
            .border(if (d.status == DocStatus.NOT_STARTED) 1.dp else 1.5.dp, if (d.status == DocStatus.NOT_STARTED) AppTheme.colors.separator else ink, shape)
            .semantics { contentDescription = "${d.name}: ${label.lowercase(Locale.ENGLISH)}. Tap to change" }
            .clickable(role = Role.Button, onClick = onClick).padding(horizontal = 10.dp),
    ) { BoardLabel(label, color = ink, size = 14.sp, weight = FontWeight.Bold, spacing = 0.08.em) }
}

