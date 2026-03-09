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

    @Insert
    suspend fun createBirthday(birthdayEntity: BirthdayEntity)

    @Update
    suspend fun updateBirthday(birthdayEntity: BirthdayEntity)

    @Delete
    suspend fun deleteBirthday(birthdayEntity: BirthdayEntity)

    @Query("""
        SELECT * FROM birthday_table
        WHERE firstName LIKE '%' || :search || '%'
           OR lastName LIKE '%' || :search || '%'
        ORDER BY month, day, firstName
    """)
    suspend fun readBirthdays(search: String): List<BirthdayEntity>

    @Query("""
        SELECT * FROM birthday_table
        WHERE month = :month AND day = :day
        ORDER BY firstName
    """)
    suspend fun readBirthdaysForDay(month: Int, day: Int): List<BirthdayEntity>

    @Query("""
        DELETE FROM birthday_table
        WHERE month = :month AND day = :day
    """)
    suspend fun deleteBirthdaysForDay(month: Int, day: Int)

    @Query("""
        SELECT * FROM birthday_table
        WHERE externalSource = :source AND externalEventId = :eventId
        LIMIT 1
    """)
    suspend fun readByExternalSourceAndEventId(
        source: String,
        eventId: Long
    ): BirthdayEntity?

    @Query("""
        SELECT * FROM birthday_table
        WHERE externalSource = :source
    """)
    suspend fun readByExternalSource(source: String): List<BirthdayEntity>

    @Query("""
        DELETE FROM birthday_table
        WHERE externalSource = :source
        AND externalEventId NOT IN (:eventIds)
    """)
    suspend fun deleteMissingImportedEvents(
        source: String,
        eventIds: List<Long>
    )

    @Query("""
        SELECT * FROM birthday_table
        WHERE firstName = :title
          AND month = :month
          AND day = :day
          AND yearOfBirth = :year
        LIMIT 1
    """)
    suspend fun readByDisplaySignature(
        title: String,
        month: Int,
        day: Int,
        year: String
    ): BirthdayEntity?
}