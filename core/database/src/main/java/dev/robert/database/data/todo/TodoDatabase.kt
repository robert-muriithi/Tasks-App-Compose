package dev.robert.database.data.todo

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.robert.database.TasksTypeConverter

@Database(
    exportSchema = false,
    entities = [
        TodoEntity::class,
    ],
    version = 1,
)
@TypeConverters(TasksTypeConverter::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract val dao: TodoDao
}
