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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.robert.design_system.presentation.components.AdvancedTimePicker
import dev.robert.design_system.presentation.components.AlertDialog
import dev.robert.design_system.presentation.components.DialogType
import dev.robert.design_system.presentation.components.TDButton
import dev.robert.design_system.presentation.components.TDFilledTextField
import dev.robert.design_system.presentation.components.TDSpacer
import dev.robert.design_system.presentation.components.TDSurface
import dev.robert.design_system.presentation.utils.convertMillisToDate
import dev.robert.design_system.presentation.utils.formatTimeToAmPm
import dev.robert.tasks.R
import dev.robert.tasks.domain.model.TaskCategory
import dev.robert.tasks.presentation.screens.tasks.TasksCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    onNavigateUp: () -> Unit,
    viewModel: AddTaskViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    var showTimePicker by remember {
        mutableStateOf(false)
    }
    var action by remember {
        mutableStateOf(TIME.START_TIME)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }
    var result by remember {
        mutableStateOf(ActionResult.Success)
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit) {
        viewModel.getCategories()
        viewModel.actions.collectLatest {
            when (it) {
                is Action.AddCategory -> {
                    showBottomSheet = true
                }

                is Action.ShowDialog -> {
                    result = it.result
                    showDialog = true
                }

                else -> {}
            }
        }
    }

    AddTaskContent(
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        onEvent = viewModel::onEvent,
        onInitDatePicker = { showDatePicker = true },
        onInitTimePicker = { timeAction ->
            showTimePicker = true
            action = timeAction
        },
        onInputChange = viewModel::onInputChanged
    )

    if (showBottomSheet) AddCategoryBottomSheet(
        onDismiss = {
            showBottomSheet = false
        },
        onAddCategory = {
            viewModel.onEvent(AddTaskEvents.GetCategoriesEvent)
        },
        scope = scope,
        sheetState = sheetState
    )

    if (showDatePicker) DatePickerModal(
        onSelectDate = {
            viewModel.onInputChanged(OnInputChanged.TaskStartDateChanged(convertMillisToDate(it ?: 0)))
            showDatePicker = false
        },
        onDismiss = {
            showDatePicker = false
        }
    )

    if (showTimePicker) AdvancedTimePicker(
        onConfirm = {
            when (action) {
                TIME.START_TIME -> viewModel.onInputChanged(
                    OnInputChanged.TaskStartTimeChanged(
                        formatTimeToAmPm(it)
                    )
                )

                TIME.END_TIME -> viewModel.onInputChanged(
                    OnInputChanged.TaskEndTimeChanged(
                        formatTimeToAmPm(it)
                    )
                )
            }
            showTimePicker = false
        },
        onDismiss = {
            showTimePicker = false
        }
    )

    if (showDialog) AlertDialog(
        onConfirm = {
            showDialog = false
            onNavigateUp()
        },
        onDismiss = {
            showDialog = false
        },
        title = if (result.name ==
            ActionResult.Success.name) stringResource(R.string.success) else stringResource(
            R.string.error
        ),
        message = if (result.name ==
            ActionResult.Success.name) stringResource(R.string.task_created_successfully) else stringResource(
            R.string.an_error_occurred
        ),
        showCancel = result.name != ActionResult.Success.name,
        type = if (result.name ==
            ActionResult.Success.name) DialogType.SUCCESS else DialogType.ERROR
    )
}

enum class TIME {
    START_TIME,
    END_TIME
}

fun showDialogState(result: ActionResult, showDialog: Boolean): @Composable () -> Unit = {
    if (showDialog) {
    }
}

@Composable
fun AddTaskContent(
    uiState: AddTaskState,
    onNavigateUp: () -> Unit,
    onEvent: (AddTaskEvents) -> Unit,
    onInputChange: (OnInputChanged) -> Unit,
    onInitDatePicker: () -> Unit,
    onInitTimePicker: (TIME) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                AddTaskAppBar(
                    title = stringResource(R.string.create_new_task),
                    onBackClick = onNavigateUp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Box(modifier = Modifier.fillMaxHeight(0.5f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    ) {
                        TDFilledTextField(
                            value = uiState.taskTitle,
                            onValueChange = { onInputChange(OnInputChanged.TaskTitleChanged(it)) },
                            label = stringResource(R.string.title),
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                                capitalization = KeyboardCapitalization.Words
                            ),
                            isLoading = uiState.isLoading,
                            enabled = uiState.isLoading.not()
                        )
                        if (uiState.taskTitleError != null && uiState.isLoading.not())
                            Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                                Text(
                                    text = uiState.taskTitleError,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.errorContainer
                                )
                            }
                        else TDSpacer(modifier = Modifier.height(10.dp))
                        TDFilledTextField(
                            value = uiState.taskStartDate,
                            onValueChange = {
                                onInputChange(OnInputChanged.TaskStartDateChanged(it))
                            },
                            label = stringResource(R.string.date),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onInitDatePicker()
                                },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            isLoading = uiState.isLoading,
                            enabled = false,
                        )
                        if (uiState.taskStartDateError != null && uiState.isLoading.not())
                            Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                                Text(
                                    text = uiState.taskStartDateError,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.errorContainer
                                )
                            }
                        else TDSpacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .padding(top = 250.dp)
                .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(
                    color = MaterialTheme.colorScheme.background
                )
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 30.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TDFilledTextField(
                        value = uiState.startTime,
                        onValueChange = { onInputChange(OnInputChanged.TaskStartTimeChanged(it)) },
                        label = stringResource(R.string.start_time),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onInitTimePicker(TIME.START_TIME)
                            },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        isLoading = uiState.isLoading,
                        enabled = false,
                    )
                    TDSpacer(modifier = Modifier.width(10.dp))
                    TDFilledTextField(
                        value = uiState.endTime,
                        onValueChange = { onInputChange(OnInputChanged.TaskEndTimeChanged(it)) },
                        label = stringResource(R.string.end_time),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onInitTimePicker(TIME.END_TIME)
                            },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        isLoading = uiState.isLoading,
                        enabled = false
                    )
                }
                TDSpacer(modifier = Modifier.height(10.dp))
                TDFilledTextField(
                    value = uiState.taskDescription,
                    onValueChange = { onInputChange(OnInputChanged.TaskDescriptionChanged(it)) },
                    label = stringResource(R.string.description),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    maxLines = 10,
                    isLoading = uiState.isLoading,
                    enabled = uiState.isLoading.not()
                )
                if (uiState.taskDescriptionError != null && uiState.isLoading.not())
                    Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                        Text(
                            text = uiState.taskDescriptionError,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                else TDSpacer(modifier = Modifier.height(10.dp))
                Text(stringResource(R.string.category))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(uiState.categories.size) { index ->
                        val category = uiState.categories[index]
                        if (index == uiState.categories.size - 1)
                            Box(modifier = Modifier
                                .padding(top = 8.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    onEvent(AddTaskEvents.AddCategoryEvent)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add_category),
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        else TasksCategory(
                            category = category.name,
                            onClick = {
                                onInputChange(OnInputChanged.SelectCategory(category))
                            },
                            selected = uiState.category?.name == category.name,
                            modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                TDButton(
                    onClick = {
                        onEvent(AddTaskEvents.CreateTaskEvent)
                    },
                    text = "Create task",
                    enabled = uiState.isLoading.not(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    isLoading = uiState.isLoading
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onSelectDate: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onSelectDate(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun AddTaskAppBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Transparent)
                .clickable {
                    onBackClick()
                }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back),
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryBottomSheet(
    onDismiss: () -> Unit,
    onAddCategory: () -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // TODO: Implement add category functionality to customize categories
        }
    }
}

@Preview
@Composable
private fun AddTaskScreenPreview() {
    TDSurface {
        AddTaskContent(
            AddTaskState(
                categories = listOf(
                    TaskCategory("Work"),
                    TaskCategory("Personal"),
                    TaskCategory("Shopping"),
                    TaskCategory("Home"),
                    TaskCategory("School"),
                    TaskCategory("Others")
                ),
            ),
            onNavigateUp = {},
            onEvent = {},
            onInitDatePicker = {},
            onInitTimePicker = {},
            onInputChange = {}
        )
    }
}
