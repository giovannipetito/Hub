package it.giovanni.hub.domain.model

/**
 * Clean Architecture:
 * User è un domain model.
 */
data class User(
    var id: Int,
    var email: String,
    var fullName: String,
    var firstName: String,
    var lastName: String,
    var avatar: String
)