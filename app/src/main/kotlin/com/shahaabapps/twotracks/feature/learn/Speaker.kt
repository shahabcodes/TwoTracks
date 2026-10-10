package com.shahaabapps.twotracks.feature.learn

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import com.shahaabapps.core.common.datastore.SpeechPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Text-to-speech for the language courses, using the phone's voices so it
 * works offline. [state] tells the UI whether the language is ready, missing
 * (so it can offer the download page) or unavailable.
 *
 * Which voice reads a language is the person's choice and is remembered per
 * language. The app cannot supply voices of its own — they belong to the
 * engine installed on the phone — so what it can do is show every one that is
 * there, let it be heard before it is chosen, and point at the system page for
 * installing more.
 */
@Singleton
class Speaker @Inject constructor(
    @ApplicationContext private val context: Context,
    private val speechPreferences: SpeechPreferences,
) {

    enum class State { INIT, READY, MISSING_LANGUAGE, UNAVAILABLE }

    /** One installed voice, as the settings list shows it. */
    data class VoiceChoice(
        /** The engine's own name, which is what gets stored and set. */
        val name: String,
        val label: String,
        val gender: VoiceGender,
        val detail: String,
        /** Country the voice speaks for, e.g. "GB". Empty when the name does not say. */
        val region: String = "",
    )

    private val _state = MutableStateFlow(State.INIT)
    val state: StateFlow<State> = _state

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var language: Locale = Locale.GERMANY

    /** Written on a background thread, read from several; hence volatile. */
    @Volatile
    private var tts: TextToSpeech? = null

    init {
        // Built off the main thread. This is a singleton injected into a view
        // model, so it is constructed during composition — and constructing a
        // TextToSpeech binds to the speech service and looks up the engine,
        // which froze the Settings screen for about a second on first open.
        scope.launch {
            tts = TextToSpeech(context) { status ->
                if (status != TextToSpeech.SUCCESS) { _state.value = State.UNAVAILABLE; return@TextToSpeech }
                applyLanguage()
            }
        }
    }

    /**
     * The engine's calls are binder round trips, and the init callback that
     * gets us here runs on the main thread — so setting the language from it
     * blocked the UI. Everything the engine is asked now happens off it.
     */
    private fun applyLanguage() {
        scope.launch { applyLanguageNow() }
    }

    private fun applyLanguageNow() {
        val engine = tts ?: return
        val r = engine.setLanguage(language)
        _state.value = if (r == TextToSpeech.LANG_MISSING_DATA || r == TextToSpeech.LANG_NOT_SUPPORTED) State.MISSING_LANGUAGE else State.READY
        if (_state.value != State.READY) return
        engine.setSpeechRate(0.9f)
        // The stored choice is applied here rather than at start-up, because
        // a voice can only be set once the engine has a language.
        scope.launch {
            val tag = language.toLanguageTag()
            applyVoice(runCatching { speechPreferences.voiceName(tag).first() }.getOrNull())
        }
    }

    /** Waits for the engine to finish starting up, then reports what it can do. */
    private suspend fun awaitReady(): State = _state.first { it != State.INIT }

    /**
     * The voices installed for [tag], best first.
     *
     * The engine holds one language at a time, so asking for a language's
     * voices necessarily switches it there. The learn screens set their own
     * language when they open, which puts it back.
     */
    suspend fun voicesFor(tag: String): List<VoiceChoice> = withContext(Dispatchers.IO) {
        if (awaitReady() == State.UNAVAILABLE) return@withContext emptyList()
        setLanguage(tag)
        val engine = tts ?: return@withContext emptyList()
        if (_state.value != State.READY) return@withContext emptyList()
        listVoices(engine)
    }

    /** Whether [tag] can be spoken at all on this phone. */
    suspend fun stateFor(tag: String): State = withContext(Dispatchers.IO) {
        if (awaitReady() == State.UNAVAILABLE) return@withContext State.UNAVAILABLE
        setLanguage(tag)
        _state.value
    }

    /** The voice stored for [tag], or null for the engine's own. */
    fun storedVoiceName(tag: String): Flow<String?> = speechPreferences.voiceName(tag)

    /**
     * The installed voices for the current language, local before network and
     * better quality first, numbered within each gender.
     *
     * getVoices has been known to throw or return null on some engines, and a
     * crash here would take out a topic screen, so it is allowed to come back
     * empty: the engine's default voice still reads the text.
     */
    private fun listVoices(engine: TextToSpeech): List<VoiceChoice> {
        val all = runCatching { engine.voices?.toList() }.getOrNull().orEmpty()
        val mine = all.filter { v ->
            runCatching {
                v.locale.language == language.language &&
                    // A voice the engine has listed but not downloaded cannot
                    // speak. Offering it is how the picker came to be full of
                    // voices that do nothing when tapped.
                    VoiceNames.NOT_INSTALLED !in v.features.orEmpty() &&
                    // The engine's own default, already the first row.
                    !VoiceNames.isLegacyLocaleVoice(v.name)
            }.getOrDefault(false)
        }.sortedWith(
            compareBy(
                { it.isNetworkConnectionRequired },
                { -it.quality },
                { it.name },
            ),
        )
        // One row per voice, not one per way of fetching it: the local and the
        // network copy of `en-us-x-iob` are the same voice. Sorted local-first
        // above, so the first of each pair is the one that works offline.
        return mine
            .distinctBy { VoiceNames.regionOf(it.name) + "/" + VoiceNames.variantOf(it.name) }
            .map { v ->
                val gender = VoiceNames.genderOf(v.name)
                VoiceChoice(
                    name = v.name,
                    label = VoiceNames.placeholderLabel(v.name),
                    gender = gender,
                    detail = VoiceNames.detail(v.isNetworkConnectionRequired, v.quality),
                    region = VoiceNames.regionOf(v.name),
                )
            }
    }

    /**
     * Uses [name] for [tag] from now on, and reads [sample] back so the choice
     * can be heard as it is made. A null name returns to the engine's own.
     *
     * The language is set here rather than by the caller, so the choice cannot
     * be stored against whichever language the engine happened to hold.
     */
    suspend fun chooseVoice(tag: String, name: String?, sample: String?) {
        if (awaitReady() == State.UNAVAILABLE) return
        setLanguage(tag)
        speechPreferences.setVoiceName(tag, name)
        applyVoice(name)
        sample?.let { speak(it) }
    }

    private fun applyVoice(name: String?) {
        val engine = tts ?: return
        val voice: Voice? = name?.let { wanted ->
            runCatching { engine.voices?.firstOrNull { it.name == wanted } }.getOrNull()
        }
        // A stored voice that is no longer installed leaves the engine on its
        // default, which reads the text in a different voice rather than none.
        if (voice != null) runCatching { engine.setVoice(voice) }
    }

    fun setLanguage(tag: String) {
        val loc = Locale.forLanguageTag(tag)
        if (loc != language) { language = loc; if (tts != null) applyLanguage() }
    }

    fun speak(text: String, slow: Boolean = false) {
        val engine = tts ?: return
        if (_state.value != State.READY) return
        engine.setSpeechRate(if (slow) 0.6f else 0.9f)
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tt-" + text.hashCode())
    }

    fun stop() { tts?.stop() }

    /** Opens the system page where the German voice can be installed. */
    fun openInstallPage() {
        val i = Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        runCatching { context.startActivity(i) }
    }
}
