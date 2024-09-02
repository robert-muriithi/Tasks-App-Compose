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
