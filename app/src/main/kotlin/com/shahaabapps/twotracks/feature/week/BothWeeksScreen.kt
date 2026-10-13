package com.shahaabapps.twotracks.feature.week

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyLog
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.PlanStore
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

/** One person's last seven days, as Both weeks shows them. */
data class PersonWeek(
    val role: PersonRole,
    val name: String,
    val totalMinutes: Int,
    /** "AZ-204 5.5 · German 2.9": hours by goal, largest first. */
    val byGoal: String,
    /** Minutes on each of the seven days, oldest first. */
    val minutesByDay: List<Int>,
)

data class BothWeeksUiState(
    val days: List<LocalDate> = emptyList(),
    val me: PersonWeek? = null,
    val partner: PersonWeek? = null,
    val togetherStreak: Int = 0,
    val partnerName: String = "",
)

@HiltViewModel
class BothWeeksViewModel @Inject constructor(planStore: PlanStore, dataStore: CoupleDataStore) : ViewModel() {
    val uiState: StateFlow<BothWeeksUiState> = combine(planStore.plan, dataStore.logs) { plan, logs ->
        if (plan == null) return@combine BothWeeksUiState()
        val today = plan.me.today
        val days = (6 downTo 0).map { today.minusDays(it.toLong()) }
        fun week(role: PersonRole, name: String, goals: List<StudyGoal>) =
            personWeek(role, name, days, logs, goals.associate { it.id to it.title })
        BothWeeksUiState(
            days = days,
            me = week(plan.me.role, plan.me.name, plan.me.goals),
            partner = plan.session.profileFor(plan.partner.role)?.let { week(plan.partner.role, plan.partner.name, plan.partner.goals) },
            togetherStreak = plan.togetherStreak,
            partnerName = plan.partner.name,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BothWeeksUiState())
}

/** Seven days of one person's logs, totalled by day and by goal. Pure, so it is tested. */
internal fun personWeek(role: PersonRole, name: String, days: List<LocalDate>, logs: List<StudyLog>, goalTitles: Map<String, String>): PersonWeek {
    val isos = days.map(Dates::format)
    val mine = logs.filter { it.owner == role && it.dateIso in isos }
    val byGoal = mine.groupBy { goalTitles[it.goalId] ?: "Other" }
        .mapValues { (_, l) -> l.sumOf { it.minutes } }
        .filterValues { it > 0 }
        .entries.sortedByDescending { it.value }
        .joinToString(" · ") { (title, m) -> "$title %.1f".format(m / 60.0) }
    return PersonWeek(
        role = role, name = name,
        totalMinutes = mine.sumOf { it.minutes },
        byGoal = byGoal,
        minutesByDay = isos.map { iso -> mine.filter { it.dateIso == iso }.sumOf { it.minutes } },
    )
}

/** "1h 15m", "45m", or a dash for nothing. */
internal fun minutesShort(m: Int): String = when {
    m <= 0 -> "—"
    m < 60 -> "${m}m"
    m % 60 == 0 -> "${m / 60}h"
    else -> "${m / 60}h ${m % 60}m"
}

/**
 * Both weeks, as the Parallel prototype draws it: the last seven days for the
 * two of you, a total each, then each day as two bars in your colours and a
 * tick where you both studied, which is what the run counts.
 */
@Composable
fun BothWeeksScreen(onBack: () -> Unit, viewModel: BothWeeksViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BothWeeksContent(state, onBack)
}

/** Both weeks drawn from its state alone, so a preview can show it without an account. */
@Composable
fun BothWeeksContent(state: BothWeeksUiState, onBack: () -> Unit) {
    if (AppTheme.departures) { BoardBothLines(state, onBack); return }
    DetailScaffold(title = "Both weeks", onBack = onBack, eyebrow = "Last 7 days") {
        val me = state.me ?: return@DetailScaffold
        val partner = state.partner
        val meIsTech = me.role == PersonRole.TECH
        val myColor = AppTheme.colors.track(meIsTech)
        val partnerColor = AppTheme.colors.track(!meIsTech)
        val peak = (me.minutesByDay + (partner?.minutesByDay ?: emptyList())).maxOrNull()?.coerceAtLeast(30) ?: 30

        Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = Spacing.listHorizontal).padding(top = 10.dp, bottom = Spacing.xl)) {
            HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                Total(me, myColor, Modifier.weight(1f).padding(end = 12.dp))
                Box(Modifier.width(1.dp).fillMaxHeight().background(AppTheme.colors.separator))
                if (partner != null) Total(partner, partnerColor, Modifier.weight(1f).padding(start = 12.dp))
                else Text("Your partner has not joined yet.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.weight(1f).padding(start = 12.dp, top = 14.dp))
            }
            HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
            state.days.forEachIndexed { i, day ->
                val mine = me.minutesByDay[i]
                val theirs = partner?.minutesByDay?.get(i) ?: 0
                val both = mine > 0 && theirs > 0
                Row(
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                ) {
                    Column(Modifier.width(44.dp)) {
                        Text(day.format(DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text("${day.dayOfMonth}", style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 10.sp), color = AppTheme.colors.secondaryLabel)
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Bar(mine, peak, myColor)
                        if (partner != null) Bar(theirs, peak, partnerColor)
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(if (both) AppTheme.colors.accent else AppTheme.colors.separator)
                            .semantics { contentDescription = if (both) "Both studied" else "Not yet both" },
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = null, tint = if (both) AppTheme.colors.onSolid else AppTheme.colors.groupedBackground, modifier = Modifier.size(12.dp))
                    }
                }
                HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
            }
            Text(
                "A tick means both of you studied that day, which is what the ${state.togetherStreak}-day run counts." +
                    if (partner != null) " Today stays open until midnight on each of your clocks." else "",
                style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                modifier = Modifier.padding(top = 14.dp),
            )
        }
    }
}

@Composable
private fun Total(week: PersonWeek, color: Color, modifier: Modifier) {
    Column(modifier = modifier.padding(vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(color))
            Text(week.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
        Text("%.1f h".format(week.totalMinutes / 60.0), style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp))
        Text(week.byGoal.ifBlank { "Nothing logged yet" }, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
    }
}

@Composable
private fun Bar(minutes: Int, peak: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(Modifier.weight(1f)) {
            Box(Modifier.fillMaxWidth((minutes.toFloat() / peak).coerceIn(0.01f, 1f)).height(8.dp).background(color))
        }
        Text(minutesShort(minutes), style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 11.sp), color = AppTheme.colors.secondaryLabel, modifier = Modifier.width(50.dp))
    }
}
