package dev.robert.auth.presentation.utils

class NameValidator {
    fun validate(name: String): ValidatorResult = if (name.isBlank())
        ValidatorResult(false, "Name cannot be empty")
    else
        ValidatorResult(true)
}
