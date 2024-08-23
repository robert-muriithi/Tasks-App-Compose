package dev.robert.tasks.presentation.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.datastore.data.TodoAppPreferences
import dev.robert.tasks.domain.model.TaskCategory
import dev.robert.tasks.domain.usecase.GetTasksUseCase
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

    fun onEvent(event: TaskScreenEvents) {
        when (event) {
            is TaskScreenEvents.LoadTasks -> getTasks()
            is TaskScreenEvents.FilterTasks -> filterTask(event.filterString)
            is TaskScreenEvents.ToggleGrid -> saveToPrefs(event.isGrid)
            is TaskScreenEvents.NavigateToDetails -> {
                // Navigate to details
            }
        }
    }
}
