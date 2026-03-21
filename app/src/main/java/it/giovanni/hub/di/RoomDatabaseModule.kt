package it.giovanni.hub.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.giovanni.hub.data.dao.MemoDao
import it.giovanni.hub.data.dao.UserDao
import it.giovanni.hub.data.database.MemoRoomDatabase
import it.giovanni.hub.data.database.HubRoomDatabase
import it.giovanni.hub.data.repository.local.MemoRepository
import it.giovanni.hub.data.repository.local.RoomRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomDatabaseModule {

    @Provides
    @Singleton
    fun provideHubRoomDatabase(@ApplicationContext context: Context): HubRoomDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = HubRoomDatabase::class.java,
            name = "hub_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: HubRoomDatabase): UserDao = database.userDao()

    @Provides
    @Singleton
    fun provideRoomRepository(userDao: UserDao): RoomRepository = RoomRepository(userDao = userDao)

    // -------------------- (memos) --------------------

    private val migration12 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                ALTER TABLE memo_table
                ADD COLUMN externalSource TEXT
                """.trimIndent()
            )

            db.execSQL("""
                ALTER TABLE memo_table
                ADD COLUMN externalEventId INTEGER
                """.trimIndent()
            )
        }
    }

    private val migration23 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                ALTER TABLE memo_table
                ADD COLUMN isBirthday INTEGER NOT NULL DEFAULT 0
                """.trimIndent()
            )
        }
    }

    @Provides
    @Singleton
    fun provideMemoRoomDatabase(@ApplicationContext context: Context): MemoRoomDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = MemoRoomDatabase::class.java,
            name = "memo_database"
        )
            .addMigrations(migration12, migration23)
            .build()
    }

    @Provides
    @Singleton
    fun provideMemoDao(database: MemoRoomDatabase): MemoDao = database.memoDao()

    @Provides
    @Singleton
    fun provideMemoRepository(memoDao: MemoDao): MemoRepository =
        MemoRepository(memoDao = memoDao)
}