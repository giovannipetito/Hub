package it.giovanni.hub.data.model.realtime

data class Customer(
    val displayName: String?,
    val email: String?,
    val messages: List<Message>?
)