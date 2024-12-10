package dev.robert.database.data.categories

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.robert.database.ConstUtils.TODO_TASK_CATEGORY_TABLE_NAME

@Entity(tableName = TODO_TASK_CATEGORY_TABLE_NAME)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val color: String,
    @field:DrawableRes
    val icon: Int,
)
