package dev.robert.settings.presentation.screens.change_password

data class ChangePasswordState(
    val currentPassword: String = "",
    val currentPasswordError: String? = null,
    val newPassword: String = "",
    val newPasswordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val changePasswordError: String? = null,
    val strongPasswordsConditions: List<StrongPasswordCondition> = listOf(
        StrongPasswordCondition("At least 8 characters"),
        StrongPasswordCondition("At least 1 uppercase letter"),
        StrongPasswordCondition("At least 1 lowercase letter"),
        StrongPasswordCondition("At least 1 number"),
        StrongPasswordCondition("At least 1 special character"),
        StrongPasswordCondition("No consecutive numbers"),
        StrongPasswordCondition("Password must match"),

    )
)

data class StrongPasswordCondition(
    val description: String,
    val isMet: Boolean = false
)
