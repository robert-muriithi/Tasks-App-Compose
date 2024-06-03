package dev.robert.database.data

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

    @Query("UPDATE todos SET isComplete =:isComplete WHERE id =:id")
    suspend fun setComplete(
        id: Int,
        isComplete: Boolean,
    )

    @Query("UPDATE todos SET isSynced =:isSynced WHERE id =:id")
    suspend fun setSynced(
        id: Int,
        isSynced: Boolean,
    )

    @Query("DELETE FROM todos")
    suspend fun clear()
}
