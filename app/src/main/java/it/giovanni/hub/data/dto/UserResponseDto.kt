package it.giovanni.hub.data.dto

import com.google.gson.annotations.SerializedName

data class UsersResponseDto(
    @SerializedName("page")
    val page: Int,

    @SerializedName("per_page")
    val perPage: Int,

    @SerializedName("total")
    val total: Int,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("data")
    val data: List<UserDto>
)

/*
class UsersResponseDto {
    @SerializedName("page")
    val page: Int? = null
    @SerializedName("per_page")
    val perPage: Int? = null
    @SerializedName("total")
    val total: Int? = null
    @SerializedName("total_pages")
    val totalPages: Int? = null
    @SerializedName("data")
    var users: List<UserDto>? = null
}
*/