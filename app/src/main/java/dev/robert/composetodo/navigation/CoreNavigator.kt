package dev.robert.composetodo.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.robert.design_system.presentation.theme.TodoTheme
import dev.robert.navigation.auth.AuthNavGraph
import dev.robert.navigation.auth.authNavGraph
import dev.robert.navigation.task.tasksNavGraph

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
                startDestination = AuthNavGraph,
            ) {
                authNavGraph(navController = navController)
                tasksNavGraph(navController = navController)
            }
        }
    }
}
