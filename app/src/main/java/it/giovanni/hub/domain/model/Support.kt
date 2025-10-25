package it.giovanni.hub.domain.model

import com.google.gson.annotations.SerializedName

class Support {

    @SerializedName("url")
    var url: String? = null

    @SerializedName("text")
    var text: String? = null
}