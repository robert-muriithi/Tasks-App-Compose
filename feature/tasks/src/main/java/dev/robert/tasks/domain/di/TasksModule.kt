package dev.robert.tasks.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.tasks.domain.repository.TasksRepository
import dev.robert.tasks.domain.usecase.CompleteTaskUseCase
import dev.robert.tasks.domain.usecase.DeleteTaskUseCase
import dev.robert.tasks.domain.usecase.GetTasksUseCase
import dev.robert.tasks.domain.usecase.SaveTaskUseCase
import dev.robert.tasks.domain.usecase.UploadTaskToServerUseCase
import dev.robert.tasks.presentation.utils.Validator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TasksModule {

    @[
    Provides
    Singleton
    ]
    fun provideGetAllTasksUseCase(
        repository: TasksRepository
    ): GetTasksUseCase = GetTasksUseCase(tasksRepository = repository)

    @[
    Provides
    Singleton
    ]
    fun provideUploadTaskToServerUseCase(
        repository: TasksRepository
    ): UploadTaskToServerUseCase = UploadTaskToServerUseCase(repository = repository)

    @[
    Provides
    Singleton
    ]
    fun provideCompleteTaskUseCase(
        repository: TasksRepository
    ): CompleteTaskUseCase = CompleteTaskUseCase(tasksRepository = repository)

    @[
    Provides
    Singleton
    ]
    fun provideDeleteTaskUseCase(
        repository: TasksRepository
    ): DeleteTaskUseCase = DeleteTaskUseCase(tasksRepository = repository)

    @[
    Provides
    Singleton
    ]
    fun provideSaveTaskUseCase(
        repository: TasksRepository
    ): SaveTaskUseCase = SaveTaskUseCase(tasksRepository = repository)

    @[
    Provides
    ]
    fun provideValidator(): Validator = Validator()
}
