package dev.robert.tasks.data.repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.robert.tasks.data.datasource.LocalDataSource
import dev.robert.tasks.data.mappers.toTodoItem
import dev.robert.tasks.data.mappers.toTodoModel
import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.domain.repository.TasksRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

@Singleton
class TasksRepositoryImpl @Inject constructor(
    private val dataSource: LocalDataSource,
    private val database: FirebaseFirestore,
    private val mAuth: FirebaseAuth,
) : TasksRepository {

    override val tasks: Flow<List<TaskItem>>
        get() = dataSource.tasks.map { list ->
            list.map {
                it.toTodoItem()
            }
        }

    override fun getTaskById(id: Int): Flow<TaskItem> {
        return dataSource.getTaskById(id).map {
            it.toTodoItem()
        }
    }

    override suspend fun saveTask(task: TaskItem): Result<Boolean> {
        return dataSource.saveTask(task.toTodoModel())
    }

    override suspend fun deleteTask(taskId: Int): Result<Boolean> {
        return try {
            dataSource.deleteTask(taskId)
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
                        dataSource.setSynced(taskId)
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
            dataSource.completeTask(taskId)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
