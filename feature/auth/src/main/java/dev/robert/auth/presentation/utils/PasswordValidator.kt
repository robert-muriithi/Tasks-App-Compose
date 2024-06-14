package dev.robert.auth.presentation.utils

class PasswordValidator {
    fun validate(password: String): ValidatorResult = if (password.isBlank()) {
        ValidatorResult(false, "Password cannot be empty")
    } else if (password.length < 6) {
        ValidatorResult(false, "Password must be at least 6 characters long")
    } else if (password.any { it.isDigit() } && password.any { it.isLetter() }.not()) {
        ValidatorResult(false, "Password must contain at least one letter and one digit")
    } else {
        ValidatorResult(true)
    }
}
