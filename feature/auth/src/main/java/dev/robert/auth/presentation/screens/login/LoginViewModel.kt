package dev.robert.auth.presentation.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.auth.domain.repository.AuthenticationRepository
import dev.robert.auth.presentation.utils.EmailValidator
import dev.robert.auth.presentation.utils.PasswordValidator
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthenticationRepository,
    private val passwordValidator: PasswordValidator,
    private val emailValidator: EmailValidator,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState = _uiState.asStateFlow()

    private val _authenticated = MutableStateFlow(false)
    val authenticated = _authenticated.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.update { it.copy(error = exception.message) }
    }
    val showDialog = mutableStateOf(false)

    private fun onEmailChange(email: String) = _uiState.update { uiState ->
        uiState.copy(email = email)
    }
    private fun onPasswordChange(password: String) = _uiState.update { uiState ->
        uiState.copy(password = password)
    }

    fun setAuthenticated(authenticated: Boolean) = _authenticated.update { authenticated }

    fun onEvent(event: LoginScreenEvents) {
        when (event) {
            is LoginScreenEvents.OnEmailChanged -> onEmailChange(event.email)
            is LoginScreenEvents.OnPasswordChanged -> onPasswordChange(event.password)
            is LoginScreenEvents.LoginEvent -> login()
            is LoginScreenEvents.SignInWithGoogle -> signInWithGoogle()
        }
    }

    fun login() {
        val currentState = uiState.value
        val emailValid = emailValidator.validate(currentState.email)
        val passwordValid = passwordValidator.validate(currentState.password)
        val hasError = listOf(emailValid, passwordValid).any { !it.isValid }
        _uiState.update {
            it.copy(
                buttonEnabled = listOf(
                    emailValid,
                    passwordValid
                ).all { it.isValid }
            )
        }
        if (hasError) return
        viewModelScope.launch(coroutineExceptionHandler) {
            _uiState.update { it.copy(isLoading = true) }
            repository.login(currentState.email, currentState.password)
            _uiState.update { it.copy(isLoading = false, isSuccess = true) }
        }
    }

    fun signInWithGoogle() {
        val currentAuthState = authenticated.value
    }
}
