package dev.robert.tasks.presentation.screens.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
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
import dev.robert.design_system.presentation.components.AlertDialog
import dev.robert.design_system.presentation.components.DialogType
import dev.robert.design_system.presentation.components.Option
import dev.robert.design_system.presentation.components.OptionsDialog
import dev.robert.design_system.presentation.components.RotatingSyncIcon
import dev.robert.design_system.presentation.utils.getCurrentDateTime
import dev.robert.design_system.presentation.utils.isInternetAvailable
import dev.robert.tasks.R
import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.presentation.components.CircularProgressbar
import dev.robert.tasks.presentation.components.HomeShimmerLoading
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun TaskScreen(
    onNavigateToDetails: (TaskItem) -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val tasks by viewModel.uiState.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()

    LaunchedEffect(key1 = Unit) {
        val isOnline = withContext(Dispatchers.IO) {
            isInternetAvailable()
        }

        if (tasks.tasks.isEmpty()) {
            viewModel.onEvent(TaskScreenEvents.LoadTasks(isOnline))
        }

        if (isOnline) {
            tasks.tasks.filter { !it.isSynced }.forEach {
                viewModel.onEvent(TaskScreenEvents.SyncTask(it))
            }
        }
//        } else {
//            viewModel.onEvent(TaskScreenEvents.SetOfflineMode(true))
//        }
    }
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeShimmerLoading(
            uiState = tasks,
            successContent = {
                TaskSuccessState(
                    state = tasks,
                    onNavigateToDetails = onNavigateToDetails,
                    categories = tasks.category.map { it?.name ?: "" },
                    onEvent = viewModel::onEvent,
                )
            },
            errorContent = {
                DialogErrorState(
                    state = tasks,
                    onRetry = {
                        viewModel.onEvent(TaskScreenEvents.LoadTasks())
                    },
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
) {
    val showDialog = remember { mutableStateOf(false) }

    if (state.error != null) {
        showDialog.value = true
    }

    if (showDialog.value) {
        AlertDialog(
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
    categories: List<String>?,
    onEvent: (TaskScreenEvents) -> Unit
) {
    val showOptionsDialog = remember { mutableStateOf(false) }
    var selectedTaskItem by remember { mutableStateOf<TaskItem?>(null) }

    val context = LocalContext.current
    var isOnline by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        isOnline = withContext(Dispatchers.IO) {
            isInternetAvailable()
        }
//        if (state.refreshed) Toast.makeText(context, "Data successfully refreshed", Toast.LENGTH_SHORT).show()
    }

    if (!state.isLoading && state.error == null) {
        PullToRefreshLazyVerticalGrid(
            state = state,
            onNavigateToDetails = onNavigateToDetails,
            categories = categories,
            onTaskLongPress = { taskItem ->
                showOptionsDialog.value = true
                selectedTaskItem = taskItem
            },
            onEvent = onEvent,
            onRefresh = {
                onEvent(TaskScreenEvents.RefreshTasks(
                    fetchRemote = isOnline
                ))
            },
            isRefreshing = state.isRefreshing
        )
    }
    if (showOptionsDialog.value)
        OptionsDialog(
            title = stringResource(R.string.options),
            options = listOf(
                Option(
                    text = stringResource(R.string.edit_task),
                    icon = Icons.Default.Edit,
                    onClick = { selectedTaskItem?.let(onNavigateToDetails) },
                ),
                Option(
                    text = stringResource(R.string.mark_task_as_complete),
                    icon = Icons.Default.CheckCircle,
                    onClick = {
                        selectedTaskItem?.let {
                            onEvent(TaskScreenEvents.CompleteTask(it))
                        }
                    },
                    enabled = !selectedTaskItem?.isComplete!!
                ),
                Option(
                    text = stringResource(R.string.sync_task),
                    icon = Icons.Default.Refresh,
                    onClick = {
                        selectedTaskItem?.let {
                            onEvent(TaskScreenEvents.SyncTask(it))
                        }
                    },
                    enabled = !selectedTaskItem?.isSynced!!
                ),
                Option(
                    text = stringResource(R.string.delete_task),
                    icon = Icons.Default.Delete,
                    iconTint = {
                        MaterialTheme.colorScheme.error
                    },
                    textColor = {
                        MaterialTheme.colorScheme.error
                    },
                    onClick = {
                        selectedTaskItem?.let {
                            onEvent(TaskScreenEvents.DeleteTask(it))
                        }
                    }
                )
            ),
            onDismiss = { showOptionsDialog.value = false }
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshLazyVerticalGrid(
    state: TasksScreenState,
    categories: List<String>?,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onNavigateToDetails: (TaskItem) -> Unit,
    onTaskLongPress: (TaskItem) -> Unit,
    onEvent: (TaskScreenEvents) -> Unit,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState()
) {
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            pullToRefreshState.startRefresh()
        } else {
            pullToRefreshState.endRefresh()
        }
    }

    LaunchedEffect(pullToRefreshState.isRefreshing) {
        if (pullToRefreshState.isRefreshing) {
            onRefresh()
        }
    }

    val isGridView = state.isGridView

    Box(
        modifier = modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            state = gridState,
            columns = if (isGridView) GridCells.Adaptive(180.dp) else GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
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
                TasksCategories(
                    categories = categories,
                    onEvent = onEvent,
                    state = state
                )
            }
            items(
                count = state.tasks.size,
                key = { index -> state.tasks[index].id.toString() },
                span = {
                    GridItemSpan(if (isGridView) 1 else 2)
                }
            ) { index ->
                val task = state.tasks[index]
                TaskCardItem(
                    modifier = Modifier,
                    onClick = {
                        onNavigateToDetails(task)
                    },
                    task = task,
                    isGridView = isGridView,
                    onTaskLongPress = onTaskLongPress,
                    syncing = state.syncing,
                    isSycned = task.isSynced,
                )
            }
        }

        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}

@Composable
fun AnalyticsSection(
    modifier: Modifier = Modifier,
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
                    text = stringResource(
                        R.string.congrats_you_have_completed_tasks,
                        state.tasks.count { it.isComplete }),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight(800),
                        textAlign = TextAlign.Start,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(R.string.complete_today_s_task_to_keep_the_streak_ongoing),
                    style = TextStyle(
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
                        text = stringResource(R.string.total_tasks, state.tasks.size),
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
                            state.tasks.count { it.completionDate == getCurrentDateTime().toString() }
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
fun TasksCategories(
    categories: List<String>?,
    state: TasksScreenState,
    onEvent: (TaskScreenEvents) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedCategory = remember { mutableStateOf(categories?.firstOrNull()) }
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                text = stringResource(R.string.categories),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight(800),
                    textAlign = TextAlign.Start,
                ),
                modifier = Modifier.padding(vertical = 10.dp)
            )
            ViewSwapIcon(
                onUpdateGridState = { isGridView ->
                    onEvent(TaskScreenEvents.ToggleGrid(isGridView))
                },
                state = state
            )
        }
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
                            onEvent(TaskScreenEvents.FilterTasks(cat[index]))
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
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .clip(RoundedCornerShape(10.dp))
        .background(
            color = if (selected) MaterialTheme.colorScheme.tertiaryContainer else
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
        )
        .padding(8.dp)
        .clickable {
            onClick(category)
        }
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
fun ViewSwapIcon(
    onUpdateGridState: (Boolean) -> Unit,
    state: TasksScreenState,
    modifier: Modifier = Modifier
) {
    var isGridView by remember { mutableStateOf(true) }
    IconButton(
        onClick = {
            isGridView = !isGridView
            onUpdateGridState(isGridView)
        }
    ) {
        Icon(
            painter = painterResource(id = if (state.isGridView) R.drawable.baseline_grid_view_24 else R.drawable.baseline_list_24),
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCardItem(
    onClick: (TaskItem) -> Unit,
    task: TaskItem,
    isGridView: Boolean,
    onTaskLongPress: (TaskItem) -> Unit,
    syncing: Boolean,
    isSycned: Boolean,
    modifier: Modifier = Modifier,
) {
    val haptics = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
            .combinedClickable(
                onClick = { onClick(task) },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onTaskLongPress(task)
                }
            )

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
                    text = stringResource(R.string.created_on, task.taskDate), style = TextStyle(
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
                    ), modifier = Modifier.padding(bottom = 5.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (isGridView) 7 else 8
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.category?.name ?: "", style = TextStyle(
                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                            fontWeight = FontWeight(800),
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(5.dp)
                    )
                    Column {
                        Row {
                            Image(
                                painter = painterResource(id = if (task.isComplete) R.drawable.baseline_check_circle_24 else R.drawable.baseline_check_circle_outline_24),
                                contentDescription = null,
                                modifier = Modifier.size(10.dp),
                                colorFilter = ColorFilter.tint(
                                    if (task.isComplete) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.3f
                                    )
                                )
                            )
                            Text(
                                text = if (task.isComplete) stringResource(R.string.completed) else stringResource(
                                    R.string.incomplete
                                ),
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                    fontWeight = FontWeight(800),
                                    color = if (task.isComplete) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.3f
                                    )
                                )
                            )
                        }
                        AnimatedVisibility(
                            visible = syncing && !isSycned,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            RotatingSyncIcon(
                                sync = !isSycned
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskScreenPreview() {
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
