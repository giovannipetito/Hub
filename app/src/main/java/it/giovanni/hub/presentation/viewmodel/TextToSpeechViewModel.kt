package it.giovanni.hub.presentation.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Locale

class TextToSpeechViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val textToSpeech = TextToSpeech(application.applicationContext, this)

    init {
        textToSpeech.language = Locale.ITALY
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.language = Locale.ITALY
        }
    }

    fun speakText(text: String, language: Locale, pitch: Float = 1.0f, rate: Float = 1.0f) {
        viewModelScope.launch {
            textToSpeech.language = language
            textToSpeech.setPitch(pitch)
            textToSpeech.setSpeechRate(rate)
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}