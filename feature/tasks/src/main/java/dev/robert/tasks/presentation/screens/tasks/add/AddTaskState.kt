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
package dev.robert.tasks.presentation.screens.tasks.add

import dev.robert.tasks.domain.model.TaskCategory

data class AddTaskState(
    val isLoading: Boolean = false,
    val error: String = "",
    val taskTitle: String = "",
    val taskTitleError: String? = null,
    val taskDescription: String = "",
    val taskDescriptionError: String? = null,
    val taskStartDate: String = "",
    val taskStartDateError: String? = null,
    val taskEndDate: String = "",
    val taskEndDateError: String? = null,
    val taskPriority: String = "",
    val category: TaskCategory? = null,
    val categoryError: String? = null,
    val startTime: String = "",
    val endTime: String = "",
    val categories: List<TaskCategory> = emptyList()
)
