package dev.robert.tasks.domain.usecase

import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.domain.repository.TasksRepository

class UpdateTaskUseCase(private val repository: TasksRepository) {
    operator fun invoke(task: TaskItem) = repository.updateTask(task)
}
