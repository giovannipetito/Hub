package it.giovanni.hub.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.ServerException
import it.giovanni.hub.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TextInputViewModel : ViewModel() {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    private val _responseText: MutableState<String?> = mutableStateOf(null)
    val responseText: State<String?> = _responseText

    fun generateContent(prompt: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _responseText.value = generativeModel.generateContent(prompt).text
            } catch (e: ServerException) {
                e.printStackTrace()
                _responseText.value = e.message
            }
        }
    }
}