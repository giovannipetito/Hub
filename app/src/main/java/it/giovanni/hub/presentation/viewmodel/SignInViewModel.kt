package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import it.giovanni.hub.data.model.SignInState
import it.giovanni.hub.data.response.SignInResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel: ViewModel() {

    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    fun onSignInResponse(response: SignInResponse) {
        _signInState.update {
            it.copy(
                isSignInSuccessful = response.user != null,
                signInError = response.errorMessage
            )
        }
    }

    fun resetSignInState() {
        _signInState.update {
            SignInState()
        }
    }
}