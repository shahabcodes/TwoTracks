package com.shahaabapps.twotracks.feature.study

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.planner.FitPlanner
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.components.SheetGrabber
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Shown when a saved goal's hours no longer fit the level by its due date,
 * as the prototype draws it: what is needed against what the study blocks
 * hold, then the ways out as a choice, and one button that does the chosen
 * one.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitSheet(fit: FitCheck, onChoose: (FitChoice) -> Unit, onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = AppTheme.colors.groupedBackground, dragHandle = { SheetGrabber() }) {
        FitContent(fit, onChoose, onDismiss)
    }
}

/** One way out: what it is called, what it does, and what its button says. */
internal data class FitOption(val choice: FitChoice, val title: String, val detail: String, val cta: String)

internal fun fitOptions(fit: FitCheck): List<FitOption> {
    val r = fit.result
    return buildList {
        r.extend?.let { ext ->
            add(
                FitOption(
                    FitChoice.EXTEND, "Move the level to ${Clock.formatDate(ext.newDueDate)}",
                    "${weeksWord(ext.shiftDays)} more. Later levels move with it, so the order of the plan stays the same.",
                    "Move it and save",
                ),
            )
        }
        r.moreHours?.let { more ->
            val sample = more.newBlocks.firstOrNull()?.let { b ->
                val oldEnd = b.endMinutes - more.extraMinutesPerDay
                " ${Dates.minutesToClock(b.startMinutes)} to ${Dates.minutesToClock(oldEnd)} becomes ${Dates.minutesToClock(b.startMinutes)} to ${Dates.minutesToClock(b.endMinutes)}."
            }.orEmpty()
            val warning = if (more.tooLong) " Some days run past ${FitPlanner.MAX_STUDY_MINUTES_PER_DAY / 60} hours, so moving the date is the kinder option." else ""
            add(FitOption(FitChoice.MORE_HOURS, "Add ${more.extraMinutesPerDay} minutes to each study day", "About ${fmtHours(more.extraWeeklyHours)} more a week, and every date stays.$sample$warning", "Lengthen and save"))
        }
        add(FitOption(FitChoice.SAVE_ANYWAY, "Save anyway", "Keep the date and the blocks. Today will tell you each week how far behind it is.", "Save anyway"))
    }
}

private fun weeksWord(days: Long): String {
    val weeks = ((days + 6) / 7).toInt()
    val n = listOf("No", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten").getOrNull(weeks) ?: "$weeks"
    return if (weeks < 2 && days < 7) "$days day${if (days == 1L) "" else "s"}".replaceFirstChar { it.uppercase() } else "$n week${if (weeks == 1) "" else "s"}"
}

private val dayMonth = DateTimeFormatter.ofPattern("d MMMM", Locale.ENGLISH)

@Composable
internal fun FitContent(fit: FitCheck, onChoose: (FitChoice) -> Unit, onBack: () -> Unit) {
    val r = fit.result
    val options = fitOptions(fit)
    var picked by remember(fit) { mutableStateOf(0) }
    val available = r.weeklyCapacityHours * r.weeksLeft
    val due = Dates.parseOrNull(fit.levelDueIso)
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = Spacing.listHorizontal).navigationBarsPadding().padding(top = 10.dp, bottom = 36.dp),
    ) {
        Text("LEVEL ${fit.levelNumber} · ${fit.levelName}".uppercase(Locale.ENGLISH), style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.warn)
        Text(
            "That won't fit by ${due?.format(dayMonth) ?: Clock.formatDate(fit.levelDueIso)}",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp, lineHeight = 31.sp), fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            "Adding ${fmtHours(fit.addedHours)} leaves the level ${fmtHours(r.demandHours)} of work, and your study blocks hold ${fmtHours(available)} between now and its due date.",
            style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = 10.dp),
        )
        Column(modifier = Modifier.padding(top = 18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            val most = maxOf(r.demandHours, available, 0.1)
            Bar("Needed", r.demandHours, (r.demandHours / most).toFloat(), AppTheme.colors.ink)
            Bar("Available", available, (available / most).toFloat(), AppTheme.colors.separator)
        }
        Column(modifier = Modifier.padding(top = 22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            options.forEachIndexed { i, o ->
                val on = picked == i
                val shape = RoundedCornerShape(14.dp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth().clip(shape).border(if (on) 2.dp else Spacing.rule, if (on) AppTheme.colors.ink else AppTheme.colors.separator, shape)
                        .clickable(role = Role.RadioButton) { picked = i }.padding(horizontal = 16.dp, vertical = 14.dp),
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(top = 2.dp).size(20.dp).border(2.dp, AppTheme.colors.ink, CircleShape)) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(if (on) AppTheme.colors.ink else Color.Transparent))
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(o.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text(o.detail, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 28.dp)) {
            val shape = RoundedCornerShape(12.dp)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f).height(52.dp).clip(shape).border(Spacing.rule, AppTheme.colors.separator, shape).clickable(role = Role.Button, onClick = onBack),
            ) { Text("Back", style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp), color = AppTheme.colors.ink) }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1.6f).height(52.dp).clip(shape).background(AppTheme.colors.accent).clickable(role = Role.Button) { onChoose(options[picked].choice) },
            ) { Text(options[picked].cta, style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp), color = AppTheme.colors.onSolid) }
        }
    }
}

@Composable
private fun Bar(label: String, hours: Double, fraction: Float, color: Color) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel, modifier = Modifier.weight(1f))
        Text(fmtHours(hours).uppercase(Locale.ENGLISH), style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
    }
    Box(Modifier.fillMaxWidth(fraction.coerceIn(0.02f, 1f)).height(8.dp).background(color))
}

internal fun fmtHours(h: Double): String =
    if (h >= 10) "%.0f h".format(Locale.ENGLISH, h) else "%.1f h".format(Locale.ENGLISH, h).replace(".0 h", " h")
