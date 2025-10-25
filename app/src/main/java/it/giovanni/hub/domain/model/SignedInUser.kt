package it.giovanni.hub.domain.model

data class SignedInUser(
    val uid: String?,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?
)