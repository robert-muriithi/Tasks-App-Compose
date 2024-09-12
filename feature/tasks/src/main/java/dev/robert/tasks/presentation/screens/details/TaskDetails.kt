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
package dev.robert.tasks.presentation.screens.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.robert.tasks.R
import dev.robert.tasks.domain.model.TaskItem

@Composable
fun TaskDetailsScreen(
    taskItem: TaskItem,
    onNavigateUp: () -> Unit,
    viewModel: TaskDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(taskItem) {
        viewModel.initializeTask(taskItem)
    }

    TaskDetailsScreenContent(
        state = state,
        onInputChange = viewModel::onInputChange,
        onEvent = viewModel::onEvent,
        taskItem = taskItem,
        onNavigateUp = onNavigateUp
    )
}
@Composable
fun TaskDetailsScreenContent(
    state: TaskDetailsState,
    taskItem: TaskItem,
    onNavigateUp: () -> Unit,
    onInputChange: (OnInputChange) -> Unit,
    onEvent: (TaskDetailsEvents) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(state.isEditing) {
        if (state.isEditing) {
            focusRequester.requestFocus()
        }
    }

    LaunchedEffect(key1 = state.taskUpdated) {
        if (state.taskUpdated) {
            onNavigateUp()
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        item {
            AppBar(
                taskItem = taskItem,
                onNavigateUp = onNavigateUp,
                state = state,
                onEvent = onEvent
            )
        }
        item {
            Box(modifier = Modifier.fillMaxSize()) {
                OutlinedTextField(
                    value = state.taskItem?.description ?: "",
                    onValueChange = { newDescription ->
                        onInputChange(OnInputChange.DescriptionChange(newDescription))
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            isFocused = it.isFocused
                        }
                        .clickable {
                            onEvent(TaskDetailsEvents.ToggleEditMode(!state.isEditing))
                        },
                    maxLines = 150,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    enabled = state.isEditing,
                )
            }
        }
    }
}

@Composable
fun AppBar(
    taskItem: TaskItem,
    onNavigateUp: () -> Unit,
    state: TaskDetailsState,
    onEvent: (TaskDetailsEvents) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDescriptionChanged = state.taskItem?.description != taskItem.description

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                onNavigateUp()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.back)
            )
        }
        Text(
            text = taskItem.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = {
                if (state.isEditing && isDescriptionChanged) {
                    onEvent(TaskDetailsEvents.UpdateTask(state.taskItem!!))
                }
                onEvent(TaskDetailsEvents.ToggleEditMode(!state.isEditing))
            }
        ) {
            Icon(
                imageVector = if (state.isEditing &&
                    isDescriptionChanged) Icons.Filled.Check else Icons.Filled.Edit,
                contentDescription = stringResource(id = R.string.edit)
            )
        }
    }
}
