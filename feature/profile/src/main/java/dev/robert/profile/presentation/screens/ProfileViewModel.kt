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
package dev.robert.profile.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.datastore.data.TodoAppPreferences
import dev.robert.profile.domain.repository.ProfileRepository
import dev.robert.tasks.domain.repository.TasksRepository
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val tasksRepository: TasksRepository,
    preferences: TodoAppPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileScreenState())
    val uiState = _uiState.asStateFlow()

    private val _action = Channel<ProfileScreenActions>()
    val action = _action.receiveAsFlow()

    private val loginType = preferences.loginType
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    fun onEvent(event: ProfileScreenEvents) {
        when (event) {
            is ProfileScreenEvents.EditProfile -> {}
        }
    }

    private fun loadProfile() = viewModelScope.launch {
        loginType.collectLatest { type ->
            when (type) {
                "google" -> {
                    repository.getProfileGoogleSignIn().collectLatest { profile ->
                        _uiState.update { it.copy(profile = profile, loginType = type) }
                    }
                }
                else -> {
                    repository.getProfileFirebaseFirestore().collectLatest { result ->
                        result.onSuccess { profile ->
                            _uiState.update { it.copy(profile = profile, loginType = type.toString()) }
                        }
                        result.onFailure { error ->
                            Timber.e(error)
                            _action.send(
                                ProfileScreenActions.ShowError(
                                    error.message ?: "An error occurred"
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    init {
        loadProfile()
    }
}

sealed class ProfileScreenActions {
    object NavigateToEditProfile : ProfileScreenActions()
    data class ShowError(val message: String) : ProfileScreenActions()
}
