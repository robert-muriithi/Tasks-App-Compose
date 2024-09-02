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
package dev.robert.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import dev.robert.database.data.todo.TaskCategoryModelEntity

@ProvidedTypeConverter
class TasksTypeConverter(
    private val gson: Gson
) {

    @TypeConverter
    fun fromTaskCategoryModel(taskCategoryModelEntity: TaskCategoryModelEntity): String {
        return gson.toJson(taskCategoryModelEntity)
    }

    @TypeConverter
    fun toTaskCategoryModel(taskCategoryModel: String): TaskCategoryModelEntity {
        return gson.fromJson(taskCategoryModel, TaskCategoryModelEntity::class.java)
    }
}
