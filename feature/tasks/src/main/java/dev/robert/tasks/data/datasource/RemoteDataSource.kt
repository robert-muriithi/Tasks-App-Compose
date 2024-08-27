package dev.robert.tasks.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.robert.tasks.data.model.TodoModel
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

interface RemoteDataSource {
    suspend fun getTasks(): List<TodoModel>
}

class RemoteDataSourceImpl @Inject constructor(
    private val mAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : RemoteDataSource {

    override suspend fun getTasks(): List<TodoModel> {
        val tasks = firestore.collection("tasks")
            .document(mAuth.currentUser!!.uid)
            .collection("user_tasks")
            .get()
            .await()
            .documents
            .map { it.toObject(TodoModel::class.java)!! }
        return tasks
    }
}
