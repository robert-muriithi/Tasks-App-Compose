package dev.robert.tasks.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class TaskItem(
    val id: Int? = null,
    val name: String,
    val description: String,
    val startDateTime: String,
    val endDateTime: String,
    val isComplete: Boolean,
    val isSynced: Boolean,
    val category: TaskCategory? = null,
    val taskDate: String,
    val completionDate: String? = null
) : Parcelable
