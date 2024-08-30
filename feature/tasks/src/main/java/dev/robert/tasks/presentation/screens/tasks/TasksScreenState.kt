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
    val syncing: Boolean = false,
    val isRefreshing: Boolean = false,
    val refreshed: Boolean = false,
    val analytics: Analytics = Analytics(),
    val selectedCategory: TaskCategory? = null,
)

data class Analytics(
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val todaysCompleteTasks: Int = 0,
    val completionPercentage: Float = 0f
)
