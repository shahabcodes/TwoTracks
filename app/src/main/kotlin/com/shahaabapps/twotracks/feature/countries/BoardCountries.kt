package com.shahaabapps.twotracks.feature.countries

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.model.CountryTodo
import com.shahaabapps.core.model.CountryTrack
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.BarlowFamily
import com.shahaabapps.core.ui.theme.CondensedFamily
import com.shahaabapps.twotracks.ui.board.BoardBack
import com.shahaabapps.twotracks.ui.board.BoardField
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardLink
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardTitle
import java.util.Locale

/** "45–60%", or "<10%" for a chance the guide writes as under ten. */
internal fun boardChance(text: String): String = chance(text).short.replace("Under ", "<")

/** "6 DESTINATIONS · ONE OF THEM HOME" */
internal fun destinationsLine(n: Int): String = "$n destination${if (n == 1) "" else "s"} · one of them home".uppercase(Locale.ROOT)

private fun techName(r: Reader) = if (r.myRole == PersonRole.TECH) r.myName else r.partnerName
private fun teacherName(r: Reader) = if (r.myRole == PersonRole.TEACHER) r.myName else r.partnerName

/**
 * The destinations on the Departures board, as its prototype draws them: a
 * code, the name and each of your chances a row, the code in the colour of
 * whoever leads and each chance lit for the person it counts on.
 */
@Composable
internal fun BoardCountries(state: CountriesUiState, onBack: () -> Unit, onOpen: (String) -> Unit) {
    val r = state.reader
    val tech = AppTheme.colors.track(true)
    val teacher = AppTheme.colors.track(false)
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(inner), contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(Modifier.height(44.dp), contentAlignment = Alignment.CenterStart) { BoardBack(onBack, "Back to Route") }
                    BoardMono(destinationsLine(state.countries.size))
                    BoardTitle("Destinations", size = 52.sp)
                }
            }
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 14.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                        BoardLabel("Code", spacing = 0.12.em, modifier = Modifier.width(40.dp))
                        BoardLabel("Destination", spacing = 0.12.em, modifier = Modifier.weight(1f))
                        BoardLabel(techName(r), color = tech, spacing = 0.12.em, textAlign = TextAlign.End, modifier = Modifier.width(64.dp))
                        BoardLabel(teacherName(r), color = teacher, spacing = 0.12.em, textAlign = TextAlign.End, modifier = Modifier.width(64.dp))
                    }
                    Rule()
                }
            }
            items(state.countries, key = { it.code }) { c ->
                val (lead, leadRole) = leadOf(c, r)
                val leadInk = leadRole?.let { AppTheme.colors.track(it == PersonRole.TECH) } ?: AppTheme.colors.ink
                Column(modifier = Modifier.fillMaxWidth().clickable(role = Role.Button) { onOpen(c.code) }.padding(horizontal = BoardGutter)) {
                    Column(modifier = Modifier.padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(c.code, color = leadInk, modifier = Modifier.width(40.dp), style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp))
                            Text(c.name, color = AppTheme.colors.ink, modifier = Modifier.weight(1f), style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 22.sp))
                            BoardMono(
                                boardChance(c.chanceTech), color = if (leadRole != PersonRole.TEACHER) tech else AppTheme.colors.secondaryLabel,
                                size = 12.sp, weight = FontWeight.Bold, spacing = 0.sp, textAlign = TextAlign.End, modifier = Modifier.width(64.dp),
                            )
                            BoardMono(
                                boardChance(c.chanceTeacher), color = if (leadRole != PersonRole.TECH) teacher else AppTheme.colors.secondaryLabel,
                                size = 12.sp, weight = FontWeight.Bold, spacing = 0.sp, textAlign = TextAlign.End, modifier = Modifier.width(64.dp),
                            )
                        }
                        Column(modifier = Modifier.padding(start = 48.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            BoardLabel(if (leadRole == null) "$lead leads" else lead, color = leadInk, size = 12.sp, weight = FontWeight.Bold, spacing = 0.1.em)
                            if (c.tagline.isNotBlank()) BoardText(c.tagline, size = 13.sp)
                        }
                    }
                    Rule()
                }
            }
            item {
                BoardText(
                    "Chances are a range, not a promise, and move as tests and paperwork clear.", size = 12.sp,
                    modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 12.dp),
                )
            }
        }
    }
}

/**
 * One destination on the Departures board, as its prototype draws it: the
 * code and who leads in that person's colour, the name in capitals, both
 * chances and the routes to staying on cards, then the travel information,
 * the boarding steps to tick, and what could disrupt it.
 */
@Composable
internal fun BoardCountry(
    state: CountryUiState,
    onBack: () -> Unit,
    onToggle: (CountryTrack, CountryTodo) -> Unit,
    onSaveNotes: (CountryTrack, String) -> Unit,
    onClearNotes: (CountryTrack) -> Unit,
) {
    val c = state.country
    val r = state.reader
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(inner), contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(Modifier.height(44.dp), contentAlignment = Alignment.CenterStart) { BoardBack(onBack, "Back to destinations") }
                    if (c == null) return@Column
                    val (lead, leadRole) = leadOf(c, r)
                    val ink = leadRole?.let { AppTheme.colors.track(it == PersonRole.TECH) } ?: AppTheme.colors.ink
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(c.code, color = ink, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 28.sp))
                        BoardLabel(if (leadRole == null) "$lead leads" else lead, color = ink, size = 13.sp, weight = FontWeight.Bold)
                    }
                    BoardTitle(c.name, size = 52.sp)
                    if (c.tagline.isNotBlank()) BoardText(c.tagline)
                }
            }
            if (c == null) return@LazyColumn
            item {
                val me = chance(myChance(c, r))
                val them = chance(theirChance(c, r))
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.height(IntrinsicSize.Min)) {
                        Fact("Your chance", me.short.replace("Under ", "<"), me.rest, AppTheme.colors.track(r.myRole == PersonRole.TECH), Modifier.weight(1f).fillMaxHeight())
                        Fact("${r.partnerName}'s chance", them.short.replace("Under ", "<"), them.rest, AppTheme.colors.track(r.myRole != PersonRole.TECH), Modifier.weight(1f).fillMaxHeight())
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.height(IntrinsicSize.Min)) {
                        Fact("Permanent residence", null, c.permanentResidence.replace(" (else", ", else").removeSuffix(")"), null, Modifier.weight(1f).fillMaxHeight())
                        Fact("Citizenship", null, c.citizenship.replace(" + ", ", "), null, Modifier.weight(1f).fillMaxHeight())
                    }
                }
            }
            if (c.keyFacts.isNotEmpty()) {
                item { Heading("Travel information") }
                items(c.keyFacts, key = { "fact-" + it.first }) { (k, v) ->
                    Column(modifier = Modifier.padding(horizontal = BoardGutter)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(vertical = 10.dp)) {
                            BoardLabel(k.substringBefore(" ("), color = AppTheme.colors.accent, size = 13.sp, weight = FontWeight.Bold, spacing = 0.1.em, modifier = Modifier.width(80.dp).padding(top = 1.dp))
                            BoardText(v, color = AppTheme.colors.ink, size = 14.sp, modifier = Modifier.weight(1f))
                        }
                        Rule()
                    }
                }
            }
            if (c.todos.isNotEmpty()) {
                item { Heading("Boarding steps") }
                items(c.todos, key = { "todo-" + it.id }) { t ->
                    Column(modifier = Modifier.clickable(role = Role.Checkbox) { onToggle(c, t) }.padding(horizontal = BoardGutter)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(vertical = 12.dp)) {
                            val shape = RoundedCornerShape(5.dp)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(top = 2.dp).size(20.dp).clip(shape).background(if (t.done) AppTheme.colors.success else Color.Transparent)
                                    .border(2.dp, if (t.done) AppTheme.colors.success else AppTheme.colors.strongBorder, shape),
                            ) { if (t.done) Icon(Icons.Filled.Check, contentDescription = "Done", tint = AppTheme.colors.onSolid, modifier = Modifier.size(14.dp)) }
                            Text(
                                t.text, color = if (t.done) AppTheme.colors.secondaryLabel else AppTheme.colors.ink, modifier = Modifier.weight(1f),
                                textDecoration = if (t.done) TextDecoration.LineThrough else null,
                                style = TextStyle(fontFamily = BarlowFamily, fontSize = 14.sp, lineHeight = 20.sp),
                            )
                        }
                        Rule()
                    }
                }
            }
            if (c.risks.isNotEmpty()) {
                item {
                    val warn = AppTheme.colors.warn
                    Column(
                        modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 18.dp).fillMaxWidth().clip(RoundedCornerShape(14.dp))
                            .background(lerp(AppTheme.colors.groupedBackground, warn, 0.1f))
                            .drawBehind { drawRect(warn, topLeft = Offset.Zero, size = Size(4.dp.toPx(), size.height)) }
                            .padding(start = 18.dp, end = 14.dp, top = 12.dp, bottom = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        BoardLabel("Possible disruption", color = warn, size = 13.sp, weight = FontWeight.Bold, spacing = 0.12.em)
                        c.risks.forEach { risk ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                BoardText("•", color = AppTheme.colors.ink, size = 14.sp)
                                BoardText(risk, color = AppTheme.colors.ink, size = 14.sp)
                            }
                        }
                    }
                }
            }
            // Not in the prototype: the shared notes this page has always kept.
            item {
                var text by remember(c.code, c.notes) { mutableStateOf(c.notes) }
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    BoardField(text, { text = it }, "Notes", placeholder = "Recruiters, salary quotes, anything worth keeping", singleLine = false, height = 64.dp)
                    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        if (text != c.notes) BoardLink("Save notes", { onSaveNotes(c, text) }, modifier = Modifier.padding(vertical = 10.dp))
                        if (c.notes.isNotBlank()) BoardLink("Clear notes", { text = ""; onClearNotes(c) }, color = AppTheme.colors.destructive, modifier = Modifier.padding(vertical = 10.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun Rule() {
    Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
}

@Composable
private fun Heading(text: String) {
    Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 18.dp)) {
        BoardLabel(text, size = 13.sp, spacing = 0.12.em, modifier = Modifier.padding(bottom = 8.dp))
        Rule()
    }
}

@Composable
private fun Fact(label: String, value: String?, detail: String, edge: Color?, modifier: Modifier) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(12.dp)).background(AppTheme.colors.card)
            .then(if (edge != null) Modifier.drawBehind { drawRect(edge, topLeft = Offset.Zero, size = Size(size.width, 3.dp.toPx())) } else Modifier)
            .padding(start = 12.dp, end = 12.dp, top = if (edge != null) 13.dp else 10.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        BoardLabel(label, size = 12.sp, spacing = 0.1.em)
        value?.let { Text(it, color = AppTheme.colors.ink, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp)) }
        if (detail.isNotBlank()) {
            BoardText(detail, color = if (value == null) AppTheme.colors.ink else AppTheme.colors.secondaryLabel, size = if (value == null) 14.sp else 12.sp, weight = if (value == null) FontWeight.SemiBold else FontWeight.Normal)
        }
    }
}
