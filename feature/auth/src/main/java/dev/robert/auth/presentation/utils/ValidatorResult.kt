package dev.robert.auth.presentation.utils

data class ValidatorResult(
    val isValid: Boolean,
    val message: String? = ""
)
