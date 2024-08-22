package dev.robert.datastore.data.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object ConstantUtils {
    val THEME_OPTIONS = intPreferencesKey(name = "theme_option")
    val ONBOARDING_COMPLETED = booleanPreferencesKey(name = "onboarded")
    val USER_LOGGED_IN = booleanPreferencesKey(name = "user_is_logged_in")
    val LOGIN_TYPE = stringPreferencesKey(name = "login_type")
    val IS_GRID_VIEW = booleanPreferencesKey(name = "grid_view")
}
