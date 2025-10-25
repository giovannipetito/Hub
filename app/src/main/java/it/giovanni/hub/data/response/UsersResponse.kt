package it.giovanni.hub.data.response

import com.google.gson.annotations.SerializedName
import it.giovanni.hub.domain.model.Support
import it.giovanni.hub.domain.model.User

class UsersResponse {

    @SerializedName("page")
    var page = 0

    @SerializedName("per_page")
    var perPage = 0

    @SerializedName("total")
    var total = 0

    @SerializedName("total_pages")
    var totalPages = 0

    @SerializedName("data")
    var users: List<User>? = null

    @SerializedName("support")
    var support: Support? = null
}