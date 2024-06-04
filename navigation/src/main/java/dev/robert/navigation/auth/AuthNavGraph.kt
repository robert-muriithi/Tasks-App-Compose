package dev.robert.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.robert.auth.presentation.screens.login.LoginScreen
import kotlinx.serialization.Serializable

@Serializable
object LoginScreen

fun NavGraphBuilder.authNavGraph(onNavigate: () -> Unit) {
    composable<LoginScreen> {
        LoginScreen(
            onNavigate = onNavigate,
        )
    }
}
