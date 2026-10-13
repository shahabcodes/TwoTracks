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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.core.planner.LevelState
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.PlanStore
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.guide.GuideAnchors
import com.shahaabapps.twotracks.ui.guide.guideAnchor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

/** One person's ladder, as the Levels screen draws it. */
data class Ladder(val name: String, val isTech: Boolean, val levels: List<LevelProgress>)

data class LevelsUiState(val me: Ladder? = null, val partner: Ladder? = null, val showPartner: Boolean = false) {
    val shown: Ladder? get() = if (showPartner) partner else me
}

@HiltViewModel
class LevelsViewModel @Inject constructor(planStore: PlanStore) : ViewModel() {
    private val showPartner = MutableStateFlow(false)
    val uiState: StateFlow<LevelsUiState> = combine(planStore.plan, showPartner) { p, s ->
        LevelsUiState(
            me = p?.me?.let { Ladder(it.name, it.role == PersonRole.TECH, it.levels) },
            partner = p?.partner?.let { Ladder(it.name, it.role == PersonRole.TECH, it.levels) },
            showPartner = s,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LevelsUiState())

    fun show(partner: Boolean) { showPartner.value = partner }
}

/** Both ladders, one at a time behind a You / partner tab. Opened from Together. */
@Composable
fun LevelsScreen(onBack: () -> Unit, onOpenLevel: (String) -> Unit, viewModel: LevelsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LevelsContent(state, onBack = onBack, onOpenLevel = onOpenLevel, onShowPartner = viewModel::show)
}

/**
 * The prototype's ladder: a rail with a numbered dot per level, the current
 * one filled in the person's colour, and for each level its name, where it
 * stands, what it is for and when it runs.
 */
@Composable
fun LevelsContent(state: LevelsUiState, onBack: () -> Unit, onOpenLevel: (String) -> Unit, onShowPartner: (Boolean) -> Unit) {
    if (AppTheme.departures) { BoardLevels(state, onBack, onOpenLevel, onShowPartner); return }
    DetailScaffold(title = "Levels", onBack = onBack, eyebrow = "The roadmap, quarter by quarter") {
        LazyColumn(contentPadding = PaddingValues(bottom = Spacing.xl)) {
            val me = state.me
            val partner = state.partner
            if (me != null && partner != null) {
                item {
                    Row(modifier = Modifier.padding(horizontal = Spacing.listHorizontal).padding(top = 6.dp)) {
                        WhoTab("You", me.isTech, selected = !state.showPartner, modifier = Modifier.weight(1f)) { onShowPartner(false) }
                        WhoTab(partner.name, partner.isTech, selected = state.showPartner, modifier = Modifier.weight(1f)) { onShowPartner(true) }
                    }
                    HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline, modifier = Modifier.padding(horizontal = Spacing.listHorizontal))
                }
            }
            val ladder = state.shown ?: return@LazyColumn
            itemsIndexed(ladder.levels, key = { _, lp -> lp.level.id }) { i, lp ->
                LadderRow(
                    lp, color = AppTheme.colors.track(ladder.isTech), last = i == ladder.levels.lastIndex,
                    modifier = if (i == 0) Modifier.guideAnchor(GuideAnchors.LEVELS_LADDER) else Modifier,
                    onClick = { onOpenLevel(lp.level.id) },
                )
            }
        }
    }
}

@Composable
private fun WhoTab(label: String, isTech: Boolean, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.height(46.dp).semantics { this.selected = selected }.clickable(role = Role.Tab, onClick = onClick),
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = if (selected) AppTheme.colors.ink else AppTheme.colors.secondaryLabel)
        Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(2.dp).background(if (selected) AppTheme.colors.track(isTech) else Color.Transparent))
    }
}

@Composable
private fun LadderRow(lp: LevelProgress, color: Color, last: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val stateInk = when {
        lp.state == LevelState.DONE -> AppTheme.colors.success
        lp.isOverdue -> AppTheme.colors.warn
        lp.state == LevelState.OPEN -> color
        else -> AppTheme.colors.secondaryLabel
    }
    Row(
        modifier = modifier.fillMaxWidth().height(IntrinsicSize.Min).heightIn(min = 92.dp).clickable(onClick = onClick).padding(horizontal = Spacing.listHorizontal),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.width(28.dp).fillMaxHeight()) {
            if (!last) Box(Modifier.width(2.dp).fillMaxHeight().background(AppTheme.colors.separator))
            val filled = lp.state != LevelState.LOCKED
            val dot = when {
                lp.state == LevelState.DONE -> AppTheme.colors.success
                lp.isOverdue -> AppTheme.colors.warn
                else -> color
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(top = 18.dp).size(24.dp).clip(CircleShape)
                    .background(if (filled) dot else AppTheme.colors.groupedBackground)
                    .border(2.dp, if (filled) dot else AppTheme.colors.separator, CircleShape),
            ) {
                if (lp.state == LevelState.DONE) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = AppTheme.colors.onSolid, modifier = Modifier.size(14.dp))
                } else {
                    Text(
                        "${lp.level.number}", style = MaterialTheme.typography.labelMedium.copy(fontFamily = MonoFamily, fontSize = 11.sp, letterSpacing = 0.sp),
                        fontWeight = FontWeight.Medium, color = if (filled) AppTheme.colors.onSolid else AppTheme.colors.secondaryLabel, textAlign = TextAlign.Center,
                    )
                }
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.padding(vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(lp.level.name, style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp), fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    Text(levelState(lp), style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp), color = stateInk, modifier = Modifier.padding(top = 3.dp))
                }
                if (lp.level.summary.isNotBlank()) Text(lp.level.summary, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                Text(levelDates(lp, last), style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp, letterSpacing = 0.sp), color = AppTheme.colors.secondaryLabel)
            }
            HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
        }
    }
}

/** "NOW · 2 OF 5", "OVERDUE · 2 OF 5", "DONE" or "LATER". */
internal fun levelState(lp: LevelProgress): String = when (lp.state) {
    LevelState.DONE -> "DONE"
    LevelState.LOCKED -> "LATER"
    LevelState.OPEN -> (if (lp.isOverdue) "OVERDUE" else "NOW") + " · ${lp.requiredDone} OF ${lp.requiredTotal}"
}

private val dayMonth = DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH)
private val dayMonthYear = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH)

/**
 * When a level runs, as the ladder prints it: the span of an open or done
 * level ("1 OCT – 31 DEC 2026"), when a later one opens, and the date the
 * last one, the checkpoint, has to be reached by.
 */
internal fun levelDates(lp: LevelProgress, last: Boolean): String {
    val start = lp.startDate
    val due = lp.dueDate
    val text = when {
        last && lp.state == LevelState.LOCKED -> due?.let { "By ${it.format(dayMonthYear)}" }
        lp.state == LevelState.LOCKED -> start?.let { "Opens ${it.format(dayMonthYear)}" }
        start != null && due != null -> span(start, due)
        else -> due?.let { "By ${it.format(dayMonthYear)}" }
    }
    return text.orEmpty().uppercase(Locale.ENGLISH)
}

private fun span(start: LocalDate, end: LocalDate): String =
    if (start.year == end.year) "${start.format(dayMonth)} – ${end.format(dayMonthYear)}"
    else "${start.format(dayMonthYear)} – ${end.format(dayMonthYear)}"
