package dev.robert.tasks.presentation.screens.tasks.add

import dev.robert.tasks.domain.model.TaskCategory

data class AddTaskState(
    val isLoading: Boolean = false,
    val error: String = "",
    val taskTitle: String = "",
    val taskTitleError: String? = null,
    val taskDescription: String = "",
    val taskDescriptionError: String? = null,
    val taskStartDate: String = "",
    val taskStartDateError: String? = null,
    val taskEndDate: String = "",
    val taskEndDateError: String? = null,
    val taskPriority: String = "",
    val category: TaskCategory? = null,
    val categoryError: String? = null,
    val startTime: String = "",
    val endTime: String = "",
    val categories: List<TaskCategory> = emptyList()
)
