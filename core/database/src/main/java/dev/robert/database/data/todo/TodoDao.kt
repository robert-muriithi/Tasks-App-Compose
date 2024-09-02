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
}
