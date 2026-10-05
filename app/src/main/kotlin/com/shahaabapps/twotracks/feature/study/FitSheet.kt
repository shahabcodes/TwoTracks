package com.shahaabapps.twotracks.feature.study

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.planner.FitPlanner
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoSecondaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.components.SheetGrabber
import com.shahaabapps.twotracks.ui.components.StatTile
import java.util.Locale

/**
 * Shown when a saved goal's hours no longer fit the level by its due date.
 * Offers the two computed ways out, or saving as is.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitSheet(fit: FitCheck, onChoose: (FitChoice) -> Unit, onDismiss: () -> Unit) {
    val r = fit.result
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = AppTheme.colors.groupedBackground, dragHandle = { SheetGrabber() }) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = Spacing.listHorizontal).navigationBarsPadding().padding(bottom = Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            Text("This does not fit yet", style = MaterialTheme.typography.displayLarge)
            Text(
                "${fmtHours(fit.addedHours)} more to study puts Level ${fit.levelNumber} · ${fit.levelName} over what your weekly template can hold by ${Clock.formatDate(fit.levelDueIso)}.",
                style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                StatTile(label = "Needs a week", value = fmtHours(r.neededWeeklyHours), modifier = Modifier.weight(1f))
                StatTile(label = "Template gives", value = fmtHours(r.weeklyCapacityHours), modifier = Modifier.weight(1f))
                StatTile(label = "Hours left", value = fmtHours(r.demandHours), modifier = Modifier.weight(1f))
            }

            r.extend?.let { ext ->
                CupertinoCard {
                    Text("Extend the plan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        "Level due ${Clock.formatDate(fit.levelDueIso)} becomes ${Clock.formatDate(ext.newDueDate)}. " +
                            "Later levels move ${ext.shiftDays} days too, so the order of the plan stays the same.",
                        style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
                    )
                    Spacer(Modifier.height(Spacing.sm))
                    CupertinoPrimaryButton(text = "Extend to ${Clock.formatDate(ext.newDueDate)}", onClick = { onChoose(FitChoice.EXTEND) }, modifier = Modifier.fillMaxWidth())
                }
            }

            r.moreHours?.let { more ->
                CupertinoCard {
                    Text("Study more each day", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    val sample = more.newBlocks.firstOrNull()
                    val sampleText = sample?.let { b ->
                        val oldEnd = b.endMinutes - more.extraMinutesPerDay
                        "${Dates.minutesToClock(b.startMinutes)} to ${Dates.minutesToClock(oldEnd)} becomes ${Dates.minutesToClock(b.startMinutes)} to ${Dates.minutesToClock(b.endMinutes)}"
                    }.orEmpty()
                    Text(
                        "Add ${more.extraMinutesPerDay} minutes to each study day: $sampleText. " +
                            "That is about ${fmtHours(more.extraWeeklyHours)} more a week and keeps every date as it is.",
                        style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
                    )
                    if (more.tooLong) {
                        Spacer(Modifier.height(Spacing.xs))
                        Text(
                            "That makes some days longer than ${FitPlanner.MAX_STUDY_MINUTES_PER_DAY / 60.0} hours. Extending the date, or adding a weekend block under Settings, is the kinder option.",
                            style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.warn,
                        )
                    }
                    Spacer(Modifier.height(Spacing.sm))
                    if (more.tooLong) {
                        CupertinoSecondaryButton(text = "Study ${more.extraMinutesPerDay} minutes more anyway", onClick = { onChoose(FitChoice.MORE_HOURS) }, modifier = Modifier.fillMaxWidth())
                    } else {
                        CupertinoPrimaryButton(text = "Study ${more.extraMinutesPerDay} minutes more a day", onClick = { onChoose(FitChoice.MORE_HOURS) }, modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            if (r.extend == null && r.moreHours == null) {
                Text(
                    "There are no study blocks in your weekly template, so nothing can be stretched. Add blocks under Settings, Weekly template.",
                    style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.warn,
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                CupertinoTextButton(text = "Save anyway, I will manage", onClick = { onChoose(FitChoice.SAVE_ANYWAY) })
            }
        }
    }
}

private fun fmtHours(h: Double): String =
    if (h >= 10) "%.0f h".format(Locale.ENGLISH, h) else "%.1f h".format(Locale.ENGLISH, h).replace(".0 h", " h")
