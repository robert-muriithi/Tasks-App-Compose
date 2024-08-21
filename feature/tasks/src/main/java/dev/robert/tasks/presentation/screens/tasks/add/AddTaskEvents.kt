package dev.robert.tasks.presentation.screens.tasks.add

sealed class AddTaskEvents {
    data object CreateTaskEvent : AddTaskEvents()
    data object GetCategoriesEvent : AddTaskEvents()
    data object AddCategoryEvent : AddTaskEvents()
}
