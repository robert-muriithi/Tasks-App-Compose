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
package dev.robert.settings.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.auth.domain.repository.AuthenticationRepository
import dev.robert.datastore.data.TodoAppPreferences
import dev.robert.design_system.presentation.theme.Theme
import dev.robert.settings.domain.ThemeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themeRepository: ThemeRepository,
    private val prefs: TodoAppPreferences,
    private val authenticationRepository: AuthenticationRepository,
) : ViewModel() {

    private val mAuth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState = _uiState.asStateFlow()

    val themeValue = themeRepository.themeValue
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Theme.FOLLOW_SYSTEM.themeValue
        )

    private fun loadSettings() {
        viewModelScope.launch {
            val loginType = prefs.loginType.firstOrNull()
            when (loginType) {
                "google" -> {
                    val user = mAuth.currentUser
                    _uiState.update {
                        it.copy(
                            email = user?.email ?: "",
                            name = user?.displayName ?: "",
                            profileImageUrl = user?.photoUrl.toString(),
                            selectedTheme = themeValue.value
                        )
                    }
                }

                else -> {
                    val userId = prefs.userData.firstOrNull()?.id ?: ""
                    val user = authenticationRepository.getUserFromFirestore(userId)
                    _uiState.update {
                        it.copy(
                            email = user?.email ?: "",
                            name = user?.name ?: "",
                            selectedTheme = themeValue.value
                        )
                    }
                }
            }
        }
    }

    private fun changeTheme(theme: Theme) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedTheme = theme.themeValue) }
            themeRepository.setTheme(theme.themeValue)
        }
    }

    fun onEvent(events: SettingsEvents) {
        when (events) {
            is SettingsEvents.LoadSettings -> loadSettings()
            is SettingsEvents.ChangeTheme -> changeTheme(events.theme)
            is SettingsEvents.Logout -> mAuth.signOut()
        }
    }
}
