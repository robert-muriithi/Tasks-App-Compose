package dev.robert.auth.presentation.screens.login

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val email: String = "",
    val password: String = "",
    val name: String = ""
)
