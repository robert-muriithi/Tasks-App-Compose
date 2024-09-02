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
