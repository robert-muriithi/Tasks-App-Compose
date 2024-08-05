package dev.robert.auth.presentation.screens.login

import dev.robert.auth.domain.model.GoogleUser

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isAuthenticated: Boolean = false,
    val email: String = "",
    val emailError: String? = "",
    val password: String = "",
    val passwordError: String? = "",
    val buttonEnabled: Boolean = false,
    val error: String? = null,
    val user: GoogleUser? = null,
    val signInOption: SignInOption = SignInOption.EmailAndPassword
)
enum class SignInOption {
    EmailAndPassword,
    Google
}
