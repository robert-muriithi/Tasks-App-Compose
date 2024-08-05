package dev.robert.datastore.data.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object ConstantUtils {
    val THEME_OPTIONS = intPreferencesKey(name = "theme_option")
    val ONBOARDING_COMPLETED = booleanPreferencesKey(name = "onboarded")
    val USER_LOGGED_IN = booleanPreferencesKey(name = "user_is_logged_in")
}
