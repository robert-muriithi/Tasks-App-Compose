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
