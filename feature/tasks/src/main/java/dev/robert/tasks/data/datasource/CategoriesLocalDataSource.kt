package dev.robert.tasks.data.datasource

import dev.robert.database.data.categories.CategoriesDao
import dev.robert.tasks.data.mappers.toDomain
import dev.robert.tasks.data.mappers.toEntity
import dev.robert.tasks.domain.model.TaskCategory
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface CategoriesLocalDataSource {
    val categories: Flow<List<TaskCategory>>
    suspend fun saveCategory(category: TaskCategory): Result<Boolean>
    suspend fun clear()
    suspend fun updateCategoryName(name: String, id: Int): Result<Boolean>
}

class CategoriesLocalDataSourceImpl @Inject constructor(
    private val categoriesDao: CategoriesDao
) : CategoriesLocalDataSource {

    override val categories: Flow<List<TaskCategory>> = flow {
        emit(categoriesDao.getCategories().map {
            it.toDomain()
        }
        )
    }.flowOn(Dispatchers.IO).catch { e -> throw e }

    override suspend fun saveCategory(category: TaskCategory): Result<Boolean> {
        return try {
            categoriesDao.saveCategory(category.toEntity())
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun clear() {
        categoriesDao.clear()
    }

    override suspend fun updateCategoryName(name: String, id: Int): Result<Boolean> {
        return try {
            categoriesDao.updateCategoryName(name, id)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
