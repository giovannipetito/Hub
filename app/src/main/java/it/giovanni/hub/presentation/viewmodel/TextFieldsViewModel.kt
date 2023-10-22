package it.giovanni.hub.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import it.giovanni.hub.enums.SearchWidgetState

class TextFieldsViewModel : ViewModel() {

    /**
     * TextFields
     */

    private var _text1: MutableState<String> = mutableStateOf("")
    val text1: State<String> = _text1

    private var _text2: String by mutableStateOf("")
    val text2: String
        get() = _text2

    fun onText1Changed(input: String) {
        _text1.value = input
    }

    fun onText2Changed(input: String) {
        _text2 = input
    }

    /**
     * Search AppBar
     */

    private val _searchWidgetState: MutableState<SearchWidgetState> =
        mutableStateOf(value = SearchWidgetState.CLOSED)
    val searchWidgetState: State<SearchWidgetState> = _searchWidgetState

    private val _searchTextState: MutableState<String> =
        mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        _searchWidgetState.value = newValue
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }
}