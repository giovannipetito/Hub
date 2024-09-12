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

    fun generateContent(prompt: String, image: Bitmap) {
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
}