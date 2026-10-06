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

    sealed interface State {
        data object Idle : State
        data object Listening : State
        data class Heard(val text: String) : State
        data class Failed(val reason: String) : State
        data object Unavailable : State
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state
    private val main = Handler(Looper.getMainLooper())
    private var recognizer: SpeechRecognizer? = null
    private var language = "de-DE"

    val available: Boolean get() = SpeechRecognizer.isRecognitionAvailable(context)

    fun setLanguage(tag: String) { language = tag }

    /** Starts one utterance; the result lands in [state] as Heard or Failed. */
    fun listen() {
        if (!available) { _state.value = State.Unavailable; return }
        main.post {
            val r = recognizer ?: SpeechRecognizer.createSpeechRecognizer(context).also { recognizer = it }
            r.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) { _state.value = State.Listening }
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onError(error: Int) {
                    _state.value = when (error) {
                        SpeechRecognizer.ERROR_NO_MATCH, SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> State.Failed("Nothing heard. Hold the phone closer and try again.")
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> State.Failed("Microphone permission is needed.")
                        SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED, SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE -> State.Failed("German recognition is not installed on this phone.")
                        SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> State.Failed("The recogniser needs a connection on this phone.")
                        else -> State.Failed("Recognition failed (code $error). Try again.")
                    }
                }
                override fun onResults(results: Bundle?) {
                    val best = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                    _state.value = if (best.isNullOrBlank()) State.Failed("Nothing heard. Try again.") else State.Heard(best)
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
            _state.value = State.Listening
            runCatching { r.startListening(intent) }.onFailure { _state.value = State.Failed("Could not start the recogniser.") }
            // Some devices never time out without audio; give up after a while so the button comes back.
            val token = ++listenToken
            main.postDelayed({
                if (listenToken == token && _state.value is State.Listening) {
                    runCatching { r.cancel() }
                    _state.value = State.Failed("Nothing heard in 12 seconds. Tap the mic and speak straight away.")
                }
            }, 12_000)
        }
    }

    private var listenToken = 0

    fun stop() { main.post { recognizer?.stopListening() } }
    fun reset() { _state.value = State.Idle }
}
