package dev.robert.tasks.presentation.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.robert.design_system.presentation.components.TDFilledTextField
import dev.robert.tasks.domain.model.TaskItem
import dev.robert.tasks.presentation.screens.tasks.TaskCardItem

@Composable
fun SearchScreen(
    onNavigateUp: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SearchScreenContent(
        state = uiState,
        onInputChange = viewModel::onInputChange,
        onEvent = viewModel::onEvent,
        onNavigateToDetails = { },
        onNavigateUp = onNavigateUp
    )
}

@Composable
private fun SearchScreenContent(
    state: SearchScreenState,
    onNavigateUp: () -> Unit,
    onNavigateToDetails: (TaskItem) -> Unit,
    onInputChange: (InputChange) -> Unit,
    onEvent: (SearchScreenEvents) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.searchResults.isEmpty() && state.searchQuery.isEmpty())
        Box(modifier = modifier.fillMaxSize()) {
            Text(
                text = "Search for tasks...",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }

    if (state.searchResults.isEmpty() && state.searchQuery.isNotEmpty()) {
        Box(modifier = modifier.fillMaxSize()) {
            Text(
                text = "No tasks found",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        onNavigateUp()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                TDFilledTextField(
                    onValueChange = {
                        onInputChange(InputChange.OnSearchStringChange(it))
                    },
                    value = state.searchQuery,
                    label = "Search for tasks...",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    modifier = Modifier
                        .weight(1f),
                    isLoading = false,
                    trailingIcon = {
                        AnimatedVisibility(visible = state.searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    onEvent(SearchScreenEvents.ClearSearch)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    )
                )
            }
        }
        items(state.searchResults.size) { index ->
            val task = state.searchResults[index]
            TaskCardItem(
                modifier = Modifier.padding(
                    top = 8.dp
                ),
                onClick = {
                    onNavigateToDetails(task)
                },
                task = task,
                isGridView = false,
                onTaskLongPress = {},
                syncing = false,
                isSycned = task.isSynced,
            )
        }
    }
}
