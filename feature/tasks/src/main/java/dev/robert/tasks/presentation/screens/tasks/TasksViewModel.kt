package dev.robert.tasks.presentation.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.datastore.data.TodoAppPreferences
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
            is TaskScreenEvents.LoadTasks -> getTasks()
            is TaskScreenEvents.FilterTasks -> filterTask(event.filterString)
            is TaskScreenEvents.ToggleGrid -> saveToPrefs(event.isGrid)
            is TaskScreenEvents.SyncTask -> syncTaskToServer(event.task)
            is TaskScreenEvents.CompleteTask -> completeTask(event.task)
            is TaskScreenEvents.DeleteTask -> deleteTask(event.task)
        }
    }

    private fun getTasks() {
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            getIsGrid()
            getTasksUseCase().collectLatest { tasks ->
                _uiState.update { state ->
                    state.copy(
                        tasks = tasks,
                        isLoading = false,
                        category = tasks.map { it.category }
                            .distinct()
                            .toMutableList()
                            .apply { add(0, TaskCategory("All")) }
                            .filterNotNull()
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
            val result = uploadTaskToServerUseCase(task.copy(isSynced = true))
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
            getTasksUseCase().collectLatest { tasks ->
                _uiState.update { state ->
                    state.copy(
                        tasks = if (filterString == "All") tasks
                        else tasks.filter { category -> filterString.let { categoryName ->
                            category.category?.name?.contains(
                                categoryName,
                                ignoreCase = true
                            )
                        } == true },
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
                completeTaskUseCase(it)
            }
            if (result?.isSuccess == true) {
                _uiState.update {
                    it.copy(
                        tasks = it.tasks.map { taskItem ->
                            if (taskItem.id == task.id && !taskItem.isComplete) {
                                taskItem.copy(isComplete = true)
                            } else {
                                taskItem
                            }
                        }
                    )
                }
                syncTaskToServer(task.copy(isComplete = true))
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
}
