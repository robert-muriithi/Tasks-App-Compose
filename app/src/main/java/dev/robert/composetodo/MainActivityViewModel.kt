package dev.robert.composetodo

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.auth.presentation.navigation.AuthNavGraph
import dev.robert.design_system.presentation.theme.Theme
import dev.robert.onboarding.domain.repository.OnBoardingRepository
import dev.robert.onboarding.presentation.navigation.OnBoardingNavGraph
import dev.robert.settings.domain.ThemeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
    themeRepository: ThemeRepository,
    onBoardingRepository: OnBoardingRepository
) : ViewModel() {

    private val _startDestination = mutableStateOf(Any())
    val startDestination = _startDestination

    private val _showSplashScreen = mutableStateOf(true)
    val showSplashScreen: State<Boolean> = _showSplashScreen

    val currentTheme: StateFlow<Int> = themeRepository.themeValue
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Theme.FOLLOW_SYSTEM.themeValue
        )

    private val onboardingStatus: StateFlow<Boolean> = onBoardingRepository.isOnboarded
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    private fun init() {
        combine(
            onboardingStatus
        ) { onboarded ->
            onboarded.first().let {
                if (it) AuthNavGraph
                else OnBoardingNavGraph
            }
        }.onEach { destination ->
            _startDestination.value = destination
            _showSplashScreen.value = false
        }.launchIn(viewModelScope)
    }

    init {
        init()
    }
}
