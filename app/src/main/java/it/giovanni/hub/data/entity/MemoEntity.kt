package it.giovanni.hub.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo_table")
data class MemoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val memo: String,
    val month: Int,
    val day: Int,
    val time: String,
    val isBirthday: Boolean = false,
    val externalSource: String? = null,
    val externalEventId: Long? = null
)