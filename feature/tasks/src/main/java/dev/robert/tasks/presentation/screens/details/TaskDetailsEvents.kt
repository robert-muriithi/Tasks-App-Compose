package dev.robert.tasks.presentation.screens.details

import dev.robert.tasks.domain.model.TaskItem

sealed class TaskDetailsEvents {
    data class UpdateTask(val taskItem: TaskItem) : TaskDetailsEvents()
    data class ToggleEditMode(val isEditing: Boolean) : TaskDetailsEvents()
}
