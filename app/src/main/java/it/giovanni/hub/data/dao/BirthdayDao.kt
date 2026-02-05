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

    // Search by firstName OR lastName (if search is empty -> return all)
    @Query(
        """
        SELECT * FROM birthday_table
        WHERE (:search = '' 
               OR firstName LIKE '%' || :search || '%'
               OR lastName  LIKE '%' || :search || '%')
        ORDER BY id ASC
        """
    )
    suspend fun readBirthdays(search: String): List<BirthdayEntity>

    @Query("SELECT * FROM birthday_table WHERE id = :id")
    suspend fun readBirthdayById(id: Int): BirthdayEntity

    @Query(
        """
        SELECT * FROM birthday_table
        WHERE month = :month AND day = :day
        ORDER BY lastName ASC, firstName ASC
        """
    )
    suspend fun readBirthdaysForDay(month: Int, day: Int): List<BirthdayEntity>

    @Query("DELETE FROM birthday_table WHERE month = :month AND day = :day")
    suspend fun deleteBirthdaysForDay(month: Int, day: Int)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateBirthday(birthdayEntity: BirthdayEntity)

    @Delete
    suspend fun deleteBirthday(birthdayEntity: BirthdayEntity)
}