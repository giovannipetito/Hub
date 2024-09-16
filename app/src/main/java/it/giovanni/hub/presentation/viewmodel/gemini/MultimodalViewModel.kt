package it.giovanni.hub.presentation.viewmodel.gemini

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.ServerException
import com.google.ai.client.generativeai.type.content
import it.giovanni.hub.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MultimodalViewModel : ViewModel() {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash", // pro
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    private val _contentResponse: MutableState<String?> = mutableStateOf(null)
    val contentResponse: State<String?> = _contentResponse

    fun generateContentText(prompt: String, onCompletion: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _contentResponse.value = ""
                val response: GenerateContentResponse = generativeModel.generateContent(prompt)
                _contentResponse.value = response.text
                onCompletion(true)
            } catch (e: ServerException) {
                e.printStackTrace()
                _contentResponse.value = e.message ?: "Error occurred"
                onCompletion(false)
            }
        }
    }

    fun generateContentTextStream(prompt: String, onCompletion: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _contentResponse.value = ""
                generativeModel.generateContentStream(prompt).collect { chunk ->
                    _contentResponse.value += chunk.text
                }
                onCompletion(true)
            } catch (e: ServerException) {
                e.printStackTrace()
                _contentResponse.value = e.message ?: "Error occurred"
                onCompletion(false)
            }
        }
    }

    fun generateContentImage(prompt: String, image: Bitmap, onCompletion: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputContent: Content = content {
                    image(image = image)
                    text(text = prompt)
                }
                _contentResponse.value = ""
                val response: GenerateContentResponse = generativeModel.generateContent(inputContent)
                _contentResponse.value = response.text
                onCompletion(true)
            } catch (e: ServerException) {
                e.printStackTrace()
                _contentResponse.value = e.message ?: "Error occurred"
                onCompletion(false)
            }
        }
    }

    fun generateContentImageStream(prompt: String, image: Bitmap, onCompletion: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputContent = content {
                    image(image = image)
                    text(text = prompt)
                }
                _contentResponse.value = ""
                generativeModel.generateContentStream(inputContent).collect { chunk ->
                    _contentResponse.value += chunk.text
                }
                onCompletion(true)
            } catch (e: ServerException) {
                e.printStackTrace()
                _contentResponse.value = e.message ?: "Error occurred"
                onCompletion(false)
            }
        }
    }

    fun generateContentImages(prompt: String, images: List<Bitmap>, onCompletion: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputContent: Content = content {
                    for (image in images) {
                        image(image = image)
                    }
                    text(text = prompt)
                }
                _contentResponse.value = ""
                val response: GenerateContentResponse = generativeModel.generateContent(inputContent)
                _contentResponse.value = response.text
                onCompletion(true)
            } catch (e: ServerException) {
                e.printStackTrace()
                _contentResponse.value = e.message ?: "Error occurred"
                onCompletion(false)
            }
        }
    }

    fun generateContentImagesStream(prompt: String, images: List<Bitmap>, onCompletion: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputContent = content {
                    for (image in images) {
                        image(image = image)
                    }
                    text(text = prompt)
                }
                _contentResponse.value = ""
                generativeModel.generateContentStream(inputContent).collect { chunk ->
                    _contentResponse.value += chunk.text
                }
                onCompletion(true)
            } catch (e: ServerException) {
                e.printStackTrace()
                _contentResponse.value = e.message ?: "Error occurred"
                onCompletion(false)
            }
        }
    }
}