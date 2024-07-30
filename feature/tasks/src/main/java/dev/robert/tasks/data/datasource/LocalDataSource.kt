package dev.robert.tasks.data.datasource

import dev.robert.database.data.todo.TodoDao
import dev.robert.tasks.data.mappers.toEntity
import dev.robert.tasks.data.mappers.toTodoModel
import dev.robert.tasks.data.model.TodoModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface LocalDataSource {
    val tasks: Flow<List<TodoModel>>
    fun getTaskById(id: Int): Flow<TodoModel>
    suspend fun saveTask(task: TodoModel)
    suspend fun deleteTask(taskId: Int)
}

class LocalDataStoreImpl @Inject constructor(
    private val taskDao: TodoDao
) : LocalDataSource {

    override val tasks: Flow<List<TodoModel>>
        get() = try {
            taskDao.getTodos().map { list ->
                list.map { it.toTodoModel() }
            }
        } catch (e: Exception) {
            throw e
        }

    override fun getTaskById(id: Int): Flow<TodoModel> =
        try {
            taskDao.getTodoBtId(id).map { it.toTodoModel() }
        } catch (e: Exception) {
            throw e
        }

    override suspend fun saveTask(task: TodoModel) =
        try {
            taskDao.saveTodo(task.toEntity())
        } catch (e: Exception) {
            throw e
        }

    override suspend fun deleteTask(taskId: Int) =
        try {
            taskDao.deleteTask(taskId)
        } catch (e: Exception) {
            throw e
        }
}
