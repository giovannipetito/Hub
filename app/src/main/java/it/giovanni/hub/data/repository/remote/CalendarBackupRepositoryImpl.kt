package it.giovanni.hub.data.repository.remote

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import it.giovanni.hub.data.entity.BirthdayEntity
import it.giovanni.hub.data.repository.local.BirthdayRepository
import it.giovanni.hub.data.repository.local.DataStoreRepository
import it.giovanni.hub.domain.repository.remote.CalendarBackupRepository
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarBackupRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStoreRepository: DataStoreRepository,
    private val birthdayRepository: BirthdayRepository
) : CalendarBackupRepository {

    companion object {
        private const val TAG = "CalendarBackup"
        private const val GOOGLE_CALENDAR_SOURCE = "GOOGLE_CALENDAR"
        private const val APP_MARKER = "HUB_BIRTHDAY_BACKUP"
    }

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

            val hubId = "${birthday.firstName}_${birthday.lastName}_${birthday.month}_${birthday.day}_${birthday.yearOfBirth}"

            val values = ContentValues().apply {
                put(CalendarContract.Events.CALENDAR_ID, calendarId)
                put(CalendarContract.Events.TITLE, title)
                put(CalendarContract.Events.DESCRIPTION, "$APP_MARKER|$hubId")
                put(CalendarContract.Events.DTSTART, startMillis)
                put(CalendarContract.Events.ALL_DAY, 1)
                put(CalendarContract.Events.EVENT_TIMEZONE, "UTC")
                put(CalendarContract.Events.RRULE, "FREQ=YEARLY")
                put(CalendarContract.Events.DURATION, "P1D")
            }

            val insertedUri = resolver.insert(CalendarContract.Events.CONTENT_URI, values)
            Log.d(TAG, "Inserted birthday event uri=$insertedUri title=$title calendarId=$calendarId")
        }
    }

    override suspend fun removeSyncedBirthdays() {
        val selection = "${CalendarContract.Events.DESCRIPTION} LIKE ?"
        val args = arrayOf("%$APP_MARKER%")
        val rows = resolver.delete(CalendarContract.Events.CONTENT_URI, selection, args)
        Log.d(TAG, "Removed synced birthday rows=$rows")
    }

    override suspend fun importGoogleCalendarEventsIntoBirthdayDb() {
        val googleCalendarIds = findGoogleCalendarIds()
        if (googleCalendarIds.isEmpty()) {
            Log.w(TAG, "No Google calendars found for import")
            return
        }

        val importedEventIds = mutableListOf<Long>()

        val currentYear = LocalDate.now().year

        val rangeStart = LocalDate
            .of(currentYear, 1, 1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val rangeEnd = LocalDate
            .of(currentYear + 1, 1, 1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(builder, rangeStart)
        ContentUris.appendId(builder, rangeEnd)

        val projection = arrayOf(
            CalendarContract.Instances.EVENT_ID,
            CalendarContract.Instances.BEGIN,
            CalendarContract.Instances.END,
            CalendarContract.Instances.TITLE,
            CalendarContract.Instances.ALL_DAY,
            CalendarContract.Instances.CALENDAR_ID
        )

        val placeholders = googleCalendarIds.joinToString(",") { "?" }
        val selection = """
            ${CalendarContract.Instances.CALENDAR_ID} IN ($placeholders)
        """.trimIndent()

        val selectionArgs = googleCalendarIds.map { it.toString() }.toTypedArray()

        resolver.query(
            builder.build(),
            projection,
            selection,
            selectionArgs,
            "${CalendarContract.Instances.BEGIN} ASC"
        )?.use { cursor ->

            val eventIdIndex = cursor.getColumnIndexOrThrow(CalendarContract.Instances.EVENT_ID)
            val beginIndex = cursor.getColumnIndexOrThrow(CalendarContract.Instances.BEGIN)
            val titleIndex = cursor.getColumnIndexOrThrow(CalendarContract.Instances.TITLE)

            while (cursor.moveToNext()) {
                val eventId = cursor.getLong(eventIdIndex)
                val beginMillis = cursor.getLong(beginIndex)
                val rawTitle = cursor.getString(titleIndex).orEmpty().trim()

                if (rawTitle.isBlank()) continue

                val localDate = Instant.ofEpochMilli(beginMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                if (localDate.year != currentYear) continue

                importedEventIds += eventId

                val mapped = BirthdayEntity(
                    firstName = rawTitle,
                    lastName = "",
                    yearOfBirth = localDate.year.toString(),
                    month = localDate.monthValue,
                    day = localDate.dayOfMonth,
                    externalSource = GOOGLE_CALENDAR_SOURCE,
                    externalEventId = eventId
                )

                val existingImported =
                    birthdayRepository.readByExternalSourceAndEventId(
                        source = GOOGLE_CALENDAR_SOURCE,
                        eventId = eventId
                    )

                if (existingImported != null) {
                    val updated = existingImported.copy(
                        firstName = mapped.firstName,
                        lastName = mapped.lastName,
                        yearOfBirth = mapped.yearOfBirth,
                        month = mapped.month,
                        day = mapped.day
                    )

                    if (updated != existingImported) {
                        birthdayRepository.updateBirthday(updated)
                    }
                    continue
                }

                val sameDisplayRow = birthdayRepository.readByDisplaySignature(
                    title = mapped.firstName,
                    month = mapped.month,
                    day = mapped.day,
                    year = mapped.yearOfBirth
                )

                if (sameDisplayRow != null) {
                    // Conflict strategy:
                    // if a manual/app row already looks identical in the current Birthday table,
                    // skip insert to avoid duplicates.
                    continue
                }

                birthdayRepository.createBirthday(mapped)
            }
        }

        if (importedEventIds.isNotEmpty()) {
            birthdayRepository.deleteMissingImportedEvents(
                source = GOOGLE_CALENDAR_SOURCE,
                eventIds = importedEventIds.distinct()
            )
        }

        Log.d(TAG, "Imported Google Calendar occurrences count=${importedEventIds.distinct().size}")
    }

    private fun findWritableGoogleCalendarId(): Long? {
        return findGoogleCalendars(minAccessLevel = CalendarContract.Calendars.CAL_ACCESS_CONTRIBUTOR)
            .firstOrNull()
            ?.id
    }

    private fun findGoogleCalendarIds(): List<Long> {
        return findGoogleCalendars(
            minAccessLevel = CalendarContract.Calendars.CAL_ACCESS_READ
        ).map { it.id }
    }

    private fun findGoogleCalendars(minAccessLevel: Int): List<GoogleCalendarInfo> {
        val result = mutableListOf<GoogleCalendarInfo>()

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
            minAccessLevel.toString()
        )

        resolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${CalendarContract.Calendars._ID} ASC"
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(CalendarContract.Calendars._ID)
            val accountNameIndex = cursor.getColumnIndexOrThrow(CalendarContract.Calendars.ACCOUNT_NAME)
            val displayNameIndex = cursor.getColumnIndexOrThrow(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)

            while (cursor.moveToNext()) {
                result += GoogleCalendarInfo(
                    id = cursor.getLong(idIndex),
                    accountName = cursor.getString(accountNameIndex),
                    displayName = cursor.getString(displayNameIndex)
                )
            }
        }

        return result
    }

    private data class GoogleCalendarInfo(
        val id: Long,
        val accountName: String,
        val displayName: String
    )
}