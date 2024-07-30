package dev.robert.tasks.presentation.screens.tasks

sealed class TaskScreenEvents {
    object LoadTasks : TaskScreenEvents()
    object RefreshTasks : TaskScreenEvents()
    object NavigateToDetails : TaskScreenEvents()
}
