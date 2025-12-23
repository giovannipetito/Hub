package it.giovanni.hub.data.mapper

import it.giovanni.hub.data.dto.UserDto
import it.giovanni.hub.domain.model.User

/**
 * Mapper toDomain():
 * - Converte UserDto in User del domain layer.
 * - Può arricchire i dati: è un data enrichment lato data/domain boundary.
 *
 * Data layer → Domain model:
 * data dipende da domain (il domain non deve conoscere i DTO); quindi il mapper può
 * visualizzare sia i modelli DTO/DB che i modelli del domain e convertire DTO/Entity ↔ Domain.
 */
fun UserDto.toDomain(): User {
    return User(
        id = id,
        email = email,
        fullName = "$firstName $lastName",
        firstName = firstName,
        lastName = lastName,
        avatar = avatar
    )
}