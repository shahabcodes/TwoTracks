package com.shahaabapps.twotracks.feature.learn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Speech recognition for the Sprechen exercises, through the phone's own
 * recogniser so nothing leaves the device beyond what the system service
 * does. One shared instance; the UI starts a listen and reads [state].
 */
@Singleton
class Recognizer @Inject constructor(@ApplicationContext private val context: Context) {

    /**
     * One recogniser serves every exercise on the page, so a result has to say
     * whose it is. Without [owner] a single utterance landed in every Sprechen
     * card on screen at once, scoring the others against words never spoken.
     */
    sealed interface State {
        val owner: String? get() = null

        data object Idle : State
        data class Listening(override val owner: String) : State
        data class Heard(override val owner: String, val text: String) : State
        data class Failed(override val owner: String, val reason: String) : State
        data object Unavailable : State
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state
    private val main = Handler(Looper.getMainLooper())
    private var recognizer: SpeechRecognizer? = null
    private var language = "de-DE"

    val available: Boolean get() = SpeechRecognizer.isRecognitionAvailable(context)

    fun setLanguage(tag: String) { language = tag }

    /** The language being listened for, in words, for messages the reader sees. */
    private val languageName: String
        get() = when (language.substringBefore("-")) {
            "de" -> "German"; "en" -> "English"; "fr" -> "French"; else -> language
        }

    /**
     * Starts one utterance for [owner]; the result lands in [state] as Heard or
     * Failed, tagged with that owner so only the exercise that asked reads it.
     */
    fun listen(owner: String) {
        if (!available) { _state.value = State.Unavailable; return }
        main.post {
            val r = recognizer ?: SpeechRecognizer.createSpeechRecognizer(context).also { recognizer = it }
            r.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) { _state.value = State.Listening(owner) }
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onError(error: Int) {
                    _state.value = when (error) {
                        SpeechRecognizer.ERROR_NO_MATCH, SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> State.Failed(owner, "Nothing heard. Hold the phone closer and try again.")
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> State.Failed(owner, "Microphone permission is needed.")
                        SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED, SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE -> State.Failed(owner, "$languageName recognition is not installed on this phone.")
                        SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> State.Failed(owner, "The recogniser needs a connection on this phone.")
                        else -> State.Failed(owner, "Recognition failed (code $error). Try again.")
                    }
                }
                override fun onResults(results: Bundle?) {
                    val best = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                    _state.value = if (best.isNullOrBlank()) State.Failed(owner, "Nothing heard. Try again.") else State.Heard(owner, best)
                }
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            }
            _state.value = State.Listening(owner)
            runCatching { r.startListening(intent) }.onFailure { _state.value = State.Failed(owner, "Could not start the recogniser.") }
            // Some devices never time out without audio; give up after a while so the button comes back.
            val token = ++listenToken
            main.postDelayed({
                if (listenToken == token && _state.value is State.Listening) {
                    runCatching { r.cancel() }
                    _state.value = State.Failed(owner, "Nothing heard in 12 seconds. Tap the mic and speak straight away.")
                }
            }, 12_000)
        }
    }

    private var listenToken = 0

    fun stop() { main.post { recognizer?.stopListening() } }

    /**
     * Clears the state, but only when it belongs to [owner], so leaving one
     * exercise cannot wipe the result another is still showing.
     */
    fun reset(owner: String) { if (_state.value.owner == owner) _state.value = State.Idle }
}
