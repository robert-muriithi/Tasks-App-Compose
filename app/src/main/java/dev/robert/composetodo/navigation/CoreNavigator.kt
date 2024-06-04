package dev.robert.composetodo.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.robert.design_system.theme.TodoTheme
import dev.robert.navigation.auth.LoginScreen
import dev.robert.navigation.auth.authNavGraph
import dev.robert.navigation.task.TasksNavGraph

@Composable
fun TodoCoreNavigator() {
    TodoTheme {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            NavHost(
                navController = navController,
                startDestination = LoginScreen,
            ) {
                authNavGraph(
                    onNavigate = {
                        navController.navigate(
                            TasksNavGraph,
                            navOptions =
                                NavOptions.Builder()
                                    .setPopUpTo(navController.graph.id, true)
                                    .build(),
                        )
                    },
                )
            }
        }
    }
}
