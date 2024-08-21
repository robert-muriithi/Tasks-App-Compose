package dev.robert.tasks.presentation.screens.tasks.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.robert.design_system.presentation.components.AdvancedTimePicker
import dev.robert.design_system.presentation.components.CustomDialog
import dev.robert.design_system.presentation.components.TDButton
import dev.robert.design_system.presentation.components.TDFilledTextField
import dev.robert.design_system.presentation.components.TDSpacer
import dev.robert.design_system.presentation.components.TDSurface
import dev.robert.design_system.presentation.utils.convertMillisToDate
import dev.robert.design_system.presentation.utils.formatTimeToAmPm
import dev.robert.tasks.domain.model.TaskCategory
import dev.robert.tasks.presentation.screens.tasks.TasksCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    viewModel: AddTaskViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
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
        onTaskTitleChange = viewModel::onTitleChanged,
        onTaskDescriptionChange = viewModel::onDescriptionChanged,
        onTaskStartDateChange = viewModel::onStartDateChanged,
        onTaskEndDateChange = viewModel::onEndDateChanged,
        onNavigateUp = onNavigateUp,
        onEvent = viewModel::onEvent,
        onTaskStartTimeChanged = viewModel::onTaskStartTimeChanged,
        onTaskEndTimeChanged = viewModel::onTaskEndTimeChanged,
        onInitDatePicker = { showDatePicker = true },
        onInitTimePicker = { timeAction ->
            showTimePicker = true
            action = timeAction
        },
        onSetCategory = viewModel::setSelectCategory
    )

    if (showBottomSheet) AddCategoryBottomSheet(
        onDismiss = {
            showBottomSheet = false
        },
        onCategoryAdded = {
            viewModel.onEvent(AddTaskEvents.GetCategoriesEvent)
        },
        scope = scope,
        sheetState = sheetState
    )

    if (showDatePicker) DatePickerModal(
        onDateSelected = {
            viewModel.onStartDateChanged(convertMillisToDate(it ?: 0))
            showDatePicker = false
        },
        onDismiss = {
            showDatePicker = false
        }
    )

    if (showTimePicker) AdvancedTimePicker(
        onConfirm = {
            when (action) {
                TIME.START_TIME -> viewModel.onTaskStartTimeChanged(formatTimeToAmPm(it))
                TIME.END_TIME -> viewModel.onTaskEndTimeChanged(formatTimeToAmPm(it))
            }
            showTimePicker = false
        },
        onDismiss = {
            showTimePicker = false
        }
    )

    if (showDialog) CustomDialog(
        onConfirm = {
            showDialog = false
            onNavigateUp()
        },
        onDismiss = {
            showDialog = false
        },
        title = if (result.name == ActionResult.Success.name) "Success" else "Error",
        message = if (result.name == ActionResult.Success.name) "Task created successfully" else "An error occurred"
    )
}

enum class TIME {
    START_TIME,
    END_TIME
}

fun showDialogState(result: ActionResult, showDialog: Boolean): @Composable () -> Unit {
    return {
        if (showDialog) {
        }
    }
}

@Composable
fun AddTaskContent(
    uiState: AddTaskState,
    onTaskTitleChange: (String) -> Unit,
    onTaskDescriptionChange: (String) -> Unit,
    onTaskStartDateChange: (String) -> Unit,
    onTaskEndDateChange: (String) -> Unit,
    onNavigateUp: () -> Unit,
    onEvent: (AddTaskEvents) -> Unit,
    onTaskStartTimeChanged: (String) -> Unit,
    onTaskEndTimeChanged: (String) -> Unit,
    onInitDatePicker: () -> Unit,
    onInitTimePicker: (TIME) -> Unit,
    onSetCategory: (TaskCategory) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                AddTaskAppBar(
                    title = "Create New Task",
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
                            onValueChange = onTaskTitleChange,
                            label = "Title",
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            isLoading = uiState.isLoading
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
                            onValueChange = onTaskStartDateChange,
                            label = "Date",
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
        Box(
            modifier = Modifier
                .padding(top = 250.dp)
                .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(
                    color = MaterialTheme.colorScheme.background
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 30.dp)
            ) {

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TDFilledTextField(
                        value = uiState.startTime,
                        onValueChange = onTaskStartTimeChanged,
                        label = "Start time",
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
                        onValueChange = onTaskEndTimeChanged,
                        label = "End time",
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
                    onValueChange = onTaskDescriptionChange,
                    label = "Description",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    maxLines = 4,
                    isLoading = uiState.isLoading
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
                Text("Category")
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
                                    contentDescription = "Add Category",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        else TasksCategory(
                            category = category.name,
                            onClick = {
                                onSetCategory(category)
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
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun AddTaskAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit
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
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
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
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onCategoryAdded: () -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
        }
    }
}

@Preview
@Composable
fun AddTaskScreenPreview() {
    TDSurface {
        AddTaskContent(
            AddTaskState(),
            onTaskDescriptionChange = {},
            onTaskEndDateChange = {},
            onTaskStartDateChange = {},
            onTaskTitleChange = {},
            onNavigateUp = {},
            onEvent = {},
            onTaskStartTimeChanged = {},
            onTaskEndTimeChanged = {},
            onInitDatePicker = {},
            onInitTimePicker = {},
            onSetCategory = {}
        )
    }
}
