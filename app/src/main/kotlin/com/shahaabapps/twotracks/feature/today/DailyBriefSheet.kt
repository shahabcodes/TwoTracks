package com.shahaabapps.twotracks.feature.today

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import com.shahaabapps.core.ui.theme.CondensedFamily
import com.shahaabapps.twotracks.ui.board.BoardOutlineButton
import com.shahaabapps.twotracks.ui.board.BoardButton
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardTitle
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardGutter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoSecondaryButton
import com.shahaabapps.core.ui.components.SectionLabel
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Shapes
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.SheetGrabber

/** What the brief says: built once a day by the Today view model. */
data class DailyBrief(
    val greeting: String,
    val dateLine: String,
    /** The headline: what to do today. */
    val task: String,
    val taskDetail: String,
    /** Why it matters, in order: this goal, this level, the move. */
    val reasons: List<BriefReason>,
    val streakLine: String,
    val isRestDay: Boolean,
    /** When today's one thing starts, on the reader's clock; the board prints it beside the task. */
    val time: String? = null,
)

/**
 * The morning sheet, as the Parallel prototype draws it: the date, the
 * greeting, today's one thing under an ink rule with its kind in your colour,
 * then why it matters, a line each with a mono label and a bar. Shown once a
 * day on open.
 *
 * [color] is the reader's track colour. The move is the two of you, so its
 * line is drawn in ink rather than in anyone's colour.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyBriefSheet(
    brief: DailyBrief,
    color: Color,
    onStart: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val departures = AppTheme.departures
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = if (departures) AppTheme.colors.card else AppTheme.colors.groupedBackground,
        shape = Shapes.sheet,
        dragHandle = { SheetGrabber() },
    ) {
        if (departures) {
            BoardBrief(brief, onStart, onDismiss)
            return@ModalBottomSheet
        }
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = Spacing.listHorizontal).padding(bottom = Spacing.md).navigationBarsPadding(),
        ) {
            SectionLabel(brief.dateLine, modifier = Modifier.padding(top = 14.dp))
            Text(brief.greeting, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(top = 8.dp).semantics { heading() })

            Column(modifier = Modifier.padding(top = 20.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
                SectionLabel(if (brief.isRestDay) "Rest day" else "Today's one thing", color = color, modifier = Modifier.padding(top = 12.dp))
                Text(brief.task, style = MaterialTheme.typography.headlineMedium)
                Text(brief.taskDetail, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(bottom = 12.dp))
                HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
            }

            Text("Why it matters", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 18.dp, bottom = 6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                brief.reasons.forEach { (kind, text) ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.height(IntrinsicSize.Min)) {
                        Box(Modifier.width(3.dp).fillMaxHeight().background(if (kind == BriefKind.MOVE) AppTheme.colors.ink else color))
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            SectionLabel(BriefLabels.of(kind))
                            Text(text, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            Text(
                brief.streakLine,
                style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
                modifier = Modifier.padding(top = 18.dp, bottom = 20.dp),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CupertinoSecondaryButton(text = "Later", onClick = onDismiss, modifier = Modifier.weight(1f))
                CupertinoPrimaryButton(text = if (brief.isRestDay) "Got it" else "Start ${brief.task}", onClick = onStart, modifier = Modifier.weight(2f))
            }
        }
    }
}

/**
 * The mono label for each kind of brief line.
 *
 * The lines themselves are built in [DailyBriefs], which knows nothing about
 * Compose; that is what lets them be tested. This is the only place the two
 * meet.
 */
object BriefLabels {
    fun of(kind: BriefKind): String = when (kind) {
        BriefKind.GOAL -> "Goal"
        BriefKind.LEVEL -> "Level"
        BriefKind.LEARN -> "Learn"
        BriefKind.MOVE -> "The move"
    }
}

/**
 * The brief on the Departures board, as its prototype draws it: the date,
 * the greeting in capitals, today's one departure with its time in yellow,
 * why this one as rows of the board, and LATER beside BOARD NOW.
 */
@Composable
private fun BoardBrief(brief: DailyBrief, onStart: () -> Unit, onDismiss: () -> Unit) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = BoardGutter).padding(bottom = 24.dp).navigationBarsPadding()) {
        BoardMono(brief.dateLine.uppercase(java.util.Locale.ROOT), modifier = Modifier.padding(top = 14.dp))
        BoardTitle(brief.greeting.removePrefix("Good "), size = 40.sp, modifier = Modifier.padding(top = 6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth().height(IntrinsicSize.Min).clip(RoundedCornerShape(16.dp)).background(AppTheme.colors.groupedBackground),
        ) {
            Box(Modifier.width(4.dp).fillMaxHeight().background(AppTheme.colors.accent))
            Column(Modifier.weight(1f).padding(vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                BoardLabel(if (brief.isRestDay) "No departures today" else "Today's one departure", color = AppTheme.colors.accent, size = 13.sp, weight = FontWeight.Bold)
                Text(brief.task, color = AppTheme.colors.ink, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 28.sp))
                BoardText(brief.taskDetail, size = 13.sp)
            }
            brief.time?.let { BoardMono(it, color = AppTheme.colors.accent, size = 22.sp, weight = FontWeight.Bold, spacing = 0.sp, modifier = Modifier.padding(end = 14.dp)) }
        }
        Column(modifier = Modifier.padding(top = 16.dp)) {
            BoardLabel("Why this one", size = 13.sp, modifier = Modifier.padding(bottom = 6.dp))
            Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
            brief.reasons.forEach { (kind, text) ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(vertical = 9.dp)) {
                    BoardLabel(BriefLabels.of(kind), color = if (kind == BriefKind.MOVE) AppTheme.colors.teacher else AppTheme.colors.accent, size = 13.sp, weight = FontWeight.Bold, spacing = 0.1.em, modifier = Modifier.width(74.dp))
                    BoardText(text, color = AppTheme.colors.ink.copy(alpha = 0.92f), size = 13.sp, modifier = Modifier.weight(1f))
                }
                Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
            }
        }
        BoardLabel(brief.streakLine, size = 15.sp, spacing = 0.08.em, modifier = Modifier.padding(top = 12.dp, bottom = 24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            BoardOutlineButton("Later", onClick = onDismiss, height = 52.dp, modifier = Modifier.weight(1f))
            BoardButton(if (brief.isRestDay) "Got it" else "Board now", onClick = onStart, arrow = !brief.isRestDay, modifier = Modifier.weight(2f))
        }
    }
}
