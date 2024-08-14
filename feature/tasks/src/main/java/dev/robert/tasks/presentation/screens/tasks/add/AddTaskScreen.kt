package dev.robert.tasks.presentation.screens.tasks.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import dev.robert.design_system.presentation.components.TDFilledTextField
import dev.robert.design_system.presentation.components.TDSurface

@Composable
fun AddTaskScreen(
    viewModel: AddTaskViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AddTaskContent(
        uiState = uiState,
        onTaskTitleChange = viewModel::onTitleChanged,
        onTaskDescriptionChange = viewModel::onDescriptionChanged,
        onTaskStartDateChange = viewModel::onStartDateChanged,
        onTaskEndDateChange = viewModel::onEndDateChanged,
        onNavigateUp = onNavigateUp,
        onEvent = viewModel::onEvent,
        onTaskStartTimeChanged = viewModel::onTaskStartTimeChanged,
        onTaskEndTimeChanged = viewModel::onTaskEndTimeChanged
    )
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
    onTaskEndTimeChanged: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(state = scrollState, orientation = Orientation.Vertical)
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
                    Column(modifier = Modifier.fillMaxWidth().align(Alignment.Center)) {
                        TDFilledTextField(
                            value = uiState.taskTitle,
                            onValueChange = onTaskTitleChange,
                            label = "Title",
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TDFilledTextField(
                            value = uiState.taskStartDate,
                            onValueChange = onTaskStartDateChange,
                            label = "Date",
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            )
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 250.dp)
                .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(
                    color = MaterialTheme.colorScheme.background
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
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
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TDFilledTextField(
                        value = uiState.endTime,
                        onValueChange = onTaskEndTimeChanged,
                        label = "End time",
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                TDFilledTextField(
                    value = uiState.taskDescription,
                    onValueChange = onTaskDescriptionChange,
                    label = "Description",
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    maxLines = 4
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Category")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        onEvent(AddTaskEvents.AddTask)
                    },
                    modifier = Modifier.fillMaxWidth().align(Alignment.End)
                ) {
                    Text("Create Task")
                }
            }
        }
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
            onTaskEndTimeChanged = {}
        )
    }
}
