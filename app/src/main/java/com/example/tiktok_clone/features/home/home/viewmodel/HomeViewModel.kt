package com.example.tiktok_clone.features.home.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.features.home.home.data.HomeUiState
import com.example.tiktok_clone.features.social.model.Post
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialAction
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class HomeViewModel(
    private val socialViewModel: SocialViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                // Load posts
                socialViewModel.loadPosts()
                
                // Get current user (temporary hardcoded user ID)
                val currentUser = socialViewModel.getUser("u1")
                
                // Update UI state
                socialViewModel.uiState.collect { socialState ->
                    _uiState.value = _uiState.value.copy(
                        posts = socialState.posts,
                        currentUser = currentUser,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun refreshPosts() {
        loadHomeData()
    }

    fun onSocialAction(action: SocialAction) {
        socialViewModel.onAction(action)
    }

    fun isFollowing(currentUserId: String, authorId: String): Boolean {
        return socialViewModel.isFollowing(currentUserId, authorId)
    }

    fun loadFriends(userId: String) {
        socialViewModel.loadFriends(userId)
    }
}
