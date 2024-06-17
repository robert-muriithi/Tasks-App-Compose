package dev.robert.auth.presentation.screens.login

sealed class LoginScreenEvents {
    object LoginEvent : LoginScreenEvents()

    data class OnEmailChanged(
        val email: String,
    ) : LoginScreenEvents()

    data class OnPasswordChanged(
        val password: String,
    ) : LoginScreenEvents()
}
