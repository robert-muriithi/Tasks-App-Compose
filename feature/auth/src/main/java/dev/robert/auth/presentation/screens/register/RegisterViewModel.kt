package dev.robert.auth.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.auth.domain.repository.AuthenticationRepository
import dev.robert.auth.presentation.utils.EmailValidator
import dev.robert.auth.presentation.utils.PasswordMatchValidator
import dev.robert.auth.presentation.utils.PasswordValidator
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthenticationRepository,
    private val passwordValidator: PasswordValidator,
    private val emailValidator: EmailValidator,
    private val confirmPasswordValidator: PasswordMatchValidator
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState = _uiState.asStateFlow()

    private val _action = Channel<RegisterAction>()
    val action = _action.receiveAsFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.update { it.copy(error = exception.message) }
    }
    fun onNameChanged(name: String) = _uiState.update { it.copy(name = name) }
    fun onEmailChanged(email: String) = _uiState.update { it.copy(email = email) }
    fun onPasswordChanged(password: String) = _uiState.update { it.copy(password = password) }
    fun onConfirmPasswordChanged(confirmPassword: String) = _uiState.update { it.copy(confirmPassword = confirmPassword) }

    fun register() {
        val currentState = uiState.value
        val emailValid = emailValidator.validate(currentState.email)
        val passwordValid = passwordValidator.validate(currentState.password)
        val confirmPasswordValid = confirmPasswordValidator.validate(currentState.password, currentState.confirmPassword)
        val hasError = listOf(emailValid, passwordValid, confirmPasswordValid).any { !it.isValid }
        if (hasError) _uiState.update { it.copy(emailError = emailValid.message, passwordError = passwordValid.message, confirmPasswordError = confirmPasswordValid.message) }
        viewModelScope.launch(coroutineExceptionHandler) {
//            validationEventChannel.send(ValidationEvent.Success)
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.register(currentState.email, currentState.password)
                _uiState.update { it.copy(isSuccess = true, isLoading = false) }
                _action.send(RegisterAction.NavigateToLogin)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}
sealed class RegisterAction {
    object NavigateToLogin : RegisterAction()
}
