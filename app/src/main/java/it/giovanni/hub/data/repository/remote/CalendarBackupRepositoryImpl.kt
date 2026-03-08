package it.giovanni.hub.data.repository.remote

import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import it.giovanni.hub.data.entity.BirthdayEntity
import it.giovanni.hub.data.repository.local.DataStoreRepository
import it.giovanni.hub.domain.repository.remote.CalendarBackupRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarBackupRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStoreRepository: DataStoreRepository
) : CalendarBackupRepository {

    private val resolver = context.contentResolver

    override fun isBackupEnabled(): Flow<Boolean> =
        dataStoreRepository.isBirthdayBackupEnabled()

    override suspend fun setBackupEnabled(enabled: Boolean) {
        dataStoreRepository.setBirthdayBackupEnabled(enabled)
    }

    override suspend fun syncBirthdays(birthdays: List<BirthdayEntity>) {
        val calendarId = findWritableGoogleCalendarId() ?: return

        removeSyncedBirthdays()

        birthdays.forEach { birthday ->
            val startMillis = LocalDate
                .of(2000, birthday.month, birthday.day)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli()

            val title = buildString {
                append(birthday.firstName)
                if (birthday.lastName.isNotBlank()) {
                    append(" ")
                    append(birthday.lastName)
                }
                append("'s birthday")
            }

            val marker = "HUB_BIRTHDAY_BACKUP"
            val hubId = "${birthday.firstName}_${birthday.lastName}_${birthday.month}_${birthday.day}_${birthday.yearOfBirth}"

            val values = ContentValues().apply {
                put(CalendarContract.Events.CALENDAR_ID, calendarId)
                put(CalendarContract.Events.TITLE, title)
                put(CalendarContract.Events.DESCRIPTION, "$marker|$hubId")
                put(CalendarContract.Events.DTSTART, startMillis)
                put(CalendarContract.Events.ALL_DAY, 1)
                put(CalendarContract.Events.EVENT_TIMEZONE, "UTC")
                put(CalendarContract.Events.RRULE, "FREQ=YEARLY")
                put(CalendarContract.Events.DURATION, "P1D")
            }

            val insertedUri = resolver.insert(CalendarContract.Events.CONTENT_URI, values)
            Log.d("CalendarBackup", "Inserted birthday event uri=$insertedUri title=$title calendarId=$calendarId")
        }
    }

    override suspend fun removeSyncedBirthdays() {
        val selection = "${CalendarContract.Events.DESCRIPTION} LIKE ?"
        val args = arrayOf("%HUB_BIRTHDAY_BACKUP%")
        val rows = resolver.delete(CalendarContract.Events.CONTENT_URI, selection, args)
        Log.d("CalendarBackup", "Removed synced birthday rows=$rows")
    }

    private fun findWritableGoogleCalendarId(): Long? {
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.ACCOUNT_TYPE,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
            CalendarContract.Calendars.VISIBLE,
            CalendarContract.Calendars.SYNC_EVENTS
        )

        val selection = """
            ${CalendarContract.Calendars.VISIBLE} = 1 AND
            ${CalendarContract.Calendars.SYNC_EVENTS} = 1 AND
            ${CalendarContract.Calendars.ACCOUNT_TYPE} = ? AND
            ${CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL} >= ?
        """.trimIndent()

        val selectionArgs = arrayOf(
            "com.google",
            CalendarContract.Calendars.CAL_ACCESS_CONTRIBUTOR.toString()
        )

        val sortOrder = "${CalendarContract.Calendars._ID} ASC"

        resolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(CalendarContract.Calendars._ID)
            val accountNameIndex = cursor.getColumnIndexOrThrow(CalendarContract.Calendars.ACCOUNT_NAME)
            val displayNameIndex = cursor.getColumnIndexOrThrow(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIndex)
                val accountName = cursor.getString(accountNameIndex)
                val displayName = cursor.getString(displayNameIndex)

                Log.d(
                    "CalendarBackup",
                    "Using Google calendar id=$id account=$accountName displayName=$displayName"
                )
                return id
            }
        }

        Log.w("CalendarBackup", "No writable Google calendar found")
        return null
    }
}