package it.giovanni.hub.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Person(
    val id: Int,
    val firstName: String,
    val lastName: String,
    var visibility: Boolean
): Parcelable