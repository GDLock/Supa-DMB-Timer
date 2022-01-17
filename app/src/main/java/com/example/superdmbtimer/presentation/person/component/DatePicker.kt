package com.example.superdmbtimer.presentation.person.component

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapperLayoutInfo
import dev.chrisbanes.snapper.rememberLazyListSnapperLayoutInfo
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.*
import java.time.temporal.WeekFields
import java.util.*
import kotlin.math.abs

/* Реализация была создана в июне
* Необходим рефакторинг */

class DatePickerState(
    val initialDate: LocalDate,
    startDate: Int,
    endDate: Int,
    private val isRange: Boolean
) {
    var firstSelected by mutableStateOf(ofEpochSeconds(startDate))
    var secondSelected by mutableStateOf(ofEpochSeconds(endDate))
    var isCleared by mutableStateOf(false)

    private val now: LocalDate = LocalDate.now()

    var count = if (startDate > 0 && endDate > 0 && endDate != startDate)
        false
    else
        startDate > 0 && isRange


    fun getType(date: LocalDate): DayBoxType {
        return when {
            firstSelected == date || date == secondSelected -> DayBoxType.SELECTED
            firstSelected < date && date < secondSelected -> DayBoxType.RANGED
            date == now -> DayBoxType.NOW
            else -> DayBoxType.NONE
        }
    }

    fun select(localDate: LocalDate, function: (Int, Int) -> Unit) {
        when (count) {
            false -> {
                firstSelected = localDate
                secondSelected = localDate
                if (isRange) count = true
                isCleared = false
            }
            true -> {
                if (isRange) {
                    if (localDate.toEpochDay() < firstSelected.toEpochDay()) {
                        firstSelected = localDate
                        secondSelected = localDate
                    } else {
                        secondSelected = localDate
                        count = false
                    }
                    isCleared = false
                }
            }
        }
        function(
            firstSelected.atStartOfDay().toEpochSecond(OffsetDateTime.now().offset).toInt(),
            secondSelected.atStartOfDay().toEpochSecond(OffsetDateTime.now().offset).toInt()
        )
    }

    fun clear(onClick: (Int, Int) -> Unit) {
        isCleared = true
        firstSelected = ofEpochSeconds(-1)
        secondSelected = ofEpochSeconds(-1)
        count = false
        onClick(-1, -1)
    }

    private fun ofEpochSeconds(seconds: Int): LocalDate {
        return LocalDateTime.ofEpochSecond(seconds.toLong(), 0, OffsetDateTime.now().offset)
            .toLocalDate()
    }
}

enum class DayBoxType {
    SELECTED, RANGED, NOW, NONE
}

@OptIn(ExperimentalSnapperApi::class, ExperimentalFoundationApi::class)
@Composable
fun DatePickerView(
    modifier: Modifier = Modifier,
    initialDate: LocalDate = LocalDate.now(),
    start: Int = -1,
    end: Int = -1,
    isRange: Boolean = false,
    enabled: Boolean = true,
    yearRange: IntRange = IntRange(1980, 2100),
    onDateRangeChange: (Int, Int) -> Unit,
) {
    var date by remember { mutableStateOf(initialDate) }

    val state = remember(date) { DatePickerState(date, start, end, isRange) }
    var yearPicker by rememberSaveable { mutableStateOf(false) }

    Crossfade(targetState = yearPicker) {
        if (!it)
            DatePickerImpl(
                modifier = modifier.padding(10.dp),
                state = state,
                enabled = enabled,
                yearRange = yearRange,
                onYearClick = { yearPicker = true },
                onClick = onDateRangeChange
            )
        else {
            YearPicker(
                date = date,
                months = DatePickerUtils.monthsList,
                yearRange = yearRange,
                onClose = { yearPicker = false }
            ) { month, year -> date = LocalDate.of(year, month, 1) }
        }
    }
}

@ExperimentalSnapperApi
@ExperimentalFoundationApi
@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun DatePickerImpl(
    modifier: Modifier = Modifier,
    state: DatePickerState,
    yearRange: IntRange,
    onYearClick: () -> Unit,
    onClick: (Int, Int) -> Unit,
    enabled: Boolean,
) {
    val pagerState =
        rememberPagerState((state.initialDate.year - yearRange.first) * 12 + state.initialDate.monthValue - 1)

    Column(modifier.height(270.dp)) {

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            count = (yearRange.last - yearRange.first) * 12,
            state = pagerState,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                snapAnimationSpec = spring(stiffness = 100f)
            )
        ) { page ->
            val viewDate = remember(page) {
                LocalDate.of(
                    yearRange.first + (page.toLong() / 12).toInt(),
                    (page.toLong() % 12).toInt() + 1,
                    1
                )
            }
            Column {
                CalendarViewHeader(viewDate, pagerState) { onYearClick() }

                CalendarView(viewDate, state, enabled, onClick)
                Spacer(modifier = Modifier.weight(1f))
            }
        }

    }
}

@ExperimentalSnapperApi
@ExperimentalFoundationApi
@OptIn(ExperimentalPagerApi::class)
@Composable
private fun YearPicker(
    date: LocalDate,
    yearRange: IntRange,
    months: List<String>,
    onClose: () -> Unit,
    onSet: (Int, Int) -> Unit,
) {
    val currentM = remember(date) { date.monthValue - 1 }
    val currentY = remember(date) { date.year - yearRange.first }

    val lazyListState1 = rememberLazyListState(currentM)
    val lazyListState2 = rememberLazyListState(currentY)

    Column(
        Modifier
            .padding(10.dp)
            .height(270.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(start = 14.dp, end = 14.dp)
                .height(24.dp)
                .fillMaxWidth()
        ) {
            Text(
                "${months[currentM]} ${date.year}",
                modifier = Modifier
                    .align(Alignment.Center),
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.W600),
                color = MaterialTheme.colors.onBackground
            )
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = onClose
            ) {
                Icon(imageVector = Icons.Outlined.KeyboardArrowUp, null)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        YearPickerMenuItem(
            lazyListState1 = lazyListState1,
            lazyListState2 = lazyListState2,
            months = months,
            yearRange = yearRange,
            onDateChange = { month, year ->
                onSet(month - 1, year - 2 + yearRange.first)
            }
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}


@ExperimentalSnapperApi
@Composable
fun YearPickerMenuItem(
    lazyListState1: LazyListState,
    lazyListState2: LazyListState,
    size: Dp = 200.dp,
    onDateChange: (Int, Int) -> Unit,
    yearRange: IntRange,
    months: List<String>,
) {
    val layoutInfo2 = rememberLazyListSnapperLayoutInfo(lazyListState2)
    val layoutInfo1 = rememberLazyListSnapperLayoutInfo(lazyListState1)

    val height by remember { derivedStateOf { layoutInfo1.endScrollOffset / 5 * 2 } }

    DisposableEffect(lazyListState1.isScrollInProgress, lazyListState2.isScrollInProgress) {
        if (!lazyListState1.isScrollInProgress && !lazyListState2.isScrollInProgress)
            onDateChange(
                layoutInfo1.currentItem?.index ?: (lazyListState1.firstVisibleItemIndex + 2),
                layoutInfo2.currentItem?.index ?: (lazyListState2.firstVisibleItemIndex + 2)
            )
        onDispose {
            onDateChange(
                layoutInfo1.currentItem?.index ?: (lazyListState1.firstVisibleItemIndex + 2),
                layoutInfo2.currentItem?.index ?: (lazyListState2.firstVisibleItemIndex + 2)
            )
        }
    }

    val listMonths = remember {
        months.toMutableList().also {
            it.add(0, "")
            it.add(0, "")
            it.add("")
            it.add("")
        }
    }

    val listYear = remember {
        yearRange.map { it.toString() }.toMutableList().also {
            it.add(0, "")
            it.add(0, "")
            it.removeLast()
            it.add("")
            it.add("")
        }.toList()
    }

    Box(
        modifier = Modifier
            .height(size)
            .clip(RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            YearPickerList(
                list = listMonths,
                lazyListState = lazyListState1,
                layoutInfo = layoutInfo1,
                height = height,
                size = size,
            )

            Spacer(modifier = Modifier.width(16.dp))

            YearPickerList(
                list = listYear,
                lazyListState = lazyListState2,
                layoutInfo = layoutInfo2,
                height = height,
                size = size,
            )
        }

        Text(
            text = "0",
            color = Color.Transparent,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .background(MaterialTheme.colors.onBackground.copy(0.1f), RoundedCornerShape(5.dp))
        )
    }
}


@ExperimentalSnapperApi
@Composable
fun YearPickerList(
    lazyListState: LazyListState,
    layoutInfo: SnapperLayoutInfo,
    height: Int,
    size: Dp,
    list: List<String>
) {
    val offset by remember { derivedStateOf { layoutInfo.currentItem?.offset ?: height } }

    LazyColumn(
        modifier = Modifier.width(size / 3 * 2),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = lazyListState,
        flingBehavior = rememberSnapperFlingBehavior(
            layoutInfo = layoutInfo,
        )
    ) {
        itemsIndexed(list) { i, item ->
            Box(
                modifier = Modifier
                    .height(size / 5)
                    .width(size / 3 * 2),
                contentAlignment = Alignment.Center
            ) {
                CompositionLocalProvider(
                    LocalContentAlpha provides if (1f - measureSnapperScrollItem(
                            i,
                            layoutInfo,
                            height,
                            offset
                        ).toFloat() in 0f..1f
                    ) {
                        1f - measureSnapperScrollItem(i, layoutInfo, height, offset).toFloat()
                    } else 1f
                ) {
                    Text(
                        modifier = Modifier.graphicsLayer {
                            rotationX = measureSnapperScrollItem(
                                i,
                                layoutInfo,
                                height,
                                offset
                            ).toFloat() * 100f
                        },
                        text = item,
                        fontSize = 20.sp / (1.0 + measureSnapperScrollItem(
                            i,
                            layoutInfo,
                            height,
                            offset
                        ))
                    )
                }
            }
        }
    }
}


@ExperimentalSnapperApi
private fun measureSnapperScrollItem(
    item: Int,
    layoutInfo: SnapperLayoutInfo,
    height: Int,
    offset: Int,
    count: Int = 5
): Double {
    return (abs(
        item - (layoutInfo.currentItem?.index ?: 0)
    ) * 0.33 +
            (if (item > (layoutInfo.currentItem?.index
                    ?: 0)
            ) -1.0 else 1.0) * (height - offset).toDouble()
            /
            (layoutInfo.endScrollOffset / count).toDouble() * 0.33)
}


@ExperimentalPagerApi
@Composable
private fun CalendarViewHeader(
    viewDate: LocalDate,
    pagerState: PagerState,
    showYearPicker: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        Modifier
            .padding(start = 24.dp, end = 24.dp)
            .height(24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.KeyboardArrowLeft,
            contentDescription = "Previous Month",
            modifier = Modifier
                .size(24.dp)
                .clickable(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                ),
        )

        Text(
            "${DatePickerUtils.monthsList[viewDate.monthValue - 1]} ${viewDate.year}",
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .clickable { showYearPicker() },
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.W600),
            color = MaterialTheme.colors.onBackground
        )
        Icon(
            Icons.Default.KeyboardArrowRight,
            contentDescription = "Next Month",
            modifier = Modifier
                .size(24.dp)
                .clickable(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                ),
        )
    }
}


@ExperimentalFoundationApi
@Composable
private fun CalendarView(
    viewDate: LocalDate,
    state: DatePickerState,
    enabled: Boolean,
    onClick: (Int, Int) -> Unit
) {
    Column(Modifier.padding(start = 12.dp, end = 12.dp)) {

        DayOfWeekHeader()

        val calendarDatesData = remember { DatePickerUtils.getDates(viewDate) }

        val isSixRows = remember { (calendarDatesData.first + calendarDatesData.second.size) > 35 }

        val heightRow = remember { if (isSixRows) 36.dp else 43.dp }


        LazyVerticalGrid(cells = GridCells.Fixed(7)) {

            items(calendarDatesData.first) {
                Box(Modifier.size(heightRow))
            }

            items(calendarDatesData.second) {

                val dateType = remember(state.firstSelected, state.secondSelected) {
                    state.getType(LocalDate.of(viewDate.year, viewDate.month, it))
                }

                DateSelectionBox(it, dateType, enabled, heightRow) {
                    if (state.firstSelected >= state.secondSelected && dateType == DayBoxType.SELECTED)
                        state.clear(onClick)
                    else
                        state.select(LocalDate.of(viewDate.year, viewDate.month, it), onClick)
                }
            }
        }
    }
}

@Composable
private fun DateSelectionBox(
    date: Int,
    type: DayBoxType,
    enabled: Boolean,
    heightRow: Dp,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colors
    Box(
        Modifier
            .size(heightRow)
            .clickable(
                interactionSource = MutableInteractionSource(),
                enabled = enabled,
                onClick = onClick,
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            date.toString(),
            modifier = Modifier
                .size(35.dp)
                .clip(CircleShape)
                .background(
                    when (type) {
                        DayBoxType.SELECTED -> MaterialTheme.colors.primary
                        DayBoxType.RANGED -> MaterialTheme.colors.primary.copy(0.1f)
                        else -> Color.Transparent
                    }
                )
                .wrapContentSize(Alignment.Center),
            style = TextStyle(
                color = if (type == DayBoxType.SELECTED) colors.onPrimary else {
                    if (type == DayBoxType.NOW) colors.primary else colors.onSurface
                },
                fontSize = 16.sp
            )
        )
    }
}


@Composable
private fun DayOfWeekHeader() {
    Row(
        modifier = Modifier
            .height(30.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        for (i in 0..6) {
            Box(
                modifier = Modifier
                    .size(width = 46.dp, height = 30.dp)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    DatePickerUtils.weekdays[i],
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center),
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.W600),
                    color = MaterialTheme.colors.onBackground.copy(ContentAlpha.disabled)
                )
            }
        }
    }
}


object DatePickerUtils {

    val weekdays = listOf(
        getWeekDayByIndex(0),
        getWeekDayByIndex(1),
        getWeekDayByIndex(2),
        getWeekDayByIndex(3),
        getWeekDayByIndex(4),
        getWeekDayByIndex(5),
        getWeekDayByIndex(6),
    )

    @SuppressLint("ConstantLocale")
    private val isFirstDaySunday = WeekFields.of(Locale.getDefault()).firstDayOfWeek.value != 1

    fun getDates(date: LocalDate): Pair<Int, List<Int>> {
        val numDays = date.month.length(date.isLeapYear)
        val firstDate = date.withDayOfMonth(1)
        val firstDay =
            if (isFirstDaySunday)
                firstDate.dayOfWeek.value % 7
            else firstDate.dayOfWeek.value - 1

        val dateRange = IntRange(1, numDays).toList()

        return Pair(firstDay, dateRange)
    }

    private fun getWeekDayByIndex(i: Int): String {
        val day: DayOfWeek =
            if (isFirstDaySunday)
                DayOfWeek.of((i + 6) % 7 + 1)
            else DayOfWeek.of(i + 1)

        return day.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault())
    }

    val monthsList = Month.values().map {
        SimpleDateFormat("LLLL", Locale.getDefault()).format(Date(0, it.value, 0))
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}