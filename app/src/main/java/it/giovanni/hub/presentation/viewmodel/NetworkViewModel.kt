package it.giovanni.hub.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.data.datasource.remote.NetworkDataSource
import it.giovanni.hub.data.request.NetworkRequest
import it.giovanni.hub.data.response.NetworkResponse
import it.giovanni.hub.domain.result.simple.HubResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val dataSource: NetworkDataSource
) : ViewModel() {

    private val _username: MutableState<String> = mutableStateOf("")
    val username: State<String> = _username

    private val _message: MutableState<String> = mutableStateOf("")
    val message: State<String> = _message

    private val _responseMessage: MutableState<String> = mutableStateOf("")
    val responseMessage: State<String> = _responseMessage

    fun setUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun setMessage(newMessage: String) {
        _message.value = newMessage
    }

    fun sendMessage() {
        viewModelScope.launch(Dispatchers.IO) {
            val request = NetworkRequest(username = _username.value, message = _message.value)
            when (val result: HubResult<NetworkResponse> = dataSource.sendMessage(request)) {
                is HubResult.Success<NetworkResponse> -> {
                    _responseMessage.value = result.data.reply
                }
                is HubResult.Error -> {
                    _responseMessage.value = "Error: ${result.message}"
                }
            }
        }
    }
}