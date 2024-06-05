package it.giovanni.hub.data.datasource.local

import it.giovanni.hub.data.dao.UserDao
import it.giovanni.hub.domain.entity.UserEntity

class RoomRepository(private val userDao: UserDao) {

    suspend fun insertUser(userEntity: UserEntity) {
        userDao.insertUser(userEntity = userEntity)
    }

    suspend fun updateUser(userEntity: UserEntity) {
        userDao.updateUser(userEntity = userEntity)
    }

    suspend fun deleteUser(userEntity: UserEntity) {
        userDao.deleteUser(userEntity = userEntity)
    }

    suspend fun getUserById(id: Int): UserEntity {
        return userDao.getUserById(id = id)
    }

    suspend fun getUsers(): List<UserEntity> {
        return userDao.getUsers()
    }

    suspend fun deleteUsers() {
        userDao.deleteUsers()
    }
}