package com.example.tiktok_clone.features.social.viewModel

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.post.ui.UploadState
import com.example.tiktok_clone.features.social.data.FollowCountResponse
import com.example.tiktok_clone.features.social.data.FollowUserResponse
import com.example.tiktok_clone.features.social.data.PostStateResponse
import com.example.tiktok_clone.features.social.data.SocialRepository
import com.example.tiktok_clone.features.social.data.model.Comment
import com.example.tiktok_clone.features.social.data.model.ShareCategory
import com.example.tiktok_clone.features.social.data.model.ShareItem
import com.example.tiktok_clone.features.social.data.model.ShareSheetMode
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.fakeData.FakeNotInterestedOption
import com.example.tiktok_clone.features.social.fakeData.FakeReportOptionData
import com.example.tiktok_clone.features.social.fakeData.FakeSpeedOptionData
import com.example.tiktok_clone.features.user.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SocialViewModel(
    private val socialRepository: SocialRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    // region Upload / API state
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState = _uploadState.asStateFlow()

    // region Post + state
    private val _postStates = MutableStateFlow<Map<String, PostStateResponse>>(emptyMap())
    val postStates = _postStates.asStateFlow()

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts = _posts.asStateFlow()

    private val _postLikes = MutableStateFlow<Set<String>>(emptySet())
    val postLikes = _postLikes.asStateFlow()
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // region Comments
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments = _comments.asStateFlow()

    // region Friends / follow
    private val _friends = MutableStateFlow<List<FollowUserResponse>>(emptyList())
    val friends = _friends.asStateFlow()

    private val _following = MutableStateFlow<Set<String>>(emptySet())

    // region Share / report UI
    private val _selectedFriendShare = MutableStateFlow<Set<String>>(emptySet())
    val selectedFriendShare: StateFlow<Set<String>> = _selectedFriendShare.asStateFlow()

    private val _reportOptions = MutableStateFlow(FakeReportOptionData.reportOptions)
    val reportOptions = _reportOptions.asStateFlow()

    private val _notInterestedOptions =
        MutableStateFlow(FakeNotInterestedOption.NotInterestedOptions)
    val notInterestedOptions = _notInterestedOptions.asStateFlow()

    private val _speedOptions = MutableStateFlow(FakeSpeedOptionData.speedOptions)
    val speedOptions = _speedOptions.asStateFlow()

    private val _showShareSheet = MutableStateFlow(false)

    private val _shareSheetMode = MutableStateFlow<ShareSheetMode>(ShareSheetMode.Default)
    val shareSheetMode = _shareSheetMode.asStateFlow()

    private val _showReportSheet = MutableStateFlow(false)

    init {
        // Prefetch toàn bộ Firebase users để cache cho /api/v1/users
        viewModelScope.launch {
            userRepository.getAllUsers()
        }
    }

    fun getCurrentUser(userId: String) {
        viewModelScope.launch {
            try {
                _currentUser.value =
                    userRepository.getUser(userId) ?: throw Exception("User not found")
            } catch (e: Exception) {
            }
        }
    }

    // region Action dispatcher
    fun onAction(action: SocialAction) {
        when (action) {
            is SocialAction.LikeComment -> likeComment(action.commentId)
            is SocialAction.AddComment -> addComment(
                action.postId,
                action.commentText,
                action.userId
            )

            is SocialAction.ClearSelectedFriendShare -> clearSelectedFriendShare()
            is SocialAction.OpenReportOption -> openReportSheet()
            is SocialAction.DismissReportSheet -> dismissReportSheet()
            is SocialAction.Follow -> follow(action.authorId)
            is SocialAction.SelectedFriendShare -> onSelectedFriendShare(action.friendId)
            is SocialAction.Comment -> loadComments(action.postId, force = true)
            is SocialAction.Like -> likePost(action.postId)
            is SocialAction.Save -> savePost(action.postId)
            is SocialAction.Share -> sharePost(action.postId)
            else -> {}
        }
    }

    // region Share actions
    val shareApps = MutableStateFlow(
        listOf(
            ShareItem("facebook", "Facebook", ShareCategory.APP),
            ShareItem("copy_link", "Copy link", ShareCategory.APP),
            ShareItem("repost", "Repost", ShareCategory.APP),
        )
    )

    val shareReports = MutableStateFlow(
        listOf(
            ShareItem("report", "Report", ShareCategory.REPORT),
            ShareItem("not_interested", "Not interested", ShareCategory.REPORT),
        )
    )

    fun onShareActionClicked(item: ShareItem) {
        _shareSheetMode.value = when (item.id) {
            "report" -> ShareSheetMode.Report
            "not_interested" -> ShareSheetMode.NotInterested
            "speed" -> ShareSheetMode.Speed
            else -> ShareSheetMode.Default
        }
    }

    fun openReportSheet() {
        _showReportSheet.value = true
        _showShareSheet.value = false
    }

    fun dismissReportSheet() {
        _shareSheetMode.value = ShareSheetMode.Default
    }

    fun clearSelectedFriendShare() {
        _selectedFriendShare.value = emptySet()
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

    // region Comments
    fun loadComments(postId: String, force: Boolean = false) {
        if (!force && _comments.value.any { it.postId == postId }) return
        viewModelScope.launch {
            try {
                val remote = socialRepository.getComments(postId)
                _comments.update { current ->
                    current.filterNot { it.postId == postId } + remote
                }
            } catch (_: Exception) {
            }
        }
    }

    fun getComment(postId: String): List<Comment> {
        return comments.value.filter { it.postId == postId }
    }

    fun getReply(commentId: String): List<Comment> {
        return comments.value.filter { it.parentId == commentId }
    }

    fun likeComment(commentId: String) {
        val before = _comments.value
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
                        likeCount = newCount,
                    )
                } else {
                    comment
                }
            }
        }

        viewModelScope.launch {
            try {
                val isNowLiked =
                    _comments.value.firstOrNull { it.id == commentId }?.isLiked ?: false
                if (isNowLiked) {
                    socialRepository.likeComment(commentId)
                } else {
                    socialRepository.unlikeComment(commentId)
                }
            } catch (e: Exception) {
                // rollback nếu lỗi
                _comments.value = before
            }
        }
    }

    fun loadPostState(postId: String, force: Boolean = false) {
        if (!force && _postStates.value.containsKey(postId)) return
        viewModelScope.launch {
            try {
                val state = socialRepository.getPostState(postId)
                _postStates.update { it + (postId to state) }
            } catch (_: Exception) {
            }
        }
    }

    fun likePost(postId: String) {
        val stateBefore = _postStates.value[postId] ?: return
        val wasLiked = stateBefore.isLiked

        _postStates.update { current ->
            val existing = current[postId] ?: return@update current
            current + (postId to existing.copy(
                isLiked = !wasLiked,
                likeCount = if (!wasLiked) existing.likeCount + 1
                else (existing.likeCount - 1).coerceAtLeast(0)
            ))
        }

        viewModelScope.launch {
            try {
                if (!wasLiked) socialRepository.likePost(postId)
                else socialRepository.unlikePost(postId)
            } catch (e: Exception) {
                _postStates.update { it + (postId to stateBefore) }
            }
        }
    }

    fun savePost(postId: String) {
        val stateBefore = _postStates.value[postId] ?: return
        val wasSaved = stateBefore.isSaved

        _postStates.update { current ->
            val existing = current[postId] ?: return@update current
            current + (postId to existing.copy(
                isSaved = !wasSaved,
                saveCount = if (!wasSaved) existing.saveCount + 1
                else (existing.saveCount - 1).coerceAtLeast(0)
            ))
        }

        viewModelScope.launch {
            try {
                if (!wasSaved) socialRepository.savePost(postId)
                else socialRepository.unSavePost(postId)
            }
            catch (e: Exception) {
                _postStates.update { it + (postId to stateBefore) }
            }
        }
    }

    fun sharePost(postId: String) {
        val stateBefore = _postStates.value[postId] ?: return
        val wasShared = stateBefore.isShared
        _postStates.update { current ->
            val existing = current[postId] ?: return@update current
            current + (postId to existing.copy(
                isShared = !wasShared,
                shareCount = if (!wasShared) existing.shareCount + 1
                else (existing.shareCount - 1).coerceAtLeast(0)
            ))
        }
        viewModelScope.launch {
            try {
                if(!wasShared) socialRepository.sharePost(postId)
                else socialRepository.unSharePost(postId)
            }
            catch (e: Exception) {
                _postStates.update { it + (postId to stateBefore) }
            }
        }
    }

    // region Follow / friends
    fun follow(authorId: String) {
        val currentlyFollowing = isFollowing(authorId)
        _following.update { current ->
            if (currentlyFollowing) current - authorId else current + authorId
        }
        viewModelScope.launch {
            try {
                if (currentlyFollowing) {
                    socialRepository.unfollowUser(authorId)
                } else {
                    socialRepository.followUser(authorId)
                }
            } catch (e: Exception) {
                // rollback nếu lỗi
                _following.update { current ->
                    if (currentlyFollowing) current + authorId else current - authorId
                }
            }
        }
    }

    fun isFollowing(authorId: String): Boolean {
        return _following.value.contains(authorId)
    }

    fun getFriends(currentUserId: String) {
        viewModelScope.launch {
            try {
                val followers = socialRepository.getFollowers(currentUserId)
                val following = socialRepository.getFollowing(currentUserId)
                val allFriends = (followers + following)
                    .distinctBy { it.uid }
                    .filter { it.uid != currentUserId }
                _friends.value = allFriends
            } catch (e: Exception) {
            }
        }
    }

    // region User helpers
    fun getUser(userId: String): User {
        return userRepository.getCachedUser(userId)
            ?: User(id = userId, userName = userId)
    }

    fun getUserList(userIds: List<String>): List<User> {
        return userIds.map { id ->
            userRepository.getCachedUser(id) ?: User(id = id, userName = id)
        }
    }

    // region Private helpers
    private fun addComment(postId: String, commentText: String, userId: String) {
        if (commentText.isBlank()) return
        val tempId = "temp_${System.currentTimeMillis()}"
        val pendingComment = Comment(
            id = tempId,
            postId = postId,
            userId = userId,
            content = commentText,
            createdAt = System.currentTimeMillis(),
        )

        _comments.update { it -> listOf(pendingComment) + it }
        _postStates.update { current ->
            val existing = current[postId] ?: return@update current
            current + (postId to existing.copy(
                commentCount = existing.commentCount + 1
            ))
        }

        viewModelScope.launch {
            try {
                _uploadState.value = UploadState.Loading
                val created = socialRepository.addComment(
                    postId = postId,
                    content = commentText,
                    parentId = null,
                )
                _uploadState.value = UploadState.Success
                _comments.update { current ->
                    val withoutTemp = current.filterNot { it.id == tempId }
                    listOf(created) + withoutTemp.filterNot { it.id == created.id }
                }
                loadComments(postId, force = true)
                loadPostState(postId, force = true)
            } catch (e: Exception) {
                _comments.update { currentComments ->
                    currentComments.filterNot { it.id == tempId }
                }
            }
        }
    }
}
