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
package dev.robert.tasks.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import dev.robert.datastore.data.TodoAppPreferences
import dev.robert.tasks.data.datasource.LocalDataSource
import dev.robert.tasks.data.datasource.RemoteDataSource
import dev.robert.tasks.data.mappers.toTodoItem
import dev.robert.tasks.data.mappers.toTodoModel
import dev.robert.tasks.data.utils.ConstUtils.TASKS_COLLECTION
import dev.robert.tasks.data.utils.ConstUtils.TASKS_COLLECTION_PATH
import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.domain.repository.TasksRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

@Singleton
class TasksRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val database: FirebaseFirestore,
    private val preferences: TodoAppPreferences
) : TasksRepository {

    override val tasks: (fetchRemote: Boolean) -> Flow<List<TaskItem>>
        get() = { fetchRemote ->
            flow {
                val uid = preferences.userData.firstOrNull()?.id
                if (uid.isNullOrEmpty()) {
                    emit(emptyList())
                    return@flow
                }
                if (fetchRemote) {
                    val remoteTasks = remoteDataSource.getTasks(uid).map { it.toTodoItem() }
                    localDataSource.clear()
                    remoteTasks.forEach { saveTask(it) }
                }
                emitAll(localDataSource.tasks.map { list -> list.map { it.toTodoItem() } })
            }
        }

    override val task: (taskId: Int) -> Flow<TaskItem>
        get() = { taskId ->
            localDataSource.getTaskById(taskId).map {
                it.toTodoItem()
            }
        }

    override val searchTasks: (query: String) -> Flow<List<TaskItem>>
        get() = { query ->
            localDataSource.tasks.map { list ->
                list.filter { it.name.contains(query, ignoreCase = true) }
                    .map { it.toTodoItem() }
            }
        }

    override suspend fun saveTask(task: TaskItem): Result<Boolean> {
        return localDataSource.saveTask(task.toTodoModel())
    }

    override suspend fun deleteTask(taskId: Int): Result<Boolean> {
        return try {
            val uid = preferences.userData.firstOrNull()?.id
                ?: return Result.failure(Exception("User not authenticated"))
            localDataSource.deleteTask(taskId)
            remoteDataSource.deleteTask(uid, taskId)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadTask(task: TaskItem): Result<Boolean> {
        return try {
            val uid = preferences.userData.firstOrNull()?.id
                ?: return Result.failure(Exception("User not authenticated"))
            uid.let { userId ->
                task.id?.let { taskId ->
                    with(task) {
                        database.collection(TASKS_COLLECTION)
                            .document(userId)
                            .collection(TASKS_COLLECTION_PATH)
                            .document(taskId.toString())
                            .set(this.toTodoModel())
                            .await()
                        localDataSource.setSynced(taskId)
                    }
                }
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun completeTask(taskId: Int, completionDate: String): Result<Boolean> {
        return try {
            localDataSource.completeTask(taskId = taskId, completionDate = completionDate)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
