package it.giovanni.hub.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.giovanni.hub.domain.entity.UserEntity

@Dao
interface UserDao {

    // CRUD operations

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createUser(userEntity: UserEntity)

    @Query("SELECT * FROM users_table ORDER BY id ASC") // DESC
    suspend fun readUsers(): List<UserEntity>

    @Query("SELECT * FROM users_table WHERE id = :id")
    suspend fun readUserById(id: Int): UserEntity

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(userEntity: UserEntity)

    @Query("DELETE FROM users_table")
    suspend fun deleteUsers()

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)
}