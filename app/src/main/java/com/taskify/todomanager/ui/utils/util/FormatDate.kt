package com.taskify.todomanager.ui.utils.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateString: String?): String {
    if (dateString.isNullOrEmpty()) return "No date available"

    val formatter = DateTimeFormatter.ofPattern("dd - MM - yyyy")
    val taskDate = LocalDate.parse(dateString, formatter)
    val today = LocalDate.now()

    return when {
        taskDate == today -> "Today"
        taskDate.year == today.year -> taskDate.format(DateTimeFormatter.ofPattern("dd - MM"))
        else -> taskDate.format(formatter)
    }
}
