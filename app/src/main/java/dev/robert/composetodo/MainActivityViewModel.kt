package dev.robert.composetodo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.design_system.presentation.theme.Theme
import dev.robert.onboarding.domain.repository.OnBoardingRepository
import dev.robert.settings.domain.ThemeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
    themeRepository: ThemeRepository,
    onBoardingRepository: OnBoardingRepository
) : ViewModel() {

    val currentTheme: StateFlow<Int> = themeRepository.themeValue
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Theme.FOLLOW_SYSTEM.themeValue
        )

    val onboardingStatus: StateFlow<Boolean> = onBoardingRepository.isOnboarded
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
}
