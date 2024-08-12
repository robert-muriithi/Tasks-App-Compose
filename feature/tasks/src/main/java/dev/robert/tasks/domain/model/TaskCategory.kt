package dev.robert.tasks.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TaskCategory(
    val name: String,
)
