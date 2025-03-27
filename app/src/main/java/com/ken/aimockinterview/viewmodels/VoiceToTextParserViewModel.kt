package com.ken.aimockinterview.viewmodels

import androidx.lifecycle.ViewModel
import com.ken.aimockinterview.components.VoiceToTextParser
import com.ken.aimockinterview.states.VoiceToTextParserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class VoiceToTextParserViewModel @Inject constructor(private val voiceToTextParser: VoiceToTextParser) :
    ViewModel() {

    val state: StateFlow<VoiceToTextParserState> = voiceToTextParser.state

    fun startListening() {
        voiceToTextParser.startListening()
    }

    fun stopListening() {
        voiceToTextParser.stopListening()
    }

}