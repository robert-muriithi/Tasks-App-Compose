/*
 * Copyright 2024 Robert Muriithi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.robert.tasks.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.tasks.domain.usecase.SearchUseCase
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenState())
    val uiState = _uiState.asStateFlow()

    private val searchInputChannel = Channel<String>(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            searchInputChannel
                .receiveAsFlow()
                .debounce(500)
                .collect { query ->
                    if (query.isNotBlank()) {
                        search(query)
                    } else {
                        clearSearch()
                    }
                }
        }
    }

    fun onEvent(events: SearchScreenEvents) = when (events) {
        is SearchScreenEvents.Search -> search(events.query)
        is SearchScreenEvents.ClearSearch -> clearSearch()
    }

    private fun onSearchInputChange(inputChange: String) {
        _uiState.update {
            it.copy(searchQuery = inputChange)
        }
        viewModelScope.launch {
            searchInputChannel.send(inputChange)
        }
    }

    fun onInputChange(inputChange: InputChange) = when (inputChange) {
        is InputChange.OnSearchStringChange -> onSearchInputChange(inputChange.query)
    }

    private fun search(query: String) {
        viewModelScope.launch {
            searchUseCase(query).collect { tasks ->
                _uiState.update {
                    it.copy(
                        searchQuery = query,
                        searchResults = tasks
                    )
                }
            }
        }
    }

    private fun clearSearch() {
        _uiState.update {
            it.copy(searchQuery = "", searchResults = emptyList())
        }
    }
}

sealed class InputChange {
    data class OnSearchStringChange(val query: String) : InputChange()
}
