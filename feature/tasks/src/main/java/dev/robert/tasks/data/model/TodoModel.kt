package dev.robert.tasks.data.model

data class TodoModel(
    val id: Int? = null,
    val name: String = "",
    val description: String = "",
    val startDateTime: String = "",
    val endDateTime: String = "",
    val complete: Boolean = false,
    val synced: Boolean = false,
    val category: TaskCategoryModel? = null,
    val taskDate: String = ""
)

data class TaskCategoryModel(
    val name: String = "",
)
