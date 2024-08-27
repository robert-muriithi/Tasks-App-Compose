package dev.robert.tasks.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.tasks.data.datasource.LocalDataSource
import dev.robert.tasks.data.datasource.LocalDataStoreImpl
import dev.robert.tasks.data.datasource.RemoteDataSource
import dev.robert.tasks.data.datasource.RemoteDataSourceImpl
import dev.robert.tasks.data.repo.TasksRepositoryImpl
import dev.robert.tasks.domain.repository.TasksRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryBindings {
    @Binds
    fun bindTasksRepository(tasksRepository: TasksRepositoryImpl): TasksRepository

    @Binds
    fun bindLocalDataSource(tasksRepository: LocalDataStoreImpl): LocalDataSource

    @Binds
    fun bindRemoteDataSource(tasksRepository: RemoteDataSourceImpl): RemoteDataSource
}
