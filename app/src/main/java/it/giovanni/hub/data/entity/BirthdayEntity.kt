package it.giovanni.hub.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "birthday_table")
data class BirthdayEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val yearOfBirth: String,

    // Needed to know which calendar cell this belongs to
    val month: Int,
    val day: Int
)