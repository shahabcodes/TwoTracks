package com.shahaabapps.twotracks.feature.us

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.planner.GoalProgress
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import java.time.ZoneId
import java.util.Locale

/** A goal as a name, its hours, and a thin bar: the shape Together and Partner both draw. */
data class GoalBar(val title: String, val hours: String, val fraction: Float, val goalId: String? = null)

internal fun goalBar(p: GoalProgress, counted: Boolean): GoalBar = GoalBar(
    title = p.goal.title.substringBefore(" (").let { shortTitle(it) },
    hours = if (counted) "%.0f/%.0f h".format(Locale.ENGLISH, p.hoursLogged, p.hoursRequired) else p.targetDate?.let { "by ${it.dayOfMonth} ${it.month.name.take(3).lowercase().replaceFirstChar { c -> c.uppercase() }}" }.orEmpty(),
    fraction = if (counted) p.fraction.toFloat() else 0f,
    goalId = p.goal.id,
)

/** "AZ-204 Azure Developer Associate" reads as "AZ-204" in a narrow column; a plain name stays whole. */
internal fun shortTitle(title: String): String {
    val first = title.substringBefore(' ')
    return if (first.any { it.isDigit() } && first.contains('-')) first else title
}

/** The city a zone is named after, as people say it: "Asia/Qatar" is Doha. */
internal fun cityOf(zone: String): String {
    val raw = zone.substringAfterLast('/').replace('_', ' ')
    return when (raw) {
        "Qatar" -> "Doha"
        "Calcutta" -> "Kolkata"
        "UTC", "GMT" -> raw
        else -> raw
    }
}

internal fun zoneOrUtc(id: String): ZoneId = runCatching { ZoneId.of(id) }.getOrDefault(ZoneId.of("UTC"))

@Composable
fun GoalBarRow(bar: GoalBar, color: Color, modifier: Modifier = Modifier, big: Boolean = false) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(if (big) 6.dp else 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                bar.title, style = MaterialTheme.typography.bodySmall.copy(fontSize = if (big) 14.sp else 13.sp),
                color = AppTheme.colors.ink, maxLines = 1, modifier = Modifier.weight(1f),
            )
            Text(
                bar.hours.uppercase(Locale.ENGLISH).replace("/", if (big) " / " else "/"),
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = if (big) 12.sp else 11.sp),
                color = AppTheme.colors.secondaryLabel, maxLines = 1,
            )
        }
        Box(Modifier.fillMaxWidth().height(4.dp).background(AppTheme.colors.separator)) {
            if (bar.fraction > 0f) Box(Modifier.fillMaxWidth(bar.fraction.coerceIn(0.01f, 1f)).height(4.dp).background(color))
        }
    }
}
