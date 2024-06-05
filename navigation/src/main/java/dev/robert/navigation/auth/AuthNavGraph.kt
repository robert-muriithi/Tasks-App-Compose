package dev.robert.navigation.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import dev.robert.auth.presentation.navigation.LoginScreen
import dev.robert.auth.presentation.navigation.RegisterScreen
import dev.robert.auth.presentation.navigation.loginScreen
import dev.robert.auth.presentation.navigation.registerScreen
import dev.robert.navigation.task.TasksNavGraph
import kotlinx.serialization.Serializable

@Serializable
object AuthNavGraph

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation<AuthNavGraph>(
        startDestination = LoginScreen,
    ) {
        loginScreen(
            onNavigateToHome = {
                navController.navigate(
                    TasksNavGraph,
                    navOptions =
                        NavOptions.Builder()
                            .setPopUpTo(navController.graph.id, true)
                            .build(),
                )
            },
            onNavigateToRegister = {
                navController.navigate(RegisterScreen)
            },
        )
        registerScreen(
            onNavigate = {
                navController.navigate(
                    TasksNavGraph,
                    navOptions =
                        NavOptions.Builder()
                            .setPopUpTo(navController.graph.id, true)
                            .build(),
                )
            },
            onNavigateUp = {
                navController.navigateUp()
            },
        )
    }
}
