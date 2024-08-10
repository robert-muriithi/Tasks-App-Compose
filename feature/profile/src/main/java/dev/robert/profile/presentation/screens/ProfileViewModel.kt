package dev.robert.profile.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.datastore.data.TodoAppPreferences
import dev.robert.profile.domain.repository.ProfileRepository
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
    preferences: TodoAppPreferences
) : ViewModel() {

    private val _profile = MutableStateFlow(ProfileScreenState())
    val profile = _profile.asStateFlow()

    private val _action = Channel<ProfileScreenActions>()
    val action = _action.receiveAsFlow()

    private val loginType = preferences.loginType
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ""
        )

    fun onEvent(event: ProfileScreenEvents) {
        when (event) {
            is ProfileScreenEvents.LoadProfile -> loadProfile()
            is ProfileScreenEvents.EditProfile -> {}
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            loginType.collectLatest { type ->
                Timber.d("Login type: $type")
                when (type) {
                    TodoAppPreferences.LOGIN_TYPE_GOOGLE -> {
                        val profile = repository.getProfileGoogleSignIn()
                            .stateIn(
                                scope = viewModelScope,
                                started = SharingStarted.WhileSubscribed(5000L),
                                initialValue = null
                            )
                        _profile.update {
                            it.copy(profile = profile.value, isLoading = false, loginType = TodoAppPreferences.LOGIN_TYPE_GOOGLE)
                        }
                    }

                    TodoAppPreferences.LOGIN_TYPE_EMAIL -> {
                        val profile = repository.getProfileFirebaseFirestore()
                            .stateIn(
                                scope = viewModelScope,
                                started = SharingStarted.WhileSubscribed(5000L),
                                initialValue = null
                            ).value
                        if (profile?.isSuccess == true) _profile.update {
                            it.copy(profile = profile.getOrNull(), isLoading = false, loginType = TodoAppPreferences.LOGIN_TYPE_EMAIL)
                        } else {
                            _profile.update {
                                it.copy(error = profile?.exceptionOrNull()?.message, isLoading = false)
                            }
                            _action.send(ProfileScreenActions.ShowError(profile?.exceptionOrNull()?.message ?: "Error"))
                        }
                    }
                }
            }
        }
    }
}

sealed class ProfileScreenActions {
    object NavigateToEditProfile : ProfileScreenActions()
    data class ShowError(val message: String) : ProfileScreenActions()
}
