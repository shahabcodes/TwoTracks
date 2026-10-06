package com.shahaabapps.twotracks.feature.learn

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Text-to-speech for the language courses, using the phone's voices so it
 * works offline. [state] tells the UI whether the language is ready, missing
 * (so it can offer the download page) or unavailable.
 */
@Singleton
class Speaker @Inject constructor(@ApplicationContext private val context: Context) {

    enum class State { INIT, READY, MISSING_LANGUAGE, UNAVAILABLE }

    private val _state = MutableStateFlow(State.INIT)
    val state: StateFlow<State> = _state
    private var language: Locale = Locale.GERMANY
    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.SUCCESS) { _state.value = State.UNAVAILABLE; return@TextToSpeech }
            applyLanguage()
        }
    }

    private fun applyLanguage() {
        val engine = tts ?: return
        val r = engine.setLanguage(language)
        _state.value = if (r == TextToSpeech.LANG_MISSING_DATA || r == TextToSpeech.LANG_NOT_SUPPORTED) State.MISSING_LANGUAGE else State.READY
        if (_state.value == State.READY) engine.setSpeechRate(0.9f)
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
