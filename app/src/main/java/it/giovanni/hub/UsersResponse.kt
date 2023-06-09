package it.giovanni.hub

import com.google.gson.annotations.SerializedName

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
    var data: List<Data>? = null

    @SerializedName("support")
    var support: Support? = null
}