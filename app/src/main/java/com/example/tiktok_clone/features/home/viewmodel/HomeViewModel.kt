package com.example.tiktok_clone.features.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.post.data.repository.UploadRepository
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.user.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val postRepository: UploadRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    // Map of userId -> User (author info from Firebase)
    private val _users = MutableStateFlow<Map<String, User>>(emptyMap())
    val users: StateFlow<Map<String, User>> = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Pagination state
    private var currentPage = 1
    private val pageSize = 10
    private var hasMore = true
    private var isLoadingMore = false

    init {
        loadFeed()
    }

    // Tai feed lan dau (co pagination state)
    private fun loadFeed() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            currentPage = 1
            hasMore = true
            try {
                val loadedPosts = postRepository.getPosts(page = currentPage, pageSize = pageSize)
                    .filter { it.mediaUrl.isNotBlank() }  // Skip posts still processing
                _posts.value = loadedPosts.sortedBy { it.id }

//                // Load postState ngay khi load được 2 post đầu tiên
//                loadedPosts.take(1).forEach { post ->
//                    socialViewModel.loadPostState(post.id.toString())
//                }
                // Fetch users for all unique userIds in the posts
                val userIds = loadedPosts.map { it.userId }.distinct()
                if (userIds.isNotEmpty()) {
                    val usersMap = userRepository.getUsersByIds(userIds)
                    _users.value = usersMap
                }

                hasMore = loadedPosts.size >= pageSize
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load feed"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** Load the next page and append to the current list. */
    // Load them page tiep theo va append vao list
    fun loadMore() {
        if (isLoadingMore || !hasMore) return
        isLoadingMore = true

        viewModelScope.launch {
            try {
                val nextPage = currentPage + 1
                val newPosts = postRepository.getPosts(page = nextPage, pageSize = pageSize)

                if (newPosts.isNotEmpty()) {
                    _posts.value = (_posts.value + newPosts).sortedBy { it.id }
                    currentPage = nextPage

                    // Fetch any new users we haven't seen yet
                    val existingUserIds = _users.value.keys
                    val newUserIds = newPosts.map { it.userId }.distinct()
                        .filter { it !in existingUserIds }
                    if (newUserIds.isNotEmpty()) {
                        val newUsers = userRepository.getUsersByIds(newUserIds)
                        _users.value = _users.value + newUsers
                    }
                }

                hasMore = newPosts.size >= pageSize
            } catch (e: Exception) {
                // Silently fail pagination — don't overwrite the main error
            } finally {
                isLoadingMore = false
            }
        }
    }

    // Reload feed tu dau
    fun refreshPosts() {
        loadFeed()
    }

    // Forward social action (hien dang khong dung)
    fun onSocialAction(action: SocialAction) {
//        socialViewModel.onAction(action)
    }

    // Check follow status (hien dang chua implement)
    fun isFollowing(currentUserId: String, authorId: String): Boolean {
//        return socialViewModel.isFollowing(currentUserId, authorId)
        return TODO("Provide the return value")
    }

    // Load danh sach friend (hien dang khong dung)
    fun loadFriends(userId: String) {
//        socialViewModel.loadFriends(userId)
    }
}
