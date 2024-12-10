package dev.robert.tasks.presentation.utils

import android.graphics.Color.parseColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun String?.toSafeColor(defaultColor: Color = MaterialTheme.colorScheme.tertiaryContainer): Color {
    return if (!isNullOrBlank()) {
        try {
            Color(parseColor(this))
        } catch (e: IllegalArgumentException) {
            defaultColor
        }
    } else {
        defaultColor
    }
}

