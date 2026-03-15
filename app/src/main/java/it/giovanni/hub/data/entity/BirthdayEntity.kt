package it.giovanni.hub.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 1. Semplifica BirthdayEntity in: id, birthdayName, eventName, rangeTime
 * 2.
 * 3.
 */

@Entity(tableName = "birthday_table")
data class BirthdayEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val yearOfBirth: String,
    val month: Int,
    val day: Int,
    val externalSource: String? = null,
    val externalEventId: Long? = null
)