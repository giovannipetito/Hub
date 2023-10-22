package it.giovanni.hub.data.response

import it.giovanni.hub.data.model.Character

data class CharactersResponse(
    var characters: List<Character>? = null
)