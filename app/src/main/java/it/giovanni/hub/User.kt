package it.giovanni.hub

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id: Int,
    @SerializedName("email") var email: String,
    @SerializedName("first_name") var firstName: String,
    @SerializedName("last_name") var lastName: String,
    @SerializedName("avatar") var avatar: String
)

/*
object User {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("email")
    var email: String? = null

    @SerializedName("first_name")
    var firstName: String? = null

    @SerializedName("last_name")
    var lastName: String? = null

    @SerializedName("avatar")
    var avatar: String? = null
}
*/