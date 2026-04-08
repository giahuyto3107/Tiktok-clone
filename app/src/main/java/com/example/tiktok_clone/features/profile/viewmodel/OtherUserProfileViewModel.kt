package com.example.tiktok_clone.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.features.social.data.FollowCountResponse
import com.example.tiktok_clone.features.social.data.SocialRepository
import com.example.tiktok_clone.features.user.data.FirebaseUserResponse
import com.example.tiktok_clone.features.user.data.UserApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OtherUserProfileViewModel(
    private val userApiService: UserApiService,
    private val socialRepository: SocialRepository
) : ViewModel() {

    private val _targetUser = MutableStateFlow<FirebaseUserResponse?>(null)
    val targetUser: StateFlow<FirebaseUserResponse?> = _targetUser.asStateFlow()

    private val _followCounts = MutableStateFlow<FollowCountResponse?>(null)
    val followCounts = _followCounts.asStateFlow()
    private val _guestFollowers = MutableStateFlow<Set<String>>(emptySet())
    val guestFollowers = _guestFollowers.asStateFlow()
    private val _guestFollowing = MutableStateFlow<Set<String>>(emptySet())
    val guestFollowing = _guestFollowing.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = userApiService.getUser(userId)
                val counts = socialRepository.getFollowCounts(userId)
                val followers = socialRepository.getFollowers(userId).map { it.uid }.toSet()
                val following = socialRepository.getFollowing(userId).map { it.uid }.toSet()
                
                _targetUser.value = user
                _followCounts.value = counts
                _guestFollowers.value = followers
                _guestFollowing.value = following
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshGuestFollow(userId: String) {
        viewModelScope.launch {
            try {
                _guestFollowers.value = socialRepository.getFollowers(userId).map { it.uid }.toSet()
                _guestFollowing.value = socialRepository.getFollowing(userId).map { it.uid }.toSet()
            } catch (_: Exception) {
            }
        }
    }
}
