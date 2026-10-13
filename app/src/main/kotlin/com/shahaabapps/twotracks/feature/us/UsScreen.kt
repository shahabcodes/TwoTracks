package com.shahaabapps.twotracks.feature.us

import androidx.compose.runtime.getValue
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.data.study.forRole
import com.shahaabapps.core.model.BudgetItem
import com.shahaabapps.core.model.CountryTrack
import com.shahaabapps.core.model.Deadline
import com.shahaabapps.core.model.DocStatus
import com.shahaabapps.core.model.DocumentItem
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.TestRecord
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.core.planner.MissedDays
import com.shahaabapps.core.planner.PlanStart
import com.shahaabapps.core.ui.components.CupertinoScaffold
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.PersonPlan
import com.shahaabapps.twotracks.session.PlanStore
import com.shahaabapps.twotracks.ui.Routes
import com.shahaabapps.twotracks.ui.guide.GuideAnchors
import com.shahaabapps.twotracks.ui.guide.guideAnchor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject

/** One of you, as a column on Together. */
data class TogetherPerson(
    val role: PersonRole,
    val name: String,
    val joined: Boolean,
    /** "DOHA · 09:12" */
    val place: String,
    val level: LevelProgress?,
    val goals: List<GoalBar>,
    /** Weekly counts, drawn the way the goals are. */
    val counts: List<GoalBar> = emptyList(),
    /** Missed study days and what they cost, when there are any. */
    val missed: String? = null,
    /** Every level, for the board's two lines. */
    val ladder: List<LevelProgress> = emptyList(),
) {
    val isTech: Boolean get() = role == PersonRole.TECH
}

data class WeekDay(val label: String, val me: Boolean, val partner: Boolean, val isToday: Boolean)

data class MoveRow(val code: String, val name: String, val lead: String, val leadRole: PersonRole?)

data class UsUiState(
    val me: TogetherPerson? = null,
    val partner: TogetherPerson? = null,
    val streak: Int = 0,
    val week: List<WeekDay> = emptyList(),
    /** "MARRIAGE · 112 DAYS", the next fixed date, and whose it is. */
    val nextDate: String? = null,
    val nextDateRole: PersonRole? = null,
    val countries: List<MoveRow> = emptyList(),
    val tests: String = "",
    val documents: String = "",
    val budget: String = "",
)

@HiltViewModel
class UsViewModel @Inject constructor(
    planStore: PlanStore,
    dataStore: CoupleDataStore,
) : ViewModel() {
    private val records = combine(dataStore.countries, dataStore.tests, dataStore.documents, dataStore.budget, dataStore.deadlines) { c, t, d, b, dl -> Records(c, t, d, b, dl) }

    val uiState: StateFlow<UsUiState> = combine(planStore.plan, dataStore.logs, dataStore.habits, dataStore.habitLogs, combine(dataStore.availability, records) { a, r -> a to r }) { plan, logs, habits, habitLogs, (availability, rec) ->
        if (plan == null) return@combine UsUiState()
        fun datesOf(role: PersonRole) = logs.filter { it.owner == role }.map { it.dateIso }.toSet()
        fun column(person: PersonPlan): TogetherPerson {
            val profile = plan.session.profileFor(person.role)
            val zone = profile?.timezone ?: "UTC"
            val report = MissedDays.report(
                person.today, availability.forRole(person.role), datesOf(person.role),
                PlanStart.effective(profile?.startDateIso, person.levels.map { it.level.startDateIso }),
            )
            val weekIso = Dates.format(Dates.weekStart(person.today))
            return TogetherPerson(
                role = person.role, name = person.name, joined = profile != null,
                place = place(zone, ZonedDateTime.now(zoneOrUtc(zone))),
                level = person.current,
                goals = person.activeGoals.take(3).mapNotNull { g -> person.goalProgress[g.id]?.let { goalBar(it, Courses.forGoal(g.id) != null) } },
                counts = habits.filter { it.owner == person.role && it.active && it.weeklyTarget != null }.map { h ->
                    val n = habitLogs.filter { it.habitId == h.id && it.dateIso >= weekIso }.sumOf { it.value }
                    GoalBar(h.name, "$n/${h.weeklyTarget} wk", (n.toFloat() / (h.weeklyTarget ?: 1)).coerceIn(0f, 1f))
                },
                missed = missedLine(report),
                ladder = person.levels,
            )
        }
        val me = plan.me
        val today = me.today
        val next = rec.deadlines.filter { it.dateIso >= Dates.format(today) }.minByOrNull { it.dateIso }
        UsUiState(
            me = column(me), partner = column(plan.partner), streak = plan.togetherStreak,
            week = lastWeek(today, datesOf(me.role), datesOf(plan.partner.role)),
            nextDate = next?.let { dateLine(it, today) }, nextDateRole = next?.owner,
            countries = rec.countries.sortedBy { it.order }.map { moveRow(it, plan.session.nameFor(PersonRole.TECH), plan.session.nameFor(PersonRole.TEACHER)) },
            tests = testsLine(rec.tests), documents = documentsLine(rec.documents), budget = budgetLine(rec.budget),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UsUiState())

    private data class Records(val countries: List<CountryTrack>, val tests: List<TestRecord>, val documents: List<DocumentItem>, val budget: List<BudgetItem>, val deadlines: List<Deadline>)
}

/** "DOHA · 09:12" */
internal fun place(zone: String, now: ZonedDateTime): String = "${cityOf(zone).uppercase(Locale.ENGLISH)} · ${"%02d:%02d".format(now.hour, now.minute)}"

/** The seven days ending today, oldest first, and who studied on each. */
internal fun lastWeek(today: LocalDate, mine: Set<String>, theirs: Set<String>): List<WeekDay> = (6L downTo 0L).map { back ->
    val d = today.minusDays(back)
    val iso = Dates.format(d)
    WeekDay(d.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.ENGLISH), iso in mine, iso in theirs, back == 0L)
}

internal fun missedLine(r: MissedDays.Report): String? =
    if (r.missedCount == 0) null
    else "${r.missedCount} missed day${if (r.missedCount == 1) "" else "s"} · lands about ${r.delayDays} day${if (r.delayDays == 1L) "" else "s"} later"

/** "MARRIAGE · 112 DAYS" */
internal fun dateLine(d: Deadline, today: LocalDate): String {
    val days = ChronoUnit.DAYS.between(today, LocalDate.parse(d.dateIso))
    val title = d.title.substringBefore(":").uppercase(Locale.ENGLISH)
    return "$title · " + when (days) { 0L -> "TODAY"; 1L -> "TOMORROW"; else -> "$days DAYS" }
}

/** The guide writes the lead from the developer's side: "You", "Her", or a rule such as "Higher scorer". */
internal fun moveRow(c: CountryTrack, techName: String, teacherName: String): MoveRow = when (c.lead.lowercase(Locale.ENGLISH)) {
    "you" -> MoveRow(c.code, c.name, techName, PersonRole.TECH)
    "her", "she" -> MoveRow(c.code, c.name, teacherName, PersonRole.TEACHER)
    else -> MoveRow(c.code, c.name, c.lead, null)
}

internal fun testsLine(tests: List<TestRecord>): String = tests.sortedBy { it.order }.map { shortTitle(it.name.substringBefore(" (")) }.distinct().take(3).joinToString(" · ")

internal fun documentsLine(docs: List<DocumentItem>): String = if (docs.isEmpty()) "Nothing listed yet" else "${docs.count { it.status == DocStatus.DONE }} of ${docs.size} done"

internal fun budgetLine(items: List<BudgetItem>): String {
    val c = items.map { it.currency }.distinct()
    return when (c.size) {
        0 -> "Nothing planned yet"
        1 -> c.first()
        else -> c.dropLast(1).joinToString(", ") + " and " + c.last()
    }
}

/** Together: the two of you side by side, the week you share, the move, and the plan in detail. */
@Composable
fun UsScreen(onNavigate: (String) -> Unit, onOpenLevel: (String) -> Unit, viewModel: UsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    UsContent(state, onNavigate = onNavigate, onOpenLevel = onOpenLevel)
}

@Composable
fun UsContent(state: UsUiState, onNavigate: (String) -> Unit, onOpenLevel: (String) -> Unit) {
    if (AppTheme.departures) { BoardUs(state, onNavigate, onOpenLevel); return }
    CupertinoScaffold(title = "Together", eyebrow = "Two tracks · one move") {
        LazyColumn(contentPadding = PaddingValues(bottom = Spacing.xl)) {
            val me = state.me
            val partner = state.partner
            if (me != null && partner != null) {
                item {
                    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal).padding(top = 12.dp)) {
                        HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
                        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                            PersonColumn(me, mine = true, onOpenLevel = onOpenLevel, onOpenPerson = { me.level?.let { onOpenLevel(it.level.id) } }, modifier = Modifier.weight(1f).padding(end = 14.dp))
                            Box(Modifier.width(Spacing.hairline).fillMaxHeight().background(AppTheme.colors.separator))
                            PersonColumn(partner, mine = false, onOpenLevel = onOpenLevel, onOpenPerson = { onNavigate(Routes.PARTNER) }, modifier = Modifier.weight(1f).padding(start = 14.dp))
                        }
                    }
                }
                item { Week(state, me, partner) }
            }
            item { Move(state, onNavigate) }
            item {
                Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 20.dp).semantics { contentDescription = "The plan in detail" }) {
                    HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
                    PlanRow("Levels", "Five each, quarter by quarter", Modifier.guideAnchor(GuideAnchors.US_LEVELS)) { onNavigate(Routes.LEVELS) }
                    PlanRow("Tests", state.tests) { onNavigate(Routes.TESTS) }
                    PlanRow("Documents", state.documents) { onNavigate(Routes.DOCUMENTS) }
                    PlanRow("Budget", state.budget) { onNavigate(Routes.BUDGET) }
                }
            }
        }
    }
}

@Composable
private fun PersonColumn(p: TogetherPerson, mine: Boolean, onOpenLevel: (String) -> Unit, onOpenPerson: () -> Unit, modifier: Modifier) {
    val color = AppTheme.colors.track(p.isTech)
    Column(modifier = modifier.padding(top = 14.dp, bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(min = 28.dp).clip(RoundedCornerShape(6.dp)).clickable(role = Role.Button, onClick = onOpenPerson),
            ) {
                Box(Modifier.size(10.dp).clip(CircleShape).background(color))
                Text(p.name, style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp), fontWeight = FontWeight.SemiBold, maxLines = 1)
                if (!mine) Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.ink, modifier = Modifier.size(16.dp))
            }
            Text(if (p.joined) p.place else "NOT JOINED YET", style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp, letterSpacing = 0.sp), color = AppTheme.colors.secondaryLabel)
        }
        p.level?.let { lp ->
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Level ${lp.level.number} · ${lp.level.name}", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = if (mine) AppTheme.colors.accentText else AppTheme.colors.secondaryLabel, maxLines = 1,
                    modifier = Modifier.clip(RoundedCornerShape(4.dp)).clickable(role = Role.Button) { onOpenLevel(lp.level.id) },
                )
                Text("${lp.requiredDone} of ${lp.requiredTotal}", style = MaterialTheme.typography.headlineMedium.copy(fontSize = 22.sp), fontWeight = FontWeight.SemiBold)
                lp.daysLeft?.let { d ->
                    Text(if (d >= 0) "$d days left" else "${-d} days over", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = if (d >= 0) AppTheme.colors.secondaryLabel else AppTheme.colors.warn)
                }
            }
        }
        if (p.goals.isNotEmpty() || p.counts.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                (p.goals + p.counts).forEach { GoalBarRow(it, color) }
            }
        }
        p.missed?.let { Text(it, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.warn) }
    }
}

@Composable
private fun Week(state: UsUiState, me: TogetherPerson, partner: TogetherPerson) {
    Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 4.dp)) {
        HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
        Column(modifier = Modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text("This week, both of you", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                if (state.streak > 0) Text("${state.streak}-DAY STREAK", style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp), color = AppTheme.colors.secondaryLabel)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val mine = AppTheme.colors.track(me.isTech)
                val theirs = AppTheme.colors.track(partner.isTech)
                state.week.forEach { d ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.weight(1f)) {
                        Box(Modifier.fillMaxWidth().height(18.dp).clip(RoundedCornerShape(4.dp)).background(if (d.me) mine else AppTheme.colors.separator))
                        Box(Modifier.fillMaxWidth().height(18.dp).clip(RoundedCornerShape(4.dp)).background(if (d.partner) theirs else AppTheme.colors.separator))
                        Text(d.label, style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp, letterSpacing = 0.sp), color = if (d.isToday) AppTheme.colors.ink else AppTheme.colors.secondaryLabel)
                    }
                }
            }
            Text(
                "A day counts for the streak when both of you study. Today is open for each of you until midnight on your own clock.",
                style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
            )
        }
    }
}

@Composable
private fun Move(state: UsUiState, onNavigate: (String) -> Unit) {
    Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 20.dp)) {
        HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.heightIn(min = 28.dp).clip(RoundedCornerShape(6.dp)).clickable(role = Role.Button) { onNavigate(Routes.COUNTRIES) },
            ) {
                Text("The move", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.ink, modifier = Modifier.size(16.dp))
            }
            Box(Modifier.weight(1f))
            state.nextDate?.let {
                Text(
                    it, style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp),
                    color = state.nextDateRole?.let { r -> AppTheme.colors.track(r == PersonRole.TECH) } ?: AppTheme.colors.secondaryLabel, maxLines = 1,
                )
            }
        }
        state.countries.forEach { c ->
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).clickable(role = Role.Button) { onNavigate(Routes.country(c.code)) },
                ) {
                    Box(Modifier.width(3.dp).height(22.dp).background(c.leadRole?.let { AppTheme.colors.track(it == PersonRole.TECH) } ?: AppTheme.colors.separator))
                    Text(c.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                    Text("${c.lead} leads", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
                }
                HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
            }
        }
    }
}

@Composable
private fun PlanRow(title: String, subtitle: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).clickable(role = Role.Button, onClick = onClick),
        ) {
            Column(modifier = Modifier.weight(1f).padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                if (subtitle.isNotBlank()) Text(subtitle, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.ink, modifier = Modifier.size(16.dp))
        }
        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
    }
}
