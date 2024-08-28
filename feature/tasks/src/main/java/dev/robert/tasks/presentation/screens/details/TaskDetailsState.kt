package dev.robert.tasks.presentation.screens.details

import dev.robert.tasks.domain.model.TaskItem

data class TaskDetailsState(
    val taskItem: TaskItem? = null,
    val isEditing: Boolean = false
)
