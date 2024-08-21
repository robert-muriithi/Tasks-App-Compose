package dev.robert.design_system.presentation.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat(
        "E, MMM dd, yyyy",
        Locale.getDefault())
    return formatter.format(Date(millis))
}
@OptIn(ExperimentalMaterial3Api::class)
fun formatTimeToAmPm(timePickerState: TimePickerState): String {
    val hour = timePickerState.hour
    val minute = timePickerState.minute

    val localTime = LocalTime.of(hour, minute)
    val formatter = DateTimeFormatter.ofPattern("h:mm a")
    return localTime.format(formatter).replace(" ", "")
}
