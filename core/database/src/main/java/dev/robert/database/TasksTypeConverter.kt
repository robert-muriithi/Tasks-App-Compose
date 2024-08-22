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
