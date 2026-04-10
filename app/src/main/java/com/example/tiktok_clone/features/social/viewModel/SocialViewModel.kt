package com.example.tiktok_clone.features.social.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.features.inbox.data.InboxRepository
import com.example.tiktok_clone.features.post.ui.UploadState
import com.example.tiktok_clone.features.social.data.FollowCountResponse
import com.example.tiktok_clone.features.social.data.FollowUserResponse
import com.example.tiktok_clone.features.social.data.PostStateResponse
import com.example.tiktok_clone.features.social.data.SocialRepository
import com.example.tiktok_clone.features.social.data.model.Comment
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.ui.CommentFeedUiState
import com.example.tiktok_clone.features.social.ui.PostFeedUiState
import com.example.tiktok_clone.features.social.ui.SharePickerUiState
import com.example.tiktok_clone.features.social.ui.SocialGraphUiState
import com.example.tiktok_clone.features.user.data.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SocialViewModel(
    private val socialRepository: SocialRepository,
    private val userRepository: UserRepository,
    private val inboxRepository: InboxRepository,
) : ViewModel() {

    // region Core state
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState = _uploadState.asStateFlow()

    private val _postStates = MutableStateFlow<Map<String, PostStateResponse>>(emptyMap())

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _friends = MutableStateFlow<List<FollowUserResponse>>(emptyList())
    val friends = _friends.asStateFlow()

    private val _following = MutableStateFlow<Set<String>>(emptySet())
    val following = _following.asStateFlow()

    private val _followers = MutableStateFlow<Set<String>>(emptySet())
    val followers = _followers.asStateFlow()

    private val _followCountsMap = MutableStateFlow<Map<String, FollowCountResponse>>(emptyMap())
    val followCountsMap: StateFlow<Map<String, FollowCountResponse>> = _followCountsMap.asStateFlow()

    private val _selectedFriendShare = MutableStateFlow<Set<String>>(emptySet())
    val selectedFriendShare = _selectedFriendShare.asStateFlow()

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments = _comments.asStateFlow()

    private val _commentLoading = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val commentLoading = _commentLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    // endregion

    // region Derived UI state
    val postFeedUiState: StateFlow<PostFeedUiState> = _postStates
        .map(::PostFeedUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PostFeedUiState(),
        )

    val commentFeedUiState: StateFlow<CommentFeedUiState> = combine(comments, commentLoading) { list, loadingMap ->
        CommentFeedUiState(comments = list, commentLoading = loadingMap)
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CommentFeedUiState(),
        )

    val socialGraphUiState: StateFlow<SocialGraphUiState> = combine(
        currentUser,
        friends,
        followers,
        following,
    ) { user, friendsList, followersSet, followingSet ->
        SocialGraphUiState(
            currentUser = user,
            friends = friendsList,
            followers = followersSet,
            following = followingSet,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SocialGraphUiState(),
    )

    val sharePickerUiState: StateFlow<SharePickerUiState> = selectedFriendShare
        .map(::SharePickerUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SharePickerUiState(),
        )
    // endregion

    init {
        viewModelScope.launch {
            userRepository.getAllUsers()
        }
    }

    // Load thong tin current user va cap nhat state
    fun getCurrentUser(userId: String) {
        viewModelScope.launch {
            try {
                _currentUser.value =
                    userRepository.getUser(userId) ?: throw Exception("User not found")
            } catch (_: Exception) {
            }
        }
    }

    // Lấy follow counts cho một user (có cache)
    // Load follow counts (co cache)
    fun getFollowCounts(userId: String, force: Boolean = false) {
        if (!force && _followCountsMap.value.containsKey(userId)) return
        viewModelScope.launch {
            try {
                val counts = socialRepository.getFollowCounts(userId)
                _followCountsMap.update { it + (userId to counts) }
            } catch (_: Exception) { }
        }
    }

    // Dieu huong social action tu UI
    fun onAction(action: SocialAction) {
        when (action) {
            is SocialAction.LikeComment -> likeComment(action.commentId)
            is SocialAction.AddComment -> addComment(
                postId = action.postId,
                commentText = action.commentText,
                userId = action.userId,
                parentId = action.parentId,
            )
            is SocialAction.ClearSelectedFriendShare -> clearSelectedFriendShare()
            is SocialAction.Follow -> follow(action.authorId)
            is SocialAction.SelectedFriendShare -> onSelectedFriendShare(action.friendId)
            is SocialAction.LoadComment -> loadComments(action.postId, force = true)
            is SocialAction.LikePost -> likePost(action.postId)
            is SocialAction.SavePost -> savePost(action.postId)
            is SocialAction.SharePost -> sharePost(action.postId)
        }
    }

    // Clear danh sach friend duoc chon khi share
    fun clearSelectedFriendShare() {
        _selectedFriendShare.value = emptySet()
    }

    // Toggle chon friend de share
    fun onSelectedFriendShare(friendId: String) {
        _selectedFriendShare.update { current ->
            if (friendId in current) current - friendId else current + friendId
        }
    }

    // Load comment theo postId
    fun loadComments(postId: String, force: Boolean = false) {
        if (!force && _comments.value.any { it.postId == postId }) return
        viewModelScope.launch {
            _commentLoading.update { it + (postId to true) }
            _error.value = null
            try {
                val remote = socialRepository.getComments(
                    postId = postId,
                    offset = 0,
                )
                _comments.update { current ->
                    current.filterNot { it.postId == postId } + remote
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Lỗi tải bình luận"
            } finally {
                _commentLoading.update { it + (postId to false) }
            }
        }
    }

    // Add comment (optimistic)
    private fun addComment(
        postId: String,
        commentText: String,
        userId: String,
        parentId: String? = null,
    ) {
        if (commentText.isBlank()) return
        val tempId = "temp_${System.currentTimeMillis()}"
        val pendingComment = Comment(
            id = tempId,
            postId = postId,
            userId = userId,
            content = commentText,
            parentId = parentId,
            likeCount = 0,
            isLiked = false,
            createdAt = System.currentTimeMillis(),
        )
        _comments.update { listOf(pendingComment) + it }
        _postStates.update { current ->
            val existing = current[postId] ?: return@update current
            current + (postId to existing.copy(commentCount = existing.commentCount + 1))
        }

        viewModelScope.launch {
            try {
                _error.value = null
                _uploadState.value = UploadState.Loading
                val created = socialRepository.addComment(
                    postId = postId,
                    content = commentText,
                    parentId = parentId,
                )
                _uploadState.value = UploadState.Success
                _comments.update { current ->
                    val withoutTemp = current.filterNot { it.id == tempId }
                    listOf(created) + withoutTemp.filterNot { it.id == created.id }
                }
                loadComments(postId, force = true)
                loadPostState(postId, force = true)
            } catch (e: Exception) {
                _error.value = e.message ?: "Gửi bình luận thất bại"
                _comments.update { it.filterNot { c -> c.id == tempId } }
            }
        }
    }

    // Like/unlike comment (optimistic)
    fun likeComment(commentId: String) {
        val before = _comments.value
        _comments.update { current ->
            current.map { comment ->
                if (comment.id == commentId) {
                    val newIsLiked = !comment.isLiked
                    comment.copy(
                        isLiked = newIsLiked,
                        likeCount = if (newIsLiked) comment.likeCount + 1
                        else (comment.likeCount - 1).coerceAtLeast(0),
                    )
                } else comment
            }
        }
        viewModelScope.launch {
            try {
                val isNowLiked =
                    _comments.value.firstOrNull { it.id == commentId }?.isLiked ?: false
                if (isNowLiked) socialRepository.likeComment(commentId)
                else socialRepository.unlikeComment(commentId)
            } catch (_: Exception) {
                _comments.value = before
            }
        }
    }

    // Load post state (like/save/share/comment count)
    fun loadPostState(postId: String, force: Boolean = false) {
        if (!force && _postStates.value.containsKey(postId)) return
        viewModelScope.launch {
            try {
                _error.value = null
                val state = socialRepository.getPostState(postId)
                setPostState(postId, state)
            } catch (e: Exception) {
                _error.value = e.message ?: "Lỗi tải trạng thái bài viết"
            }
        }
    }

    // Like/unlike post (optimistic)
    fun likePost(postId: String) {
        val stateBefore = _postStates.value[postId] ?: return
        val wasLiked = stateBefore.isLiked
        updateExistingPostState(postId) { existing ->
            existing.copy(
                isLiked = !wasLiked,
                likeCount = if (!wasLiked) existing.likeCount + 1
                else (existing.likeCount - 1).coerceAtLeast(0),
            )
        }
        viewModelScope.launch {
            try {
                if (!wasLiked) socialRepository.likePost(postId) else socialRepository.unlikePost(
                    postId
                )
            } catch (_: Exception) {
                setPostState(postId, stateBefore)
            }
        }
    }

    // Save/unsave post (optimistic)
    fun savePost(postId: String) {
        val stateBefore = _postStates.value[postId] ?: return
        val wasSaved = stateBefore.isSaved
        updateExistingPostState(postId) { existing ->
            existing.copy(
                isSaved = !wasSaved,
                saveCount = if (!wasSaved) existing.saveCount + 1
                else (existing.saveCount - 1).coerceAtLeast(0),
            )
        }
        viewModelScope.launch {
            try {
                if (!wasSaved) socialRepository.savePost(postId) else socialRepository.unSavePost(
                    postId
                )
            } catch (_: Exception) {
                setPostState(postId, stateBefore)
            }
        }
    }

    // Share/unshare post (optimistic)
    fun sharePost(postId: String) {
        val stateBefore = _postStates.value[postId] ?: return
        val wasShared = stateBefore.isShared
        updateExistingPostState(postId) { existing ->
            existing.copy(
                isShared = !wasShared,
                shareCount = if (!wasShared) existing.shareCount + 1
                else (existing.shareCount - 1).coerceAtLeast(0),
            )
        }
        viewModelScope.launch {
            try {
                if (!wasShared) socialRepository.sharePost(postId) else socialRepository.unSharePost(
                    postId
                )
            } catch (_: Exception) {
                setPostState(postId, stateBefore)
            }
        }
    }

    // Set post state vao map
    private fun setPostState(postId: String, state: PostStateResponse) {
        _postStates.update { current -> current + (postId to state) }
    }

    // Update post state neu da co trong map
    private fun updateExistingPostState(
        postId: String,
        transform: (PostStateResponse) -> PostStateResponse,
    ) {
        _postStates.update { current ->
            val existing = current[postId] ?: return@update current
            current + (postId to transform(existing))
        }
    }

    // Follow/unfollow user (optimistic + rollback)
    fun follow(authorId: String) {
        val wasFollowing = isFollowing(authorId)

        _following.update { current ->
            if (wasFollowing) current - authorId else current + authorId
        }

        //Cập nhật follower count trong map (optimistic)
        _followCountsMap.update { map ->
            // Lấy counts hiện tại của user, nếu chưa có thì tạo mới với 0,0
            val currentCounts = map[authorId] ?: FollowCountResponse(0, 0)
            val newFollowerCount = if (wasFollowing)
                (currentCounts.followerCount - 1).coerceAtLeast(0)
            else
                currentCounts.followerCount + 1
            // Tạo bản copy mới với followerCount đã thay đổi, giữ nguyên followingCount
            val updatedCounts = currentCounts.copy(followerCount = newFollowerCount)
            map + (authorId to updatedCounts)
        }

        viewModelScope.launch {
            try {
                if (wasFollowing) socialRepository.unfollowUser(authorId)
                else socialRepository.followUser(authorId)
                // Sau thành công, refresh lại để đồng bộ (tuỳ chọn)
                getFollowCounts(authorId, force = true)
            } catch (_: Exception) {
                // Rollback nếu lỗi
                _following.update { current ->
                    if (wasFollowing) current + authorId else current - authorId
                }
                _followCountsMap.update { map ->
                    val currentCounts = map[authorId] ?: return@update map
                    val rolledBackCount = if (wasFollowing)
                        currentCounts.followerCount + 1
                    else
                        (currentCounts.followerCount - 1).coerceAtLeast(0)
                    val rolledBackCounts = currentCounts.copy(followerCount = rolledBackCount)
                    map + (authorId to rolledBackCounts)
                }
            }
        }
    }

    // Load followers set cua author
    fun loadFollowers(authorId: String) {
        viewModelScope.launch {
            try {
                _followers.value = socialRepository.getFollowers(authorId).map { it.uid }.toSet()
            } catch (_: Exception) {
            }
        }
    }

    // Load following set cua author
    fun loadFollowing(authorId: String) {
        viewModelScope.launch {
            try {
                _following.value = socialRepository.getFollowing(authorId).map { it.uid }.toSet()
            } catch (_: Exception) {
            }
        }
    }

    // Check authorId co nam trong following set khong
    fun isFollowing(authorId: String): Boolean = authorId in _following.value

    // Load danh sach friends/contacts cho inbox header
    fun getFriends(currentUserId: String) {
        viewModelScope.launch {
            try {
                val followers = socialRepository.getFollowers(currentUserId)
                val following = socialRepository.getFollowing(currentUserId)
                val chatWith = inboxRepository.getContacts()
                val chatWithIds = chatWith.map { it.uid }.toHashSet()
                _friends.value =
                    (chatWith + (followers + following).filter { it.uid !in chatWithIds })
                        .filter { it.uid != currentUserId }
            } catch (_: Exception) {
            }
        }
    }

    // Lay user tu cache (fallback id->userName)
    fun getUser(userId: String): User =
        userRepository.getCachedUser(userId) ?: User(id = userId, userName = userId)

    // Lay list user tu cache theo danh sach id
    fun getUserList(userIds: List<String>): List<User> =
        userIds.map { id -> userRepository.getCachedUser(id) ?: User(id = id, userName = id) }

}