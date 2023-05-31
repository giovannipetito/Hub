package it.giovanni.hub.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TextFieldsViewModel : ViewModel() {

    // private val _text = MutableLiveData("")
    // val text: LiveData<String> = _text

    // var _text: MutableStateFlow<TextFieldState> = MutableStateFlow(TextFieldState())
    // val text: StateFlow<TextFieldState> = _text.asStateFlow()

    private var _text1: MutableState<String> = mutableStateOf("")
    val text1: State<String> = _text1

    private var _text2: String by mutableStateOf("")
    val text2: String
        get() = _text2

    // onTextChanged is an event we are defining that the UI can invoke (Events flow up from UI).

    fun onText1Changed(input: String) {
        _text1.value = input
    }

    fun onText2Changed(input: String) {
        _text2 = input
    }
}