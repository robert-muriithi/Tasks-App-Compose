package dev.robert.profile.presentation.screens

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.datastore.data.TodoAppPreferences
import dev.robert.profile.domain.repository.ProfileRepository
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val preferences: TodoAppPreferences
) : ViewModel() {

    private val _profile = MutableStateFlow(ProfileScreenState())
    val profile = _profile.asStateFlow()

    val _action = Channel<ProfileScreenEvents>()
    val action = _action.receiveAsFlow()
}
