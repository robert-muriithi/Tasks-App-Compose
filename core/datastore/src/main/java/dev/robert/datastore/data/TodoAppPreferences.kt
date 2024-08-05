package dev.robert.datastore.data

import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import dev.robert.datastore.data.utils.ConstantUtils
import dev.robert.datastore.data.utils.ConstantUtils.THEME_OPTIONS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoAppPreferences(
    private val prefs: DataStore<Preferences>,
) {
    suspend fun saveTheme(theme: Int) =
        prefs.edit {
            it[THEME_OPTIONS] = theme
        }

    val themeValue: Flow<Int> =
        prefs.data.map {
            it[THEME_OPTIONS] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

    suspend fun saveOnboardingCompleted(complete: Boolean) = prefs.edit {
        it[ConstantUtils.ONBOARDING_COMPLETED] = complete
    }

    val onboardingCompleted: Flow<Boolean> =
        prefs.data.map {
            it[ConstantUtils.ONBOARDING_COMPLETED] ?: false
        }

    suspend fun saveUserLoggedIn(loggedIn: Boolean) = prefs.edit {
        it[ConstantUtils.USER_LOGGED_IN] = loggedIn
    }

    val userLoggedIn: Flow<Boolean> =
        prefs.data.map {
            it[ConstantUtils.USER_LOGGED_IN] ?: false
        }
}
