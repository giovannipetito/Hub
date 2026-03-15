package it.giovanni.hub.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import it.giovanni.hub.data.entity.MemoEntity

@Dao
interface MemoDao {

    @Insert
    suspend fun createMemo(memoEntity: MemoEntity)

    @Update
    suspend fun updateMemo(memoEntity: MemoEntity)

    @Delete
    suspend fun deleteMemo(memoEntity: MemoEntity)

    // todo: remove DESC?
    @Query("""
        SELECT * FROM memo_table
        WHERE memo LIKE '%' || :search || '%'
        ORDER BY month, day, memo DESC
    """)
    suspend fun readMemos(search: String): List<MemoEntity>

    // todo: remove DESC?
    @Query("""
        SELECT * FROM memo_table
        WHERE month = :month AND day = :day
        ORDER BY memo DESC
    """)
    suspend fun readMemosForDay(month: Int, day: Int): List<MemoEntity>

    @Query("""
    DELETE FROM memo_table
    WHERE month = :month AND day = :day
    """)
    suspend fun deleteMemosForDay(month: Int, day: Int)

    @Query("""
        SELECT * FROM memo_table
        WHERE externalSource = :source AND externalEventId = :eventId
        LIMIT 1
    """)
    suspend fun readByExternalSourceAndEventId(
        source: String,
        eventId: Long,
    ): MemoEntity?

    @Query("""
        SELECT * FROM memo_table
        WHERE externalSource = :source
    """)
    suspend fun readByExternalSource(source: String): List<MemoEntity>

    @Query("""
        DELETE FROM memo_table
        WHERE externalSource = :source
        AND externalEventId NOT IN (:eventIds)
    """)
    suspend fun deleteMissingImportedEvents(
        source: String,
        eventIds: List<Long>
    )

    @Query("""
        SELECT * FROM memo_table
        WHERE memo = :memo
          AND month = :month
          AND day = :day
          AND time = :time
        LIMIT 1
    """)
    suspend fun readByDisplaySignature(
        memo: String,
        month: Int,
        day: Int,
        time: String
    ): MemoEntity?

    @Query("""
    SELECT * FROM memo_table
    WHERE memo = :memo
      AND month = :month
      AND day = :day
      AND time = :time
    LIMIT 1
    """)
    suspend fun readByLocalIdentity(
        memo: String,
        month: Int,
        day: Int,
        time: String
    ): MemoEntity?
}