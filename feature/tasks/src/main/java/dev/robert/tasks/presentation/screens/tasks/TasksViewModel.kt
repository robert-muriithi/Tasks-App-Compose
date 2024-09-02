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
package dev.robert.tasks.presentation.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.datastore.data.TodoAppPreferences
import dev.robert.design_system.presentation.utils.getCurrentDateTime
import dev.robert.tasks.domain.model.TaskCategory
import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.domain.usecase.CompleteTaskUseCase
import dev.robert.tasks.domain.usecase.DeleteTaskUseCase
import dev.robert.tasks.domain.usecase.GetTasksUseCase
import dev.robert.tasks.domain.usecase.UploadTaskToServerUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val prefs: TodoAppPreferences,
    private val uploadTaskToServerUseCase: UploadTaskToServerUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksScreenState())
    val uiState = _uiState.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.update {
            it.copy(
                error = exception,
                isLoading = false
            )
        }
    }

    fun onEvent(event: TaskScreenEvents) {
        when (event) {
            is TaskScreenEvents.LoadTasks -> getTasks(event.fetchRemote)
            is TaskScreenEvents.FilterTasks -> filterTask(event.filterString)
            is TaskScreenEvents.ToggleGrid -> saveToPrefs(event.isGrid)
            is TaskScreenEvents.SyncTask -> syncTaskToServer(event.task)
            is TaskScreenEvents.CompleteTask -> completeTask(event.task)
            is TaskScreenEvents.DeleteTask -> deleteTask(event.task)
            is TaskScreenEvents.RefreshTasks -> getTasks(event.fetchRemote, event.refresh)
        }
    }

    private fun getTasks(fetchRemote: Boolean, refresh: Boolean = false) {
        _uiState.update {
            it.copy(
                isLoading = !refresh,
                isRefreshing = refresh
            )
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            getIsGrid()
            getTasksUseCase(fetchRemote).collectLatest { tasks ->
                _uiState.update { state ->
                    state.copy(
                        tasks = tasks,
                        isLoading = false,
                        isRefreshing = false,
                        refreshed = refresh,
                        selectedCategory = TaskCategory("All"),
                        category = tasks.map { it.category }
                            .distinct()
                            .toMutableList()
                            .apply { add(0, TaskCategory("All")) }
                            .filterNotNull(),
                        analytics = state.analytics.copy(
                            totalTasks = tasks.size,
                            completedTasks = tasks.count { it.complete },
                            todaysCompleteTasks = tasks.count { it.complete && it.completionDate == getCurrentDateTime().toString() },
                            completionPercentage = if (tasks.isNotEmpty()) (tasks.count { it.complete }.toFloat() / tasks.size.toFloat()) * 100 else 0f
                        )
                    )
                }
            }
        }
    }

    private fun syncTaskToServer(task: TaskItem) {
        _uiState.update {
            it.copy(
                syncing = true
            )
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = uploadTaskToServerUseCase(task.copy(synced = true))
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        syncing = false,
                        isSynced = true
                    )
                }
            }
        }
    }

    private fun filterTask(filterString: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            getTasksUseCase(false).collectLatest { tasks ->
                _uiState.update { state ->
                    state.copy(
                        tasks = if (filterString == "All") tasks
                        else tasks.filter { category ->
                            filterString.let { categoryName ->
                                category.category?.name?.contains(
                                    categoryName,
                                    ignoreCase = true
                                )
                            } == true
                        },
                        selectedCategory = state.category.find { it?.name == filterString }
                    )
                }
            }
        }
    }

    private fun saveToPrefs(isGrid: Boolean) {
        viewModelScope.launch {
            prefs.saveGridView(isGrid)
            _uiState.update {
                it.copy(
                    isGridView = isGrid
                )
            }
        }
    }

    private fun getIsGrid() {
        viewModelScope.launch {
            prefs.isGridView.collectLatest { isGrid ->
                _uiState.update {
                    it.copy(
                        isGridView = isGrid
                    )
                }
            }
        }
    }

    private fun completeTask(task: TaskItem) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = task.id?.let {
                completeTaskUseCase(it, getCurrentDateTime().toString())
            }
            if (result?.isSuccess == true) {
                _uiState.update {
                    it.copy(
                        tasks = it.tasks.map { taskItem ->
                            if (taskItem.id == task.id && !taskItem.complete) {
                                taskItem.copy(
                                    complete = true,
                                    completionDate = getCurrentDateTime().toString()
                                )
                            } else {
                                taskItem
                            }
                        }
                    )
                }
                syncTaskToServer(task.copy(complete = true))
            }
        }
    }

    private fun deleteTask(task: TaskItem) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = task.id?.let {
                deleteTaskUseCase(it)
            }
            if (result?.isSuccess == true) {
                _uiState.update {
                    it.copy(
                        tasks = it.tasks.filter { taskItem ->
                            taskItem.id != task.id
                        }
                    )
                }
            }
        }
    }
    private fun resetState() {
        _uiState.update { TasksScreenState() }
    }
}
