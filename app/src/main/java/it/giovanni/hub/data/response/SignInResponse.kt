package it.giovanni.hub.data.response

import it.giovanni.hub.data.model.SignedInUser

data class SignInResponse(
    val user: SignedInUser?,
    val errorMessage: String?
)