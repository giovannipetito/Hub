package it.giovanni.hub.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Person(
    val firstName: String,
    val lastName: String
): Parcelable