package it.giovanni.hub.presentation.viewmodel

import android.app.Application
import android.os.Environment
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

class TextToSpeechViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val textToSpeech: TextToSpeech = TextToSpeech(application.applicationContext, this)

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

    fun saveSpeechToFile(
        text: String,
        language: Locale,
        fileName: String,
        useExternalStorage: Boolean,
        pitch: Float = 1.0f,
        rate: Float = 1.0f,
        onComplete: (Boolean, File?) -> Unit
    ) {
        viewModelScope.launch {
            textToSpeech.language = language
            textToSpeech.setPitch(pitch)
            textToSpeech.setSpeechRate(rate)

            /**
             * Use Environment.getExternalStorageDirectory() if:
             * - You need the file to be accessible by other apps or the user.
             * - The file should persist even if the app is uninstalled (unless the user deletes it manually).
             * - Example path: /storage/emulated/0/$fileName.wav
             *
             * Use getApplication<Application>().externalCacheDir if:
             * - The file is temporary and related to your app's cache.
             * - You do not want to deal with storage permissions.
             * - It is okay if the file is deleted when the system needs to free up space.
             * - Example path: /storage/emulated/0/Android/data/your.package.name/cache/$fileName.wav
             */

            val file = if (useExternalStorage) {
                File(Environment.getExternalStorageDirectory(), "$fileName.wav")
            } else {
                File(getApplication<Application>().externalCacheDir, "$fileName.wav")
            }

            val result = textToSpeech.synthesizeToFile(text, null, file, fileName)

            if (result == TextToSpeech.SUCCESS) {
                onComplete(true, file)
            } else {
                onComplete(false, null)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}