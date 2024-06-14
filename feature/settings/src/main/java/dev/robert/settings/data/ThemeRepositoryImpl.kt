package dev.robert.settings.data

import dev.robert.datastore.data.TodoAppPreferences
import dev.robert.settings.domain.ThemeRepository
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
