package dev.robert.auth.data.model

data class GoogleUserDto(
    val email: String,
    val name: String,
    val photoUrl: String,
    val id: String
)
