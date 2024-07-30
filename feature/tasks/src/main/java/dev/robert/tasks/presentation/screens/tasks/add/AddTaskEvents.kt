package dev.robert.tasks.presentation.screens.tasks.add

sealed class AddTaskEvents {
    data object AddTask : AddTaskEvents()
}
