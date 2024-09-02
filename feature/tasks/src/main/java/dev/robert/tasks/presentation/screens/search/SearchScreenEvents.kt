package dev.robert.tasks.presentation.screens.search

sealed class SearchScreenEvents {
    data class Search(val query: String) : SearchScreenEvents()
    object ClearSearch : SearchScreenEvents()
}
