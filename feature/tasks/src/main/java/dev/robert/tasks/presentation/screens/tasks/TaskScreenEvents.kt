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
package dev.robert.tasks.presentation.screens.tasks

import dev.robert.tasks.domain.model.TaskItem

sealed class TaskScreenEvents {
    data class LoadTasks(val fetchRemote: Boolean = false) : TaskScreenEvents()
    // object RefreshTasks : TaskScreenEvents()
    data class FilterTasks(val filterString: String) : TaskScreenEvents()
    data class ToggleGrid(val isGrid: Boolean) : TaskScreenEvents()
    data class SyncTask(val task: TaskItem) : TaskScreenEvents()
    data class CompleteTask(val task: TaskItem) : TaskScreenEvents()
    data class DeleteTask(val task: TaskItem) : TaskScreenEvents()
    data class RefreshTasks(val fetchRemote: Boolean = false, val refresh: Boolean = true) : TaskScreenEvents()
}
