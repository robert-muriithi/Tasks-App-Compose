package dev.robert.tasks.presentation.screens.tasks.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.tasks.domain.model.TaskCategory
import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.domain.repository.TasksRepository
import dev.robert.tasks.presentation.utils.Validator
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val repository: TasksRepository,
    private val validator: Validator
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTaskState())
    val uiState = _uiState.asStateFlow()

    private val _actions = Channel<Action?>()
    val actions = _actions.receiveAsFlow()

    private val handler = CoroutineExceptionHandler { _, exception ->
        _uiState.update { it.copy(error = exception.message ?: "An error occurred") }
        Timber.e(exception.message)
    }

    fun onTitleChanged(title: String) = _uiState.update { it.copy(taskTitle = title) }
    fun onDescriptionChanged(description: String) = _uiState.update { it.copy(taskDescription = description) }
    fun onStartDateChanged(startDate: String) = _uiState.update { it.copy(taskStartDate = startDate) }
    fun onEndDateChanged(endDate: String) = _uiState.update { it.copy(taskEndDate = endDate) }
    fun onTaskEndTimeChanged(endTime: String) = _uiState.update { it.copy(endTime = endTime) }
    fun onTaskStartTimeChanged(startTime: String) = _uiState.update { it.copy(startTime = startTime) }
    fun setSelectCategory(category: TaskCategory) { _uiState.update { it.copy(category = category) } }

    fun onEvent(event: AddTaskEvents) = when (event) {
        is AddTaskEvents.CreateTaskEvent -> addTask()
        is AddTaskEvents.GetCategoriesEvent -> getCategories()
        is AddTaskEvents.AddCategoryEvent -> addCategory()
    }

    fun getCategories() {
        val taskCategories = listOf(
            TaskCategory("Personal"),
            TaskCategory("Work"),
            TaskCategory("Shopping"),
            TaskCategory("Health"),
            TaskCategory("Finance"),
            TaskCategory("Home"),
            TaskCategory("School"),
            TaskCategory("Other")
        )
        _uiState.update { it.copy(categories = taskCategories) }
    }

    private fun addCategory() { viewModelScope.launch { _actions.send(Action.AddCategory) } }

    private fun addTask() {
        val currentState = uiState.value
        val titleValid = validator.validateTaskTitle(currentState.taskTitle)
        val descriptionValid = validator.validateTaskDescription(currentState.taskDescription)
        val taskDateValid = validator.validateTaskDueDate(currentState.taskStartDate)
        val endDateValid = validator.validateTaskDueDate(currentState.taskEndDate)
        val hasError = listOf(titleValid, descriptionValid, taskDateValid).any { !it.isValid }

        if (hasError) {
            _uiState.update {
                it.copy(
                    taskTitleError = titleValid.message,
                    taskDescriptionError = descriptionValid.message,
                    taskStartDateError = taskDateValid.message
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
                    startDateTime = currentState.startTime,
                    endDateTime = currentState.endTime,
                    category = currentState.category,
                    isSynced = false,
                    isComplete = false,
                    taskDate = currentState.taskStartDate
                )
            )
            // Delay to show loading
            delay(100)
            when (result.isSuccess) {
                true -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _actions.send(Action.ShowDialog(ActionResult.Success))
                }
                false -> {
                    _uiState.update { it.copy(error = "An error occurred", isLoading = false) }
                    _actions.send(Action.ShowDialog(ActionResult.Error))
                }
            }
        }
    }
}

enum class ActionResult {
    Success,
    Error
}

sealed class Action {
    data object AddCategory : Action()
    data class ShowDialog(val result: ActionResult) : Action()
}
