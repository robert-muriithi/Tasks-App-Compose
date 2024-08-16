package dev.robert.tasks.presentation.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import dev.robert.design_system.presentation.utils.scaleIntoContainer
import dev.robert.design_system.presentation.utils.scaleOutOfContainer
import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.presentation.screens.details.TaskDetailsScreen
import dev.robert.tasks.presentation.screens.tasks.TaskScreen
import dev.robert.tasks.presentation.screens.tasks.add.AddTaskScreen
import kotlin.reflect.KClass
import kotlin.reflect.typeOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
object TasksNavGraph

@Serializable
object TasksScreen

@Serializable
object AddTaskScreen

@Serializable
data class TodoItem(val item: TaskItem)

fun NavGraphBuilder.tasksNavGraph(
    onNavigateToDetails: (TaskItem) -> Unit,
    onNavigateToAddTask: () -> Unit,
    onNavigateUp: () -> Unit
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
                onNavigateToDetails = { _, _ -> },
                onNavigateToAddTask = onNavigateToAddTask,
            )
        }
        composable<TodoItem>(
            typeMap = mapOf(typeOf<TaskItem>() to todoItem)
        ) { backStackEntry ->
            val item: TodoItem = backStackEntry.toRoute()
            TaskDetailsScreen()
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
                onNavigateUp = onNavigateUp
            )
        }
    }
}

val todoItem = CustomNavTypeArgs.create(TaskItem::class, TaskItem.serializer())

object CustomNavTypeArgs {
    fun <T : Parcelable> create(kClass: KClass<T>, kSerializer: KSerializer<T>): NavType<T> {
        return object : NavType<T>(true) {
            override fun get(bundle: Bundle, key: String): T? {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelable(key, kClass.java)
                } else bundle.getParcelable(key)
            }

            override fun parseValue(value: String): T {
                return Json.decodeFromString(kSerializer, value)
            }

            override fun put(bundle: Bundle, key: String, value: T) {
                bundle.putParcelable(key, value)
            }

            override fun serializeAsValue(value: T): String {
                return Json.encodeToString(serializer = kSerializer, value = value)
            }
        }
    }
}
