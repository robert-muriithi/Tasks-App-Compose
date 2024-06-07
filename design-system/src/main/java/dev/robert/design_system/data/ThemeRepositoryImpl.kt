package dev.robert.design_system.data

import dev.robert.datastore.data.TodoAppPreferences
import dev.robert.design_system.domain.ThemeRepository
import kotlinx.coroutines.flow.Flow

class ThemeRepositoryImpl(
    val prefs: TodoAppPreferences,
) : ThemeRepository {
    override val themeValue: Flow<Int>
        get() = prefs.themeValue

    override suspend fun setTheme(themeValue: Int) {
        prefs.saveTheme(themeValue)
    }
}
