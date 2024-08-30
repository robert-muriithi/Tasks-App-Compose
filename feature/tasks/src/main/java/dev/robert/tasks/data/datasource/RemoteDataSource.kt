package dev.robert.tasks.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import dev.robert.tasks.data.model.TodoModel
import dev.robert.tasks.data.utils.ConstUtils.TASKS_COLLECTION
import dev.robert.tasks.data.utils.ConstUtils.TASKS_COLLECTION_PATH
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

interface RemoteDataSource {
    suspend fun getTasks(userId: String): List<TodoModel>
    suspend fun deleteTask(userId: String, taskId: Int): Result<Boolean>
}

class RemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RemoteDataSource {

    override suspend fun getTasks(userId: String): List<TodoModel> {
        val tasks = firestore.collection(TASKS_COLLECTION)
            .document(userId)
            .collection(TASKS_COLLECTION_PATH)
            .get()
            .await()
            .documents
            .map { it.toObject(TodoModel::class.java)!! }
        return tasks
    }

    override suspend fun deleteTask(userId: String, taskId: Int): Result<Boolean> {
        return try {
            firestore.collection(TASKS_COLLECTION)
                .document(userId)
                .collection(TASKS_COLLECTION_PATH)
                .document(taskId.toString())
                .delete()
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
