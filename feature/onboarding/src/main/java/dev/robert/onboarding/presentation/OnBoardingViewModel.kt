package dev.robert.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.datastore.data.TodoAppPreferences
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val appPrefs: TodoAppPreferences
) : ViewModel() {

    private val _currentValue = MutableStateFlow(0)
    val currentValue = _currentValue.asStateFlow()

    fun onCompleteOnboarding() {
        viewModelScope.launch {
            appPrefs.saveOnboardingCompleted(true)
        }
    }
}
