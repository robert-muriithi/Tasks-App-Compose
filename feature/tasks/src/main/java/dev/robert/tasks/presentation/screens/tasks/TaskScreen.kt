package dev.robert.tasks.presentation.screens.tasks

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.robert.design_system.presentation.components.CustomDialog
import dev.robert.design_system.presentation.components.DialogType
import dev.robert.tasks.R
import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.presentation.components.CircularProgressbar
import dev.robert.tasks.presentation.components.HomeShimmerLoading
import java.util.Date

@Composable
fun TaskScreen(
    onNavigateToDetails: (TaskItem) -> Unit,
    viewModel: TasksViewModel = hiltViewModel(),
) {
    val tasks by viewModel.uiState.collectAsStateWithLifecycle()

    val gridState = rememberLazyGridState()

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(TaskScreenEvents.LoadTasks)
    }

    val scrollState = rememberScrollState()
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
                    categories = tasks.category.map { it?.name ?: "" },
                    onFilter = { category ->
                        viewModel.onEvent(TaskScreenEvents.FilterTasks(category))
                    }
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
                    state = tasks,
                    scrollState = scrollState
                )
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun DialogErrorState(
    state: TasksScreenState,
    onRetry: () -> Unit,
    showDialog: MutableState<Boolean>
) {
    if (state.error != null) {
        showDialog.value = true
    }

    if (showDialog.value) {
        CustomDialog(
            onDismiss = { showDialog.value = false },
            onConfirm = onRetry,
            title = stringResource(R.string.error),
            message = state.error?.message ?: stringResource(R.string.an_error_occurred),
            showCancel = false,
            type = DialogType.ERROR
        )
    }
}

@Composable
fun TasksEmptyState(
    state: TasksScreenState,
    scrollState: ScrollState
) {
    if (!state.isLoading && state.error == null && state.tasks.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AnalyticsSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(16.dp),
                state = state
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.empty_state_image),
                        contentDescription = stringResource(R.string.empty_state_image),
                        modifier = Modifier.size(250.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.no_tasks_available_add_a_task_to_get_started),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight(800)),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun TaskSuccessState(
    state: TasksScreenState,
    onNavigateToDetails: (TaskItem) -> Unit,
    gridState: LazyGridState,
    categories: List<String>?,
    onFilter: (String) -> Unit
) {
    if (!state.isLoading && state.error == null) {
        TasksList(
            state = state,
            onNavigateToDetails = onNavigateToDetails,
            gridState = gridState,
            categories = categories,
            onFilter = onFilter
        )
    }
}

@Composable
fun TasksList(
    state: TasksScreenState,
    onNavigateToDetails: (TaskItem) -> Unit,
    gridState: LazyGridState,
    categories: List<String>?,
    onFilter: (String) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        state = gridState,
        columns = GridCells.Adaptive(180.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        item(
            span = {
                GridItemSpan(maxLineSpan)
            }
        ) {
            AnalyticsSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(16.dp),
                state = state
            )
        }
        item(
            span = {
                GridItemSpan(maxLineSpan)
            }
        ) {
            TasksCategories(categories = categories) { category ->
                onFilter(category)
            }
        }
        items(
            count = state.tasks.size,
            key = { index -> state.tasks[index].id.toString() }
        ) { index: Int ->
            val task = state.tasks[index]
            TaskCardItem(
                modifier = Modifier,
                onClick = {
                    onNavigateToDetails(task)
                },
                task = task
            )
        }
    }
}

@Composable
fun AnalyticsSection(
    modifier: Modifier,
    state: TasksScreenState
) {
    val completeTask = state.tasks.count { it.isComplete }
    val todaysCompleteTasks = state.tasks.count { it.completionDate == Date().toString() }
    Box(modifier = modifier) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp)
            ) {
                Text(
                    text = "Congrats, You have ${state.tasks.count { it.isComplete }} completed tasks",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight(800),
                        textAlign = TextAlign.Start,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Complete today's task to keep the streak ongoing", style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                        textAlign = TextAlign.Start,
                        color = Color.White
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
                            .background(color = Color.White)
                            .clip(RoundedCornerShape(5.dp))
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(5.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Total tasks: ${state.tasks.size}",
                        style = MaterialTheme.typography.titleSmall.copy(color = Color.White),
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
                            .background(color = Color.White)
                            .clip(RoundedCornerShape(5.dp))
                            .shadow(
                                elevation = 2.dp
                            )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = stringResource(R.string.today_s_complete_tasks,
                            state.tasks.count { it.completionDate == Date().toString() }
                        ),
                        style = MaterialTheme.typography.titleSmall.copy(color = Color.White),
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
                            .background(color = Color.White)
                            .clip(RoundedCornerShape(5.dp))
                            .shadow(
                                elevation = 2.dp
                            )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = stringResource(
                            R.string.total_incomplete_tasks,
                            state.tasks.count { !it.isComplete }),
                        style = MaterialTheme.typography.titleSmall.copy(color = Color.White),
                        fontWeight = FontWeight(600)
                    )
                }
            }
            val percentage =
                if (state.tasks.isNotEmpty())
                    (completeTask.toFloat() / state.tasks.size.toFloat()) * 100
                else 0f
            CircularProgressbar(
                dataUsage = percentage,
            )
        }
    }
}

@Composable
fun TasksCategories(categories: List<String>?, onClick: (String) -> Unit) {
    val selectedCategory = remember { mutableStateOf(categories?.firstOrNull()) }
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(
            text = "Categories",
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight(800),
                textAlign = TextAlign.Start,
            ),
            modifier = Modifier.padding(vertical = 10.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            categories?.let { cat ->
                items(cat.size) { index ->
                    TasksCategory(
                        category = cat[index],
                        onClick = {
                            selectedCategory.value = cat[index]
                            onClick(cat[index])
                        },
                        selected = selectedCategory.value == cat[index],
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun TasksCategory(
    category: String,
    onClick: (String) -> Unit,
    selected: Boolean,
    modifier: Modifier
) {
    Box(modifier = modifier
        .clickable {
            onClick(category)
        }
        .clip(RoundedCornerShape(10.dp))
        .background(
            color = if (selected) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.secondary.copy(
                alpha = 0.2f
            )
        )
        .padding(8.dp)
    ) {
        Text(
            text = category,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight(800)),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp)
        )
    }
}

@Composable
fun TaskCardItem(modifier: Modifier = Modifier, onClick: (TaskItem) -> Unit, task: TaskItem) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp)
            .clickable {
                onClick(task)
            }
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f))

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(8.dp)
                    .clip(
                        shape = RoundedCornerShape(
                            bottomEnd = 16.dp,
                            bottomStart = 16.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
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
                    text = "Created on ${task.taskDate}", style = TextStyle(
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        fontWeight = MaterialTheme.typography.labelSmall.fontWeight,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                )
                Text(
                    text = task.name, style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight(800),
                        textAlign = TextAlign.Start,
                    ), modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
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
                        fontWeight = FontWeight(800),
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    AnalyticsSection(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        state = TasksScreenState()
    )
}
