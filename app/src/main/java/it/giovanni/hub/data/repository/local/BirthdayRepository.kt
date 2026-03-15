package it.giovanni.hub.data.repository.local

import it.giovanni.hub.data.dao.MemoDao
import it.giovanni.hub.data.entity.MemoEntity

class BirthdayRepository(private val birthdayDao: MemoDao) {

    suspend fun createBirthday(birthdayEntity: MemoEntity) {
        birthdayDao.createMemo(birthdayEntity)
    }

    suspend fun readBirthdays(search: String): List<MemoEntity> {
        return birthdayDao.readMemos(search)
    }

    suspend fun readBirthdaysForDay(month: Int, day: Int): List<MemoEntity> {
        return birthdayDao.readMemosForDay(month, day)
    }

    suspend fun updateBirthday(birthdayEntity: MemoEntity) {
        birthdayDao.updateMemo(birthdayEntity)
    }

    suspend fun deleteBirthday(birthdayEntity: MemoEntity) {
        birthdayDao.deleteMemo(birthdayEntity)
    }

    suspend fun deleteBirthdaysForDay(month: Int, day: Int) {
        birthdayDao.deleteMemosForDay(month, day)
    }

    suspend fun readByExternalSourceAndEventId(
        source: String,
        eventId: Long
    ): MemoEntity? {
        return birthdayDao.readByExternalSourceAndEventId(source, eventId)
    }

    suspend fun readByExternalSource(source: String): List<MemoEntity> {
        return birthdayDao.readByExternalSource(source)
    }

    suspend fun deleteMissingImportedEvents(
        source: String,
        eventIds: List<Long>
    ) {
        if (eventIds.isNotEmpty()) {
            birthdayDao.deleteMissingImportedEvents(source, eventIds)
        }
    }

    suspend fun readByDisplaySignature(
        memo: String,
        month: Int,
        day: Int,
        time: String
    ): MemoEntity? {
        return birthdayDao.readByDisplaySignature(memo, month, day, time)
    }

    suspend fun readByLocalIdentity(
        memo: String,
        month: Int,
        day: Int,
        time: String
    ): MemoEntity? {
        return birthdayDao.readByLocalIdentity(memo, month, day, time)
    }
}