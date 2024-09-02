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
