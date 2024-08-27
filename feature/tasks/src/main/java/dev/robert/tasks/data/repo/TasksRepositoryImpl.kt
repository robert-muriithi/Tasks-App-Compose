package dev.robert.tasks.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.robert.tasks.data.datasource.LocalDataSource
import dev.robert.tasks.data.datasource.RemoteDataSource
import dev.robert.tasks.data.mappers.toTodoItem
import dev.robert.tasks.data.mappers.toTodoModel
import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.domain.repository.TasksRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

@Singleton
class TasksRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val database: FirebaseFirestore,
    private val mAuth: FirebaseAuth,
) : TasksRepository {

    override val tasks: Flow<List<TaskItem>>
        get() = flow {
            val taskList = remoteDataSource.getTasks()
            localDataSource.clear()
            taskList.forEach {
                saveTask(it.toTodoItem())
            }
            val localTaskList = localDataSource.tasks.map { localTasks ->
                localTasks.map {
                    it.toTodoItem()
                }
            }
            emitAll(localTaskList)
        }

    override fun getTasks(fetchRemote: Boolean): Flow<List<TaskItem>> = flow {
        if (fetchRemote) {
            val remoteTasks = remoteDataSource.getTasks().map { it.toTodoItem() }
            localDataSource.clear()
            remoteTasks.forEach { saveTask(it) }
        }
        emitAll(localDataSource.tasks.map { list -> list.map { it.toTodoItem() } })
    }

    override fun getTaskById(id: Int): Flow<TaskItem> {
        return localDataSource.getTaskById(id).map {
            it.toTodoItem()
        }
    }

    override suspend fun saveTask(task: TaskItem): Result<Boolean> {
        return localDataSource.saveTask(task.toTodoModel())
    }

    override suspend fun deleteTask(taskId: Int): Result<Boolean> {
        return try {
            localDataSource.deleteTask(taskId)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadTask(task: TaskItem): Result<Boolean> {
        return try {
            mAuth.currentUser?.uid?.let { userId ->
                task.id?.let { taskId ->
                    with(task) {
                        database.collection("tasks")
                            .document(userId)
                            .collection("user_tasks")
                            .document(taskId.toString())
                            .set(this.toTodoModel())
                            .await()
                        localDataSource.setSynced(taskId)
                    }
                }
                Result.success(true)
            } ?: Result.failure(Exception("User not authenticated"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun completeTask(taskId: Int): Result<Boolean> {
        return try {
            localDataSource.completeTask(taskId)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
