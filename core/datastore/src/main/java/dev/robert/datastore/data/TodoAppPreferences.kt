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
package dev.robert.datastore.data

import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import dev.robert.datastore.data.utils.ConstantUtils
import dev.robert.datastore.data.utils.ConstantUtils.THEME_OPTIONS
import dev.robert.datastore.domain.model.UserData
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

    suspend fun saveLoginType(loginType: String) = prefs.edit {
        it[ConstantUtils.LOGIN_TYPE] = loginType
    }

    val loginType: Flow<String> =
        prefs.data.map {
            it[ConstantUtils.LOGIN_TYPE] ?: ""
        }

    val isGridView: Flow<Boolean> =
        prefs.data.map {
            it[ConstantUtils.IS_GRID_VIEW] ?: true
        }

    suspend fun saveGridView(isGridView: Boolean) = prefs.edit {
        it[ConstantUtils.IS_GRID_VIEW] = isGridView
    }

    suspend fun saveUserData(uid: String, name: String, email: String, password: String) = prefs.edit {
        it[ConstantUtils.USER_NAME] = name
        it[ConstantUtils.USER_EMAIL] = email
        it[ConstantUtils.USER_PASSWORD] = password
        it[ConstantUtils.USER_UID] = uid
    }

    val userData: Flow<UserData> =
        prefs.data.map {
            UserData(
                it[ConstantUtils.USER_UID] ?: "",
                it[ConstantUtils.USER_NAME] ?: "",
                it[ConstantUtils.USER_EMAIL] ?: "",
                it[ConstantUtils.USER_PASSWORD] ?: "",
            )
        }

    suspend fun clearPreferences() = prefs.edit {
        it.clear()
    }

    companion object {
        const val LOGIN_TYPE_GOOGLE = "google"
        const val LOGIN_TYPE_EMAIL = "email"
    }
}
