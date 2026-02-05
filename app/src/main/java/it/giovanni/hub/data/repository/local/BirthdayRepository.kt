package it.giovanni.hub.data.repository.local

import it.giovanni.hub.data.dao.BirthdayDao
import it.giovanni.hub.data.entity.BirthdayEntity

class BirthdayRepository(private val birthdayDao: BirthdayDao) {

    suspend fun createBirthday(birthdayEntity: BirthdayEntity) {
        birthdayDao.createBirthday(birthdayEntity)
    }

    suspend fun readBirthdays(search: String): List<BirthdayEntity> {
        return birthdayDao.readBirthdays(search)
    }

    suspend fun readBirthdaysForDay(month: Int, day: Int): List<BirthdayEntity> {
        return birthdayDao.readBirthdaysForDay(month, day)
    }

    suspend fun updateBirthday(birthdayEntity: BirthdayEntity) {
        birthdayDao.updateBirthday(birthdayEntity)
    }

    suspend fun deleteBirthday(birthdayEntity: BirthdayEntity) {
        birthdayDao.deleteBirthday(birthdayEntity)
    }

    suspend fun deleteBirthdaysForDay(month: Int, day: Int) {
        birthdayDao.deleteBirthdaysForDay(month, day)
    }
}