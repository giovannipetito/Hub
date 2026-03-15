package it.giovanni.hub.domain.repository.remote

import it.giovanni.hub.data.entity.MemoEntity
import kotlinx.coroutines.flow.Flow

interface CalendarBackupRepository {

    fun isBackupEnabled(): Flow<Boolean>

    suspend fun setBackupEnabled(enabled: Boolean)

    suspend fun syncBirthdays(birthdays: List<MemoEntity>)

    suspend fun removeSyncedBirthdays()

    suspend fun importGoogleCalendarEventsIntoBirthdayDb(restoreAppManagedEvents: Boolean = false)

    suspend fun deleteImportedGoogleEvent(eventId: Long): Boolean

    suspend fun updateImportedGoogleEvent(event: MemoEntity): Boolean
}