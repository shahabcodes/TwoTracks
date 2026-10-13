package com.shahaabapps.twotracks.feature.tests

import androidx.compose.runtime.setValue
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.data.tracking.TrackingRepository
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.TestRecord
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.Celebrations
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.DialogButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

data class TestsUiState(
    val tests: List<TestRecord> = emptyList(),
    val myRole: PersonRole = PersonRole.TECH,
    val today: LocalDate = LocalDate.now(),
)

@HiltViewModel
class TestsViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    dataStore: CoupleDataStore,
    private val trackingRepository: TrackingRepository,
    private val celebrations: Celebrations,
) : ViewModel() {
    val uiState: StateFlow<TestsUiState> = combine(sessionStore.session, dataStore.tests) { session, tests ->
        val me = session?.myRole ?: PersonRole.TECH
        // One list, as the prototype draws it: by the guide's order, yours first where two tie.
        TestsUiState(tests.sortedWith(compareBy<TestRecord> { it.order }.thenBy { if (it.owner == me) 0 else 1 }), me)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TestsUiState())

    fun save(test: TestRecord) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        trackingRepository.saveTest(s.coupleId, test)
        celebrations.saved("Result saved")
    }
}

private val dayMonth = DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH)

/** "NOT TAKEN", "TAKEN 4 OCT · MET", "TAKEN 4 OCT · 2 SHORT", "EXPIRED". */
internal fun testStatus(t: TestRecord, today: LocalDate): String {
    val taken = Dates.parseOrNull(t.takenOnIso)
    val expires = Dates.parseOrNull(t.expiresOnIso)
    return when {
        expires != null && expires.isBefore(today) -> "EXPIRED"
        t.actualBands.isEmpty() -> "NOT TAKEN"
        else -> {
            val short = t.targetBands.count { (k, target) -> t.actualBands[k]?.let { !meets(it, target) } ?: false }
            "TAKEN" + (taken?.let { " ${it.format(dayMonth).uppercase(Locale.ENGLISH)}" } ?: "") + " · " + if (short == 0) "MET" else "$short SHORT"
        }
    }
}

/** "TARGET  L 8 · R 7 · W 7 · S 8", or each band as score / target once taken. */
internal fun bandLine(t: TestRecord): String {
    if (t.targetBands.isEmpty()) return ""
    fun key(k: String) = if (t.targetBands.size >= 3) k.take(1).uppercase(Locale.ENGLISH) else ""
    fun num(v: String) = v.removeSuffix(".0")
    val taken = t.actualBands.isNotEmpty()
    val parts = t.targetBands.map { (k, target) ->
        val label = key(k)
        val value = if (taken) "${t.actualBands[k]?.let(::num) ?: "–"}/${num(target)}" else num(target)
        if (label.isEmpty()) value else "$label $value"
    }
    // Four bands that all want the same thing read better said once.
    val same = !taken && t.targetBands.size >= 3 && t.targetBands.values.distinct().size == 1
    return if (same) "TARGET  ${num(t.targetBands.values.first())} IN EVERY BAND".uppercase(Locale.ENGLISH)
    else (if (taken) "" else "TARGET  ") + parts.joinToString(" · ").uppercase(Locale.ENGLISH)
}

internal fun meets(actual: String, target: String): Boolean {
    val a = actual.filter { it.isDigit() || it == '.' }.toDoubleOrNull()
    val t = target.filter { it.isDigit() || it == '.' }.toDoubleOrNull()
    return if (a != null && t != null) a >= t else actual.equals(target, ignoreCase = true)
}

@Composable
fun TestsScreen(onBack: () -> Unit, viewModel: TestsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TestsContent(state, onBack = onBack, onSave = viewModel::save)
}

/**
 * Every test either of you is sitting, as the prototype draws them: a bar in
 * the colour of whose it is, the name and where it stands, what it is for,
 * and the bands it needs. Tap one to enter the result.
 */
@Composable
fun TestsContent(state: TestsUiState, onBack: () -> Unit, onSave: (TestRecord) -> Unit) {
    var editing by remember { mutableStateOf<TestRecord?>(null) }
    if (AppTheme.departures) BoardTests(state, onBack) { editing = it } else DetailScaffold(title = "Tests", onBack = onBack, backLabel = "Back to Together", eyebrow = "Official results against what each route needs") {
        LazyColumn(contentPadding = PaddingValues(bottom = Spacing.xl)) {
            item { HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule, modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 10.dp)) }
            items(state.tests, key = { it.id }) { t ->
                val status = testStatus(t, state.today)
                Column(modifier = Modifier.clickable(role = Role.Button, onClickLabel = "Enter the result") { editing = t }.padding(horizontal = Spacing.listHorizontal)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).padding(vertical = 14.dp)) {
                        Box(Modifier.width(4.dp).fillMaxHeight().background(AppTheme.colors.track(t.owner == PersonRole.TECH)))
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(t.name, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp), fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                                Text(
                                    status, style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp), maxLines = 1, modifier = Modifier.padding(top = 4.dp),
                                    color = when {
                                        status == "EXPIRED" -> AppTheme.colors.destructive
                                        status.endsWith("MET") -> AppTheme.colors.success
                                        status.startsWith("TAKEN") -> AppTheme.colors.warn
                                        else -> AppTheme.colors.secondaryLabel
                                    },
                                )
                            }
                            if (t.purpose.isNotBlank()) Text(t.purpose, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                            val bands = bandLine(t)
                            if (bands.isNotBlank()) Text(bands, style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 12.sp))
                        }
                    }
                    HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                }
            }
        }
    }

    editing?.let { t ->
        val actual = remember(t.id) { mutableStateOf(t.targetBands.keys.associateWith { t.actualBands[it].orEmpty() }) }
        var taken by remember(t.id) { mutableStateOf(t.takenOnIso.orEmpty()) }
        var expires by remember(t.id) { mutableStateOf(t.expiresOnIso.orEmpty()) }
        CupertinoDialog(
            onDismissRequest = { editing = null },
            confirmButton = {
                DialogButton(onClick = {
                    onSave(t.copy(actualBands = actual.value.filterValues { it.isNotBlank() }, takenOnIso = taken.ifBlank { null }, expiresOnIso = expires.ifBlank { null }))
                    editing = null
                }) { Text("Save") }
            },
            dismissButton = { DialogButton(onClick = { editing = null }) { Text("Cancel") } },
            title = { Text(t.name) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    t.targetBands.forEach { (k, target) ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                            Text("$k (target $target)", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                            CupertinoTextField(value = actual.value[k].orEmpty(), onValueChange = { v -> actual.value = actual.value + (k to v) }, placeholder = "score", modifier = Modifier.width(90.dp))
                        }
                    }
                    CupertinoTextField(value = taken, onValueChange = { taken = it }, placeholder = "Taken on (yyyy-mm-dd)")
                    CupertinoTextField(value = expires, onValueChange = { expires = it }, placeholder = "Valid until (yyyy-mm-dd)")
                    Text("IELTS is valid 2 years (3 for Australian visas).", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.fillMaxWidth())
                }
            },
        )
    }
}
