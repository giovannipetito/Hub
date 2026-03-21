package it.giovanni.hub.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import it.giovanni.hub.data.dao.MemoDao
import it.giovanni.hub.data.entity.MemoEntity

@Database(
    entities = [MemoEntity::class],
    version = 3,
    exportSchema = true // Room exports the schema JSON files for every database version.
)
abstract class MemoRoomDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
}