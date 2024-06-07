package dev.robert.database.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    exportSchema = false,
    entities = [
        TodoEntity::class,
    ],
    version = 1,
)
abstract class TodoDatabase : RoomDatabase() {
    abstract val dao: TodoDao
}
