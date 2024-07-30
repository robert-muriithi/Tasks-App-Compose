package dev.robert.tasks.data.model

data class TodoModel(
    val id: Int,
    val name: String,
    val description: String,
    val startDateTime: String,
    val endDateTime: String,
    val isComplete: Boolean,
    val isSynced: Boolean,
    val category: TaskCategoryModel? = null
)

data class TaskCategoryModel(
    val id: Int,
    val name: String,
    val color: Int
)
