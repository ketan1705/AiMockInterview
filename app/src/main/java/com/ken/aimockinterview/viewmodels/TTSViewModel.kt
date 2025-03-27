package com.ken.aimockinterview.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ken.aimockinterview.repository.TTSRepository
import com.ken.aimockinterview.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TTSViewModel @Inject constructor(
    private val textToSpeechRepository: TTSRepository,
) : ViewModel() {

    fun speak(text: String) {
        viewModelScope.launch {
            textToSpeechRepository.speak(text = text)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(Constants.TAG, "TTS ViewModel Destroyed")
        textToSpeechRepository.shutDown()
    }
}