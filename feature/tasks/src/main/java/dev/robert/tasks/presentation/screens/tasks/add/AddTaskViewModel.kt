package dev.robert.tasks.presentation.screens.tasks.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.tasks.domain.model.TaskCategory
import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.domain.usecase.SaveTaskUseCase
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
    private val saveTaskUseCase: SaveTaskUseCase,
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

    private fun onTitleChanged(title: String) =
        _uiState.update { it.copy(taskTitle = title) }

    private fun onDescriptionChanged(description: String) =
        _uiState.update { it.copy(taskDescription = description) }

    private fun onStartDateChanged(startDate: String) =
        _uiState.update { it.copy(taskStartDate = startDate) }

    private fun onTaskEndTimeChanged(endTime: String) =
        _uiState.update { it.copy(endTime = endTime) }

    private fun onTaskStartTimeChanged(startTime: String) =
        _uiState.update { it.copy(startTime = startTime) }

    private fun setSelectCategory(category: TaskCategory) {
        _uiState.update { it.copy(category = category) }
    }

    fun onEvent(event: AddTaskEvents) = when (event) {
        is AddTaskEvents.CreateTaskEvent -> addTask()
        is AddTaskEvents.GetCategoriesEvent -> getCategories()
        is AddTaskEvents.AddCategoryEvent -> addCategory()
    }

    fun onInputChanged(event: OnInputChanged) = when (event) {
        is OnInputChanged.TaskTitleChanged -> onTitleChanged(event.title)
        is OnInputChanged.TaskDescriptionChanged -> onDescriptionChanged(event.description)
        is OnInputChanged.TaskStartDateChanged -> onStartDateChanged(event.startDate)
        // is OnInputChanged.TaskEndDate -> onEndDateChanged(event.endDate)
        is OnInputChanged.TaskStartTimeChanged -> onTaskStartTimeChanged(event.startTime)
        is OnInputChanged.TaskEndTimeChanged -> onTaskEndTimeChanged(event.endTime)
        is OnInputChanged.SelectCategory -> setSelectCategory(event.category)
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

    private fun addCategory() {
        viewModelScope.launch { _actions.send(Action.AddCategory) }
    }

    private fun addTask() {
        val currentState = uiState.value
        val titleValid = validator.validateTaskTitle(currentState.taskTitle)
        val descriptionValid = validator.validateTaskDescription(currentState.taskDescription)
        val taskDateValid = validator.validateTaskDueDate(currentState.taskStartDate)
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

        viewModelScope.launch(handler) {
            _uiState.update { it.copy(isLoading = true) }
            val result = saveTaskUseCase(
                task = TaskItem(
                    name = currentState.taskTitle,
                    description = currentState.taskDescription,
                    startDateTime = currentState.startTime,
                    endDateTime = currentState.endTime,
                    category = currentState.category,
                    synced = false,
                    complete = false,
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

sealed class OnInputChanged {
    data class TaskTitleChanged(val title: String) : OnInputChanged()
    data class TaskDescriptionChanged(val description: String) : OnInputChanged()
    data class TaskStartDateChanged(val startDate: String) : OnInputChanged()
    // data class TaskEndDate(val endDate: String) : OnInputChanged()
    data class TaskStartTimeChanged(val startTime: String) : OnInputChanged()
    data class TaskEndTimeChanged(val endTime: String) : OnInputChanged()
    data class SelectCategory(val category: TaskCategory) : OnInputChanged()
}

sealed class Action {
    data object AddCategory : Action()
    data class ShowDialog(val result: ActionResult) : Action()
}
