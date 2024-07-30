package dev.robert.tasks.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import dev.robert.design_system.presentation.utils.scaleIntoContainer
import dev.robert.design_system.presentation.utils.scaleOutOfContainer
import dev.robert.tasks.presentation.screens.details.TaskDetailsScreen
import dev.robert.tasks.presentation.screens.tasks.TaskScreen
import dev.robert.tasks.presentation.screens.tasks.add.AddTaskScreen
import kotlinx.serialization.Serializable

@Serializable
object TasksNavGraph

@Serializable
object TasksScreen

@Serializable
object AddTaskScreen

@Serializable
data class TodoItem(val name: String,
    val age: Int)

fun NavGraphBuilder.tasksNavGraph(
    onNavigateToDetails: (String, Int) -> Unit,
    onNavigateToAddTask: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    navigation<TasksNavGraph>(
        startDestination = TasksScreen,
    ) {
        composable<TasksScreen>(
            enterTransition = {
                scaleIntoContainer()
            },
            exitTransition = {
                scaleOutOfContainer(
                    direction = AnimatedContentTransitionScope.SlideDirection.Left
                )
            },
            popEnterTransition = {
                scaleIntoContainer(
                    direction = AnimatedContentTransitionScope.SlideDirection.Right
                )
            },
            popExitTransition = {
                scaleOutOfContainer()
            },
        ) {
            TaskScreen(
                onNavigateToDetails = onNavigateToDetails,
                onNavigateToAddTask = onNavigateToAddTask,
            )
        }
        composable<TodoItem> { backStackEntry ->
            val item: TodoItem = backStackEntry.toRoute()
            TaskDetailsScreen(
                onNavigateBack = onNavigateUp,
                user = item.name,
            )
        }
        composable<AddTaskScreen>(
            enterTransition = {
                scaleIntoContainer(
                    direction = AnimatedContentTransitionScope.SlideDirection.Left
                )
            },
            exitTransition = {
                scaleOutOfContainer(
                    direction = AnimatedContentTransitionScope.SlideDirection.Left
                )
            },
            popEnterTransition = {
                scaleIntoContainer(
                    direction = AnimatedContentTransitionScope.SlideDirection.Right
                )
            },
            popExitTransition = {
                scaleOutOfContainer(
                    direction = AnimatedContentTransitionScope.SlideDirection.Right
                )
            }
        ) {
            AddTaskScreen(
                onNavigateBack = onNavigateUp
            )
        }
    }
}
