package dev.robert.tasks.presentation.screens.tasks

import dev.robert.tasks.domain.model.TaskCategory
import dev.robert.tasks.domain.model.TaskItem

data class TasksScreenState(
    val tasks: List<TaskItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val category: List<TaskCategory?> = emptyList(),
    val update: Boolean = false,
    val isGridView: Boolean = true,
    val isSynced: Boolean = false,
    val syncing: Boolean = false
)
