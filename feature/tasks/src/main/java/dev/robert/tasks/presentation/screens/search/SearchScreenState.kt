package dev.robert.tasks.presentation.screens.search

import dev.robert.tasks.domain.model.TaskItem

data class SearchScreenState(
    val searchQuery: String = "",
    val isSearchFocused: Boolean = false,
    val searchResults: List<TaskItem> = emptyList(),
    val isSearchResultsLoading: Boolean = false,
    val isSearchResultsError: Boolean = false,
)
