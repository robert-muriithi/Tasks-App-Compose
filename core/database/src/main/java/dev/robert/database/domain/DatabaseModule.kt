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
package dev.robert.database.domain

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.robert.database.ConstUtils.TODO_DATABASE
import dev.robert.database.TasksTypeConverter
import dev.robert.database.data.todo.TodoDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @[
    Provides
    Singleton
    ]
    fun provideGson() = Gson()

    @[
    Provides
    Singleton
    ]
    fun provideTodoDao(db: TodoDatabase) = db.dao

    @[
    Provides
    Singleton
    ]
    fun provideTypeConverters(gson: Gson) = TasksTypeConverter(gson)

    @[
    Provides
    Singleton
    ]
    fun provideTodoDatabase(
        @ApplicationContext context: Context,
        converter: TasksTypeConverter
    ): TodoDatabase {
        return Room.databaseBuilder(
            context = context,
            TodoDatabase::class.java,
            TODO_DATABASE,
        ).addTypeConverter(converter)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}
