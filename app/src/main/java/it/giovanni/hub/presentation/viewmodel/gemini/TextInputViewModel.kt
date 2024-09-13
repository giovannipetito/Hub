package it.giovanni.hub.presentation.viewmodel.gemini

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.ServerException
import it.giovanni.hub.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TextInputViewModel : ViewModel() {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    private val _responseText: MutableState<String?> = mutableStateOf("")
    val responseText: State<String?> = _responseText

    fun generateContent(prompt: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _responseText.value = ""
                val response: GenerateContentResponse = generativeModel.generateContent(prompt)
                _responseText.value = response.text
            } catch (e: ServerException) {
                e.printStackTrace()
                _responseText.value = e.message ?: "Error occurred"
            }
        }
    }

    fun generateContentStream(prompt: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _responseText.value = ""
                generativeModel.generateContentStream(prompt).collect { chunk ->
                    _responseText.value += chunk.text
                }
            } catch (e: ServerException) {
                e.printStackTrace()
                _responseText.value = e.message ?: "Error occurred"
            }
        }
    }
}