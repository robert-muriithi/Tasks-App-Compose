package dev.robert.tasks.presentation.screens.tasks.add

import dev.robert.tasks.domain.model.TaskCategory

sealed class AddTaskEvents {
    data object CreateTaskEvent : AddTaskEvents()
    data class SelectCategoryEvent(val category: TaskCategory) : AddTaskEvents()
    data object GetCategoriesEvent : AddTaskEvents()
    data object AddCategoryEvent : AddTaskEvents()
}
