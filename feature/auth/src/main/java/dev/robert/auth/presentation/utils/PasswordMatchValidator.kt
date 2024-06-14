package dev.robert.auth.presentation.utils

class PasswordMatchValidator {
    fun validate(password: String, passwordConfirmation: String): ValidatorResult = if (password != passwordConfirmation) {
        ValidatorResult(false, "Passwords do not match")
    } else {
        ValidatorResult(true)
    }
}
