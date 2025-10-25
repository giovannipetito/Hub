package it.giovanni.hub.domain.model.realtime

data class Message(
    val text: String? = "",
    val timestamp: Long? = 0L
)