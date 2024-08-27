package dev.robert.tasks.domain.repository

import dev.robert.tasks.domain.model.TaskItem
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    val tasks: Flow<List<TaskItem>>
    fun getTasks(fetchRemote: Boolean): Flow<List<TaskItem>>
    fun getTaskById(id: Int): Flow<TaskItem>
    suspend fun saveTask(task: TaskItem): Result<Boolean>
    suspend fun deleteTask(taskId: Int): Result<Boolean>
    suspend fun uploadTask(task: TaskItem): Result<Boolean>
    suspend fun completeTask(taskId: Int): Result<Boolean>
}
