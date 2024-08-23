package dev.robert.tasks.presentation.screens.tasks

sealed class TaskScreenEvents {
    data object LoadTasks : TaskScreenEvents()
    // object RefreshTasks : TaskScreenEvents()
    data class FilterTasks(val filterString: String) : TaskScreenEvents()
    data class ToggleGrid(val isGrid: Boolean) : TaskScreenEvents()
    object NavigateToDetails : TaskScreenEvents()
}
