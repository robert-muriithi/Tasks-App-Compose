/*
 * Copyright 2024 Robert Muriithi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import dev.robert.tasks.domain.usecase.SearchUseCase
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
    fun provideSearchTasksUseCase(
        repository: TasksRepository
    ): SearchUseCase = SearchUseCase(repository = repository)

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
