package it.giovanni.hub.domain.repository.remote

import it.giovanni.hub.data.entity.BirthdayEntity
import kotlinx.coroutines.flow.Flow

interface CalendarBackupRepository {

    fun isBackupEnabled(): Flow<Boolean>

    suspend fun setBackupEnabled(enabled: Boolean)

    suspend fun syncBirthdays(birthdays: List<BirthdayEntity>)

    suspend fun removeSyncedBirthdays()

    suspend fun importGoogleCalendarEventsIntoBirthdayDb()

    suspend fun deleteImportedGoogleEvent(eventId: Long): Boolean

    suspend fun updateImportedGoogleEvent(event: BirthdayEntity): Boolean
}