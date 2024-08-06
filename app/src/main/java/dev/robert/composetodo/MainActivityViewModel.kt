package dev.robert.composetodo

import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.auth.domain.repository.AuthenticationRepository
import dev.robert.auth.presentation.navigation.AuthNavGraph
import dev.robert.design_system.presentation.theme.Theme
import dev.robert.onboarding.domain.repository.OnBoardingRepository
import dev.robert.onboarding.presentation.navigation.OnBoardingNavGraph
import dev.robert.settings.domain.ThemeRepository
import dev.robert.tasks.presentation.navigation.TasksNavGraph
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    themeRepository: ThemeRepository,
    onBoardingRepository: OnBoardingRepository,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private var _startDestination = MutableStateFlow(Any())
    val startDestination = _startDestination.asStateFlow()

    private val _showSplashScreen = MutableStateFlow(true)
    val showSplashScreen: StateFlow<Boolean> = _showSplashScreen

    val currentTheme: StateFlow<Int> = themeRepository.themeValue
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Theme.FOLLOW_SYSTEM.themeValue
        )

    private val onboardingStatus: StateFlow<Boolean> = onBoardingRepository.isOnboarded
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    private val authenticationStatus: StateFlow<Boolean> = onBoardingRepository.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    private val combinedStatus: StateFlow<Pair<Boolean, Boolean>> = combine(
        onboardingStatus,
        authenticationStatus
    ) { onboarded, authenticated ->
        Pair(onboarded, authenticated)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Pair(false, false)
    )

    fun signOut() = viewModelScope.launch { authenticationRepository.logout() }

    private val isUserLoggedIn = authenticationRepository.userLoggedIn

    init {
        viewModelScope.launch {
            combinedStatus.collectLatest { (onboarded, authenticated) ->
                if (!onboarded) {
                    _startDestination.update { OnBoardingNavGraph }
                } else if (authenticated && isUserLoggedIn) {
                    _startDestination.update { TasksNavGraph }
                } else {
                    _startDestination.update { AuthNavGraph }
                }
                delay(200)
                _showSplashScreen.value = false
            }
        }
    }
}
