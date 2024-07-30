package dev.robert.tasks.presentation.screens.tasks.add

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class AddTaskViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AddTaskState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: AddTaskEvents) = when (event) {
        AddTaskEvents.AddTask -> addTask()
    }

    private fun addTask() {}
}
