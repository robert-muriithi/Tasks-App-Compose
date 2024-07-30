package dev.robert.tasks.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TaskCategory(
    val id: Int,
    val name: String,
    val color: Int
)
