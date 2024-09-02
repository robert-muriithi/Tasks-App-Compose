package dev.robert.tasks.domain.usecase

import dev.robert.tasks.domain.repository.TasksRepository

class SearchUseCase(
    private val repository: TasksRepository
) {
    operator fun invoke(query: String) = repository.searchTasks(query)
}
