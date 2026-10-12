package com.shahaabapps.twotracks.feature.countries

import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.data.tracking.TrackingRepository
import com.shahaabapps.core.model.CountryTodo
import com.shahaabapps.core.model.CountryTrack
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Celebrations
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

/** Who is reading: their role, and both names, so a chance can say "Your" and "Sanya's". */
data class Reader(val myRole: PersonRole = PersonRole.TECH, val myName: String = "", val partnerName: String = "")

data class CountriesUiState(val countries: List<CountryTrack> = emptyList(), val reader: Reader = Reader())

data class CountryUiState(val country: CountryTrack? = null, val reader: Reader = Reader())

@HiltViewModel
class CountriesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionStore: SessionStore,
    dataStore: CoupleDataStore,
    private val trackingRepository: TrackingRepository,
    private val celebrations: Celebrations,
) : ViewModel() {
    private val code: String? = savedStateHandle.get<String>("code")

    private val reader = sessionStore.session

    val list: StateFlow<CountriesUiState> = combine(dataStore.countries, reader) { list, s ->
        CountriesUiState(list.sortedBy { it.order }, s?.let { Reader(it.myRole, it.nameFor(it.myRole), it.nameFor(it.partnerRole)) } ?: Reader())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CountriesUiState())

    val detail: StateFlow<CountryUiState> = combine(dataStore.countries, reader) { list, s ->
        CountryUiState(list.firstOrNull { it.code == code }, s?.let { Reader(it.myRole, it.nameFor(it.myRole), it.nameFor(it.partnerRole)) } ?: Reader())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CountryUiState())

    fun toggleTodo(c: CountryTrack, todo: CountryTodo) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        trackingRepository.saveCountry(s.coupleId, c.copy(todos = c.todos.map { if (it.id == todo.id) it.copy(done = !it.done) else it }))
        if (!todo.done) celebrations.saved("Done")
    }

    fun clearNotes(c: CountryTrack) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        trackingRepository.saveCountry(s.coupleId, c.copy(notes = ""))
        celebrations.removed("Notes cleared")
    }

    fun saveNotes(c: CountryTrack, notes: String) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        trackingRepository.saveCountry(s.coupleId, c.copy(notes = notes))
        celebrations.saved("Notes saved")
    }
}

/** A chance as the guide writes it, split into the headline figure, what qualifies it, and a value for a bar. */
data class Chance(val short: String, val rest: String, val value: Float)

private val percent = Regex("""(?i)(under\s+)?(\d+)(?:\s*-\s*(\d+))?%""")

internal fun chance(text: String): Chance {
    val m = percent.find(text) ?: return Chance(text, "", 0f)
    val under = m.groupValues[1].isNotBlank()
    val a = m.groupValues[2].toInt()
    val b = m.groupValues[3].toIntOrNull()
    val short = when {
        under -> "Under $a%"
        b != null -> "$a–$b%"
        else -> "$a%"
    }
    val value = when {
        under -> a * 0.6f
        b != null -> (a + b) / 2f
        else -> a.toFloat()
    }
    val rest = text.substring(m.range.last + 1).trim().trimStart(';', ',').trim().removePrefix("(").removeSuffix(")").trim()
        .replace(Regex("""(\d)-(\d)"""), "$1–$2").replace(" + ", " and ")
    return Chance(short, rest, value / 100f)
}

/** Whose chance a reader sees first: their own. */
internal fun myChance(c: CountryTrack, r: Reader): String = if (r.myRole == PersonRole.TECH) c.chanceTech else c.chanceTeacher
internal fun theirChance(c: CountryTrack, r: Reader): String = if (r.myRole == PersonRole.TECH) c.chanceTeacher else c.chanceTech

/** The guide writes the lead from the developer's side. Returns the label and the role it names, if any. */
internal fun leadOf(c: CountryTrack, r: Reader): Pair<String, PersonRole?> {
    val techName = if (r.myRole == PersonRole.TECH) r.myName else r.partnerName
    val teacherName = if (r.myRole == PersonRole.TEACHER) r.myName else r.partnerName
    return when (c.lead.lowercase(Locale.ENGLISH)) {
        "you" -> (if (r.myRole == PersonRole.TECH) "You lead" else "$techName leads") to PersonRole.TECH
        "her", "she" -> (if (r.myRole == PersonRole.TEACHER) "You lead" else "$teacherName leads") to PersonRole.TEACHER
        else -> c.lead to null
    }
}

private fun countWord(n: Int) = listOf("No", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten").getOrNull(n) ?: "$n"

@Composable
fun CountriesScreen(onBack: () -> Unit, onOpen: (String) -> Unit, viewModel: CountriesViewModel = hiltViewModel()) {
    val state by viewModel.list.collectAsStateWithLifecycle()
    CountriesContent(state, onBack, onOpen)
}

/**
 * The countries, as the prototype draws them: each with its code and name,
 * who leads, the line that sums it up, and both your chances as bars.
 */
@Composable
fun CountriesContent(state: CountriesUiState, onBack: () -> Unit, onOpen: (String) -> Unit) {
    val r = state.reader
    val mine = AppTheme.colors.track(r.myRole == PersonRole.TECH)
    val theirs = AppTheme.colors.track(r.myRole != PersonRole.TECH)
    DetailScaffold(
        title = "Countries", onBack = onBack, backLabel = "Back to Together",
        eyebrow = "${countWord(state.countries.size)} tracks · one of them home",
    ) {
        LazyColumn(contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(18.dp), modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                    Key(mine, "Your chance")
                    Key(theirs, "${r.partnerName}'s chance")
                }
            }
            item { HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule, modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 16.dp)) }
            items(state.countries, key = { it.code }) { c ->
                val (lead, leadRole) = leadOf(c, r)
                val me = chance(myChance(c, r))
                val them = chance(theirChance(c, r))
                Column(modifier = Modifier.fillMaxWidth().clickable(role = Role.Button) { onOpen(c.code) }.padding(horizontal = Spacing.listHorizontal)) {
                    Column(modifier = Modifier.padding(vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(c.code, style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 12.sp), color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(bottom = 3.dp))
                            Text(c.name, style = MaterialTheme.typography.titleMedium.copy(fontSize = 19.sp), fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                            Text(lead, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), fontWeight = FontWeight.SemiBold, color = leadRole?.let { AppTheme.colors.track(it == PersonRole.TECH) } ?: AppTheme.colors.secondaryLabel, modifier = Modifier.padding(bottom = 3.dp))
                        }
                        if (c.tagline.isNotBlank()) Text(c.tagline, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            ChanceBar(me, "You", mine)
                            ChanceBar(them, r.partnerName, theirs)
                        }
                    }
                    HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                }
            }
        }
    }
}

@Composable
private fun Key(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(Modifier.width(14.dp).height(4.dp).background(color))
        Text(label, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
    }
}

@Composable
private fun ChanceBar(c: Chance, who: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(Modifier.weight(1f).height(4.dp).background(AppTheme.colors.separator)) {
            Box(Modifier.fillMaxWidth(c.value.coerceIn(0.02f, 1f)).height(4.dp).background(color))
        }
        Text(
            "$who ${c.short}".uppercase(Locale.ENGLISH), style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 11.sp),
            color = AppTheme.colors.secondaryLabel, maxLines = 1, modifier = Modifier.width(120.dp),
        )
    }
}

@Composable
fun CountryDetailScreen(code: String, onBack: () -> Unit, viewModel: CountriesViewModel = hiltViewModel()) {
    val state by viewModel.detail.collectAsStateWithLifecycle()
    CountryContent(
        state, onBack = onBack,
        onToggle = viewModel::toggleTodo, onSaveNotes = viewModel::saveNotes, onClearNotes = viewModel::clearNotes,
    )
}

/**
 * One country, as the prototype draws it: its code and who leads, the line
 * that sums it up, both chances and the routes to staying under an ink
 * rule, then what to know, what to do, and the risks.
 */
@Composable
fun CountryContent(
    state: CountryUiState,
    onBack: () -> Unit,
    onToggle: (CountryTrack, CountryTodo) -> Unit,
    onSaveNotes: (CountryTrack, String) -> Unit,
    onClearNotes: (CountryTrack) -> Unit,
) {
    val c = state.country
    val r = state.reader
    val (lead, leadRole) = c?.let { leadOf(it, r) } ?: ("" to null)
    DetailScaffold(
        title = c?.name ?: "", onBack = onBack, backLabel = "Back to countries",
        eyebrow = c?.let { "${it.code} · $lead" }, eyebrowColor = leadRole?.let { AppTheme.colors.track(it == PersonRole.TECH) } ?: AppTheme.colors.secondaryLabel,
    ) {
        if (c == null) return@DetailScaffold
        LazyColumn(contentPadding = PaddingValues(bottom = Spacing.xl)) {
            if (c.tagline.isNotBlank()) item { Text(c.tagline, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) }
            item {
                Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 18.dp)) {
                    HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
                    val me = chance(myChance(c, r))
                    val them = chance(theirChance(c, r))
                    Pair2(
                        { Cell("Your chance", me.short, me.rest, big = true) },
                        { Cell("${r.partnerName}'s chance", them.short, them.rest, big = true) },
                    )
                    HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                    Pair2(
                        { Cell("Permanent residence", null, c.permanentResidence.replace(" (else", ", else").removeSuffix(")")) },
                        { Cell("Citizenship", null, c.citizenship.replace(" + ", ", ")) },
                    )
                    HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                }
            }
            if (c.keyFacts.isNotEmpty()) {
                item { Heading("What to know") }
                items(c.keyFacts, key = { "fact-" + it.first }) { (k, v) ->
                    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(vertical = 11.dp)) {
                            Text(
                                k.substringBefore(" (").uppercase(Locale.ENGLISH), style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 11.sp),
                                color = AppTheme.colors.secondaryLabel, modifier = Modifier.width(92.dp).padding(top = 2.dp),
                            )
                            Text(v, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), modifier = Modifier.weight(1f))
                        }
                        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                    }
                }
            }
            if (c.todos.isNotEmpty()) {
                item { Heading("To do") }
                items(c.todos, key = { "todo-" + it.id }) { t ->
                    Column(modifier = Modifier.clickable(role = Role.Checkbox) { onToggle(c, t) }.padding(horizontal = Spacing.listHorizontal)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.padding(vertical = 12.dp)) {
                            val shape = RoundedCornerShape(4.dp)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(top = 2.dp).size(20.dp).clip(shape).background(if (t.done) AppTheme.colors.ink else Color.Transparent).border(Spacing.rule, AppTheme.colors.ink, shape),
                            ) { if (t.done) Icon(Icons.Filled.Check, contentDescription = "Done", tint = AppTheme.colors.groupedBackground, modifier = Modifier.size(14.dp)) }
                            Text(
                                t.text, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), modifier = Modifier.weight(1f),
                                color = if (t.done) AppTheme.colors.secondaryLabel else AppTheme.colors.ink, textDecoration = if (t.done) TextDecoration.LineThrough else null,
                            )
                        }
                        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                    }
                }
            }
            if (c.risks.isNotEmpty()) {
                item { Heading("Risks") }
                item {
                    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        c.risks.forEach { risk ->
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("•", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), color = AppTheme.colors.secondaryLabel)
                                Text(risk, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), color = AppTheme.colors.secondaryLabel)
                            }
                        }
                    }
                }
            }
            // Not in the prototype: the shared notes this page has always kept.
            item { Heading("Notes") }
            item {
                var text by remember(c.code, c.notes) { mutableStateOf(c.notes) }
                Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    CupertinoTextField(value = text, onValueChange = { text = it }, placeholder = "Recruiters, salary quotes, anything worth keeping", singleLine = false)
                    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        if (text != c.notes) Link("Save notes") { onSaveNotes(c, text) }
                        if (c.notes.isNotBlank()) Link("Clear notes", AppTheme.colors.destructive) { text = ""; onClearNotes(c) }
                    }
                }
            }
        }
    }
}

@Composable
private fun Pair2(left: @Composable () -> Unit, right: @Composable () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Box(Modifier.weight(1f).padding(end = 12.dp)) { left() }
        Box(Modifier.width(Spacing.hairline).fillMaxHeight().background(AppTheme.colors.separator))
        Box(Modifier.weight(1f).padding(start = 12.dp)) { right() }
    }
}

@Composable
private fun Cell(label: String, value: String?, detail: String, big: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
        value?.let { Text(it, style = MaterialTheme.typography.headlineMedium.copy(fontSize = if (big) 22.sp else 14.sp), fontWeight = FontWeight.SemiBold) }
        if (detail.isNotBlank()) {
            Text(
                detail, style = if (value == null) MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp) else MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                fontWeight = if (value == null) FontWeight.SemiBold else FontWeight.Normal,
                color = if (value == null) AppTheme.colors.ink else AppTheme.colors.secondaryLabel,
            )
        }
    }
}

@Composable
private fun Heading(text: String) {
    Text(text, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 22.dp, bottom = 4.dp))
}

@Composable
private fun Link(text: String, color: Color = AppTheme.colors.accentText, onClick: () -> Unit) {
    Text(
        text, style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp), color = color,
        modifier = Modifier.clip(RoundedCornerShape(6.dp)).clickable(role = Role.Button, onClick = onClick).padding(vertical = 10.dp),
    )
}
