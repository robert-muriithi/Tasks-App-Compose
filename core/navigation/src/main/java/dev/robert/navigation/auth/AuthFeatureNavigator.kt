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
package dev.robert.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import dev.robert.auth.domain.model.GoogleUser
import dev.robert.auth.presentation.screens.login.LoginScreen
import dev.robert.auth.presentation.screens.register.RegisterScreen
import kotlinx.serialization.Serializable

@Serializable
object AuthNavGraph

fun NavGraphBuilder.authNavGraph(
    onNavigateToHome: (GoogleUser) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    navigation<AuthNavGraph>(
        startDestination = LoginScreen,
    ) {
        loginScreen(
            onNavigateToHome = onNavigateToHome,
            onNavigateToRegister = onNavigateToRegister,
        )
        registerScreen(
            onNavigateUp = onNavigateUp
        )
    }
}
@Serializable
object LoginScreen

fun NavGraphBuilder.loginScreen(
    onNavigateToHome: (GoogleUser) -> Unit,
    onNavigateToRegister: () -> Unit,
) {
    composable<LoginScreen> {
        LoginScreen(
            onNavigateToHome = onNavigateToHome,
            onNavigateToRegister = onNavigateToRegister,
        )
    }
}

@Serializable
object RegisterScreen

fun NavGraphBuilder.registerScreen(
    onNavigateUp: () -> Unit,
) {
    composable<RegisterScreen> {
        RegisterScreen(
            onNavigateUp = onNavigateUp,
        )
    }
}
