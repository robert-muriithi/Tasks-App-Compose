package dev.robert.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.onboarding.domain.repository.OnBoardingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val repository: OnBoardingRepository
) : ViewModel() {

    private val _currentValue = MutableStateFlow(0)
    val currentValue = _currentValue.asStateFlow()

    private fun onCompleteOnboarding() {
        viewModelScope.launch {
            repository.setOnboarded(isOnboarded = true)
        }
    }
    fun onEvent(event: OnboardingScreenEvent) {
        when (event) {
            is OnboardingScreenEvent.OnboardingCompleted -> onCompleteOnboarding()
        }
    }
}
