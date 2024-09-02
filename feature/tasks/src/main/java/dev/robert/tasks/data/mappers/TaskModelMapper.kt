package dev.robert.tasks.data.mappers

import dev.robert.database.data.todo.TaskCategoryModelEntity
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
    complete = complete,
    synced = synced,
    taskDate = taskDate,
    category = category?.toDomain()
)

fun TaskCategoryModelEntity.toDomain() = TaskCategoryModel(
    name = name,
)

fun TaskCategoryModel.toEntity() = TaskCategoryModelEntity(
    name = name,
)

fun TodoModel.toEntity() = TodoEntity(
    id = id,
    name = name,
    description = description,
    startDateTime = startDateTime,
    endDateTime = endDateTime,
    complete = complete,
    synced = synced,
    taskDate = taskDate,
    category = category?.toEntity()
)

fun TodoModel.toTodoItem() = TaskItem(
    id = id,
    name = name,
    description = description,
    startDateTime = startDateTime,
    endDateTime = endDateTime,
    complete = complete,
    synced = synced,
    taskDate = taskDate,
    category = category?.toDomain()
)

fun TaskItem.toTodoModel() = TodoModel(
    id = id,
    name = name,
    description = description,
    startDateTime = startDateTime,
    endDateTime = endDateTime,
    complete = complete,
    synced = synced,
    taskDate = taskDate,
    category = category?.toModel()
)

fun TaskCategoryModel.toDomain() = TaskCategory(
    name = name,
)

fun TaskCategory.toModel() = TaskCategoryModel(
    name = name,
)
