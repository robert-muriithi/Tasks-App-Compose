package dev.robert.tasks.presentation.screens.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
// import dev.robert.resources.viewmodels.MainActivityViewModel
import dev.robert.tasks.domain.model.TaskItem

@Composable
fun TaskDetailsScreen(
    taskItem: TaskItem,
    viewModel: TaskDetailsViewModel = hiltViewModel(),
//    mainViewModel: MainActivityViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
//    val toggleState by mainViewModel.isToggled.collectAsStateWithLifecycle()

    LaunchedEffect(taskItem) {
        viewModel.initializeTask(taskItem)
    }

    TaskDetailsScreenContent(
        state = state,
        onInputChange = viewModel::onInputChange,
    )
}

@Composable
fun TaskDetailsScreenContent(
    state: TaskDetailsState,
    onInputChange: (OnInputChange) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = FocusRequester()
    var isFocused by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
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
                },
            maxLines = 150,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        )
    }
}
