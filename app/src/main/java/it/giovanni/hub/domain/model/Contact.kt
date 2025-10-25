package it.giovanni.hub.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Contact(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val leftActions: List<String>,
    val rightActions: List<String>
): Parcelable