package com.shahaabapps.twotracks.feature.us

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.core.planner.LevelState
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.BarlowFamily
import com.shahaabapps.core.ui.theme.CondensedFamily
import com.shahaabapps.twotracks.ui.Routes
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardTitle
import com.shahaabapps.twotracks.ui.guide.GuideAnchors
import com.shahaabapps.twotracks.ui.guide.guideAnchor
import java.util.Locale

/** "6 DAYS RUNNING · BOTH LINES", the together streak as the board says it. */
internal fun runningLine(streak: Int): String = when (streak) {
    0 -> "No run yet · both lines"
    1 -> "1 day running · both lines"
    else -> "$streak days running · both lines"
}.uppercase(Locale.ROOT)

/** "NOW · 2 OF 5" for the current stop, "ARRIVED" for one done, "LEVEL 3" for the rest. */
internal fun lineTag(lp: LevelProgress): String = when (lp.state) {
    LevelState.OPEN -> (if (lp.isOverdue) "Delayed" else "Now") + " · ${lp.requiredDone} of ${lp.requiredTotal}"
    LevelState.DONE -> "Arrived"
    LevelState.LOCKED -> "Level ${lp.level.number}"
}.uppercase(Locale.ROOT)

/** The initial a destination's chip carries for whoever leads it, or nothing for a rule such as "Higher scorer". */
internal fun leadMark(c: MoveRow): String = if (c.leadRole != null) c.lead.take(1).uppercase(Locale.ROOT) else ""

/**
 * Route on the Departures board, as its prototype draws it: the two of you
 * as passenger cards with your city and time, the run you share, both
 * ladders as two lines side by side down to the move as the terminal, the
 * destinations as chips in the colour of whoever leads them, and the
 * paperwork.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun BoardUs(state: UsUiState, onNavigate: (String) -> Unit, onOpenLevel: (String) -> Unit) {
    val me = state.me
    val partner = state.partner
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(inner), contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    BoardMono("TWO LINES · ONE TERMINAL")
                    BoardTitle("Route", size = 52.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
            if (me != null && partner != null) {
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 16.dp).height(IntrinsicSize.Min)) {
                        Passenger(me, mine = true, modifier = Modifier.weight(1f).fillMaxHeight()) { me.level?.let { onOpenLevel(it.level.id) } }
                        Passenger(partner, mine = false, modifier = Modifier.weight(1f).fillMaxHeight()) { onNavigate(Routes.PARTNER) }
                    }
                }
                item {
                    // The board has no room for the week's grid; the run opens both weeks, which draws it day by day.
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 10.dp).fillMaxWidth().heightIn(min = 32.dp)
                            .clip(RoundedCornerShape(8.dp)).clickable(role = Role.Button, onClickLabel = "Open both weeks") { onNavigate(Routes.BOTH_WEEKS) },
                    ) { BoardLabel(runningLine(state.streak), size = 14.sp, spacing = 0.12.em) }
                }
                item {
                    Row(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Box(
                            contentAlignment = Alignment.CenterEnd,
                            modifier = Modifier.guideAnchor(GuideAnchors.US_LEVELS).heightIn(min = 32.dp).clip(RoundedCornerShape(6.dp))
                                .clickable(role = Role.Button) { onNavigate(Routes.LEVELS) },
                        ) { BoardLabel("All stops ›", color = AppTheme.colors.accent, size = 14.sp, weight = FontWeight.Bold, spacing = 0.1.em) }
                    }
                }
                item { TwoLines(me, partner, onOpenLevel, onTerminal = { onNavigate(Routes.COUNTRIES) }) }
            }
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.weight(1f)) {
                            Box(Modifier.heightIn(min = 32.dp).clip(RoundedCornerShape(6.dp)).clickable(role = Role.Button) { onNavigate(Routes.COUNTRIES) }, contentAlignment = Alignment.CenterStart) {
                                BoardLabel("Destinations ›", size = 13.sp, spacing = 0.12.em)
                            }
                        }
                        state.nextDate?.let {
                            BoardLabel(it, color = state.nextDateRole?.let { r -> AppTheme.colors.track(r == PersonRole.TECH) } ?: AppTheme.colors.secondaryLabel, size = 13.sp, spacing = 0.12.em)
                        }
                    }
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        state.countries.forEach { c ->
                            val color = c.leadRole?.let { AppTheme.colors.track(it == PersonRole.TECH) } ?: AppTheme.colors.strongBorder
                            val shape = RoundedCornerShape(18.dp)
                            Row(
                                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.heightIn(min = 36.dp).clip(shape).border(1.5.dp, color, shape)
                                    .semantics { contentDescription = "${c.name}, ${c.lead} leads" }
                                    .clickable(role = Role.Button) { onNavigate(Routes.country(c.code)) }.padding(horizontal = 12.dp),
                            ) {
                                Text(c.name, color = AppTheme.colors.ink, style = TextStyle(fontFamily = BarlowFamily, fontSize = 14.sp, fontWeight = FontWeight.Medium))
                                val mark = leadMark(c)
                                if (mark.isNotEmpty()) BoardMono(mark, size = 10.sp, spacing = 0.sp)
                            }
                        }
                    }
                }
            }
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 20.dp)) {
                    Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                    Paper("Tests", state.tests) { onNavigate(Routes.TESTS) }
                    Paper("Documents", state.documents) { onNavigate(Routes.DOCUMENTS) }
                    Paper("Budget", state.budget) { onNavigate(Routes.BUDGET) }
                }
            }
        }
    }
}

@Composable
private fun Passenger(p: TogetherPerson, mine: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val color = AppTheme.colors.track(p.isTech)
    Column(
        modifier = modifier.clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.card)
            .drawBehind { drawRect(color, topLeft = Offset.Zero, size = Size(size.width, 3.dp.toPx())) }
            .clickable(role = Role.Button, onClick = onClick).padding(start = 14.dp, end = 14.dp, top = 15.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            p.name + if (mine) "" else " ›", color = AppTheme.colors.ink, maxLines = 1,
            style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
        )
        BoardMono(if (p.joined) p.place.replace(" · ", " ") else "NOT JOINED YET", spacing = 0.sp)
        p.missed?.let { BoardText(it, color = AppTheme.colors.warn, size = 12.sp) }
    }
}

/** Both ladders side by side, a stop a row, each rail lit up to where its person is. */
@Composable
private fun TwoLines(me: TogetherPerson, partner: TogetherPerson, onOpenLevel: (String) -> Unit, onTerminal: () -> Unit) {
    val mine = AppTheme.colors.track(me.isTech)
    val theirs = AppTheme.colors.track(partner.isTech)
    val rows = maxOf(me.ladder.size, partner.ladder.size)
    val myHere = me.ladder.indexOfFirst { it.state == LevelState.OPEN }.let { if (it < 0) me.ladder.count { l -> l.state == LevelState.DONE } - 1 else it }
    val theirHere = partner.ladder.indexOfFirst { it.state == LevelState.OPEN }.let { if (it < 0) partner.ladder.count { l -> l.state == LevelState.DONE } - 1 else it }
    Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        repeat(rows) { i ->
            val a = me.ladder.getOrNull(i)
            val b = partner.ladder.getOrNull(i)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).heightIn(min = 58.dp)) {
                Stop(a, TextAlign.End, i == myHere, mine, Modifier.weight(1f)) { a?.let { onOpenLevel(it.level.id) } }
                Rail(a, lit = i <= myHere, here = i == myHere, color = mine)
                Rail(b, lit = i <= theirHere, here = i == theirHere, color = theirs)
                Stop(b, TextAlign.Start, i == theirHere, theirs, Modifier.weight(1f)) { b?.let { onOpenLevel(it.level.id) } }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.width(120.dp).clip(RoundedCornerShape(12.dp)).border(2.dp, AppTheme.colors.ink, RoundedCornerShape(12.dp))
                .clickable(role = Role.Button, onClickLabel = "Open the destinations", onClick = onTerminal).padding(vertical = 10.dp),
        ) {
            BoardLabel("The move", color = AppTheme.colors.ink, size = 18.sp, weight = FontWeight.Bold, spacing = 0.1.em)
            BoardMono("TERMINAL", size = 10.sp)
        }
    }
}

@Composable
private fun Stop(lp: LevelProgress?, align: TextAlign, here: Boolean, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Column(
        horizontalAlignment = if (align == TextAlign.End) Alignment.End else Alignment.Start, verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier.fillMaxHeight().then(if (lp != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier).padding(top = 2.dp),
    ) {
        if (lp == null) return@Column
        Text(
            lp.level.name, color = if (here) AppTheme.colors.ink else AppTheme.colors.secondaryLabel, textAlign = align,
            style = TextStyle(fontFamily = BarlowFamily, fontSize = 14.sp, fontWeight = if (here) FontWeight.SemiBold else FontWeight.Medium),
        )
        val ink = when {
            lp.isOverdue -> AppTheme.colors.warn
            lp.state == LevelState.DONE -> AppTheme.colors.success
            here -> color
            else -> AppTheme.colors.secondaryLabel
        }
        BoardMono(lineTag(lp), color = ink, size = 10.sp, spacing = 0.sp, textAlign = align)
    }
}

@Composable
private fun Rail(lp: LevelProgress?, lit: Boolean, here: Boolean, color: Color) {
    Box(Modifier.width(22.dp).fillMaxHeight()) {
        if (lp == null) return@Box
        Box(Modifier.padding(start = 9.dp).width(4.dp).fillMaxHeight().background(if (lit) color else AppTheme.colors.separator))
        val filled = here || lp.state == LevelState.DONE
        Box(
            Modifier.padding(start = 3.dp, top = 3.dp).size(16.dp).clip(CircleShape)
                .background(if (filled) color else AppTheme.colors.groupedBackground).border(3.dp, color, CircleShape),
        )
    }
}

@Composable
private fun Paper(title: String, subtitle: String, onClick: () -> Unit) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp).clickable(role = Role.Button, onClick = onClick),
        ) {
            Column(modifier = Modifier.weight(1f).padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, color = AppTheme.colors.ink, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 19.sp))
                if (subtitle.isNotBlank()) BoardText(subtitle, size = 12.sp)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(18.dp))
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
    }
}
