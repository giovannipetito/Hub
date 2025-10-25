package it.giovanni.hub.data.response

import com.google.gson.annotations.SerializedName
import it.giovanni.hub.domain.model.Character

data class CharactersResponse(
    @SerializedName("results") var results: List<Character>
)