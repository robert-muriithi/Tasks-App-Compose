package dev.robert.composetodo.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.robert.auth.presentation.navigation.AuthNavGraph
import dev.robert.auth.presentation.navigation.authNavGraph
import dev.robert.design_system.presentation.theme.TodoTheme
import dev.robert.onboarding.presentation.navigation.OnBoardingNavGraph
import dev.robert.onboarding.presentation.navigation.onBoardingNavGraph
import dev.robert.tasks.presentation.navigation.TasksNavGraph
import dev.robert.tasks.presentation.navigation.tasksNavGraph

@Composable
fun TodoCoreNavigator(theme: Int) {
    TodoTheme(
        theme = theme,
    ) {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            NavHost(
                navController = navController,
                startDestination = OnBoardingNavGraph,
            ) {
                onBoardingNavGraph(
                    onCompleteOnBoarding = {
                        navController.navigate(
                            AuthNavGraph,
                            navOptions = NavOptions.Builder()
                                .setPopUpTo(navController.graph.id, true)
                                .build(),
                        )
                    }
                )
                authNavGraph(
                    onNavigateToHome = {
                        navController.navigate(
                            TasksNavGraph,
                            navOptions =
                            NavOptions.Builder()
                                .setPopUpTo(navController.graph.id, true)
                                .build(),
                        ) },
                    onNavigateToRegister = {
                    },
                    onNavigateUp = {
                        navController.navigateUp()
                    }
                )
                tasksNavGraph(navController = navController)
            }
        }
    }
}
