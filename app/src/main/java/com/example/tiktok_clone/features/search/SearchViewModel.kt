package com.example.tiktok_clone.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.features.search.data.SearchRepository
import com.example.tiktok_clone.features.search.model.SearchUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: SearchRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SearchUiState(recentSearches = repository.getRecentSearches()),
    )
    val uiState: StateFlow<SearchUiState> = _uiState

    private var suggestJob: Job? = null

    init {
        refreshDiscover()
    }

    fun refreshDiscover() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDiscoverLoading = true) }
            try {
                val items = repository.discover().items
                _uiState.update { it.copy(discoverItems = items, isDiscoverLoading = false) }
            } catch (_: Exception) {
                _uiState.update { it.copy(isDiscoverLoading = false) }
            }
        }
    }

    fun onQueryChange(text: String) {
        _uiState.update {
            it.copy(
                query = text,
                error = null,
                isSuggestLoading = false,
                suggestions = if (text.isBlank()) emptyList() else it.suggestions,
            )
        }
        if (text.isBlank()) {
            suggestJob?.cancel()
            return
        }
        suggestJob?.cancel()
        suggestJob = viewModelScope.launch {
            delay(280)
            if (_uiState.value.query != text) return@launch
            _uiState.update { it.copy(isSuggestLoading = true) }
            try {
                val result = repository.suggest(text)
                _uiState.update { state ->
                    if (state.query != text) {
                        state.copy(isSuggestLoading = false)
                    } else {
                        state.copy(suggestions = result, isSuggestLoading = false)
                    }
                }
            } catch (_: Exception) {
                _uiState.update { state ->
                    if (state.query != text) {
                        state.copy(isSuggestLoading = false)
                    } else {
                        state.copy(suggestions = emptyList(), isSuggestLoading = false)
                    }
                }
            }
        }
    }

    /**
     * @param skipNavigate true khi đang ở màn kết quả — chỉ tải lại dữ liệu, không điều hướng lại.
     */
    fun onSearchSubmit(text: String, skipNavigate: Boolean = false) {
        val q = text.trim()
        if (q.isEmpty()) return

        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        query = q,
                        suggestions = emptyList(),
                    )
                }

                val res = repository.searchAll(q)
                repository.addRecentSearch(q)

                _uiState.update {
                    it.copy(
                        recentSearches = repository.getRecentSearches(),
                        videos = res.videos.orEmpty(),
                        users = res.users.orEmpty(),
                        products = res.products.orEmpty(),
                        images = res.images.orEmpty(),
                        lives = res.lives.orEmpty(),
                        isLoading = false,
                        navigateToResult = !skipNavigate,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Lỗi tìm kiếm",
                    )
                }
            }
        }
    }

    fun onResultNavigated() {
        _uiState.update { it.copy(navigateToResult = false) }
    }

    fun removeRecent(text: String) {
        repository.removeRecentSearch(text)
        _uiState.update {
            it.copy(recentSearches = repository.getRecentSearches())
        }
    }
}
