package it.giovanni.hub.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var firstName: String,
    var lastName: String,
    var age: String
)