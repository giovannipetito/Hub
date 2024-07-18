package it.giovanni.hub.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

class TextToSpeechViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val textToSpeech: TextToSpeech = TextToSpeech(application.applicationContext, this)

    private val _file: MutableState<File> = mutableStateOf(File(""))
    var file: State<File> = _file

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
             * - the file should be accessible by other apps or the user.
             * - The file should persist even if the app is uninstalled (unless the user deletes it manually).
             * - Example path: /storage/emulated/0/$fileName.mp3 // .wav
             *
             * Use getApplication<Application>().externalCacheDir if:
             * - the file is temporary and related to app cache.
             * - You don't want to deal with storage permissions.
             * - It is okay if the file is deleted when the system needs to free up space.
             * - Example path: /storage/emulated/0/Android/data/your.package.name/cache/$fileName.mp3 // .wav
             */

            val file = if (useExternalStorage) {
                val publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                if (!publicDir.exists()) {
                    publicDir.mkdirs()
                }
                File(publicDir, "$fileName.mp3") // File(Environment.getExternalStorageDirectory(), "$fileName.mp3")
            } else {
                File(getApplication<Application>().externalCacheDir, "$fileName.mp3") // .wav
            }

            val result = textToSpeech.synthesizeToFile(text, null, file, fileName)

            if (result == TextToSpeech.SUCCESS) {
                onComplete(true, file)
            } else {
                onComplete(false, null)
            }
        }
    }

    fun setFile(file: File) {
        _file.value = file
    }

    fun showFileDialog(context: Context, file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "audio/mpeg") // "audio/wav"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(intent, "Open file with")
        context.startActivity(chooser)
    }

    fun shareFile(context: Context, file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}.provider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "audio/mpeg" // "audio/wav"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share file via"))
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}