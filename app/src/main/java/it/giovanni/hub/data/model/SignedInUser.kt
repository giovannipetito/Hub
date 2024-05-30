package it.giovanni.hub.data.model

data class SignedInUser(
    val uid: String,
    val displayName: String?,
    val photoUrl: String?
)