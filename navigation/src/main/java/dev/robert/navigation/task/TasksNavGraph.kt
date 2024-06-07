package dev.robert.navigation.task

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import dev.robert.tasks.presentation.navigation.TasksScreen
import dev.robert.tasks.presentation.navigation.TodoItem
import dev.robert.tasks.presentation.navigation.taskDetailsScreen
import dev.robert.tasks.presentation.navigation.tasksScreen
import kotlinx.serialization.Serializable

@Serializable
object TasksNavGraph

fun NavGraphBuilder.tasksNavGraph(navController: NavController) {
    navigation<TasksNavGraph>(
        startDestination = TasksScreen,
    ) {
        tasksScreen(
            onNavigateToDetails = {
                navController.navigate(route = TodoItem)
            },
        )

        taskDetailsScreen(
            navController = navController,
            onNavigateBack = {
                navController.navigateUp()
            },
        )
    }
}
