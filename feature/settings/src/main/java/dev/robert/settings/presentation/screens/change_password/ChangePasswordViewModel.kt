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
package dev.robert.settings.presentation.screens.change_password

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ChangePasswordViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ChangePasswordState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ChangePasswordEvents) {
        when (event) {
            is ChangePasswordEvents.ChangePassword -> {
            }
        }
    }

    private fun onCurrentPasswordChanged(currentPassword: String) {
        _uiState.update {
            it.copy(
                currentPassword = currentPassword,
            )
        }
    }

    private fun onNewPasswordChanged(newPassword: String) {
        _uiState.update { currentState ->
            val updatedConditions = updatePasswordConditions(newPassword, currentState.confirmPassword)
            currentState.copy(
                newPassword = newPassword,
                strongPasswordsConditions = updatedConditions
            )
        }
    }

    private fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { currentState ->
            val updatedConditions = updatePasswordConditions(currentState.newPassword, confirmPassword)
            currentState.copy(
                confirmPassword = confirmPassword,
                strongPasswordsConditions = updatedConditions
            )
        }
    }

    private fun updatePasswordConditions(newPassword: String, confirmPassword: String): List<StrongPasswordCondition> {
        return _uiState.value.strongPasswordsConditions.map { condition ->
            when (condition.description) {
                "At least 8 characters" -> condition.copy(isMet = newPassword.length >= 8)
                "At least 1 uppercase letter" -> condition.copy(isMet = newPassword.any { it.isUpperCase() })
                "At least 1 lowercase letter" -> condition.copy(isMet = newPassword.any { it.isLowerCase() })
                "At least 1 number" -> condition.copy(isMet = newPassword.any { it.isDigit() })
                "At least 1 special character" -> condition.copy(isMet = newPassword.any { !it.isLetterOrDigit() })
                "No consecutive numbers" -> condition.copy(isMet = !hasConsecutiveNumbers(newPassword))
                "Password must match" -> condition.copy(isMet = newPassword == confirmPassword && newPassword.isNotEmpty())
                else -> condition
            }
        }
    }

    private fun hasConsecutiveNumbers(password: String): Boolean {
        val consecutivePatterns = listOf("012", "123", "234", "345", "456", "567", "678", "789")
        return consecutivePatterns.any { it in password }
    }

    fun areAllPasswordConditionsMet(): Boolean {
        val currentUiStatusConditions = _uiState.value.strongPasswordsConditions
        return currentUiStatusConditions.all { it.isMet }
    }

    fun onInputChange(input: InputChange) {
        when (input) {
            is InputChange.CurrentPasswordChange -> onCurrentPasswordChanged(input.value)
            is InputChange.NewPasswordChange -> onNewPasswordChanged(input.value)
            is InputChange.ConfirmPasswordChange -> onConfirmPasswordChanged(input.value)
        }
    }
}

sealed class InputChange {
    data class CurrentPasswordChange(val value: String) : InputChange()
    data class NewPasswordChange(val value: String) : InputChange()
    data class ConfirmPasswordChange(val value: String) : InputChange()
}
