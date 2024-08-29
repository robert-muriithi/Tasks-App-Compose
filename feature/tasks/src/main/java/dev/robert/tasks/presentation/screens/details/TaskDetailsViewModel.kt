package dev.robert.tasks.presentation.screens.details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.tasks.domain.model.TaskItem
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class TaskDetailsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailsState())
    val uiState = _uiState.asStateFlow()

    private fun onEditDescription(description: String) {
        _uiState.update { currentState ->
            currentState.copy(
                taskItem = currentState.taskItem?.copy(description = description)
            )
        }
    }

    private fun toggleEditMode(isEditing: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isEditing = isEditing)
        }
    }

    private fun updateTask(taskItem: TaskItem) {
    }

    fun initializeTask(taskItem: TaskItem) {
        _uiState.update { currentState ->
            currentState.copy(taskItem = taskItem)
        }
    }

    fun onInputChange(input: OnInputChange) {
        when (input) {
            is OnInputChange.DescriptionChange -> onEditDescription(input.description)
        }
    }

    fun onEvent(event: TaskDetailsEvents) {
        when (event) {
            is TaskDetailsEvents.UpdateTask -> updateTask(event.taskItem)
            is TaskDetailsEvents.ToggleEditMode -> toggleEditMode(event.isEditing)
        }
    }
}

sealed class OnInputChange {
    data class DescriptionChange(val description: String) : OnInputChange()
}
