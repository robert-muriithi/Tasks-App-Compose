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
package dev.robert.resources.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.auth.domain.model.GoogleUser
import dev.robert.auth.domain.repository.AuthenticationRepository
import dev.robert.design_system.presentation.theme.Theme
import dev.robert.navigation.auth.AuthNavGraph
import dev.robert.navigation.onboarding.OnBoardingNavGraph
import dev.robert.navigation.tasks.TasksNavGraph
import dev.robert.onboarding.domain.repository.OnBoardingRepository
import dev.robert.settings.domain.ThemeRepository
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    themeRepository: ThemeRepository,
    onBoardingRepository: OnBoardingRepository,
    private val authenticationRepository: AuthenticationRepository,
) : ViewModel() {

    private var _startDestination = MutableStateFlow(Any())
    val startDestination = _startDestination.asStateFlow()

    private val _showSplashScreen = MutableStateFlow(true)
    val showSplashScreen: StateFlow<Boolean> = _showSplashScreen

    private val _userData = MutableStateFlow(UserDataState())
    val userData: StateFlow<UserDataState> = _userData.asStateFlow()

    private val _isToggled = MutableStateFlow(false)
    val isToggled = _isToggled.asStateFlow()

    fun toggleState() {
        _isToggled.update { !it }
    }

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

//    private val loginType = preferences.loginType
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000L),
//            initialValue = null
//        )

    fun clearUserData() = viewModelScope.launch { authenticationRepository.clearUserData() }

    fun setUser(user: GoogleUser) {
        _userData.update {
            it.copy(
                name = user.name.ifEmpty { FirebaseAuth.getInstance().currentUser?.displayName },
                email = user.email,
                photoUrl = user.photoUrl.ifEmpty { FirebaseAuth.getInstance().currentUser?.photoUrl.toString() },
                id = user.id.ifEmpty { FirebaseAuth.getInstance().currentUser?.uid.toString() }
            )
        }
    }

    private val isUserLoggedIn = authenticationRepository.userLoggedIn

    init {
        viewModelScope.launch {
            combinedStatus.collectLatest { (onboarded, authenticated) ->
                if (!onboarded) {
                    _startDestination.update { OnBoardingNavGraph }
                } else if (authenticated && isUserLoggedIn) {
                    val uId = authenticationRepository.userId.firstOrNull()
                    val user = uId?.let { authenticationRepository.getUserFromFirestore(it) }
                    user?.let {
                        setUser(it)
                        _startDestination.update { TasksNavGraph }
                    }
//                    }
                } else {
                    _startDestination.update { AuthNavGraph }
                }
                delay(1000)
                _showSplashScreen.value = false
            }
        }
    }
}

data class UserDataState(
    val id: String = "",
    val name: String? = "",
    val email: String = "",
    val password: String? = "",
    val photoUrl: String? = ""
)
