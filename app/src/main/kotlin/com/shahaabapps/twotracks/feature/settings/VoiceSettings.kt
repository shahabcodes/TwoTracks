package com.shahaabapps.twotracks.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.content.Course
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.common.datastore.SpeechPreferences
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.IosColors
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.feature.learn.Speaker
import com.shahaabapps.twotracks.feature.learn.VoiceCharacter
import com.shahaabapps.twotracks.feature.learn.VoiceGender
import com.shahaabapps.twotracks.feature.learn.VoiceNames
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import com.shahaabapps.twotracks.ui.components.DialogButton
import com.shahaabapps.twotracks.ui.components.SegmentedChoice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

    init {
        // The engine holds one language at a time, so each is asked in turn and
        // the answers are kept. Doing it per row instead left every section
        // showing whichever language had most recently won the engine.
        viewModelScope.launch { load() }
    }

    private suspend fun load() {
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

    fun openInstallPage() = speaker.openInstallPage()

    fun stop() = speaker.stop()
}

/** A line in each language, so a voice can be judged on something worth hearing. */
internal fun sampleFor(tag: String): String = when (tag.substringBefore("-").lowercase()) {
    "de" -> "Guten Morgen. Heute lernen wir zusammen."
    "fr" -> "Bonjour. Voici cette voix."
    else -> "Good morning. Here is how this voice sounds."
}

/** How many voices a language shows before it has to be asked for the rest. */
private const val SHOWN_BEFORE_EXPAND = 4

/** One section per language the courses are read in. */
@Composable
fun VoiceSettingsSections(viewModel: VoiceSettingsViewModel) {
    val languages by viewModel.languages.collectAsStateWithLifecycle()
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
        CupertinoListSection(
            header = "${lang.course.languageName} voice",
            footer = when {
                lang.state == Speaker.State.UNAVAILABLE ->
                    "This phone has no text-to-speech engine, so nothing can be read aloud."
                lang.state == Speaker.State.MISSING_LANGUAGE ->
                    "${lang.course.languageName} is not installed on this phone yet. Add it below and the voices appear here."
                lang.voices.isEmpty() ->
                    "This phone offers no separate ${lang.course.languageName} voices, so the engine's own is used."
                else ->
                    "Tap one to hear it and keep it. Android does not say whether a voice is a man or a woman, " +
                        "so tap Describe once you have heard it and it will say so from then on."
            },
        ) {
            if (lang.state == Speaker.State.READY && lang.voices.isNotEmpty()) {
                CupertinoListRow(
                    "The engine's own voice",
                    subtitle = "Whatever the phone picks by default",
                    leadingIcon = Icons.Filled.RecordVoiceOver,
                    iconColor = IosColors.Blue,
                    onClick = { viewModel.choose(lang.tag, null) },
                    trailing = { if (lang.chosen == null) Chosen() },
                )
                shown.forEach { v ->
                    CupertinoListRow(
                        v.label,
                        subtitle = v.detail,
                        leadingIcon = Icons.Filled.VolumeUp,
                        iconColor = IosColors.Indigo,
                        onClick = { viewModel.choose(lang.tag, v.name) },
                        trailing = {
                            if (lang.chosen == v.name) Chosen()
                            Chip(text = "Describe", onClick = { viewModel.preview(lang.tag, v.name); describing = v })
                        },
                    )
                }
                if (hidden > 0) {
                    CupertinoListRow(
                        "Show $hidden more",
                        subtitle = "Every voice this phone has for ${lang.course.languageName}",
                        leadingIcon = Icons.Filled.ExpandMore,
                        iconColor = IosColors.Gray,
                        onClick = { expanded = true },
                    )
                }
            }
            CupertinoListRow(
                "Get more voices",
                subtitle = "Opens Android's speech settings",
                leadingIcon = Icons.Filled.Download,
                iconColor = IosColors.Green,
                showDivider = false,
                onClick = viewModel::openInstallPage,
            )
        }
    }

    describing?.let { row ->
        DescribeVoiceDialog(
            row = row,
            onSave = { g, c -> viewModel.describe(row.name, g, c); describing = null },
            onDismiss = { describing = null },
        )
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
                        Chip(
                            text = if (character == c) "${c.label} ✓" else c.label,
                            onClick = { character = if (character == c) null else c },
                        )
                    }
                }
            }
        },
    )
}

@Composable
private fun Chosen() {
    Icon(Icons.Filled.Check, contentDescription = "Chosen", tint = AppTheme.colors.accent, modifier = Modifier.size(20.dp))
}
