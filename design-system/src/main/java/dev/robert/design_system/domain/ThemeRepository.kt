package dev.robert.design_system.domain

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    suspend fun setTheme(themeValue: Int)

    val themeValue: Flow<Int>
}
