package com.example.tiktok_clone.features.search.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tiktok_clone.features.search.model.SearchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun onQueryChange(text: String) {
        _uiState.update {
            it.copy(
                query = text,
                suggestions = if (text.isBlank()) emptyList()
                else listOf(
                    "$text live",
                    "$text remix",
                    "$text tiktok",
                    "$text cover"
                )
            )
        }
    }

    fun onSearchSubmit(text: String) {
        _uiState.update {
            it.copy(
                query = text,
                suggestions = emptyList(),
                recentSearches = listOf(text) + it.recentSearches.filter { r -> r != text }
            )
        }
    }

    fun removeRecent(text: String) {
        _uiState.update {
            it.copy(
                recentSearches = it.recentSearches.filter { r -> r != text }
            )
        }
    }
}