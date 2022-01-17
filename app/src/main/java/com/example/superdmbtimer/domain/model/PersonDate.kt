package com.example.superdmbtimer.domain.model

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class PersonDate(
    val start: Int = -1,
    val end: Int = -1
) {
    fun getDate(): String {
        val dateStart = if (start != -1) getDateByMills(start) else ""
        val dateEnd = if (start != end) " - ${getDateByMills(end)}" else ""

        return dateStart + dateEnd
    }

    private fun getDateByMills(d: Int): String {
        val formatter = DateTimeFormatter.ofPattern("dd MMMM y")
        val date = LocalDateTime.ofEpochSecond(d.toLong(), 0, OffsetDateTime.now().offset)
        return date.format(formatter)
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
    }
}