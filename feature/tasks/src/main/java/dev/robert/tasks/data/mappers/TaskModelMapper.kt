package dev.robert.tasks.data.mappers

import dev.robert.database.data.todo.TodoEntity
import dev.robert.tasks.data.model.TaskCategoryModel
import dev.robert.tasks.data.model.TodoModel
import dev.robert.tasks.domain.model.TaskCategory
import dev.robert.tasks.domain.model.TaskItem

fun TodoEntity.toTodoModel() = TodoModel(
    id = id,
    name = name,
    description = description,
    startDateTime = startDateTime,
    endDateTime = endDateTime,
    isComplete = isComplete,
    isSynced = isSynced,
)

fun TodoModel.toEntity() = TodoEntity(
    id = id,
    name = name,
    description = description,
    startDateTime = startDateTime,
    endDateTime = endDateTime,
    isComplete = isComplete,
    isSynced = isSynced,
)

fun TodoModel.toTodoItem() = TaskItem(
    id = id,
    name = name,
    description = description,
    startDateTime = startDateTime,
    endDateTime = endDateTime,
    isComplete = isComplete,
    isSynced = isSynced,
)

fun TaskItem.toTodoModel() = TodoModel(
    id = id,
    name = name,
    description = description,
    startDateTime = startDateTime,
    endDateTime = endDateTime,
    isComplete = isComplete,
    isSynced = isSynced,
)

fun TaskCategoryModel.toDomain() = TaskCategory(
    id = id,
    name = name,
    color = color
)
