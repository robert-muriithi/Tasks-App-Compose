package dev.robert.database.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.robert.database.ConstUtils.TODO_TABLE_NAME

@Entity(tableName = TODO_TABLE_NAME)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val startDateTime: String,
    val endDateTime: String,
    val category: String,
    val isComplete: Boolean = false,
    val isSynced: Boolean = false,
//    val attachment : Attachment? = null
)
