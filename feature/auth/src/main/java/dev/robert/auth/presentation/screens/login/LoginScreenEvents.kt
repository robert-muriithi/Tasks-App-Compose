package dev.robert.auth.presentation.screens.login

import dev.robert.auth.domain.model.GoogleSignResult

sealed class LoginScreenEvents {
    object LoginEvent : LoginScreenEvents()

    data class OnEmailChanged(
        val email: String,
    ) : LoginScreenEvents()

    data class OnPasswordChanged(
        val password: String,
    ) : LoginScreenEvents()

    data class OnSignInWithGoogle(val result: GoogleSignResult) : LoginScreenEvents()

    data object OnResetState : LoginScreenEvents()
}
