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
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    private val _responseText: MutableState<String?> = mutableStateOf(null)
    val responseText: State<String?> = _responseText

    fun generateContentImage(prompt: String, image: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // val image: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.image)
                val inputContent: Content = content {
                    image(image = image)
                    text(text = prompt) // Example: What's in this picture?
                }
                _responseText.value = ""
                val response: GenerateContentResponse = generativeModel.generateContent(inputContent)
                _responseText.value = response.text
            } catch (e: ServerException) {
                e.printStackTrace()
                _responseText.value = e.message ?: "Error occurred"
            }
        }
    }

    fun generateContentImageStream(prompt: String, image: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputContent = content {
                    image(image = image)
                    text(text = prompt)
                }
                _responseText.value = ""
                generativeModel.generateContentStream(inputContent).collect { chunk ->
                    _responseText.value += chunk.text
                }
            } catch (e: ServerException) {
                e.printStackTrace()
                _responseText.value = e.message ?: "Error occurred"
            }
        }
    }

    fun generateContentImages(prompt: String, images: List<Bitmap>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputContent: Content = content {
                    for (image in images) {
                        image(image = image)
                    }
                    text(text = prompt) // What's the difference between these pictures?
                }
                _responseText.value = ""
                val response: GenerateContentResponse = generativeModel.generateContent(inputContent)
                _responseText.value = response.text
            } catch (e: ServerException) {
                e.printStackTrace()
                _responseText.value = e.message ?: "Error occurred"
            }
        }
    }

    fun generateContentImagesStream(prompt: String, images: List<Bitmap>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputContent = content {
                    for (image in images) {
                        image(image = image)
                    }
                    text(text = prompt)
                }
                _responseText.value = ""
                generativeModel.generateContentStream(inputContent).collect { chunk ->
                    _responseText.value += chunk.text
                }
            } catch (e: ServerException) {
                e.printStackTrace()
                _responseText.value = e.message ?: "Error occurred"
            }
        }
    }
}