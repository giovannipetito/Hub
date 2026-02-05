package it.giovanni.hub.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import it.giovanni.hub.data.dao.BirthdayDao
import it.giovanni.hub.data.entity.BirthdayEntity

@Database(entities = [BirthdayEntity::class], version = 1, exportSchema = true)
abstract class BirthdayRoomDatabase : RoomDatabase() {
    abstract fun birthdayDao(): BirthdayDao
}