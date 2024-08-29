package dev.robert.tasks.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import dev.robert.tasks.data.model.TodoModel
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

interface RemoteDataSource {
    suspend fun getTasks(userId: String): List<TodoModel>
}

class RemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RemoteDataSource {

    override suspend fun getTasks(userId: String): List<TodoModel> {
        val tasks = firestore.collection("tasks")
            .document(userId)
            .collection("user_tasks")
            .get()
            .await()
            .documents
            .map { it.toObject(TodoModel::class.java)!! }
        return tasks
    }
}
