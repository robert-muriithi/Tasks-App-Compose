/*
 * Copyright 2024 Robert Muriithi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
