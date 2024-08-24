package dev.robert.tasks.domain.usecase

import dev.robert.tasks.domain.repository.TasksRepository

class CompleteTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    suspend operator fun invoke(taskId: Int) = tasksRepository.completeTask(taskId)
}
