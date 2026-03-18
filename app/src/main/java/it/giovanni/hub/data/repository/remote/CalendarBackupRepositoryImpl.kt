package it.giovanni.hub.data.repository.remote

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import it.giovanni.hub.data.entity.MemoEntity
import it.giovanni.hub.data.repository.local.MemoRepository
import it.giovanni.hub.data.repository.local.DataStoreRepository
import it.giovanni.hub.domain.repository.remote.CalendarBackupRepository
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarBackupRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStoreRepository: DataStoreRepository,
    private val memoRepository: MemoRepository
) : CalendarBackupRepository {

    companion object {
        private const val TAG = "CalendarBackup"
        private const val GOOGLE_CALENDAR_SOURCE = "GOOGLE_CALENDAR"
        private const val APP_MARKER = "HUB_MEMO_BACKUP"
    }

    private val resolver = context.contentResolver

    override fun isBackupEnabled(): Flow<Boolean> =
        dataStoreRepository.isBackupEnabled()

    override suspend fun setBackupEnabled(enabled: Boolean) {
        dataStoreRepository.setBackupEnabled(enabled)
    }

    override suspend fun syncMemos(memos: List<MemoEntity>) {
        val calendarId = findWritableGoogleCalendarId() ?: return

        removeSyncedMemos()

        val memosToExport = memos.filter { memo ->
            memo.externalSource == null
        }

        memosToExport.forEach { memo ->
            val startMillis = LocalDate
                .of(2000, memo.month, memo.day)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli()

            val title = buildString {
                append(memo.memo)
                append("'s birthday")
            }

            val hubId = "${memo.memo}_${memo.month}_${memo.day}_${memo.time}"

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
            Log.d(TAG, "Inserted memo event uri=$insertedUri title=$title calendarId=$calendarId")
        }
    }

    override suspend fun removeSyncedMemos() {
        val selection = "${CalendarContract.Events.DESCRIPTION} LIKE ?"
        val args = arrayOf("%$APP_MARKER%")
        val rows = resolver.delete(CalendarContract.Events.CONTENT_URI, selection, args)
        Log.d(TAG, "Removed synced memo rows=$rows")
    }

    override suspend fun importGoogleCalendarEventsIntoMemoDb(appMemos: Boolean) {
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
                val rawMemo = cursor.getString(titleIndex).orEmpty().trim()

                if (rawMemo.isBlank()) continue

                val description = getEventDescription(eventId).orEmpty()
                val isAppManaged = description.contains(APP_MARKER)

                // Normal mode: keep skipping app-managed events to avoid loops
                if (isAppManaged && !appMemos) continue

                // Restore mode: if this is an app-managed event, rebuild the original local row
                if (isAppManaged) {
                    val restored = parseAppManagedMemoFromDescription(description) ?: continue

                    val existingLocal = memoRepository.readByLocalIdentity(
                        memo = restored.memo,
                        month = restored.month,
                        day = restored.day,
                        time = restored.time
                    )

                    if (existingLocal == null) {
                        memoRepository.createMemo(restored)
                    }

                    continue
                }

                val localDate = Instant.ofEpochMilli(beginMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                if (localDate.year != currentYear) continue

                importedEventIds += eventId

                val mapped = MemoEntity(
                    memo = rawMemo,
                    month = localDate.monthValue,
                    day = localDate.dayOfMonth,
                    time = "12:00", // todo: add current time
                    externalSource = GOOGLE_CALENDAR_SOURCE,
                    externalEventId = eventId
                )

                val existingImported =
                    memoRepository.readByExternalSourceAndEventId(
                        source = GOOGLE_CALENDAR_SOURCE,
                        eventId = eventId
                    )

                if (existingImported != null) {
                    val updated = existingImported.copy(
                        memo = mapped.memo,
                        month = mapped.month,
                        day = mapped.day,
                        time = mapped.time
                    )

                    if (updated != existingImported) {
                        memoRepository.updateMemo(updated)
                    }
                    continue
                }

                val sameDisplayRow = memoRepository.readByDisplaySignature(
                    memo = mapped.memo,
                    month = mapped.month,
                    day = mapped.day,
                    time = mapped.time
                )

                if (sameDisplayRow != null) {
                    continue
                }

                memoRepository.createMemo(mapped)
            }
        }

        if (importedEventIds.isNotEmpty()) {
            memoRepository.deleteMissingImportedEvents(
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

    private fun getEventDescription(eventId: Long): String? {
        val projection = arrayOf(
            CalendarContract.Events.DESCRIPTION
        )

        val selection = "${CalendarContract.Events._ID} = ?"
        val selectionArgs = arrayOf(eventId.toString())

        resolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val descriptionIndex =
                    cursor.getColumnIndexOrThrow(CalendarContract.Events.DESCRIPTION)
                return cursor.getString(descriptionIndex)
            }
        }

        return null
    }

    private fun parseAppManagedMemoFromDescription(description: String): MemoEntity? {
        if (!description.startsWith("$APP_MARKER|")) return null

        val payload = description.removePrefix("$APP_MARKER|")
        val parts = payload.split("_")

        if (parts.size < 5) return null

        val memo = parts[0]
        val month = parts[1].toIntOrNull() ?: return null
        val day = parts[2].toIntOrNull() ?: return null
        val time = parts[3]

        return MemoEntity(
            memo = memo,
            month = month,
            day = day,
            time = time,
            externalSource = null,
            externalEventId = null
        )
    }

    override suspend fun deleteImportedGoogleEvent(eventId: Long): Boolean {
        val deleteUri = ContentUris.withAppendedId(
            CalendarContract.Events.CONTENT_URI,
            eventId
        )
        val rows = resolver.delete(deleteUri, null, null)
        Log.d(TAG, "Deleted imported Google event eventId=$eventId rows=$rows")
        return rows > 0
    }

    override suspend fun updateImportedGoogleEvent(memoEntity: MemoEntity): Boolean {
        val eventId = memoEntity.externalEventId ?: return false

        val localDate = LocalDate.of(
            LocalDate.now().year,
            memoEntity.month,
            memoEntity.day
        )

        val startMillis = localDate
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()

        val title = buildString {
            append(memoEntity.memo)
        }.trim()

        val eventUri = ContentUris.withAppendedId(
            CalendarContract.Events.CONTENT_URI,
            eventId
        )

        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.DURATION,
            CalendarContract.Events.RRULE
        )

        var hasDtEnd = false

        resolver.query(
            eventUri,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val dtEndIndex = cursor.getColumnIndexOrThrow(CalendarContract.Events.DTEND)
                hasDtEnd = !cursor.isNull(dtEndIndex)
            }
        }

        val values = ContentValues().apply {
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.ALL_DAY, 1)
            put(CalendarContract.Events.EVENT_TIMEZONE, "UTC")

            if (hasDtEnd) {
                put(CalendarContract.Events.DTEND, startMillis + 24L * 60L * 60L * 1000L)
                putNull(CalendarContract.Events.DURATION)
            } else {
                put(CalendarContract.Events.DURATION, "P1D")
                putNull(CalendarContract.Events.DTEND)
            }
        }

        val rows = resolver.update(eventUri, values, null, null)
        Log.d(TAG, "Updated imported Google event eventId=$eventId rows=$rows title=$title")
        return rows > 0
    }
}