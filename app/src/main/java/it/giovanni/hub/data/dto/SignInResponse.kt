package it.giovanni.hub.data.dto

import it.giovanni.hub.domain.model.SignedInUser

data class SignInResponse(
    val user: SignedInUser?,
    val errorMessage: String?
)