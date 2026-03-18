package it.giovanni.hub.data.repository.local

import it.giovanni.hub.data.dao.MemoDao
import it.giovanni.hub.data.entity.MemoEntity

class MemoRepository(private val memoDao: MemoDao) {

    suspend fun createMemo(memoEntity: MemoEntity) {
        memoDao.createMemo(memoEntity)
    }

    suspend fun readMemos(search: String): List<MemoEntity> {
        return memoDao.readMemos(search)
    }

    suspend fun readMemosForDay(month: Int, day: Int): List<MemoEntity> {
        return memoDao.readMemosForDay(month, day)
    }

    suspend fun updateMemo(memoEntity: MemoEntity) {
        memoDao.updateMemo(memoEntity)
    }

    suspend fun deleteMemo(memoEntity: MemoEntity) {
        memoDao.deleteMemo(memoEntity)
    }

    suspend fun deleteMemosForDay(month: Int, day: Int) {
        memoDao.deleteMemosForDay(month, day)
    }

    suspend fun readByExternalSourceAndEventId(
        source: String,
        eventId: Long
    ): MemoEntity? {
        return memoDao.readByExternalSourceAndEventId(source, eventId)
    }

    suspend fun readByExternalSource(source: String): List<MemoEntity> {
        return memoDao.readByExternalSource(source)
    }

    suspend fun deleteMissingImportedEvents(
        source: String,
        eventIds: List<Long>
    ) {
        if (eventIds.isNotEmpty()) {
            memoDao.deleteMissingImportedEvents(source, eventIds)
        }
    }

    suspend fun readByDisplaySignature(
        memo: String,
        month: Int,
        day: Int,
        time: String
    ): MemoEntity? {
        return memoDao.readByDisplaySignature(memo, month, day, time)
    }

    suspend fun readByLocalIdentity(
        memo: String,
        month: Int,
        day: Int,
        time: String
    ): MemoEntity? {
        return memoDao.readByLocalIdentity(memo, month, day, time)
    }
}