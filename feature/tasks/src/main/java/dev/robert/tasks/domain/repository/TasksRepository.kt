package dev.robert.tasks.domain.repository

import dev.robert.tasks.domain.model.TaskItem
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    val task: (taskId: Int) -> Flow<TaskItem>
    val tasks: (fetchRemote: Boolean) -> Flow<List<TaskItem>>
    suspend fun saveTask(task: TaskItem): Result<Boolean>
    suspend fun deleteTask(taskId: Int): Result<Boolean>
    suspend fun uploadTask(task: TaskItem): Result<Boolean>
    suspend fun completeTask(taskId: Int, completionDate: String): Result<Boolean>
    val searchTasks: (query: String) -> Flow<List<TaskItem>>
}
