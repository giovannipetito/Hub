package it.giovanni.hub.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.giovanni.hub.data.entity.BirthdayEntity

@Dao
interface BirthdayDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createBirthday(birthdayEntity: BirthdayEntity)

    @Query("SELECT * FROM birthday_table ORDER BY id ASC")
    suspend fun readBirthdays(): List<BirthdayEntity>

    @Query("SELECT * FROM birthday_table WHERE id = :id")
    suspend fun readBirthdayById(id: Int): BirthdayEntity

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateBirthday(birthdayEntity: BirthdayEntity)

    @Delete
    suspend fun deleteBirthday(birthdayEntity: BirthdayEntity)
}