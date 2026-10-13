package com.shahaabapps.twotracks.feature.settings

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.unit.em
import androidx.compose.ui.text.TextStyle
import com.shahaabapps.core.ui.theme.CondensedFamily
import com.shahaabapps.twotracks.ui.board.BoardTitle
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardLink
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardBack
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.common.datastore.SpeechPreferences
import com.shahaabapps.core.content.Course
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.feature.learn.Speaker
import com.shahaabapps.twotracks.feature.learn.VoiceCharacter
import com.shahaabapps.twotracks.feature.learn.VoiceGender
import com.shahaabapps.twotracks.feature.learn.VoiceNames
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.DialogButton
import com.shahaabapps.twotracks.ui.components.SegmentedChoice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

/**
 * Picking the voice that reads each language.
 *
 * The app has no voices of its own: they belong to the text-to-speech engine
 * on the phone. So this lists what is installed, reads a line aloud the moment
 * one is chosen, and links to the system page for getting more.
 *
 * Android does not expose a voice's gender — there is no such field — so a
 * voice is labelled male or female where the engine's own name says so, and
 * numbered where it does not. Either way it can be heard before it is kept,
 * which is what actually answers "which one do I want".
 */
@HiltViewModel
class VoiceSettingsViewModel @Inject constructor(
    private val speaker: Speaker,
    private val speechPreferences: SpeechPreferences,
) : ViewModel() {

    /** One voice as the list shows it, with whatever has been said about it. */
    data class VoiceRow(
        val choice: Speaker.VoiceChoice,
        val gender: VoiceGender,
        val character: VoiceCharacter?,
    ) {
        val name: String get() = choice.name
        val label: String get() = VoiceNames.label(gender, character, choice.name)
        val detail: String
            get() = listOfNotNull(choice.region.takeIf { it.isNotBlank() }, choice.detail).joinToString(" · ")
        /** True once somebody has said what this voice is. */
        val described: Boolean get() = gender != VoiceGender.UNKNOWN || character != null
    }

    /** One language's voices, as the list shows them. */
    data class LanguageVoices(
        val course: Course,
        val tag: String,
        val state: Speaker.State,
        val voices: List<VoiceRow>,
        val chosen: String?,
    )

    private val _languages = MutableStateFlow<List<LanguageVoices>>(emptyList())
    val languages: StateFlow<List<LanguageVoices>> = _languages

    /**
     * True until the engine has answered. Starting a text-to-speech engine
     * takes about a second on a cold app and the list cannot be built before
     * it has — measured at 1.1s of that one wait. It no longer blocks
     * anything, but an empty screen for a second reads as broken, so it says
     * what it is doing.
     */
    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    init {
        // The engine holds one language at a time, so each is asked in turn and
        // the answers are kept. Doing it per row instead left every section
        // showing whichever language had most recently won the engine.
        viewModelScope.launch { load() }
    }

    private suspend fun load() {
        _loading.value = true
        val labels = runCatching { speechPreferences.voiceLabels().first() }.getOrDefault(emptyMap())
        _languages.value = Courses.all.mapNotNull { course ->
            val tag = course.speechLanguage ?: return@mapNotNull null
            val state = speaker.stateFor(tag)
            val voices = if (state == Speaker.State.READY) speaker.voicesFor(tag) else emptyList()
            LanguageVoices(
                course = course,
                tag = tag,
                state = state,
                voices = voices.map { row(it, labels) }.sortedWith(
                    // Described voices first: once you have said what one is,
                    // it is the one you are looking for next time.
                    compareByDescending<VoiceRow> { it.described }.thenBy { it.choice.region }.thenBy { it.label },
                ),
                chosen = runCatching { speaker.storedVoiceName(tag).first() }.getOrNull(),
            )
        }
        _loading.value = false
    }

    private fun row(choice: Speaker.VoiceChoice, labels: Map<String, String>): VoiceRow {
        val stored = labels[choice.name]?.split('|').orEmpty()
        val storedGender = stored.getOrNull(0)?.let { g -> VoiceGender.entries.firstOrNull { it.name == g } }
        return VoiceRow(
            choice = choice,
            // What somebody heard beats what the engine's name happened to say.
            gender = storedGender ?: choice.gender,
            character = VoiceCharacter.from(stored.getOrNull(1)),
        )
    }

    fun choose(tag: String, voiceName: String?) {
        viewModelScope.launch {
            speaker.chooseVoice(tag, voiceName, sampleFor(tag))
            _languages.value = _languages.value.map {
                if (it.tag == tag) it.copy(chosen = voiceName) else it
            }
        }
    }

    /** Remembers what a voice turned out to be. Both null clears it. */
    fun describe(voiceName: String, gender: VoiceGender, character: VoiceCharacter?) {
        viewModelScope.launch {
            val value = when {
                gender == VoiceGender.UNKNOWN && character == null -> null
                character == null -> gender.name
                else -> "${gender.name}|${character.name}"
            }
            speechPreferences.setVoiceLabel(voiceName, value)
            load()
        }
    }

    /** Plays a voice again without changing which one is kept. */
    fun preview(tag: String, voiceName: String) {
        viewModelScope.launch { speaker.chooseVoice(tag, voiceName, sampleFor(tag)) }
    }

    /** How fast every language is read. */
    val rate: StateFlow<Float> = speechPreferences.rate().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SpeechPreferences.DEFAULT_RATE)

    fun setRate(r: Float) {
        viewModelScope.launch { speechPreferences.setRate(r) }
    }

    /** Reads a line at the new pace once the slider is let go, so the change can be heard. */
    fun sayAtRate() {
        val lang = _languages.value.firstOrNull { it.state == Speaker.State.READY } ?: return
        viewModelScope.launch { speaker.chooseVoice(lang.tag, lang.chosen, sampleFor(lang.tag)) }
    }

    fun openInstallPage() = speaker.openInstallPage()

    fun stop() = speaker.stop()
}

/** A line in each language, so a voice can be judged on something worth hearing. */
internal fun sampleFor(tag: String): String = when (tag.substringBefore("-").lowercase()) {
    "de" -> "Guten Morgen. Heute lernen wir zusammen."
    "fr" -> "Bonjour. Voici cette voix."
    else -> "Good morning. Here is how this voice sounds."
}

/** "GERMAN · FOR WORDS, DIALOGUES AND HÖREN": the language, and what the course reads in it. */
internal fun voiceHeading(course: Course): String {
    val use = when (course.id) {
        "de-a1" -> "for words, dialogues and Hören"
        "ielts" -> "for IELTS Listening"
        else -> "for ${course.title}"
    }
    return "${course.languageName} · $use".uppercase(Locale.ROOT)
}

/** "0.85×" */
internal fun rateLabel(rate: Float): String = "%.2f×".format(Locale.ENGLISH, rate)

/** How many voices a language shows before it has to be asked for the rest. */
private const val SHOWN_BEFORE_EXPAND = 4

/**
 * One section per language the courses are read in, as the prototype draws
 * it: a mono heading over an ink rule, and for each voice a button to hear
 * it, the engine's own name with what somebody said it sounds like, and a
 * radio to keep it.
 */
@Composable
fun VoiceSettingsSections(viewModel: VoiceSettingsViewModel) {
    val languages by viewModel.languages.collectAsStateWithLifecycle()
    val rate by viewModel.rate.collectAsStateWithLifecycle()
    DisposableEffect(Unit) { onDispose { viewModel.stop() } }
    var describing by remember { mutableStateOf<VoiceSettingsViewModel.VoiceRow?>(null) }
    languages.forEach { lang ->
        var expanded by remember(lang.tag) { mutableStateOf(false) }
        // The chosen voice is always on screen, even when it sits far down the
        // list: a picker that hides what is currently in use is unreadable.
        val shown = if (expanded) lang.voices else {
            (lang.voices.take(SHOWN_BEFORE_EXPAND) + lang.voices.filter { it.name == lang.chosen }).distinct()
        }
        val hidden = lang.voices.size - shown.size
        Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 22.dp)) {
            if (AppTheme.departures) {
                BoardLabel(voiceHeading(lang.course), size = 13.sp, spacing = 0.12.em, modifier = Modifier.padding(bottom = 6.dp).semantics { heading() })
                HorizontalDivider(color = AppTheme.colors.separator, thickness = 1.dp)
            } else {
                Text(voiceHeading(lang.course), style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp), color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(bottom = 4.dp).semantics { heading() })
                HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
            }
            when {
                lang.state == Speaker.State.UNAVAILABLE -> Note("This phone has no text-to-speech engine, so nothing can be read aloud.")
                lang.state == Speaker.State.MISSING_LANGUAGE -> Note("${lang.course.languageName} is not installed on this phone yet. Add it below and its voices appear here.")
                lang.voices.isEmpty() -> Note("This phone offers no separate ${lang.course.languageName} voices, so the engine's own is used.")
            }
            if (lang.state == Speaker.State.READY && lang.voices.isNotEmpty()) {
                VoiceLine(
                    title = "The phone's own voice", mono = false, tags = listOf("default"), chosen = lang.chosen == null,
                    onPlay = null, onTags = null, onChoose = { viewModel.choose(lang.tag, null) },
                )
                shown.forEach { v ->
                    VoiceLine(
                        title = v.name, mono = true,
                        tags = if (v.described) listOfNotNull(v.gender.takeIf { it != VoiceGender.UNKNOWN }?.let { if (it == VoiceGender.FEMALE) "female" else "male" }, v.character?.label) else listOf("label it"),
                        chosen = lang.chosen == v.name,
                        onPlay = { viewModel.preview(lang.tag, v.name) },
                        onTags = { viewModel.preview(lang.tag, v.name); describing = v },
                        onChoose = { viewModel.choose(lang.tag, v.name) },
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                if (hidden > 0) Link("Show $hidden more") { expanded = true }
                Link("Get more voices", viewModel::openInstallPage)
            }
        }
    }
    val board = AppTheme.departures
    Column(
        modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 22.dp)
            .then(if (board) Modifier.clip(RoundedCornerShape(14.dp)).background(AppTheme.colors.card).padding(14.dp) else Modifier),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (board) {
                BoardLabel("Speaking speed", size = 14.sp, weight = FontWeight.Bold, spacing = 0.12.em, modifier = Modifier.weight(1f))
                Text(rateLabel(rate), color = AppTheme.colors.accent, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp))
            } else {
                Text("Speaking speed", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                Text(rateLabel(rate), style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 13.sp))
            }
        }
        val knob = if (board) AppTheme.colors.accent else AppTheme.colors.ink
        Slider(
            value = rate, onValueChange = viewModel::setRate, onValueChangeFinished = viewModel::sayAtRate,
            valueRange = SpeechPreferences.MIN_RATE..SpeechPreferences.MAX_RATE, steps = 9,
            colors = SliderDefaults.colors(thumbColor = knob, activeTrackColor = knob, inactiveTrackColor = AppTheme.colors.separator, activeTickColor = Color.Transparent, inactiveTickColor = Color.Transparent),
            modifier = Modifier.semantics { contentDescription = "Speaking speed" },
        )
        Text("Slower than normal is better for Hören practice in the first months.", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
    }

    describing?.let { row ->
        DescribeVoiceDialog(
            row = row,
            onSave = { g, c -> viewModel.describe(row.name, g, c); describing = null },
            onDismiss = { describing = null },
        )
    }
}

@Composable
private fun Note(text: String) {
    Text(text, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(vertical = 12.dp))
}

@Composable
private fun Link(text: String, onClick: () -> Unit) {
    if (AppTheme.departures) { BoardLink(text, onClick, size = 14.sp, modifier = Modifier.padding(vertical = 12.dp)); return }
    Text(
        text, style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp), color = AppTheme.colors.accentText,
        modifier = Modifier.clip(RoundedCornerShape(6.dp)).clickable(role = Role.Button, onClick = onClick).padding(vertical = 12.dp),
    )
}

@Composable
private fun VoiceLine(title: String, mono: Boolean, tags: List<String>, chosen: Boolean, onPlay: (() -> Unit)?, onTags: (() -> Unit)?, onChoose: () -> Unit) {
    if (AppTheme.departures) { BoardVoiceLine(title, mono, tags, chosen, onPlay, onTags, onChoose); return }
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.heightIn(min = 64.dp)) {
            if (onPlay != null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(40.dp).clip(CircleShape).border(Spacing.rule, AppTheme.colors.separator, CircleShape)
                        .clickable(role = Role.Button, onClickLabel = "Play", onClick = onPlay),
                ) { Icon(Icons.Filled.PlayArrow, contentDescription = "Play $title", tint = AppTheme.colors.ink, modifier = Modifier.size(18.dp)) }
            } else {
                Box(Modifier.size(40.dp))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f).heightIn(min = 56.dp).semantics { selected = chosen }.clickable(role = Role.RadioButton, onClick = onChoose),
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        title, maxLines = 1,
                        style = if (mono) MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 13.sp) else MaterialTheme.typography.bodyMedium,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        tags.forEach { t ->
                            val shape = RoundedCornerShape(11.dp)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.height(22.dp).clip(shape).background(AppTheme.colors.fill)
                                    .then(if (onTags != null) Modifier.clickable(role = Role.Button, onClickLabel = "Say what it sounds like", onClick = onTags) else Modifier)
                                    .padding(horizontal = 8.dp),
                            ) { Text(t, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)) }
                        }
                    }
                }
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(20.dp).border(2.dp, AppTheme.colors.ink, CircleShape)) {
                    Box(Modifier.size(8.dp).clip(CircleShape).background(if (chosen) AppTheme.colors.ink else Color.Transparent))
                }
            }
        }
        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
    }
}

/**
 * Saying what a voice is, after hearing it.
 *
 * The voice plays as the dialog opens, because the only way anyone can answer
 * this is by listening; nothing here is guessed from the voice's name.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DescribeVoiceDialog(
    row: VoiceSettingsViewModel.VoiceRow,
    onSave: (VoiceGender, VoiceCharacter?) -> Unit,
    onDismiss: () -> Unit,
) {
    var gender by remember { mutableStateOf(row.gender) }
    var character by remember { mutableStateOf(row.character) }
    CupertinoDialog(
        onDismissRequest = onDismiss,
        confirmButton = { DialogButton(onClick = { onSave(gender, character) }) { Text("Save") } },
        dismissButton = { DialogButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("What does it sound like?") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                Text(
                    "Playing now. Android will not tell the app whether a voice is a man or a woman, so this is the only way the list can say.",
                    style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                )
                SegmentedChoice(
                    options = listOf(VoiceGender.FEMALE, VoiceGender.MALE, VoiceGender.UNKNOWN),
                    selected = gender,
                    label = { g -> when (g) { VoiceGender.FEMALE -> "Woman"; VoiceGender.MALE -> "Man"; VoiceGender.UNKNOWN -> "Not sure" } },
                    onSelect = { gender = it },
                )
                Text("And how it sounds, if you like", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    VoiceCharacter.entries.forEach { c ->
                        Chip(text = c.label, selected = character == c, onClick = { character = if (character == c) null else c })
                    }
                }
            }
        },
    )
}

/**
 * The voices, on their own screen.
 *
 * They used to sit inside Settings, which is a plain scrolling column, so
 * opening Settings built this view model and everything behind it before the
 * screen could draw — about a second of dropped frames, every time. Here that
 * cost is paid only by someone who has come looking for the voices.
 */
@Composable
fun VoiceSettingsScreen(onBack: () -> Unit) {
    val viewModel: VoiceSettingsViewModel = hiltViewModel()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    if (AppTheme.departures) {
        Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
            Column(modifier = Modifier.fillMaxSize().padding(inner).verticalScroll(rememberScrollState()).padding(bottom = 40.dp)) {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(Modifier.height(44.dp), contentAlignment = Alignment.CenterStart) { BoardBack(onBack, "Back to settings") }
                    BoardMono("THE ANNOUNCER")
                    BoardTitle("Voices", size = 52.sp)
                    BoardText("Your phone names its voices with a code that says nothing about how they sound. Play one, then label it.")
                    if (loading) BoardText("Asking this phone what it can read with…", size = 13.sp)
                }
                VoiceSettingsSections(viewModel)
            }
        }
        return
    }
    DetailScaffold(title = "Voices", onBack = onBack, backLabel = "Back to settings") {
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(bottom = 40.dp)) {
            Text(
                "Your phone names its voices with a code that says nothing about how they sound. Tap one to hear it, then label it so you can find it again.",
                style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(horizontal = Spacing.listHorizontal),
            )
            if (loading) {
                Text(
                    "Asking this phone what it can read with…",
                    style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                    modifier = Modifier.padding(horizontal = Spacing.listHorizontal, vertical = Spacing.md),
                )
            }
            VoiceSettingsSections(viewModel)
        }
    }
}

@Composable
private fun BoardVoiceLine(title: String, mono: Boolean, tags: List<String>, chosen: Boolean, onPlay: (() -> Unit)?, onTags: (() -> Unit)?, onChoose: () -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.heightIn(min = 62.dp)) {
            if (onPlay != null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(AppTheme.colors.card)
                        .clickable(role = Role.Button, onClickLabel = "Play", onClick = onPlay),
                ) { Icon(Icons.Filled.PlayArrow, contentDescription = "Play $title", tint = AppTheme.colors.accent, modifier = Modifier.size(20.dp)) }
            } else {
                Box(Modifier.size(44.dp))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f).heightIn(min = 56.dp).semantics { selected = chosen }.clickable(role = Role.RadioButton, onClick = onChoose),
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (mono) BoardMono(title, color = AppTheme.colors.ink, size = 13.sp, weight = FontWeight.Bold, spacing = 0.sp)
                    else BoardText(title, color = AppTheme.colors.ink)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        tags.forEach { tag ->
                            val shape = RoundedCornerShape(4.dp)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.height(20.dp).clip(shape).border(1.dp, AppTheme.colors.separator, shape)
                                    .then(if (onTags != null) Modifier.clickable(role = Role.Button, onClickLabel = "Say what it sounds like", onClick = onTags) else Modifier)
                                    .padding(horizontal = 6.dp),
                            ) { BoardLabel(if (tag == "label it") "+ Label it" else tag, color = AppTheme.colors.ink, size = 12.sp, spacing = 0.08.em) }
                        }
                    }
                }
                if (chosen) BoardLabel("In use", color = AppTheme.colors.accent, size = 13.sp, weight = FontWeight.Bold, spacing = 0.1.em)
            }
        }
        HorizontalDivider(color = AppTheme.colors.separator, thickness = 1.dp)
    }
}
