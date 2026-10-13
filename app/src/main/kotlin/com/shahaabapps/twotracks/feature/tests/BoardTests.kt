package com.shahaabapps.twotracks.feature.tests

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.TestRecord
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.CondensedFamily
import com.shahaabapps.twotracks.ui.board.BoardBack
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardTitle

/** The bands a test needs, as its chip prints them: the target is the chip's job, so it is not said. */
internal fun boardBands(t: TestRecord): String = bandLine(t).removePrefix("TARGET  ").replace(Regex("""\b([A-Z]) (\d)"""), "$1$2")

/**
 * The tests on the Departures board, as its prototype draws them: each a
 * card edged in the colour of whose it is, with where it stands, what it is
 * for, and the bands it needs on a chip. Tap one to enter the result.
 */
@Composable
internal fun BoardTests(state: TestsUiState, onBack: () -> Unit, onEdit: (TestRecord) -> Unit) {
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(inner), contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp, bottom = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(Modifier.height(44.dp), contentAlignment = Alignment.CenterStart) { BoardBack(onBack, "Back to Route") }
                    BoardMono("CHECKPOINTS EACH ROUTE NEEDS")
                    BoardTitle("Tests", size = 52.sp)
                }
            }
            items(state.tests, key = { it.id }) { t ->
                val color = AppTheme.colors.track(t.owner == PersonRole.TECH)
                val status = testStatus(t, state.today)
                Column(
                    modifier = Modifier.padding(horizontal = BoardGutter).fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.card)
                        .drawBehind { drawRect(color, topLeft = Offset.Zero, size = Size(4.dp.toPx(), size.height)) }
                        .clickable(role = Role.Button, onClickLabel = "Enter the result") { onEdit(t) }
                        .padding(start = 18.dp, end = 14.dp, top = 12.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(t.name, color = AppTheme.colors.ink, modifier = Modifier.weight(1f), style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 21.sp))
                        BoardLabel(
                            status, size = 12.sp, weight = FontWeight.Bold, spacing = 0.1.em, modifier = Modifier.padding(top = 3.dp),
                            color = when {
                                status == "EXPIRED" -> AppTheme.colors.destructive
                                status.endsWith("MET") -> AppTheme.colors.success
                                status.startsWith("TAKEN") -> AppTheme.colors.warn
                                else -> AppTheme.colors.secondaryLabel
                            },
                        )
                    }
                    if (t.purpose.isNotBlank()) BoardText(t.purpose, size = 13.sp)
                    val bands = boardBands(t)
                    if (bands.isNotBlank()) {
                        Box(Modifier.clip(RoundedCornerShape(6.dp)).background(AppTheme.colors.deep).padding(horizontal = 8.dp, vertical = 4.dp)) {
                            BoardMono(bands, color = color, size = 12.sp, weight = FontWeight.Bold, spacing = 0.sp)
                        }
                    }
                }
            }
        }
    }
}
