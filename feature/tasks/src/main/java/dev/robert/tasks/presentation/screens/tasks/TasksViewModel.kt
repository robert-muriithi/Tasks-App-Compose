package dev.robert.tasks.presentation.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.tasks.domain.usecase.GetTasksUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksScreenState())
    val uiState = _uiState.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.update {
            it.copy(
                error = exception
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
            getTasksUseCase().collectLatest { tasks ->
                delay(1000)
                _uiState.update {
                    it.copy(
                        tasks = tasks,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onEvent(event: TaskScreenEvents) {
        when (event) {
            is TaskScreenEvents.LoadTasks -> getTasks()
            is TaskScreenEvents.RefreshTasks -> getTasks()
            is TaskScreenEvents.NavigateToDetails -> {
                // Navigate to details
            }
        }
    }
}
