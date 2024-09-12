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
package dev.robert.database.data.todo

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Upsert
    suspend fun saveTodo(entity: TodoEntity)

    @Query("SELECT * FROM todos")
    fun getTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE id =:id")
    fun getTodoBtId(id: Int): Flow<TodoEntity>

    @Query(
        "UPDATE todos SET " +
            "name =:name, " +
            "description =:description, " +
            "startDateTime =:startDateTime " +
            "WHERE id =:id",
    )
    suspend fun updateTodoName(
        name: String? = "",
        description: String? = "",
        startDateTime: String? = "",
        id: Int,
    )

    @Query("UPDATE todos SET complete =:isComplete WHERE id =:id")
    suspend fun setComplete(
        id: Int,
        isComplete: Boolean,
    )

    @Query("UPDATE todos SET synced = 1 WHERE id =:id")
    suspend fun setSynced(id: Int)

    @Query("DELETE FROM todos")
    suspend fun clear()

    @Query("DELETE FROM todos WHERE id =:id")
    suspend fun deleteTask(id: Int)

    @Query("UPDATE todos SET complete = 1, completionDate = :completionDate WHERE id =:id")
    suspend fun completeTask(id: Int, completionDate: String)

    @Upsert
    suspend fun updateTask(entity: TodoEntity)
}
