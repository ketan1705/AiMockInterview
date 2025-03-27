package com.ken.aimockinterview.states

data class VoiceToTextParserState(

    val isSpeaking: Boolean = false,
    val spokenText: String = "",
    val error: String? = null,
)