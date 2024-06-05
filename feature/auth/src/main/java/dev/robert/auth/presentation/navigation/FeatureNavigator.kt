package dev.robert.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.robert.auth.presentation.screens.login.LoginScreen
import dev.robert.auth.presentation.screens.register.RegisterScreen
import kotlinx.serialization.Serializable

@Serializable
object LoginScreen

fun NavGraphBuilder.loginScreen(
    onNavigateToHome: () -> Unit,
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
    onNavigate: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    composable<RegisterScreen> {
        RegisterScreen(
            onNavigateUp = onNavigateUp,
            onNavigate = onNavigate,
        )
    }
}
