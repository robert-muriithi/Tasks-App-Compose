package dev.robert.auth.presentation.screens.register

import dev.robert.auth.domain.model.GoogleUser

data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val email: String = "",
    val emailError: String? = "",
    val password: String = "",
    val passwordError: String? = "",
    val confirmPassword: String = "",
    val confirmPasswordError: String? = "",
    val name: String = "",
    val nameError: String? = "",
    val error: String? = "",
    val buttonEnabled: Boolean = true,
    val user: GoogleUser? = null
)
