package dev.robert.tasks.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class TaskCategory(
    val name: String,
) : Parcelable
