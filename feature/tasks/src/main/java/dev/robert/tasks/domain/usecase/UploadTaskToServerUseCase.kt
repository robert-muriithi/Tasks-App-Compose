package dev.robert.tasks.domain.usecase

import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.domain.repository.TasksRepository

class UploadTaskToServerUseCase(
    private val repository: TasksRepository
) {
    suspend operator fun invoke(task: TaskItem) = repository.uploadTask(task)
}
