package dev.robert.auth.presentation.utils

import android.util.Patterns

class EmailValidator {
    fun validate(email: String): ValidatorResult = if (email.isBlank()) {
        ValidatorResult(false, "Email cannot be empty")
    } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
        ValidatorResult(false, "Invalid email")
    } else {
        ValidatorResult(true)
    }
}
