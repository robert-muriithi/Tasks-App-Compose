package dev.robert.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import dev.robert.database.data.todo.TaskCategoryModel

@ProvidedTypeConverter
class TasksTypeConverter(
    private val gson: Gson
) {

    @TypeConverter
    fun fromTaskCategoryModel(taskCategoryModel: TaskCategoryModel): String {
        return gson.toJson(taskCategoryModel)
    }

    @TypeConverter
    fun toTaskCategoryModel(taskCategoryModel: String): TaskCategoryModel {
        return gson.fromJson(taskCategoryModel, TaskCategoryModel::class.java)
    }
}
