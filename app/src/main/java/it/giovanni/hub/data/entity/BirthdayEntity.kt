package it.giovanni.hub.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "birthday_table")
data class BirthdayEntity(
    @PrimaryKey(autoGenerate = true)
    var firstName: String,
    var lastName: String,
    var yearOfBirth: String
)