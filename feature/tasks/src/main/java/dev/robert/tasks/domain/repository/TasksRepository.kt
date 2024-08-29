package dev.robert.tasks.domain.repository

import dev.robert.tasks.domain.model.TaskItem
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun getTaskById(id: Int): Flow<TaskItem>
    suspend fun saveTask(task: TaskItem): Result<Boolean>
    suspend fun deleteTask(taskId: Int): Result<Boolean>
    suspend fun uploadTask(task: TaskItem): Result<Boolean>
    suspend fun completeTask(taskId: Int): Result<Boolean>
    val tasks: (fetchRemote: Boolean) -> Flow<List<TaskItem>>
}
