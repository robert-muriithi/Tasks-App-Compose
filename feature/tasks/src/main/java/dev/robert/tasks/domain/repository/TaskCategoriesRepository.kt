package dev.robert.tasks.domain.repository

import dev.robert.tasks.domain.model.TaskCategory
import kotlinx.coroutines.flow.Flow

interface TaskCategoriesRepository {
    val categories: Flow<List<TaskCategory>>
    suspend fun saveCategory(category: TaskCategory): Result<Boolean>
    suspend fun clear()
    suspend fun updateCategoryName(name: String, id: Int): Result<Boolean>
}
