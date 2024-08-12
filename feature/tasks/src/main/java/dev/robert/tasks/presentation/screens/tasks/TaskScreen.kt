package dev.robert.tasks.presentation.screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.robert.tasks.domain.model.TaskCategory
import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.presentation.components.CircularProgressbar
import dev.robert.tasks.presentation.components.HomeShimmerLoading

@Composable
fun TaskScreen(
    onNavigateToDetails: (String, Int) -> Unit,
    onNavigateToAddTask: () -> Unit,
    viewModel: TasksViewModel = hiltViewModel(),
) {
    val tasks by viewModel.uiState.collectAsStateWithLifecycle()

    val categories = listOf(
        "All",
        "Completed",
        "In progress",
        "Done"
    )
    val gridState = rememberLazyGridState()

    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(TaskScreenEvents.LoadTasks)
    }

    val showDialog = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeShimmerLoading(
            uiState = tasks,
            successContent = {
                TaskSuccessState(
                    state = tasks,
                    onNavigateToDetails = onNavigateToDetails,
                    gridState = gridState,
                    categories = categories
                )
            },
            errorContent = {
                DialogErrorState(
                    state = tasks,
                    onRetry = {
                        viewModel.onEvent(TaskScreenEvents.LoadTasks)
                        showDialog.value = false
                    },
                    showDialog = showDialog
                )
            },
            emptyContent = {
                TasksEmptyState(
                    state = tasks
                )
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun DialogErrorState(
    modifier: Modifier = Modifier,
    state: TasksScreenState,
    onRetry: () -> Unit,
    showDialog: MutableState<Boolean>
) {
    if (state.error != null) {
        showDialog.value = true
    }

    if (showDialog.value) {
        Dialog(
            properties = DialogProperties(dismissOnBackPress = true),
            onDismissRequest = {
                showDialog.value = false
            },
            content = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .clip(RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Error",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight(800)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.error?.message ?: "An error occurred",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Retry",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                onRetry()
                            }
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun TasksEmptyState(
    state: TasksScreenState,
) {
    if (!state.isLoading && state.error == null && state.tasks.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No tasks available :)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight(800)
            )
        }
    }
}

@Composable
fun TasksLoadingState(
    state: TasksScreenState
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun TaskSuccessState(
    state: TasksScreenState,
    onNavigateToDetails: (String, Int) -> Unit,
    gridState: LazyGridState,
    categories: List<String>
) {
    if (!state.isLoading && state.error == null) {
        TasksList(
            tasks = state.tasks,
            onNavigateToDetails = onNavigateToDetails,
            gridState = gridState,
            categories = categories
        )
    }
}

@Composable
fun TasksList(
    tasks: List<TaskItem>,
    onNavigateToDetails: (String, Int) -> Unit,
    gridState: LazyGridState,
    categories: List<String>
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        state = gridState,
        columns = GridCells.Adaptive(180.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item(
            span = {
                GridItemSpan(maxLineSpan)
            }
        ) {
            AnalyticsSection()
        }
        item(
            span = {
                GridItemSpan(maxLineSpan)
            }
        ) {
            TasksCategories(categories = categories) { category ->
            }
        }
        items(tasks.size) { index: Int ->
            val task = tasks[index]
            TaskCardItem(
                modifier = Modifier,
                onClick = {
                    task.id?.let { it1 -> onNavigateToDetails(task.name, it1) }
                },
                task = task
            )
        }
    }
}

@Composable
fun AnalyticsSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer.copy(0.5f))
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(horizontal = 2.dp)) {
                Text(
                    text = "Congrats, You have 55 completed tasks", style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight(800),
                        textAlign = TextAlign.Start,
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Complete today's task to keep the streak ongoing", style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                        textAlign = TextAlign.Start,
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(MaterialTheme.colorScheme.onSurfaceVariant)
                            .clip(RoundedCornerShape(5.dp))
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(5.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Total tasks: 50",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight(600)
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(MaterialTheme.colorScheme.onSurfaceVariant)
                            .clip(RoundedCornerShape(5.dp))
                            .shadow(
                                elevation = 2.dp
                            )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Complete tasks: 4",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight(600)
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(MaterialTheme.colorScheme.onSurfaceVariant)
                            .clip(RoundedCornerShape(5.dp))
                            .shadow(
                                elevation = 2.dp
                            )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Complete tasks: 4",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight(600)
                    )
                }
            }
            CircularProgressbar(
                text = "60%"
            )
        }
    }
}

@Composable
fun TasksCategories(categories: List<String>, onClick: (String) -> Unit) {
    val selectedCategory = remember { mutableStateOf(categories.firstOrNull()) }
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(
            text = "Categories",
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight(800),
                textAlign = TextAlign.Start,
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(categories.size) { index ->
                TasksCategory(
                    category = categories[index],
                    onClick = {
                        selectedCategory.value = categories[index]
                        onClick(categories[index])
                    },
                    selected = selectedCategory.value == categories[index]
                )
            }
        }
    }
}

@Composable
fun TasksCategory(
    category: String,
    onClick: (String) -> Unit,
    selected: Boolean
) {
    Box(modifier = Modifier
        .clickable {
            onClick(category)
        }
        .clip(RoundedCornerShape(10.dp))
        .background(
            color = if (selected) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.tertiaryContainer.copy(
                alpha = 0.7f
            )
        )
        .padding(8.dp)
    ) {
        Text(
            text = category,
            color = if (selected) Color.White else Color.White.copy(alpha = 0.7f),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp)
        )
    }
}

@Composable
fun TaskCardItem(modifier: Modifier = Modifier, onClick: (TaskItem) -> Unit, task: TaskItem) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable {
                onClick(task)
            }
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)

    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(end = 16.dp)) {
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .width(5.dp)
                    .clip(
                        shape = RoundedCornerShape(
                            bottomEnd = 16.dp,
                            bottomStart = 16.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.onSurfaceVariant)
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = task.name, style = TextStyle(
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        fontWeight = MaterialTheme.typography.labelSmall.fontWeight,

                    ), modifier = Modifier.padding(vertical = 5.dp)
                )
                Text(
                    text = task.name, style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight(800),
                        textAlign = TextAlign.Start,
                    ), modifier = Modifier.padding(bottom = 5.dp)
                )
                Text(
                    text = task.description, style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                        textAlign = TextAlign.Start,
                    ), modifier = Modifier.padding(bottom = 5.dp), overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = task.category?.name ?: "", style = TextStyle(
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        fontWeight = MaterialTheme.typography.labelSmall.fontWeight
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    TaskScreen(
        onNavigateToDetails = { _, _ -> },
        onNavigateToAddTask = {},
    )
}

val taskCategories = listOf(
    TaskCategory(1, "Work", 0xFF4CAF50.toInt()), // Green
    TaskCategory(2, "Personal", 0xFF2196F3.toInt()), // Blue
    TaskCategory(3, "Health", 0xFFF44336.toInt()), // Red
    TaskCategory(4, "Study", 0xFFFF9800.toInt()) // Orange
)
