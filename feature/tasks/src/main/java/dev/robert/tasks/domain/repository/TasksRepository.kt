package dev.robert.tasks.domain.repository

import dev.robert.tasks.domain.model.TaskItem
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    val tasks: Flow<List<TaskItem>>
    fun getTaskById(id: Int): Flow<TaskItem>
    suspend fun saveTask(task: TaskItem)
    suspend fun deleteTask(taskId: Int)
}