package dev.robert.auth.domain.model

data class GoogleUser(
    val email: String,
    val name: String,
    val photoUrl: String,
    val id: String
)
