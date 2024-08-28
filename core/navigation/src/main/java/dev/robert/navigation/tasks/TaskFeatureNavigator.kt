package dev.robert.navigation.tasks

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import dev.robert.navigation.navtype.CustomNavTypeArgs
import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.presentation.screens.details.TaskDetailsScreen
import dev.robert.tasks.presentation.screens.search.SearchScreen
import dev.robert.tasks.presentation.screens.tasks.TaskScreen
import dev.robert.tasks.presentation.screens.tasks.add.AddTaskScreen
import kotlin.reflect.typeOf
import kotlinx.serialization.Serializable

@Serializable
object TasksNavGraph

@Serializable
object TasksScreen

@Serializable
object AddTaskScreen

@Serializable
data class Task(val item: TaskItem)

@Serializable
object SearchScreen

fun NavGraphBuilder.tasksNavGraph(
    onNavigateToDetails: (TaskItem) -> Unit,
    onNavigateUp: () -> Unit,
) {
    navigation<TasksNavGraph>(
        startDestination = TasksScreen,
    ) {
        composable<TasksScreen> {
            TaskScreen(
                onNavigateToDetails = onNavigateToDetails,
            )
        }
        composable<Task>(
            typeMap = mapOf(typeOf<TaskItem>() to todoItem)
        ) { backStackEntry ->
            val item: Task = backStackEntry.toRoute()
            TaskDetailsScreen(
                taskItem = item.item,
            )
        }
        composable<AddTaskScreen> {
            AddTaskScreen(
                onNavigateUp = onNavigateUp
            )
        }
        composable<SearchScreen> {
            SearchScreen(
                onNavigateUp = onNavigateUp
            )
        }
    }
}

val todoItem = CustomNavTypeArgs.create(TaskItem::class, TaskItem.serializer())
