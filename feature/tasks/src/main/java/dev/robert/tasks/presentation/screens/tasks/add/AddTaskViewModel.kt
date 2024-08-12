package dev.robert.tasks.presentation.screens.tasks.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.domain.repository.TasksRepository
import dev.robert.tasks.presentation.utils.Validator
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val repository: TasksRepository,
    private val validator: Validator
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTaskState())
    val uiState = _uiState.asStateFlow()

    private val handler = CoroutineExceptionHandler { _, exception ->
        _uiState.update { it.copy(error = exception.message ?: "An error occurred") }
    }

    fun onTitleChanged(title: String) = _uiState.update { it.copy(taskTitle = title) }
    fun onDescriptionChanged(description: String) = _uiState.update { it.copy(taskDescription = description) }
    fun onStartDateChanged(startDate: String) = _uiState.update { it.copy(taskStartDate = startDate) }
    fun onEndDateChanged(endDate: String) = _uiState.update { it.copy(taskEndDate = endDate) }
    fun onPriorityChanged(priority: String) = _uiState.update { it.copy(taskPriority = priority) }
    fun onTaskEndTimeChanged(endTime: String) = _uiState.update { it.copy(endTime = endTime) }
    fun onTaskStartTimeChanged(startTime: String) = _uiState.update { it.copy(startTime = startTime) }

    // private fun onCategoryChanged(category: String) = _uiState.update { it.copy(category = category) }

    fun onEvent(event: AddTaskEvents) = when (event) {
        AddTaskEvents.AddTask -> addTask()
    }

    private fun addTask() {
        val currentState = uiState.value
        val titleValid = validator.validateTaskTitle(currentState.taskTitle)
        val descriptionValid = validator.validateTaskDescription(currentState.taskDescription)
        val endDateValid = validator.validateTaskDueDate(currentState.taskEndDate)
        val hasError = listOf(titleValid, descriptionValid, endDateValid).any { !it.isValid }

        if (hasError) {
            _uiState.update {
                it.copy(
                    taskTitleError = titleValid.message,
                    taskDescriptionError = descriptionValid.message,
                    taskEndDateError = endDateValid.message,
                )
            }
            return
        }

        // Add task
        viewModelScope.launch(handler) {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.saveTask(
                task = TaskItem(
                    name = currentState.taskTitle,
                    description = currentState.taskDescription,
                    startDateTime = currentState.taskStartDate,
                    endDateTime = currentState.taskEndDate,
                    category = currentState.category,
                    isSynced = false,
                    isComplete = false
                )
            )
            when (result.isSuccess) {
                true -> _uiState.update { it.copy(isLoading = false) }
                false -> _uiState.update { it.copy(error = "An error occurred", isLoading = false) }
            }
        }
    }
}
