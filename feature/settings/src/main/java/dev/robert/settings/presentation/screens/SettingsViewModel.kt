package dev.robert.settings.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.design_system.presentation.theme.Theme
import dev.robert.settings.domain.ThemeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themeRepository: ThemeRepository
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
        val user = mAuth.currentUser
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    email = user?.email ?: "",
                    name = user?.displayName ?: "",
                    profileImageUrl = user?.photoUrl.toString(),
                )
            }
        }
    }

    private fun changeTheme(theme: Theme) {
        viewModelScope.launch {
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