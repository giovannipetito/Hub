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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(userEntity: UserEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(userEntity: UserEntity)

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

    @Query("SELECT * FROM users_table WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity

    @Query("SELECT * FROM users_table ORDER BY id ASC") // DESC
    suspend fun getUsers(): List<UserEntity>

    @Query("DELETE FROM users_table")
    suspend fun deleteUsers()
}