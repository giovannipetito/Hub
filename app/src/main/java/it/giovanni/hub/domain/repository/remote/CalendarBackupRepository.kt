package it.giovanni.hub.domain.repository.remote

import it.giovanni.hub.data.entity.MemoEntity
import kotlinx.coroutines.flow.Flow

interface CalendarBackupRepository {

    fun isBackupEnabled(): Flow<Boolean>

    suspend fun setBackupEnabled(enabled: Boolean)

    suspend fun syncMemos(memos: List<MemoEntity>)

    suspend fun removeSyncedMemos()

    suspend fun importGoogleCalendarEventsIntoMemoDb(appMemos: Boolean = false)

    suspend fun deleteImportedGoogleEvent(eventId: Long): Boolean

    suspend fun updateImportedGoogleEvent(memoEntity: MemoEntity): Boolean
}