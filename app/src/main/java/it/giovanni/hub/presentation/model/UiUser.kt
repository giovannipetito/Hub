package it.giovanni.hub.presentation.model

import androidx.annotation.DrawableRes

data class UiUser(
    var id: Int,
    var email: String,
    var fullName: String,
    var firstName: String,
    var lastName: String,
    var avatar: String,
    var description: String,
    @DrawableRes
    val badgeIds: List<Int> = emptyList()
)