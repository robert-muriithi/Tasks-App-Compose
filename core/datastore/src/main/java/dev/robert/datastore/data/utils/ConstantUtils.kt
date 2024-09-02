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
    val USER_NAME = stringPreferencesKey(name = "user_name")
    val USER_EMAIL = stringPreferencesKey(name = "user_email")
    val USER_PASSWORD = stringPreferencesKey(name = "user_password")
    val USER_UID = stringPreferencesKey(name = "user_password")
}
