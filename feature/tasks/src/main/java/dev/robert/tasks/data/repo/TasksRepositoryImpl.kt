package dev.robert.tasks.data.repo
import dev.robert.tasks.data.datasource.LocalDataSource
import dev.robert.tasks.data.mappers.toTodoItem
import dev.robert.tasks.data.mappers.toTodoModel
import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.domain.repository.TasksRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class TasksRepositoryImpl @Inject constructor(
    private val dataSource: LocalDataSource
) : TasksRepository {

    override val tasks: Flow<List<TaskItem>>
        get() = dataSource.tasks.map { list ->
            list.map {
                it.toTodoItem()
            }
        }

    override fun getTaskById(id: Int): Flow<TaskItem> {
        return dataSource.getTaskById(id).map {
            it.toTodoItem()
        }
    }

    override suspend fun saveTask(task: TaskItem) {
        dataSource.saveTask(task.toTodoModel())
    }

    override suspend fun deleteTask(taskId: Int) {
        dataSource.deleteTask(taskId)
    }
}
