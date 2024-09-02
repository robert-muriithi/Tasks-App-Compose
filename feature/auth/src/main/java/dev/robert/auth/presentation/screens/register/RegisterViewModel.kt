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
package dev.robert.auth.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.auth.domain.model.RegisterRequestBody
import dev.robert.auth.domain.repository.AuthenticationRepository
import dev.robert.auth.presentation.utils.EmailValidator
import dev.robert.auth.presentation.utils.NameValidator
import dev.robert.auth.presentation.utils.PasswordMatchValidator
import dev.robert.auth.presentation.utils.PasswordValidator
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthenticationRepository,
    private val passwordValidator: PasswordValidator,
    private val emailValidator: EmailValidator,
    private val confirmPasswordValidator: PasswordMatchValidator,
    private val nameValidator: NameValidator,
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
    fun onConfirmPasswordChanged(confirmPassword: String) =
        _uiState.update { it.copy(confirmPassword = confirmPassword) }

    fun register() {
        val currentState = uiState.value
        val nameValid = nameValidator.validate(currentState.name)
        val emailValid = emailValidator.validate(currentState.email)
        val passwordValid = passwordValidator.validate(currentState.password)
        val confirmPasswordValid =
            confirmPasswordValidator.validate(currentState.password, currentState.confirmPassword)
        val hasError =
            listOf(emailValid, passwordValid, confirmPasswordValid, nameValid).any { !it.isValid }
        if (hasError) {
            _uiState.update {
                it.copy(
                    emailError = emailValid.message,
                    passwordError = passwordValid.message,
                    confirmPasswordError = confirmPasswordValid.message,
                    nameError = nameValid.message
                )
            }
            return
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val requestBody = RegisterRequestBody(
                    email = currentState.email.trim(),
                    password = currentState.password.trim(),
                    name = currentState.name.trim()
                )
                repository.registerWithEmailAndPassword(requestBody)
                    .collectLatest { result ->
                        if (result.isSuccess) {
                            val user = result.getOrNull()
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isSuccess = true,
                                    user = user
                                )
                            }
                            _action.send(RegisterAction.NavigateToLogin)
                        } else {
                            val message = result.exceptionOrNull()?.message ?: "An error occurred"
                            _uiState.update { it.copy(error = message, isLoading = false) }
                            _action.send(RegisterAction.ShowError(message))
                        }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
                _action.send(RegisterAction.ShowError(e.message ?: "An error occurred"))
            }
        }
    }

    fun resetState() {
        _uiState.update { RegisterState() }
    }
}

sealed class RegisterAction {
    data object NavigateToLogin : RegisterAction()
    data class ShowError(val message: String) : RegisterAction()
}
