package com.shahaabapps.twotracks.feature.levels

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.model.LevelItem
import com.shahaabapps.core.model.LevelItemKind
import com.shahaabapps.core.planner.LevelItemProgress
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.core.planner.LevelState
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.BarlowFamily
import com.shahaabapps.core.ui.theme.CondensedFamily
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.board.BoardBack
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardSegments
import com.shahaabapps.twotracks.ui.board.BoardStat
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardTitle
import com.shahaabapps.twotracks.ui.components.SwipeToDelete
import com.shahaabapps.twotracks.ui.guide.GuideAnchors
import com.shahaabapps.twotracks.ui.guide.guideAnchor
import java.time.temporal.ChronoUnit
import java.util.Locale

/** "NOW · 2 OF 5", "DELAYED · 2 OF 5", "ARRIVED" or "SCHEDULED", as a stop on the board stands. */
internal fun boardLevelState(lp: LevelProgress): String = when (lp.state) {
    LevelState.DONE -> "ARRIVED"
    LevelState.LOCKED -> "SCHEDULED"
    LevelState.OPEN -> (if (lp.isOverdue) "DELAYED" else "NOW") + " · ${lp.requiredDone} OF ${lp.requiredTotal}"
}

/** "STOP 01 · NOW BOARDING", or "SANYA'S STOP 01 · DELAYED" for your partner's. */
internal fun boardStopLine(lp: LevelProgress, owner: String?): String {
    val word = when {
        lp.state == LevelState.DONE -> "arrived"
        lp.isOverdue -> "delayed"
        lp.state == LevelState.OPEN -> "now boarding"
        else -> "scheduled"
    }
    return ((if (owner != null) "$owner's stop" else "Stop") + " %02d · $word".format(lp.level.number)).uppercase(Locale.ROOT)
}

/** "USAID · DEV", whose line a tab shows. */
internal fun whoseLine(name: String, isTech: Boolean): String = "$name · ${if (isTech) "Dev" else "Edu"}"

/**
 * The code on a goal's chip: the exam's own prefix where it has one
 * ("AZ-204" is AZ), the language's code for a language goal.
 */
internal fun goalCode(title: String): String {
    val t = title.trim()
    return when {
        t.startsWith("German", ignoreCase = true) || t.startsWith("Deutsch", ignoreCase = true) -> "DE"
        t.startsWith("IELTS", ignoreCase = true) -> "IE"
        t.startsWith("French", ignoreCase = true) -> "FR"
        else -> t.takeWhile { it.isLetterOrDigit() }.take(2).uppercase(Locale.ROOT).ifEmpty { "GO" }
    }
}

/** "12/120 H · BY 31 MAR 2027", the line under a goal on the manifest. */
internal fun boardGoalLine(ip: LevelItemProgress, state: LevelUiState): String {
    val item = ip.item
    val goal = item.goalId?.let { state.goalsById[it] }
    return buildList {
        if (ip.done) add("Passed")
        item.goalId?.let { state.goalHours[it] }?.takeIf { it.second > 0 }?.let { (done, need) -> add("$done/$need h") }
        (item.dueDateIso ?: goal?.targetDateIso)?.takeIf { it.isNotBlank() }?.let { add("by ${Clock.formatDate(it)}") }
    }.joinToString(" · ").uppercase(Locale.ENGLISH)
}

/**
 * Levels on the Departures board, as its prototype draws them: whose line
 * as two tabs in each person's colour, then each level as a stop on a rail,
 * the current one on a card, down to the move itself.
 */
@Composable
internal fun BoardLevels(state: LevelsUiState, onBack: () -> Unit, onOpenLevel: (String) -> Unit, onShowPartner: (Boolean) -> Unit) {
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(inner), contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(Modifier.height(44.dp), contentAlignment = Alignment.CenterStart) { BoardBack(onBack, "Back to Route") }
                    BoardMono(state.shown?.let { "${it.levels.size} stops · quarter by quarter".uppercase(Locale.ROOT) }.orEmpty())
                    BoardTitle("Levels", size = 52.sp)
                    val me = state.me
                    val partner = state.partner
                    if (me != null && partner != null) {
                        val mine = AppTheme.colors.track(me.isTech)
                        val theirs = AppTheme.colors.track(partner.isTech)
                        BoardSegments(
                            options = listOf(false to whoseLine(me.name.ifBlank { "You" }, me.isTech), true to whoseLine(partner.name, partner.isTech)),
                            selected = state.showPartner, onSelect = onShowPartner, colorOf = { if (it) theirs else mine },
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                }
            }
            val ladder = state.shown ?: return@LazyColumn
            item { Box(Modifier.height(14.dp)) }
            itemsIndexed(ladder.levels, key = { _, lp -> lp.level.id }) { i, lp ->
                BoardStop(
                    lp, color = AppTheme.colors.track(ladder.isTech), last = i == ladder.levels.lastIndex,
                    modifier = if (i == 0) Modifier.guideAnchor(GuideAnchors.LEVELS_LADDER) else Modifier,
                    onClick = { onOpenLevel(lp.level.id) },
                )
            }
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(start = BoardGutter + 36.dp).width(120.dp).border(2.dp, AppTheme.colors.ink, RoundedCornerShape(12.dp)).padding(vertical = 8.dp),
                ) { BoardLabel("The move", color = AppTheme.colors.ink, size = 16.sp, weight = FontWeight.Bold, spacing = 0.1.em) }
            }
        }
    }
}

@Composable
private fun BoardStop(lp: LevelProgress, color: Color, last: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val current = lp.state == LevelState.OPEN
    val ink = when {
        lp.state == LevelState.DONE -> AppTheme.colors.success
        lp.isOverdue -> AppTheme.colors.warn
        current -> color
        else -> AppTheme.colors.secondaryLabel
    }
    Row(
        modifier = modifier.fillMaxWidth().height(IntrinsicSize.Min).heightIn(min = 96.dp).clickable(onClick = onClick).padding(horizontal = BoardGutter),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(Modifier.width(22.dp).fillMaxHeight()) {
            Box(Modifier.padding(start = 9.dp).width(4.dp).fillMaxHeight().background(AppTheme.colors.separator))
            val lit = lp.state != LevelState.LOCKED
            Box(
                Modifier.padding(start = 1.dp, top = 16.dp).size(20.dp).clip(CircleShape)
                    .background(if (lit) ink else AppTheme.colors.groupedBackground)
                    .border(3.dp, if (lit) ink else AppTheme.colors.strongBorder, CircleShape),
            )
        }
        Column(
            modifier = Modifier.weight(1f).padding(vertical = 8.dp).clip(RoundedCornerShape(14.dp))
                .background(if (current) AppTheme.colors.card else Color.Transparent).padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "%02d · %s".format(lp.level.number, lp.level.name), color = AppTheme.colors.ink, modifier = Modifier.weight(1f),
                    style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 24.sp),
                )
                BoardLabel(boardLevelState(lp), color = ink, size = 12.sp, weight = FontWeight.Bold, spacing = 0.1.em, modifier = Modifier.padding(top = 4.dp))
            }
            if (lp.level.summary.isNotBlank()) BoardText(lp.level.summary, size = 13.sp)
            BoardMono(levelDates(lp, last), size = 10.sp)
        }
    }
}

/**
 * One level on the Departures board, as its prototype draws it: the stop
 * and whether it is boarding, its name and purpose, two figures and a
 * segment per required item, then its manifest of goals and things to
 * tick, and the ways to add to it.
 */
@Composable
internal fun BoardLevelBody(
    state: LevelUiState,
    snackbar: SnackbarHostState,
    swipeReset: Int,
    onBack: () -> Unit,
    clickOf: (LevelItem) -> (() -> Unit)?,
    removable: (LevelItem) -> Boolean,
    onRemove: (LevelItem) -> Unit,
    onAdd: () -> Unit,
    onStartNext: () -> Unit,
    onChooseBranch: (String) -> Unit,
) {
    val p = state.progress
    val color = AppTheme.colors.track(state.isTech)
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        Box(Modifier.fillMaxSize().padding(inner)) {
            LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
                item {
                    Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(Modifier.height(44.dp), contentAlignment = Alignment.CenterStart) { BoardBack(onBack, "Back to levels") }
                        if (p == null) return@Column
                        val ink = when {
                            p.state == LevelState.DONE -> AppTheme.colors.success
                            p.isOverdue -> AppTheme.colors.warn
                            p.state == LevelState.OPEN -> color
                            else -> AppTheme.colors.secondaryLabel
                        }
                        BoardLabel(boardStopLine(p, if (state.isMine) null else state.ownerName), color = ink, size = 14.sp, weight = FontWeight.Bold)
                        BoardTitle(p.level.name, size = 52.sp)
                        if (p.level.summary.isNotBlank()) BoardText(p.level.summary)
                    }
                }
                if (p == null) return@LazyColumn
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 14.dp).height(IntrinsicSize.Min)) {
                        BoardStat("${p.requiredDone}/${p.requiredTotal}", "Required cleared", color = color, modifier = Modifier.weight(1f).fillMaxHeight())
                        val days = p.daysLeft
                        val start = p.startDate
                        val m = Modifier.weight(1f).fillMaxHeight()
                        when {
                            p.state == LevelState.LOCKED && start != null ->
                                BoardStat("${ChronoUnit.DAYS.between(p.today, start).coerceAtLeast(0)}", "Days · opens ${Clock.formatDate(start)}", modifier = m)
                            p.state == LevelState.DONE ->
                                BoardStat("Arrived", p.level.completedAtIso?.let { "On ${Clock.formatDate(it.take(10))}" } ?: "Every required item", color = AppTheme.colors.success, modifier = m)
                            days != null && days >= 0 -> BoardStat("$days", "Days · due ${Clock.formatDate(p.level.dueDateIso)}", modifier = m)
                            days != null -> BoardStat("${-days}", "Days late · due ${Clock.formatDate(p.level.dueDateIso)}", color = AppTheme.colors.warn, modifier = m)
                        }
                    }
                }
                if (p.requiredTotal > 0) {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 10.dp)) {
                            repeat(p.requiredTotal) { i ->
                                Box(Modifier.weight(1f).height(5.dp).clip(RoundedCornerShape(2.dp)).background(if (i < p.requiredDone) color else AppTheme.colors.separator))
                            }
                        }
                    }
                }
                item {
                    Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 18.dp)) {
                        BoardLabel("Manifest", size = 13.sp, spacing = 0.12.em, modifier = Modifier.padding(bottom = 8.dp))
                        Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                    }
                }
                p.items.forEach { ip ->
                    item(key = ip.item.id) {
                        val row: @Composable () -> Unit = { ManifestRow(ip, color, state, clickOf(ip.item)) }
                        if (removable(ip.item)) {
                            key(ip.item.id, swipeReset) { SwipeToDelete(onDelete = { onRemove(ip.item) }) { row() } }
                        } else {
                            row()
                        }
                    }
                }
                if (p.level.number == 5 && state.isMine) {
                    item {
                        Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            BoardText("Which way? The other items stay visible but stop counting.", size = 13.sp)
                            BoardSegments(listOf("offer" to "Offer in hand", "backup" to "No offer yet"), selected = p.level.branch.orEmpty(), onSelect = onChooseBranch)
                        }
                    }
                }
                if (state.isMine) {
                    item {
                        Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            val shape = RoundedCornerShape(12.dp)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().height(50.dp).clip(shape).border(1.dp, AppTheme.colors.separator, shape).clickable(role = Role.Button, onClick = onAdd),
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = null, tint = AppTheme.colors.ink, modifier = Modifier.size(16.dp))
                                BoardLabel("Add a goal", color = AppTheme.colors.ink, size = 17.sp, weight = FontWeight.Bold, spacing = 0.1.em)
                            }
                            val next = state.next
                            if (next != null && next.state == LevelState.LOCKED) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxWidth().height(48.dp).clip(shape).clickable(role = Role.Button, onClick = onStartNext),
                                ) { BoardLabel("Board stop %02d early".format(next.level.number), color = AppTheme.colors.accent, size = 17.sp, weight = FontWeight.Bold, spacing = 0.1.em) }
                            }
                        }
                    }
                }
            }
            SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
private fun ManifestRow(ip: LevelItemProgress, color: Color, state: LevelUiState, onClick: (() -> Unit)?) {
    val item = ip.item
    val ticked = item.kind == LevelItemKind.TASK && ip.done
    Column(modifier = Modifier.fillMaxWidth().background(AppTheme.colors.groupedBackground).padding(horizontal = BoardGutter)) {
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp).then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.width(40.dp)) {
                when {
                    item.kind == LevelItemKind.TASK -> Tick(ip.done)
                    !item.required -> Chip("Opt", ink = AppTheme.colors.secondaryLabel, fill = Color.Transparent, dashed = AppTheme.colors.strongBorder)
                    item.kind == LevelItemKind.GOAL -> Chip(goalCode(item.title), ink = AppTheme.colors.onSolid, fill = color)
                    else -> Chip("${ip.weekCount ?: 0}", ink = color, fill = Color.Transparent, dashed = null, border = color)
                }
            }
            Column(modifier = Modifier.weight(1f).padding(vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    item.title, color = if (ticked) AppTheme.colors.secondaryLabel else AppTheme.colors.ink,
                    textDecoration = if (ticked) TextDecoration.LineThrough else null,
                    style = TextStyle(fontFamily = BarlowFamily, fontSize = 15.sp, fontWeight = if (item.required) FontWeight.SemiBold else FontWeight.Medium),
                )
                if (item.kind == LevelItemKind.GOAL) {
                    val line = boardGoalLine(ip, state)
                    if (line.isNotBlank()) BoardMono(line, size = 11.sp, spacing = 0.sp)
                    if (item.detail.isNotBlank()) BoardText(item.detail, size = 12.sp)
                } else {
                    val line = itemLine(ip, state)
                    if (line.isNotBlank()) BoardText(line, size = 12.sp)
                }
            }
            when {
                ticked || (item.kind == LevelItemKind.GOAL && ip.done) ->
                    BoardLabel(if (ticked) "Cleared" else "Passed", color = AppTheme.colors.success, size = 12.sp, weight = FontWeight.Bold, spacing = 0.1.em)
                item.kind == LevelItemKind.GOAL && onClick != null ->
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(16.dp))
            }
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
    }
}

@Composable
private fun Chip(text: String, ink: Color, fill: Color, dashed: Color? = null, border: Color? = null) {
    val shape = RoundedCornerShape(6.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.width(40.dp).height(24.dp).clip(shape).background(fill)
            .then(if (border != null) Modifier.border(1.dp, border, shape) else Modifier)
            .then(
                if (dashed != null) Modifier.drawBehind {
                    drawRoundRect(dashed, cornerRadius = CornerRadius(6.dp.toPx()), style = Stroke(width = 1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))))
                } else Modifier,
            ),
    ) { Text(text.uppercase(Locale.ROOT), color = ink, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 0.06.em)) }
}

@Composable
private fun Tick(done: Boolean) {
    val shape = RoundedCornerShape(5.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(22.dp).clip(shape).background(if (done) AppTheme.colors.success else Color.Transparent)
            .border(2.dp, if (done) AppTheme.colors.success else AppTheme.colors.strongBorder, shape),
    ) { if (done) Icon(Icons.Filled.Check, contentDescription = "Done", tint = AppTheme.colors.onSolid, modifier = Modifier.size(15.dp)) }
}
