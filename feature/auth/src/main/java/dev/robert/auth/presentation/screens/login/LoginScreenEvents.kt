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
