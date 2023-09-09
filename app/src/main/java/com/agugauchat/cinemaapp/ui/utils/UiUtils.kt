package com.agugauchat.cinemaapp.ui.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object UtilsUi {
    const val STATUS_SUCCESS = 1
    const val STATUS_NO_SEATS = 2
    const val STATUS_CONVERSION_ERROR = 3
    const val STATUS_INCOMPLETE_DATA = 4
}

fun String?.toDayOfWeek(): Int? {
    return try {
        this?.let {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = dateFormat.parse(it)
            val calendar = Calendar.getInstance()
            date?.let { _date ->
                calendar.time = _date
                calendar.get(Calendar.DAY_OF_WEEK)
            }
        }
    } catch (e: Exception) {
        null
    }
}

fun allNotNull(vararg elements: Any?): Boolean {
    return elements.all { it != null }
}