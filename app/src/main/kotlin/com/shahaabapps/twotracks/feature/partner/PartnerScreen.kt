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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.model.GoalKind
import com.shahaabapps.core.model.MockScore
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.feature.us.GoalBar
import com.shahaabapps.twotracks.feature.us.GoalBarRow
import com.shahaabapps.twotracks.feature.us.cityOf
import com.shahaabapps.twotracks.feature.us.goalBar
import com.shahaabapps.twotracks.feature.us.zoneOrUtc
import com.shahaabapps.twotracks.reminders.SessionPlanning
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.PlanStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

/** One of the partner's sessions today: on their clock, and when that is on yours. */
data class PartnerSessionRow(val time: String, val title: String, val detail: String, val done: Boolean, val goalId: String?)

data class Band(val name: String, val score: String, val need: String?, val met: Boolean)

data class Paper(val date: String, val bands: List<Band>, val note: String)

data class PartnerUiState(
    val loaded: Boolean = false,
    val name: String = "",
    val isTech: Boolean = false,
    val joined: Boolean = true,
    /** "THE TEACHER · KARACHI 11:12" */
    val roleLine: String = "",
    val city: String = "",
    val sessions: List<PartnerSessionRow> = emptyList(),
    val level: LevelProgress? = null,
    val weekHours: String = "",
    val weekLine: String = "",
    val goals: List<GoalBar> = emptyList(),
    val status: String = "",
    val statusGood: Boolean = true,
    val paper: Paper? = null,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PartnerViewModel @Inject constructor(
    sessionStore: SessionStore,
    planStore: PlanStore,
    dataStore: CoupleDataStore,
    sessionPlanning: SessionPlanning,
) : ViewModel() {
    private val sessions = sessionStore.session.flatMapLatest { s ->
        if (s == null) flowOf(emptyList()) else {
            val today = Clock.today(s.partner ?: s.me)
            sessionPlanning.sessionsBetween(today.minusDays(1), today.plusDays(1))
        }
    }

    val uiState: StateFlow<PartnerUiState> = combine(planStore.plan, sessions, dataStore.logs, dataStore.mockScores) { plan, sessions, logs, mocks ->
        if (plan == null) return@combine PartnerUiState()
        val p = plan.partner
        val profile = plan.session.partner
        val zone = profile?.timezone ?: "UTC"
        val readerZone = zoneOrUtc(plan.session.me.timezone)
        val todayIso = Dates.format(p.today)
        val goalsById = p.goals.associateBy { it.id }
        val weekStart = Dates.weekStart(p.today)
        val weekLogs = logs.filter { it.owner == p.role && it.dateIso >= Dates.format(weekStart) && it.dateIso <= todayIso }
        val daysSoFar = (p.today.toEpochDay() - weekStart.toEpochDay() + 1).toInt()
        val (status, good) = p.statusSentence
        PartnerUiState(
            loaded = true, name = p.name, isTech = p.role == PersonRole.TECH, joined = profile != null,
            roleLine = roleLine(p.role, zone, ZonedDateTime.now(zoneOrUtc(zone))), city = cityOf(zone),
            sessions = sessions.filter { it.owner == p.role && it.dateIso == todayIso }.sortedBy { it.startMinutes }.map { s ->
                val reader = Clock.convert(p.today, s.startMinutes, zoneOrUtc(zone), readerZone)
                sessionRow(s, goalsById[s.goalId], if (readerZone == zoneOrUtc(zone)) null else Dates.minutesToClock(reader.hour * 60 + reader.minute))
            },
            level = p.current,
            weekHours = hoursText(weekLogs.sumOf { it.minutes } / 60.0),
            weekLine = weekLine(weekLogs.map { it.dateIso }.toSet().size, daysSoFar),
            goals = p.activeGoals.mapNotNull { g -> p.goalProgress[g.id]?.let { goalBar(it, Courses.forGoal(g.id) != null) } },
            status = status, statusGood = good,
            paper = paper(p.goals, mocks.filter { it.owner == p.role }),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PartnerUiState())
}

/** "THE TEACHER · KARACHI 11:12" */
internal fun roleLine(role: PersonRole, zone: String, now: ZonedDateTime): String =
    (if (role == PersonRole.TECH) "The developer" else "The teacher").uppercase(Locale.ENGLISH) +
        " · ${cityOf(zone).uppercase(Locale.ENGLISH)} ${"%02d:%02d".format(now.hour, now.minute)}"

internal fun sessionRow(s: PlannedSession, goal: StudyGoal?, readerTime: String?): PartnerSessionRow {
    val part = goal?.components?.firstOrNull { it.id == s.componentId }?.name?.substringBefore(" (")
    val title = listOfNotNull(goal?.title?.substringBefore(" ("), part?.let { if (s.isClass) "$it class" else it }).joinToString(" · ").ifBlank { "Study" }
    val detail = buildList {
        add("${s.durationMinutes} min")
        if (s.isClass) add("counts by itself")
        readerTime?.let { add("$it for you") }
    }.joinToString(" · ")
    return PartnerSessionRow(Dates.minutesToClock(s.startMinutes), title, detail, s.status == SessionStatus.DONE, goal?.id)
}

internal fun hoursText(h: Double): String = if (h == 0.0) "0 h" else "%.1f h".format(Locale.ENGLISH, h).replace(".0 h", " h")

internal fun weekLine(studied: Int, daysSoFar: Int): String = when {
    studied == 0 -> "nothing yet this week"
    studied >= daysSoFar -> "every day so far"
    else -> "$studied of $daysSoFar days so far"
}

private val dayMonth = DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH)

/**
 * The latest practice paper for a scored test, band by band against the
 * target: the prototype's four columns. Null when there is no such paper.
 */
internal fun paper(goals: List<StudyGoal>, mocks: List<MockScore>): Paper? {
    val scored = goals.filter { g -> g.kind == GoalKind.LANGUAGE_TEST && g.components.any { it.targetScore != null } }.associateBy { it.id }
    val last = mocks.filter { it.goalId in scored }.maxByOrNull { it.dateIso } ?: return null
    val goal = scored.getValue(last.goalId)
    val bands = goal.components.mapNotNull { c ->
        val score = last.scores[c.id] ?: return@mapNotNull null
        val need = c.targetScore
        val met = need == null || (score.toDoubleOrNull() ?: 0.0) >= (need.toDoubleOrNull() ?: Double.MAX_VALUE)
        Band(c.name.substringBefore(" ("), score, need?.removeSuffix(".0"), met)
    }
    if (bands.isEmpty()) return null
    val short = bands.filter { !it.met }.map { it.name }
    val note = when (short.size) {
        0 -> "Every band meets its target."
        1 -> "${short[0]} is the gap."
        else -> short.dropLast(1).joinToString(", ") + " and ${short.last()} are the gap."
    }
    return Paper(LocalDate.parse(last.dateIso).format(dayMonth), bands, note)
}

/**
 * Your partner, read-only, as the prototype draws it: a bar in their colour
 * beside their name, their day on their own clock, where their level
 * stands and how much they studied this week, their goals, and their last
 * practice paper band by band.
 */
@Composable
fun PartnerScreen(
    onBack: () -> Unit,
    onOpenLevel: (String) -> Unit,
    onOpenGoal: (String) -> Unit,
    viewModel: PartnerViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    PartnerContent(state, onBack = onBack, onOpenLevel = onOpenLevel, onOpenGoal = onOpenGoal)
}

@Composable
fun PartnerContent(state: PartnerUiState, onBack: () -> Unit, onOpenLevel: (String) -> Unit, onOpenGoal: (String) -> Unit) {
    if (AppTheme.departures) { BoardPartner(state, onBack, onOpenLevel, onOpenGoal); return }
    val color = AppTheme.colors.track(state.isTech)
    DetailScaffold(title = "", onBack = onBack, backLabel = "Back to Together") {
        if (!state.loaded) return@DetailScaffold
        LazyColumn(contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                    Box(Modifier.width(6.dp).height(40.dp).background(color))
                    Column {
                        Text(state.name, style = MaterialTheme.typography.displayLarge)
                        Text(if (state.joined) state.roleLine else "NOT JOINED YET", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
                    }
                }
            }
            item { Heading("${state.name}'s day, by ${state.city} time") }
            if (state.sessions.isEmpty()) {
                item { Text("Nothing planned today.", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(horizontal = Spacing.listHorizontal, vertical = 10.dp)) }
            }
            state.sessions.forEach { s ->
                item {
                    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal).then(if (s.goalId != null) Modifier.clickable(role = Role.Button) { onOpenGoal(s.goalId) } else Modifier)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(vertical = 10.dp)) {
                            Text(s.time, style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 13.sp), modifier = Modifier.width(52.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
                                Text(
                                    s.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold,
                                    textDecoration = if (s.done) TextDecoration.LineThrough else null, color = if (s.done) AppTheme.colors.secondaryLabel else AppTheme.colors.ink,
                                )
                                Text(s.detail, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
                            }
                            if (s.done) Icon(Icons.Filled.Check, contentDescription = "Done", tint = AppTheme.colors.success, modifier = Modifier.size(16.dp))
                        }
                        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                    }
                }
            }
            item {
                Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 20.dp)) {
                    HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
                    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                        val lp = state.level
                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier.weight(1f).then(if (lp != null) Modifier.clickable(role = Role.Button) { onOpenLevel(lp.level.id) } else Modifier).padding(top = 14.dp, bottom = 14.dp, end = 12.dp),
                        ) {
                            Text(lp?.let { "Level ${it.level.number} · ${it.level.name}" } ?: "Levels", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel, maxLines = 1)
                            Text(lp?.let { "${it.requiredDone} of ${it.requiredTotal}" } ?: "–", style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp), fontWeight = FontWeight.SemiBold)
                            lp?.daysLeft?.let { d ->
                                Text(
                                    if (d >= 0) "$d days, to ${lp.dueDate?.format(dayMonth)}" else "${-d} days over",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = if (d >= 0) AppTheme.colors.secondaryLabel else AppTheme.colors.warn,
                                )
                            }
                        }
                        Box(Modifier.width(Spacing.hairline).fillMaxHeight().background(AppTheme.colors.separator))
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f).padding(top = 14.dp, bottom = 14.dp, start = 12.dp)) {
                            Text("This week", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
                            Text(state.weekHours, style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp), fontWeight = FontWeight.SemiBold)
                            Text(state.weekLine, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
                        }
                    }
                    // Not in the prototype: the pace sentence Today used to show for both of you.
                    if (state.status.isNotBlank()) {
                        val ink = if (state.statusGood) AppTheme.colors.success else AppTheme.colors.warn
                        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 14.dp)) {
                            Icon(if (state.statusGood) Icons.Filled.Check else Icons.Filled.Info, contentDescription = null, tint = ink, modifier = Modifier.padding(top = 2.dp).size(14.dp))
                            Text(state.status, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = ink)
                        }
                    }
                }
            }
            item { Heading("${state.name}'s goals", top = 0.dp) }
            state.goals.forEach { g ->
                item {
                    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal).then(if (g.goalId != null) Modifier.clickable(role = Role.Button) { onOpenGoal(g.goalId) } else Modifier)) {
                        GoalBarRow(g, color, big = true, modifier = Modifier.padding(vertical = 10.dp))
                        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                    }
                }
            }
            state.paper?.let { paper ->
                item { Heading("Last practice paper · ${paper.date}", top = 20.dp) }
                item {
                    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            paper.bands.forEach { b ->
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
                                    Box(Modifier.fillMaxWidth().height(3.dp).background(if (b.met) AppTheme.colors.success else color))
                                    Text(b.score, style = MaterialTheme.typography.headlineMedium.copy(fontSize = 22.sp), fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 6.dp))
                                    Text(b.name, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = AppTheme.colors.secondaryLabel, maxLines = 1)
                                    b.need?.let {
                                        Text(
                                            if (b.met) "MET" else "NEEDS $it", style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 10.sp),
                                            color = if (b.met) AppTheme.colors.success else AppTheme.colors.warn,
                                        )
                                    }
                                }
                            }
                        }
                        Text(paper.note, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = 12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun Heading(text: String, top: androidx.compose.ui.unit.Dp = 20.dp) {
    Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = top)) {
        HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
        Text(text, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 14.dp, bottom = 6.dp))
    }
}
