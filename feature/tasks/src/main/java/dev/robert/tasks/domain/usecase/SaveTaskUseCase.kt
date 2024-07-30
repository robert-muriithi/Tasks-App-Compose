package dev.robert.tasks.domain.usecase

import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.domain.repository.TasksRepository
import javax.inject.Inject

class SaveTaskUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {
    suspend operator fun invoke(task: TaskItem) = tasksRepository.saveTask(task)
}
