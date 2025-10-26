package it.giovanni.hub.data.mapper

import it.giovanni.hub.data.dto.UserDto
import it.giovanni.hub.domain.model.User
import it.giovanni.hub.utils.Constants

fun UserDto.toDomain(): User {
    val description: String = Constants.LOREM_IPSUM_LONG_TEXT
    val badgeIds: List<Int> = Constants.ICON_IDS
    return User(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        avatar = avatar,
        description = description,
        badgeIds = badgeIds
    )
}