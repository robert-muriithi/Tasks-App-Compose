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
package dev.robert.database.data.categories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface CategoriesDao {
    @Upsert
    suspend fun saveCategory(entity: CategoryEntity)

    @Query("SELECT * FROM task_categories")
    fun getCategories(): List<CategoryEntity>

    @Query("DELETE FROM task_categories")
    suspend fun clear()

    @Query("UPDATE task_categories SET name =:name WHERE id =:id")
    suspend fun updateCategoryName(name: String, id: Int)

    @Insert
    suspend fun insertAll(categories: List<CategoryEntity>)
}
