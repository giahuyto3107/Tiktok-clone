package com.example.tiktok_clone.features.social.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.R
import com.example.tiktok_clone.features.social.fakeData.FakeAppData
import com.example.tiktok_clone.features.social.fakeData.FakeCommentData
import com.example.tiktok_clone.features.social.fakeData.FakeShareAcctionData
import com.example.tiktok_clone.features.social.fakeData.FakeFriendData
import com.example.tiktok_clone.features.social.fakeData.FakeNotInterestedOption
import com.example.tiktok_clone.features.social.fakeData.FakeReportOptionData
import com.example.tiktok_clone.features.social.fakeData.FakeSpeedOptionData
import com.example.tiktok_clone.features.social.fakeData.FakeUserData
import com.example.tiktok_clone.features.social.model.Comment
import com.example.tiktok_clone.features.social.ui.SocialUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.tiktok_clone.features.social.model.Post
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SocialViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SocialUiState())
    val uiState = _uiState.asStateFlow()
    private val _comments = MutableStateFlow(FakeCommentData.comments)
    val comments = _comments.asStateFlow()

    private val _user = MutableStateFlow(FakeUserData.user)
    val user = _user.asStateFlow()

    private val _friends = MutableStateFlow(FakeFriendData.Friends)
    val friends = _friends.asStateFlow()

    private val _apps = MutableStateFlow(FakeAppData.apps)
    val apps = _apps.asStateFlow()
    private val _selectedFriendShare = MutableStateFlow<Set<String>>(emptySet())
    val selectedFriendShare: StateFlow<Set<String>> = _selectedFriendShare.asStateFlow()

    private val _shareAcctions = MutableStateFlow(FakeShareAcctionData.shareAcctions)
    val shareAcctions = _shareAcctions.asStateFlow()

    private val _reportOptions = MutableStateFlow(FakeReportOptionData.reportOptions)
    val reportOptions = _reportOptions.asStateFlow()
    private val _notInterestedOptions =
        MutableStateFlow(FakeNotInterestedOption.NotInterestedOptions)
    val notInterestedOptions = _notInterestedOptions.asStateFlow()
    private val _speedOptions = MutableStateFlow(FakeSpeedOptionData.speedOptions)
    val speedOptions = _speedOptions.asStateFlow()


    init {
        loadPosts()
    }

    private fun loadPosts() {

    }

    fun onAction(action: SocialAction) {
        when (action) {
            is SocialAction.LikeComment -> handleLikeComment(action.commentId)
            is SocialAction.Like -> handleLike(action.postId)
            is SocialAction.Comment -> handleComment(action.postId)
            is SocialAction.Share -> handleShare(action.postId)
            is SocialAction.Save -> handleSave(action.postId)
            is SocialAction.Follow -> handleFollow(action.userId)

            is SocialAction.DismissCommentSheet -> dismissCommentSheet(action.postId)
            is SocialAction.DismissShareSheet -> dismissShareSheet(action.postId)

            is SocialAction.Refresh -> handleRefresh()
            is SocialAction.LoadMore -> handleLoadMore()

            is SocialAction.Retry -> handleRetry()
            is SocialAction.DismissError -> handleDismissError()

            is SocialAction.SelectedFriendShare -> onSelectedFriendShare(action.friendId)
            is SocialAction.ClearSelectedFriendShare -> clearSelectedFriendShare()
            is SocialAction.AddComment -> handleAddComment(action.postId, action.commentText)

            else -> {}
        }
    }

    fun onSelectedFriendShare(friendId: String) {
        _selectedFriendShare.update { currentSelectedFriendShare ->
            if (friendId in currentSelectedFriendShare) {
                currentSelectedFriendShare - friendId
            } else {
                currentSelectedFriendShare + friendId
            }
        }
    }

    fun clearSelectedFriendShare() {
        _selectedFriendShare.value = emptySet()
    }

    fun selectedFriendShareCount(): (Int) {
        return _selectedFriendShare.value.size
    }

    fun handleAddComment(postId: String, commentText: String) {
        if (commentText.isBlank()) return
        val currentUser = user.value

        val newComment = Comment(
            id = "comment_${comments.value.size + 1}",
            postId = postId,
            userId = currentUser.userID,
            userName = currentUser.userName,
            avatarUrl = currentUser.avatarUrl,
            commentContent = commentText,
            likeCount = 0,
            isLiked = false,
            createdAt = System.currentTimeMillis(),
        )
        _comments.update { currentComments ->
            currentComments + listOf(newComment)
        }
        _uiState.update { currentState ->
            val updatedPosts = currentState.posts.map { post ->
                if (post.id == postId) {
                    post.copy(commentCount = post.commentCount + 1)
                } else {
                    post
                }
            }
            currentState.copy(posts = updatedPosts)
        }
    }


    private fun handleLikeComment(commentId: String) {
        _comments.update { currentComments ->
            currentComments.map { comment ->
                if (comment.id == commentId) {

                    val newIsLiked = !comment.isLiked

                    val newCount = if (newIsLiked) {
                        comment.likeCount + 1
                    } else {
                        (comment.likeCount - 1).coerceAtLeast(0)
                    }
                    comment.copy(
                        isLiked = newIsLiked,
                        likeCount = newCount
                    )
                } else {
                    comment
                }
            }
        }
    }


    private fun handleLike(postId: String) {
        _uiState.update { currentState ->
            val updatedPosts = currentState.posts.map { post ->
                if (post.id == postId) {
                    val newIsLiked = !post.isLiked
                    val newCount = if (newIsLiked) {
                        post.likeCount + 1
                    } else {
                        post.likeCount - 1
                    }
                    post.copy(
                        isLiked = newIsLiked,
                        likeCount = newCount
                    )

                } else {
                    post
                }
            }
            currentState.copy(posts = updatedPosts)
        }
    }

    private fun handleComment(postId: String) {
        _uiState.update {
            it.copy(
                showCommentSheet = true,
                selectedPostId = postId
            )
        }
        //load comment tu api
    }

    private fun handleShare(postId: String) {
        _uiState.update {
            it.copy(
                showShareSheet = true,
                selectedPostId = postId
            )
        }
        //load save tu api
    }

    private fun handleSave(postId: String) {
        _uiState.update { currentState ->
            val updatedPosts = currentState.posts.map { post ->
                if (post.id == postId) {
                    val newIsSaved = !post.isSaved
                    val newCount = if (newIsSaved) {
                        post.shareCount + 1
                    } else {
                        post.shareCount - 1
                    }
                    post.copy(
                        isSaved = newIsSaved,
                        shareCount = newCount
                    )
                } else {
                    post
                }
            }
            currentState.copy(posts = updatedPosts)
        }
    }

    private fun handleFollow(userId: String) {
        _uiState.update { currentState ->
            val updatedPosts = currentState.posts.map { post ->
                if (post.userId == userId) {
                    post.copy(isFollowing = !post.isFollowing)
                } else {
                    post
                }
            }
            currentState.copy(posts = updatedPosts)
        }
    }

    private fun dismissCommentSheet(postId: String) {
        _uiState.update {
            it.copy(
                showCommentSheet = false,
                selectedPostId = null
            )
        }
    }

    private fun dismissShareSheet(postId: String) {
        _uiState.update {
            it.copy(
                showShareSheet = false,
                selectedPostId = null
            )
        }
    }

    private fun loadPost() {
        _uiState.update {
            it.copy(isLoading = true, error = null)
        }
        try {
            //delay(1000)
            val posts = createMockPosts()
            _uiState.update {
                it.copy(posts = posts, isLoading = false)
            }

        } catch (e: Exception) {
            _uiState.update {
                it.copy(isLoading = false, error = e.message ?: "unknoew error")
            }

        }
    }

    private fun handleRefresh() {
        _uiState.update {
            it.copy(isRefreshing = true, error = null)
        }
        try {
            val posts = createMockPosts()
            _uiState.update {
                it.copy(posts = posts, isRefreshing = false, currentPostIndex = 0)
            }

        } catch (e: Exception) {
            _uiState.update {
                it.copy(isRefreshing = false, error = e.message ?: "unknow error")
            }
        }
    }

    private fun handleLoadMore() {
        if (!_uiState.value.canLoadMore) return
        viewModelScope.launch {
            try {

                val newPosts = createMockPosts()
                _uiState.update { currentState ->
                    currentState.copy(
                        posts = currentState.posts + newPosts,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "unknow error")
                }
            }
        }
    }

    private fun handleRetry() {
        if (_uiState.value.posts.isEmpty()) {
            loadPost()
        } else {
            handleLoadMore()
        }
    }

    private fun handleDismissError() {
        _uiState.update {
            it.copy(error = null)
        }
    }

    private fun createMockPosts(): List<Post> {
        val images = listOf(
            R.drawable.apartment,
            R.drawable.cat,
            R.drawable.cherry_flower,
            R.drawable.city_post_office,
            R.drawable.independence_palace,
            R.drawable.river,
            R.drawable.road,
            R.drawable.street,
            R.drawable.uni,
            R.drawable.video,
        )
        return images.mapIndexed { index, imageRes ->
            Post(
                id = "post_${index + 1}",
                userId = "user_${index + 1}",
                userName = "User ${index + 1}",
                description = "This is post number ${index + 1}",
                thumbnailRes = imageRes,
                isLiked = false,
                likeCount = (1000..10000).random(),
                commentCount = (100..5000).random(),
                isSaved = false,
                saveCount = (50..1000).random(),
                shareCount = (500..5000).random(),
                isFollowing = false
            )

        }
    }


}
