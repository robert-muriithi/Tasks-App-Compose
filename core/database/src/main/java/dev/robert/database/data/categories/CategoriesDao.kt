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