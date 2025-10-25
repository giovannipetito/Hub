package it.giovanni.hub.domain.model.realtime

data class Customer(
    val displayName: String?,
    val email: String?,
    val messages: List<Message>?
)