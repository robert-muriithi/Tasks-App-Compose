package dev.robert.tasks.domain.model

data class Action(
    val icon: Int,
    val contentDescription: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true
)
