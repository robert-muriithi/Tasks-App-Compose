package dev.robert.tasks.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import dev.robert.tasks.presentation.screens.TaskDetailsScreen
import dev.robert.tasks.presentation.screens.TaskScreen
import kotlinx.serialization.Serializable

@Serializable
object TasksNavGraph

@Serializable
object TasksScreen

@Serializable
data class TodoItem(val name: String,
    val age: Int)

fun NavGraphBuilder.tasksNavGraph(navController: NavController) {
    navigation<TasksNavGraph>(
        startDestination = TasksScreen,
    ) {
        composable<TasksScreen> {
            TaskScreen(
                onNavigateToDetails = { name, age ->
                    navController.navigate(route = TodoItem(name = name, age = age))
                },
            )
        }
        composable<TodoItem> { backStackEntry ->
            val item: TodoItem = backStackEntry.toRoute()
            TaskDetailsScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                user = item.name,
            )
        }
    }
}
