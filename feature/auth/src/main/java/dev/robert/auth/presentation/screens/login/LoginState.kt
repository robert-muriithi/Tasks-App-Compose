package dev.robert.auth.presentation.screens.login

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val email: String = "",
    val emailError: String? = "",
    val password: String = "",
    val passwordError: String? = "",
    val buttonEnabled: Boolean = false,
    val error: String? = null,
)
