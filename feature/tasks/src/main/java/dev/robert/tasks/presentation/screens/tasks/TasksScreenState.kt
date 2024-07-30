package dev.robert.tasks.presentation.screens.tasks

import dev.robert.tasks.domain.model.TaskItem

data class TasksScreenState(
    val tasks: List<TaskItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null
)
