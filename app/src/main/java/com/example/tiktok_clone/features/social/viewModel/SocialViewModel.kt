package com.example.tiktok_clone.features.social.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.core.network.RealtimeWebSocketClient
import com.example.tiktok_clone.features.post.ui.UploadState
import com.example.tiktok_clone.features.social.data.FollowCountResponse
import com.example.tiktok_clone.features.social.data.FollowUserResponse
import com.example.tiktok_clone.features.social.data.PostStateResponse
import com.example.tiktok_clone.features.social.data.SocialRepository
import com.example.tiktok_clone.features.social.data.model.Comment
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.user.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class SocialViewModel(
    private val socialRepository: SocialRepository,
    private val userRepository: UserRepository,
    okHttpClient: OkHttpClient,
) : ViewModel() {

    // WS cho post feed (like/save/share/comment)
    private val postWsClient = RealtimeWebSocketClient(okHttpClient)
    // WS cho follow (tách riêng để có thể mở đồng thời với post WS)
    private val userWsClient = RealtimeWebSocketClient(okHttpClient)

    private val commentPageSize = 200

    // region Upload / API state
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState = _uploadState.asStateFlow()

    // region Post state
    private val _postStates = MutableStateFlow<Map<String, PostStateResponse>>(emptyMap())
    val postStates = _postStates.asStateFlow()

    // region Current user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // region Friends / follow
    private val _friends = MutableStateFlow<List<FollowUserResponse>>(emptyList())
    val friends = _friends.asStateFlow()

    private val _following = MutableStateFlow<Set<String>>(emptySet())
    val following = _following.asStateFlow()

    private val _followers = MutableStateFlow<Set<String>>(emptySet())
    val followers = _followers.asStateFlow()

    private val _followCounts = MutableStateFlow<FollowCountResponse?>(null)
    val followCounts = _followCounts.asStateFlow()

    // region Share / report UI
    private val _selectedFriendShare = MutableStateFlow<Set<String>>(emptySet())
    val selectedFriendShare: StateFlow<Set<String>> = _selectedFriendShare.asStateFlow()

    private val _showShareSheet = MutableStateFlow(false)
    private val _showReportSheet = MutableStateFlow(false)

    // region Comments
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments = _comments.asStateFlow()

    private val _commentHasMore = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val commentHasMore = _commentHasMore.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getAllUsers()
        }
    }

    fun getCurrentUser(userId: String) {
        viewModelScope.launch {
            try {
                _currentUser.value =
                    userRepository.getUser(userId) ?: throw Exception("User not found")
            } catch (_: Exception) {
            }
        }
    }

    // region Action
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
            is SocialAction.OpenReportOption -> openReportSheet()
            is SocialAction.Follow -> follow(action.authorId)
            is SocialAction.SelectedFriendShare -> onSelectedFriendShare(action.friendId)
            is SocialAction.LoadComment -> loadComments(action.postId, force = true)
            is SocialAction.LikePost -> likePost(action.postId)
            is SocialAction.SavePost -> savePost(action.postId)
            is SocialAction.SharePost -> sharePost(action.postId)
            else -> {}
        }
    }

    // region Share / report
    fun openReportSheet() {
        _showReportSheet.value = true
        _showShareSheet.value = false
    }

    fun clearSelectedFriendShare() {
        _selectedFriendShare.value = emptySet()
    }

    fun onSelectedFriendShare(friendId: String) {
        _selectedFriendShare.update { current ->
            if (friendId in current) current - friendId else current + friendId
        }
    }

    // region Comments
    fun loadComments(postId: String, force: Boolean = false) {
        if (!force && _comments.value.any { it.postId == postId }) return
        viewModelScope.launch {
            try {
                val remote = socialRepository.getComments(
                    postId = postId,
                    limit = commentPageSize,
                    offset = 0,
                )
                _comments.update { current ->
                    current.filterNot { it.postId == postId } + remote
                }
                _commentHasMore.update { current ->
                    current + (postId to (remote.size == commentPageSize))
                }
            } catch (_: Exception) {
            }
        }
    }

    fun getComment(postId: String): List<Comment> =
        comments.value.filter { it.postId == postId }

    fun loadMoreComments(postId: String) {
        val offset = getComment(postId).size
        viewModelScope.launch {
            try {
                val remote = socialRepository.getComments(
                    postId = postId,
                    limit = commentPageSize,
                    offset = offset,
                )
                if (remote.isEmpty()) {
                    _commentHasMore.update { it + (postId to false) }
                } else {
                    _comments.update { current ->
                        val existingIds = current.map { it.id }.toSet()
                        current + remote.filter { it.id !in existingIds }
                    }
                    _commentHasMore.update { it + (postId to (remote.size == commentPageSize)) }
                }
            } catch (_: Exception) {
            }
        }
    }

    fun getReply(commentId: String): List<Comment> =
        comments.value.filter { it.parentId == commentId }

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
            } catch (_: Exception) {
                _comments.update { it.filterNot { c -> c.id == tempId } }
            }
        }
    }

    fun addCommentWithImage(
        postId: String,
        commentText: String,
        parentId: String? = null,
        file: java.io.File,
    ) {
        viewModelScope.launch {
            try {
                _uploadState.value = UploadState.Loading
                socialRepository.addCommentWithImage(
                    postId = postId,
                    file = file,
                    content = commentText.ifBlank { null },
                    parentId = parentId,
                )
                _uploadState.value = UploadState.Success
                loadComments(postId, force = true)
                loadPostState(postId, force = true)
            } catch (_: Exception) {
                _uploadState.value = UploadState.Idle
            }
        }
    }

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
                val isNowLiked = _comments.value.firstOrNull { it.id == commentId }?.isLiked ?: false
                if (isNowLiked) socialRepository.likeComment(commentId)
                else socialRepository.unlikeComment(commentId)
            } catch (_: Exception) {
                _comments.value = before
            }
        }
    }

    // region Post state
    fun loadPostState(postId: String, force: Boolean = false) {
        if (!force && _postStates.value.containsKey(postId)) return
        viewModelScope.launch {
            try {
                val state = socialRepository.getPostState(postId)
                setPostState(postId, state)
                loadComments(postId, force = true)
            } catch (_: Exception) {
            }
        }
    }

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
                if (!wasLiked) socialRepository.likePost(postId) else socialRepository.unlikePost(postId)
            } catch (_: Exception) {
                setPostState(postId, stateBefore)
            }
        }
    }

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
                if (!wasSaved) socialRepository.savePost(postId) else socialRepository.unSavePost(postId)
            } catch (_: Exception) {
                setPostState(postId, stateBefore)
            }
        }
    }

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
                if (!wasShared) socialRepository.sharePost(postId) else socialRepository.unSharePost(postId)
            } catch (_: Exception) {
                setPostState(postId, stateBefore)
            }
        }
    }

    private fun setPostState(postId: String, state: PostStateResponse) {
        _postStates.update { current -> current + (postId to state) }
    }

    private fun updateExistingPostState(
        postId: String,
        transform: (PostStateResponse) -> PostStateResponse,
    ) {
        _postStates.update { current ->
            val existing = current[postId] ?: return@update current
            current + (postId to transform(existing))
        }
    }

    // region Follow / friends
    fun follow(authorId: String) {
        val wasFollowing = isFollowing(authorId)
        _following.update { current ->
            if (wasFollowing) current - authorId else current + authorId
        }
        viewModelScope.launch {
            try {
                if (wasFollowing) socialRepository.unfollowUser(authorId)
                else socialRepository.followUser(authorId)
            } catch (_: Exception) {
                _following.update { current ->
                    if (wasFollowing) current + authorId else current - authorId
                }
            }
        }
    }

    fun loadFollowers(authorId: String) {
        viewModelScope.launch {
            try {
                _followers.value = socialRepository.getFollowers(authorId).map { it.uid }.toSet()
            } catch (_: Exception) {
            }
        }
    }

    fun loadFollowing(authorId: String) {
        viewModelScope.launch {
            try {
                _following.value = socialRepository.getFollowing(authorId).map { it.uid }.toSet()
            } catch (_: Exception) {
            }
        }
    }

    fun isFollowing(authorId: String): Boolean = authorId in _following.value

    fun getFriends(currentUserId: String) {
        viewModelScope.launch {
            try {
                val followers = socialRepository.getFollowers(currentUserId)
                val following = socialRepository.getFollowing(currentUserId)
                _friends.value = (followers + following)
                    .distinctBy { it.uid }
                    .filter { it.uid != currentUserId }
            } catch (_: Exception) {
            }
        }
    }

    // region User helpers
    fun getUser(userId: String): User =
        userRepository.getCachedUser(userId) ?: User(id = userId, userName = userId)

    fun getUserList(userIds: List<String>): List<User> =
        userIds.map { id -> userRepository.getCachedUser(id) ?: User(id = id, userName = id) }

    // region Realtime WebSocket

    fun loadFollowCounts(uid: String) {
        viewModelScope.launch {
            try {
                _followCounts.value = socialRepository.getFollowCounts(uid)
            } catch (_: Exception) { }
        }
    }

    /**
     * Gọi khi post hiện tại trên feed thay đổi (pager scroll).
     * Đóng WS post cũ, mở WS mới cho [postId].
     */
    fun connectPostRealtime(postId: String) {
        viewModelScope.launch {
            postWsClient.disconnect()
            postWsClient.connect("api/v1/ws/social/posts/$postId") { event, _ ->
                viewModelScope.launch {
                    when (event) {
                        "post_state_changed" -> loadPostState(postId, force = true)
                        "comments_changed" -> {
                            loadComments(postId, force = true)
                            loadPostState(postId, force = true)
                        }
                    }
                }
            }
        }
    }

    /** Gọi khi rời khỏi feed hoặc đổi post focus. */
    fun disconnectPostRealtime() {
        postWsClient.disconnect()
    }

    /**
     * Mở WS theo dõi follow changes của [uid].
     * Gọi khi vào màn profile / danh sách followers/following.
     * Nhận event=follow_changed → refetch counts + danh sách.
     */
    fun connectUserRealtime(uid: String) {
        viewModelScope.launch {
            userWsClient.disconnect()
            userWsClient.connect("api/v1/ws/social/users/$uid") { event, _ ->
                viewModelScope.launch {
                    if (event == "follow_changed") {
                        loadFollowCounts(uid)
                        loadFollowers(uid)
                        loadFollowing(uid)
                    }
                }
            }
        }
    }

    /** Gọi khi rời màn profile / follow list. */
    fun disconnectUserRealtime() {
        userWsClient.disconnect()
    }

    override fun onCleared() {
        super.onCleared()
        postWsClient.disconnect()
        userWsClient.disconnect()
    }
}
