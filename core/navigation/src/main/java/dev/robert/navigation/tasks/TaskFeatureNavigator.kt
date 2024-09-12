/*
 * Copyright 2024 Robert Muriithi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
data class TaskDetails(val item: TaskItem)

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
        composable<TaskDetails>(
            typeMap = mapOf(typeOf<TaskItem>() to todoItem)
        ) { backStackEntry ->
            val item: TaskDetails = backStackEntry.toRoute()
            TaskDetailsScreen(
                taskItem = item.item,
                onNavigateUp = onNavigateUp
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
