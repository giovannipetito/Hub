package it.giovanni.hub.domain.repository.remote

import it.giovanni.hub.data.entity.BirthdayEntity
import kotlinx.coroutines.flow.Flow

interface CalendarBackupRepository {

    fun isBackupEnabled(): Flow<Boolean>
    suspend fun setBackupEnabled(enabled: Boolean)

    suspend fun syncBirthdays(birthdays: List<BirthdayEntity>)
    suspend fun removeSyncedBirthdays()
}