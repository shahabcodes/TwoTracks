package com.shahaabapps.twotracks.feature.partner

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.BarlowFamily
import com.shahaabapps.core.ui.theme.CondensedFamily
import com.shahaabapps.core.ui.theme.JetBrainsFamily
import com.shahaabapps.twotracks.feature.us.GoalBar
import com.shahaabapps.twotracks.ui.board.BoardBack
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardTitle
import java.time.format.DateTimeFormatter
import java.util.Locale

/** "The teacher · IELTS and German", from the role line and what the goals are. */
internal fun partnerLine(roleLine: String, goals: List<GoalBar>): String {
    val role = roleLine.substringBefore(" · ").lowercase(Locale.ENGLISH).replaceFirstChar { it.uppercase() }
    val names = goals.map { it.title.substringBefore(' ') }.filter { it.isNotBlank() }.distinct()
    val what = when (names.size) {
        0 -> ""
        1 -> names.first()
        else -> names.dropLast(1).joinToString(", ") + " and " + names.last()
    }
    return listOf(role, what).filter { it.isNotBlank() }.joinToString(" · ")
}

/** "KARACHI 11:12", the place half of the role line. */
internal fun partnerPlace(roleLine: String): String = roleLine.substringAfter(" · ", "").replace(" · ", " ")

/** An IELTS skill as the band tiles print it: LISTEN, READ, WRITE, SPEAK. */
internal fun bandName(name: String): String = when (val n = name.trim().lowercase(Locale.ENGLISH)) {
    "listening" -> "Listen"
    "reading" -> "Read"
    "writing" -> "Write"
    "speaking" -> "Speak"
    else -> n.take(6).replaceFirstChar { it.uppercase() }
}.uppercase(Locale.ENGLISH)

private val dayMonth = DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH)

/**
 * Your partner on the Departures board, as its prototype draws it: their
 * line on a chip in their colour with their city and time, the name in
 * capitals, their day as departures on their clock, the stop and the week
 * as two cards, their goals with bars, and the last practice paper as a
 * tile a skill.
 */
@Composable
internal fun BoardPartner(state: PartnerUiState, onBack: () -> Unit, onOpenLevel: (String) -> Unit, onOpenGoal: (String) -> Unit) {
    val color = AppTheme.colors.track(state.isTech)
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(inner), contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(Modifier.height(44.dp), contentAlignment = Alignment.CenterStart) { BoardBack(onBack, "Back to Route") }
                    if (!state.loaded) return@Column
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.height(26.dp).clip(RoundedCornerShape(6.dp)).background(color).padding(horizontal = 10.dp)) {
                            BoardLabel(if (state.isTech) "Line dev" else "Line edu", color = AppTheme.colors.onSolid, size = 15.sp, weight = FontWeight.Bold, spacing = 0.1.em)
                        }
                        Box(Modifier.weight(1f))
                        BoardMono(if (state.joined) partnerPlace(state.roleLine) else "NOT JOINED YET", spacing = 0.sp)
                    }
                    BoardTitle(state.name, size = 52.sp, modifier = Modifier.padding(top = 4.dp))
                    BoardText(partnerLine(state.roleLine, state.goals))
                }
            }
            if (!state.loaded) return@LazyColumn
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                        BoardLabel("Local", spacing = 0.12.em, modifier = Modifier.width(52.dp))
                        BoardLabel("Service", spacing = 0.12.em, modifier = Modifier.weight(1f))
                        BoardLabel("Status", spacing = 0.12.em, textAlign = TextAlign.End, modifier = Modifier.width(76.dp))
                    }
                    Rule()
                    if (state.sessions.isEmpty()) BoardText("No service today.", modifier = Modifier.padding(vertical = 14.dp))
                    state.sessions.forEach { s ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth().heightIn(min = 58.dp).then(if (s.goalId != null) Modifier.clickable(role = Role.Button) { onOpenGoal(s.goalId) } else Modifier),
                        ) {
                            Text(s.time, color = AppTheme.colors.ink, modifier = Modifier.width(52.dp), style = TextStyle(fontFamily = JetBrainsFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp))
                            Column(modifier = Modifier.weight(1f).padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    s.title, color = if (s.done) AppTheme.colors.secondaryLabel else AppTheme.colors.ink,
                                    textDecoration = if (s.done) TextDecoration.LineThrough else null,
                                    style = TextStyle(fontFamily = BarlowFamily, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
                                )
                                BoardText(s.detail, size = 12.sp)
                            }
                            BoardLabel(
                                if (s.done) "Departed" else "On time", color = if (s.done) AppTheme.colors.secondaryLabel else AppTheme.colors.success,
                                size = 14.sp, spacing = 0.06.em, textAlign = TextAlign.End, modifier = Modifier.width(76.dp),
                            )
                        }
                        Rule()
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 16.dp).height(IntrinsicSize.Min)) {
                    val lp = state.level
                    Figure(
                        lp?.let { "Stop %02d · %s".format(it.level.number, it.level.name) } ?: "Levels",
                        lp?.let { "${it.requiredDone}/${it.requiredTotal}" } ?: "–",
                        lp?.daysLeft?.let { d -> if (d >= 0) "$d days, to ${lp.dueDate?.format(dayMonth)}" else "${-d} days late" }.orEmpty(),
                        color, detailInk = if ((lp?.daysLeft ?: 0) < 0) AppTheme.colors.warn else AppTheme.colors.secondaryLabel,
                        modifier = Modifier.weight(1f).fillMaxHeight(), onClick = lp?.let { { onOpenLevel(it.level.id) } },
                    )
                    Figure("This week", state.weekHours.replace(" h", "h"), state.weekLine, color, modifier = Modifier.weight(1f).fillMaxHeight())
                }
            }
            // Not in the prototype: the pace sentence, as the Parallel page keeps it.
            if (state.status.isNotBlank()) {
                item {
                    val ink = if (state.statusGood) AppTheme.colors.success else AppTheme.colors.warn
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 10.dp)) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(ink))
                        BoardText(state.status, color = ink, size = 13.sp, weight = FontWeight.SemiBold)
                    }
                }
            }
            if (state.goals.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 18.dp)) {
                        BoardLabel("${state.name}'s goals", size = 13.sp, spacing = 0.12.em, modifier = Modifier.padding(bottom = 8.dp))
                        Rule()
                        state.goals.forEach { g ->
                            Column(
                                modifier = Modifier.fillMaxWidth().then(if (g.goalId != null) Modifier.clickable(role = Role.Button) { onOpenGoal(g.goalId) } else Modifier).padding(vertical = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                Row {
                                    BoardText(g.title, color = AppTheme.colors.ink, size = 14.sp, weight = FontWeight.Medium, modifier = Modifier.weight(1f))
                                    BoardMono(g.hours.replace(" h", "H").uppercase(Locale.ENGLISH), size = 12.sp, spacing = 0.sp)
                                }
                                Box(Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(3.dp)).background(AppTheme.colors.separator)) {
                                    if (g.fraction > 0f) Box(Modifier.fillMaxWidth(g.fraction.coerceIn(0.01f, 1f)).height(5.dp).clip(RoundedCornerShape(3.dp)).background(color))
                                }
                            }
                            Rule()
                        }
                    }
                }
            }
            state.paper?.let { paper ->
                item {
                    Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 18.dp)) {
                        BoardLabel("Last practice paper · ${paper.date}", size = 13.sp, spacing = 0.12.em, modifier = Modifier.padding(bottom = 8.dp))
                        Rule()
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = 10.dp)) {
                            paper.bands.forEach { b ->
                                val ink = if (b.need == null) AppTheme.colors.ink else if (b.met) AppTheme.colors.success else AppTheme.colors.warn
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp),
                                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp)).background(AppTheme.colors.card).padding(8.dp),
                                ) {
                                    Text(b.score, color = ink, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp))
                                    BoardLabel(bandName(b.name), color = AppTheme.colors.ink, size = 12.sp, spacing = 0.08.em)
                                    b.need?.let { BoardLabel(if (b.met) "Met" else "Needs $it", color = ink, size = 11.sp, weight = FontWeight.Bold, spacing = 0.06.em) }
                                }
                            }
                        }
                        if (paper.note.isNotBlank()) BoardText(paper.note, size = 13.sp, modifier = Modifier.padding(top = 10.dp))
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
private fun Figure(label: String, value: String, detail: String, color: Color, modifier: Modifier, detailInk: Color = AppTheme.colors.secondaryLabel, onClick: (() -> Unit)? = null) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.card)
            .drawBehind { drawRect(color, topLeft = Offset.Zero, size = Size(size.width, 3.dp.toPx())) }
            .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier)
            .padding(start = 14.dp, end = 14.dp, top = 15.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        BoardLabel(label, size = 12.sp, spacing = 0.1.em)
        Text(value, color = AppTheme.colors.ink, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp))
        if (detail.isNotBlank()) BoardText(detail, color = detailInk, size = 12.sp)
    }
}
