package com.shahaabapps.twotracks.feature.today

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.twotracks.feature.us.cityOf
import com.shahaabapps.twotracks.feature.us.shortTitle
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.board.BoardButton
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardTitle
import com.shahaabapps.twotracks.ui.board.FlipTiles
import com.shahaabapps.twotracks.ui.board.WhoMark
import java.time.format.DateTimeFormatter
import java.util.Locale

/** How a row on the board stands. */
internal enum class Departure(val word: String) { DEPARTED("Departed"), BOARDING("Boarding"), ON_TIME("On time"), CANCELLED("Cancelled") }

internal fun departureOf(row: SessionRow, isNext: Boolean): Departure = when {
    row.session.status == SessionStatus.DONE -> Departure.DEPARTED
    row.session.status == SessionStatus.SKIPPED -> Departure.CANCELLED
    isNext -> Departure.BOARDING
    else -> Departure.ON_TIME
}

private fun countWord(n: Int) = listOf("No", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten").getOrNull(n) ?: "$n"

/** "Two of today's four are yours." */
internal fun boardCountLine(mine: Int, total: Int): String = when {
    total == 0 -> "Nothing on the board today."
    mine == total -> "All of today's ${countWord(total).lowercase()} are yours."
    else -> "${countWord(mine)} of today's ${countWord(total).lowercase()} ${if (mine == 1) "is" else "are"} yours."
}

/** "2 of 5 stops passed on your line. Next stop: a German-style CV." */
internal fun routeLine(lp: LevelProgress): String {
    val next = lp.items.firstOrNull { it.item.required && !it.done }?.item?.title?.takeIf { it.isNotBlank() }
    return "${lp.requiredDone} of ${lp.requiredTotal} stops passed on your line." + (next?.let { " Next stop: $it." } ?: "")
}

/**
 * Today as the Departures prototype draws it: the board for both of you,
 * the next departure on flip tiles with BOARD NOW, every session of the day
 * as a row of the board, and your level as stops on a line.
 */
@Composable
internal fun BoardTodayBody(
    state: TodayUiState,
    onOpenLevel: (String) -> Unit,
    onOpenWeek: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenTopic: (String, String) -> Unit,
    onOpenQuiz: (String, String) -> Unit,
    onOpenBrief: () -> Unit,
    onDone: (SessionRow) -> Unit,
    onTap: (SessionRow, Boolean) -> Unit,
) {
    val me = state.me
    val partner = state.partner?.takeIf { it.profile != null }
    val meIsTech = me?.role != PersonRole.TEACHER
    val mine = AppTheme.colors.track(meIsTech)
    val theirs = AppTheme.colors.track(!meIsTech)
    val next = me?.sessions?.firstOrNull { it.session.status == SessionStatus.PLANNED }
    val entries = buildList {
        me?.sessions?.forEach { add(it to true) }
        partner?.sessions?.forEach { add(it to false) }
    }.sortedBy { it.first.readerStartMinutes }

    LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            Column(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(start = BoardGutter, end = BoardGutter, top = 18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.heightIn(min = 28.dp).clickable(role = Role.Button, onClickLabel = "Open the brief", onClick = onOpenBrief),
                    ) {
                        BoardMono((state.today.format(DateTimeFormatter.ofPattern("EEE d MMM", Locale.ENGLISH)) + " · brief").uppercase(Locale.ROOT))
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(12.dp))
                    }
                    Spacer(Modifier.weight(1f))
                    if (me != null) BoardMono("${cityOf(me.profile?.timezone ?: "UTC").uppercase(Locale.ROOT)} ${me.localTime}")
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    BoardTitle("Departures", size = 52.sp, modifier = Modifier.weight(1f))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.offset(x = 10.dp).size(44.dp).clip(CircleShape).clickable(role = Role.Button, onClickLabel = "Settings", onClick = onOpenSettings),
                    ) { Icon(Icons.Outlined.Settings, contentDescription = "Settings", tint = AppTheme.colors.ink, modifier = Modifier.size(22.dp)) }
                }
                val hour = Clock.now(me?.profile).hour
                // The whole day's board, gone and still to come, as the prototype counts it.
                val planned = entries.count { it.first.session.status != SessionStatus.SKIPPED }
                val minePlanned = entries.count { it.second && it.first.session.status != SessionStatus.SKIPPED }
                BoardText(
                    DailyBriefs.greeting(me?.name ?: "you", hour) + ". " + boardCountLine(minePlanned, planned),
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }

        if (me != null) item { NextDeparture(next, me.isRestDay, onOpenTopic, onOpenQuiz, onDone) }

        item {
            Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                    BoardLabel("Time", modifier = Modifier.width(52.dp), spacing = 0.12.em)
                    BoardLabel("Service", modifier = Modifier.weight(1f), spacing = 0.12.em)
                    Text("Week ›", onOpenWeek)
                }
                Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                if (entries.isEmpty()) {
                    BoardLabel("No service today · anything either of you reads still counts", size = 15.sp, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(vertical = 16.dp))
                }
                entries.forEach { (row, isMine) ->
                    BoardRowLine(
                        row, isMine, departureOf(row, isMine && row == next), if (isMine) mine else theirs,
                        owner = if (isMine) me?.name.orEmpty() else partner?.name.orEmpty(),
                        ownerCity = if (isMine) null else cityOf(partner?.profile?.timezone ?: "UTC"),
                        onClick = { onTap(row, isMine) },
                    )
                }
            }
        }

        state.level?.let { lp -> item { Route(lp, mine, onOpenLevel) } }
    }
}

@Composable
private fun Text(text: String, onClick: () -> Unit) {
    BoardLabel(text, color = AppTheme.colors.accent, spacing = 0.12.em, modifier = Modifier.clip(RoundedCornerShape(4.dp)).clickable(role = Role.Button, onClick = onClick))
}

@Composable
private fun NextDeparture(row: SessionRow?, restDay: Boolean, onOpenTopic: (String, String) -> Unit, onOpenQuiz: (String, String) -> Unit, onDone: (SessionRow) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 18.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(AppTheme.colors.card).padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BoardLabel(if (row != null) "Next · boarding at" else "No more departures", color = AppTheme.colors.accent, size = 14.sp, spacing = 0.12.em, modifier = Modifier.weight(1f))
            if (row != null) BoardMono(if (row.nextTopic != null || row.review != null) "GATE · LEARN" else "GATE · ${row.componentName.uppercase(Locale.ROOT).take(14)}")
        }
        if (row != null) {
            FlipTiles(Dates.minutesToClock(row.session.startMinutes))
            val topic = row.nextTopic
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                androidx.compose.material3.Text(
                    shortTitle(row.goal?.title?.substringBefore(" (") ?: "Study") + " · " + (topic?.title?.substringAfter(": ")?.takeIf { it.isNotBlank() } ?: row.componentName),
                    color = AppTheme.colors.ink, style = MaterialThemeCondensed(26),
                )
                BoardText(
                    when {
                        topic != null -> "${row.session.durationMinutes} min · read ${topic.minutes} min, then the checks"
                        row.review != null -> "${row.session.durationMinutes} min · ${row.review.count} missed question${if (row.review.count == 1) "" else "s"} to bring back"
                        else -> "${row.session.durationMinutes} min · ${row.componentName}, minutes count themselves"
                    },
                    size = 14.sp,
                )
            }
            when {
                topic != null -> BoardButton("Board now", onClick = { onOpenTopic(topic.courseId, topic.topicId) }, height = 50.dp)
                row.review != null -> BoardButton("Board now", onClick = { onOpenQuiz(row.review.courseId, Courses.REVIEW_MODULE_ID) }, height = 50.dp)
                else -> BoardButton("Mark departed", onClick = { onDone(row) }, arrow = false, height = 50.dp)
            }
        } else {
            BoardText(if (restDay) "A rest day. Anything you read or review still counts." else "Every one of today's services has gone. Anything you read or review still counts.")
        }
    }
}

@Composable
private fun MaterialThemeCondensed(size: Int) = androidx.compose.ui.text.TextStyle(
    fontFamily = com.shahaabapps.core.ui.theme.CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = size.sp, lineHeight = (size + 2).sp, letterSpacing = 0.01.em,
)

@Composable
private fun BoardRowLine(row: SessionRow, mine: Boolean, status: Departure, color: Color, owner: String, ownerCity: String?, onClick: () -> Unit) {
    val gone = status == Departure.DEPARTED || status == Departure.CANCELLED
    val ink = if (gone) AppTheme.colors.secondaryLabel else AppTheme.colors.ink
    val statusInk = when (status) {
        Departure.DEPARTED -> AppTheme.colors.secondaryLabel
        Departure.CANCELLED -> AppTheme.colors.destructive
        Departure.BOARDING -> AppTheme.colors.accent
        Departure.ON_TIME -> AppTheme.colors.success
    }
    Column(modifier = Modifier.clickable(role = Role.Button, onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().heightIn(min = 58.dp).padding(vertical = 6.dp)) {
            BoardMono(
                Dates.minutesToClock(row.readerStartMinutes), size = 15.sp, weight = FontWeight.Bold, spacing = 0.sp,
                color = if (status == Departure.BOARDING) AppTheme.colors.accent else ink, modifier = Modifier.width(52.dp),
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                BoardText(listOfNotNull(row.goal?.title?.substringBefore(" (")?.let(::shortTitle), row.componentName.takeIf { it.isNotBlank() }).joinToString(" · ").ifBlank { "Study" }, color = ink, weight = FontWeight.SemiBold)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    WhoMark(color)
                    BoardText(
                        if (ownerCity != null) "$owner · ${row.timeLabel.substringBefore(" ")} $ownerCity" else "$owner · ${row.session.durationMinutes} min",
                        size = 12.sp,
                    )
                }
            }
            BoardLabel(status.word, color = statusInk, size = 14.sp, spacing = 0.06.em, modifier = Modifier.width(76.dp), textAlign = androidx.compose.ui.text.style.TextAlign.End)
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
    }
}

@Composable
private fun Route(lp: LevelProgress, color: Color, onOpenLevel: (String) -> Unit) {
    Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BoardLabel(
                "Route · level ${lp.level.number} ${lp.level.name} ›", size = 13.sp, spacing = 0.1.em,
                modifier = Modifier.weight(1f).heightIn(min = 24.dp).clip(RoundedCornerShape(4.dp)).clickable(role = Role.Button, onClickLabel = "Open the level") { onOpenLevel(lp.level.id) },
            )
            lp.daysLeft?.let { BoardLabel(if (it >= 0) "$it days" else "${-it} days late", size = 13.sp, spacing = 0.1.em, color = if (it >= 0) AppTheme.colors.secondaryLabel else AppTheme.colors.warn) }
        }
        val stops = lp.requiredTotal.coerceIn(1, 10)
        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(stops) { i ->
                val passed = i < lp.requiredDone
                Box(
                    Modifier.size(14.dp).clip(CircleShape)
                        .then(if (passed) Modifier.background(color) else Modifier.border(2.dp, AppTheme.colors.strongBorder, CircleShape)),
                )
                if (i < stops - 1) Box(Modifier.weight(1f).height(3.dp).background(if (i < lp.requiredDone - 1) color else AppTheme.colors.separator))
            }
        }
        BoardText(routeLine(lp), size = 13.sp)
    }
}
