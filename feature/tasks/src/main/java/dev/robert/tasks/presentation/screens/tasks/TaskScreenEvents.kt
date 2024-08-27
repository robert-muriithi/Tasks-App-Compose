package dev.robert.tasks.presentation.screens.tasks

import dev.robert.tasks.domain.model.TaskItem

sealed class TaskScreenEvents {
    data class LoadTasks(val fetchRemote: Boolean = false) : TaskScreenEvents()
    // object RefreshTasks : TaskScreenEvents()
    data class FilterTasks(val filterString: String) : TaskScreenEvents()
    data class ToggleGrid(val isGrid: Boolean) : TaskScreenEvents()
    data class SyncTask(val task: TaskItem) : TaskScreenEvents()
    data class CompleteTask(val task: TaskItem) : TaskScreenEvents()
    data class DeleteTask(val task: TaskItem) : TaskScreenEvents()
    data class RefreshTasks(val fetchRemote: Boolean = false, val refresh: Boolean = true) : TaskScreenEvents()
}
