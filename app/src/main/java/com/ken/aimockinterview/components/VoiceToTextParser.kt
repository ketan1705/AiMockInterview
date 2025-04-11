package com.ken.aimockinterview.components

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.ken.aimockinterview.states.VoiceToTextParserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceToTextParser @Inject constructor(
    private val app: Application,
    private val recognizer: SpeechRecognizer,
) : RecognitionListener {

    private val _state = MutableStateFlow(
        VoiceToTextParserState()
    )

    val state = _state.asStateFlow()

    //    val recognizer = SpeechRecognizer.createSpeechRecognizer(app)
    //    private var isManualStop = false  // Track if user manually stopped listening

    init {
        recognizer.setRecognitionListener(this)
    }

    fun startListening(langCode: String = "en") {
        _state.update { VoiceToTextParserState() }

        if (!SpeechRecognizer.isRecognitionAvailable(app)) {
            _state.update {
                it.copy(
                    error = "Speech Recognition is not available"
                )
            }
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            // ACTION_RECOGNIZE_SPEECH
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, langCode)
        }

//        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)
        _state.update {
            it.copy(
                isSpeaking = true
            )
        }
    }

    fun stopListening() {
//        isManualStop = true
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
        recognizer.stopListening()
    }

    override fun onReadyForSpeech(p0: Bundle?) {
        _state.update {
            it.copy(error = null)
        }
    }

    override fun onBeginningOfSpeech() = Unit

    override fun onRmsChanged(p0: Float) = Unit

    override fun onBufferReceived(p0: ByteArray?) = Unit

    override fun onEndOfSpeech() {

        _state.update {
            it.copy(isSpeaking = false)
        }
//        if (!isManualStop) {
//            startListening()
//        }
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT) {
            return
        }
        _state.update {
            it.copy(
                error = "Error: $error"
            )
        }
    }

    override fun onResults(results: Bundle?) {

        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//            ?.get(0)
            ?.firstOrNull()
            ?.let { result ->
                _state.update {
                    it.copy(
                        spokenText = _state.value.spokenText + " " + result
                    )
                }
            }
    }

    override fun onPartialResults(partialResult: Bundle?) {
        partialResult
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//            ?.get(0)
            ?.firstOrNull()
            ?.let { result ->
                _state.update {
                    it.copy(
                        spokenText = _state.value.spokenText + " " + result
                    )
                }
            }
    }

    override fun onEvent(p0: Int, p1: Bundle?) = Unit

}