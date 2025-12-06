package it.giovanni.hub.presentation.mapper

import it.giovanni.hub.domain.model.User
import it.giovanni.hub.presentation.model.UiUser
import it.giovanni.hub.utils.Constants

/**
 * Mapper toPresentation():
 * - Converte User in UiUser del presentation layer.
 * - Può arricchire i dati: è un data enrichment lato domain/presentation boundary.
 *
 * Domain model → UI model (presentation):
 * presentation dipende da domain (domain non deve conoscere presentation), quindi il mapper può
 * visualizzare sia i modelli del domain che i modelli della UI e convertire Domain ↔ UiModel.
 */
fun User.toPresentation(): UiUser {
    val description: String = Constants.LOREM_IPSUM_LONG_TEXT
    val badgeIds: List<Int> = Constants.ICON_IDS
    return UiUser(
        id = id,
        email = email,
        fullName = fullName,
        firstName = firstName,
        lastName = lastName,
        avatar = avatar,
        description = description,
        badgeIds = badgeIds
    )
}