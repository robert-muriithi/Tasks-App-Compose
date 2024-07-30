package dev.robert.tasks.domain.usecase

import dev.robert.tasks.domain.repository.TasksRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {
    suspend operator fun invoke(taskId: Int) = tasksRepository.deleteTask(taskId)
}
