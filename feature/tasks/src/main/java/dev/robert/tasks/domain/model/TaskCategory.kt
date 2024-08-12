package dev.robert.tasks.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class TaskCategory(
    val id: Int,
    val name: String,
    val color: Int
) : Parcelable
