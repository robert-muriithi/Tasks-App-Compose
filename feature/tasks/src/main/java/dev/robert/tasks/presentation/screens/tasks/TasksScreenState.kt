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
package dev.robert.tasks.presentation.screens.tasks

import dev.robert.tasks.domain.model.TaskCategory
import dev.robert.tasks.domain.model.TaskItem

data class TasksScreenState(
    val tasks: List<TaskItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val category: List<TaskCategory?> = emptyList(),
    val update: Boolean = false,
    val isGridView: Boolean = true,
    val isSynced: Boolean = false,
    val syncing: Boolean = false,
    val isRefreshing: Boolean = false,
    val refreshed: Boolean = false,
    val analytics: Analytics = Analytics(),
    val selectedCategory: TaskCategory? = null,
)

data class Analytics(
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val todaysCompleteTasks: Int = 0,
    val completionPercentage: Float = 0f
)
