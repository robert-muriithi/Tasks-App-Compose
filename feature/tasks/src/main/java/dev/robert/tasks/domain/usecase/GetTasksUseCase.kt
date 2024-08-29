package dev.robert.tasks.domain.usecase

import dev.robert.tasks.domain.repository.TasksRepository
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {
//    operator fun invoke() = tasksRepository.tasks
    operator fun invoke(fetchRemote: Boolean) = tasksRepository.tasks(fetchRemote)
}
