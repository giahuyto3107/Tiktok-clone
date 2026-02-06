package com.example.tiktok_clone.features.social.viewModel

import androidx.lifecycle.ViewModel
import com.example.tiktok_clone.features.social.fakeData.FakeAppData
import com.example.tiktok_clone.features.social.fakeData.FakeCommentData
import com.example.tiktok_clone.features.social.fakeData.FakeFollowData
import com.example.tiktok_clone.features.social.fakeData.FakeNotInterestedOption
import com.example.tiktok_clone.features.social.fakeData.FakeReportOptionData
import com.example.tiktok_clone.features.social.fakeData.FakeShareActionData
import com.example.tiktok_clone.features.social.fakeData.FakeSpeedOptionData
import com.example.tiktok_clone.features.social.fakeData.FakeUserData
import com.example.tiktok_clone.features.social.model.Comment
import com.example.tiktok_clone.features.social.model.Follow
import com.example.tiktok_clone.features.social.ui.SocialUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.tiktok_clone.features.social.model.Post
import com.example.tiktok_clone.features.social.model.ShareSheetMode
import com.example.tiktok_clone.features.social.model.ShareCategory
import com.example.tiktok_clone.features.social.model.ShareItem
import com.example.tiktok_clone.features.social.model.User
import kotlinx.coroutines.flow.StateFlow

// đừng để ý, t ko hiểu cái mẹ gì cả
class SocialViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SocialUiState())
    val uiState = _uiState.asStateFlow()
    private val _comments = MutableStateFlow(FakeCommentData.comments)
    val comments = _comments.asStateFlow()
    private val _posts = MutableStateFlow(FakePostData.posts)
    val posts = _posts.asStateFlow()
    private val _currentPostIndex = MutableStateFlow(0)
    val currentPostIndex = _currentPostIndex.asStateFlow()
    private val _user = MutableStateFlow(FakeUserData.user)
    val user = _user.asStateFlow()

    private val _friends = MutableStateFlow<List<User>>(emptyList())
    val friends = _friends.asStateFlow()


    private val _apps = MutableStateFlow(FakeAppData.apps)
    val apps = _apps.asStateFlow()
    private val _selectedFriendShare = MutableStateFlow<Set<String>>(emptySet())
    val selectedFriendShare: StateFlow<Set<String>> = _selectedFriendShare.asStateFlow()

    private val _shareItems = MutableStateFlow(FakeShareActionData.shareItems)
    val shareItems = _shareItems.asStateFlow()


    private val _reportOptions = MutableStateFlow(FakeReportOptionData.reportOptions)
    val reportOptions = _reportOptions.asStateFlow()
    private val _notInterestedOptions =
        MutableStateFlow(FakeNotInterestedOption.NotInterestedOptions)
    val notInterestedOptions = _notInterestedOptions.asStateFlow()
    private val _speedOptions = MutableStateFlow(FakeSpeedOptionData.speedOptions)
    val speedOptions = _speedOptions.asStateFlow()

    private val _showShareSheet = MutableStateFlow(false)
    val showShareSheet = _showShareSheet.asStateFlow()

    private val _shareSheetMode = MutableStateFlow<ShareSheetMode>(ShareSheetMode.Default)
    val shareSheetMode = _shareSheetMode.asStateFlow()

    private val _follow = MutableStateFlow(FakeFollowData.follows)
    val follow = _follow.asStateFlow()


    private val _showReportSheet = MutableStateFlow(false)
    val showReportSheet = _showReportSheet.asStateFlow()

    init {
        loadPosts()
    }



    fun onAction(action: SocialAction) {
        when (action) {
            is SocialAction.LikeComment -> likeComment(action.commentId)
            is SocialAction.ClearSelectedFriendShare -> clearSelectedFriendShare()
            is SocialAction.OpenReportOption -> openReportSheet()
            is SocialAction.DismissReportSheet -> dismissReportSheet()
            is SocialAction.Follow -> follow(action.userId, action.authorId)
            is SocialAction.SelectedFriendShare -> onSelectedFriendShare(action.friendId)

            else -> {}
        }
    }

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

    private fun getFriends(currentUserId: String): List<User> {
        val following = FakeFollowData.follows
            .filter { it.fromUserId == currentUserId }
            .map { it.toUserId }
            .toSet()
        val followers = FakeFollowData.follows
            .filter { it.toUserId == currentUserId }
            .map { it.fromUserId }
            .toSet()
        val friends = following.intersect(followers)
        return FakeUserData.user.filter { it.id in friends }
    }
    fun follow(userId: String , authorId: String) {
        _follow.update { currentFollow ->
            val isFollowing = currentFollow.any {
                it.fromUserId == userId && it.toUserId == authorId
            }

            if(isFollowing){
                currentFollow.filterNot {
                    it.fromUserId == userId && it.toUserId == authorId
                }
            }else{
                currentFollow + Follow(userId, authorId)
            }
        }
    }

    fun isFollowing(userId: String, authorId: String): Boolean {
        return FakeFollowData.follows.any {
            it.fromUserId == userId && it.toUserId == authorId
        }
    }


    fun loadFriends(currentUserId: String) {
        _friends.value = getFriends(currentUserId)
    }

    fun getComment(postId: String): List<Comment> {
        return comments.value.filter { it.postId == postId }
    }
    fun getReply(commentId: String): List<Comment> {
        return comments.value.filter { it.parentId == commentId }
    }
    fun getUser(userId: String): User {
        return FakeUserData.user.find { it.id == userId }!!
    }
    fun getUserList(userIds: List<String>): List<User> {
        return FakeUserData.user.filter { it.id in userIds }
    }
    fun likeComment(commentId: String) {
        _comments.update { currentComments ->
            currentComments.map {
                if (it.id == commentId) {
                    val newIsLiked = !it.isLiked

                    val newCount = if (newIsLiked)
                        it.likeCount + 1
                    else
                        (it.likeCount - 1).coerceAtLeast(0)
                    it.copy(
                        isLiked = newIsLiked,
                        likeCount = newCount
                    )
                } else
                    it

            }
        }
    }
    private fun getPosts(): List<Post> {
        return _posts.value
    }
    fun getCurrentPost(postId: String):Post{
        return _posts.value.find { it.id == postId }!!
    }
    fun loadPosts() {
        _uiState.update {
            it.copy(isLoading = true, error = null)
        }
        _uiState.update {
            it.copy(
                posts = getPosts(),
                isLoading = false,
                currentPostIndex = 0
            )
        }
    }
    fun onShareActionClicked(item: ShareItem) {
        _shareSheetMode.value = when (item.id) {
            "report" -> ShareSheetMode.Report
            "not_interested" -> ShareSheetMode.NotInterested
            "speed" -> ShareSheetMode.Speed
//            "copy_link" -> ShareSheetMode.CopyLink
            else -> ShareSheetMode.Default

        }
    }

    fun openShareSheet() {
        _showShareSheet.value = true
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

    fun selectedFriendShareCount(): (Int) {
        return _selectedFriendShare.value.size
    }


}
