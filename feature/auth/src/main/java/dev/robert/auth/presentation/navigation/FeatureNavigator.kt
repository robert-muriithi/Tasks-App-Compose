package dev.robert.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import dev.robert.auth.presentation.screens.login.LoginScreen
import dev.robert.auth.presentation.screens.register.RegisterScreen
import kotlinx.serialization.Serializable

@Serializable
object AuthNavGraph

fun NavGraphBuilder.authNavGraph(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    navigation<AuthNavGraph>(
        startDestination = RegisterScreen,
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
    onNavigateUp: () -> Unit,
) {
    composable<RegisterScreen> {
        RegisterScreen(
            onNavigateUp = onNavigateUp,
        )
    }
}
