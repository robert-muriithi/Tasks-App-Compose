package dev.robert.auth.domain.model

data class RegisterRequestBody(
    val email: String,
    val password: String,
    val name: String
)
