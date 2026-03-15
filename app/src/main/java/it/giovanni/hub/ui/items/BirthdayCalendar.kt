package it.giovanni.hub.ui.items

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import it.giovanni.hub.data.entity.MemoEntity
import it.giovanni.hub.domain.birthday.rememberDeviceLocale
import it.giovanni.hub.utils.Globals.getExtraContentPadding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun BirthdayCalendar(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    birthdaysByMonthDay: Map<Int, List<MemoEntity>>,
    selectedDate: LocalDate?,
    onDayClick: (LocalDate) -> Unit,
    weekStartsOn: DayOfWeek = DayOfWeek.MONDAY,
    cellSpacing: Dp = 6.dp,
) {
    val locale = rememberDeviceLocale()

    val today = remember { LocalDate.now() }
    val year = today.year
    val currentMonthIndex = today.monthValue - 1
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = currentMonthIndex)

    val months = remember(year, locale, weekStartsOn) {
        (1..12).map { m ->
            val ym = YearMonth.of(year, m)
            val title = ym.month.getDisplayName(TextStyle.FULL, locale)
                .replaceFirstChar { it.titlecase(locale) }

            val first = LocalDate.of(year, m, 1).dayOfWeek
            val offset = ((first.value - weekStartsOn.value) + 7) % 7

            MonthModel(
                year = year,
                month = m,
                title = title,
                firstDayOffset = offset,
                daysInMonth = ym.lengthOfMonth()
            )
        }
    }

    // week labels
    val weekdayLabels = remember(locale, weekStartsOn) {
        val start = weekStartsOn.value // 1..7 (Mon..Sun)
        (0..6).map { i ->
            val v = ((start - 1 + i) % 7) + 1
            DayOfWeek.of(v).getDisplayName(TextStyle.SHORT, locale)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = getExtraContentPadding(
            paddingValues = paddingValues,
            extraPadding = 80.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            count = months.size,
            key = { idx -> "m-${months[idx].year}-${months[idx].month}" }
        ) { idx ->
            val m = months[idx]

            MonthSection(
                month = m,
                birthdaysByMonthDay = birthdaysByMonthDay,
                selectedDate = selectedDate,
                today = today,
                weekdayLabels = weekdayLabels,
                onDayClick = onDayClick,
                cellSpacing = cellSpacing,
                horizontalPadding = 12.dp
            )
        }
    }
}

@Immutable
private data class MonthModel(
    val year: Int,
    val month: Int,
    val title: String,
    val firstDayOffset: Int,
    val daysInMonth: Int
)

@Composable
private fun MonthSection(
    month: MonthModel,
    birthdaysByMonthDay: Map<Int, List<MemoEntity>>,
    selectedDate: LocalDate?,
    today: LocalDate,
    weekdayLabels: List<String>,
    onDayClick: (LocalDate) -> Unit,
    cellSpacing: Dp = 6.dp,
    horizontalPadding: Dp = 12.dp,
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
    ) {
        val density = LocalDensity.current
        val spacingPx = with(density) { cellSpacing.toPx() }
        val availablePx = with(density) { maxWidth.toPx() }

        // Make 7 cells + 6 spacings fit perfectly in the available width
        val cellPx = ((availablePx - spacingPx * 6f) / 7f).coerceAtLeast(0f)
        val cellSize = with(density) { cellPx.toDp() }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(
                text = "${month.title} ${month.year}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 6.dp),
                textAlign = TextAlign.Start
            )

            // Week labels aligned with the grid width
            Row(modifier = Modifier.fillMaxWidth()) {
                weekdayLabels.forEach { w ->
                    Text(
                        text = w,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            MonthGridCanvas(
                month = month,
                birthdaysByMonthDay = birthdaysByMonthDay,
                selectedDate = selectedDate,
                today = today,
                cellSize = cellSize, // dynamic per device width
                cellSpacing = cellSpacing,
                onDayClick = onDayClick
            )
        }
    }
}

@Composable
private fun MonthGridCanvas(
    month: MonthModel,
    birthdaysByMonthDay: Map<Int, List<MemoEntity>>,
    selectedDate: LocalDate?,
    today: LocalDate,
    cellSize: Dp,
    cellSpacing: Dp,
    onDayClick: (LocalDate) -> Unit
) {
    val density = LocalDensity.current
    val cellPx = with(density) { cellSize.toPx() }
    val spacingPx = with(density) { cellSpacing.toPx() }

    val rows = remember(month.year, month.month, month.firstDayOffset, month.daysInMonth) {
        val totalCells = month.firstDayOffset + month.daysInMonth
        ceil(totalCells / 7f).toInt().coerceIn(4, 6)
    }

    val gridHeight = cellPx * rows + spacingPx * (rows - 1)

    val cs = MaterialTheme.colorScheme
    val bgBase = cs.surfaceVariant.toArgb()
    val bgSelected = cs.primary.copy(alpha = 0.6f).toArgb()
    val bgToday = cs.primaryContainer.toArgb()
    val bgBirthday = cs.errorContainer.toArgb()
    val textColor = cs.onSurface.toArgb()
    val textDisabled = cs.onSurface.copy(alpha = 0.12f).toArgb()

    val paint = remember {
        Paint(Paint.ANTI_ALIAS_FLAG).apply { textAlign = Paint.Align.CENTER }
    }

    val birthdayKeyForMonth = remember(birthdaysByMonthDay, month.month) {
        BooleanArray(32) { false }.also { arr ->
            birthdaysByMonthDay.keys.forEach { key ->
                val kMonth = key / 100
                val kDay = key % 100
                if (kMonth == month.month && kDay in 1..31) arr[kDay] = true
            }
        }
    }

    val pointer = Modifier.pointerInput(month.year, month.month, month.firstDayOffset, month.daysInMonth, rows) {
        detectTapGestures { pos ->
            val col = floor(pos.x / (cellPx + spacingPx)).toInt()
            val row = floor(pos.y / (cellPx + spacingPx)).toInt()

            if (col !in 0..6 || row !in 0 until rows) return@detectTapGestures

            val inCellX = (pos.x - col * (cellPx + spacingPx)) <= cellPx
            val inCellY = (pos.y - row * (cellPx + spacingPx)) <= cellPx
            if (!inCellX || !inCellY) return@detectTapGestures

            val index = row * 7 + col
            val day = index - month.firstDayOffset + 1
            if (day in 1..month.daysInMonth) {
                onDayClick(LocalDate.of(month.year, month.month, day))
            }
        }
    }

    Canvas(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .height(height = with(density) { gridHeight.toDp() })
            .then(pointer)
    ) {
        for (row in 0 until rows) {
            for (col in 0..6) {
                val x = col * (cellPx + spacingPx)
                val y = row * (cellPx + spacingPx)

                val index = row * 7 + col
                val day = index - month.firstDayOffset + 1
                val isRealDay = day in 1..month.daysInMonth

                val isToday =
                    isRealDay && today.year == month.year && today.monthValue == month.month && today.dayOfMonth == day

                val isSelected =
                    isRealDay && selectedDate?.year == month.year &&
                            selectedDate.monthValue == month.month && selectedDate.dayOfMonth == day

                val hasBirthdays = isRealDay && birthdayKeyForMonth[day]

                val bg = when {
                    !isRealDay -> bgBase
                    isSelected -> bgSelected
                    hasBirthdays -> bgBirthday
                    isToday -> bgToday
                    else -> bgBase
                }

                drawRect(
                    color = Color(bg),
                    topLeft = Offset(x, y),
                    size = Size(cellPx, cellPx)
                )

                paint.color = if (isRealDay) textColor else textDisabled
                paint.textSize = cellPx * 0.36f

                val cy = y + cellPx * 0.62f
                drawContext.canvas.nativeCanvas.drawText(
                    if (isRealDay) day.toString() else "",
                    x + cellPx / 2f,
                    cy,
                    paint
                )

                if (isRealDay && hasBirthdays) {
                    drawCircle(
                        color = Color(textColor),
                        radius = cellPx * 0.05f,
                        center = Offset(x + cellPx * 0.82f, y + cellPx * 0.18f)
                    )
                }
            }
        }
    }
}