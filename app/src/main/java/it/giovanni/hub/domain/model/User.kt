package it.giovanni.hub.domain.model

import androidx.annotation.DrawableRes

data class User(
    var id: Int,
    var email: String,
    var firstName: String,
    var lastName: String,
    var avatar: String,
    var description: String,
    @DrawableRes
    val badgeIds: List<Int> = emptyList()
)