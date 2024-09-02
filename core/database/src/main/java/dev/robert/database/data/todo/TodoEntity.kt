package dev.robert.database.data.todo

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.robert.database.ConstUtils.TODO_TABLE_NAME

@Entity(tableName = TODO_TABLE_NAME)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val description: String,
    val startDateTime: String,
    val endDateTime: String,
    val category: TaskCategoryModelEntity? = null,
    val complete: Boolean = false,
    val synced: Boolean = false,
    val taskDate: String,
    val completionDate: String? = null,
//    val attachment : Attachment? = null
)

data class TaskCategoryModelEntity(
    val name: String,
)
