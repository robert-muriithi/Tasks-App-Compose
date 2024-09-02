/*
 * Copyright 2024 Robert Muriithi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.robert.design_system.presentation.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.net.InetAddress
import java.text.SimpleDateFormat
import java.util.Calendar
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

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun isInternetAvailable(): Boolean {
    return try {
        val address = InetAddress.getByName("www.google.com")
        val reachable = (1..5).any { _ ->
            address.isReachable(3000)
        }
        reachable
    } catch (e: Exception) {
        false
    }
}
