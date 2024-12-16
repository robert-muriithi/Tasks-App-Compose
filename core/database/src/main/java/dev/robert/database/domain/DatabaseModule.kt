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
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.robert.database.ConstUtils.TODO_DATABASE
import dev.robert.database.TasksTypeConverter
import dev.robert.database.data.TodoDatabase
import dev.robert.database.data.categories.CategoryEntity
import dev.robert.design_system.R
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    fun provideTodoDao(db: TodoDatabase) = db.tasksDao

    @[
    Provides
    Singleton
    ]
    fun provideCategoryDao(db: TodoDatabase) = db.categoryDao

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
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
//                    db.execSQL("INSERT INTO task_categories (name, color, icon) VALUES ('Work', '#FF5733', '')")
//                    db.execSQL("INSERT INTO task_categories (name, color, icon) VALUES ('Personal', '#33FF57', '')")
//                    db.execSQL("INSERT INTO task_categories (name, color, icon) VALUES ('Shopping', '#3357FF', '')")
//                    db.execSQL("INSERT INTO task_categories (name, color, icon) VALUES ('Health', '#FF33F6', '')")
//                    db.execSQL("INSERT INTO task_categories (name, color, icon) VALUES ('Miscellaneous', '#33FFF6', '')")
                    CoroutineScope(Dispatchers.IO).launch {
                        provideCategoryDao(provideTodoDatabase(context, converter))
                            .insertAll(getInitialCategories())
                    }
                }
            })
            .build()
    }

    // Pre-populate the database with some categories
    private fun getInitialCategories(): List<CategoryEntity> = listOf(
        CategoryEntity(name = "Work", color = "#FF5733", icon = R.drawable.ic_work_outline),
        CategoryEntity(name = "Personal", color = "#33FF57", icon = R.drawable.ic_personal),
        CategoryEntity(name = "Shopping", color = "#3357FF", icon = R.drawable.ic_shopping),
        CategoryEntity(name = "Health", color = "#FF33F6", icon = R.drawable.ic_health_safety),
        CategoryEntity(name = "Miscellaneous", color = "#33FFF6", icon = R.drawable.ic_miscellaneous_services)
    )
}
