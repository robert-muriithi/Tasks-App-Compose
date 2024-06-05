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
object TasksScreen

@Serializable data class TodoItem(val name: String)

fun NavGraphBuilder.tasksScreen(onNavigateToDetails: () -> Unit) {
    composable<TasksScreen> {
        TaskScreen(
            onNavigateToDetails = onNavigateToDetails,
        )
    }
}

fun NavGraphBuilder.taskDetailsScreen(
    navController: NavController,
    onNavigateBack: () -> Unit,
) {
    composable<TodoItem> { backStackEntry ->
        val item: TodoItem = backStackEntry.toRoute()
        TaskDetailsScreen(
            navController = navController,
            onNavigateBack = onNavigateBack,
            item = item,
        )
    }
}
