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
package dev.robert.tasks.domain.repository

import dev.robert.tasks.domain.model.TaskItem
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    val task: (taskId: Int) -> Flow<TaskItem>
    val tasks: (fetchRemote: Boolean) -> Flow<List<TaskItem>>
    val updateTask: (task: TaskItem) -> Flow<Boolean>
    suspend fun saveTask(task: TaskItem): Result<Boolean>
    suspend fun deleteTask(taskId: Int): Result<Boolean>
    suspend fun uploadTask(task: TaskItem): Result<Boolean>
    suspend fun completeTask(taskId: Int, completionDate: String): Result<Boolean>
    val searchTasks: (query: String) -> Flow<List<TaskItem>>
}
