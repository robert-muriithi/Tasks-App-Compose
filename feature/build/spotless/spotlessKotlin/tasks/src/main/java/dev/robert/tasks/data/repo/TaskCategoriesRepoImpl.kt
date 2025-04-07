/*
 * Copyright 2025 Robert Muriithi.
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
package dev.robert.tasks.data.repo

import dev.robert.tasks.data.datasource.CategoriesLocalDataSource
import dev.robert.tasks.domain.model.TaskCategory
import dev.robert.tasks.domain.repository.TaskCategoriesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

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
