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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.robert.tasks.R
import dev.robert.tasks.domain.model.Action
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
    var textFieldValue by remember { mutableStateOf(TextFieldValue(state.taskItem?.description ?: "")) }
    var undoStack by remember { mutableStateOf(mutableListOf<String>()) }
    var redoStack by remember { mutableStateOf(mutableListOf<String>()) }

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

    LaunchedEffect(state.taskItem?.description) {
        textFieldValue = TextFieldValue(
            text = state.taskItem?.description ?: "",
            selection = TextRange(state.taskItem?.description?.length ?: 0)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
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
                        value = textFieldValue,
                        onValueChange = { newDescription ->
                            undoStack.add(textFieldValue.text)
                            redoStack.clear()
                            textFieldValue = newDescription
                            onInputChange(OnInputChange.DescriptionChange(newDescription.text))
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                if (it.isFocused) {
                                    textFieldValue = textFieldValue.copy(
                                        selection = TextRange(textFieldValue.text.length)
                                    )
                                }
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

        BottomToolBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .imePadding(),
            undoStack = undoStack,
            redoStack = redoStack,
            onUndoRedo = { newDescription ->
                textFieldValue = TextFieldValue(
                    text = newDescription,
                    selection = TextRange(newDescription.length)
                )
                onInputChange(OnInputChange.DescriptionChange(newDescription))
            }
        )
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
@Composable
fun BottomToolBar(
    modifier: Modifier = Modifier,
    undoStack: MutableList<String>,
    redoStack: MutableList<String>,
    onUndoRedo: (String) -> Unit,
) {

    val actions = listOf(
        Action(
            icon = dev.robert.design_system.R.drawable.undo_icon,
            contentDescription = "Undo",
            onClick = {
                if (undoStack.isNotEmpty()) {
                    redoStack.add(undoStack.last())
                    undoStack.removeAt(undoStack.lastIndex)
                    onUndoRedo(undoStack.last())
                }
            },
            enabled = undoStack.isNotEmpty()
        ),
        Action(
            icon = dev.robert.design_system.R.drawable.redo_icon,
            contentDescription = "Redo",
            onClick = {
                if (redoStack.isNotEmpty()) {
                    undoStack.add(redoStack.last())
                    redoStack.removeAt(redoStack.lastIndex)
                    onUndoRedo(redoStack.last())
                }
            },
        ),
        Action(
            icon = dev.robert.design_system.R.drawable.text_clear_formatting,
            contentDescription = "Format",
            onClick = {
            },
        ),
        Action(
            icon = R.drawable.baseline_list_24,
            contentDescription = "List",
            onClick = {
            },
        ),
    )

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(
            items = actions,
            itemContent = { action ->
                ActionIcon(
                    onClick = action.onClick,
                    icon = action.icon,
                    contentDescription = action.contentDescription,
                    enabled = action.enabled
                )
            }
        )
    }
}

@Composable
fun ActionIcon(
    onClick: () -> Unit,
    icon: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .clickable {
                if (enabled) {
                    onClick()
                }
            }
            .padding(3.dp)

    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            tint = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
