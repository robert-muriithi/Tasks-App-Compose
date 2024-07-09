package dev.robert.auth.presentation.screens.login

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import dev.robert.auth.domain.model.GoogleSignResult
import dev.robert.auth.presentation.utils.GoogleAuthSignInClient

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

    data class GoogleSignInEvent(
        val client: GoogleAuthSignInClient,
        val launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
    ) : LoginScreenEvents()
}
