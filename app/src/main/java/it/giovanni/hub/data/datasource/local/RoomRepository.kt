package it.giovanni.hub.data.datasource.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import it.giovanni.hub.data.dao.UserDao
import it.giovanni.hub.domain.entity.UserEntity

class RoomRepository(private val userDao: UserDao) {

    // Coroutines functions

    suspend fun createUser(userEntity: UserEntity) {
        userDao.createUser(userEntity = userEntity)
    }

    suspend fun readUsers(): List<UserEntity> {
        return userDao.readUsers()
    }

    suspend fun readUserById(id: Int): UserEntity {
        return userDao.readUserById(id = id)
    }

    suspend fun updateUser(userEntity: UserEntity) {
        userDao.updateUser(userEntity = userEntity)
    }

    suspend fun deleteUsers() {
        userDao.deleteUsers()
    }

    suspend fun deleteUser(userEntity: UserEntity) {
        userDao.deleteUser(userEntity = userEntity)
    }

    // RxJava functions

    fun createRxJavaUser(userEntity: UserEntity): Completable {
        return userDao.createRxJavaUser(userEntity = userEntity)
    }

    fun readRxJavaUsers(): Flowable<List<UserEntity>> {
        return userDao.readRxJavaUsers()
    }

    fun readRxJavaUserById(id: Int): Flowable<UserEntity> {
        return userDao.readRxJavaUserById(id = id)
    }

    fun updateRxJavaUser(userEntity: UserEntity): Completable {
        return userDao.updateRxJavaUser(userEntity = userEntity)
    }

    fun deleteRxJavaUsers(): Completable {
        return userDao.deleteRxJavaUsers()
    }

    fun deleteRxJavaUser(userEntity: UserEntity): Completable {
        return userDao.deleteRxJavaUser(userEntity = userEntity)
    }
}