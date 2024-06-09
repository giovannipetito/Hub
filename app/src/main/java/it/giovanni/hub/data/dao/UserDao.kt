package it.giovanni.hub.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import it.giovanni.hub.domain.entity.UserEntity

@Dao
interface UserDao {

    // CRUD operations with Coroutines

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createUser(userEntity: UserEntity)

    @Query("SELECT * FROM users_table ORDER BY id ASC")
    suspend fun readUsers(): List<UserEntity>

    @Query("SELECT * FROM users_table WHERE id = :id")
    suspend fun readUserById(id: Int): UserEntity

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateUser(userEntity: UserEntity)

    @Query("DELETE FROM users_table")
    suspend fun deleteUsers()

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

    // CRUD operations with RxJava

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createRxJavaUser(userEntity: UserEntity): Completable

    @Query("SELECT * FROM users_table ORDER BY id DESC")
    fun readRxJavaUsers(): Flowable<List<UserEntity>>

    @Query("SELECT * FROM users_table WHERE id = :id")
    fun readRxJavaUserById(id: Int): Flowable<UserEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateRxJavaUser(userEntity: UserEntity): Completable

    @Query("DELETE FROM users_table")
    fun deleteRxJavaUsers(): Completable

    @Delete
    fun deleteRxJavaUser(userEntity: UserEntity): Completable
}