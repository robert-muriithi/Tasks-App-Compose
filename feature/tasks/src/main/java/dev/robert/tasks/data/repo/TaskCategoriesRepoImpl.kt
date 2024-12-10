package dev.robert.tasks.data.repo

import dev.robert.tasks.data.datasource.CategoriesLocalDataSource
import dev.robert.tasks.domain.model.TaskCategory
import dev.robert.tasks.domain.repository.TaskCategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TaskCategoriesRepoImpl @Inject constructor(
    private val categoriesDataSource: CategoriesLocalDataSource
) : TaskCategoriesRepository {

    override val categories: Flow<List<TaskCategory>>
        get() = flow {
            emitAll(categoriesDataSource.categories)
        }

    override suspend fun saveCategory(category: TaskCategory): Result<Boolean> =
        categoriesDataSource.saveCategory(category = category)


    override suspend fun clear() = categoriesDataSource.clear()

    override suspend fun updateCategoryName(name: String, id: Int): Result<Boolean> =
        categoriesDataSource.updateCategoryName(name = name, id = id)
}