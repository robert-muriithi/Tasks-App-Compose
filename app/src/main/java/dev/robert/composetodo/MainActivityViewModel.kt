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
import dev.robert.tasks.presentation.navigation.TasksNavGraph
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MainActivityViewModel @Inject constructor(
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

    private fun init() {
        Timber.d("IsAuthenticated: ${authenticationStatus.value}")
        Timber.d("IsOnboarded: ${onboardingStatus.value}")
        combine(
            onboardingStatus,
            authenticationStatus
        ) { onboarded: Boolean, authenticated: Boolean ->
            Timber.d("onboardingStatus: $onboarded, authenticationStatus: $authenticated")
            if (onboarded) {
                if (authenticated) {
                    Timber.d("IsAuthenticated: ${authenticationStatus.value}, $authenticated")
                    TasksNavGraph
                } else {
                    AuthNavGraph
                }
            } else {
                OnBoardingNavGraph
            }
        }.onEach { destination ->
            _startDestination.value = destination
            delay(300)
            _showSplashScreen.value = false
        }.launchIn(viewModelScope)
    }

    init {
        viewModelScope.launch {
            combinedStatus.collect { (onboarded, authenticated) ->
                if (onboarded) {
                    if (authenticated) {
                        Timber.d("IsAuthenticated: ${authenticationStatus.value}")
                        _startDestination.value = TasksNavGraph
                        _showSplashScreen.value = false
                    } else {
                        _startDestination.value = AuthNavGraph
                        _showSplashScreen.value = false
                    }
                } else {
                    _startDestination.value = OnBoardingNavGraph
                    _showSplashScreen.value = false
                }
            }
        }
    }
}
