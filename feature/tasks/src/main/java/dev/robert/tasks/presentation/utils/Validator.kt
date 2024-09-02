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
package dev.robert.tasks.presentation.utils

class Validator {
    fun validateTaskTitle(title: String): ValidatorResult = if (title.isEmpty()) {
        ValidatorResult(false, "Task title cannot be empty")
    } else {
        ValidatorResult(true)
    }

    fun validateTaskDescription(description: String): ValidatorResult = if (description.isEmpty()) {
        ValidatorResult(false, "Description cannot be empty")
    } else {
        ValidatorResult(true)
    }

    fun validateTaskDueDate(dueDate: String): ValidatorResult = if (dueDate.isEmpty()) {
        ValidatorResult(false, "date cannot be empty")
    } else {
        ValidatorResult(true)
    }

    fun validateTaskPriority(priority: String): ValidatorResult = if (priority.isEmpty()) {
        ValidatorResult(false, "Priority cannot be empty")
    } else {
        ValidatorResult(true)
    }
}
