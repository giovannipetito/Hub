package it.giovanni.hub.data.datasource.local

import it.giovanni.hub.data.dao.UserDao
import it.giovanni.hub.domain.entity.UserEntity

class RoomRepository(private val userDao: UserDao) {

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
}