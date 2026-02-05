package it.giovanni.hub.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.giovanni.hub.data.dao.BirthdayDao
import it.giovanni.hub.data.dao.UserDao
import it.giovanni.hub.data.database.BirthdayRoomDatabase
import it.giovanni.hub.data.database.HubRoomDatabase
import it.giovanni.hub.data.repository.local.BirthdayRepository
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

    // -------------------- (birthdays) --------------------

    @Provides
    @Singleton
    fun provideBirthdayRoomDatabase(@ApplicationContext context: Context): BirthdayRoomDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = BirthdayRoomDatabase::class.java,
            name = "birthday_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBirthdayDao(database: BirthdayRoomDatabase): BirthdayDao = database.birthdayDao()

    @Provides
    @Singleton
    fun provideBirthdayRepository(birthdayDao: BirthdayDao): BirthdayRepository =
        BirthdayRepository(birthdayDao = birthdayDao)
}