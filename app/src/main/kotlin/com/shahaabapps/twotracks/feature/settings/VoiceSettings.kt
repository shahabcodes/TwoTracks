package com.shahaabapps.twotracks.feature.settings

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.content.Course
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.IosColors
import com.shahaabapps.twotracks.feature.learn.Speaker
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
) : ViewModel() {

    /** One language's voices, as the list shows them. */
    data class LanguageVoices(
        val course: Course,
        val tag: String,
        val state: Speaker.State,
        val voices: List<Speaker.VoiceChoice>,
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
        _languages.value = Courses.all.mapNotNull { course ->
            val tag = course.speechLanguage ?: return@mapNotNull null
            val state = speaker.stateFor(tag)
            LanguageVoices(
                course = course,
                tag = tag,
                state = state,
                voices = if (state == Speaker.State.READY) speaker.voicesFor(tag) else emptyList(),
                chosen = runCatching { speaker.storedVoiceName(tag).first() }.getOrNull(),
            )
        }
    }

    fun choose(tag: String, voiceName: String?) {
        viewModelScope.launch {
            speaker.chooseVoice(tag, voiceName, sampleFor(tag))
            _languages.value = _languages.value.map {
                if (it.tag == tag) it.copy(chosen = voiceName) else it
            }
        }
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

/** One section per language the courses are read in. */
@Composable
fun VoiceSettingsSections(viewModel: VoiceSettingsViewModel) {
    val languages by viewModel.languages.collectAsStateWithLifecycle()
    DisposableEffect(Unit) { onDispose { viewModel.stop() } }
    languages.forEach { lang ->
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
                    "Tap one to hear it and keep it. ${lang.course.languageName} is read in this voice everywhere in the app."
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
                lang.voices.forEach { v ->
                    CupertinoListRow(
                        v.label,
                        subtitle = v.detail,
                        leadingIcon = Icons.Filled.VolumeUp,
                        iconColor = IosColors.Indigo,
                        onClick = { viewModel.choose(lang.tag, v.name) },
                        trailing = { if (lang.chosen == v.name) Chosen() },
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
}

@Composable
private fun Chosen() {
    Icon(Icons.Filled.Check, contentDescription = "Chosen", tint = AppTheme.colors.accent, modifier = Modifier.size(20.dp))
}
