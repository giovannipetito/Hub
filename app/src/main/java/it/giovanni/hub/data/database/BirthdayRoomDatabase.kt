package it.giovanni.hub.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import it.giovanni.hub.data.dao.MemoDao
import it.giovanni.hub.data.entity.MemoEntity

@Database(
    entities = [MemoEntity::class],
    version = 2,
    exportSchema = true
)
abstract class BirthdayRoomDatabase : RoomDatabase() {
    abstract fun birthdayDao(): MemoDao
}