package it.giovanni.hub.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import it.giovanni.hub.data.dao.UserDao
import it.giovanni.hub.data.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = true)
abstract class HubRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}