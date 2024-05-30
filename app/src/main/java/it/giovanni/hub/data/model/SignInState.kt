package it.giovanni.hub.data.model

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)